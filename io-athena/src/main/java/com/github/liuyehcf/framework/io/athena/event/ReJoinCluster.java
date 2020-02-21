package com.github.liuyehcf.framework.io.athena.event;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.io.athena.Member;

/**
 * @author hechenfeng
 * @date 2020/2/10
 */
public class ReJoinCluster implements SystemEvent {

    private final Member leader;
    private final long version;

    public ReJoinCluster(Member leader, long version) {
        Assert.assertNotNull(leader, "leader");
        this.leader = leader.copy();
        this.version = version;
    }

    public Member getLeader() {
        return leader;
    }

    public long getVersion() {
        return version;
    }
}
