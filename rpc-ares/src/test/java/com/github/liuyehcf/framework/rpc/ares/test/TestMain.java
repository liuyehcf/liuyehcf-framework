package com.github.liuyehcf.framework.rpc.ares.test;

import com.github.liuyehcf.framework.rpc.ares.constant.SchemaType;
import com.github.liuyehcf.framework.rpc.ares.test.ares.TestClient;
import com.github.liuyehcf.framework.rpc.ares.test.model.Person;
import com.github.liuyehcf.framework.rpc.ares.util.AresContext;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Random;

/**
 * @author hechenfeng
 * @date 2019/11/8
 */
public class TestMain extends BaseConfig {

    @Resource
    private TestClient testClient;

    private Random random = new Random();

    @Test
    public void zeroRequestParamWithOneMoreParam() {
        String result = testClient.zeroRequestParamWithOneMoreParam("hello");
        Assert.assertEquals("zeroRequestParam()[]", result);
    }

    @Test
    public void zeroRequestParamWithOneMoreRequestBody() {
        String result = testClient.zeroRequestParamWithOneMoreRequestBody("hello");
        Assert.assertEquals("zeroRequestParam()[]", result);
    }

    @Test
    public void testZeroRequestParam() {
        String result = testClient.zeroRequestParam();
        Assert.assertEquals("zeroRequestParam()[]", result);
    }

    @Test
    public void oneRequestParamMissingParam() {
        try {
            testClient.oneRequestParamMissingParam();
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Required String parameter 'param1' is not present"));
            return;
        }
        throw new Error();
    }

