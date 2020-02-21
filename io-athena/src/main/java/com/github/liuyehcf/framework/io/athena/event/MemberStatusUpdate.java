package com.github.liuyehcf.framework.io.athena.event;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.io.athena.Identifier;
import com.github.liuyehcf.framework.io.athena.MemberStatus;

/**
 * @author hechenfeng
 * @date 2020/2/8
 */
public class MemberStatusUpdate implements SystemEvent {

    private final Identifier identifier;
    private final MemberStatus status;

    public MemberStatusUpdate(Identifier identifier, MemberStatus status) {
        Assert.assertNotNull(identifier, "identifier");
        Assert.assertNotNull(status, "status");
        this.identifier = identifier.copy();
        this.status = status;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public MemberStatus getStatus() {
        return status;
    }
}
