package com.github.liuyehcf.framework.flow.engine.runtime.remote.io;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author hechenfeng
 * @date 2019/9/8
 */
abstract class BaseScheduler {

    static Scheduler scheduler;

    static {
        init();
    }

    private static void init() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
