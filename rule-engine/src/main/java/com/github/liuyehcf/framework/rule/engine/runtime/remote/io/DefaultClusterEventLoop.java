package com.github.liuyehcf.framework.rule.engine.runtime.remote.io;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.promise.AbstractPromise;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.promise.PromiseListener;
import com.github.liuyehcf.framework.rule.engine.runtime.config.RuleProperties;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.*;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.handler.*;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message.GreetMessage;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message.JoiningMessage;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message.SpreadNodeStatusMessage;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class DefaultClusterEventLoop implements ClusterEventLoop {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    private final Random random = new Random();
    private final ExecutorService joiningExecutor = Executors.newFixedThreadPool(1);
    private final EventLoopGroup clientWorkGroup = new NioEventLoopGroup();
    private final RuleEngine engine;
    private final RuleProperties properties;
    private final ClusterTopology topology = new DefaultClusterTopology();
    private final List<ClusterNodeConfig> otherSeeds = Lists.newArrayList();
    private final Map<String, ClusterChannel> channels = Maps.newConcurrentMap();
    private final Promise<Void> closePromise = new AbstractPromise<Void>() {
    };
    private ChannelFuture channelFuture;

    public DefaultClusterEventLoop(RuleEngine engine) {
        Assert.assertNotNull(engine, "engine");
        this.engine = engine;
        this.properties = engine.getProperties();
        init();
    }

    private void init() {
        TopologyKeepAlive.register(this, properties.getTopologyKeepAliveInterval());

        if (properties.getClusterConfig() == null) {
            LOGGER.error("cluster mode missing cluster config, downgrade to singleton mode");
            return;
        }

        if (CollectionUtils.isEmpty(properties.getClusterConfig().getSeeds())) {
            LOGGER.error("cluster mode missing seed node config, downgrade to singleton mode");
            return;
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        addHandlers(ch, ChannelMode.server, null, null);
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_RCVBUF, NettyConstant.MAX_RCVBUF)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_REUSEADDR, true);

        try {
            channelFuture = bootstrap.bind(properties.getPort()).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        joiningExecutor.execute(this::heartbeatLoop);
    }

    @Override
    public final RuleEngine getEngine() {
        return engine;
    }

    @Override
    public ClusterTopology getTopology() {
        return topology;
    }

    @Override
    public final boolean addChannelIfAbsent(ClusterChannel clusterChannel) {
        if (channels.putIfAbsent(clusterChannel.getPeerIdentifier(), clusterChannel) == null) {
            clusterChannel.getChannel().closeFuture().addListener((ChannelFuture future) ->
                    channels.remove(clusterChannel.getPeerIdentifier())
            );
            return true;
        } else {
            clusterChannel.closeAsync();
            return false;
        }
    }

    @Override
    public final boolean hasChannel(String peerIdentifier) {
        return channels.containsKey(peerIdentifier);
    }

    @Override
    public final ClusterChannel getChannel(String peerIdentifier) {
        return channels.get(peerIdentifier);
    }

    @Override
    public final Collection<ClusterChannel> getChannels() {
        return channels.values();
    }

    @Override
    public final void createChannel(ClusterNodeIdentifier peerIdentifier) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientWorkGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ClusterChannel clusterChannel = new DefaultClusterChannel(peerIdentifier.getIdentifier(), ch, ChannelMode.client);
                        addHandlers(ch, ChannelMode.client, peerIdentifier, clusterChannel);
                    }
                })
                .option(ChannelOption.SO_RCVBUF, NettyConstant.MAX_RCVBUF)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true);

        Channel channel;
        try {
            channel = bootstrap.connect(peerIdentifier.getHost(), peerIdentifier.getPort()).sync().channel();
        } catch (Throwable e) {
            ClusterNode node = topology.getNode(peerIdentifier.getIdentifier());
            if (node != null) {
                // todo leaving hook
                updateAndSpreadNodeStatus(node.clone(ClusterNodeStatus.leaving));

                // todo inactive hook
                updateAndSpreadNodeStatus(node.clone(ClusterNodeStatus.inactive));
            }

            if (e instanceof InterruptedException) {
                throw (InterruptedException) e;
            } else {
                throw new RuntimeException(e);
            }
        }

        ClusterChannel clusterChannel = new DefaultClusterChannel(peerIdentifier.getIdentifier(), channel, ChannelMode.client);
        if (addChannelIfAbsent(clusterChannel)) {
            clusterChannel.write(new GreetMessage(properties.getHost(), properties.getPort()));

            HeartBeat.register(this, clusterChannel, properties.getHeartbeatInterval());
        }
    }

    @Override
    public final void updateAndSpreadNodeStatus(ClusterNode node) {
        LOGGER.info("Node is {}: {}", node.getStatus(), node);

        topology.putNode(node);

        for (ClusterChannel otherClusterChannel : getChannels()) {
            otherClusterChannel.write(new SpreadNodeStatusMessage(node));
        }
    }

    @Override
    public final Promise<Void> addCloseListener(PromiseListener<Void> listener) {
        return closePromise.addListener(listener);
    }

    @Override
    public final void shutdown() {
        closePromise.trySuccess(null);
        joiningExecutor.shutdownNow();
        if (channelFuture != null) {
            try {
                channelFuture.channel().close().sync();
            } catch (Throwable e) {
                LOGGER.error("server socket close catch unknown error, error={}", e.getMessage(), e);
            }
        }
        for (ClusterChannel clusterChannel : getChannels()) {
            try {
                clusterChannel.closeSync();
            } catch (Throwable e) {
                LOGGER.error("socket close catch unknown error, error={}", e.getMessage(), e);
            }
        }
        clientWorkGroup.shutdownGracefully();
    }

    private void heartbeatLoop() {
        if (isTheOnlySeed()) {
            DefaultClusterNode onlySeed = new DefaultClusterNode(properties.getHost(),
                    properties.getPort(),
                    properties.isSeed(),
                    ClusterNodeStatus.active);
            topology.putNode(onlySeed);
            LOGGER.info("current node is the only seed in the cluster, so it doesn't need to keep heartbeat with other nodes");
            return;
        }

        initOtherSeedHosts();

        while (!Thread.currentThread().isInterrupted()) {
            try {
                ClusterNodeConfig seed = otherSeeds.get(random.nextInt(otherSeeds.size()));
                createChannel(seed);

                ClusterChannel clusterChannel = getChannel(seed.getIdentifier());

                clusterChannel.write(
                        new JoiningMessage(
                                new DefaultClusterNode(
                                        properties.getHost(),
                                        properties.getPort(),
                                        properties.isSeed(),
                                        ClusterNodeStatus.init)
                        )
                );

                clusterChannel.getChannel().closeFuture().sync();
            } catch (InterruptedException e) {
                LOGGER.warn("heartbeat loop interrupted");
                Thread.currentThread().interrupt();
            } catch (Throwable e) {
                LOGGER.warn("heartbeat loop catch unknown error, errorMsg={}", e.getMessage(), e);
            } finally {
                try {
                    TimeUnit.SECONDS.sleep(properties.getHeartbeatRetryInterval());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        LOGGER.warn("heartbeat loop finished");
    }

    private boolean isTheOnlySeed() {
        return properties.getClusterConfig().getSeeds().size() == 1 &&
                properties.isSeed();
    }

    private void initOtherSeedHosts() {
        String host = properties.getHost();
        int port = properties.getPort();
        List<ClusterNodeConfig> seeds = properties.getClusterConfig().getSeeds();

        for (ClusterNodeConfig seed : seeds) {
            if (!Objects.equals(host, seed.getHost())
                    || !Objects.equals(port, seed.getPort())) {
                otherSeeds.add(seed);
            }
        }
    }

    private void addHandlers(SocketChannel ch, ChannelMode channelMode, ClusterNodeIdentifier peerIdentifier, ClusterChannel clusterChannel) {
        ChannelPipeline pipeline = ch.pipeline();
        addLast(pipeline, new IdleStateHandler(0, 0, properties.getIdleTime(), TimeUnit.SECONDS));
        addLast(pipeline, new FrameHandler());
        addLast(pipeline, new FrameAggregatorHandler());
        addLast(pipeline, new FrameChunkedWriteHandler());
        addLast(pipeline, new MessageHandler(this, channelMode, peerIdentifier, clusterChannel));
        addLast(pipeline, new SerializeWriteHandler(properties.getSerializeType()));
    }

    private void addLast(ChannelPipeline pipeline, ChannelHandler handler) {
        Assert.assertNotNull(handler, "handler");
        pipeline.addLast(handler.getClass().getSimpleName(), handler);
    }
}
