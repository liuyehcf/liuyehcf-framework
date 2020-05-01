package com.github.liuyehcf.framework.rpc.ares.test.ares;

import com.github.liuyehcf.framework.rpc.ares.*;
import com.github.liuyehcf.framework.rpc.ares.test.model.Person;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/11/8
 */
@SuppressWarnings("all")
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

    @AresMethod(path = "/status404")
    Object status404();

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

    // integer
    @AresMethod(path = "/primitiveInteger")
    int primitiveIntegerWithPrimitivePrimitive(@AresRequestParam(name = "param1") int param1);

    @AresMethod(path = "/primitiveInteger")
    int primitiveIntegerWithPrimitiveWrapperPrimitive(@AresRequestParam(name = "param1") Integer param1);

    @AresMethod(path = "/primitiveInteger")
    Integer primitiveIntegerWithPrimitivePrimitiveWrapper(@AresRequestParam(name = "param1") int param1);

    @AresMethod(path = "/primitiveInteger")
    Integer primitiveIntegerWithPrimitiveWrapperPrimitiveWrapper(@AresRequestParam(name = "param1") Integer param1);

    @AresMethod(path = "/primitiveWrapperInteger")
    int primitiveWrapperIntegerWithPrimitivePrimitive(@AresRequestParam(name = "param1") int param1);

    @AresMethod(path = "/primitiveWrapperInteger")
    int primitiveWrapperIntegerWithPrimitiveWrapperPrimitive(@AresRequestParam(name = "param1") Integer param1);

    @AresMethod(path = "/primitiveWrapperInteger")
    Integer primitiveWrapperIntegerWithPrimitivePrimitiveWrapper(@AresRequestParam(name = "param1") int param1);

    @AresMethod(path = "/primitiveWrapperInteger")
    Integer primitiveWrapperIntegerWithPrimitiveWrapperPrimitiveWrapper(@AresRequestParam(name = "param1") Integer param1);

    // long
    @AresMethod(path = "/primitiveLong")
    long primitiveLongWithPrimitivePrimitive(@AresRequestParam(name = "param1") long param1);

    @AresMethod(path = "/primitiveLong")
    long primitiveLongWithPrimitiveWrapperPrimitive(@AresRequestParam(name = "param1") Long param1);

    @AresMethod(path = "/primitiveLong")
    Long primitiveLongWithPrimitivePrimitiveWrapper(@AresRequestParam(name = "param1") long param1);

    @AresMethod(path = "/primitiveLong")
    Long primitiveLongWithPrimitiveWrapperPrimitiveWrapper(@AresRequestParam(name = "param1") Long param1);

    @AresMethod(path = "/primitiveWrapperLong")
    long primitiveWrapperLongWithPrimitivePrimitive(@AresRequestParam(name = "param1") long param1);

    @AresMethod(path = "/primitiveWrapperLong")
    long primitiveWrapperLongWithPrimitiveWrapperPrimitive(@AresRequestParam(name = "param1") Long param1);

    @AresMethod(path = "/primitiveWrapperLong")
    Long primitiveWrapperLongWithPrimitivePrimitiveWrapper(@AresRequestParam(name = "param1") long param1);

    @AresMethod(path = "/primitiveWrapperLong")
    Long primitiveWrapperLongWithPrimitiveWrapperPrimitiveWrapper(@AresRequestParam(name = "param1") Long param1);

    // float
    @AresMethod(path = "/primitiveFloat")
    float primitiveFloatWithPrimitivePrimitive(@AresRequestParam(name = "param1") float param1);

    @AresMethod(path = "/primitiveFloat")
    float primitiveFloatWithPrimitiveWrapperPrimitive(@AresRequestParam(name = "param1") Float param1);

    @AresMethod(path = "/primitiveFloat")
    Float primitiveFloatWithPrimitivePrimitiveWrapper(@AresRequestParam(name = "param1") float param1);

    @AresMethod(path = "/primitiveFloat")
    Float primitiveFloatWithPrimitiveWrapperPrimitiveWrapper(@AresRequestParam(name = "param1") Float param1);

    @AresMethod(path = "/primitiveWrapperFloat")
    float primitiveWrapperFloatWithPrimitivePrimitive(@AresRequestParam(name = "param1") float param1);

    @AresMethod(path = "/primitiveWrapperFloat")
    float primitiveWrapperFloatWithPrimitiveWrapperPrimitive(@AresRequestParam(name = "param1") Float param1);

    @AresMethod(path = "/primitiveWrapperFloat")
    Float primitiveWrapperFloatWithPrimitivePrimitiveWrapper(@AresRequestParam(name = "param1") float param1);

    @AresMethod(path = "/primitiveWrapperFloat")
    Float primitiveWrapperFloatWithPrimitiveWrapperPrimitiveWrapper(@AresRequestParam(name = "param1") Float param1);

    // double
    @AresMethod(path = "/primitiveDouble")
    double primitiveDoubleWithPrimitivePrimitive(@AresRequestParam(name = "param1") double param1);

    @AresMethod(path = "/primitiveDouble")
    double primitiveDoubleWithPrimitiveWrapperPrimitive(@AresRequestParam(name = "param1") Double param1);

    @AresMethod(path = "/primitiveDouble")
    Double primitiveDoubleWithPrimitivePrimitiveWrapper(@AresRequestParam(name = "param1") double param1);

    @AresMethod(path = "/primitiveDouble")
    Double primitiveDoubleWithPrimitiveWrapperPrimitiveWrapper(@AresRequestParam(name = "param1") Double param1);

    @AresMethod(path = "/primitiveWrapperDouble")
    double primitiveWrapperDoubleWithPrimitivePrimitive(@AresRequestParam(name = "param1") double param1);

    @AresMethod(path = "/primitiveWrapperDouble")
    double primitiveWrapperDoubleWithPrimitiveWrapperPrimitive(@AresRequestParam(name = "param1") Double param1);

    @AresMethod(path = "/primitiveWrapperDouble")
    Double primitiveWrapperDoubleWithPrimitivePrimitiveWrapper(@AresRequestParam(name = "param1") double param1);

    @AresMethod(path = "/primitiveWrapperDouble")
    Double primitiveWrapperDoubleWithPrimitiveWrapperPrimitiveWrapper(@AresRequestParam(name = "param1") Double param1);

    // bigInteger
    @AresMethod(path = "/bigInteger")
    BigInteger bigInteger(@AresRequestParam(name = "param1") BigInteger param1);

    // bigDecimal
    @AresMethod(path = "/bigDecimal")
    BigDecimal bigDecimal(@AresRequestParam(name = "param1") BigDecimal param1);

    @AresMethod(path = "primitiveBytes")
    byte[] primitiveBytes(@AresRequestBody byte[] data);
}
