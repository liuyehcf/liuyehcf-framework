package com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.codec.digest.DigestUtils;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author hechenfeng
 * @date 2019/9/5
 */
public class DefaultTopology implements Topology {

    private final MemberConfig selfConfig;
    private final Map<String, Member> members = Maps.newHashMap();
    private final AtomicLong memberIdCnt = new AtomicLong(0L);
    private final AtomicLong transactionId = new AtomicLong(0L);
    private volatile boolean isLeaderValid = false;
    private volatile Member leader;
    private volatile boolean isIdentifierValid = false;
    private volatile String identifier;

    public DefaultTopology(MemberConfig selfConfig) {
        Assert.assertNotNull(selfConfig, "selfConfig");
        this.selfConfig = selfConfig;

        addOrReplaceMember(
                new DefaultMember(
                        selfConfig.getHost(),
                        selfConfig.getPort(),
                        MemberRole.follower,
                        MemberStatus.init
                )
        );
    }

    @Override
    public final Member getLeader() {
        if (isLeaderValid) {
            return leader;
        }
        synchronized (this) {
            if (!isLeaderValid) {
                initLeader();
            }
        }
        return leader;
    }

    @Override
    public final boolean isSelfLeader() {
        Member leader;
        return (leader = getLeader()) != null
                && Identifier.equals(leader, selfConfig);
    }

    @Override
    public final boolean isLeader(Identifier identifier) {
        Member leader;
        return (leader = getLeader()) != null
                && Identifier.equals(leader, identifier);
    }

    @Override
    public final boolean isSelf(Identifier identifier) {
        return Identifier.equals(selfConfig, identifier);
    }

    @Override
    public final Member getSelf() {
        return getMember(selfConfig);
    }

    @Override
    public final boolean hasMember(Identifier identifier) {
        return members.containsKey(identifier.getIdentifier());
    }

    @Override
    public final Member getMember(Identifier identifier) {
        return members.get(identifier.getIdentifier());
    }

    @Override
    public synchronized final void addMemberIfAbsent(Member member) {
        Member exist = members.putIfAbsent(member.getIdentifier(), member);
        if (exist == null) {
            member.setLocalStatus(member.getStatus());
        }
        isLeaderValid = false;
        isIdentifierValid = false;
    }

    @Override
    public synchronized final void addOrReplaceMember(Member member) {
        members.put(member.getIdentifier(), member);
        member.setLocalStatus(member.getStatus());
        isLeaderValid = false;
        isIdentifierValid = false;
    }

    @Override
    public final int activeNum() {
        int activeNum = 0;
        for (Member member : members.values()) {
            if (member.getStatus().isActive()
                    && !member.getLocalStatus().isUnreachable()) {
                activeNum++;
            }
        }
        return activeNum;
    }

    @Override
    public final long getMemberIdCnt() {
        return memberIdCnt.get();
    }

    @Override
    public final long generateNextMemberId() {
        isIdentifierValid = false;
        return memberIdCnt.incrementAndGet();
    }

    @Override
    public final long getTransactionId() {
        return transactionId.get();
    }

    @Override
    public final void increaseTransactionId(long expectedTransactionId) {
        compareAndSetTransactionId(expectedTransactionId, expectedTransactionId + 1);
    }

    @Override
    public boolean assumeLeader(long expectedTransactionId) {
        return assumeLeader(expectedTransactionId, expectedTransactionId + 1);
    }

    @Override
    public final boolean assumeLeader(long expectedTransactionId, long updateTransactionId) {
        if (compareAndSetTransactionId(expectedTransactionId, updateTransactionId)) {
            synchronized (this) {
                guaranteeNoOtherLeader();
                addOrReplaceMember(getSelf().clone(generateNextMemberId(), MemberRole.leader, MemberStatus.active));
                guaranteeIdCntGreaterThanAnyMemberId();
                guaranteeMemberIdUnique();
                isLeaderValid = false;
                isIdentifierValid = false;
            }
            return true;
        }
        return false;
    }

    @Override
    public synchronized final void syncFrom(Topology otherTopology) {
        for (Member member : members.values()) {
            if (!otherTopology.hasMember(member)) {
                addOrReplaceMember(member.clone(null, MemberRole.follower, MemberStatus.unreachable));
            }
        }

        for (Member member : otherTopology) {
            // strong correction regardless of version size
            // different from member status spread process(gossip protocol)
            addOrReplaceMember(member);
        }

        this.transactionId.set(otherTopology.getTransactionId());
        this.memberIdCnt.set(otherTopology.getMemberIdCnt());

        isLeaderValid = false;
        isIdentifierValid = false;
    }

    @Override
    public final String getIdentifier() {
        if (isIdentifierValid) {
            return identifier;
        }
        synchronized (this) {
            if (!isIdentifierValid) {
                initIdentifier();
            }
        }
        return identifier;
    }

    @Override
    @Nonnull
    public final Iterator<Member> iterator() {
        return members.values().iterator();
    }

    private boolean compareAndSetTransactionId(long expectedTransactionId, long updateTransactionId) {
        if (this.transactionId.compareAndSet(expectedTransactionId, updateTransactionId)) {
            isIdentifierValid = false;
            return true;
        }
        return false;
    }

    private void guaranteeNoOtherLeader() {
        List<Member> otherLeaders = Lists.newArrayList();
        for (Member member : members.values()) {
            if (!isSelf(member) && member.getRole().isLeader()) {
                otherLeaders.add(member);
            }
        }
        for (Member otherLeader : otherLeaders) {
            addOrReplaceMember(otherLeader.clone(generateNextMemberId(), MemberRole.follower, null));
        }
    }

    private void guaranteeIdCntGreaterThanAnyMemberId() {
        // guarantee member id cnt big enough
        for (Member member : members.values()) {
            if (memberIdCnt.get() < member.getId()) {
                memberIdCnt.compareAndSet(memberIdCnt.get(), member.getId());
            }
        }
    }

    private void guaranteeMemberIdUnique() {
        Set<Long> ids = Sets.newHashSet();
        Set<String> wrongIdentifiers = Sets.newHashSet();
        for (Map.Entry<String, Member> entry : members.entrySet()) {
            String identifier = entry.getKey();
            Member member = entry.getValue();

            if (!ids.add(member.getId())) {
                wrongIdentifiers.add(identifier);
            }
        }
        for (String identifier : wrongIdentifiers) {
            Member member = members.get(identifier);
            addOrReplaceMember(member.clone(generateNextMemberId(), null, null));
        }
    }

    private void initLeader() {
        for (Member member : members.values()) {
            if (member.getRole().isLeader()) {
                leader = member;
                isLeaderValid = true;
                return;
            }
        }
        isLeaderValid = true;
    }

    private void initIdentifier() {
        List<Member> members = Lists.newArrayList(this.members.values());
        members.sort(Comparator.comparing(Member::getId));

        StringBuilder sb = new StringBuilder();

        appendMember(sb, getLeader());

        for (Member member : members) {
            appendMember(sb, member);
        }

        sb.append('/').append(memberIdCnt)
                .append('/').append(transactionId);

        identifier = DigestUtils.md5Hex(sb.toString());
        isIdentifierValid = true;
    }

    private void appendMember(StringBuilder sb, Member member) {
        if (member == null) {
            sb.append("missing");
        } else {
            sb.append(member.getIdentifier())
                    .append('/').append(member.getRole().name())
                    .append('/').append(member.getStatus().name())
                    .append('/').append(member.getId())
                    .append(',');
        }
    }
}
