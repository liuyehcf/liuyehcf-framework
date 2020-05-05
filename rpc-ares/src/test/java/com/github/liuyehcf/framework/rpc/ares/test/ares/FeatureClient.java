package com.github.liuyehcf.framework.rpc.ares.test.ares;

import com.github.liuyehcf.framework.rpc.ares.*;
import com.github.liuyehcf.framework.rpc.ares.test.model.Person;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/11/8
 */
@SuppressWarnings("all")
public interface FeatureClient {

    @AresMethod(path = "/zeroRequestParam")
    String zeroRequestParamWithOneMoreParam(@AresRequestParam(name = "param1") Object param);

    @AresMethod(path = "/zeroRequestParam")
    String zeroRequestParamWithOneMoreRequestBody(@AresRequestBody Object param);

    @AresMethod(path = "/zeroRequestParam")
    String zeroRequestParam();

    @AresMethod(path = "/oneRequestParam")
    String oneRequestParamMissingParam();

    @AresMethod(path = "/oneRequestParam")
    String oneRequestParamWituoutParamAnnotation(Object param);

    @AresMethod(path = "/oneRequestParam")
    String oneRequestParamWithBothAnnotation(@AresRequestParam(name = "param1")
                                             @AresRequestBody Object param);

    @AresMethod(path = "/oneRequestParam")
    String oneRequestParamWithRequestBody(@AresRequestBody Object param);

    @AresMethod(path = "/oneRequestParam")
    String oneRequestParamWithJsonSerializeType(@AresRequestParam(name = "param1") Object param);

    @AresMethod(path = "/oneRequestParam")
    String oneRequestParamWithOneMoreParam(@AresRequestParam(name = "param1") Object param,
                                           @AresRequestParam(name = "param2") Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamMissingFirstAnnotation(Object param1,
                                                 @AresRequestParam(name = "param2") Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamMissingSecondAnnotation(@AresRequestParam(name = "param1") Object param1,
                                                  Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamMissingBothAnnotation(Object param1,
                                                Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamWithFirstBody(@AresRequestBody Object param1,
                                        @AresRequestParam(name = "param2") Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamWithSecondBody(@AresRequestParam(name = "param1") Object param1,
                                         @AresRequestBody Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamWithBothBody(@AresRequestBody Object param1,
                                       @AresRequestBody Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamWithDuplicateQueryParam(@AresRequestParam(name = "param1") Object param1,
                                                  @AresRequestParam(name = "param1") Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamWithStringString(@AresRequestParam(name = "param1") Object param1,
                                           @AresRequestParam(name = "param2") Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamWithStringJson(@AresRequestParam(name = "param1") Object param1,
                                         @AresRequestParam(name = "param2") Object param2);

    @AresMethod(path = "/twoRequestParam")
    String twoRequestParamWithOneMoreParam(@AresRequestParam(name = "param1") Object param1,
                                           @AresRequestParam(name = "param2") Object param2,
                                           @AresRequestParam(name = "param3") Object param3);

    @AresMethod(path = "oneRequestHeader")
    String oneRequestHeader(@AresRequestHeader(name = "header1") String header1);

    @AresMethod(path = "twoRequestHeader")
    String twoRequestHeader(@AresRequestHeader(name = "header1") String header1,
                            @AresRequestHeader(name = "header2") Object header2);

    @AresMethod(path = "/requestBody")
    String requestBodyWithoutAnnotation(Object param1);

    @AresMethod(path = "/requestBody")
    String requestBodyWithParam(@AresRequestParam(name = "param1") Object param1);

    @AresMethod(path = "/requestBody")
    String requestBodyString(@AresRequestBody Object param1);

    @AresMethod(path = "/requestBody")
    String requestBodyJson(@AresRequestBody Object param1);

    @AresMethod(path = "/requestBody")
    String requestBodyWithOneMoreParam(@AresRequestBody Object param1,
                                       @AresRequestParam(name = "param2") Object param2);

    @AresMethod(path = "/oneRequestParamOneRequestBody")
    Map<String, Person> oneRequestParamOneRequestBodyMissingFirstAnnotation(Object param1,
                                                                            @AresRequestBody Object requestBody);

    @AresMethod(path = "/oneRequestParamOneRequestBody")
    Map<String, Person> oneRequestParamOneRequestBodyMissingSecondAnnotation(@AresRequestParam(name = "param1") Object param1,
                                                                             Object requestBody);

    @AresMethod(path = "/oneRequestParamOneRequestBody")
    Map<String, Person> oneRequestParamOneRequestBodyMissingBothAnnotation(Object param1,
                                                                           Object requestBody);

    @AresMethod(path = "/oneRequestParamOneRequestBody")
    Map<String, Person> oneRequestParamOneRequestBody(@AresRequestParam(name = "param1") Object param1,
                                                      @AresRequestBody Object requestBody);

    @AresMethod(path = "/oneRequestParamOneRequestBody")
    Map<String, Person> oneRequestParamOneRequestBodyWithOneMoreParam(@AresRequestParam(name = "param1") Object param1,
                                                                      @AresRequestBody Object requestBody,
                                                                      @AresRequestParam(name = "param2") Object param2);

    @AresMethod(path = "/nullableQueryParamAndRequestBody")
    String nullableQueryParamAndRequestBody(@AresRequestParam(name = "param1") Object param1,
                                            @AresRequestBody Object requestBody);

    @AresMethod(path = "/nullableQueryParamAndRequestBody")
    void nullableQueryParamAndRequestBodyVoidReturn1(@AresRequestParam(name = "param1") Object param1,
                                                     @AresRequestBody Object requestBody);

    @AresMethod(path = "/nullableQueryParamAndRequestBody")
    void nullableQueryParamAndRequestBodyVoidReturn2(@AresRequestParam(name = "param1") Object param1,
                                                     @AresRequestBody Object requestBody);

    @AresMethod(path = "/differentPathVariable/{one}/{another}")
    String differentPathVariable(@AresPathVariable(name = "one") String one,
                                 @AresPathVariable(name = "another") String another);

    @AresMethod(path = "/samePathVariable/{one}/{one}")
    String samePathVariable(@AresPathVariable(name = "one") String one);

    @AresMethod(path = "/returnNull")
    String returnNull();

    @AresMethod(path = "/wrongPath")
    String wrongPath();

    @AresMethod(path = "/customizeContentType")
    String customizeContentType(@AresRequestHeader(name = "contentType") String contentType,
                                @AresRequestBody(contentType = "application/test1; charset=UTF-8") String text);

    @AresMethod(path = "/status500")
    Object status500();
}
