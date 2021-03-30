package com.github.liuyehcf.framework.flow.engine.test.runtime;

import com.github.liuyehcf.framework.common.tools.number.NumberUtils;
import com.github.liuyehcf.framework.flow.engine.ExecutionCondition;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.runtime.DefaultFlowEngine;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ActionContext;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author hechenfeng
 * @date 2020/6/8
 */
public class TestPerformance {

    private static final String DSL = "{\n" +
            "    statisticsAction() {\n" +
            "        join {\n" +
            "            statisticsAction()&,\n" +
            "            statisticsAction()&\n" +
            "        } then {\n" +
            "            statisticsAction()\n" +
            "        }\n" +
            "    }\n" +
            "}";


    @Test
    public void test() {
        DefaultFlowEngine engine = new DefaultFlowEngine(null, Executors.newFixedThreadPool(16), null);

        int seconds = 10;
        long endTimestamp = System.currentTimeMillis() + NumberUtils.THOUSAND * seconds;

        engine.registerActionDelegateFactory("statisticsAction", StatisticsAction::new);

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() -> {
            while (System.currentTimeMillis() < endTimestamp) {
                System.out.println(StatisticsAction.CNT.get());
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Flow flow = engine.compile(DSL).get();

        while (System.currentTimeMillis() < endTimestamp) {
            engine.startFlow(new ExecutionCondition(flow));
        }

        System.out.println(StatisticsAction.CNT.get() / 4 / seconds);
    }

    private static final class StatisticsAction implements ActionDelegate {

        private static final AtomicLong CNT = new AtomicLong();

        @Override
        public void onAction(ActionContext context) {
            CNT.incrementAndGet();
        }
    }
}
