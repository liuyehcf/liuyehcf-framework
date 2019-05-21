package com.github.liuyehcf.framework.language.hua.runtime;

import com.github.liuyehcf.framework.compile.engine.utils.ListUtils;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.core.IntermediateInfo;
import com.github.liuyehcf.framework.language.hua.core.MethodInfo;

import java.util.List;

import static com.github.liuyehcf.framework.compile.engine.utils.Assert.assertNotNull;
import static com.github.liuyehcf.framework.language.hua.core.MethodInfo.buildMethodSignature;

/**
 * 运行Deamon
 *
 * @author hechenfeng
 * @date 2018/6/26
 */
public class RuntimeDaemon {

    /**
     * Hua编译后的中间形式
     */
    private final IntermediateInfo intermediateInfo;

    public RuntimeDaemon(IntermediateInfo intermediateInfo) {
        this.intermediateInfo = intermediateInfo;
    }

    private MethodInfo getMainMethod() {
        MethodInfo mainMethod = intermediateInfo.getMethodInfoTable().getMethodByMethodSignature(buildMethodSignature("main", ListUtils.of(Type.TYPE_STRING_ARRAY)));
        assertNotNull(mainMethod, "[SYNTAX_ERROR] - The source file does not define the main method");
        return mainMethod;
    }

    public void doExecute(String[] args) {
        storeConstant();

        Reference reference = HeapMemoryManagement.allocate(Type.REFERENCE_TYPE_WIDTH, args.length);

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            Reference subReference = HeapMemoryManagement.allocate(Type.CHAR_TYPE_WIDTH, arg.length());
            for (int j = 0; j < arg.length(); j++) {
                HeapMemoryManagement.storeChar(subReference.getAddress() + Type.CHAR_TYPE_WIDTH * j, arg.charAt(j));
            }
            HeapMemoryManagement.storeReference(reference.getAddress() + Type.REFERENCE_TYPE_WIDTH * i, subReference);
        }

        new MethodRuntimeInfo(intermediateInfo, getMainMethod()).run(new Object[]{reference});
    }

    private void storeConstant() {
        List<String> constants = intermediateInfo.getConstantPool().getConstants();

        for (String constant : constants) {
            Reference reference = HeapMemoryManagement.allocate(Type.CHAR_TYPE_WIDTH, constant.length());

            for (int i = 0; i < constant.length(); i++) {
                HeapMemoryManagement.storeChar(reference.getAddress() + Type.CHAR_TYPE_WIDTH * i, constant.charAt(i));
            }

            HeapMemoryManagement.registerConstant(constant, reference);
        }
    }
}
