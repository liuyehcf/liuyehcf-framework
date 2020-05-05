package com.github.liuyehcf.framework.rpc.ares.test;

import com.github.liuyehcf.framework.rpc.ares.AresException;
import com.github.liuyehcf.framework.rpc.ares.constant.SchemaType;
import com.github.liuyehcf.framework.rpc.ares.test.ares.FeatureClient;
import com.github.liuyehcf.framework.rpc.ares.test.model.Person;
import com.github.liuyehcf.framework.rpc.ares.util.AresContext;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/11/8
 */
public class TestFeature extends BaseConfig {

    @Resource
    private FeatureClient featureClient;

    @Test
    public void zeroRequestParamWithOneMoreParam() {
        String result = featureClient.zeroRequestParamWithOneMoreParam("hello");
        Assert.assertEquals("zeroRequestParam()[]", result);
    }

    @Test
    public void zeroRequestParamWithOneMoreRequestBody() {
        String result = featureClient.zeroRequestParamWithOneMoreRequestBody("hello");
        Assert.assertEquals("zeroRequestParam()[]", result);
    }

    @Test
    public void testZeroRequestParam() {
        String result = featureClient.zeroRequestParam();
        Assert.assertEquals("zeroRequestParam()[]", result);
    }

    @Test
    public void oneRequestParamMissingParam() {
        try {
            featureClient.oneRequestParamMissingParam();
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertTrue(e.getMessage().contains("Required String parameter 'param1' is not present"));
            return;
        }
        throw new Error();
    }

