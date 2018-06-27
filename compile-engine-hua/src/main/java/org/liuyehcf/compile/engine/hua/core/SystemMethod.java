package org.liuyehcf.compile.engine.hua.core;

import org.liuyehcf.compile.engine.core.utils.ListUtils;
import org.liuyehcf.compile.engine.core.utils.Pair;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;

import java.util.HashMap;
import java.util.Map;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertTrue;
import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.NORMAL_BOOLEAN;
import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.NORMAL_INT;

/**
 * @author chenlu
 * @date 2018/6/27
 */
public class SystemMethod {

    static final Map<MethodSignature, Pair<MethodInfo, ProxyInvoke>> SYSTEM_METHOD_POOL;
    private static final MethodSignature PRINT_INT = new MethodSignature("print", new String[]{NORMAL_INT});
    private static final MethodSignature PRINT_BOOLEAN = new MethodSignature("print", new String[]{NORMAL_BOOLEAN});

    static {
        SYSTEM_METHOD_POOL = new HashMap<>();
        SYSTEM_METHOD_POOL.put(PRINT_INT,
                new Pair<>(
                        createFakeMethodInfo(PRINT_INT, Type.TYPE_VOID),
                        (args) -> {
                            int intArg = (int) args[0];
                            System.out.println(intArg);
                            return null;
                        }
                ));
        SYSTEM_METHOD_POOL.put(PRINT_BOOLEAN,
                new Pair<>(
                        createFakeMethodInfo(PRINT_BOOLEAN, Type.TYPE_VOID),
                        (args) -> {
                            boolean booleanArg = (int) args[0] == 1;
                            System.out.println(booleanArg);
                            return null;
                        }
                ));
    }

    private static MethodInfo createFakeMethodInfo(MethodSignature methodSignature, Type resultType) {
        String[] typeDescriptions = methodSignature.getTypeDescriptions();

        Type[] paramTypes = new Type[typeDescriptions.length];

        for (int i = 0; i < typeDescriptions.length; i++) {
            paramTypes[i] = Type.parse(typeDescriptions[i]);
        }

        MethodInfo methodInfo = new MethodInfo();

        methodInfo.setMethodName(methodSignature.getMethodName());
        methodInfo.setParamTypeList(ListUtils.of(paramTypes));
        methodInfo.setResultType(resultType);

        return methodInfo;
    }

    public static Object invoke(MethodSignature methodSignature, Object[] args) {
        assertTrue(SYSTEM_METHOD_POOL.containsKey(methodSignature));
        return SYSTEM_METHOD_POOL.get(methodSignature).getSecond().invoke(args);
    }

    private interface ProxyInvoke {
        Object invoke(Object[] args);
    }
}
