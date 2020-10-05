package com.github.liuyehcf.framework.rpc.ares.test;

import com.github.liuyehcf.framework.rpc.ares.test.ares.TypeClient;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

/**
 * @author hechenfeng
 * @date 2020/5/2
 */
public class TestType extends BaseConfig {

    private final Random random = new Random();

    @Resource
    private TypeClient typeClient;

    @Test
    public void primitiveBoolean() {
        //primitive
        Assert.assertFalse(typeClient.primitiveBooleanWithPrimitivePrimitive(false));
        Assert.assertTrue(typeClient.primitiveBooleanWithPrimitivePrimitive(true));

        Assert.assertFalse(typeClient.primitiveBooleanWithPrimitiveWrapperPrimitive(null));
        Assert.assertFalse(typeClient.primitiveBooleanWithPrimitiveWrapperPrimitive(false));
        Assert.assertTrue(typeClient.primitiveBooleanWithPrimitiveWrapperPrimitive(true));

        Assert.assertFalse(typeClient.primitiveBooleanWithPrimitivePrimitiveWrapper(false));
        Assert.assertTrue(typeClient.primitiveBooleanWithPrimitivePrimitiveWrapper(true));

        Assert.assertFalse(typeClient.primitiveBooleanWithPrimitiveWrapperPrimitiveWrapper(null));
        Assert.assertFalse(typeClient.primitiveBooleanWithPrimitiveWrapperPrimitiveWrapper(false));
        Assert.assertTrue(typeClient.primitiveBooleanWithPrimitiveWrapperPrimitiveWrapper(true));


        // primitive wrapper
        Assert.assertFalse(typeClient.primitiveWrapperBooleanWithPrimitivePrimitive(false));
        Assert.assertTrue(typeClient.primitiveWrapperBooleanWithPrimitivePrimitive(true));

        Assert.assertFalse(typeClient.primitiveWrapperBooleanWithPrimitiveWrapperPrimitive(false));
        Assert.assertTrue(typeClient.primitiveWrapperBooleanWithPrimitiveWrapperPrimitive(true));

        Assert.assertFalse(typeClient.primitiveWrapperBooleanWithPrimitivePrimitiveWrapper(false));
        Assert.assertTrue(typeClient.primitiveWrapperBooleanWithPrimitivePrimitiveWrapper(true));

        Assert.assertNull(typeClient.primitiveWrapperBooleanWithPrimitiveWrapperPrimitiveWrapper(null));
        Assert.assertFalse(typeClient.primitiveWrapperBooleanWithPrimitiveWrapperPrimitiveWrapper(false));
        Assert.assertTrue(typeClient.primitiveWrapperBooleanWithPrimitiveWrapperPrimitiveWrapper(true));
    }

    @Test
    public void primitiveByte() {
        //primitive
        byte value;

        Assert.assertEquals(value = (byte) random.nextInt(), typeClient.primitiveByteWithPrimitivePrimitive(value));

        Assert.assertEquals(value = (byte) random.nextInt(), typeClient.primitiveByteWithPrimitiveWrapperPrimitive(value));

        Assert.assertEquals(value = (byte) random.nextInt(), (byte) typeClient.primitiveByteWithPrimitivePrimitiveWrapper(value));

        Assert.assertEquals(value = (byte) random.nextInt(), (byte) typeClient.primitiveByteWithPrimitiveWrapperPrimitiveWrapper(value));


        // primitive wrapper
        Assert.assertEquals(value = (byte) random.nextInt(), typeClient.primitiveWrapperByteWithPrimitivePrimitive(value));

        Assert.assertEquals(value = (byte) random.nextInt(), typeClient.primitiveWrapperByteWithPrimitiveWrapperPrimitive(value));

        Assert.assertEquals(value = (byte) random.nextInt(), (byte) typeClient.primitiveWrapperByteWithPrimitivePrimitiveWrapper(value));

        Assert.assertNull(typeClient.primitiveWrapperByteWithPrimitiveWrapperPrimitiveWrapper(null));
        Assert.assertEquals(value = (byte) random.nextInt(), (byte) typeClient.primitiveWrapperByteWithPrimitiveWrapperPrimitiveWrapper(value));
    }

