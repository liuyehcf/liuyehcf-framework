package com.github.liuyehcf.framework.io.athena.util;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.github.liuyehcf.framework.io.athena.AthenaException;

import java.io.*;

/**
 * @author hechenfeng
 * @date 2020/2/6
 */
public abstract class SerializeUtils {

    public static byte[] hessianSerialize(Object object) {
        Hessian2Output hessian2Output = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            hessian2Output = new Hessian2Output(byteArrayOutputStream);

            hessian2Output.getSerializerFactory().setAllowNonSerializable(true);

            hessian2Output.writeObject(object);
            hessian2Output.flush();

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new AthenaException(e);
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
            throw new AthenaException(e);
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

    public static byte[] javaSerialize(Object object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream java2Output = new ObjectOutputStream(byteArrayOutputStream)) {

            java2Output.writeObject(object);
            java2Output.flush();

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new AthenaException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T javaDeserialize(byte[] bytes) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream java2Input = new ObjectInputStream(byteArrayInputStream)) {

            return (T) java2Input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new AthenaException(e);
        }
    }
}
