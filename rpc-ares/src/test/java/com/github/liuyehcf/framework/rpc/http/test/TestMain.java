package com.github.liuyehcf.framework.rpc.http.test;

import com.github.liuyehcf.framework.rpc.http.test.ares.TestClient;
import com.github.liuyehcf.framework.rpc.http.test.model.Person;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author chenfeng.hcf
 * @date 2019/11/8
 */
public class TestMain extends BaseConfig {

    @Resource
    private TestClient testClient;

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
    public void oneRequestParamWituoutParamAnnotation() {
        try {
            testClient.oneRequestParamWituoutParamAnnotation(null);
        } catch (Exception e) {
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void oneRequestParamWithBothAnnotation() {
        try {
            testClient.oneRequestParamWithBothAnnotation(null);
        } catch (Exception e) {
            Assert.assertEquals("parameter contains both '@AresRequestParam' and '@AresRequestBody'", e.getMessage());
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
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void twoRequestParamMissingSecondAnnotation() {
        try {
            testClient.twoRequestParamMissingSecondAnnotation("param1", "param2");
        } catch (Exception e) {
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void twoRequestParamMissingBothAnnotation() {
        try {
            testClient.twoRequestParamMissingBothAnnotation("param1", "param2");
        } catch (Exception e) {
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresRequestBody'", e.getMessage());
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
    public void requestBodyWithoutAnnotation() {
        try {
            testClient.requestBodyWithoutAnnotation("hello");
        } catch (Exception e) {
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresRequestBody'", e.getMessage());
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
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void oneRequestParamOneRequestBodyMissingSecondAnnotation() {
        try {
            testClient.oneRequestParamOneRequestBodyMissingSecondAnnotation("hello", "world");
        } catch (Exception e) {
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresRequestBody'", e.getMessage());
            return;
        }
        throw new Error();
    }

    @Test
    public void oneRequestParamOneRequestBodyMissingBothAnnotation() {
        try {
            testClient.oneRequestParamOneRequestBodyMissingBothAnnotation("hello", "world");
        } catch (Exception e) {
            Assert.assertEquals("parameter missing '@AresRequestParam' or '@AresRequestBody'", e.getMessage());
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
    public void returnNull() {
        Assert.assertNull(testClient.returnNull());
    }
}
