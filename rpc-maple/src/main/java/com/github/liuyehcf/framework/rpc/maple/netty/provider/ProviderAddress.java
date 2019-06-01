package com.github.liuyehcf.framework.rpc.maple.netty.provider;


import com.github.liuyehcf.framework.rpc.maple.MapleException;
import com.github.liuyehcf.framework.rpc.maple.register.DefaultServiceAddress;
import com.github.liuyehcf.framework.rpc.maple.register.ServiceAddress;
import lombok.Data;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.util.Enumeration;

/**
 * @author chenlu
 * @date 2019/3/22
 */
@Data
public abstract class ProviderAddress {

    private static final ServiceAddress serviceAddress;

    static {
        try {
            serviceAddress = new DefaultServiceAddress(getLocalIp(), getAnyUnusedPort());
        } catch (IOException e) {
            throw new Error("can not get port automatically", e);
        }
    }

    public static ServiceAddress getServiceAddress() {
        return serviceAddress;
    }

    private static String getLocalIp() {
        try {
            final Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                final NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                final Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    final InetAddress ip = (InetAddress) addresses.nextElement();
                    if (ip instanceof Inet4Address) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            throw new MapleException(MapleException.Code.NETWORK, "get local ip failed", e);
        }
        throw new MapleException(MapleException.Code.NETWORK, "get local ip failed");
    }

    private static int getAnyUnusedPort() throws IOException {
        try (final ServerSocket serverSocket = new ServerSocket(0)) {
            return serverSocket.getLocalPort();
        }
    }
}