    @Test
    public void primitiveShort() {
        //primitive
        short value;

        Assert.assertEquals(value = (short) random.nextInt(), typeClient.primitiveShortWithPrimitivePrimitive(value));

        Assert.assertEquals(value = (short) random.nextInt(), typeClient.primitiveShortWithPrimitiveWrapperPrimitive(value));

        Assert.assertEquals(value = (short) random.nextInt(), (short) typeClient.primitiveShortWithPrimitivePrimitiveWrapper(value));

        Assert.assertEquals(value = (short) random.nextInt(), (short) typeClient.primitiveShortWithPrimitiveWrapperPrimitiveWrapper(value));


        // primitive wrapper
        Assert.assertEquals(value = (short) random.nextInt(), typeClient.primitiveWrapperShortWithPrimitivePrimitive(value));

        Assert.assertEquals(value = (short) random.nextInt(), typeClient.primitiveWrapperShortWithPrimitiveWrapperPrimitive(value));

        Assert.assertEquals(value = (short) random.nextInt(), (short) typeClient.primitiveWrapperShortWithPrimitivePrimitiveWrapper(value));

        Assert.assertNull(typeClient.primitiveWrapperShortWithPrimitiveWrapperPrimitiveWrapper(null));
        Assert.assertEquals(value = (short) random.nextInt(), (short) typeClient.primitiveWrapperShortWithPrimitiveWrapperPrimitiveWrapper(value));
    }

    @Test
    public void primitiveInteger() {
        //primitive
        int value;

        Assert.assertEquals(value = random.nextInt(), typeClient.primitiveIntegerWithPrimitivePrimitive(value));

        Assert.assertEquals(value = random.nextInt(), typeClient.primitiveIntegerWithPrimitiveWrapperPrimitive(value));

        Assert.assertEquals(value = random.nextInt(), (int) typeClient.primitiveIntegerWithPrimitivePrimitiveWrapper(value));

        Assert.assertEquals(value = random.nextInt(), (int) typeClient.primitiveIntegerWithPrimitiveWrapperPrimitiveWrapper(value));


        // primitive wrapper
        Assert.assertEquals(value = random.nextInt(), typeClient.primitiveWrapperIntegerWithPrimitivePrimitive(value));

        Assert.assertEquals(value = random.nextInt(), typeClient.primitiveWrapperIntegerWithPrimitiveWrapperPrimitive(value));

        Assert.assertEquals(value = random.nextInt(), (int) typeClient.primitiveWrapperIntegerWithPrimitivePrimitiveWrapper(value));

        Assert.assertNull(typeClient.primitiveWrapperIntegerWithPrimitiveWrapperPrimitiveWrapper(null));
        Assert.assertEquals(value = random.nextInt(), (int) typeClient.primitiveWrapperIntegerWithPrimitiveWrapperPrimitiveWrapper(value));
    }

    @Test
    public void primitiveLong() {
        //primitive
        long value;

        Assert.assertEquals(value = random.nextLong(), typeClient.primitiveLongWithPrimitivePrimitive(value));

        Assert.assertEquals(value = random.nextLong(), typeClient.primitiveLongWithPrimitiveWrapperPrimitive(value));

        Assert.assertEquals(value = random.nextLong(), (long) typeClient.primitiveLongWithPrimitivePrimitiveWrapper(value));

        Assert.assertEquals(value = random.nextLong(), (long) typeClient.primitiveLongWithPrimitiveWrapperPrimitiveWrapper(value));


        // primitive wrapper
        Assert.assertEquals(value = random.nextLong(), typeClient.primitiveWrapperLongWithPrimitivePrimitive(value));

        Assert.assertEquals(value = random.nextLong(), typeClient.primitiveWrapperLongWithPrimitiveWrapperPrimitive(value));

        Assert.assertEquals(value = random.nextLong(), (long) typeClient.primitiveWrapperLongWithPrimitivePrimitiveWrapper(value));

        Assert.assertNull(typeClient.primitiveWrapperLongWithPrimitiveWrapperPrimitiveWrapper(null));
        Assert.assertEquals(value = random.nextLong(), (long) typeClient.primitiveWrapperLongWithPrimitiveWrapperPrimitiveWrapper(value));
    }

