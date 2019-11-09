package com.github.liuyehcf.framework.rpc.http.test.ares;

import com.github.liuyehcf.framework.rpc.http.AresMethod;
import com.github.liuyehcf.framework.rpc.http.AresRequestBody;
import com.github.liuyehcf.framework.rpc.http.AresRequestParam;
import com.github.liuyehcf.framework.rpc.http.test.model.Person;

import java.util.Map;

/**
 * @author chenfeng.hcf
 * @date 2019/11/8
 */
//@SuppressWarnings("all")
public interface TestClient {

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

    @AresMethod(path = "/returnNull")
    String returnNull();

    @AresMethod(path = "/wrongPath")
    String wrongPath();

    // boolean
    @AresMethod(path = "/primitiveBoolean")
    boolean primitiveBooleanWithPrimitivePrimitive(@AresRequestParam(name = "param1") boolean param1);

    @AresMethod(path = "/primitiveBoolean")
    boolean primitiveBooleanWithPrimitiveWrapperPrimitive(@AresRequestParam(name = "param1") Boolean param1);

    @AresMethod(path = "/primitiveBoolean")
    Boolean primitiveBooleanWithPrimitivePrimitiveWrapper(@AresRequestParam(name = "param1") boolean param1);

    @AresMethod(path = "/primitiveBoolean")
    Boolean primitiveBooleanWithPrimitiveWrapperPrimitiveWrapper(@AresRequestParam(name = "param1") Boolean param1);

    @AresMethod(path = "/primitiveWrapperBoolean")
    boolean primitiveWrapperBooleanWithPrimitivePrimitive(@AresRequestParam(name = "param1") boolean param1);

    @AresMethod(path = "/primitiveWrapperBoolean")
    boolean primitiveWrapperBooleanWithPrimitiveWrapperPrimitive(@AresRequestParam(name = "param1") Boolean param1);

    @AresMethod(path = "/primitiveWrapperBoolean")
    Boolean primitiveWrapperBooleanWithPrimitivePrimitiveWrapper(@AresRequestParam(name = "param1") boolean param1);

    @AresMethod(path = "/primitiveWrapperBoolean")
    Boolean primitiveWrapperBooleanWithPrimitiveWrapperPrimitiveWrapper(@AresRequestParam(name = "param1") Boolean param1);

    // byte
    @AresMethod(path = "/primitiveByte")
    byte primitiveByteWithPrimitivePrimitive(@AresRequestParam(name = "param1") byte param1);

    @AresMethod(path = "/primitiveByte")
    byte primitiveByteWithPrimitiveWrapperPrimitive(@AresRequestParam(name = "param1") Byte param1);

    @AresMethod(path = "/primitiveByte")
    Byte primitiveByteWithPrimitivePrimitiveWrapper(@AresRequestParam(name = "param1") byte param1);

    @AresMethod(path = "/primitiveByte")
    Byte primitiveByteWithPrimitiveWrapperPrimitiveWrapper(@AresRequestParam(name = "param1") Byte param1);

    @AresMethod(path = "/primitiveWrapperByte")
    byte primitiveWrapperByteWithPrimitivePrimitive(@AresRequestParam(name = "param1") byte param1);

    @AresMethod(path = "/primitiveWrapperByte")
    byte primitiveWrapperByteWithPrimitiveWrapperPrimitive(@AresRequestParam(name = "param1") Byte param1);

    @AresMethod(path = "/primitiveWrapperByte")
    Byte primitiveWrapperByteWithPrimitivePrimitiveWrapper(@AresRequestParam(name = "param1") byte param1);

    @AresMethod(path = "/primitiveWrapperByte")
    Byte primitiveWrapperByteWithPrimitiveWrapperPrimitiveWrapper(@AresRequestParam(name = "param1") Byte param1);

    // short
    @AresMethod(path = "/primitiveShort")
    short primitiveShortWithPrimitivePrimitive(@AresRequestParam(name = "param1") short param1);

    @AresMethod(path = "/primitiveShort")
    short primitiveShortWithPrimitiveWrapperPrimitive(@AresRequestParam(name = "param1") Short param1);

    @AresMethod(path = "/primitiveShort")
    Short primitiveShortWithPrimitivePrimitiveWrapper(@AresRequestParam(name = "param1") short param1);

    @AresMethod(path = "/primitiveShort")
    Short primitiveShortWithPrimitiveWrapperPrimitiveWrapper(@AresRequestParam(name = "param1") Short param1);

    @AresMethod(path = "/primitiveWrapperShort")
    short primitiveWrapperShortWithPrimitivePrimitive(@AresRequestParam(name = "param1") short param1);

    @AresMethod(path = "/primitiveWrapperShort")
    short primitiveWrapperShortWithPrimitiveWrapperPrimitive(@AresRequestParam(name = "param1") Short param1);

    @AresMethod(path = "/primitiveWrapperShort")
    Short primitiveWrapperShortWithPrimitivePrimitiveWrapper(@AresRequestParam(name = "param1") short param1);

    @AresMethod(path = "/primitiveWrapperShort")
    Short primitiveWrapperShortWithPrimitiveWrapperPrimitiveWrapper(@AresRequestParam(name = "param1") Short param1);
}
