package com.github.liuyehcf.framework.language.hua.cmd;

import com.github.liuyehcf.framework.language.hua.core.IntermediateInfo;
import org.apache.commons.cli.ParseException;

import java.io.File;

import static com.github.liuyehcf.framework.compile.engine.utils.Assert.assertNotNull;
import static com.github.liuyehcf.framework.compile.engine.utils.Assert.assertTrue;
import static com.github.liuyehcf.framework.language.hua.cmd.Hua.load;

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
        assertNotNull(filePath == null, "Please input the source file path by -f option, use -help see more options");

        File file = new File(filePath);

        assertTrue(file.exists(), "Source file '" + file.getAbsolutePath() + "' is not exist");
        assertTrue(file.isFile(), "Source file '" + file.getAbsolutePath() + "' is not file");
    }

    private void parse() {
        intermediateInfo = load(filePath);

        System.out.println("Compiled from \"Huap.java\"\n");

        System.out.println(intermediateInfo.toReadableString());
    }
}
