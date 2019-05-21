package com.github.liuyehcf.framework.language.hua.compile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * @author hechenfeng
 * @date 2018/9/30
 */
public class CompilerSerializer {
    public static void main(String[] args) throws Throwable {
        if (args == null || args.length != 1) {
            throw new IllegalArgumentException("targetDirect is required");
        }

        String targetDirect = args[0];
        File file = new File(targetDirect);
        if (!file.exists()) {
            throw new IllegalArgumentException("'" + targetDirect + "' is not exists");
        }

        if (!file.isDirectory()) {
            throw new IllegalArgumentException("'" + targetDirect + "' is not directory");
        }

        String filePath = targetDirect + File.separator + HuaCompiler.HUA_SERIALIZATION_FILE;

        System.out.println("Expression Compiler Serialization filePath='" + filePath + "'");

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(HuaCompiler.getHuaCompiler());
        }
    }
}

