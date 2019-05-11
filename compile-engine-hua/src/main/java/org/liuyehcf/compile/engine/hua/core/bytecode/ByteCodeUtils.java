package org.liuyehcf.compile.engine.hua.core.bytecode;

import org.liuyehcf.compile.engine.hua.core.bytecode.cf.*;
import org.liuyehcf.compile.engine.hua.core.bytecode.cp.*;
import org.liuyehcf.compile.engine.hua.core.bytecode.ir.*;
import org.liuyehcf.compile.engine.hua.core.bytecode.oc._anewarray;
import org.liuyehcf.compile.engine.hua.core.bytecode.oc._multianewarray;
import org.liuyehcf.compile.engine.hua.core.bytecode.oc._newarray;
import org.liuyehcf.compile.engine.hua.core.bytecode.sl.*;
import org.liuyehcf.compile.engine.hua.core.bytecode.sm._dup2;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.liuyehcf.compile.engine.core.utils.Assert.assertFalse;

/**
 * ByteCode工具类
 *
 * @author hechenfeng
 * @date 2018/6/25
 */
public class ByteCodeUtils {
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
        register(_ifge.class);
        register(_ifgt.class);
        register(_ifle.class);
        register(_iflt.class);
        register(_ifne.class);

        /*
         * Compute
         */
        register(_dadd.class);
        register(_dcmp.class);
        register(_ddiv.class);
        register(_dmul.class);
        register(_dneg.class);
        register(_drem.class);
        register(_dsub.class);
        register(_fadd.class);
        register(_fcmp.class);
        register(_fdiv.class);
        register(_fmul.class);
        register(_fneg.class);
        register(_frem.class);
        register(_fsub.class);
        register(_iadd.class);
        register(_iand.class);
        register(_idiv.class);
        register(_iinc.class);
        register(_imul.class);
        register(_ineg.class);
        register(_ior.class);
        register(_irem.class);
        register(_ishl.class);
        register(_ishr.class);
        register(_isub.class);
        register(_iushr.class);
        register(_ixor.class);
        register(_ladd.class);
        register(_land.class);
        register(_lcmp.class);
        register(_ldiv.class);
        register(_lmul.class);
        register(_lneg.class);
        register(_lor.class);
        register(_lrem.class);
        register(_lshl.class);
        register(_lshr.class);
        register(_lsub.class);
        register(_lushr.class);
        register(_lxor.class);
        register(_sizeof.class);

        /*
         * InvokeAndReturn
         */
        register(_areturn.class);
        register(_dreturn.class);
        register(_freturn.class);
        register(_invokestatic.class);
        register(_ireturn.class);
        register(_lreturn.class);
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
        register(_daload.class);
        register(_dastore.class);
        register(_dconst.class);
        register(_dload.class);
        register(_dstore.class);
        register(_faload.class);
        register(_fastore.class);
        register(_fconst.class);
        register(_fload.class);
        register(_fstore.class);
        register(_iaload.class);
        register(_iastore.class);
        register(_iconst.class);
        register(_iload.class);
        register(_istore.class);
        register(_laload.class);
        register(_lastore.class);
        register(_lconst.class);
        register(_ldc.class);
        register(_lload.class);
        register(_lstore.class);

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
        assertFalse(operatorCodePool.containsKey(operatorCode), "[SYSTEM_ERROR] - ByteCode OperatorCode repeated: " + Integer.toHexString(operatorCode));
        operatorCodePool.put(operatorCode, clazz);
    }

    public static Class<? extends ByteCode> getByteCodeByOperatorCode(int operatorCode) {
        return operatorCodePool.get(operatorCode);
    }
}