    @Test
    public void oneRequestParamWithoutParamAnnotation() {
        try {
            featureClient.oneRequestParamWituoutParamAnnotation(null);
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresHeader' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void oneRequestParamWithBothAnnotation() {
        try {
            featureClient.oneRequestParamWithBothAnnotation(null);
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertEquals("parameter contains more than one of ares annotations", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void oneRequestParamWithRequestBody() {
        try {
            featureClient.oneRequestParamWithRequestBody(Person.builder()
                    .country("China")
                    .name("hechenfeng")
                    .age(18)
                    .build());
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertTrue(e.getMessage().contains("Required String parameter 'param1' is not present"));
            return;
        }
        throw new Error();
    }

    @Test
    public void oneRequestParamWithJsonSerializeType() {
        String result = featureClient.oneRequestParamWithJsonSerializeType(Person.builder()
                .country("China")
                .name("hechenfeng")
                .age(18)
                .build());

        Assert.assertEquals("oneRequestParam({\"age\":18,\"country\":\"China\",\"name\":\"hechenfeng\"})[]", result);
    }

    @Test
    public void oneRequestParamWithOneMoreParam() {
        String result = featureClient.oneRequestParamWithOneMoreParam(Person.builder()
                .country("China")
                .name("hechenfeng")
                .age(18)
                .build(), "hello");

        Assert.assertEquals("oneRequestParam({\"age\":18,\"country\":\"China\",\"name\":\"hechenfeng\"})[]", result);
    }

    @Test
    public void twoRequestParamMissingFirstAnnotation() {
        try {
            featureClient.twoRequestParamMissingFirstAnnotation("param1", "param2");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresHeader' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void twoRequestParamMissingSecondAnnotation() {
        try {
            featureClient.twoRequestParamMissingSecondAnnotation("param1", "param2");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresHeader' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void twoRequestParamMissingBothAnnotation() {
        try {
            featureClient.twoRequestParamMissingBothAnnotation("param1", "param2");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresHeader' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void twoRequestParamWithFirstBody() {
        try {
            featureClient.twoRequestParamWithFirstBody("param1", "param2");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertTrue(e.getMessage().contains("Required String parameter 'param1' is not present"));
            return;
        }
        throw new Error();
    }

    @Test
    public void twoRequestParamWithSecondBody() {
        try {
            featureClient.twoRequestParamWithSecondBody("param1", "param2");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertTrue(e.getMessage().contains("Required String parameter 'param2"));
            return;
        }
        throw new Error();
    }

    @Test
    public void twoRequestParamWithBothBody() {
        try {
            featureClient.twoRequestParamWithBothBody("param1", "param2");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertEquals("more than one '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void twoRequestParamWithDuplicateQueryParam() {
        try {
            featureClient.twoRequestParamWithDuplicateQueryParam("param1", "param2");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertEquals("duplicate query parameter 'param1'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void twoRequestParamWithStringString() {
        String result = featureClient.twoRequestParamWithStringString("hello", " world!");

        Assert.assertEquals("twoRequestParam(hello,  world!)[]", result);
    }

    @Test
    public void twoRequestParamWithStringJson() {
        String result = featureClient.twoRequestParamWithStringJson("hello", Person.builder()
                .country("China")
                .name("hechenfeng")
                .age(18)
                .build());

        Assert.assertEquals("twoRequestParam(hello, {\"age\":18,\"country\":\"China\",\"name\":\"hechenfeng\"})[]", result);
    }

    @Test
    public void twoRequestParamWithOneMoreParam() {
        String result = featureClient.twoRequestParamWithOneMoreParam("hello", Person.builder()
                .country("China")
                .name("hechenfeng")
                .age(18)
                .build(), "null");

        Assert.assertEquals("twoRequestParam(hello, {\"age\":18,\"country\":\"China\",\"name\":\"hechenfeng\"})[]", result);
    }

    @Test
    public void testOneRequestHeader() {
        String result = featureClient.oneRequestHeader("hello");

        Assert.assertEquals("oneRequestHeader(hello)[]", result);
    }

    @Test
    public void twoOneRequestHeader() {
        String result = featureClient.twoRequestHeader("hello", "world");

        Assert.assertEquals("twoRequestHeader(hello, world)[]", result);
    }

    @Test
    public void twoOneRequestHeaderWithJson() {
        String result = featureClient.twoRequestHeader("hello", Person.builder()
                .country("China")
                .name("hechenfeng")
                .age(18)
                .build());

        Assert.assertEquals("twoRequestHeader(hello, {\"age\":18,\"country\":\"China\",\"name\":\"hechenfeng\"})[]", result);
    }

    @Test
    public void requestBodyWithoutAnnotation() {
        try {
            featureClient.requestBodyWithoutAnnotation("hello");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresHeader' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void requestBodyWithParam() {
        try {
            featureClient.requestBodyWithParam("hello");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertTrue(e.getMessage().contains("Required request body is missing"));
            return;
        }
        throw new Error();
    }

    @Test
    public void requestBodyString() {
        String result = featureClient.requestBodyString("hello");
        Assert.assertEquals("requestBody()[hello]", result);
    }

    @Test
    public void requestBodyJson() {
        String result = featureClient.requestBodyJson(Person.builder()
                .country("China")
                .name("hechenfeng")
                .age(18)
                .build());
        Assert.assertEquals("requestBody()[{\"age\":18,\"country\":\"China\",\"name\":\"hechenfeng\"}]", result);
    }

    @Test
    public void requestBodyJsonWithOneMoreParam() {
        String result = featureClient.requestBodyWithOneMoreParam(Person.builder()
                .country("China")
                .name("hechenfeng")
                .age(18)
                .build(), "hello");
        Assert.assertEquals("requestBody()[{\"age\":18,\"country\":\"China\",\"name\":\"hechenfeng\"}]", result);
    }

    @Test
    public void oneRequestParamOneRequestBodyMissingFirstAnnotation() {
        try {
            featureClient.oneRequestParamOneRequestBodyMissingFirstAnnotation("hello", "world");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresHeader' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void oneRequestParamOneRequestBodyMissingSecondAnnotation() {
        try {
            featureClient.oneRequestParamOneRequestBodyMissingSecondAnnotation("hello", "world");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresHeader' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void oneRequestParamOneRequestBodyMissingBothAnnotation() {
        try {
            featureClient.oneRequestParamOneRequestBodyMissingBothAnnotation("hello", "world");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresHeader' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void oneRequestParamOneRequestBody() {
        Map<String, Person> result = featureClient.oneRequestParamOneRequestBody("hello", "world");

        Person person = result.get("hello");

        Assert.assertEquals(Person.builder()
                .name("world")
                .build(), person);
    }

    @Test
    public void oneRequestParamOneRequestBodyWithOneMoreParam() {
        Map<String, Person> result = featureClient.oneRequestParamOneRequestBodyWithOneMoreParam("hello", "world", "!");

        Person person = result.get("hello");

        Assert.assertEquals(Person.builder()
                .name("world")
                .build(), person);
    }

    @Test
    public void nullableQueryParamAndRequestBodyBothNull() {
        String result = featureClient.nullableQueryParamAndRequestBody(null, null);

        Assert.assertEquals("both null", result);
    }

    @Test
    public void nullableQueryParamAndRequestBodyParam1Null() {
        String result = featureClient.nullableQueryParamAndRequestBody(null, "null");

        Assert.assertEquals("param1 null", result);
    }

    @Test
    public void nullableQueryParamAndRequestBodyRequestBodyNull() {
        String result = featureClient.nullableQueryParamAndRequestBody("null", null);

        Assert.assertEquals("requestBody null", result);
    }

    @Test
    public void nullableQueryParamAndRequestBodyBothNotNull() {
        String result = featureClient.nullableQueryParamAndRequestBody("null", "null");

        Assert.assertEquals("both not null", result);
    }

    @Test
    public void nullableQueryParamAndRequestBodyVoidReturn1() {
        featureClient.nullableQueryParamAndRequestBodyVoidReturn1("null", "null");
    }

    @Test
    public void nullableQueryParamAndRequestBodyVoidReturn2() {
        featureClient.nullableQueryParamAndRequestBodyVoidReturn2("null", "null");
    }

    @Test
    public void differentPathVariable() {
        Assert.assertEquals("/differentPathVariable/abc/def", featureClient.differentPathVariable("abc", "def"));
    }

    @Test
    public void samePathVariable() {
        Assert.assertEquals("/samePathVariable/abc/abc", featureClient.samePathVariable("abc"));
    }

    @Test
    public void returnNull() {
        Assert.assertNull(featureClient.returnNull());
    }

    @Test
    public void customizeContentType() {
        String result = featureClient.customizeContentType(null, "hello");
        Assert.assertEquals("application/test1; charset=UTF-8", result);

        result = featureClient.customizeContentType("application/test2; charset=UTF-8", "hello");
        Assert.assertEquals("application/test1; charset=UTF-8", result);
    }

    @Test
    public void status500() {
        try {
            featureClient.status500();
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Throwable cause = e.getCause();
            Assert.assertEquals(cause.getClass(), RuntimeException.class);
            Assert.assertTrue(cause.getMessage().contains("server error"));
            return;
        }
        throw new RuntimeException();
    }

    @Test
    public void wrongPath() {
        try {
            featureClient.wrongPath();
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertTrue(e.getMessage().contains("Not Found"));
            return;
        }
        throw new Error();
    }

    @Test
    public void testEndpointNull() {
        try {
            AresContext.setEndpoint(null, null, null);
            String result = featureClient.zeroRequestParamWithOneMoreParam("hello");
            Assert.assertEquals("zeroRequestParam()[]", result);
        } finally {
            AresContext.removeEndpoint();
        }
    }

    @Test
    public void testEndpointSchema() {
        try {
            AresContext.setEndpoint(SchemaType.https, null, null);
            featureClient.zeroRequestParamWithOneMoreParam("hello");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
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
            featureClient.zeroRequestParamWithOneMoreParam("hello");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
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
            featureClient.zeroRequestParamWithOneMoreParam("hello");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
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
            featureClient.zeroRequestParamWithOneMoreParam("hello");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
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
            featureClient.zeroRequestParamWithOneMoreParam("hello");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
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
            featureClient.zeroRequestParamWithOneMoreParam("hello");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
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
            featureClient.zeroRequestParamWithOneMoreParam("hello");
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
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
                    featureClient.zeroRequestParamWithOneMoreParam("hello")
            );
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertEquals("http request error, url=https://127.0.0.1:9999/zeroRequestParam?param1=hello; message=Unrecognized SSL message, plaintext connection?", e.getMessage());
            return;
        }

        throw new Error();
    }

    @Test
    public void testEndpointHostLambda() {
        try {
            AresContext.invokeWithEndpoint(null, "unknown", null, () ->
                    featureClient.zeroRequestParamWithOneMoreParam("hello")
            );
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertTrue(e.getMessage().startsWith("http request error, url=http://unknown:9999/zeroRequestParam?param1=hello; message=unknown"));
            return;
        }

        throw new Error();
    }

    @Test
    public void testEndpointPortLambda() {
        try {
            AresContext.invokeWithEndpoint(null, null, 12345, () ->
                    featureClient.zeroRequestParamWithOneMoreParam("hello")
            );
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertEquals("http request error, url=http://127.0.0.1:12345/zeroRequestParam?param1=hello; message=Connect to 127.0.0.1:12345 [/127.0.0.1] failed: Connection refused (Connection refused)", e.getMessage());
            return;
        }

        throw new Error();
    }

    @Test
    public void testEndpointSchemaHostLambda() {
        try {
            AresContext.invokeWithEndpoint(SchemaType.https, "unknown", null, () ->
                    featureClient.zeroRequestParamWithOneMoreParam("hello")
            );
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertTrue(e.getMessage().startsWith("http request error, url=https://unknown:9999/zeroRequestParam?param1=hello; message=unknown"));
            return;
        }

        throw new Error();
    }

    @Test
    public void testEndpointSchemaPortLambda() {
        try {
            AresContext.invokeWithEndpoint(SchemaType.https, null, 12345, () ->
                    featureClient.zeroRequestParamWithOneMoreParam("hello")
            );
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertEquals("http request error, url=https://127.0.0.1:12345/zeroRequestParam?param1=hello; message=Connect to 127.0.0.1:12345 [/127.0.0.1] failed: Connection refused (Connection refused)", e.getMessage());
            return;
        }

        throw new Error();
    }

    @Test
    public void testEndpointHostPortLambda() {
        try {
            AresContext.invokeWithEndpoint(null, "unknown", 12345, () ->
                    featureClient.zeroRequestParamWithOneMoreParam("hello")
            );
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertTrue(e.getMessage().startsWith("http request error, url=http://unknown:12345/zeroRequestParam?param1=hello; message=unknown"));
            return;
        }

        throw new Error();
    }

    @Test
    public void testEndpointSchemaHostPortLambda() {
        try {
            AresContext.invokeWithEndpoint(SchemaType.https, "unknown", 12345, () ->
                    featureClient.zeroRequestParamWithOneMoreParam("hello")
            );
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), AresException.class);
            Assert.assertTrue(e.getMessage().startsWith("http request error, url=https://unknown:12345/zeroRequestParam?param1=hello; message=unknown"));
            return;
        }

        throw new Error();
    }
}
