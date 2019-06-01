package com.github.liuyehcf.framework.rpc.maple.register;

import com.github.liuyehcf.framework.rpc.maple.MapleConst;
import com.github.liuyehcf.framework.rpc.maple.SerializeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author chenlu
 * @date 2019/3/18
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefaultServiceMeta implements ServiceMeta {

    private static final long serialVersionUID = 8194213934628037190L;

    private String serviceInterface;

    @Builder.Default
    private String serviceGroup = MapleConst.DEFAULT_GROUP;

    @Builder.Default
    private String serviceVersion = MapleConst.DEFAULT_VERSION;

    @Builder.Default
    private long clientTimeout = MapleConst.DEFAULT_CLIENT_TIMEOUT;

    @Builder.Default
    private byte serializeType = SerializeType.HESSIAN.getCode();

    @Override
    public String getServiceInterface() {
        return serviceInterface;
    }

    @Override
    public String getServiceGroup() {
        return serviceGroup;
    }

    @Override
    public String getServiceVersion() {
        return serviceVersion;
    }

    @Override
    public long getClientTimeout() {
        return clientTimeout;
    }

    @Override
    public byte getSerializeType() {
        return serializeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultServiceMeta that = (DefaultServiceMeta) o;
        return Objects.equals(serviceInterface, that.serviceInterface) &&
                Objects.equals(serviceGroup, that.serviceGroup) &&
                Objects.equals(serviceVersion, that.serviceVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceInterface, serviceGroup, serviceVersion);
    }
}
