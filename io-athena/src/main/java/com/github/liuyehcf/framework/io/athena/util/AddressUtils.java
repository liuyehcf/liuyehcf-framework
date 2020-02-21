package com.github.liuyehcf.framework.io.athena.util;

import com.google.common.collect.Sets;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public abstract class AddressUtils {

    private static final Pattern PATTERN = Pattern.compile("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");

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

    public static String domainToIp(String domain) {
        try {
            return InetAddress.getByName(domain).getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public static boolean isValidIp(String ip) {
        return PATTERN.matcher(ip).matches();
    }
}
