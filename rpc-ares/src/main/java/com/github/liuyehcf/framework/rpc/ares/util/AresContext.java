package com.github.liuyehcf.framework.rpc.ares.util;

import com.github.liuyehcf.framework.rpc.ares.constant.SchemaType;

/**
 * @author hechenfeng
 * @date 2019/11/29
 */
public abstract class AresContext {

    private static final ThreadLocal<Endpoint> ENDPOINT = new ThreadLocal<>();

    public static <T> T invokeWithEndpoint(SchemaType schemaType, String host, Integer port, Invoker<T> invoker) {
        try {
            setEndpoint(schemaType, host, port);
            return invoker.invoke();
        } finally {
            removeEndpoint();
        }
    }

    public static void setEndpoint(SchemaType schemaType, String host, Integer port) {
        ENDPOINT.set(new Endpoint(schemaType, host, port));
    }

    public static Endpoint getEndpoint() {
        return ENDPOINT.get();
    }

    public static void removeEndpoint() {
        ENDPOINT.remove();
    }

    public interface Invoker<T> {

        /**
         * invoker wraps http biz logic
         */
        T invoke();
    }

    public static final class Endpoint {
        /**
         * http schema
         */
        private final SchemaType schema;

        /**
         * http host
         */
        private final String host;

        /**
         * http port
         */
        private final Integer port;

        public Endpoint(SchemaType schema, String host, Integer port) {
            this.schema = schema;
            this.host = host;
            this.port = port;
        }

        public SchemaType getSchema() {
            return schema;
        }

        public String getHost() {
            return host;
        }

        public Integer getPort() {
            return port;
        }
    }
}
