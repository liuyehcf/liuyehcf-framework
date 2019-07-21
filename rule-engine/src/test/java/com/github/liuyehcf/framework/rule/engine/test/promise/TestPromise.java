package com.github.liuyehcf.framework.rule.engine.test.promise;

import com.github.liuyehcf.framework.rule.engine.RuleErrorCode;
import com.github.liuyehcf.framework.rule.engine.RuleException;
import com.github.liuyehcf.framework.rule.engine.promise.Promise;
import com.github.liuyehcf.framework.rule.engine.promise.RulePromise;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.DefaultExecutionInstance;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.ExecutionInstance;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
public class TestPromise {

    @Test
    public void testSync1() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();
        Assert.assertTrue(promise.trySuccess(null));

        promise.sync();
        long end = System.currentTimeMillis();

        Assert.assertTrue((end - start) < 100);
    }

    @Test
    public void testSync2() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();

        executeAsync(() ->
                sleepThenTrySuccess(promise, 300)
        );

        promise.sync();
        long end = System.currentTimeMillis();

        Assert.assertTrue(Math.abs(end - start - 300) < 100);
    }

    @Test
    public void testSync3() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();
        Assert.assertTrue(promise.tryFailure(null));

        promise.sync();
        long end = System.currentTimeMillis();

        Assert.assertTrue((end - start) < 100);
    }

    @Test
    public void testSync4() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();

        executeAsync(() ->
                sleepThenTryFailure(promise, 300)
        );

        promise.sync();
        long end = System.currentTimeMillis();

        Assert.assertTrue(Math.abs(end - start - 300) < 100);
    }

    @Test
    public void testSyn5() throws Exception {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();

        executeAsync(() ->
                sleepThenTryFailure(promise, 300)
        );

        List<Thread> threads = Lists.newArrayList();

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(promise::sync);
            thread.start();
            threads.add(thread);
        }

        for (int i = 0; i < 10; i++) {
            threads.get(i).join();
        }
        long end = System.currentTimeMillis();

        Assert.assertTrue(Math.abs(end - start - 300) < 100);
    }

    @Test
    public void testAwait1() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();

        Assert.assertTrue(promise.trySuccess(null));

        Assert.assertTrue(promise.await(300, TimeUnit.MILLISECONDS));
        long end = System.currentTimeMillis();

        Assert.assertTrue(Math.abs(end - start) < 100);
    }

    @Test
    public void testAwait2() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();

        executeAsync(() ->
                sleepThenTrySuccess(promise, 200)
        );

        Assert.assertTrue(promise.await(300, TimeUnit.MILLISECONDS));
        long end = System.currentTimeMillis();

        Assert.assertTrue(Math.abs(end - start - 200) < 100);
    }

    @Test
    public void testAwait3() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();

        executeAsync(() ->
                sleepThenTrySuccess(promise, 200)
        );

        Assert.assertFalse(promise.await(100, TimeUnit.MILLISECONDS));
        long end = System.currentTimeMillis();

        Assert.assertTrue(Math.abs(end - start - 100) < 100);
    }

    @Test
    public void testAwait4() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();

        Assert.assertTrue(promise.tryFailure(null));

        Assert.assertTrue(promise.await(300, TimeUnit.MILLISECONDS));
        long end = System.currentTimeMillis();

        Assert.assertTrue(Math.abs(end - start) < 100);
    }

    @Test
    public void testAwait5() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();

        executeAsync(() ->
                sleepThenTryFailure(promise, 200)
        );

        Assert.assertTrue(promise.await(300, TimeUnit.MILLISECONDS));
        long end = System.currentTimeMillis();

        Assert.assertTrue(Math.abs(end - start - 200) < 100);
    }

    @Test
    public void testAwait6() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();

        executeAsync(() ->
                sleepThenTryFailure(promise, 200)
        );

        Assert.assertFalse(promise.await(100, TimeUnit.MILLISECONDS));
        long end = System.currentTimeMillis();

        Assert.assertTrue(Math.abs(end - start - 100) < 100);
    }

    @Test
    public void testGet1() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();
        Assert.assertTrue(promise.trySuccess(null));

        promise.get();
        long end = System.currentTimeMillis();

        Assert.assertTrue((end - start) < 100);
    }

    @Test
    public void testGet2() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();

        executeAsync(() ->
                sleepThenTrySuccess(promise, 300)
        );

        promise.get();
        long end = System.currentTimeMillis();

        Assert.assertTrue(Math.abs(end - start - 300) < 100);
    }

    @Test
    public void testGet3() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();
        Assert.assertTrue(promise.tryFailure(null));

        try {
            promise.get();
        } catch (RuleException e) {
            Assert.assertEquals(RuleErrorCode.PROMISE, e.getCode());
        }
        long end = System.currentTimeMillis();

        Assert.assertTrue((end - start) < 100);
    }

    @Test
    public void testGet4() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();

        executeAsync(() ->
                sleepThenTryFailure(promise, 300)
        );

        try {
            promise.get();
        } catch (RuleException e) {
            Assert.assertEquals(RuleErrorCode.PROMISE, e.getCode());
        }
        long end = System.currentTimeMillis();

        Assert.assertTrue(Math.abs(end - start - 300) < 100);
    }

    @Test(expected = RuleException.class)
    public void testFailedAndGet() {
        Promise<ExecutionInstance> promise = new RulePromise();

        promise.tryFailure(null);

        promise.get();
    }

    @Test
    public void testGetConcurrent() throws Exception {
        Promise<ExecutionInstance> promise = new RulePromise();

        List<Thread> threads = Lists.newArrayList();

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(promise::get);
            thread.start();
            threads.add(thread);
        }

        TimeUnit.MILLISECONDS.sleep(500);

        promise.trySuccess(null);

        for (Thread thread : threads) {
            thread.join();
        }

        promise.get();
    }

    @Test
    public void testGetAwait1() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();

        Assert.assertTrue(promise.trySuccess(new DefaultExecutionInstance(null, null)));

        Assert.assertNotNull(promise.get(300, TimeUnit.MILLISECONDS));
        long end = System.currentTimeMillis();

        Assert.assertTrue(Math.abs(end - start) < 100);
    }

    @Test
    public void testGetAwait2() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();

        executeAsync(() ->
                sleepThenTrySuccess(promise, 200)
        );

        Assert.assertNotNull(promise.get(300, TimeUnit.MILLISECONDS));
        long end = System.currentTimeMillis();

        Assert.assertTrue(Math.abs(end - start - 200) < 100);
    }

    @Test
    public void testGetAwait3() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();

        executeAsync(() ->
                sleepThenTrySuccess(promise, 200)
        );

        try {
            promise.get(100, TimeUnit.MILLISECONDS);
        } catch (RuleException e) {
            Assert.assertEquals(RuleErrorCode.PROMISE, e.getCode());
        }
        long end = System.currentTimeMillis();

        Assert.assertTrue(Math.abs(end - start - 100) < 100);
    }

    @Test
    public void testGetAwait4() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();

        Assert.assertTrue(promise.tryFailure(null));

        try {
            promise.get(300, TimeUnit.MILLISECONDS);
        } catch (RuleException e) {
            Assert.assertEquals(RuleErrorCode.PROMISE, e.getCode());
        }
        long end = System.currentTimeMillis();

        Assert.assertTrue(Math.abs(end - start) < 100);
    }

    @Test
    public void testGetAwait5() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();

        executeAsync(() ->
                sleepThenTryFailure(promise, 200)
        );

        try {
            promise.get(300, TimeUnit.MILLISECONDS);
        } catch (RuleException e) {
            Assert.assertEquals(RuleErrorCode.PROMISE, e.getCode());
        }
        long end = System.currentTimeMillis();

        Assert.assertTrue(Math.abs(end - start - 200) < 100);
    }

    @Test
    public void testGetAwait6() {
        Promise<ExecutionInstance> promise = new RulePromise();
        long start = System.currentTimeMillis();

        executeAsync(() ->
                sleepThenTryFailure(promise, 200)
        );

        try {
            promise.get(100, TimeUnit.MILLISECONDS);
        } catch (RuleException e) {
            Assert.assertEquals(RuleErrorCode.PROMISE, e.getCode());
        }
        long end = System.currentTimeMillis();

        Assert.assertTrue(Math.abs(end - start - 100) < 100);
    }

    private void sleepThenTrySuccess(Promise<ExecutionInstance> promise, int milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
            Assert.assertTrue(promise.trySuccess(new DefaultExecutionInstance(null, null)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sleepThenTryFailure(Promise<ExecutionInstance> promise, int milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
            Assert.assertTrue(promise.tryFailure(null));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void executeAsync(Runnable runnable) {
        new Thread(runnable).start();
    }
}
