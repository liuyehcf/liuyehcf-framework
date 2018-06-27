package org.liuyehcf.compile.engine.hua.runtime;

import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.core.IntermediateInfo;
import org.liuyehcf.compile.engine.hua.core.MethodInfo;

import java.util.List;

import static org.liuyehcf.compile.engine.hua.core.MethodInfo.buildMethodSignature;

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
        MethodInfo mainMethod = intermediateInfo.getMethodInfoTable().getMethodByMethodSignature(buildMethodSignature("main", null));
        if (mainMethod == null) {
            throw new RuntimeException("The source file does not define the main method");
        }
        return mainMethod;
    }

    public void doExecute() {
        storeConstant();

        new MethodRuntimeInfo(intermediateInfo, getMainMethod()).run(new Object[0]);
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
