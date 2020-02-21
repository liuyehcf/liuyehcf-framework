package com.github.liuyehcf.framework.io.athena;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.codec.digest.DigestUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author hechenfeng
 * @date 2020/2/7
 */
public class Cluster implements AtomicExecutor {

    /**
     * self member
     */
    private final Member self;

    /**
     * all the members of the cluster, including leader, followers
     */
    private final Map<Address, Member> members = Maps.newConcurrentMap();

    /**
     * version of cluster
     */
    private long version = 0;

    /**
     * leader of the cluster
     */
    private Member leader;

    /**
     * abstract info of the cluster
     * containing all information of the cluster
     */
    private volatile String abstractInfo;

    public Cluster(Member self) {
        this.self = self;
        members.put(this.self, this.self);
    }

    @Override
    public final synchronized void atomicOperate(Operation operation) {
        try {
            operation.operate();
        } catch (Throwable e) {
            throw new AthenaException(e);
        }
    }

    public Member getSelf() {
        return self;
    }

    public Member getLeader() {
        return leader;
    }

    @Atomic
    public void setLeader(Member leader) {
        this.leader = leader;
        cleanup();
    }

    public List<Member> getMembers() {
        return Collections.unmodifiableList(Lists.newArrayList(members.values()));
    }

    public Member getMember(Address address) {
        return members.get(address);
    }

    public boolean hasMember(Member member) {
        return members.containsKey(member);
    }

    public long getVersion() {
        return version;
    }

    @Atomic
    public void setVersion(long version) {
        this.version = version;
        cleanup();
    }

    private void cleanup() {
        abstractInfo = null;
    }

    public String getAbstractInfo() {
        String abstractInfo = this.abstractInfo;

        if (abstractInfo != null) {
            return abstractInfo;
        }

        synchronized (this) {
            String content = String.format("version[%d]/leader[%s]/members(%s)", version, leader, getMembers()
                    .stream()
                    .sorted()
                    .map(Member::toReadableString)
                    .collect(Collectors.toList()));

            this.abstractInfo = DigestUtils.md5Hex(content);
            abstractInfo = this.abstractInfo;
        }

        return abstractInfo;
    }

    @Atomic
    public boolean addMember(Member member) {
        if (!Objects.equals(self, member)
                && members.putIfAbsent(member, member) == null) {
            cleanup();
            return true;
        }
        return false;
    }

    @Atomic
    public boolean removeMember(Member member) {
        if (!Objects.equals(self, member)
                && members.remove(member) != null) {
            cleanup();
            return true;
        }
        return false;
    }

    @Atomic
    public boolean updateMemberStatus(Address address, MemberStatus memberStatus) {
        Member member = members.get(address);
        if (member != null && !member.getStatus().equals(memberStatus)) {
            member.setStatus(memberStatus);
            cleanup();
            return true;
        }
        return false;
    }

    @Atomic
    public boolean updateMemberRole(Address address, MemberRole memberRole) {
        Member member = members.get(address);
        if (member != null && !member.getRole().equals(memberRole)) {
            member.setRole(memberRole);
            cleanup();
            return true;
        }
        return false;
    }

    @Atomic
    public void reset() {
        self.setRole(MemberRole.follower);
        self.setStatus(MemberStatus.init);
        version = 0;
        members.clear();
        members.put(self, self);
        leader = null;
        cleanup();
    }

    public String toReadableString() {
        return String.format("version[%d]/self[%s]/leader[%s]/members(%s)", version, self, leader, getMembers()
                .stream()
                .sorted()
                .map(Member::toReadableString)
                .collect(Collectors.toList()));
    }

    /**
     * this annotation is just for your attention
     * method with this annotation should be wrapped with atomicOperate
     * like @Override annotation
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Atomic {
    }
}
