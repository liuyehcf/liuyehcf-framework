package com.github.liuyehcf.framework.language.hua.cmd;

import com.github.liuyehcf.framework.language.hua.core.IntermediateInfo;
import com.github.liuyehcf.framework.language.hua.core.io.HuaClassInputStream;
import com.github.liuyehcf.framework.language.hua.runtime.HeapMemoryManagement;
import com.github.liuyehcf.framework.language.hua.runtime.RuntimeDaemon;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotNull;
import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertTrue;

/**
 * Hua执行器
 *
 * @author hechenfeng
 * @date 2018/6/24
 */
public class Hua extends BaseCmd {

    /**
     * 字节码文件路径
     */
    private String filePath;

    /**
     * Hua编译后的中间形式
     */
    private IntermediateInfo intermediateInfo;

    private Hua(String[] args) {
        super(args);
        registerOption("f", "source", false, true, "Source(.hclass) file path", (optValue) -> filePath = optValue);
    }

    public static void main(String[] args) {
        Hua hua = new Hua(args);

        try {
            hua.init();
            hua.run();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }

    }

    static IntermediateInfo load(String filePath) {

        try (HuaClassInputStream inputStream = new HuaClassInputStream(new FileInputStream(filePath))) {

            return inputStream.readHClass();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void init() throws ParseException {
        parseCmd();

        check();
    }

    private void check() {
        assertNotNull(filePath == null, "Please input the source file path by -f option, use -help see more options");

        File file = new File(filePath);

        assertTrue(file.exists(), "Source file '" + file.getAbsolutePath() + "' is not exist");
        assertTrue(file.isFile(), "Source file '" + file.getAbsolutePath() + "' is not file");
    }

    private void run() {

        intermediateInfo = load(filePath);

        HeapMemoryManagement.init();

        execute();

    }

    private void execute() {
        RuntimeDaemon daemon = new RuntimeDaemon(intermediateInfo);

        daemon.doExecute(getRemainArgs());
    }

}
