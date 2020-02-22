package com.github.liuyehcf.framework.flow.engine.runtime.remote.io;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.flow.engine.FlowEngine;
import com.github.liuyehcf.framework.flow.engine.promise.AbstractPromise;
import com.github.liuyehcf.framework.flow.engine.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.promise.PromiseListener;
import com.github.liuyehcf.framework.flow.engine.runtime.config.FlowProperties;
import com.github.liuyehcf.framework.flow.engine.runtime.config.RuntimeMode;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.cluster.*;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.handler.*;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.message.*;
import com.github.liuyehcf.framework.flow.engine.runtime.remote.io.support.LeaderStatusSupport;
import com.github.liuyehcf.framework.flow.engine.util.StatisticsUtils;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class DefaultClusterEventLoop implements ClusterEventLoop {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultClusterEventLoop.class);

    private final EventLoopGroup serverBossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup serverWorkerGroup = new NioEventLoopGroup();
    private final EventLoopGroup clientWorkGroup = new NioEventLoopGroup();
    private final FlowEngine engine;
    private final FlowProperties properties;
    private final Topology topology;
    private final Map<String, ClusterChannel> channels = Maps.newConcurrentMap();
    private final LeaderStatusSupport leaderStatusSupport = new LeaderStatusSupport();
    private final AtomicReference<Election> proposedFirstStateElection = new AtomicReference<>();
    private final AtomicReference<Election> proposedSecondStateElection = new AtomicReference<>();
    private final AtomicReference<Election> lastAcceptedFirstStateElection = new AtomicReference<>();
    private final AtomicReference<Election> lastAcceptedSecondStateElection = new AtomicReference<>();
    private final AtomicBoolean firstStateElectionFinished = new AtomicBoolean(false);
    private final AtomicBoolean secondStateElectionFinished = new AtomicBoolean(false);
    private final List<Vote> receivedFirstStateVotes = Lists.newCopyOnWriteArrayList();
    private final List<Vote> receivedSecondStateVotes = Lists.newCopyOnWriteArrayList();
    private final Promise<Void> closePromise = new AbstractPromise<Void>() {
    };
    private ChannelFuture channelFuture;

    public DefaultClusterEventLoop(FlowEngine engine) {
        Assert.assertNotNull(engine, "engine");
        this.engine = engine;
        this.properties = engine.getProperties();
        this.topology = new DefaultTopology(this.properties.getSelfConfig());
        init();
    }

    private void init() {
        if (properties.getClusterConfig() == null) {
            LOGGER.warn("[{}] mode missing cluster config, downgrade to [{}] mode", RuntimeMode.cluster, RuntimeMode.singleton);
            return;
        }

        if (CollectionUtils.isEmpty(properties.getClusterConfig().getSeeds())) {
            LOGGER.warn("[{}] mode missing seed member config, downgrade to [{}] mode", RuntimeMode.cluster, RuntimeMode.singleton);
            return;
        }

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(serverBossGroup, serverWorkerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        addHandlers(ch, ChannelMode.server, null);
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_RCVBUF, NettyConstant.MAX_RCVBUF)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_REUSEADDR, true);

        try {
            channelFuture = bootstrap.bind(properties.getPort()).sync();
            TopologyProbe.register(this);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final FlowEngine getEngine() {
        return engine;
    }

    @Override
    public final Topology getTopology() {
        return topology;
    }

    @Override
    public final synchronized boolean addChannelIfAbsent(ClusterChannel newChannel) {
        String peerIdentifier = newChannel.getPeerIdentifier().getIdentifier();
        String selfIdentifier = topology.getSelf().getIdentifier();
        ClusterChannel existChannel = channels.get(peerIdentifier);

        // ignore the cases that channel removed right after channels.get
        if (existChannel != null) {
            if (Objects.equals(newChannel.getChannelMode(), existChannel.getChannelMode())) {
                newChannel.closeAsync();
                return false;
            } else {
                // keep big one as server mode
                if (StringUtils.compare(selfIdentifier, peerIdentifier) < 0) {
                    if (newChannel.getChannelMode().isServerMode()) {
                        newChannel.closeAsync();
                        return false;
                    }
                } else if (StringUtils.compare(selfIdentifier, peerIdentifier) > 0) {
                    if (newChannel.getChannelMode().isClientMode()) {
                        newChannel.closeAsync();
                        return false;
                    }
                }
            }
        }

        ClusterChannel removedChannel;

        synchronized (channels) {
            removedChannel = channels.put(peerIdentifier, newChannel);
        }
        if (removedChannel != null) {
            removedChannel.closeAsync();
        }

        newChannel.getChannel().closeFuture().addListener((ChannelFuture future) -> {
                    synchronized (channels) {
                        ClusterChannel clusterChannel = channels.get(peerIdentifier);
                        if (Objects.equals(clusterChannel.getChannel(), future.channel())) {
                            channels.remove(peerIdentifier);
                        }
                    }
                }
        );
        return true;
    }

    @Override
    public final boolean hasChannel(Identifier peerIdentifier) {
        return channels.containsKey(peerIdentifier.getIdentifier());
    }

    @Override
    public final ClusterChannel getChannel(Identifier peerIdentifier) {
        return channels.get(peerIdentifier.getIdentifier());
    }

    @Override
    public final Collection<ClusterChannel> getChannels() {
        return channels.values();
    }

    @Override
    public final void createChannel(MemberIdentifier peerIdentifier) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientWorkGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        addHandlers(ch, ChannelMode.client, peerIdentifier);
                    }
                })
                .option(ChannelOption.SO_RCVBUF, NettyConstant.MAX_RCVBUF)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true);

        Channel channel;
        try {
            channel = bootstrap.connect(peerIdentifier.getHost(), peerIdentifier.getPort()).sync().channel();
        } catch (Throwable e) {
            Member member = topology.getMember(peerIdentifier);
            if (member != null) {
                if (!member.getStatus().isInactive()) {
                    member.setLocalStatus(MemberStatus.unreachable);

                    if (topology.isSelfLeader()) {
                        updateAndBroadcastMemberStatus(member.clone(topology.generateNextMemberId(), MemberRole.follower, MemberStatus.leaving));
                        updateAndBroadcastMemberStatus(member.clone(topology.generateNextMemberId(), MemberRole.follower, MemberStatus.inactive));
                    } else if (topology.isLeader(member)) {
                        // ask other member about leader's status

                        // todo 配置化
                        long resendTimeGap;
                        if ((resendTimeGap = System.nanoTime() - leaderStatusSupport.getPreviousRequestId()) > 3000000000L) {
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("[{}] broadcast leader status request", topology.getLeader().getIdentifier());
                            }
                            sendLeaderStatusRequest(new LeaderStatusRequest(leaderStatusSupport.start()));
                        } else {
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("[{}] broadcasting leader status request is forbidden because still in silence [{}]ms",
                                        topology.getLeader().getIdentifier(), resendTimeGap / 1000000);
                            }
                        }
                    }
                }
            }

            if (e instanceof InterruptedException) {
                throw (InterruptedException) e;
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("create channel error, errorMsg={}", e.getMessage());
            }
            return;
        }

        ClusterChannel clusterChannel = new DefaultClusterChannel(peerIdentifier, channel, ChannelMode.client);
        if (addChannelIfAbsent(clusterChannel)) {
            clusterChannel.write(new Greet(properties.getHost(), properties.getPort()));

            HeartBeat.register(this, clusterChannel, properties.getHeartbeatInterval());
        }
    }

    @Override
    public final boolean unicast(Identifier peerIdentifier, Object message) {
        ClusterChannel clusterChannel = getChannel(peerIdentifier);
        if (clusterChannel != null) {
            clusterChannel.write(message);
            return true;
        }
        return false;
    }

    @Override
    public final void broadcast(Object message) {
        for (ClusterChannel clusterChannel : getChannels()) {
            clusterChannel.write(message);
        }
    }

    @Override
    public final void statisticsForLeaderStatus(LeaderStatusResponse leaderStatusResponse) {
        MemberStatus leaderStatus = leaderStatusResponse.getStatus();

        if (!MemberStatus.active.equals(leaderStatus)) {
            int notActiveNum = leaderStatusSupport.increase(leaderStatusResponse.getRequestId());
            if (StatisticsUtils.isAccept(notActiveNum, topology.activeNum())) {
                leaderStatusSupport.finish();

                Member self = topology.getSelf();
                Election firstStateElection = new Election(ElectionState.first, topology.getTransactionId() + 1, self, self);

                if (proposedFirstStateElection.compareAndSet(null, firstStateElection)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("[{}] launch leader election process, send first state election, {}",
                                self.getIdentifier(), firstStateElection);
                    }
                    sendElection(firstStateElection);
                }
            }
        }
    }

    @Override
    public final void updateAndBroadcastMemberStatus(Member member) {
        topology.addOrReplaceMember(member);

        broadcast(new SyncMemberStatus(member));
    }

    @Override
    public synchronized final void voteForFirstStateElection(Election election) {
        ElectionState state = election.getState();
        long transactionId = election.getTransactionId();
        long proposerMemberId = election.getProposer().getId();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[{}] receive election, state={}, transactionId={}, proposerMemberId={}",
                    topology.getSelf().getIdentifier(), state, transactionId, proposerMemberId);
        }

        boolean accept = false;
        Long lastAcceptedFirstStateTransactionId = null;
        Long lastAcceptedFirstStateProposerMemberId = null;

        Election lastAcceptedFirstStateElection;

        // in paxos, if a_proposal_id > max_proposal_id then vote accept for this first stat election
        // consider following situation
        // 1. a, b, c  form a cluster and a is leader, b-id > c-id
        // 2. then a crashed
        // 3. this time b detected a is unreachable, and start leader election
        // 4. b vote true for itself, and cached max-id=b-id
        // 5. c vote false, because in this moment c hasn't detected a unreachable, and don't cache id
        // 6. b's election rejected
        // 6.1 c detected a unreachable and start leader election
        // 6.2 c vote true for itself, and cached max-id=c-id
        // 6.3 b vote false because c-id < max-id(in step 4, max-id has been set to b-id)
        // 6.4 b clear local max-id after reject
        // 7. c's election rejected
        // 7.1 and after a while b start another leader election (continuous elections are an important way to keep alive)
        // 7.2 b vote true and cache max-id=b-id
        // 7.3 c vote true and cache max-id=b-id
        // 8. b's election accepted
        boolean isUpToDate = (lastAcceptedFirstStateElection = this.lastAcceptedFirstStateElection.get()) == null
                || (lastAcceptedFirstStateTransactionId = lastAcceptedFirstStateElection.getTransactionId()) < transactionId
                || lastAcceptedFirstStateTransactionId == transactionId
                && (lastAcceptedFirstStateProposerMemberId = lastAcceptedFirstStateElection.getProposer().getId()) < proposerMemberId;

        if (isUpToDate) {
            if (this.lastAcceptedFirstStateElection.compareAndSet(lastAcceptedFirstStateElection, election)) {
                accept = true;
            } else {
                // re do recursively
                voteForFirstStateElection(election);

                // must return avoid unicast multiply times
                return;
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[{}] vote [{}], state={}, transactionId={}, lastAcceptedFirstStateTransactionId={}, proposerMemberId={}, lastAcceptedFirstStateProposerMemberId={}",
                    topology.getSelf().getIdentifier(), accept ? "accept" : "reject", state,
                    transactionId, lastAcceptedFirstStateTransactionId, proposerMemberId, lastAcceptedFirstStateProposerMemberId);
        }

        Election lastAcceptedSecondStateElection = this.lastAcceptedSecondStateElection.get();
        Member lastAcceptedCandidate = null;

        // if this election's transaction id is expired (for example when leader is determined, than the election is received)
        // Vote only return the newest accepted election (accepted election's transaction id is equal with or newer than this election's transaction id)
        if (lastAcceptedSecondStateElection != null) {
            lastAcceptedCandidate = lastAcceptedSecondStateElection.getCandidate();
            if (lastAcceptedSecondStateElection.getTransactionId() < transactionId) {
                lastAcceptedCandidate = null;
            }
        }

        Vote vote = new Vote(
                state,
                transactionId,
                accept,
                election.getProposer(),
                lastAcceptedCandidate
        );

        if (topology.isSelf(election.getProposer())) {
            statisticsForFirstStateElection(vote);
        } else {
            unicast(election.getProposer(), vote);
        }
    }

    @Override
    public synchronized final void voteForSecondStateElection(Election election) {
        ElectionState state = election.getState();
        long transactionId = election.getTransactionId();
        long proposerMemberId = election.getProposer().getId();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[{}] receive election, state={}, transactionId={}, proposerMemberId={}",
                    topology.getSelf().getIdentifier(), election.getState(), transactionId, proposerMemberId);
        }

        boolean accept = false;
        long candidateMemberId = election.getCandidate().getId();
        Election lastAcceptedSecondStateElection;
        Long lastAcceptedSecondStateTransactionId = null;
        Long lastAcceptedSecondStateCandidateMemberId = null;

        boolean isUpToDate = (lastAcceptedSecondStateElection = this.lastAcceptedSecondStateElection.get()) == null
                || (lastAcceptedSecondStateTransactionId = lastAcceptedSecondStateElection.getTransactionId()) < transactionId
                || lastAcceptedSecondStateTransactionId == transactionId
                && (lastAcceptedSecondStateCandidateMemberId = lastAcceptedSecondStateElection.getCandidate().getId()) <= candidateMemberId;

        if (isUpToDate) {
            if (this.lastAcceptedSecondStateElection.compareAndSet(lastAcceptedSecondStateElection, election)) {
                accept = true;
                lastAcceptedSecondStateElection = election;
            } else {
                // re do recursively
                voteForSecondStateElection(election);

                // must return avoid unicast multiply times
                return;
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[{}] vote [{}], state={}, transactionId={}, lastAcceptedSecondStateTransactionId={}, candidateMemberId={}, lastAcceptedSecondStateCandidateMemberId={}",
                    topology.getSelf().getIdentifier(), accept ? "accept" : "reject", state,
                    transactionId, lastAcceptedSecondStateTransactionId, candidateMemberId, lastAcceptedSecondStateCandidateMemberId);
        }

        Member lastAcceptedCandidate = lastAcceptedSecondStateElection.getCandidate();

        Vote vote = new Vote(
                election.getState(),
                transactionId,
                accept,
                election.getProposer(),
                lastAcceptedCandidate
        );

        if (topology.isSelf(election.getProposer())) {
            statisticsForSecondStateElection(vote);
        } else {
            unicast(election.getProposer(), vote);
        }
    }

    @Override
    public synchronized final void statisticsForFirstStateElection(Vote vote) {
        long transactionId = vote.getTransactionId();
        long proposerMemberId = vote.getProposer().getId();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[{}] receive vote [{}], state={}, transactionId={}, proposerMemberId={}",
                    topology.getSelf().getIdentifier(), vote.isAccept() ? "accept" : "reject",
                    vote.getState(), transactionId, proposerMemberId);
        }

        int activeNum = topology.activeNum();

        Election proposedFirstStateElection = this.proposedFirstStateElection.get();

        if (proposedFirstStateElection == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[{}] receive first state vote, but proposedFirstStateElection is null, transactionId={}, proposerMemberId={}",
                        topology.getSelf().getIdentifier(), transactionId, proposerMemberId);
            }
            return;
        }

        long proposedTransactionId = proposedFirstStateElection.getTransactionId();
        if (!Objects.equals(transactionId, proposedTransactionId)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[{}] receive first state vote, but transaction id [{}] of this vote is not equal with proposer's transaction id [{}], proposerMemberId={}",
                        topology.getSelf().getIdentifier(), transactionId, proposedTransactionId, proposerMemberId);
            }
            return;
        }

        receivedFirstStateVotes.add(vote);

        int acceptNum = (int) receivedFirstStateVotes.stream()
                .filter(Vote::isAccept).count();
        int rejectNum = (int) receivedFirstStateVotes.stream()
                .filter(Vote::isReject).count();

        boolean isFirstStateElectionAccepted = StatisticsUtils.isAccept(acceptNum, activeNum);
        boolean isFirstStateElectionRejected = StatisticsUtils.isReject(rejectNum, activeNum);

        if (isFirstStateElectionAccepted && firstStateElectionFinished.compareAndSet(false, true)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[{}]'s first state election is accepted [{}/{}/{}], {}",
                        topology.getSelf().getIdentifier(), acceptNum, rejectNum, activeNum, proposedFirstStateElection);
            }

            Member self = topology.getMember(properties.getSelfConfig());

            Election secondStateElection;

            Pair<Long, Member> maximumPair = selectMaximumAcceptedSecondStateElection(receivedFirstStateVotes);
            if (maximumPair != null) {
                long mostQualifiedTransactionId = maximumPair.getKey();

                // first state vote must has equal or higher transaction id
                // so only continue when mostQualified transaction Id equals with election's transaction id
                // ignore when election's transaction id is lower than mostQualifiedTransactionId
                if (proposedTransactionId == mostQualifiedTransactionId) {
                    Member mostQualifiedCandidate = maximumPair.getValue();
                    Member proposedProposer = proposedFirstStateElection.getProposer();
                    Member proposedCandidate = proposedFirstStateElection.getCandidate();

                    if (mostQualifiedCandidate.getId() != proposedProposer.getId()) {
                        // use 'value' of acceptedCandidate and keep version(transaction id and candidate unique id) of election
                        // the proposer will collect all the votes for the mostQualifiedCandidate
                        // in this situation, candidate's id is not its true id
                        secondStateElection = new Election(
                                ElectionState.second,
                                proposedTransactionId,
                                proposedProposer,
                                mostQualifiedCandidate.clone(proposedCandidate.getId(), null, null)
                        );
                    } else {
                        secondStateElection = proposedFirstStateElection.toSecondState();
                    }

                    if (proposedSecondStateElection.compareAndSet(null, secondStateElection)) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("[{}] send second state election, {}", self.getIdentifier(), secondStateElection);
                        }
                        sendElection(secondStateElection);
                    }
                }
            } else {
                // in principle, you can choose any member and for simplicity, just choose self
                secondStateElection = proposedFirstStateElection.toSecondState();

                if (proposedSecondStateElection.compareAndSet(null, secondStateElection)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("[{}] send second state election, {}", self.getIdentifier(), secondStateElection);
                    }
                    sendElection(secondStateElection);
                }
            }
        } else if (isFirstStateElectionRejected && firstStateElectionFinished.compareAndSet(false, true)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[{}]'s first state election is rejected [{}/{}/{}], {}",
                        topology.getSelf().getIdentifier(), acceptNum, rejectNum, activeNum, proposedFirstStateElection);
            }

            topology.increaseTransactionId(topology.getTransactionId());

            this.proposedFirstStateElection.set(null);
            this.firstStateElectionFinished.set(false);
            this.receivedFirstStateVotes.clear();
        }
    }

    @Override
    public synchronized final void statisticsForSecondStateElection(Vote vote) {
        long transactionId = vote.getTransactionId();
        long proposerMemberId = vote.getProposer().getId();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[{}] receive vote [{}], state={}, transactionId={}, proposerMemberId={}",
                    topology.getSelf().getIdentifier(), vote.isAccept() ? "accept" : "reject",
                    vote.getState(), transactionId, proposerMemberId);
        }

        int activeNum = topology.activeNum();

        Election proposedFirstStateElection = this.proposedFirstStateElection.get();

        if (proposedFirstStateElection == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[{}] receive second state vote, but election is already finished. transactionId={}, proposerMemberId={}",
                        topology.getSelf().getIdentifier(), transactionId, proposerMemberId);
            }
            return;
        }

        long proposedTransactionId = proposedFirstStateElection.getTransactionId();
        if (!Objects.equals(vote.getTransactionId(), proposedTransactionId)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[{}] receive second state vote, but transaction id [{}] of this vote is not equal with proposer's transaction id [{}], proposerMemberId={}",
                        topology.getSelf().getIdentifier(), transactionId, proposedTransactionId, proposerMemberId);
            }
            return;
        }

        receivedSecondStateVotes.add(vote);

        int acceptNum = (int) receivedSecondStateVotes.stream()
                .filter(Vote::isAccept).count();
        int rejectNum = (int) receivedSecondStateVotes.stream()
                .filter(Vote::isReject).count();

        boolean isSecondStateElectionAccepted = StatisticsUtils.isAccept(acceptNum, activeNum);
        boolean isSecondStateElectionRejected = StatisticsUtils.isReject(rejectNum, activeNum);

        if (isSecondStateElectionAccepted && secondStateElectionFinished.compareAndSet(false, true)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[{}]'s second state election is accepted [{}/{}/{}], {}",
                        topology.getSelf().getIdentifier(), acceptNum, rejectNum, activeNum, vote);
            }

            Member acceptedCandidate = vote.getCandidate();

            // according to paxos protocol, A can propose itself as candidate
            // and also A can propose B as candidate if maximumAcceptedSecondStateElection of A is B
            if (topology.isSelf(acceptedCandidate)) {
                if (!topology.isSelfLeader()) {

                    if (topology.assumeLeader(topology.getTransactionId(), proposedTransactionId)) {
                        Member newLeader = topology.getLeader();

                        clearElectionStatus();

                        broadcast(new ElectionResult(newLeader, topology.getTransactionId()));

                        LOGGER.info("cluster member [{}] is the new leader locally", newLeader.getIdentifier());
                    } else {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("[{}]'s second state election is accepted but assume leader failed", topology.getSelf().getIdentifier());
                        }
                    }
                }
            } else {
                // in this situation, acceptCandidate's id is proposer's id
                LOGGER.info("cluster member [{}] is the new leader non-locally", acceptedCandidate.getIdentifier());
            }
        } else if (isSecondStateElectionRejected && secondStateElectionFinished.compareAndSet(false, true)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[{}]'s second state election is rejected [{}/{}/{}], {}",
                        topology.getSelf().getIdentifier(), acceptNum, rejectNum, activeNum, proposedFirstStateElection);
            }

            this.proposedSecondStateElection.set(null);
            this.secondStateElectionFinished.set(false);
            this.receivedSecondStateVotes.clear();
        }
    }

    @Override
    public final void clearElectionStatus() {
        proposedFirstStateElection.set(null);
        proposedSecondStateElection.set(null);
        lastAcceptedFirstStateElection.set(null);
        lastAcceptedSecondStateElection.set(null);
        firstStateElectionFinished.set(false);
        secondStateElectionFinished.set(false);
        receivedFirstStateVotes.clear();
        receivedSecondStateVotes.clear();
    }

    @Override
    public final Promise<Void> addCloseListener(PromiseListener<Void> listener) {
        return closePromise.addListener(listener);
    }

    @Override
    public final void shutdown() {
        closePromise.trySuccess(null);
        if (channelFuture != null) {
            try {
                channelFuture.channel().close().sync();
            } catch (Throwable e) {
                LOGGER.error("server socket close catch unknown error, error={}", e.getMessage(), e);
            }
            channelFuture = null;
        }
        for (ClusterChannel clusterChannel : getChannels()) {
            try {
                clusterChannel.closeSync();
            } catch (Throwable e) {
                LOGGER.error("socket close catch unknown error, error={}", e.getMessage(), e);
            }
        }

        serverBossGroup.shutdownGracefully();
        serverWorkerGroup.shutdownGracefully();
        clientWorkGroup.shutdownGracefully();
    }

    private void sendLeaderStatusRequest(LeaderStatusRequest leaderStatusRequest) {
        // send leader status request to others
        broadcast(leaderStatusRequest);
        // send leader status request self
        statisticsForLeaderStatus(new LeaderStatusResponse(leaderStatusRequest.getRequestId(), MemberStatus.unreachable));
    }

    private void sendElection(Election election) {
        // send election to others
        broadcast(election);
        // send election to self
        if (election.getState().isFirstState()) {
            voteForFirstStateElection(election);
        } else {
            voteForSecondStateElection(election);
        }
    }

    private Pair<Long, Member> selectMaximumAcceptedSecondStateElection(List<Vote> receivedVotes) {
        long maxTransactionId = -1;
        long maxMemberId = -1;
        Member mostQualifiedCandidate = null;

        for (Vote vote : receivedVotes) {
            long transactionId = vote.getTransactionId();
            Member acceptedCandidate = vote.getCandidate();

            // if member receive first state election when leader is determined
            // then it will vote false with nullable acceptedCandidate
            if (acceptedCandidate == null) {
                continue;
            }
            if (maxTransactionId < transactionId) {
                maxTransactionId = transactionId;
                maxMemberId = acceptedCandidate.getId();
                mostQualifiedCandidate = acceptedCandidate;
            } else if (maxTransactionId == transactionId
                    && maxMemberId < acceptedCandidate.getId()) {
                maxMemberId = acceptedCandidate.getId();
                mostQualifiedCandidate = acceptedCandidate;
            }
        }

        if (mostQualifiedCandidate == null) {
            return null;
        }
        return new ImmutablePair<>(maxTransactionId, mostQualifiedCandidate);
    }

    private void addHandlers(SocketChannel ch, ChannelMode channelMode, Identifier peerIdentifier) {
        ChannelPipeline pipeline = ch.pipeline();
        addLast(pipeline, new IdleStateHandler(0, 0, properties.getIdleTime(), TimeUnit.SECONDS));
        addLast(pipeline, new FrameHandler());
        addLast(pipeline, new FrameAggregatorHandler());
        addLast(pipeline, new FrameChunkedWriteHandler());
        addLast(pipeline, new MessageHandler(this, channelMode, peerIdentifier));
        addLast(pipeline, new SerializeWriteHandler(properties.getProtocolSerializeType()));
    }

    private void addLast(ChannelPipeline pipeline, ChannelHandler handler) {
        Assert.assertNotNull(handler, "handler");
        pipeline.addLast(handler.getClass().getSimpleName(), handler);
    }
}
