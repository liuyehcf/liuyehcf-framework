package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.handler;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.*;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.ChannelMode;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.ClusterChannel;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.ClusterEventLoop;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.DefaultClusterChannel;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message.*;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.protocol.Package;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.protocol.SerializeType;
import com.github.liuyehcf.framework.rule.engine.util.CloneUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class MessageHandler extends SimpleChannelInboundHandler<Package> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    private final ClusterEventLoop clusterEventLoop;
    private final ChannelMode channelMode;
    private ClusterNodeIdentifier peerIdentifier;
    private ClusterChannel clusterChannel;

    public MessageHandler(ClusterEventLoop clusterEventLoop, ChannelMode channelMode, ClusterNodeIdentifier peerIdentifier, ClusterChannel clusterChannel) {
        Assert.assertNotNull(clusterEventLoop, "clusterEventLoop");
        Assert.assertNotNull(channelMode, "channelMode");
        if (channelMode.isClientMode()) {
            Assert.assertNotNull(peerIdentifier, "peerIdentifier");
            Assert.assertNotNull(clusterChannel, "clusterChannel");
        }
        this.clusterEventLoop = clusterEventLoop;
        this.channelMode = channelMode;
        this.peerIdentifier = peerIdentifier;
        this.clusterChannel = clusterChannel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Package msg) {
        try {
            MessageType messageType = MessageType.typeOf(msg.getType());

            switch (messageType) {
                case GREET:
                    onGreet(ctx.channel(), deserialize(msg));
                    break;
                case JOINING:
                    onJoining(deserialize(msg));
                    break;
                case SPREAD_NODE_STATUS:
                    onSpreadNodeStatus(deserialize(msg));
                    break;
                case CLUSTER_TOPOLOGY:
                    onClusterTopology(deserialize(msg));
                    break;
                case HEART_BEAT:
                    onHeartbeat(deserialize(msg));
                    break;
                default:
                    LOGGER.error("Unknown type message, messageType={}", messageType.name());
                    break;
            }
        } catch (Throwable e) {
            LOGGER.error("Package handler catch an unexpected error, errorMsg={}", e.getMessage(), e);
        }
    }

    private void onGreet(Channel channel, GreetMessage message) {
        LOGGER.info("Receive greet from {}", message.getIdentifier());

        if (peerIdentifier != null) {
            LOGGER.warn("Peer config has already init: {}", message.getIdentifier());
        }

        peerIdentifier = new ClusterNodeConfig(message.getHost(), message.getPort());
        clusterChannel = new DefaultClusterChannel(message.getIdentifier(), channel, channelMode);
        clusterEventLoop.addChannelIfAbsent(clusterChannel);
    }

    // todo 触发钩子方法
    private void onJoining(JoiningMessage message) {
        LOGGER.info("Node is joining cluster: {}", message.getNode());

        ClusterNode node = message.getNode();
        ClusterNode exist;

        if ((exist = clusterEventLoop.getTopology().getNode(node.getIdentifier())) != null) {
            if (node.getVersion() <= exist.getVersion()) {
                LOGGER.warn("Exist node has not finish event loop and this joining is rejected: {}", exist);
                clusterChannel.closeAsync();
                return;
            }
        }

        ClusterNode joiningNode = node.clone(ClusterNodeStatus.joining);
        updateAndSpreadNodeStatus(joiningNode);

        ClusterNode activeNode = node.clone(ClusterNodeStatus.active);
        updateAndSpreadNodeStatus(activeNode);

        syncClusterTopology();
    }

    private void onSpreadNodeStatus(SpreadNodeStatusMessage message) {
        LOGGER.info("Spread node status: {}", message.getNode());

        ClusterNode node = message.getNode();
        ClusterNode exist;

        if ((exist = clusterEventLoop.getTopology().getNode(node.getIdentifier())) != null) {
            if (node.getVersion() <= exist.getVersion()) {
                LOGGER.warn("Node is obsoleted, ignore: {}", message.getNode());
                return;
            }
        }

        updateAndSpreadNodeStatus(node);

        LOGGER.info("Cluster size is: {}", clusterEventLoop.getTopology().activeNum());
    }

    private void onClusterTopology(ClusterTopologyMessage message) {
        ClusterTopology topology = message.getTopology();

        for (ClusterNode node : topology) {
            ClusterNode exist;
            if ((exist = clusterEventLoop.getTopology().getNode(node.getIdentifier())) != null) {
                if (node.getVersion() <= exist.getVersion()) {
                    LOGGER.warn("Node is obsoleted, ignore: {}", node);
                    continue;
                }
            }

            updateAndSpreadNodeStatus(node);
        }

        LOGGER.info("cluster size is: {}", clusterEventLoop.getTopology().activeNum());
    }

    private void onHeartbeat(HeartbeatMessage message) {
        LOGGER.info("Node heartbeat: {}", message.getNodeIdentifier());

        if (!Objects.equals(clusterEventLoop.getTopology().getIdentifier(), message.getClusterIdentifier())) {
            LOGGER.info("cluster identifier mismatch: {}" + message.getNodeIdentifier());
            syncClusterTopology();
        }
    }

    private void updateAndSpreadNodeStatus(ClusterNode node) {
        LOGGER.info("Node is {}: {}", node.getStatus(), node);

        clusterEventLoop.getTopology().putNode(node);

        for (ClusterChannel otherClusterChannel : clusterEventLoop.getChannels()) {
            if (!Objects.equals(this.clusterChannel, otherClusterChannel)) {
                otherClusterChannel.write(new SpreadNodeStatusMessage(node));
            }
        }
    }

    private void syncClusterTopology() {
        clusterChannel.write(new ClusterTopologyMessage(clusterEventLoop.getTopology()));
    }

    private <T> T deserialize(Package pkg) {
        SerializeType serializeType = SerializeType.typeOf(pkg.getSerializeType());
        switch (serializeType) {
            case java:
                return CloneUtils.javaDeserialize(pkg.getPayload());
            case hessian:
                return CloneUtils.hessianDeserialize(pkg.getPayload());
            default:
                throw new UnsupportedOperationException(String.format("Unsupported serialize serializeType='%s'", serializeType));
        }
    }
}
