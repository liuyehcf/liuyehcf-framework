package com.github.liuyehcf.framework.rpc.maple.register;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.rpc.maple.MapleException;
import com.github.liuyehcf.framework.rpc.maple.util.Assert;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * todo cache
 *
 * @author chenlu
 * @date 2019/3/22
 */
@Data
public class ZookeeperConfigClient implements ConfigClient, InitializingBean {

    private String host;

    private int port;

    private ZooKeeper client;

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    @Override
    public void registerService(final ServiceMeta serviceMeta, final ServiceAddress serviceAddress) {
        final String serviceDir = toServiceDir(serviceMeta);

        try {
            final Stat stat = client.exists(serviceDir, false);

            if (stat == null) {
                createPersistentIfNecessary(serviceDir);
            }

            final String providerPath = String.format("%s/%s", serviceDir, UUID.randomUUID().toString());

            client.create(
                    providerPath,
                    JSON.toJSONBytes(new DefaultServiceInstance(serviceAddress, serviceMeta)),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL);
        } catch (KeeperException e) {
            throw new MapleException(MapleException.Code.REGISTER_SERVICE_FAILED, "register service address failed", e);
        } catch (Throwable e) {
            throw new MapleException(MapleException.Code.UNKNOWN, "fetch service address failed", e);
        }
    }

    @Override
    public List<ServiceInstance> fetchAllServiceAddresses(final ServiceMeta serviceMeta) {
        try {
            final String serviceDir = toServiceDir(serviceMeta);

            final Stat stat = client.exists(serviceDir, false);
            if (stat == null) {
                return Lists.newArrayList();
            }

            final List<String> availableProviders = client.getChildren(serviceDir, false);

            final List<ServiceInstance> serviceInstances = Lists.newArrayList();

            for (final String availableProvider : availableProviders) {
                final String providerPath = String.format("%s/%s", serviceDir, availableProvider);
                final byte[] data = getEphemeralOrNull(providerPath);

                if (data != null) {
                    serviceInstances.add(DefaultServiceInstance.parse(JSON.parseObject(new String(data, Charset.defaultCharset()))));
                }
            }

            return serviceInstances;
        } catch (KeeperException e) {
            throw new MapleException(MapleException.Code.FETCH_SERVICE_FAILED, "fetch service address failed", e);
        } catch (Throwable e) {
            throw new MapleException(MapleException.Code.UNKNOWN, "fetch service address failed", e);
        }
    }

    @Override
    public ServiceInstance fetchAnyServiceAddress(final ServiceMeta serviceMeta) {
        final List<ServiceInstance> serviceAddresses = fetchAllServiceAddresses(serviceMeta);
        Collections.shuffle(serviceAddresses);
        final Optional<ServiceInstance> any = serviceAddresses.stream().findAny();

        if (any.isPresent()) {
            return any.get();
        }

        throw new MapleException(MapleException.Code.CANNOT_FIND_TARGET_SERVICE,
                String.format("cannot find target service. serviceInterface='%s', serviceGroup='%s', serviceVersion='%s'",
                        serviceMeta.getServiceInterface(),
                        serviceMeta.getServiceGroup(),
                        serviceMeta.getServiceVersion()));
    }

    private void init() throws IOException {
        client = new ZooKeeper(host, port, new ServiceWatcher());
    }

    private String toServiceDir(final ServiceMeta serviceMeta) {
        return String.format("/MapleRegistry/%s/%s/%s",
                serviceMeta.getServiceInterface(),
                serviceMeta.getServiceGroup(),
                serviceMeta.getServiceVersion());
    }

    private void createPersistentIfNecessary(final String serviceDir) {
        final String[] segments = serviceDir.substring(1).split("/");
        Assert.assertEquals(segments.length, 4);

        final StringBuilder pathAppender = new StringBuilder();
        int index = 0;
        pathAppender.append('/').append(segments[index]);

        for (; ; ) {
            try {
                final String path = pathAppender.toString();

                final Stat stat = client.exists(path, false);

                if (stat == null) {
                    client.create(path,
                            path.getBytes(),
                            ZooDefs.Ids.OPEN_ACL_UNSAFE,
                            CreateMode.PERSISTENT);
                } else {
                    if (++index < 4) {
                        pathAppender.append('/').append(segments[index]);
                    } else {
                        break;
                    }
                }
            } catch (KeeperException e) {
                switch (e.code()) {
                    case NODEEXISTS:
                        continue;
                    default:
                        throw new MapleException(MapleException.Code.REGISTER_SERVICE_FAILED, "register service address failed", e);
                }
            } catch (Throwable e) {
                throw new MapleException(MapleException.Code.REGISTER_SERVICE_FAILED, "register service address failed", e);
            }
        }
    }

    private byte[] getEphemeralOrNull(final String path) {
        try {
            return client.getData(path, null, null);
        } catch (KeeperException e) {
            switch (e.code()) {
                case NONODE:
                    return null;
                default:
                    throw new MapleException(MapleException.Code.FETCH_SERVICE_FAILED, "fetch service address failed", e);
            }
        } catch (Throwable e) {
            throw new MapleException(MapleException.Code.UNKNOWN, "fetch service address failed", e);
        }
    }

    private static final class ServiceWatcher implements Watcher {
        @Override
        public void process(WatchedEvent event) {

        }
    }
}
