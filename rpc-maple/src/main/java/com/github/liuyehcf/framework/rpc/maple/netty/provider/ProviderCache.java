package com.github.liuyehcf.framework.rpc.maple.netty.provider;

import com.github.liuyehcf.framework.rpc.maple.register.ServiceMeta;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenlu
 * @date 2019/3/22
 */
public abstract class ProviderCache {

    private static final Map<ServiceMeta, Object> PROVIDER_BEAN_CACHE = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T getProviderTargetBean(final ServiceMeta serviceMeta) {
        return (T) PROVIDER_BEAN_CACHE.get(serviceMeta);
    }

    public static void addProviderTargetBean(final ServiceMeta serviceMeta, final Object providerBean) {
        PROVIDER_BEAN_CACHE.put(serviceMeta, providerBean);
    }

    public static void removeProviderTargetBean(final ServiceMeta serviceMeta) {
        PROVIDER_BEAN_CACHE.remove(serviceMeta);
    }
}
