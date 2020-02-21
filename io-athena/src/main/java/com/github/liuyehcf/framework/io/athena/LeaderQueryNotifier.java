package com.github.liuyehcf.framework.io.athena;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.common.tools.promise.AbstractPromise;
import com.github.liuyehcf.framework.io.athena.event.LeaderQuery;
import com.github.liuyehcf.framework.io.athena.event.LeaderQueryResponse;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2020/2/11
 */
public abstract class LeaderQueryNotifier {

    private static final Map<Long, Promise> cache = Maps.newConcurrentMap();

    public static Promise register(LeaderQuery query) {
        Promise promise = new Promise();
        Assert.assertNull(cache.putIfAbsent(query.getId(), promise), "id duplicates");
        promise.addListener(p -> cache.remove(query.getId()));
        return promise;
    }

    public static void receive(LeaderQueryResponse response) {
        Promise promise = cache.get(response.getId());
        if (promise != null) {
            promise.trySuccess(response);
        }
    }

    public static final class Promise extends AbstractPromise<LeaderQueryResponse> {

    }
}
