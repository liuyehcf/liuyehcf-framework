package com.github.liuyehcf.framework.io.athena.event;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.io.athena.Member;

/**
 * @author hechenfeng
 * @date 2020/2/8
 */
public class JoiningMember implements SystemEvent {

    private final Member member;

    public JoiningMember(Member member) {
        Assert.assertNotNull(member, "member");
        this.member = member.copy();
    }

    public Member getMember() {
        return member;
    }
}
