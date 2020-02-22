package com.github.liuyehcf.framework.flow.engine.runtime.operation.context;

import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.flow.engine.FlowEngine;
import com.github.liuyehcf.framework.flow.engine.model.Element;
import com.github.liuyehcf.framework.flow.engine.model.Executable;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.Node;
import com.github.liuyehcf.framework.flow.engine.model.activity.Condition;
import com.github.liuyehcf.framework.flow.engine.model.gateway.JoinGateway;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.UnsafeDelegateInvocation;
import com.github.liuyehcf.framework.flow.engine.runtime.operation.AbstractOperation;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionLink;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.Trace;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public interface OperationContext {

    /**
     * get flow engine
     *
     * @return flow engine
     */
    FlowEngine getEngine();

    /**
     * get promise of this flow
     *
     * @return promise
     */
    Promise<ExecutionInstance> getPromise();

    /**
     * get lock of given element, created if not exists
     *
     * @param element element
     * @return lock
     */
    ReentrantLock getElementLock(Element element);

    /**
     * the specified flow
     *
     * @return flow
     */
    Flow getFlow();

    /**
     * whether flow contains only one link
     * flow engine will rely on this flag to do some optimization ( such as env clone )
     */
    boolean isSingleLink();

    /**
     * whether mark context
     */
    boolean isMarkContext();

    /**
     * get execution instance
     *
     * @return execution instance
     */
    ExecutionInstance getExecutionInstance();

    /**
     * get execution link
     *
     * @return execution link
     */
    ExecutionLink getExecutionLink();

    /**
     * get env of current execution link
     *
     * @return env
     */
    Map<String, Object> getLinkEnv();

    /**
     * get current node of current execution link
     *
     * @return node
     */
    Node getNode();

    /**
     * set current node of current execution link
     *
     * @param node node
     */
    void setNode(Node node);

    /**
     * get execution id generator
     *
     * @return execution id generator
     */
    AtomicLong getExecutionIdGenerator();

    /**
     * mark flow finished
     *
     * @return whether success(concurrency)
     */
    boolean markFlowFinished();

    /**
     * mark element finished
     * make sure trace is added when mark element finished
     *
     * @param element finished element
     * @return whether success(concurrency)
     */
    boolean markElementFinished(Element element);

    /**
     * whether element is finished
     *
     * @param element the specified element
     * @return whether finished
     */
    boolean isElementFinished(Element element);

    /**
     * mark node unreachable
     *
     * @param node unreachable node
     */
    void markNodeUnreachable(Node node);

    /**
     * whether node is unreachable
     *
     * @param node the specified node
     * @return whether unreachable
     */
    boolean isNodeUnreachable(Node node);

    /**
     * mark global failure listener finished
     */
    boolean markGlobalFailureListenerFinished();

    /**
     * mark output of specified condition
     */
    void setConditionOutput(Condition condition, boolean output);

    /**
     * get output of specified condition
     *
     * @param condition condition node
     * @return null if condition hasn't executed yet
     */
    Boolean getConditionOutput(Condition condition);

    /**
     * reach gateway
     *
     * @param joinGateway target gateway
     */
    void linkReachesJoinGateway(JoinGateway joinGateway);

    /**
     * whether current execution link reaches joinGateway
     *
     * @param joinGateway join gateway
     * @return whether reaches joinGateway
     */
    boolean isLinkReachedJoinGateway(JoinGateway joinGateway, ExecutionLink link);

    /**
     * get join gateway reach count
     *
     * @param joinGateway target target
     * @return count
     */
    int getJoinGatewayReachesNum(JoinGateway joinGateway);

    /**
     * mark joinGateway finish merge operation
     *
     * @param joinGateway join gateway
     */
    void markJoinGatewayAggregated(JoinGateway joinGateway);

    /**
     * whether joinGateway finish merge operation
     *
     * @param joinGateway join gateway
     * @return whether merged
     */
    boolean isJoinGatewayAggregated(JoinGateway joinGateway);

    /**
     * get delegate invocation which wraps target delegate with delegate interception
     *
     * @param executable target executable
     * @param result     element's execution result(only for listener)
     * @param cause      element's execution exception(only for listener)
     * @return delegate invocation
     */
    UnsafeDelegateInvocation getDelegateInvocation(Executable executable, Object result, Throwable cause);

    /**
     * get unique execution id(within flow)
     *
     * @return execution id
     */
    long getNextExecutionId();

    /**
     * execute operation sync
     *
     * @param operation the specified operation
     */
    void executeSync(AbstractOperation operation);

    /**
     * execute operation async
     *
     * @param operation the specified operation
     */
    void executeAsync(AbstractOperation operation);

    /**
     * add execution trace to current execution link
     *
     * @param trace trace
     */
    void addTraceToExecutionLink(Trace trace);

    /**
     * add execution trace to execution instance
     *
     * @param trace trace
     */
    void addTraceToExecutionInstance(Trace trace);

    /**
     * clone context and add link to instance
     *
     * @param executionLink cloned if null
     * @return cloned context
     */
    OperationContext cloneLinkedContext(ExecutionLink executionLink);

    /**
     * clone context without adding link to instance
     *
     * @return cloned context
     */
    OperationContext cloneUnLinkedContext();

    /**
     * clone context for MarkSuccessorUnreachableOperation
     * env and execution link of this context is null
     *
     * @return cloned context
     */
    OperationContext cloneMarkContext();
}
