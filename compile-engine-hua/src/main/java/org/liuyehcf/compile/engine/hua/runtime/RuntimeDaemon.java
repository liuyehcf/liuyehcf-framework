package org.liuyehcf.compile.engine.hua.runtime;

import org.liuyehcf.compile.engine.core.utils.ListUtils;
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
        MethodInfo mainMethod = intermediateInfo.getMethodInfoTable().getMethodByMethodSignature(buildMethodSignature("main", ListUtils.of(Type.TYPE_STRING_ARRAY)));
        if (mainMethod == null) {
            throw new RuntimeException("The source file does not define the main method");
        }
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
