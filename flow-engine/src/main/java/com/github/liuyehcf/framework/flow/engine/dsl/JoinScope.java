package com.github.liuyehcf.framework.flow.engine.dsl;

import com.github.liuyehcf.framework.flow.engine.model.LinkType;
import com.github.liuyehcf.framework.flow.engine.model.Node;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/4/26
 */
class JoinScope {

    private final List<Pair<Node, LinkType>> joinNodes = Lists.newArrayList();

    List<Pair<Node, LinkType>> getJoinNodes() {
        return joinNodes;
    }

    void addJoinNode(Node node, LinkType linkType) {
        joinNodes.add(new ImmutablePair<>(node, linkType));
    }
}
