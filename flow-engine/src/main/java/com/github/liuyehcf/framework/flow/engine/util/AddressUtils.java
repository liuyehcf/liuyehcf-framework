package com.github.liuyehcf.framework.flow.engine.util;

import com.google.common.collect.Sets;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Set;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public abstract class AddressUtils {

    private static final Set<String> localIps = Sets.newHashSet();

    static {
        initLocalIps();
    }

    private static void initLocalIps() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress.getHostAddress().indexOf(':') == -1) {
                        localIps.add(inetAddress.getHostAddress());
                    }
                }
            }

        } catch (Throwable e) {
            throw new Error(e);
        }
    }

    public static boolean isLocalIp(String host) {
        return localIps.contains(host);
    }
}
