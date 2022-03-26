package com.github.liuyehcf.framework.common.tools.promise;

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
public class DefaultPromise<T> implements Promise<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPromise.class);

    private final List<PromiseListenerWrapper<T>> listeners = Lists.newCopyOnWriteArrayList();
    private final ReentrantLock waitLock = new ReentrantLock();
    private final Condition completeCondition = waitLock.newCondition();
    private volatile boolean isCanceled = false;
    private volatile boolean isDone = false;
    private volatile boolean isSuccess = false;
    private volatile boolean isFailure = false;
    private T outcome;
    private Throwable cause;

    @Override
    public final boolean isCancelled() {
        return isCanceled;
    }

    @Override
    public final boolean isDone() {
        return isDone;
    }

    @Override
    public final boolean isSuccess() {
        return isSuccess;
    }

    @Override
    public final boolean isFailure() {
        return isFailure;
    }

    @Override
    public final Throwable cause() {
        return cause;
    }

    @Override
    public final boolean tryCancel() {
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
    public final boolean trySuccess(T outcome) {
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
    public final boolean tryFailure(Throwable cause) {
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
    public final Promise<T> addListener(PromiseListener<T> listener) {
        addListener0(listener);

        if (isDone()) {
            notifyAllListeners();
        }

        return this;
    }

    @Override
    public final void sync() {
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
    public final boolean await(long timeout, TimeUnit unit) {
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
    public final T get() {
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
    public final T get(long timeout, TimeUnit unit) {
        if (isDone()) {
            return report(false);
        }

        // guarantee when this current thread is going to block itself,
        // there must be other thread to wait it up
        return executeSynchronousUnderLock(() -> {
            if (isDone()) {
                return report(false);
            }

            long nanoTimeout = TimeUnit.NANOSECONDS.convert(timeout, unit);
            long start = System.nanoTime();

            boolean await = completeCondition.await(timeout, unit);
            if (!await) {
                report(true);
            }

            long end = System.nanoTime();

            return report((end - start) > nanoTimeout);
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
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PromiseException("interrupt", e);
        } catch (Throwable e) {
            throw reportUnknownError(e);
        } finally {
            waitLock.unlock();
        }
    }

    private void setCanceledUnderLock() {
        // isDone is used to determine success, so the assignment of isDone must be at the end
        isFailure = true;
        isCanceled = true;
        isDone = true;
        completeCondition.signalAll();
    }

    private void setSuccessUnderLock(T outcome) {
        // isDone is used to determine success, so the assignment of isDone must be at the end
        isSuccess = true;
        this.outcome = outcome;
        isDone = true;
        completeCondition.signalAll();
    }

    private void setFailureUnderLock(Throwable cause) {
        // isDone is used to determine success, so the assignment of isDone must be at the end
        isFailure = true;
        this.cause = cause;
        isDone = true;
        completeCondition.signalAll();
    }

    private T report(boolean isTimeout) {
        if (isSuccess()) {
            return outcome;
        }

        if (isCancelled()) {
            throw reportCancel();
        }

        if (isTimeout) {
            throw reportTimeout();
        }

        throw reportFailure(cause());
    }

    protected RuntimeException reportCancel() {
        return new PromiseException("promise canceled");
    }

    protected RuntimeException reportTimeout() {
        return new PromiseException("promise timeout");
    }

    protected RuntimeException reportFailure(Throwable cause) {
        if (cause instanceof PromiseException) {
            return (PromiseException) cause;
        }
        return new PromiseException("promise failed", cause);
    }

    protected RuntimeException reportUnknownError(Throwable cause) {
        if (cause instanceof PromiseException) {
            return (PromiseException) cause;
        }
        return new PromiseException("unknown error", cause);
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
