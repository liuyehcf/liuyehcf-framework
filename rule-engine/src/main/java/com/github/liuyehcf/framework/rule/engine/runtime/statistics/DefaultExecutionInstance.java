package com.github.liuyehcf.framework.rule.engine.runtime.statistics;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.model.IDGenerator;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
public class DefaultExecutionInstance implements ExecutionInstance {

    private final String id;
    private final Rule rule;
    private final Map<String, Object> env;
    private final List<ExecutionLink> links = Lists.newCopyOnWriteArrayList();
    private final List<ExecutionLink> unreachableLinks = Lists.newCopyOnWriteArrayList();
    private final List<Trace> traces = Lists.newCopyOnWriteArrayList();
    private final Map<String, Attribute> attributes = Maps.newConcurrentMap();
    private final long startNanos = System.nanoTime();
    private long endNanos;

    public DefaultExecutionInstance(String id, Rule rule, Map<String, Object> env) {
        Assert.assertNotNull(rule);
        Assert.assertNotNull(env);
        if (id == null) {
            this.id = IDGenerator.generateUuid();
        } else {
            this.id = id;
        }
        this.rule = rule;
        this.env = env;
    }

    @Override
    public final String getId() {
        return id;
    }

    @Override
    public final Rule getRule() {
        return rule;
    }

    @Override
    public final Map<String, Object> getEnv() {
        return env;
    }

    @Override
    public final void addLink(ExecutionLink link) {
        Assert.assertNotNull(link);
        ((CopyOnWriteArrayList<ExecutionLink>) links).addIfAbsent(link);
    }

    @Override
    public final boolean removeLink(ExecutionLink link) {
        return links.remove(link);
    }

    @Override
    public final List<ExecutionLink> getLinks() {
        return links;
    }

    @Override
    public final void addUnreachableLink(ExecutionLink link) {
        Assert.assertNotNull(link);
        ((CopyOnWriteArrayList<ExecutionLink>) unreachableLinks).addIfAbsent(link);
    }

    @Override
    public final boolean removeUnreachableLink(ExecutionLink link) {
        return unreachableLinks.remove(link);
    }

    @Override
    public final List<ExecutionLink> getUnreachableLinks() {
        return unreachableLinks;
    }

    @Override
    public final void addTrace(Trace trace) {
        traces.add(trace);
    }

    @Override
    public final void removeTrace(Trace trace) {
        traces.remove(trace);
    }

    @Override
    public final List<Trace> getTraces() {
        return traces;
    }

    @Override
    public final Map<String, Attribute> getAttributes() {
        return attributes;
    }

    @Override
    public final long getStartNanos() {
        return startNanos;
    }

    @Override
    public final long getEndNanos() {
        return endNanos;
    }

    @Override
    public final void setEndNanos(long endNanos) {
        this.endNanos = endNanos;
    }
}
