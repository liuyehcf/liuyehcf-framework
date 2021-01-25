package com.github.liuyehcf.framework.common.tools.promise;

import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
public interface Promise<T> {

    /**
     * returns {@code true} if this task was cancelled before it completed
     *
     * @return {@code true} if this task was cancelled before it completed
     */
    boolean isCancelled();

    /**
     * returns {@code true} if this task completed
     * success, failure and cancellation are all complete
     *
     * @return {@code true} if this task completed
     */
    boolean isDone();

    /**
     * returns {@code true} if this task succeeded
     *
     * @return {@code true} if this task succeeded
     */
    boolean isSuccess();

    /**
     * returns {@code true} if this task failed
     *
     * @return {@code true} if this task failed
     */
    boolean isFailure();

    /**
     * returns the cause of the failure
     *
     * @return the cause of the failure
     */
    Throwable cause();

    /**
     * attempts to cancel execution of this task and notifies all
     * listeners if this promise isn't done
     *
     * @return {@code false} if the task is already completed
     * {@code true} otherwise
     */
    boolean tryCancel();

    /**
     * marks this promise as a success and notifies all
     * listeners if this promise isn't done
     *
     * @return {@code false} if the task is already completed
     * {@code true} otherwise
     */
    boolean trySuccess(T outcome);

    /**
     * marks this promise as a failure and notifies all
     * listeners if this promise isn't done
     */
    boolean tryFailure(Throwable cause);

    /**
     * adds the specified listener to this promise
     *
     * @param listener listener
     */
    Promise<T> addListener(PromiseListener<T> listener);

    /**
     * waits for this promise until it is done, and rethrows the cause of the failure if this promise
     * this method can be interrupted, but the {@link InterruptedException} was wrapped in outer Exception
     */
    void sync();

    /**
     * waits for this promise to be completed within the specified time limit
     * this method can be interrupted, but the {@link InterruptedException} was wrapped in outer Exception
     *
     * @return {@code true} if and only if the promise was completed within
     * the specified time limit
     */
    boolean await(long timeout, TimeUnit unit);

    /**
     * waits if necessary for the computation to complete, and then
     * retrieves its result, if available
     * this method can be interrupted, but the {@link InterruptedException} was wrapped in outer Exception
     */
    T get();

    /**
     * waits if necessary for at most the given time for the computation
     * to complete, and then retrieves its result, if available
     * this method can be interrupted, but the {@link InterruptedException} was wrapped in outer Exception
     *
     * @return {@code true} if and only if the promise was completed within
     * the specified time limit
     */
    T get(long timeout, TimeUnit unit);
}
