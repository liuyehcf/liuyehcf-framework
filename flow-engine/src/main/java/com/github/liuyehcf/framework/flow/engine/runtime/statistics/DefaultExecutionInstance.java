package com.github.liuyehcf.framework.flow.engine.runtime.statistics;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.IDGenerator;
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
    private final Flow flow;
    private final Map<String, Object> env;
    private final List<ExecutionLink> links = Lists.newCopyOnWriteArrayList();
    private final List<ExecutionLink> unreachableLinks = Lists.newCopyOnWriteArrayList();
    private final List<Trace> traces = Lists.newCopyOnWriteArrayList();
    private final Map<String, Attribute> attributes = Maps.newConcurrentMap();
    private final long startTimestamp = System.currentTimeMillis();
    private final long startNano = System.nanoTime();
    private long endTimestamp;
    private long endNano;

    public DefaultExecutionInstance(String id, Flow flow, Map<String, Object> env, List<Attribute> attributes) {
        Assert.assertNotNull(flow, "flow");
        Assert.assertNotNull(env, "env");
        if (id == null) {
            this.id = IDGenerator.generateUuid();
        } else {
            this.id = id;
        }
        this.flow = flow;
        this.env = env;
        if (attributes != null) {
            for (Attribute attribute : attributes) {
                if (attribute != null) {
                    this.attributes.put(attribute.getName(), attribute);
                }
            }
        }
    }

    @Override
    public final String getId() {
        return id;
    }

    @Override
    public final Flow getFlow() {
        return flow;
    }

    @Override
    public final Map<String, Object> getEnv() {
        return env;
    }

    @Override
    public final void addLink(ExecutionLink link) {
        Assert.assertNotNull(link, "link");
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
        Assert.assertNotNull(link, "link");
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
    public final long getStartTimestamp() {
        return startTimestamp;
    }

    @Override
    public final long getEndTimestamp() {
        return endTimestamp;
    }

    @Override
    public final long getUseTimeNano() {
        return endNano - startNano;
    }

    public final void setEndTime() {
        this.endNano = System.nanoTime();
        this.endTimestamp = System.currentTimeMillis();
    }
}
