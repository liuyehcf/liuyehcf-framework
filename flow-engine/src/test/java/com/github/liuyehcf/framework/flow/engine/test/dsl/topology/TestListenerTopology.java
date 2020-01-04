package com.github.liuyehcf.framework.flow.engine.test.dsl.topology;

import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.LinkType;
import com.github.liuyehcf.framework.flow.engine.model.Start;
import com.github.liuyehcf.framework.flow.engine.model.activity.Action;
import com.github.liuyehcf.framework.flow.engine.model.listener.Listener;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerEvent;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/7/19
 */
@SuppressWarnings("all")
public class TestListenerTopology extends TestBaseTopology {

    @Test
    public void testListeners() {
        Flow flow = compile("{\n" +
                "    actionA() [listenerA(event=\"before\")]\n" +
                "}");

        Start start = flow.getStart();
        assertStart(start, 1, 0);

        Action actionA = (Action) start.getSuccessors().get(0);
        assertActivity(actionA, LinkType.NORMAL, "actionA", 0, 0, 1, 0, start);

        Listener listenerA = actionA.getListeners().get(0);
        assertListener(listenerA, actionA, "listenerA", ListenerEvent.before, 1);

        assertFlow(flow, LinkType.NORMAL, 0, 1, 1, 0, 3, null);
    }
}
