package com.github.liuyehcf.framework.common.tools.test.number;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.common.tools.number.SnowFlakeIDGenerator;
import com.github.liuyehcf.framework.common.tools.time.TimeUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author hechenfeng
 * @date 2020/2/17
 */
public class TestSnowFlakeIDGenerator {

    @Test
    public void test() throws Exception {
        int threadNum = 10;
        Set<Long> ids = Sets.newConcurrentHashSet();

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<Future<Void>> futures = Lists.newArrayList();

        for (int i = 0; i < threadNum; i++) {
            SnowFlakeIDGenerator idGenerator = new SnowFlakeIDGenerator(i);
            Future<Void> future = executorService.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    Assert.assertTrue(ids.add(idGenerator.nextId()));
                }
                return null;
            });
            futures.add(future);
        }

        TimeUtils.sleep(10, TimeUnit.SECONDS);
        executorService.shutdownNow();
        TimeUtils.sleep(1, TimeUnit.SECONDS);

        for (Future<Void> future : futures) {
            Assert.assertNull(future.get());
        }
    }
}
