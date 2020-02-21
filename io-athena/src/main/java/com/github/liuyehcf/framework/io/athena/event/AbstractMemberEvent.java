package com.github.liuyehcf.framework.io.athena.event;

import com.github.liuyehcf.framework.io.athena.Member;

/**
 * @author hechenfeng
 * @date 2020/2/5
 */
public abstract class AbstractMemberEvent {

    private final Member member;

    public AbstractMemberEvent(Member member) {
        this.member = new Member(member);
    }

    public Member member() {
        return member;
    }
}
