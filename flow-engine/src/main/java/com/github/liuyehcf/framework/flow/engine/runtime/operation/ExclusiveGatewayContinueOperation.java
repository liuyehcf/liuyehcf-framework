package com.github.liuyehcf.framework.flow.engine.runtime.operation;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.common.tools.promise.PromiseListener;
import com.github.liuyehcf.framework.flow.engine.FlowErrorCode;
import com.github.liuyehcf.framework.flow.engine.FlowException;
import com.github.liuyehcf.framework.flow.engine.model.ElementType;
import com.github.liuyehcf.framework.flow.engine.model.LinkType;
import com.github.liuyehcf.framework.flow.engine.model.Node;
import com.github.liuyehcf.framework.flow.engine.model.activity.Condition;
import com.github.liuyehcf.framework.flow.engine.model.gateway.ExclusiveGateway;
import com.github.liuyehcf.framework.flow.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.flow.engine.runtime.operation.promise.ConditionPromise;

/**
 * @author hechenfeng
 * @date 2019/7/4
 */
class ExclusiveGatewayContinueOperation extends AbstractOperation<Void> implements PromiseListener<Boolean> {

    private final ExclusiveGateway exclusiveGateway;
    private final int indexOfSuccessor;
    private volatile Promise<Boolean> promise;
    private volatile boolean isInListenerLoop = false;

    ExclusiveGatewayContinueOperation(OperationContext context, ExclusiveGateway exclusiveGateway, int indexOfSuccessor) {
        super(context);
        this.exclusiveGateway = exclusiveGateway;
        this.indexOfSuccessor = indexOfSuccessor;
    }

    @Override
    public void operationComplete(Promise<Boolean> promise) {
        isInListenerLoop = true;
        this.run();
    }

    @Override
    void operate() throws Throwable {
        context.setNode(exclusiveGateway);

        if (!isInListenerLoop) {
            Node node = exclusiveGateway.getSuccessors().get(indexOfSuccessor);

            if (!(node instanceof Condition)) {
                throw new FlowException(FlowErrorCode.EXCLUSIVE_GATEWAY,
                        String.format("the type of %s's successor must be %s",
                                ElementType.EXCLUSIVE_GATEWAY.getType(),
                                ElementType.CONDITION.getType()));
            }

            Condition condition = (Condition) node;
            promise = new ConditionPromise();

            context.executeAsync(new ConditionOperation(context.cloneLinkedContext(null), condition, promise));

            promise.addListener(this);
        } else {
            // regard link terminate exception as false output
            boolean output;
            if (promise.isFailure()) {
                if (isLinkTerminateException(promise.cause())) {
                    output = false;
                } else if (promise.cause() != null) {
                    throw promise.cause();
                } else {
                    throw new FlowException(FlowErrorCode.PROMISE, "promise failed");
                }
            } else {
                Assert.assertTrue(promise.isSuccess());
                output = promise.get();
            }

            if (output) {
                // unReach all the remaining condition
                int index = indexOfSuccessor + 1;
                while (index < exclusiveGateway.getSuccessors().size()) {
                    Node node = exclusiveGateway.getSuccessors().get(index);

                    if (!(node instanceof Condition)) {
                        throw new FlowException(FlowErrorCode.EXCLUSIVE_GATEWAY,
                                String.format("the type of %s's successor must be %s",
                                        ElementType.EXCLUSIVE_GATEWAY.getType(),
                                        ElementType.CONDITION.getType()));
                    }

                    Condition condition = (Condition) node;

                    // echo with JoinGatewayMergeOperation#isSoftModeReached
                    context.markNodeUnreachable(condition);

                    context.executeAsync(new MarkSuccessorUnreachableOperation(context.cloneMarkContext(), condition, LinkType.TRUE));
                    context.executeAsync(new MarkSuccessorUnreachableOperation(context.cloneMarkContext(), condition, LinkType.FALSE));

                    index++;
                }
            } else {
                // try next selection branch
                if (indexOfSuccessor + 1 < exclusiveGateway.getSuccessors().size()) {
                    context.executeAsync(new ExclusiveGatewayContinueOperation(context, exclusiveGateway, indexOfSuccessor + 1));
                }
            }
        }
    }
}
