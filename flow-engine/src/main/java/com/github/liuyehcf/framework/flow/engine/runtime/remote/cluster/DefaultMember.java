package com.github.liuyehcf.framework.flow.engine.runtime.remote.cluster;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class DefaultMember implements Member {

    private final String host;
    private final int port;
    private final long id;
    private final MemberRole role;
    private final MemberStatus status;
    private MemberStatus localStatus;

    DefaultMember(String host, int port, MemberRole role, MemberStatus status) {
        this(host, port, INVALID_ID, role, status);
    }

    private DefaultMember(String host, int port, long id, MemberRole role, MemberStatus status) {
        this.host = host;
        this.port = port;
        this.role = role;
        this.status = status;
        this.localStatus = this.status;
        this.id = id;
    }

    @Override
    public final String getHost() {
        return host;
    }

    @Override
    public final Integer getPort() {
        return port;
    }

    @Override
    public final long getId() {
        return id;
    }

    @Override
    public final MemberRole getRole() {
        return role;
    }

    @Override
    public final MemberStatus getStatus() {
        return status;
    }

    @Override
    public final MemberStatus getLocalStatus() {
        return localStatus;
    }

    @Override
    public final void setLocalStatus(MemberStatus status) {
        this.localStatus = status;
    }

    @Override
    public final Member clone(Long id, MemberRole role, MemberStatus status) {
        return new DefaultMember(host, port,
                id == null ? this.id : id,
                role == null ? this.role : role,
                status == null ? this.status : status);
    }

    @Override
    public String toString() {
        return String.format("Member(identifier='%s', role='%s', status='%s', id='%d')", getIdentifier(), getRole(), getStatus(), getId());
    }
}
