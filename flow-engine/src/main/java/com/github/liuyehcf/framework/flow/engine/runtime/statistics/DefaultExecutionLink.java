package com.github.liuyehcf.framework.flow.engine.runtime.statistics;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.flow.engine.model.IDGenerator;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class DefaultExecutionLink implements ExecutionLink {

    private final String id = IDGenerator.generateUuid();
    private final Map<String, Object> env;
    private final List<Trace> traces;

    public DefaultExecutionLink(Map<String, Object> env, List<Trace> traces) {
        Assert.assertTrue(traces instanceof CopyOnWriteArrayList);
        Assert.assertNotNull(env, "env");
        Assert.assertNotNull(traces, "traces");
        this.env = env;
        this.traces = traces;
    }

    public DefaultExecutionLink(Map<String, Object> env) {
        this(env, Lists.newCopyOnWriteArrayList());
    }

    @Override
    public final String getId() {
        return id;
    }

    @Override
    public final Map<String, Object> getEnv() {
        return env;
    }

    @Override
    public final void addTrace(Trace trace) {
        traces.add(trace);
    }

    @Override
    public final void removeTrace(Trace trace) {
        this.traces.remove(trace);
    }

    @Override
    public final List<Trace> getTraces() {
        return traces;
    }
}
