package org.liuyehcf.compile.engine.hua.compiler;

import org.liuyehcf.compile.engine.core.CompileResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Hua编译命令行工具
 *
 * @author hechenfeng
 * @date 2018/6/24
 */
public class Huac {

    private final String filePath;

    private final String targetPath;

    private final HuaCompiler huaCompiler;

    private Huac(String filePath, String targetPath) {
        check(filePath, targetPath);

        this.filePath = filePath;
        this.targetPath = targetPath;

        huaCompiler = HuaCompiler.getHuaCompiler();
    }

    public static void main(String[] args) {
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            System.err.println("请输入 '.hua' 文件的路径 以及 输出目录");
            return;
        }

        Huac huac = new Huac(args[0], args[1]);

        huac.compile();
    }

    private void check(String filePath, String targetPath) {
        File file, target;

        try {
            file = new File(filePath);
            target = new File(targetPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!file.exists() || !file.isFile()) {
            throw new RuntimeException("非法文件: " + filePath);
        }

        if (!target.exists() && !target.isDirectory()) {
            throw new RuntimeException("非法目录: " + targetPath);
        }
    }

    private void compile() {
        String fileContent = loadContent();

        CompileResult<HuaResult> result = huaCompiler.compile(fileContent);

        if (!result.isSuccess()) {
            throw new RuntimeException("存在语法错误！");
        }

        storeCode(result.getResult());
    }

    private String loadContent() {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = fileReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void storeCode(HuaResult result) {

    }
}
