package com.github.liuyehcf.framework.io.athena;

import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2020/2/7
 */
public class Member extends Identifier {

    private final boolean isSeed;
    private volatile MemberRole role;
    private volatile MemberStatus status;

    public Member(Member member) {
        this(member.getName(), member.getHost(), member.getPort(), member.isSeed, member.role, member.status);
    }

    public Member(String name, String host, int port, boolean isSeed) {
        this(name, host, port, isSeed, MemberRole.follower, MemberStatus.init);
    }

    public Member(String name, String host, int port, boolean isSeed, MemberRole role, MemberStatus status) {
        super(name, host, port);
        this.isSeed = isSeed;
        this.role = role;
        this.status = status;
    }

    public static boolean isValidLeader(Member member) {
        return member != null
                && Objects.equals(MemberRole.leader, member.getRole())
                && Objects.equals(MemberStatus.active, member.getStatus());
    }

    public boolean isSeed() {
        return isSeed;
    }

    public MemberRole getRole() {
        return role;
    }

    void setRole(MemberRole role) {
        this.role = role;
    }

    public MemberStatus getStatus() {
        return status;
    }

    void setStatus(MemberStatus status) {
        this.status = status;
    }

    public Member copy() {
        return new Member(this);
    }

    public String toReadableString() {
        return String.format("%s/%b/%s/%s", this, isSeed(), getRole(), getStatus());
    }
}
