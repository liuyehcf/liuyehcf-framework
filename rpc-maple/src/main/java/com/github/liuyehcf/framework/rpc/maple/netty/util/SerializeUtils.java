package com.github.liuyehcf.framework.rpc.maple.netty.util;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.github.liuyehcf.framework.rpc.maple.MapleException;
import com.github.liuyehcf.framework.rpc.maple.SerializeType;

import java.io.*;

/**
 * @author chenlu
 * @date 2019/3/23
 */
public abstract class SerializeUtils {

    public static byte[] serialize(final byte serializeType, final Object object) {
        if (serializeType == SerializeType.JAVA.getCode()) {
            return javaSerialize(object);
        } else if (serializeType == SerializeType.HESSIAN.getCode()) {
            return hessianSerialize(object);
        } else {
            throw new MapleException(MapleException.Code.SERIALIZE);
        }
    }

    public static <T> T deserialize(final byte serializeType, final byte[] bytes) {
        if (serializeType == SerializeType.JAVA.getCode()) {
            return javaDeSerialize(bytes);
        } else if (serializeType == SerializeType.HESSIAN.getCode()) {
            return hessianDeserialize(bytes);
        } else {
            throw new MapleException(MapleException.Code.SERIALIZE);
        }
    }


    private static byte[] javaSerialize(final Object object) {
        try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            objectOutputStream.writeObject(object);
            objectOutputStream.flush();

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new MapleException(MapleException.Code.IO, e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T javaDeSerialize(final byte[] bytes) {
        try (final ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (T) objectInputStream.readObject();
        } catch (IOException e) {
            throw new MapleException(MapleException.Code.IO, e);
        } catch (ClassNotFoundException e) {
            throw new MapleException(MapleException.Code.SERIALIZE, e);
        }
    }

    private static byte[] hessianSerialize(final Object object) {
        Hessian2Output hessian2Output = null;
        try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            hessian2Output = new Hessian2Output(byteArrayOutputStream);

            hessian2Output.writeObject(object);
            hessian2Output.flush();

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new MapleException(MapleException.Code.IO, e);
        } finally {
            if (hessian2Output != null) {
                try {
                    hessian2Output.close();
                } catch (IOException e) {
                    // todo log
                    //ignore
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T hessianDeserialize(final byte[] bytes) {
        Hessian2Input hessian2Input = null;
        try (final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            hessian2Input = new Hessian2Input(byteArrayInputStream);
            return (T) hessian2Input.readObject();
        } catch (IOException e) {
            throw new MapleException(MapleException.Code.IO, e);
        } finally {
            if (hessian2Input != null) {
                try {
                    hessian2Input.close();
                } catch (IOException e) {
                    // todo log
                    //ignore
                }
            }
        }
    }
}
