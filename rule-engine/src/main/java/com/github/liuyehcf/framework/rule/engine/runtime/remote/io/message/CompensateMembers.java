package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message;

import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.Member;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/9/13
 */
public class CompensateMembers implements Message {

    private List<Member> members;

    public CompensateMembers(List<Member> members) {
        this.members = members;
    }

    @Override
    public final MessageType getType() {
        return MessageType.COMPENSATE_MEMBERS;
    }

    public List<Member> getMembers() {
        return members;
    }
}
