package org.liuyehcf.compile.engine.hua.cmd;

import org.liuyehcf.compile.engine.hua.core.IntermediateInfo;
import org.liuyehcf.compile.engine.hua.core.io.HuaClassInputStream;
import org.liuyehcf.compile.engine.hua.runtime.RuntimeDaemon;

import java.io.FileInputStream;
import java.io.IOException;

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

        hua.run();
    }

    private void run() {

        load();

        execute();

    }

    private void load() {

        try (HuaClassInputStream inputStream = new HuaClassInputStream(new FileInputStream(filePath))) {

            intermediateInfo = inputStream.readHClass();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void execute() {
        RuntimeDaemon daemon = new RuntimeDaemon(intermediateInfo);

        daemon.doExecute();
    }

}