    @Test
    public void oneRequestParamWithoutParamAnnotation() {
        try {
            testClient.oneRequestParamWituoutParamAnnotation(null);
        } catch (Exception e) {
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresHeader' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void oneRequestParamWithBothAnnotation() {
        try {
            testClient.oneRequestParamWithBothAnnotation(null);
        } catch (Exception e) {
            Assert.assertEquals("parameter contains more than one of ares annotations", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void oneRequestParamWithRequestBody() {
        try {
            testClient.oneRequestParamWithRequestBody(Person.builder()
                    .country("China")
                    .name("hechenfeng")
                    .age(18)
                    .build());
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Required String parameter 'param1' is not present"));
            return;
        }
        throw new Error();
    }

    @Test
    public void oneRequestParamWithJsonSerializeType() {
        String result = testClient.oneRequestParamWithJsonSerializeType(Person.builder()
                .country("China")
                .name("hechenfeng")
                .age(18)
                .build());

        Assert.assertEquals("oneRequestParam({\"age\":18,\"country\":\"China\",\"name\":\"hechenfeng\"})[]", result);
    }

    @Test
    public void oneRequestParamWithOneMoreParam() {
        String result = testClient.oneRequestParamWithOneMoreParam(Person.builder()
                .country("China")
                .name("hechenfeng")
                .age(18)
                .build(), "hello");

        Assert.assertEquals("oneRequestParam({\"age\":18,\"country\":\"China\",\"name\":\"hechenfeng\"})[]", result);
    }

    @Test
    public void twoRequestParamMissingFirstAnnotation() {
        try {
            testClient.twoRequestParamMissingFirstAnnotation("param1", "param2");
        } catch (Exception e) {
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresHeader' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void twoRequestParamMissingSecondAnnotation() {
        try {
            testClient.twoRequestParamMissingSecondAnnotation("param1", "param2");
        } catch (Exception e) {
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresHeader' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void twoRequestParamMissingBothAnnotation() {
        try {
            testClient.twoRequestParamMissingBothAnnotation("param1", "param2");
        } catch (Exception e) {
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresHeader' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void twoRequestParamWithFirstBody() {
        try {
            testClient.twoRequestParamWithFirstBody("param1", "param2");
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Required String parameter 'param1' is not present"));
            return;
        }
        throw new Error();
    }

    @Test
    public void twoRequestParamWithSecondBody() {
        try {
            testClient.twoRequestParamWithSecondBody("param1", "param2");
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Required String parameter 'param2"));
            return;
        }
        throw new Error();
    }

    @Test
    public void twoRequestParamWithBothBody() {
        try {
            testClient.twoRequestParamWithBothBody("param1", "param2");
        } catch (Exception e) {
            Assert.assertEquals("more than one '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void twoRequestParamWithDuplicateQueryParam() {
        try {
            testClient.twoRequestParamWithDuplicateQueryParam("param1", "param2");
        } catch (Exception e) {
            Assert.assertEquals("duplicate query parameter 'param1'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void twoRequestParamWithStringString() {
        String result = testClient.twoRequestParamWithStringString("hello", " world!");

        Assert.assertEquals("twoRequestParam(hello,  world!)[]", result);
    }

    @Test
    public void twoRequestParamWithStringJson() {
        String result = testClient.twoRequestParamWithStringJson("hello", Person.builder()
                .country("China")
                .name("hechenfeng")
                .age(18)
                .build());

        Assert.assertEquals("twoRequestParam(hello, {\"age\":18,\"country\":\"China\",\"name\":\"hechenfeng\"})[]", result);
    }

    @Test
    public void twoRequestParamWithOneMoreParam() {
        String result = testClient.twoRequestParamWithOneMoreParam("hello", Person.builder()
                .country("China")
                .name("hechenfeng")
                .age(18)
                .build(), "null");

        Assert.assertEquals("twoRequestParam(hello, {\"age\":18,\"country\":\"China\",\"name\":\"hechenfeng\"})[]", result);
    }

    @Test
    public void testOneRequestHeader() {
        String result = testClient.oneRequestHeader("hello");

        Assert.assertEquals("oneRequestHeader(hello)[]", result);
    }

    @Test
    public void twoOneRequestHeader() {
        String result = testClient.twoRequestHeader("hello", "world");

        Assert.assertEquals("twoRequestHeader(hello, world)[]", result);
    }

    @Test
    public void twoOneRequestHeaderWithJson() {
        String result = testClient.twoRequestHeader("hello", Person.builder()
                .country("China")
                .name("hechenfeng")
                .age(18)
                .build());

        Assert.assertEquals("twoRequestHeader(hello, {\"age\":18,\"country\":\"China\",\"name\":\"hechenfeng\"})[]", result);
    }

    @Test
    public void requestBodyWithoutAnnotation() {
        try {
            testClient.requestBodyWithoutAnnotation("hello");
        } catch (Exception e) {
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresHeader' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void requestBodyWithParam() {
        try {
            testClient.requestBodyWithParam("hello");
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Required request body is missing"));
            return;
        }
        throw new Error();
    }

    @Test
    public void requestBodyString() {
        String result = testClient.requestBodyString("hello");
        Assert.assertEquals("requestBody()[hello]", result);
    }

    @Test
    public void requestBodyJson() {
        String result = testClient.requestBodyJson(Person.builder()
                .country("China")
                .name("hechenfeng")
                .age(18)
                .build());
        Assert.assertEquals("requestBody()[{\"age\":18,\"country\":\"China\",\"name\":\"hechenfeng\"}]", result);
    }

    @Test
    public void requestBodyJsonWithOneMoreParam() {
        String result = testClient.requestBodyWithOneMoreParam(Person.builder()
                .country("China")
                .name("hechenfeng")
                .age(18)
                .build(), "hello");
        Assert.assertEquals("requestBody()[{\"age\":18,\"country\":\"China\",\"name\":\"hechenfeng\"}]", result);
    }

    @Test
    public void oneRequestParamOneRequestBodyMissingFirstAnnotation() {
        try {
            testClient.oneRequestParamOneRequestBodyMissingFirstAnnotation("hello", "world");
        } catch (Exception e) {
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresHeader' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void oneRequestParamOneRequestBodyMissingSecondAnnotation() {
        try {
            testClient.oneRequestParamOneRequestBodyMissingSecondAnnotation("hello", "world");
        } catch (Exception e) {
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresHeader' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void oneRequestParamOneRequestBodyMissingBothAnnotation() {
        try {
            testClient.oneRequestParamOneRequestBodyMissingBothAnnotation("hello", "world");
        } catch (Exception e) {
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresHeader' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void oneRequestParamOneRequestBody() {
        Map<String, Person> result = testClient.oneRequestParamOneRequestBody("hello", "world");

        Person person = result.get("hello");

        Assert.assertEquals(Person.builder()
                .name("world")
                .build(), person);
    }

    @Test
    public void oneRequestParamOneRequestBodyWithOneMoreParam() {
        Map<String, Person> result = testClient.oneRequestParamOneRequestBodyWithOneMoreParam("hello", "world", "!");

        Person person = result.get("hello");

        Assert.assertEquals(Person.builder()
                .name("world")
                .build(), person);
    }

    @Test
    public void nullableQueryParamAndRequestBodyBothNull() {
        String result = testClient.nullableQueryParamAndRequestBody(null, null);

        Assert.assertEquals("both null", result);
    }

    @Test
    public void nullableQueryParamAndRequestBodyParam1Null() {
        String result = testClient.nullableQueryParamAndRequestBody(null, "null");

        Assert.assertEquals("param1 null", result);
    }

    @Test
    public void nullableQueryParamAndRequestBodyRequestBodyNull() {
        String result = testClient.nullableQueryParamAndRequestBody("null", null);

        Assert.assertEquals("requestBody null", result);
    }

    @Test
    public void nullableQueryParamAndRequestBodyBothNotNull() {
        String result = testClient.nullableQueryParamAndRequestBody("null", "null");

        Assert.assertEquals("both not null", result);
    }

    @Test
    public void nullableQueryParamAndRequestBodyVoidReturn1() {
        testClient.nullableQueryParamAndRequestBodyVoidReturn1("null", "null");
    }

    @Test
    public void nullableQueryParamAndRequestBodyVoidReturn2() {
        testClient.nullableQueryParamAndRequestBodyVoidReturn2("null", "null");
    }

    @Test
    public void differentPathVariable() {
        Assert.assertEquals("/differentPathVariable/abc/def", testClient.differentPathVariable("abc", "def"));
    }

    @Test
    public void samePathVariable() {
        Assert.assertEquals("/samePathVariable/abc/abc", testClient.samePathVariable("abc"));
    }

    @Test
    public void returnNull() {
        Assert.assertNull(testClient.returnNull());
    }

    @Test
    public void customizeContentType() {
        String result = testClient.customizeContentType(null, "hello");
        Assert.assertEquals("application/test1; charset=UTF-8", result);

        result = testClient.customizeContentType("application/test2; charset=UTF-8", "hello");
        Assert.assertEquals("application/test1; charset=UTF-8", result);
    }

    @Test
    public void wrongPath() {
        try {
            testClient.wrongPath();
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Not Found"));
            return;
        }
        throw new Error();
    }

    @Test
    public void primitiveBoolean() {
        //primitive
        Assert.assertFalse(testClient.primitiveBooleanWithPrimitivePrimitive(false));
        Assert.assertTrue(testClient.primitiveBooleanWithPrimitivePrimitive(true));

        Assert.assertFalse(testClient.primitiveBooleanWithPrimitiveWrapperPrimitive(null));
        Assert.assertFalse(testClient.primitiveBooleanWithPrimitiveWrapperPrimitive(false));
        Assert.assertTrue(testClient.primitiveBooleanWithPrimitiveWrapperPrimitive(true));

        Assert.assertFalse(testClient.primitiveBooleanWithPrimitivePrimitiveWrapper(false));
        Assert.assertTrue(testClient.primitiveBooleanWithPrimitivePrimitiveWrapper(true));

        Assert.assertFalse(testClient.primitiveBooleanWithPrimitiveWrapperPrimitiveWrapper(null));
        Assert.assertFalse(testClient.primitiveBooleanWithPrimitiveWrapperPrimitiveWrapper(false));
        Assert.assertTrue(testClient.primitiveBooleanWithPrimitiveWrapperPrimitiveWrapper(true));


        // primitive wrapper
        Assert.assertFalse(testClient.primitiveWrapperBooleanWithPrimitivePrimitive(false));
        Assert.assertTrue(testClient.primitiveWrapperBooleanWithPrimitivePrimitive(true));

        Assert.assertFalse(testClient.primitiveWrapperBooleanWithPrimitiveWrapperPrimitive(null));
        Assert.assertFalse(testClient.primitiveWrapperBooleanWithPrimitiveWrapperPrimitive(false));
        Assert.assertTrue(testClient.primitiveWrapperBooleanWithPrimitiveWrapperPrimitive(true));

        Assert.assertFalse(testClient.primitiveWrapperBooleanWithPrimitivePrimitiveWrapper(false));
        Assert.assertTrue(testClient.primitiveWrapperBooleanWithPrimitivePrimitiveWrapper(true));

        Assert.assertNull(testClient.primitiveWrapperBooleanWithPrimitiveWrapperPrimitiveWrapper(null));
        Assert.assertFalse(testClient.primitiveWrapperBooleanWithPrimitiveWrapperPrimitiveWrapper(false));
        Assert.assertTrue(testClient.primitiveWrapperBooleanWithPrimitiveWrapperPrimitiveWrapper(true));
    }

    @Test
    public void primitiveByte() {
        //primitive
        byte value;

        Assert.assertEquals(value = (byte) random.nextInt(), testClient.primitiveByteWithPrimitivePrimitive(value));

        Assert.assertEquals(value = (byte) random.nextInt(), testClient.primitiveByteWithPrimitiveWrapperPrimitive(value));

        Assert.assertEquals(value = (byte) random.nextInt(), (byte) testClient.primitiveByteWithPrimitivePrimitiveWrapper(value));

        Assert.assertEquals(value = (byte) random.nextInt(), (byte) testClient.primitiveByteWithPrimitiveWrapperPrimitiveWrapper(value));


        // primitive wrapper
        Assert.assertEquals(value = (byte) random.nextInt(), testClient.primitiveWrapperByteWithPrimitivePrimitive(value));

        Assert.assertEquals((byte) 0, testClient.primitiveWrapperByteWithPrimitiveWrapperPrimitive(null));
        Assert.assertEquals(value = (byte) random.nextInt(), testClient.primitiveWrapperByteWithPrimitiveWrapperPrimitive(value));

        Assert.assertEquals(value = (byte) random.nextInt(), (byte) testClient.primitiveWrapperByteWithPrimitivePrimitiveWrapper(value));

        Assert.assertNull(testClient.primitiveWrapperByteWithPrimitiveWrapperPrimitiveWrapper(null));
        Assert.assertEquals(value = (byte) random.nextInt(), (byte) testClient.primitiveWrapperByteWithPrimitiveWrapperPrimitiveWrapper(value));
    }

    @Test
    public void primitiveShort() {
        //primitive
        short value;

        Assert.assertEquals(value = (short) random.nextInt(), testClient.primitiveShortWithPrimitivePrimitive(value));

        Assert.assertEquals(value = (short) random.nextInt(), testClient.primitiveShortWithPrimitiveWrapperPrimitive(value));

        Assert.assertEquals(value = (short) random.nextInt(), (short) testClient.primitiveShortWithPrimitivePrimitiveWrapper(value));

        Assert.assertEquals(value = (short) random.nextInt(), (short) testClient.primitiveShortWithPrimitiveWrapperPrimitiveWrapper(value));


        // primitive wrapper
        Assert.assertEquals(value = (short) random.nextInt(), testClient.primitiveWrapperShortWithPrimitivePrimitive(value));

        Assert.assertEquals((short) 0, testClient.primitiveWrapperShortWithPrimitiveWrapperPrimitive(null));
        Assert.assertEquals(value = (short) random.nextInt(), testClient.primitiveWrapperShortWithPrimitiveWrapperPrimitive(value));

        Assert.assertEquals(value = (short) random.nextInt(), (short) testClient.primitiveWrapperShortWithPrimitivePrimitiveWrapper(value));

        Assert.assertNull(testClient.primitiveWrapperShortWithPrimitiveWrapperPrimitiveWrapper(null));
        Assert.assertEquals(value = (short) random.nextInt(), (short) testClient.primitiveWrapperShortWithPrimitiveWrapperPrimitiveWrapper(value));
    }

    @Test
    public void primitiveInteger() {
        //primitive
        int value;

        Assert.assertEquals(value = random.nextInt(), testClient.primitiveIntegerWithPrimitivePrimitive(value));

        Assert.assertEquals(value = random.nextInt(), testClient.primitiveIntegerWithPrimitiveWrapperPrimitive(value));

        Assert.assertEquals(value = random.nextInt(), (int) testClient.primitiveIntegerWithPrimitivePrimitiveWrapper(value));

        Assert.assertEquals(value = random.nextInt(), (int) testClient.primitiveIntegerWithPrimitiveWrapperPrimitiveWrapper(value));


        // primitive wrapper
        Assert.assertEquals(value = random.nextInt(), testClient.primitiveWrapperIntegerWithPrimitivePrimitive(value));

        Assert.assertEquals(0, testClient.primitiveWrapperIntegerWithPrimitiveWrapperPrimitive(null));
        Assert.assertEquals(value = random.nextInt(), testClient.primitiveWrapperIntegerWithPrimitiveWrapperPrimitive(value));

        Assert.assertEquals(value = random.nextInt(), (int) testClient.primitiveWrapperIntegerWithPrimitivePrimitiveWrapper(value));

        Assert.assertNull(testClient.primitiveWrapperIntegerWithPrimitiveWrapperPrimitiveWrapper(null));
        Assert.assertEquals(value = random.nextInt(), (int) testClient.primitiveWrapperIntegerWithPrimitiveWrapperPrimitiveWrapper(value));
    }

    @Test
    public void primitiveLong() {
        //primitive
        long value;

        Assert.assertEquals(value = random.nextLong(), testClient.primitiveLongWithPrimitivePrimitive(value));

        Assert.assertEquals(value = random.nextLong(), testClient.primitiveLongWithPrimitiveWrapperPrimitive(value));

        Assert.assertEquals(value = random.nextLong(), (long) testClient.primitiveLongWithPrimitivePrimitiveWrapper(value));

        Assert.assertEquals(value = random.nextLong(), (long) testClient.primitiveLongWithPrimitiveWrapperPrimitiveWrapper(value));


        // primitive wrapper
        Assert.assertEquals(value = random.nextLong(), testClient.primitiveWrapperLongWithPrimitivePrimitive(value));

        Assert.assertEquals(0, testClient.primitiveWrapperLongWithPrimitiveWrapperPrimitive(null));
        Assert.assertEquals(value = random.nextLong(), testClient.primitiveWrapperLongWithPrimitiveWrapperPrimitive(value));

        Assert.assertEquals(value = random.nextLong(), (long) testClient.primitiveWrapperLongWithPrimitivePrimitiveWrapper(value));

        Assert.assertNull(testClient.primitiveWrapperLongWithPrimitiveWrapperPrimitiveWrapper(null));
        Assert.assertEquals(value = random.nextLong(), (long) testClient.primitiveWrapperLongWithPrimitiveWrapperPrimitiveWrapper(value));
    }

    @Test
    public void primitiveFloat() {
        //primitive
        float value;

        Assert.assertEquals(value = random.nextFloat(), testClient.primitiveFloatWithPrimitivePrimitive(value), 1e-10);

        Assert.assertEquals(value = random.nextFloat(), testClient.primitiveFloatWithPrimitiveWrapperPrimitive(value), 1e-10);

        Assert.assertEquals(value = random.nextFloat(), testClient.primitiveFloatWithPrimitivePrimitiveWrapper(value), 1e-10);

        Assert.assertEquals(value = random.nextFloat(), testClient.primitiveFloatWithPrimitiveWrapperPrimitiveWrapper(value), 1e-10);


        // primitive wrapper
        Assert.assertEquals(value = random.nextFloat(), testClient.primitiveWrapperFloatWithPrimitivePrimitive(value), 1e-10);

        Assert.assertEquals(0, testClient.primitiveWrapperFloatWithPrimitiveWrapperPrimitive(null), 1e-10);
        Assert.assertEquals(value = random.nextFloat(), testClient.primitiveWrapperFloatWithPrimitiveWrapperPrimitive(value), 1e-10);

        Assert.assertEquals(value = random.nextFloat(), testClient.primitiveWrapperFloatWithPrimitivePrimitiveWrapper(value), 1e-10);

        Assert.assertNull(testClient.primitiveWrapperFloatWithPrimitiveWrapperPrimitiveWrapper(null));
        Assert.assertEquals(value = random.nextFloat(), testClient.primitiveWrapperFloatWithPrimitiveWrapperPrimitiveWrapper(value), 1e-10);
    }

    @Test
    public void primitiveDouble() {
        //primitive
        double value;

        Assert.assertEquals(value = random.nextDouble(), testClient.primitiveDoubleWithPrimitivePrimitive(value), 1e-10);

        Assert.assertEquals(value = random.nextDouble(), testClient.primitiveDoubleWithPrimitiveWrapperPrimitive(value), 1e-10);

        Assert.assertEquals(value = random.nextDouble(), testClient.primitiveDoubleWithPrimitivePrimitiveWrapper(value), 1e-10);

        Assert.assertEquals(value = random.nextDouble(), testClient.primitiveDoubleWithPrimitiveWrapperPrimitiveWrapper(value), 1e-10);


        // primitive wrapper
        Assert.assertEquals(value = random.nextDouble(), testClient.primitiveWrapperDoubleWithPrimitivePrimitive(value), 1e-10);

        Assert.assertEquals(0, testClient.primitiveWrapperDoubleWithPrimitiveWrapperPrimitive(null), 1e-10);
        Assert.assertEquals(value = random.nextDouble(), testClient.primitiveWrapperDoubleWithPrimitiveWrapperPrimitive(value), 1e-10);

        Assert.assertEquals(value = random.nextDouble(), testClient.primitiveWrapperDoubleWithPrimitivePrimitiveWrapper(value), 1e-10);

        Assert.assertNull(testClient.primitiveWrapperDoubleWithPrimitiveWrapperPrimitiveWrapper(null));
        Assert.assertEquals(value = random.nextDouble(), testClient.primitiveWrapperDoubleWithPrimitiveWrapperPrimitiveWrapper(value), 1e-10);
    }

    @Test
    public void primitiveBytes() {
        byte[] data = {1, 2, 3, 4, 5};

        byte[] result = testClient.primitiveBytes(data);
        Assert.assertArrayEquals(data, result);
    }

    @Test
    public void bigInteger() {
        BigInteger bigInteger = new BigInteger(Long.toString(random.nextLong()));
        Assert.assertNull(testClient.bigInteger(null));
        Assert.assertEquals(bigInteger, testClient.bigInteger(bigInteger));
    }

    @Test
    public void bigDecimal() {
        BigDecimal bigDecimal = new BigDecimal(Double.toString(random.nextDouble()));
        Assert.assertNull(testClient.bigDecimal(null));
        Assert.assertEquals(bigDecimal, testClient.bigDecimal(bigDecimal));
    }

    @Test
    public void testEndpointNull() {
        try {
            AresContext.setEndpoint(null, null, null);
            String result = testClient.zeroRequestParamWithOneMoreParam("hello");
            Assert.assertEquals("zeroRequestParam()[]", result);
        } finally {
            AresContext.removeEndpoint();
        }
    }

    @Test
    public void testEndpointSchema() {
        try {
            AresContext.setEndpoint(SchemaType.https, null, null);
            testClient.zeroRequestParamWithOneMoreParam("hello");
        } catch (Exception e) {
            Assert.assertEquals("http request error, url=https://127.0.0.1:9999/zeroRequestParam?param1=hello; message=Unrecognized SSL message, plaintext connection?", e.getMessage());
            return;
        } finally {
            AresContext.removeEndpoint();
        }

        throw new Error();
    }

    @Test
    public void testEndpointHost() {
        try {
            AresContext.setEndpoint(null, "unknown", null);
            testClient.zeroRequestParamWithOneMoreParam("hello");
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().startsWith("http request error, url=http://unknown:9999/zeroRequestParam?param1=hello; message=unknown"));
            return;
        } finally {
            AresContext.removeEndpoint();
        }

        throw new Error();
    }

    @Test
    public void testEndpointPort() {
        try {
            AresContext.setEndpoint(null, null, 12345);
            testClient.zeroRequestParamWithOneMoreParam("hello");
        } catch (Exception e) {
            Assert.assertEquals("http request error, url=http://127.0.0.1:12345/zeroRequestParam?param1=hello; message=Connect to 127.0.0.1:12345 [/127.0.0.1] failed: Connection refused (Connection refused)", e.getMessage());
            return;
        } finally {
            AresContext.removeEndpoint();
        }

        throw new Error();
    }

    @Test
    public void testEndpointSchemaHost() {
        try {
            AresContext.setEndpoint(SchemaType.https, "unknown", null);
            testClient.zeroRequestParamWithOneMoreParam("hello");
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().startsWith("http request error, url=https://unknown:9999/zeroRequestParam?param1=hello; message=unknown"));
            return;
        } finally {
            AresContext.removeEndpoint();
        }

        throw new Error();
    }

    @Test
    public void testEndpointSchemaPort() {
        try {
            AresContext.setEndpoint(SchemaType.https, null, 12345);
            testClient.zeroRequestParamWithOneMoreParam("hello");
        } catch (Exception e) {
            Assert.assertEquals("http request error, url=https://127.0.0.1:12345/zeroRequestParam?param1=hello; message=Connect to 127.0.0.1:12345 [/127.0.0.1] failed: Connection refused (Connection refused)", e.getMessage());
            return;
        } finally {
            AresContext.removeEndpoint();
        }

        throw new Error();
    }

    @Test
    public void testEndpointHostPort() {
        try {
            AresContext.setEndpoint(null, "unknown", 12345);
            testClient.zeroRequestParamWithOneMoreParam("hello");
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().startsWith("http request error, url=http://unknown:12345/zeroRequestParam?param1=hello; message=unknown"));
            return;
        } finally {
            AresContext.removeEndpoint();
        }

        throw new Error();
    }

    @Test
    public void testEndpointSchemaHostPort() {
        try {
            AresContext.setEndpoint(SchemaType.https, "unknown", 12345);
            testClient.zeroRequestParamWithOneMoreParam("hello");
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().startsWith("http request error, url=https://unknown:12345/zeroRequestParam?param1=hello; message=unknown"));
            return;
        } finally {
            AresContext.removeEndpoint();
        }

        throw new Error();
    }

    @Test
    public void testEndpointSchemaLambda() {
        try {
            AresContext.invokeWithEndpoint(SchemaType.https, null, null, () ->
                    testClient.zeroRequestParamWithOneMoreParam("hello")
            );
        } catch (Exception e) {
            Assert.assertEquals("http request error, url=https://127.0.0.1:9999/zeroRequestParam?param1=hello; message=Unrecognized SSL message, plaintext connection?", e.getMessage());
            return;
        }

        throw new Error();
    }

    @Test
    public void testEndpointHostLambda() {
        try {
            AresContext.invokeWithEndpoint(null, "unknown", null, () ->
                    testClient.zeroRequestParamWithOneMoreParam("hello")
            );
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().startsWith("http request error, url=http://unknown:9999/zeroRequestParam?param1=hello; message=unknown"));
            return;
        }

        throw new Error();
    }

    @Test
    public void testEndpointPortLambda() {
        try {
            AresContext.invokeWithEndpoint(null, null, 12345, () ->
                    testClient.zeroRequestParamWithOneMoreParam("hello")
            );
        } catch (Exception e) {
            Assert.assertEquals("http request error, url=http://127.0.0.1:12345/zeroRequestParam?param1=hello; message=Connect to 127.0.0.1:12345 [/127.0.0.1] failed: Connection refused (Connection refused)", e.getMessage());
            return;
        }

        throw new Error();
    }

    @Test
    public void testEndpointSchemaHostLambda() {
        try {
            AresContext.invokeWithEndpoint(SchemaType.https, "unknown", null, () ->
                    testClient.zeroRequestParamWithOneMoreParam("hello")
            );
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().startsWith("http request error, url=https://unknown:9999/zeroRequestParam?param1=hello; message=unknown"));
            return;
        }

        throw new Error();
    }

    @Test
    public void testEndpointSchemaPortLambda() {
        try {
            AresContext.invokeWithEndpoint(SchemaType.https, null, 12345, () ->
                    testClient.zeroRequestParamWithOneMoreParam("hello")
            );
        } catch (Exception e) {
            Assert.assertEquals("http request error, url=https://127.0.0.1:12345/zeroRequestParam?param1=hello; message=Connect to 127.0.0.1:12345 [/127.0.0.1] failed: Connection refused (Connection refused)", e.getMessage());
            return;
        }

        throw new Error();
    }

    @Test
    public void testEndpointHostPortLambda() {
        try {
            AresContext.invokeWithEndpoint(null, "unknown", 12345, () ->
                    testClient.zeroRequestParamWithOneMoreParam("hello")
            );
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().startsWith("http request error, url=http://unknown:12345/zeroRequestParam?param1=hello; message=unknown"));
            return;
        }

        throw new Error();
    }

    @Test
    public void testEndpointSchemaHostPortLambda() {
        try {
            AresContext.invokeWithEndpoint(SchemaType.https, "unknown", 12345, () ->
                    testClient.zeroRequestParamWithOneMoreParam("hello")
            );
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().startsWith("http request error, url=https://unknown:12345/zeroRequestParam?param1=hello; message=unknown"));
            return;
        }

        throw new Error();
    }
}
