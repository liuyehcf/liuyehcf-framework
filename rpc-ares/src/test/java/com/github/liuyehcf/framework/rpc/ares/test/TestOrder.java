package com.github.liuyehcf.framework.rpc.ares.test;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.rpc.ares.ParamsConverter;
import com.github.liuyehcf.framework.rpc.ares.RequestBodyConverter;
import com.github.liuyehcf.framework.rpc.ares.ResponseBodyConverter;
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
    public void testParamsConverter() {
        List<ParamsConverter<Object>> paramsConverters = Lists.newArrayList();
        paramsConverters.add(new ParamsConverter<Object>() {
            @Override
            public int order() {
                return -1;
            }

            @Override
            public String convert(Object input, Type outputType) {
                return null;
            }
        });

        paramsConverters.add(new ParamsConverter<Object>() {
            @Override
            public int order() {
                return 0;
            }

            @Override
            public String convert(Object input, Type outputType) {
                return null;
            }
        });

        paramsConverters.add(new ParamsConverter<Object>() {
            @Override
            public int order() {
                return 1;
            }

            @Override
            public String convert(Object input, Type outputType) {
                return null;
            }
        });

        paramsConverters.add(new ParamsConverter<Object>() {
            @Override
            public String convert(Object input, Type outputType) {
                return null;
            }
        });

        Collections.sort(paramsConverters);

        Assert.assertEquals(1, paramsConverters.get(0).order());
        Assert.assertEquals(0, paramsConverters.get(1).order());
        Assert.assertEquals(0, paramsConverters.get(2).order());
        Assert.assertEquals(-1, paramsConverters.get(3).order());
    }

    @Test
    public void testRequestConverter() {
        List<RequestBodyConverter<Object>> requestBodyConverters = Lists.newArrayList();
        requestBodyConverters.add(new RequestBodyConverter<Object>() {
            @Override
            public int order() {
                return -1;
            }

            @Override
            public byte[] convert(Object input, Type outputType) {
                return new byte[0];
            }
        });

        requestBodyConverters.add(new RequestBodyConverter<Object>() {
            @Override
            public int order() {
                return 0;
            }

            @Override
            public byte[] convert(Object input, Type outputType) {
                return new byte[0];
            }
        });

        requestBodyConverters.add(new RequestBodyConverter<Object>() {
            @Override
            public int order() {
                return 1;
            }

            @Override
            public byte[] convert(Object input, Type outputType) {
                return new byte[0];
            }
        });

        requestBodyConverters.add(new RequestBodyConverter<Object>() {
            @Override
            public byte[] convert(Object input, Type outputType) {
                return new byte[0];
            }
        });

        Collections.sort(requestBodyConverters);

        Assert.assertEquals(1, requestBodyConverters.get(0).order());
        Assert.assertEquals(0, requestBodyConverters.get(1).order());
        Assert.assertEquals(0, requestBodyConverters.get(2).order());
        Assert.assertEquals(-1, requestBodyConverters.get(3).order());
    }

    @Test
    public void testResponseConverter() {
        List<ResponseBodyConverter<Object>> responseBodyConverters = Lists.newArrayList();

        responseBodyConverters.add(new ResponseBodyConverter<Object>() {
            @Override
            public Object convert(byte[] input, Type outputType) {
                return null;
            }
        });

        responseBodyConverters.add(new ResponseBodyConverter<Object>() {
            @Override
            public int order() {
                return Integer.MAX_VALUE;
            }

            @Override
            public Object convert(byte[] input, Type outputType) {
                return null;
            }
        });

        responseBodyConverters.add(new ResponseBodyConverter<Object>() {
            @Override
            public int order() {
                return 200;
            }

            @Override
            public Object convert(byte[] input, Type outputType) {
                return null;
            }
        });

        responseBodyConverters.add(new ResponseBodyConverter<Object>() {
            @Override
            public int order() {
                return Integer.MIN_VALUE;
            }

            @Override
            public Object convert(byte[] input, Type outputType) {
                return null;
            }
        });

        Collections.sort(responseBodyConverters);

        Assert.assertEquals(Integer.MAX_VALUE, responseBodyConverters.get(0).order());
        Assert.assertEquals(200, responseBodyConverters.get(1).order());
        Assert.assertEquals(0, responseBodyConverters.get(2).order());
        Assert.assertEquals(Integer.MIN_VALUE, responseBodyConverters.get(3).order());
    }
}
