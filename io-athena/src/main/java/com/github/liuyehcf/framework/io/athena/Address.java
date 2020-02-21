package com.github.liuyehcf.framework.io.athena;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2020/2/9
 */
public class Address implements Comparable<Address> {

    private static final Map<String, Map<Integer, Address>> cache = Maps.newConcurrentMap();

    private final String host;
    private final int port;

    public Address(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static Address of(String host, int port) {
        Map<Integer, Address> portAddress = cache.get(host);
        if (portAddress == null) {
            cache.putIfAbsent(host, Maps.newConcurrentMap());
            portAddress = cache.get(host);
        }

        Address address = portAddress.get(port);
        if (address != null) {
            return address;
        }

        portAddress.putIfAbsent(port, new Address(host, port));
        return portAddress.get(port);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public final int compareTo(Address o) {
        if (this.host == null) {
            return 0;
        }
        if (o == null || o.host == null) {
            return 0;
        }

        int r = this.host.compareTo(o.host);
        if (r != 0) {
            return r;
        }
        return this.port - o.port;
    }

    @Override
    public String toString() {
        return String.format("%s:%d", host, port);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        Address address = (Address) o;
        return Objects.equals(host, address.host)
                && Objects.equals(port, address.port);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(host, port);
    }
}
