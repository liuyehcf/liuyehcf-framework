package com.github.liuyehcf.framework.io.athena.event;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.io.athena.Cluster;
import com.github.liuyehcf.framework.io.athena.Member;

/**
 * @author hechenfeng
 * @date 2020/2/10
 */
public class SeedKeepAlive implements SystemEvent {

    private final Member leader;
    private final long version;

    public SeedKeepAlive(Cluster cluster) {
        Assert.assertNotNull(cluster, "cluster");
        Member leader = cluster.getLeader();
        if (leader != null) {
            this.leader = leader.copy();
        } else {
            this.leader = null;
        }
        this.version = cluster.getVersion();
    }

    public Member getLeader() {
        return leader;
    }

    public long getVersion() {
        return version;
    }
}