    @Test
    public void primitiveFloat() {
        //primitive
        float value;

        Assert.assertEquals(value = random.nextFloat(), typeClient.primitiveFloatWithPrimitivePrimitive(value), 1e-10);

        Assert.assertEquals(value = random.nextFloat(), typeClient.primitiveFloatWithPrimitiveWrapperPrimitive(value), 1e-10);

        Assert.assertEquals(value = random.nextFloat(), typeClient.primitiveFloatWithPrimitivePrimitiveWrapper(value), 1e-10);

        Assert.assertEquals(value = random.nextFloat(), typeClient.primitiveFloatWithPrimitiveWrapperPrimitiveWrapper(value), 1e-10);


        // primitive wrapper
        Assert.assertEquals(value = random.nextFloat(), typeClient.primitiveWrapperFloatWithPrimitivePrimitive(value), 1e-10);

        Assert.assertEquals(value = random.nextFloat(), typeClient.primitiveWrapperFloatWithPrimitiveWrapperPrimitive(value), 1e-10);

        Assert.assertEquals(value = random.nextFloat(), typeClient.primitiveWrapperFloatWithPrimitivePrimitiveWrapper(value), 1e-10);

        Assert.assertNull(typeClient.primitiveWrapperFloatWithPrimitiveWrapperPrimitiveWrapper(null));
        Assert.assertEquals(value = random.nextFloat(), typeClient.primitiveWrapperFloatWithPrimitiveWrapperPrimitiveWrapper(value), 1e-10);
    }

    @Test
    public void primitiveDouble() {
        //primitive
        double value;

        Assert.assertEquals(value = random.nextDouble(), typeClient.primitiveDoubleWithPrimitivePrimitive(value), 1e-10);

        Assert.assertEquals(value = random.nextDouble(), typeClient.primitiveDoubleWithPrimitiveWrapperPrimitive(value), 1e-10);

        Assert.assertEquals(value = random.nextDouble(), typeClient.primitiveDoubleWithPrimitivePrimitiveWrapper(value), 1e-10);

        Assert.assertEquals(value = random.nextDouble(), typeClient.primitiveDoubleWithPrimitiveWrapperPrimitiveWrapper(value), 1e-10);


        // primitive wrapper
        Assert.assertEquals(value = random.nextDouble(), typeClient.primitiveWrapperDoubleWithPrimitivePrimitive(value), 1e-10);

        Assert.assertEquals(value = random.nextDouble(), typeClient.primitiveWrapperDoubleWithPrimitiveWrapperPrimitive(value), 1e-10);

        Assert.assertEquals(value = random.nextDouble(), typeClient.primitiveWrapperDoubleWithPrimitivePrimitiveWrapper(value), 1e-10);

        Assert.assertNull(typeClient.primitiveWrapperDoubleWithPrimitiveWrapperPrimitiveWrapper(null));
        Assert.assertEquals(value = random.nextDouble(), typeClient.primitiveWrapperDoubleWithPrimitiveWrapperPrimitiveWrapper(value), 1e-10);
    }

    @Test
    public void primitiveBytes() {
        byte[] data = {1, 2, 3, 4, 5};

        byte[] result = typeClient.primitiveBytes(data);
        Assert.assertArrayEquals(data, result);
    }

    @Test
    public void bigInteger() {
        BigInteger bigInteger = new BigInteger(Long.toString(random.nextLong()));
        Assert.assertNull(typeClient.bigInteger(null));
        Assert.assertEquals(bigInteger, typeClient.bigInteger(bigInteger));
    }

    @Test
    public void bigDecimal() {
        BigDecimal bigDecimal = new BigDecimal(Double.toString(random.nextDouble()));
        Assert.assertNull(typeClient.bigDecimal(null));
        Assert.assertEquals(bigDecimal, typeClient.bigDecimal(bigDecimal));
    }
}
