package com.github.liuyehcf.framework.rule.engine.dsl;

import com.github.liuyehcf.framework.rule.engine.model.Node;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
class JoinScope {
    private final List<Node> joinNodes = Lists.newArrayList();

    List<Node> getJoinNodes() {
        return joinNodes;
    }

    void addJoinNode(Node node) {
        joinNodes.add(node);
    }
}
