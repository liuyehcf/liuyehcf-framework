package org.liuyehcf.compile.engine.hua.cmd;

import org.apache.commons.cli.ParseException;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.core.IntermediateInfo;
import org.liuyehcf.compile.engine.hua.core.MethodInfo;
import org.liuyehcf.compile.engine.hua.core.bytecode.ByteCode;

import java.io.File;
import java.util.List;

import static org.liuyehcf.compile.engine.hua.cmd.Hua.load;

/**
 * Hua反编译命令行工具
 *
 * @author hechenfeng
 * @date 2018/6/24
 */
public class Huap extends BaseCmd {

    /**
     * 字节码文件路径
     */
    private String filePath;

    /**
     * Hua编译后的中间形式
     */
    private IntermediateInfo intermediateInfo;

    private Huap(String[] args) {
        super(args);
        registerOption("f", "source", false, true, "Source(.hclass) file path", (optValue) -> filePath = optValue);
    }

    public static void main(String[] args) {
        Huap Huap = new Huap(args);

        try {
            Huap.init();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }

        Huap.parse();
    }

    private void init() throws ParseException {
        parseCmd();

        check();
    }

    private void check() {
        if (filePath == null) {
            throw new RuntimeException("Please input the source file path by -f option, use -help see more options");
        }

        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            throw new RuntimeException("Please input the source file path by -f option, use -help see more options");
        }
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
        System.out.println(methodInfo.getMethodSignature().getSignature());
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
