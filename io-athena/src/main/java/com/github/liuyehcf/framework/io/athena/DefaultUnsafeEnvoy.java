package com.github.liuyehcf.framework.io.athena;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.common.tools.number.IDGenerator;
import com.github.liuyehcf.framework.common.tools.number.NumberUtils;
import com.github.liuyehcf.framework.common.tools.number.SnowFlakeIDGenerator;
import com.github.liuyehcf.framework.common.tools.promise.AbstractPromise;
import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.io.athena.event.*;
import com.github.liuyehcf.framework.io.athena.processor.*;
import com.github.liuyehcf.framework.io.athena.protocol.handler.AthenaFrameHandler;
import com.github.liuyehcf.framework.io.athena.protocol.handler.SerializeHandler;
import com.github.liuyehcf.framework.io.athena.util.AddressUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author hechenfeng
 * @date 2020/2/6
 */
class DefaultUnsafeEnvoy implements UnsafeEnvoy {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUnsafeEnvoy.class);

    private final List<Processor<?>> processors = Lists.newArrayList();

    /**
     * address -> channel
     * address'port is not the actual channel port, but the member's listener port
     */
    private final Map<Address, ClusterChannel> channels = Maps.newConcurrentMap();

    /**
     * address -> channel
     * channels which currently in greeting phrase
     */
    private final Map<Address, ClusterChannel> greetingChannels = Maps.newConcurrentMap();

    private final AthenaConfig config;
    private final Member self;
    private final Cluster cluster;
    private final IDGenerator idGenerator;

    private final LeaderStatusStatistics leaderStatusStatistics;
    private final ProposalStatistics proposalStatistics;
    private final Map<Address, Long> latestLeaderKeepAliveTimestamps = Maps.newConcurrentMap();

    private ExecutorService systemEventLoop;
    private ExecutorService customEventLoop;
    private BlockingQueue<EventContext<?>> systemEventQueue;
    private BlockingQueue<EventContext<?>> customEventQueue;
    private EventLoopGroup serverBossGroup;
    private EventLoopGroup serverWorkerGroup;
    private EventLoopGroup clientWorkGroup;

    private ChannelFuture channelFuture;
    private Receiver receiver;
    private volatile boolean isRunning = false;
    private volatile long latestLeaderKeepAliveAckTimestamp;

    DefaultUnsafeEnvoy(AthenaConfig config) {
        this.config = config;

        checkConfig();
        self = new Member(config.getName(),
                config.getHost(),
                config.getPort(),
                isSelfSeed());

        cluster = new Cluster(self);
        idGenerator = new SnowFlakeIDGenerator(config.getWorkerId());

        leaderStatusStatistics = new LeaderStatusStatistics(config);
        proposalStatistics = new ProposalStatistics(config);
    }

    @Override
    public final synchronized void start() {
        if (isRunning) {
            return;
        }
        try {
            isRunning = true;

            systemEventLoop = new ThreadPoolExecutor(1, 1, 5L, TimeUnit.SECONDS,
                    new SynchronousQueue<>(),
                    new ThreadFactoryBuilder().setNameFormat("athenaSystemEventLoop(" + self + ")-t-%d").build(),
                    new ThreadPoolExecutor.CallerRunsPolicy());
            customEventLoop = new ThreadPoolExecutor(config.getThreadNum(), config.getThreadNum(), 5L, TimeUnit.SECONDS,
                    new SynchronousQueue<>(),
                    new ThreadFactoryBuilder().setNameFormat("athenaCustomEventLoop(" + self + ")-t-%d").build(),
                    new ThreadPoolExecutor.CallerRunsPolicy());
            systemEventQueue = new ArrayBlockingQueue<>(1024);
            customEventQueue = new ArrayBlockingQueue<>(1024);

            serverBossGroup = new NioEventLoopGroup(1);
            serverWorkerGroup = new NioEventLoopGroup();
            clientWorkGroup = new NioEventLoopGroup();

            registerProcessors();
            listen();
            startEventLoop();
            ClusterProbe.register(this);
        } catch (Throwable e) {
            LOGGER.error("start envoy catch exception, errorMsg={}", e.getMessage(), e);
        }
    }

    @Override
    public final synchronized void stop() {
        if (!isRunning) {
            return;
        }
        try {
            isRunning = false;

            if (channelFuture != null) {
                channelFuture.channel().close();
                channelFuture = null;
            }
            channels.values().forEach(ClusterChannel::close);
            channels.clear();

            serverBossGroup.shutdownGracefully();
            serverBossGroup = null;

            serverWorkerGroup.shutdownGracefully();
            serverWorkerGroup = null;

            clientWorkGroup.shutdownGracefully();
            clientWorkGroup = null;

            systemEventLoop.shutdownNow();
            customEventLoop.shutdownNow();
            systemEventQueue = null;
            customEventQueue = null;

            cluster.atomicOperate(cluster::reset);

            ClusterProbe.unRegister(this);
        } catch (Throwable e) {
            LOGGER.error("stop envoy catch exception, errorMsg={}", e.getMessage(), e);
        }
    }

    @Override
    public final AthenaConfig getConfig() {
        return config;
    }

    @Override
    public final Cluster getCluster() {
        return cluster;
    }

    @Override
    public final <T> boolean whisper(Address address, T event) {
        Assert.assertNotNull(address, "address");

        Member member = cluster.getMember(address);

        if (member != null) {
            if (member.getStatus().isActive()) {
                if (Objects.equals(self, member)) {
                    offerEvent(new EventContext<>(self, event));
                    return true;
                }

                ClusterChannel channel = getOrCreateChannel(member);

                if (channel != null) {
                    channel.write(event);
                    return true;
                } else {
                    LOGGER.debug("[{}] cannot whisper with [{}] due to not connectable ", self, member);
                    return false;
                }
            } else {
                LOGGER.debug("[{}] cannot whisper with [{}] due to not in active status, status=[{}]", self, member, member.getStatus());
                return false;
            }
        } else {
            LOGGER.debug("[{}] cannot whisper with [{}] due to not in the cluster", self, address);
            return false;
        }
    }

    @Override
    public final <T> void roar(T event, boolean includingSelf) {
        for (Member member : cluster.getMembers()) {
            if (includingSelf || !Objects.equals(self, member)) {
                try {
                    whisper(member, event);
                } catch (Throwable e) {
                    LOGGER.error("roar catch exception, member={}; errorMsg={}", member, e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public final void registerReceiver(Receiver receiver) {
        Assert.assertNotNull(receiver, "receiver");
        this.receiver = receiver;
    }

    @Override
    public long nextId() {
        return idGenerator.nextId();
    }

    @Override
    public final <T> void enqueue(T event) {
        Assert.assertNotNull(event, "event");

        try {
            offerEvent(new EventContext<>(self, event));
        } catch (Throwable e) {
            LOGGER.error("enqueue event catch exception, errorMsg={}", e.getMessage(), e);
        }
    }

    @Override
    public final <T> boolean whisperIgnoreStatus(Address address, T event, boolean connectIfNotExist) {
        if (Objects.equals(self, address)) {
            offerEvent(new EventContext<>(self, event));
            return true;
        }

        ClusterChannel channel;

        if (connectIfNotExist) {
            channel = getOrCreateChannel(address);
        } else {
            channel = getChannel(address);
        }

        if (channel != null) {
            channel.write(event);
            return true;
        } else {
            LOGGER.debug("[{}] cannot whisper with [{}] due to not connected", self, address);
            return false;
        }
    }

    @Override
    public final StatisticsResult<Void> statisticsLeaderStatusQuery(LeaderStatusQueryResponse response) {
        return leaderStatusStatistics.statisticsLeaderStatusQuery(response);
    }

    @Override
    public final Vote voteForPreProposal(Proposal proposal) {
        return proposalStatistics.voteForPreProposal(proposal);
    }

    @Override
    public final Vote voteForFormalProposal(Proposal proposal) {
        return proposalStatistics.voteForFormalProposal(proposal);
    }

    @Override
    public final StatisticsResult<Member> statisticsPreProposal(Vote vote) {
        return proposalStatistics.statisticsPreProposal(vote);
    }

    @Override
    public final StatisticsResult<Member> statisticsFormalProposal(Vote vote) {
        return proposalStatistics.statisticsFormalProposal(vote);
    }

    @Override
    public final void acceptLeader(Member member, long version) {
        cluster.atomicOperate(() -> {
            cluster.addMember(member);
            cluster.updateMemberRole(member, MemberRole.leader);
            cluster.updateMemberStatus(member, MemberStatus.active);
            cluster.setLeader(member);
            cluster.setVersion(version);
            leaderStatusStatistics.reset();
            proposalStatistics.reset();
        });
    }

    @Override
    public final void receiveLeaderKeepAlive(Member member) {
        latestLeaderKeepAliveTimestamps.put(member, System.currentTimeMillis());
    }

    @Override
    public final long getLatestLeaderKeepAliveTimestamp(Member member) {
        Long timestamp = latestLeaderKeepAliveTimestamps.get(member);
        if (timestamp == null) {
            return 0;
        }
        return timestamp;
    }

    @Override
    public final void receiveLeaderKeepAliveAck() {
        latestLeaderKeepAliveAckTimestamp = System.currentTimeMillis();
    }

    @Override
    public final long getLatestLeaderKeepAliveAckTimestamp() {
        return latestLeaderKeepAliveAckTimestamp;
    }

    private void checkConfig() {
        Assert.assertTrue(config.getTotalNum() > 0, "totalNum must be positive");

        if (AddressUtils.isValidIp(config.getHost())) {
            Assert.assertTrue(AddressUtils.isLocalIp(config.getHost()), "ip matches non of the local ips");
        } else {
            String ip = AddressUtils.domainToIp(config.getHost());
            Assert.assertNotNull(ip, "unknown host");
            Assert.assertTrue(AddressUtils.isLocalIp(ip), "ip matches non of the local ips");
        }

        Assert.assertTrue(1 <= config.getPort() && config.getPort() <= 65535, "port is invalid");

        Assert.assertNotBlank(config.getName(), "name is blank");

        Assert.assertNotEmpty(config.getSeeds(), "seeds is empty");
        for (Address seed : config.getSeeds()) {
            Assert.assertNotNull(seed, "seed");
            Assert.assertNotBlank(seed.getHost(), "seed's host is blank");
            Assert.assertTrue(1 <= seed.getPort() && seed.getPort() <= 65535, "seed's port is invalid");
        }

        Assert.assertTrue(config.getProbeInterval() > 0, "probeInterval must be positive");
        Assert.assertTrue(config.getHeartbeatInterval() > 0, "heartbeatInterval must be positive");
        Assert.assertTrue(config.getHeartbeatTimeout() > 0, "heartbeatTimeout must be positive");
        Assert.assertTrue(config.getTtlTimeout() > 0, "ttlTimeout must be positive");
        Assert.assertTrue(config.getRetryInterval() > 0, "retryInterval must be positive");

        Assert.assertTrue(config.getThreadNum() > 0, "threadNum must be positive");
    }

    private boolean isSelfSeed() {
        for (Address address : config.getSeeds()) {
            if (address.equals(Address.of(config.getHost(), config.getPort()))) {
                return true;
            }
        }

        return false;
    }

    private void registerProcessors() {
        processors.add(new LeaderQueryProcessor(this));
        processors.add(new LeaderQueryResponseProcessor(this));

        processors.add(new JoinClusterRequestProcessor(this));
        processors.add(new JoinClusterResponseProcessor(this));

        processors.add(new JoiningMemberProcessor(this));
        processors.add(new MemberStatusUpdateProcessor(this));

        processors.add(new ClusterAlignmentProcessor(this));

        processors.add(new LeaderKeepAliveProcessor(this));
        processors.add(new LeaderKeepAliveAckProcessor(this));

        processors.add(new SeedKeepAliveProcessor(this));
        processors.add(new SeedKeepAliveAckProcessor(this));

        processors.add(new LeaderRelieveProcessor(this));
        processors.add(new ReJoinClusterProcessor(this));

        processors.add(new LeaderStatusQueryProcessor(this));
        processors.add(new LeaderStatusQueryResponseProcessor(this));

        processors.add(new ProposalProcessor(this));
        processors.add(new VoteProcessor(this));
        processors.add(new LeaderElectedProcessor(this));
    }

    private void listen() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(serverBossGroup, serverWorkerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(createChannelInitializer())
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_RCVBUF, 640 * 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_REUSEADDR, true);

        try {
            channelFuture = bootstrap.bind(config.getPort()).sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AthenaException(e);
        }
    }

    private void startEventLoop() {
        systemEventLoop.execute(eventLoop(this::pollSystemEvent));

        for (int i = 0; i < config.getThreadNum(); i++) {
            customEventLoop.execute(eventLoop(this::pollCustomEvent));
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Runnable eventLoop(Callable<EventContext<?>> eventPoller) {
        return () -> {
            while (isRunning) {
                try {
                    EventContext<?> context = eventPoller.call();

                    boolean isSystemEvent = false;
                    for (Processor processor : processors) {
                        if (processor.match(context.getEvent())) {
                            isSystemEvent = true;
                            try {
                                processor.process(context);
                            } catch (Throwable e) {
                                LOGGER.error("processor catch exception, errorMsg={}", e.getMessage(), e);
                            }
                            break;
                        }
                    }

                    if (!isSystemEvent && receiver != null) {
                        receiver.receive(context.getEvent());
                    }
                } catch (Throwable e) {
                    Throwable t = e;
                    boolean isInterruptedException = false;
                    while (t != null) {
                        if (t instanceof InterruptedException) {
                            isInterruptedException = true;
                            break;
                        }

                        t = t.getCause();
                    }

                    if (isInterruptedException) {
                        break;
                    }
                    LOGGER.error("event loop catch exception, errorMsg={}", e.getMessage(), e);
                }
            }
            LOGGER.info("event loop '{}' finished", Thread.currentThread().getName());
        };
    }

    private ChannelInitializer<SocketChannel> createChannelInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new IdleStateHandler(config.getHeartbeatTimeout(), 0, 0, TimeUnit.SECONDS));
                pipeline.addLast(new AthenaFrameHandler());
                pipeline.addLast(new SerializeHandler());
                pipeline.addLast(DefaultUnsafeEnvoy.this.new EnvoyHandler());
            }
        };
    }

    private ClusterChannel getChannel(Address address) {
        return channels.get(address);
    }

    private ClusterChannel getOrCreateChannel(Address address) {
        ClusterChannel channel = getChannel(address);

        if (channel != null) {
            return channel;
        }

        synchronized (this) {
            channel = createChannel(address);
            if (channel != null) {
                greetingChannels.put(address, channel);
                try {
                    channel.write(new Greet(self, true));
                    LOGGER.debug("[{}] send 'Greet' to [{}], isActive={}", self, address, true);
                    // wait for passive-greet
                    if (channel.activePromise.await(3000, TimeUnit.MILLISECONDS)) {
                        if (addChannelIfAbsent(address, channel)) {
                            return channel;
                        } else {
                            LOGGER.debug("addChannel conflict, now close redundant channel");
                            channel.close();

                            return getChannel(address);
                        }
                    }
                } finally {
                    greetingChannels.remove(address);
                }
            }
        }

        return getChannel(address);
    }

    private boolean addChannelIfAbsent(Address address, ClusterChannel channel) {
        ClusterChannel existChannel;

        while ((existChannel = channels.putIfAbsent(address, channel)) != null) {
            Address existActiveAddress;
            if (existChannel.isActive) {
                existActiveAddress = self;
            } else {
                existActiveAddress = address;
            }
            Address activeAddress;
            if (channel.isActive) {
                activeAddress = self;
            } else {
                activeAddress = address;
            }

            int r = activeAddress.compareTo(existActiveAddress);
            if (r < 0) {
                existChannel.close();
            } else {
                return false;
            }
        }

        channel.addCloseListener((f) -> channels.remove(address));
        return true;
    }

    private ClusterChannel createChannel(Address address) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientWorkGroup)
                .channel(NioSocketChannel.class)
                .handler(createChannelInitializer())
                .option(ChannelOption.SO_RCVBUF, 640 * 1024)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true);

        try {
            ChannelFuture connect = bootstrap.connect(address.getHost(), address.getPort());
            if (connect.await(3000, TimeUnit.MILLISECONDS)) {
                if (connect.isSuccess()) {
                    return new ClusterChannel(true, connect.channel());
                }
                connect.channel().close();
                throw new IOException("connect rejected");
            }
            throw new IOException("connect timeout");
        } catch (Throwable e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            LOGGER.debug("failed to create channel, errorMsg={}", e.getMessage());
            return null;
        }
    }

    private void offerEvent(EventContext<?> context) {
        try {
            if (context.getEvent() instanceof SystemEvent) {
                systemEventQueue.put(context);
            } else {
                customEventQueue.put(context);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AthenaException(e);
        }
    }

    private EventContext<?> pollSystemEvent() {
        try {
            return systemEventQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AthenaException(e);
        }
    }

    private EventContext<?> pollCustomEvent() {
        try {
            return customEventQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AthenaException(e);
        }
    }

    private static final class ClusterChannel {

        /**
         * true means the one who initialized the connection
         */
        private final boolean isActive;
        private final Channel channel;
        private final Promise<Void> activePromise = new AbstractPromise<Void>() {
        };

        private ClusterChannel(boolean isActive, Channel channel) {
            this.isActive = isActive;
            this.channel = channel;
        }

        private ChannelFuture write(Object event) {
            return channel.writeAndFlush(event);
        }

        private void addCloseListener(GenericFutureListener<? extends Future<? super Void>> listener) {
            channel.closeFuture().addListener(listener);
        }

        private void close() {
            try {
                channel.close().sync();
            } catch (Throwable e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                LOGGER.error("close channel catch exception, errorMsg={}", e.getMessage(), e);
            }
        }
    }

    private static abstract class AbstractStatistics {

        protected final AthenaConfig config;
        protected final int majority;
        protected final int subMajority;

        protected AbstractStatistics(AthenaConfig config) {
            Assert.assertNotNull(config, "config");
            this.config = config;
            this.majority = NumberUtils.majorityOf(config.getTotalNum());
            this.subMajority = NumberUtils.subMajorityOf(config.getTotalNum());
        }
    }

    private static final class LeaderStatusStatistics extends AbstractStatistics {

        private long id = Long.MIN_VALUE;
        private long unhealthyNum;
        private long healthyNum;

        private LeaderStatusStatistics(AthenaConfig config) {
            super(config);
        }

        private synchronized void reset() {
            id = Long.MIN_VALUE;
            unhealthyNum = 0;
            healthyNum = 0;
        }

        private synchronized StatisticsResult<Void> statisticsLeaderStatusQuery(LeaderStatusQueryResponse response) {
            if (response.getId() < id) {
                return StatisticsResult.rejectedOf("id out-of-date");
            }

            if (response.getId() > id) {
                id = response.getId();
                unhealthyNum = 0;
                healthyNum = 0;
            }

            if (response.isHealthy()) {
                healthyNum++;
            } else {
                unhealthyNum++;
            }

            if (unhealthyNum == majority) {
                return StatisticsResult.acceptedOf(null);
            } else if (unhealthyNum > majority) {
                return StatisticsResult.alreadyAccepted();
            } else if (healthyNum == subMajority) {
                return StatisticsResult.rejectedOf("reach sub-majority");
            } else if (healthyNum > subMajority) {
                return StatisticsResult.alreadyRejected();
            } else {
                return StatisticsResult.undetermined();
            }
        }
    }

    private final class ProposalStatistics extends AbstractStatistics {

        private final List<Vote> acceptedPreVotes = Lists.newArrayList();
        private final List<Vote> rejectedPreVotes = Lists.newArrayList();
        private final List<Vote> acceptedFormalVotes = Lists.newArrayList();
        private final List<Vote> rejectedFormalVotes = Lists.newArrayList();
        private long latestProcessTimestamp;
        private long maxProposalId = Long.MIN_VALUE;
        private Proposal lastAcceptedFormalProposal;

        private ProposalStatistics(AthenaConfig config) {
            super(config);
        }

        private synchronized void reset() {
            latestProcessTimestamp = System.currentTimeMillis();
            maxProposalId = Long.MIN_VALUE;
            lastAcceptedFormalProposal = null;
            acceptedPreVotes.clear();
            rejectedPreVotes.clear();
            acceptedFormalVotes.clear();
            rejectedFormalVotes.clear();
        }

        private synchronized Vote voteForPreProposal(Proposal proposal) {
            // avoid contaminating the variable lastAcceptedFormalProposal
            // and latestProcessTimestamp with the out-of-date proposal
            if (proposal.getVersion() <= cluster.getVersion()) {
                return new Vote(
                        proposal.getId(),
                        proposal.getStage(),
                        false,
                        proposal.getProposer(),
                        proposal.getVersion(),
                        null
                );
            }

            updateLatestProcessTimestamp();

            if (proposal.getId() > maxProposalId) {
                maxProposalId = proposal.getId();
                Member lastAcceptedCandidate = null;
                if (lastAcceptedFormalProposal != null) {
                    lastAcceptedCandidate = lastAcceptedFormalProposal.getCandidate();
                }
                return new Vote(
                        proposal.getId(),
                        proposal.getStage(),
                        true,
                        proposal.getProposer(),
                        proposal.getVersion(),
                        lastAcceptedCandidate
                );
            }

            return new Vote(
                    proposal.getId(),
                    proposal.getStage(),
                    false,
                    proposal.getProposer(),
                    proposal.getVersion(),
                    null
            );
        }

        private synchronized Vote voteForFormalProposal(Proposal proposal) {
            // avoid contaminating the variable lastAcceptedFormalProposal
            // and latestProcessTimestamp with the out-of-date proposal
            if (proposal.getVersion() <= cluster.getVersion()) {
                return new Vote(
                        proposal.getId(),
                        proposal.getStage(),
                        false,
                        proposal.getProposer(),
                        proposal.getVersion(),
                        null
                );
            }

            updateLatestProcessTimestamp();

            if (proposal.getId() >= maxProposalId) {
                maxProposalId = proposal.getId();
                lastAcceptedFormalProposal = proposal;
                return new Vote(
                        proposal.getId(),
                        proposal.getStage(),
                        true,
                        proposal.getProposer(),
                        proposal.getVersion(),
                        proposal.getCandidate()
                );
            }

            return new Vote(
                    proposal.getId(),
                    proposal.getStage(),
                    false,
                    proposal.getProposer(),
                    proposal.getVersion(),
                    null
            );
        }

        private synchronized StatisticsResult<Member> statisticsPreProposal(Vote vote) {
            // avoid contaminating the variable lastAcceptedFormalProposal
            // and latestProcessTimestamp with the out-of-date proposal
            if (vote.getVersion() <= cluster.getVersion()) {
                return StatisticsResult.rejectedOf(
                        String.format("pre-vote's version out-of-date, localVersion=%s; voteVersion=%s", cluster.getVersion(), vote.getVersion()));
            }

            updateLatestProcessTimestamp();

            if (vote.isAccept()) {
                acceptedPreVotes.add(vote);
            } else {
                rejectedPreVotes.add(vote);
            }

            if (acceptedPreVotes.size() == majority) {
                long maxId = Long.MIN_VALUE;
                Vote selectedVote = null;
                for (Vote preVote : acceptedPreVotes) {
                    if (preVote.getId() > maxId) {
                        selectedVote = preVote;
                    }
                }

                if (selectedVote != null) {
                    if (selectedVote.getCandidate() == null) {
                        return StatisticsResult.acceptedOf(vote.getProposer());
                    } else {
                        return StatisticsResult.acceptedOf(selectedVote.getCandidate());
                    }
                } else {
                    return StatisticsResult.rejectedOf("no candidate");
                }
            } else if (acceptedPreVotes.size() > majority) {
                return StatisticsResult.alreadyAccepted();
            } else if (rejectedPreVotes.size() == subMajority) {
                return StatisticsResult.rejectedOf("reach sub-majority");
            } else if (rejectedPreVotes.size() > subMajority) {
                return StatisticsResult.alreadyRejected();
            } else {
                return StatisticsResult.undetermined();
            }
        }

        private synchronized StatisticsResult<Member> statisticsFormalProposal(Vote vote) {
            // avoid contaminating the variable lastAcceptedFormalProposal
            // and latestProcessTimestamp with the out-of-date proposal
            if (vote.getVersion() <= cluster.getVersion()) {
                return StatisticsResult.rejectedOf(
                        String.format("formal-vote's version out-of-date, localVersion=%s; voteVersion=%s", cluster.getVersion(), vote.getVersion()));
            }

            updateLatestProcessTimestamp();

            if (vote.isAccept()) {
                acceptedFormalVotes.add(vote);
            } else {
                rejectedFormalVotes.add(vote);
            }

            // accept only once
            if (acceptedFormalVotes.size() == majority) {
                return StatisticsResult.acceptedOf(vote.getCandidate());
            } else if (acceptedFormalVotes.size() > majority) {
                return StatisticsResult.alreadyAccepted();
            } else if (rejectedFormalVotes.size() == subMajority) {
                return StatisticsResult.rejectedOf("reach sub-majority");
            } else if (rejectedFormalVotes.size() > subMajority) {
                return StatisticsResult.alreadyRejected();
            } else {
                return StatisticsResult.undetermined();
            }
        }

        private void updateLatestProcessTimestamp() {
            long currentTimestamp = System.currentTimeMillis();
            // assuming that the election will be completed in three times TTL time
            if (currentTimestamp - latestProcessTimestamp > config.getTtlTimeout() * NumberUtils._1K * 3) {
                reset();
            } else {
                latestProcessTimestamp = currentTimestamp;
            }
        }
    }

    private final class EnvoyHandler extends SimpleChannelInboundHandler<Object> {

        private Identifier peer;

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
            LOGGER.debug("catch event, channel={}; event={}", ctx.channel(), evt);
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent e = (IdleStateEvent) evt;
                if (e.state() == IdleState.READER_IDLE) {
                    LOGGER.debug("channel READER_IDLE, channel={}", ctx.channel());
                    ctx.channel().close();
                } else if (e.state() == IdleState.WRITER_IDLE) {
                    LOGGER.debug("channel WRITER_IDLE, channel={}", ctx.channel());
                    ctx.channel().close();
                } else if (e.state() == IdleState.ALL_IDLE) {
                    LOGGER.debug("channel ALL_IDLE, channel={}", ctx.channel());
                    ctx.channel().close();
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            LOGGER.debug("channel catch exception, channel={}; errorMsg={}", ctx.channel(), cause.getMessage(), cause);
        }

        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        protected void channelRead0(ChannelHandlerContext ctx, Object event) {
            if (event instanceof Greet) {
                Greet greet = (Greet) event;
                peer = greet.getIdentifier();
                LOGGER.debug("[{}] receive 'Greet' from [{}], isActive={}", self, peer, greet.isActive());

                // this side is passive, so cache the channel
                if (greet.isActive()) {
                    ClusterChannel channel = new ClusterChannel(false, ctx.channel());
                    if (!addChannelIfAbsent(peer, channel)) {
                        LOGGER.debug("[{}] send 'Greet' to [{}] and close the channel due to addChannel conflict, isActive={} ", self, peer, false);
                        channel.write(new Greet(self, false))
                                .addListener(ChannelFutureListener.CLOSE);
                    } else {
                        LOGGER.debug("[{}] send 'Greet' to [{}], isActive={}", self, peer, false);
                        channel.write(new Greet(self, false));
                    }
                } else {
                    ClusterChannel channel = greetingChannels.get(peer);
                    if (channel != null) {
                        channel.activePromise.trySuccess(null);
                    }
                }
            } else {
                offerEvent(new EventContext(peer, event));
            }
        }
    }
}
