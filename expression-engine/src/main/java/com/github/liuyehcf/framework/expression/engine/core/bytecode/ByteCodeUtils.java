package com.github.liuyehcf.framework.expression.engine.core.bytecode;

import com.github.liuyehcf.framework.expression.engine.core.ExpressionException;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.cf.*;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.cp.*;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.ir._invokestatic;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.ir._return;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.oc._newarray;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.sl.*;
import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.util.Map;

import static com.github.liuyehcf.framework.compile.engine.utils.Assert.assertFalse;

/**
 * ByteCode工具类
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class ByteCodeUtils {
    private static final Map<Integer, Class<? extends ByteCode>> operatorCodePool = Maps.newHashMap();
    private static final String FILED_NAME_OPERATOR_CODE = "OPERATOR_CODE";
    private static final String FILED_NAME_OPERATOR_CLASSES = "OPERATOR_CLASSES";

    static {
        /*
         * ControlTransfer
         */
        register(_goto.class);
        register(_ifeq.class);
        register(_ifge.class);
        register(_ifgt.class);
        register(_ifle.class);
        register(_iflt.class);
        register(_ifne.class);

        /*
         * Compute
         */
        register(_add.class);
        register(_and.class);
        register(_cmp.class);
        register(_div.class);
        register(_mul.class);
        register(_neg.class);
        register(_or.class);
        register(_rem.class);
        register(_shl.class);
        register(_shr.class);
        register(_sub.class);
        register(_ushr.class);
        register(_xor.class);

        /*
         * InvokeAndReturn
         */
        register(_invokestatic.class);
        register(_return.class);

        /*
         * ObjectCreate
         */
        register(_newarray.class);

        /*
         * Load
         */
        register(_aaload.class);
        register(_bconst.class);
        register(_cconst.class);
        register(_dconst.class);
        register(_lconst.class);
        register(_nconst.class);
        register(_pload.class);
        register(_sconst.class);
    }

    public static int getOperatorCode(Class<? extends ByteCode> clazz) {
        return (int) getStaticField(clazz, FILED_NAME_OPERATOR_CODE);
    }

    public static Class<?>[] getOperatorClasses(Class<? extends ByteCode> clazz) {
        return (Class<?>[]) getStaticField(clazz, FILED_NAME_OPERATOR_CLASSES);
    }

    private static Object getStaticField(Class<? extends ByteCode> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            return field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ExpressionException(e);
        }
    }

    private static void register(Class<? extends ByteCode> clazz) {
        int operatorCode = getOperatorCode(clazz);
        assertFalse(operatorCodePool.containsKey(operatorCode), "[SYSTEM_ERROR] - ByteCode OperatorCode repeated: " + Integer.toHexString(operatorCode));
        operatorCodePool.put(operatorCode, clazz);
    }

    public static Class<? extends ByteCode> getByteCodeByOperatorCode(int operatorCode) {
        return operatorCodePool.get(operatorCode);
    }
}
