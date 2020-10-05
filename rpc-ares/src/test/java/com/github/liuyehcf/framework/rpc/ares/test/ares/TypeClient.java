package com.github.liuyehcf.framework.rpc.ares.test.ares;

import com.github.liuyehcf.framework.rpc.ares.AresMethod;
import com.github.liuyehcf.framework.rpc.ares.AresRequestBody;
import com.github.liuyehcf.framework.rpc.ares.AresRequestParam;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author hechenfeng
 * @date 2020/5/2
 */
public interface TypeClient {

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
