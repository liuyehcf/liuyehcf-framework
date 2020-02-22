package com.github.liuyehcf.framework.flow.engine.util;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.github.liuyehcf.framework.common.tools.bean.BeanUtils;
import com.github.liuyehcf.framework.flow.engine.FlowEngine;
import com.github.liuyehcf.framework.flow.engine.FlowErrorCode;
import com.github.liuyehcf.framework.flow.engine.FlowException;
import com.github.liuyehcf.framework.flow.engine.runtime.constant.EnvCloneType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/5/1
 */
public abstract class CloneUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloneUtils.class);

    private static final KryoPool KRYO_POOL = new KryoPool.Builder(Kryo::new).build();

    public static <K, V> Map<K, V> cloneEnv(FlowEngine flowEngine, Map<K, V> origin) {
        EnvCloneType envCloneType = flowEngine.getProperties().getEnvCloneType();

        switch (envCloneType) {
            case shallow:
                try {
                    return shallowClone(origin);
                } catch (Throwable e) {
                    LOGGER.warn("try to clone in hessian, because shallow clone catch unexpected exception, errorMsg={}", e.getMessage(), e);
                    return hessianClone(origin);
                }
            case bean:
                try {
                    return BeanUtils.clone(origin);
                } catch (Throwable e) {
                    LOGGER.warn("try to clone in hessian, because bean clone catch unexpected exception, errorMsg={}", e.getMessage(), e);
                    return hessianClone(origin);
                }
            case kryo:
                try {
                    return kryoClone(origin);
                } catch (Throwable e) {
                    LOGGER.warn("try to clone in hessian, because kryo clone catch unexpected exception, errorMsg={}", e.getMessage(), e);
                    return hessianClone(origin);
                }
            case hessian:
                return hessianClone(origin);
            case java:
                return javaClone(origin);
            default:
                return hessianClone(origin);
        }
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> shallowClone(Map<K, V> origin) {
        if (origin == null) {
            return null;
        }

        Map<K, V> clone;
        try {
            clone = (Map<K, V>) origin.getClass().newInstance();
            clone.putAll(origin);
            return clone;
        } catch (Exception e) {
            throw new FlowException(FlowErrorCode.SERIALIZE, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T kryoClone(T origin) {
        Kryo kryo = KRYO_POOL.borrow();
        try {
            return kryo.copy(origin);
        } finally {
            KRYO_POOL.release(kryo);
        }
    }

    public static <T> T hessianClone(T origin) {
        return hessianDeserialize(hessianSerialize(origin));
    }

    public static byte[] hessianSerialize(Object object) {
        Hessian2Output hessian2Output = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            hessian2Output = new Hessian2Output(byteArrayOutputStream);

            hessian2Output.getSerializerFactory().setAllowNonSerializable(true);

            hessian2Output.writeObject(object);
            hessian2Output.flush();

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new FlowException(FlowErrorCode.SERIALIZE, e);
        } finally {
            if (hessian2Output != null) {
                try {
                    hessian2Output.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T hessianDeserialize(byte[] bytes) {
        Hessian2Input hessian2Input = null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            hessian2Input = new Hessian2Input(byteArrayInputStream);

            hessian2Input.getSerializerFactory().setAllowNonSerializable(true);

            return (T) hessian2Input.readObject();
        } catch (IOException e) {
            throw new FlowException(FlowErrorCode.SERIALIZE, e);
        } finally {
            if (hessian2Input != null) {
                try {
                    hessian2Input.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }
    }

    public static <T> T javaClone(T origin) {
        return javaDeserialize(javaSerialize(origin));
    }

    public static byte[] javaSerialize(Object object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream java2Output = new ObjectOutputStream(byteArrayOutputStream)) {

            java2Output.writeObject(object);
            java2Output.flush();

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new FlowException(FlowErrorCode.SERIALIZE, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T javaDeserialize(byte[] bytes) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream java2Input = new ObjectInputStream(byteArrayInputStream)) {

            return (T) java2Input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new FlowException(FlowErrorCode.SERIALIZE, e);
        }
    }
}
