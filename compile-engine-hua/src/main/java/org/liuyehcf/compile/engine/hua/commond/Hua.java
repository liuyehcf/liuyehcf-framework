package org.liuyehcf.compile.engine.hua.commond;

import org.liuyehcf.compile.engine.hua.commond.io.HuaClassInputStream;
import org.liuyehcf.compile.engine.hua.commond.runtime.MethodRuntimeInfo;
import org.liuyehcf.compile.engine.hua.commond.runtime.MethodStack;
import org.liuyehcf.compile.engine.hua.compiler.IntermediateInfo;
import org.liuyehcf.compile.engine.hua.compiler.MethodInfo;

import java.io.FileInputStream;
import java.io.IOException;

import static org.liuyehcf.compile.engine.hua.compiler.MethodInfo.buildMethodSignature;

/**
 * Hua执行器
 *
 * @author hechenfeng
 * @date 2018/6/24
 */
public class Hua {

    /**
     * 字节码文件路径
     */
    private final String filePath;

    /**
     * Hua编译后的中间形式
     */
    private IntermediateInfo intermediateInfo;

    private Hua(String filePath) {
        this.filePath = filePath;
    }

    public static void main(String[] args) {
        if (args == null || args.length != 1 || args[0] == null) {
            System.err.println("请输入 '.hclass' 文件的路径");
            return;
        }

        Hua hua = new Hua(args[0]);

        hua.execute();
    }

    private void execute() {

        load();

        run();

    }

    private void load() {

        try (HuaClassInputStream inputStream = new HuaClassInputStream(new FileInputStream(filePath))) {

            intermediateInfo = inputStream.readHClass();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void run() {
        MethodStack methodStack = new MethodStack();

        MethodInfo mainMethod = getMainMethod();

        methodStack.push(new MethodRuntimeInfo(mainMethod));
    }

    private MethodInfo getMainMethod() {
        MethodInfo mainMethod = intermediateInfo.getMethodInfoTable().getMethodByMethodSignature(buildMethodSignature("main", null));
        if (mainMethod == null) {
            throw new RuntimeException("源文件没有定义main方法");
        }
        return mainMethod;
    }

}
