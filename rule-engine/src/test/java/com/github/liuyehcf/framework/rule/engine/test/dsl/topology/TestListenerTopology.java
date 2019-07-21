package com.github.liuyehcf.framework.rule.engine.test.dsl.topology;

import com.github.liuyehcf.framework.rule.engine.model.LinkType;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.model.Start;
import com.github.liuyehcf.framework.rule.engine.model.activity.Action;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/7/19
 */
@SuppressWarnings("all")
public class TestListenerTopology extends TestBaseTopology {

    @Test
    public void testListeners() {
        Rule rule = compile("{\n" +
                "    actionA() [listenerA(event=\"before\")]\n" +
                "}");

        Start start = rule.getStart();
        assertStart(start, 1, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 0, 1, 0, start);

        Listener listenerA = actionA.getListeners().get(0);
        assertListener(listenerA, actionA, "listenerA", ListenerEvent.before, 1);

        assertRule(rule, LinkType.NORMAL, 0, 1, 1, 0, 3, null);
    }
}
