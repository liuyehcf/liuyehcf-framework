package com.github.liuyehcf.framework.io.athena.event;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.io.athena.Identifier;
import com.github.liuyehcf.framework.io.athena.Member;

/**
 * @author hechenfeng
 * @date 2020/2/7
 */
public class JoinClusterRequest implements SystemEvent {

    private final Identifier identifier;
    private final boolean isSeed;

    public JoinClusterRequest(Member member) {
        Assert.assertNotNull(member, "member");
        this.identifier = member.copy();
        this.isSeed = member.isSeed();
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public boolean isSeed() {
        return isSeed;
    }
}

