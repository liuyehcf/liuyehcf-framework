package com.github.liuyehcf.framework.rule.engine.promise;

import com.github.liuyehcf.framework.rule.engine.RuleErrorCode;
import com.github.liuyehcf.framework.rule.engine.RuleException;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
public abstract class AbstractPromise<T> implements Promise<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPromise.class);

    private final List<PromiseListenerWrapper<T>> listeners = Lists.newCopyOnWriteArrayList();
    private final ReentrantLock waitLock = new ReentrantLock();
    private final Condition completeCondition = waitLock.newCondition();
    private volatile T outcome;
    private volatile boolean isCanceled = false;
    private volatile boolean isDone = false;
    private volatile boolean isSuccess = false;
    private volatile boolean isFailure = false;
    private volatile Throwable cause;

    @Override
    public boolean isCancelled() {
        return isCanceled;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
    public boolean isFailure() {
        return isFailure;
    }

    @Override
    public Throwable cause() {
        return cause;
    }

    @Override
    public boolean tryCancel() {
        if (isDone()) {
            return false;
        }

        // guarantee only one of three methods(setCanceledUnderLock, setSuccessUnderLock, setFailureUnderLock) can be execute
        // and only execute only once
        boolean result = executeSynchronousUnderLock(() -> {
            if (isDone()) {
                return false;
            }

            setCanceledUnderLock();

            return true;
        });

        notifyAllListeners();

        return result;
    }

    @Override
    public boolean trySuccess(T outcome) {
        if (isDone()) {
            return false;
        }

        // guarantee only one of three methods(setCanceledUnderLock, setSuccessUnderLock, setFailureUnderLock) can be execute
        // and only execute only once
        boolean result = executeSynchronousUnderLock(() -> {
            if (isDone()) {
                return false;
            }

            setSuccessUnderLock(outcome);

            return true;
        });

        notifyAllListeners();

        return result;
    }

    @Override
    public boolean tryFailure(Throwable cause) {
        if (isDone()) {
            return false;
        }

        // guarantee only one of three methods(setCanceledUnderLock, setSuccessUnderLock, setFailureUnderLock) can be execute
        // and only execute only once
        boolean result = executeSynchronousUnderLock(() -> {
            if (isDone()) {
                return false;
            }

            setFailureUnderLock(cause);

            return true;
        });

        notifyAllListeners();

        return result;
    }

    @Override
    public Promise<T> addListener(PromiseListener<T> listener) {
        addListener0(listener);

        if (isDone()) {
            notifyAllListeners();
        }

        return this;
    }

    @Override
    public void sync() {
        if (isDone()) {
            return;
        }

        // guarantee when this current thread is going to block itself,
        // there must be other thread to wait it up
        executeSynchronousUnderLock(() -> {
            if (isDone()) {
                return null;
            }

            completeCondition.await();
            return null;
        });
    }

    @Override
    public boolean await(long timeout, TimeUnit unit) {
        if (isDone()) {
            return true;
        }

        // guarantee when this current thread is going to block itself,
        // there must be other thread to wait it up
        return executeSynchronousUnderLock(() -> {
            if (isDone()) {
                return true;
            }

            return completeCondition.await(timeout, unit);
        });
    }

    @Override
    public T get() {
        if (isDone()) {
            return report(false);
        }

        // guarantee when this current thread is going to block itself,
        // there must be other thread to wait it up
        return executeSynchronousUnderLock(() -> {
            if (isDone()) {
                return report(false);
            }

            completeCondition.await();
            return report(false);
        });
    }

    @Override
    public T get(long timeout, TimeUnit unit) {
        if (isDone()) {
            return report(false);
        }

        // guarantee when this current thread is going to block itself,
        // there must be other thread to wait it up
        return executeSynchronousUnderLock(() -> {
            if (isDone()) {
                return report(false);
            }

            long nanotimeout = TimeUnit.NANOSECONDS.convert(timeout, unit);
            long start = System.nanoTime();

            completeCondition.await(timeout, unit);

            long end = System.nanoTime();

            return report((end - start) > nanotimeout);
        });
    }

    private void addListener0(PromiseListener<T> listener) {
        listeners.add(new PromiseListenerWrapper<>(listener));
    }

    private void notifyAllListeners() {
        for (PromiseListener<T> listener : listeners) {
            try {
                listener.operationComplete(this);
            } catch (Throwable e) {
                LOGGER.warn("an exception was thrown by {}.operationComplete()", listener.getClass().getName(), e);
            }
        }
    }

    private <F> F executeSynchronousUnderLock(InterruptCallable<F> callable) {
        try {
            waitLock.lock();
            return callable.call();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuleException(RuleErrorCode.PROMISE, e);
        } finally {
            waitLock.unlock();
        }
    }

    private void setCanceledUnderLock() {
        isDone = true;
        isFailure = true;
        isCanceled = true;
        completeCondition.signalAll();
    }

    private void setSuccessUnderLock(T outcome) {
        isDone = true;
        isSuccess = true;
        this.outcome = outcome;
        completeCondition.signalAll();
    }

    private void setFailureUnderLock(Throwable cause) {
        isDone = true;
        isFailure = true;
        this.cause = cause;
        completeCondition.signalAll();
    }

    private T report(boolean isTimeout) {
        if (isSuccess()) {
            return outcome;
        }

        if (isCancelled()) {
            throw new RuleException(RuleErrorCode.PROMISE, "promise canceled");
        }

        if (isTimeout) {
            throw new RuleException(RuleErrorCode.PROMISE, "promise timeout");
        }

        throw new RuleException(RuleErrorCode.PROMISE, "promise failed", cause());
    }

    private interface InterruptCallable<T> {
        T call() throws InterruptedException, ExecutionException;
    }

    private static final class PromiseListenerWrapper<T> implements PromiseListener<T> {

        private final PromiseListener<T> target;
        private final AtomicBoolean isTriggered = new AtomicBoolean(false);

        private PromiseListenerWrapper(PromiseListener<T> target) {
            this.target = target;
        }

        @Override
        public void operationComplete(Promise<T> promise) throws Throwable {
            if (isTriggered.compareAndSet(false, true)) {
                target.operationComplete(promise);
            }
        }
    }
}
