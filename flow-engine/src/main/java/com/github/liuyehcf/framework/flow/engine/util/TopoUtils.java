package com.github.liuyehcf.framework.flow.engine.util;

import com.github.liuyehcf.framework.flow.engine.model.ElementType;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.LinkType;
import com.github.liuyehcf.framework.flow.engine.model.Node;
import com.github.liuyehcf.framework.flow.engine.model.activity.Condition;
import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2019/10/24
 */
public abstract class TopoUtils {

    public static boolean isSingleLinkFlow(Flow flow) {
        boolean hasGlobalListener = flow.getListeners().stream()
                .anyMatch(listener -> listener.getScope().isGlobal());

        if (hasGlobalListener) {
            return false;
        }

        boolean hasSubFlow = flow.getElements().stream()
                .anyMatch(element -> Objects.equals(ElementType.SUB_FLOW, element.getType()));
        if (hasSubFlow) {
            return false;
        }

        LinkedList<Node> stack = Lists.newLinkedList();
        stack.push(flow.getStart());

        while (!stack.isEmpty()) {
            Node node = stack.pop();

            List<Node> successors = node.getSuccessors();

            if (node instanceof Condition) {
                List<Node> trueSuccessors = node.getSuccessors(LinkType.TRUE);

                List<Node> falseSuccessors = node.getSuccessors(LinkType.FALSE);

                if (trueSuccessors.size() > 1 || falseSuccessors.size() > 1) {
                    return false;
                }

                if (!trueSuccessors.isEmpty()) {
                    stack.push(trueSuccessors.get(0));
                }

                if (!falseSuccessors.isEmpty()) {
                    stack.push(falseSuccessors.get(0));
                }
            } else {
                if (successors.size() > 1) {
                    return false;
                }

                if (!successors.isEmpty()) {
                    stack.push(successors.get(0));
                }
            }
        }

        return true;
    }
}
