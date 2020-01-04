package com.github.liuyehcf.framework.flow.engine.promise;

import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
public interface Promise<T> {

    /**
     * Returns {@code true} if this task was cancelled before it completed
     * normally.
     *
     * @return {@code true} if this task was cancelled before it completed
     */
    boolean isCancelled();

    /**
     * Returns {@code true} if this task completed.
     * Both success or failure
     *
     * @return {@code true} if this task completed
     */
    boolean isDone();

    /**
     * Returns {@code true} if this task succeeded.
     *
     * @return {@code true} if this task succeeded
     */
    boolean isSuccess();

    /**
     * Returns {@code true} if this task failed.
     *
     * @return {@code true} if this task failed
     */
    boolean isFailure();

    /**
     * Returns the cause of the failure
     *
     * @return the cause of the failure
     */
    Throwable cause();

    /**
     * Attempts to cancel execution of this flow.
     *
     * @return {@code false} if the task is already completed
     * {@code true} otherwise
     */
    boolean tryCancel();

    /**
     * Marks this promise as a success and notifies all
     * listeners if this promise isn't done
     */
    boolean trySuccess(T outcome);

    /**
     * Marks this promise as a failure and notifies all
     * listeners if this promise isn't done
     */
    boolean tryFailure(Throwable cause);

    /**
     * Add the specified listener to this promise.
     *
     * @param listener listener
     */
    Promise<T> addListener(PromiseListener<T> listener);

    /**
     * Waits for this promise until it is done, and rethrows the cause of the failure if this promise
     * failed.
     */
    void sync();

    /**
     * Waits for this promise to be completed within the
     * specified time limit.
     *
     * @return {@code true} if and only if the promise was completed within
     * the specified time limit
     */
    boolean await(long timeout, TimeUnit unit);

    /**
     * Waits if necessary for the computation to complete, and then
     * retrieves its result.
     *
     * @return ExecutionInstance
     */
    T get();

    /**
     * Waits if necessary for at most the given time for the computation
     * to complete, and then retrieves its result, if available.
     *
     * @return {@code true} if and only if the promise was completed within
     * the specified time limit
     */
    T get(long timeout, TimeUnit unit);
}
