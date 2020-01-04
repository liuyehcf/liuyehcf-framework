package com.github.liuyehcf.framework.flow.engine;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.Attribute;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ExecutionCondition {

    /**
     * if flow is null, then dsl will be used
     * one of dsl or flow is required
     */
    private String dsl;

    /**
     * flow to be executed
     * one of dsl or flow is required
     */
    private Flow flow;

    /**
     * specified instanceId
     * optional
     */
    private String instanceId;

    /**
     * env of execution
     * optional
     */
    private Map<String, Object> env;

    /**
     * instance level attributes
     * optional
     */
    private List<Attribute> attributes;

    /**
     * specified executionIdGenerator
     * optional
     */
    private AtomicLong executionIdGenerator;

    public ExecutionCondition(String dsl) {
        Assert.assertNotNull(dsl, "dsl");
        this.dsl = dsl;
    }

    public ExecutionCondition(Flow flow) {
        Assert.assertNotNull(flow, "flow");
        this.flow = flow;
    }

    public ExecutionCondition instanceId(String instanceId) {
        this.instanceId = instanceId;
        return this;
    }

    public ExecutionCondition env(Map<String, Object> env) {
        this.env = env;
        return this;
    }

    public ExecutionCondition attributes(List<Attribute> attributes) {
        this.attributes = attributes;
        return this;
    }

    public String getDsl() {
        return dsl;
    }

    public Flow getFlow() {
        return flow;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public AtomicLong getExecutionIdGenerator() {
        return executionIdGenerator;
    }

    public Map<String, Object> getEnv() {
        return env;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public ExecutionCondition executionIdGenerator(AtomicLong executionIdGenerator) {
        this.executionIdGenerator = executionIdGenerator;
        return this;
    }
}
