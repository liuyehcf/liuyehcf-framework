package org.liuyehcf.compile.engine.hua.cmd;

import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.core.IntermediateInfo;
import org.liuyehcf.compile.engine.hua.core.MethodInfo;
import org.liuyehcf.compile.engine.hua.core.bytecode.ByteCode;

import java.util.List;

import static org.liuyehcf.compile.engine.hua.cmd.Hua.load;

/**
 * Hua反编译命令行工具
 *
 * @author hechenfeng
 * @date 2018/6/24
 */
public class Huap {

    /**
     * 字节码文件路径
     */
    private final String filePath;

    /**
     * Hua编译后的中间形式
     */
    private IntermediateInfo intermediateInfo;

    public Huap(String filePath) {
        this.filePath = filePath;
    }

    public static void main(String[] args) {
        if (args == null || args.length != 1 || args[0] == null) {
            System.err.println("请输入 '.hclass' 文件的路径");
            return;
        }

        Huap Huap = new Huap(args[0]);

        Huap.parse();
    }

    private void parse() {
        intermediateInfo = load(filePath);

        printConstantPool();

        printMethods();
    }

    private void printConstantPool() {
        List<String> constants = intermediateInfo.getConstantPool().getConstants();

        System.out.println("Compiled from \"Huap.java\"\n");
        System.out.println("Constant pool:");
        for (int i = 0; i < constants.size(); i++) {
            System.out.format("\t#%-3d%-3s%s\n", i, "=", constants.get(i));
        }
        System.out.println();
    }

    private void printMethods() {
        List<MethodInfo> methodInfoList = intermediateInfo.getMethodInfoTable().getMethodInfoList();

        methodInfoList.forEach(this::printMethodInfo);
    }

    private void printMethodInfo(MethodInfo methodInfo) {
        System.out.println(methodInfo.buildMethodSignature().getSignature());
        System.out.println("\tReturn type: " + methodInfo.getResultType().toTypeDescription());

        List<Type> paramTypeList = methodInfo.getParamTypeList();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paramTypeList.size(); i++) {
            Type paramType = paramTypeList.get(i);
            sb.append(paramType.toTypeDescription());
            if (i < paramTypeList.size() - 1) {
                sb.append(", ");
            }
        }
        System.out.println("\tParam type: " + sb.toString());

        System.out.println("\tCode: ");
        List<ByteCode> byteCodes = methodInfo.getByteCodes();
        for (int i = 0; i < byteCodes.size(); i++) {
            System.out.format("\t\t%-4d%-3s%s\n", i, ":", byteCodes.get(i));
        }

        System.out.println();
    }
}
