package com.github.liuyehcf.framework.io.athena.event;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.io.athena.Address;
import com.github.liuyehcf.framework.io.athena.Cluster;
import com.github.liuyehcf.framework.io.athena.Member;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hechenfeng
 * @date 2020/2/9
 */
public class ClusterAlignment implements SystemEvent {

    private final Member leader;
    private final Map<Address, Member> members;
    private final long version;

    public ClusterAlignment(Cluster cluster) {
        Assert.assertNotNull(cluster, "cluster");
        Assert.assertNotNull(cluster.getLeader(), "leader");
        this.leader = cluster.getLeader().copy();
        this.members = cluster.getMembers()
                .stream()
                .map(Member::copy)
                .collect(Collectors.toMap(m -> m, m -> m));
        this.version = cluster.getVersion();
    }

    public Member getLeader() {
        return leader;
    }

    public Map<Address, Member> getMembers() {
        return members;
    }

    public long getVersion() {
        return version;
    }

    public String toReadableString() {
        return String.format("version[%d]/leader[%s]/members(%s)", version, leader, members.values()
                .stream()
                .sorted()
                .map(Member::toReadableString)
                .collect(Collectors.toList()));
    }
}
