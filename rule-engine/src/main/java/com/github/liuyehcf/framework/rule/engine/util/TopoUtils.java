package com.github.liuyehcf.framework.rule.engine.util;

import com.github.liuyehcf.framework.rule.engine.model.ElementType;
import com.github.liuyehcf.framework.rule.engine.model.Node;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.model.activity.Condition;
import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author hechenfeng
 * @date 2019/10/24
 */
public abstract class TopoUtils {

    public static boolean isSingleLinkRule(Rule rule) {
        boolean hasGlobalListener = rule.getListeners().stream()
                .anyMatch(listener -> listener.getScope().isGlobal());

        if (hasGlobalListener) {
            return false;
        }

        boolean hasSubRule = rule.getElements().stream()
                .anyMatch(element -> Objects.equals(ElementType.SUB_RULE, element.getType()));
        if (hasSubRule) {
            return false;
        }

        LinkedList<Node> stack = Lists.newLinkedList();
        stack.push(rule.getStart());

        while (!stack.isEmpty()) {
            Node node = stack.pop();

            List<Node> successors = node.getSuccessors();

            if (node instanceof Condition) {
                List<Node> trueSuccessors = successors.stream()
                        .filter((successor) -> successor.getLinkType().isTrue())
                        .collect(Collectors.toList());

                List<Node> falseSuccessors = successors.stream()
                        .filter((successor) -> successor.getLinkType().isFalse())
                        .collect(Collectors.toList());

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
