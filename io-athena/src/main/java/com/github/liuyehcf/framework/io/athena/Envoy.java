package com.github.liuyehcf.framework.io.athena;

/**
 * @author hechenfeng
 * @date 2020/2/5
 */
public interface Envoy {

    static Envoy create(AthenaConfig config) {
        return new DefaultUnsafeEnvoy(config);
    }

    /**
     * start envoy
     */
    void start();

    /**
     * stop envoy
     */
    void stop();

    /**
     * get athena config
     *
     * @return config
     */
    AthenaConfig getConfig();

    /**
     * get cluster info
     *
     * @return cluster info
     */
    Cluster getCluster();

    /**
     * send event to specified active members of the cluster
     *
     * @param address address of member
     * @param event   event to be sent
     * @param <T>     type of event
     */
    <T> boolean whisper(Address address, T event);

    /**
     * send event to all the active member of the cluster
     *
     * @param event         event to be sent
     * @param includingSelf if true, send event to self
     * @param <T>           type of event
     */
    <T> void roar(T event, boolean includingSelf);

    /**
     * register event receiver
     * repeat registration will overwrite
     *
     * @param receiver receiver
     */
    void registerReceiver(Receiver receiver);
}
