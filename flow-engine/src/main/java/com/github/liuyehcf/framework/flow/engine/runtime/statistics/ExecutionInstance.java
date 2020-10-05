package com.github.liuyehcf.framework.flow.engine.runtime.statistics;

import com.github.liuyehcf.framework.flow.engine.model.Flow;

import java.util.List;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/5/10
 */
public interface ExecutionInstance {

    /**
     * unique instanceId
     */
    String getId();

    /**
     * get bound flow of this execution
     */
    Flow getFlow();

    /**
     * get env
     * If there are multiple execution links, then all envs will be aggregated
     */
    Map<String, Object> getEnv();

    /**
     * add execution link to this execution instance
     */
    void addLink(ExecutionLink link);

    /**
     * remove execution link from execution instance
     */
    boolean removeLink(ExecutionLink link);

    /**
     * get all execution links of this flow
     * including all finished links and all unfinished links
     */
    List<ExecutionLink> getLinks();

    /**
     * add unreachable link to execution instance
     */
    void addUnreachableLink(ExecutionLink link);

    /**
     * remove unreachable link
     */
    boolean removeUnreachableLink(ExecutionLink link);

    /**
     * get all unfinished execution links of this flow
     * unfinished execution means execution cannot reach end node
     */
    List<ExecutionLink> getUnreachableLinks();

    /**
     * add trace of execution instance
     * contains only global level listeners
     */
    void addTrace(Trace trace);

    /**
     * remove trace of execution instance
     * contains only global level listeners
     */
    void removeTrace(Trace trace);

    /**
     * get traces of execution instance
     * contains only global level listeners
     */
    List<Trace> getTraces();

    /**
     * attributes
     */
    Map<String, Attribute> getAttributes();

    /**
     * get start timestamp of this execution
     */
    long getStartTimestamp();

    /**
     * get end timestamp of this execution
     */
    long getEndTimestamp();

    /**
     * use time in nano seconds
     */
    long getUseTimeNanos();
}
