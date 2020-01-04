package com.github.liuyehcf.framework.flow.engine.runtime.remote.io.message;

import com.github.liuyehcf.framework.flow.engine.runtime.remote.cluster.Member;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class JoiningRequest implements Message {

    private final Member member;
    private final boolean isRelayed;

    public JoiningRequest(Member member, boolean isRelayed) {
        this.member = member;
        this.isRelayed = isRelayed;
    }

    @Override
    public final MessageType getType() {
        return MessageType.JOINING;
    }

    public Member getMember() {
        return member;
    }

    public boolean isRelayed() {
        return isRelayed;
    }
}
