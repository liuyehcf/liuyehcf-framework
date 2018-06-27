package org.liuyehcf.compile.engine.hua.core.bytecode;

import org.liuyehcf.compile.engine.hua.core.bytecode.cf.*;
import org.liuyehcf.compile.engine.hua.core.bytecode.cp.*;
import org.liuyehcf.compile.engine.hua.core.bytecode.ir._areturn;
import org.liuyehcf.compile.engine.hua.core.bytecode.ir._invokestatic;
import org.liuyehcf.compile.engine.hua.core.bytecode.ir._ireturn;
import org.liuyehcf.compile.engine.hua.core.bytecode.ir._return;
import org.liuyehcf.compile.engine.hua.core.bytecode.oc._anewarray;
import org.liuyehcf.compile.engine.hua.core.bytecode.oc._multianewarray;
import org.liuyehcf.compile.engine.hua.core.bytecode.oc._newarray;
import org.liuyehcf.compile.engine.hua.core.bytecode.sl.*;
import org.liuyehcf.compile.engine.hua.core.bytecode.sm._dup2;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertFalse;

/**
 * @author hechenfeng
 * @date 2018/6/25
 */
public class ByteCodeUtil {
    private static final Map<Integer, Class<? extends ByteCode>> operatorCodePool = new HashMap<>();
    private static final String FILED_NAME_OPERATOR_CODE = "OPERATOR_CODE";
    private static final String FILED_NAME_OPERATOR_CLASSES = "OPERATOR_CLASSES";

    static {
        /*
         * ControlTransfer
         */
        register(_goto.class);
        register(_if_icmpeq.class);
        register(_if_icmpge.class);
        register(_if_icmpgt.class);
        register(_if_icmple.class);
        register(_if_icmplt.class);
        register(_if_icmpne.class);
        register(_ifeq.class);
        register(_ifne.class);

        /*
         * Compute
         */
        register(_iadd.class);
        register(_iand.class);
        register(_idiv.class);
        register(_iinc.class);
        register(_imul.class);
        register(_ior.class);
        register(_irem.class);
        register(_ishl.class);
        register(_ishr.class);
        register(_isub.class);
        register(_iushr.class);
        register(_ixor.class);

        /*
         * InvokeAndReturn
         */
        register(_areturn.class);
        register(_invokestatic.class);
        register(_ireturn.class);
        register(_return.class);

        /*
         * ObjectCreate
         */
        register(_anewarray.class);
        register(_multianewarray.class);
        register(_newarray.class);

        /*
         * StoreLoad
         */
        register(_aaload.class);
        register(_aastore.class);
        register(_aload.class);
        register(_astore.class);
        register(_baload.class);
        register(_bastore.class);
        register(_caload.class);
        register(_castore.class);
        register(_iaload.class);
        register(_iastore.class);
        register(_iconst.class);
        register(_iload.class);
        register(_istore.class);
        register(_ldc.class);

        /*
         * OperatorStackManagement
         */
        register(_dup2.class);
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
            throw new RuntimeException(e);
        }
    }

    private static void register(Class<? extends ByteCode> clazz) {
        int operatorCode = getOperatorCode(clazz);
        assertFalse(operatorCodePool.containsKey(operatorCode));
        operatorCodePool.put(operatorCode, clazz);
    }

    public static Class<? extends ByteCode> getByteCodeByOperatorCode(int operatorCode) {
        return operatorCodePool.get(operatorCode);
    }
}
