package com.github.liuyehcf.framework.io.athena;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2020/2/6
 */
public class AthenaConfig {

    public static final int DEFAULT_TOTAL_NUM = 1;
    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final int DEFAULT_PORT = 16389;
    public static final int DEFAULT_PROBE_INTERVAL = 500;
    public static final int DEFAULT_HEARTBEAT_INTERVAL = 10;
    public static final int DEFAULT_HEARTBEAT_TIMEOUT = 30;
    public static final int DEFAULT_TTL_TIMEOUT = 90;
    public static final int DEFAULT_RETRY_INTERVAL = 5;
    public static final int DEFAULT_THREAD_NUM = 1;

    private static final int INVALID = -1;

    /**
     * total number of member in cluster
     */
    private int totalNum = DEFAULT_TOTAL_NUM;

    /**
     * host
     */
    private String host = DEFAULT_HOST;

    /**
     * port
     */
    private int port = DEFAULT_PORT;

    /**
     * unique name of member in the cluster
     */
    private String name = RandomStringUtils.randomAlphanumeric(6).toLowerCase();

    /**
     * seeds of cluster
     */
    private List<Address> seeds;

    /**
     * cluster probe interval
     * in milliseconds
     */
    private int probeInterval = DEFAULT_PROBE_INTERVAL;

    /**
     * heartbeat interval between leader and member
     * in seconds
     */
    private int heartbeatInterval = DEFAULT_HEARTBEAT_INTERVAL;

    /**
     * heartbeat timeout between leader and member
     * in seconds
     */
    private int heartbeatTimeout = DEFAULT_HEARTBEAT_TIMEOUT;

    /**
     * ttl timeout between leader and member
     * in seconds
     */
    private int ttlTimeout = DEFAULT_TTL_TIMEOUT;

    /**
     * retry interval
     * in seconds
     */
    private int retryInterval = DEFAULT_RETRY_INTERVAL;

    /**
     * threads which process event
     */
    private int threadNum = DEFAULT_THREAD_NUM;

    /**
     * workId of snowFlake
     *
     * @see com.github.liuyehcf.framework.common.tools.number.SnowFlakeIDGenerator
     */
    private int workerId = INVALID;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Address> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<Address> seeds) {
        this.seeds = seeds;
    }

    public int getProbeInterval() {
        return probeInterval;
    }

    public void setProbeInterval(int probeInterval) {
        this.probeInterval = probeInterval;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public int getHeartbeatTimeout() {
        return heartbeatTimeout;
    }

    public void setHeartbeatTimeout(int heartbeatTimeout) {
        this.heartbeatTimeout = heartbeatTimeout;
    }

    public int getTtlTimeout() {
        return ttlTimeout;
    }

    public void setTtlTimeout(int ttlTimeout) {
        this.ttlTimeout = ttlTimeout;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }
}
