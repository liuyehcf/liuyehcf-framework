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
import com.google.common.collect.Lists;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class MessageHandler extends SimpleChannelInboundHandler<Package> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    private final ClusterEventLoop clusterEventLoop;
    private final Topology topology;
    private final ChannelMode channelMode;
    private Identifier peerIdentifier;

    public MessageHandler(ClusterEventLoop clusterEventLoop, ChannelMode channelMode, Identifier peerIdentifier) {
        Assert.assertNotNull(clusterEventLoop, "clusterEventLoop");
        Assert.assertNotNull(channelMode, "channelMode");
        if (channelMode.isClientMode()) {
            Assert.assertNotNull(peerIdentifier, "peerIdentifier");
        }
        this.clusterEventLoop = clusterEventLoop;
        this.topology = this.clusterEventLoop.getTopology();
        this.channelMode = channelMode;
        this.peerIdentifier = peerIdentifier;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Package msg) {
        try {
            MessageType messageType = MessageType.typeOf(msg.getType());

            if (!MessageType.GREET.equals(messageType)) {
                if (peerIdentifier == null) {
                    ctx.close();
                    return;
                }
            }

            switch (messageType) {
                case GREET:
                    onGreet(ctx.channel(), deserialize(msg));
                    break;
                case JOINING:
                    onJoining(deserialize(msg));
                    break;
                case SYNC_MEMBER:
                    onSyncMember(deserialize(msg));
                    break;
                case SYNC_TOPOLOGY:
                    onSyncTopology(deserialize(msg));
                    break;
                case ELECTION:
                    onElection(deserialize(msg));
                    break;
                case VOTE:
                    onVote(deserialize(msg));
                    break;
                case ELECTION_RESULT:
                    onElectionResult(deserialize(msg));
                    break;
                case SYNC_TOPOLOGY_REQUEST:
                    syncTopologyRequest(deserialize(msg));
                    break;
                case LEADER_STATUS_REQUEST:
                    onLeaderStatusRequest(deserialize(msg));
                    break;
                case LEADER_STATUS_RESPONSE:
                    onLeaderStatusResponse(deserialize(msg));
                    break;
                case LEADER_RECOMMENDATION:
                    onLeaderRecommendation(deserialize(msg));
                    break;
                case COMPENSATE_MEMBERS:
                    onCompensateMembers(deserialize(msg));
                    break;
                case HEART_BEAT:
                    onHeartbeat(deserialize(msg));
                    break;
                default:
                    LOGGER.error("unknown type message, messageType={}", messageType.name());
                    break;
            }
        } catch (Throwable e) {
            LOGGER.error("package handler catch an unexpected error, errorMsg={}", e.getMessage(), e);
        }
    }

    private void onGreet(Channel channel, Greet message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[{}] receive greet from [{}]",
                    topology.getSelf().getIdentifier(), message.getIdentifier());
        }

        if (peerIdentifier != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[{}] receive greet from [{}], but peerIdentifier is already init",
                        topology.getSelf().getIdentifier(), message.getIdentifier());
            }
            channel.close();
            return;
        }

        peerIdentifier = new MemberConfig(message.getHost(), message.getPort());
        clusterEventLoop.addChannelIfAbsent(new DefaultClusterChannel(message, channel, channelMode));
    }

    private void onJoining(JoiningRequest message) {
        LOGGER.info("[{}] is joining through [{}]",
                message.getMember().getIdentifier(), topology.getSelf().getIdentifier());

        Member member = message.getMember();
        Member exist;

        if ((exist = topology.getMember(member)) != null) {
            if (member.getStatus().isActive()) {
                LOGGER.warn("exist member [{}] has not finish event loop and this joining is rejected", exist.getIdentifier());

                // relay means message is resent by seed member, not joining member itself
                if (!message.isRelayed()) {
                    ClusterChannel clusterChannel = clusterEventLoop.getChannel(member);
                    if (clusterChannel != null) {
                        clusterChannel.closeAsync();
                    }
                }
                return;
            }
        }

        if (topology.isSelfLeader()) {
            Member joiningMember = member.clone(topology.generateNextMemberId(), null, MemberStatus.joining);
            clusterEventLoop.updateAndBroadcastMemberStatus(joiningMember);

            Member activeMember = joiningMember.clone(topology.generateNextMemberId(), null, MemberStatus.active);
            clusterEventLoop.updateAndBroadcastMemberStatus(activeMember);

            clusterEventLoop.unicast(activeMember, new SyncTopology(topology));
        } else {
            Member leader;
            if ((leader = topology.getLeader()) != null
                    && !leader.getLocalStatus().isUnreachable()) {
                clusterEventLoop.unicast(leader, new JoiningRequest(member, true));
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("cluster has no leader, [{}] reject joining process", topology.getSelf().getIdentifier());
                }
                ClusterChannel channel = clusterEventLoop.getChannel(peerIdentifier);
                if (channel != null) {
                    channel.closeAsync();
                }
            }
        }
    }

    private void onSyncMember(SyncMemberStatus message) {
        Member member = message.getMember();

        Member exist;
        if ((exist = topology.getMember(member)) != null) {
            if (member.getId() <= exist.getId()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("[{}] reject sync member from [{}] due to outdated peerMemberId, localMemberId={}, peerMemberId={}",
                            topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier(), exist.getId(), member.getId());
                }
                return;
            }
        }

        if (topology.isSelfLeader()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[{}] reject sync member from [{}] due to this side is leader, {}",
                        topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier(), message.getMember());
            }
            return;
        }

        if (member.getRole().isLeader()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[{}] reject sync member from [{}] due to remote member is leader, {}",
                        topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier(), message.getMember());
            }
            return;
        }

        LOGGER.info("[{}] receive sync member from [{}], leader is moving member [{}] to [{}]",
                topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier(), member.getIdentifier(), member.getStatus());

        clusterEventLoop.updateAndBroadcastMemberStatus(member);
    }

    private void onSyncTopology(SyncTopology message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[{}] receive sync topology from [{}]",
                    topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier());
        }

        Topology peerTopology = message.getTopology();
        long localTransactionId = topology.getTransactionId();
        long peerTransactionId = peerTopology.getTransactionId();
        Member localLeader = topology.getLeader();
        Member peerLeader = peerTopology.getLeader();

        if (localTransactionId > peerTransactionId) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[{}] reject sync topology from [{}] due to outdated peerTransactionId, localTransactionId={}, peerTransactionId={}",
                        topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier(), localTransactionId, peerTransactionId);
            }
            localLeaderWinCollision(localTransactionId);
            return;
        } else if (localTransactionId == peerTransactionId) {
            if (localLeader != null && peerLeader != null) {
                if (localLeader.getId() > peerLeader.getId()) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("[{}] reject sync topology from [{}] due to outdated peerMemberId, localMemberId={}, peerMemberId={}",
                                topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier(), localLeader.getId(), peerLeader.getId());
                    }
                    localLeaderWinCollision(localTransactionId);
                    return;
                } else if (localLeader.getId() == peerLeader.getId()) {
                    if (!Identifier.equals(localLeader, peerLeader)) {
                        if (channelMode.isServerMode()) {
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("[{}] reject sync topology from [{}] due to peer client mode",
                                        topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier());
                            }
                            localLeaderWinCollision(localTransactionId);
                            return;
                        } else {
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("[{}] reject sync topology from [{}] due to peer server mode",
                                        topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier());
                            }
                            if (topology.isSelfLeader()) {
                                if (LOGGER.isDebugEnabled()) {
                                    LOGGER.debug("[{}] recommend [{}] as new leader due to peer server mode",
                                            topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier());
                                }

                                // recommend peer (server mode) to be the new leader
                                clusterEventLoop.unicast(peerIdentifier, new LeaderRecommendation(peerTransactionId));
                            }
                            return;
                        }
                    }

                    // if localLeader' identifier equals peerLeader's identifier, that means leader is same on both sides, this member is follower
                }

                // if peerLeader'id is bigger than localLeader'id, just sync topology from peer
            } else {
                if (localLeader == null && peerLeader == null) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("[{}] reject sync topology from [{}] due to the leaders in both sides are null",
                                topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier());
                    }
                    return;
                } else if (peerLeader == null) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("[{}] reject sync topology from [{}] due to the leader in peer side are null (with same transactionId)",
                                topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier());
                    }

                    if (topology.isSelfLeader()) {
                        clusterEventLoop.unicast(peerIdentifier, new SyncTopology(topology));
                    }
                    return;
                }

                // if peerLeader is not null while local Leader is null, just sync topology from peer
            }
        } else {
            if (peerLeader == null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("[{}] reject sync topology from [{}] due to the leader in peer side are null (with up-to-date transactionId)",
                            topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier());
                }
                return;
            }

            // if peerLeader is not null, just sync topology from peer
        }

        // if the program is executed here, it must satisfy one of the following conditions
        // 1. localTransactionId < peerTransactionId && peerLeader != null
        // 2. localTransactionId == peerTransactionId and localLeader'id < peerLeader'id
        // 3. localTransactionId == peerTransactionId and localLeader == null and peerLeader != null
        // 4. localTransactionId == peerTransactionId and localLeader equals with peerLeader

        if (!peerLeader.getStatus().isActive() || !peerLeader.getLocalStatus().isActive()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[{}] reject sync topology from [{}] due to the leader in peer side are not active (with up-to-date transactionId)",
                        topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier());
            }
            return;
        }

        List<Member> compensateMembers = Lists.newArrayList();

        for (Member member : topology) {
            if (!peerTopology.hasMember(member)) {
                compensateMembers.add(member);
            }
        }

        topology.syncFrom(peerTopology);

        for (Member member : peerTopology) {
            clusterEventLoop.broadcast(new SyncMemberStatus(member));
        }

        if (CollectionUtils.isNotEmpty(compensateMembers)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[{}] send compensate members to [{}]",
                        topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier());
            }
            clusterEventLoop.unicast(peerIdentifier, new CompensateMembers(compensateMembers));
        }
    }

    private void onElection(Election message) {
        if (message.getState().isFirstState()) {
            clusterEventLoop.voteForFirstStateElection(message);
        } else {
            clusterEventLoop.voteForSecondStateElection(message);
        }
    }

    private void onVote(Vote message) {
        if (message.getState().isFirstState()) {
            clusterEventLoop.statisticsForFirstStateElection(message);
        } else {
            clusterEventLoop.statisticsForSecondStateElection(message);
        }
    }

    private void onElectionResult(ElectionResult message) {
        Member leader = message.getLeader();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[{}] receive election result from [{}], new leader is [{}]",
                    topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier(), leader.getIdentifier());
        }

        long localTransactionId = topology.getTransactionId();
        long peerTransactionId = message.getTransactionId();

        if (localTransactionId < peerTransactionId) {
            topology.addOrReplaceMember(leader);
            clusterEventLoop.clearElectionStatus();
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[{}] reject election result from [{}] due to outdated peerTransactionId, localTransactionId={}, peerTransactionId={}",
                        topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier(), localTransactionId, peerTransactionId);
            }
        }
    }

    private void syncTopologyRequest(SyncTopologyRequest message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[{}] receive sync topology request from [{}]",
                    topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier());
        }

        // allow non-leader to send topology
        // think about this situation
        // 1. A receive B's heartbeat
        // 2. A find that local's transactionId is smaller than B's transactionId
        // 3. then A need to send SyncTopologyRequest to B's leader(C)
        // 4. but A has no connection with C
        // 5. so A has to send SyncTopologyRequest to B
        clusterEventLoop.unicast(peerIdentifier, new SyncTopology(topology));
    }

    private void onLeaderStatusRequest(LeaderStatusRequest message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[{}] receive leader status request from [{}]",
                    topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier());
        }

        long requestId = message.getRequestId();
        clusterEventLoop.unicast(peerIdentifier, new LeaderStatusResponse(requestId,
                topology.getLeader() == null ? MemberStatus.inactive : topology.getLeader().getLocalStatus()));
    }

    private void onLeaderStatusResponse(LeaderStatusResponse message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[{}] receive leader status response [{}] from [{}]",
                    topology.getSelf().getIdentifier(), message.getStatus(), peerIdentifier.getIdentifier());
        }

        clusterEventLoop.statisticsForLeaderStatus(message);
    }

    private void onLeaderRecommendation(LeaderRecommendation message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[{}] receive leader recommendation from [{}]",
                    topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier());
        }

        if (!topology.assumeLeader(message.getExpectedTransactionId())) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[{}] agree leader recommendation from [{}] but assume leader failed",
                        topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier());
            }
        }
    }

    private void onCompensateMembers(CompensateMembers message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[{}] receive compensate members from [{}]",
                    topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier());
        }

        List<Member> members = message.getMembers();

        if (CollectionUtils.isNotEmpty(members)) {
            for (Member member : members) {
                if (!topology.hasMember(member)) {
                    topology.addMemberIfAbsent(member.clone(topology.generateNextMemberId(), MemberRole.follower, MemberStatus.active));
                }
            }
        }
    }

    private void onHeartbeat(Heartbeat message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[{}] receive heartbeat from [{}]",
                    topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier());
        }

        Member peerLeader = message.getLeader();

        long localTransactionId = topology.getTransactionId();
        long peerTransactionId = message.getTransactionId();

        if (localTransactionId < peerTransactionId) {
            if (peerLeader != null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("[{}] send sync topology request to [{}] due to outdated peerTransactionId, localTransactionId={}, peerTransactionId={}",
                            topology.getSelf().getIdentifier(), peerLeader, localTransactionId, peerTransactionId);
                }
                if (!clusterEventLoop.unicast(peerLeader, new SyncTopologyRequest())) {
                    // in this situation, this side has no connection with peerLeader
                    // so ask peer to sync topology
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("[{}] send sync topology request to [{}] failed and send it to [{}], localTransactionId={}, peerTransactionId={}",
                                topology.getSelf().getIdentifier(), peerLeader, peerIdentifier, localTransactionId, peerTransactionId);
                    }
                    clusterEventLoop.unicast(peerIdentifier, new SyncTopologyRequest());
                }
            }
        } else if (localTransactionId > peerTransactionId) {
            if (topology.isSelfLeader()) {
                if (peerLeader != null && Identifier.equals(peerLeader, peerIdentifier)) {
                    localLeaderWinCollision(localTransactionId);
                } else {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("[{}] send sync topology to [{}] due to outdated peerTransactionId, localTransactionId={}, peerTransactionId={}",
                                topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier(), localTransactionId, peerTransactionId);
                    }
                    clusterEventLoop.unicast(peerIdentifier, new SyncTopology(topology));
                }
            } else {
                if (peerLeader == null || Identifier.equals(topology.getSelf(), peerLeader)) {
                    // in this situation, peer side regard this side as leader or peer has not leader
                    // but from perspective of this side leader is not self
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("[{}] as leader proxy send sync topology to [{}], due to outdated peerTransactionId, localTransactionId={}, peerTransactionId={}",
                                topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier(), localTransactionId, peerTransactionId);
                    }
                    clusterEventLoop.unicast(peerIdentifier, new SyncTopology(topology));
                }
            }
        } else {
            if (!Objects.equals(topology.getIdentifier(), message.getClusterIdentifier())) {
                if (topology.isSelfLeader()) {
                    // cannot do memberId's comparision if peer side is leader, so just send SyncTopology to peer side
                    // and sync topology process in the peer side will do further comparision
                    clusterEventLoop.unicast(peerIdentifier, new SyncTopology(topology));
                } else if (peerLeader != null) {
                    // cannot do memberId's comparision, so just send SyncTopologyRequest to peer side
                    // when this side receive SyncTopology, it will make further comparision
                    clusterEventLoop.unicast(peerLeader, new SyncTopologyRequest());
                }
            }
        }
    }

    private void localLeaderWinCollision(long expectedTransactionId) {
        if (topology.isSelfLeader()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[{}] defeat [{}] in the leader collision",
                        topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier());
            }
            if (topology.assumeLeader(expectedTransactionId)) {
                clusterEventLoop.unicast(peerIdentifier, new SyncTopology(topology));
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("[{}] defeat [{}] in the leader collision but assume leader failed",
                            topology.getSelf().getIdentifier(), peerIdentifier.getIdentifier());
                }
            }
        }
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
