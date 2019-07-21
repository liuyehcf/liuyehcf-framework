package com.github.liuyehcf.framework.rule.engine.runtime.statistics;

import java.util.List;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/5/10
 */
public interface ExecutionLink {

    /**
     * get unique execution link
     *
     * @return execution link
     */
    String getId();

    /**
     * get env of this execution link
     */
    Map<String, Object> getEnv();

    /**
     * add trace to this execution link
     */
    void addTrace(Trace trace);

    /**
     * add traces to this execution link
     */
    void removeTrace(Trace trace);

    /**
     * get traces of this execution link
     */
    List<Trace> getTraces();
}
