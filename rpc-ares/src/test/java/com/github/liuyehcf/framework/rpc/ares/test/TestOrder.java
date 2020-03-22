package com.github.liuyehcf.framework.rpc.ares.test;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.rpc.ares.ObjectToBytesCodes;
import com.github.liuyehcf.framework.rpc.ares.ObjectToStringCodes;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * @author hechenfeng
 * @date 2020/3/21
 */
public class TestOrder {

    @Test
    public void testStringCodes() {
        List<ObjectToStringCodes<Object>> stringCodes = Lists.newArrayList();
        stringCodes.add(new ObjectToStringCodes<Object>() {
            @Override
            public int order() {
                return -1;
            }

            @Override
            public String encode(Object obj) {
                return null;
            }

            @Override
            public Object decode(String obj, Type expectedPlainType) {
                return null;
            }
        });

        stringCodes.add(new ObjectToStringCodes<Object>() {
            @Override
            public int order() {
                return 0;
            }

            @Override
            public String encode(Object obj) {
                return null;
            }

            @Override
            public Object decode(String obj, Type expectedPlainType) {
                return null;
            }
        });

        stringCodes.add(new ObjectToStringCodes<Object>() {
            @Override
            public int order() {
                return 1;
            }

            @Override
            public String encode(Object obj) {
                return null;
            }

            @Override
            public Object decode(String obj, Type expectedPlainType) {
                return null;
            }
        });

        stringCodes.add(new ObjectToStringCodes<Object>() {
            @Override
            public String encode(Object obj) {
                return null;
            }

            @Override
            public Object decode(String obj, Type expectedPlainType) {
                return null;
            }
        });

        Collections.sort(stringCodes);

        Assert.assertEquals(1, stringCodes.get(0).order());
        Assert.assertEquals(0, stringCodes.get(1).order());
        Assert.assertEquals(0, stringCodes.get(2).order());
        Assert.assertEquals(-1, stringCodes.get(3).order());
    }

    @Test
    public void testByteCodes() {
        List<ObjectToBytesCodes<Object>> bytesCodes = Lists.newArrayList();

        bytesCodes.add(new ObjectToBytesCodes<Object>() {
            @Override
            public byte[] encode(Object obj) {
                return new byte[0];
            }

            @Override
            public Object decode(byte[] obj, Type expectedPlainType) {
                return null;
            }
        });

        bytesCodes.add(new ObjectToBytesCodes<Object>() {
            @Override
            public int order() {
                return Integer.MAX_VALUE;
            }

            @Override
            public byte[] encode(Object obj) {
                return new byte[0];
            }

            @Override
            public Object decode(byte[] obj, Type expectedPlainType) {
                return null;
            }
        });

        bytesCodes.add(new ObjectToBytesCodes<Object>() {
            @Override
            public int order() {
                return 200;
            }

            @Override
            public byte[] encode(Object obj) {
                return new byte[0];
            }

            @Override
            public Object decode(byte[] obj, Type expectedPlainType) {
                return null;
            }
        });

        bytesCodes.add(new ObjectToBytesCodes<Object>() {
            @Override
            public int order() {
                return Integer.MIN_VALUE;
            }

            @Override
            public byte[] encode(Object obj) {
                return new byte[0];
            }

            @Override
            public Object decode(byte[] obj, Type expectedPlainType) {
                return null;
            }
        });

        Collections.sort(bytesCodes);

        Assert.assertEquals(Integer.MAX_VALUE, bytesCodes.get(0).order());
        Assert.assertEquals(200, bytesCodes.get(1).order());
        Assert.assertEquals(0, bytesCodes.get(2).order());
        Assert.assertEquals(Integer.MIN_VALUE, bytesCodes.get(3).order());
    }
}
