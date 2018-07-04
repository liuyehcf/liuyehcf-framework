package org.liuyehcf.compile.engine.hua.cmd;

import org.apache.commons.cli.ParseException;
import org.liuyehcf.compile.engine.core.CompileResult;
import org.liuyehcf.compile.engine.hua.compile.HuaCompiler;
import org.liuyehcf.compile.engine.hua.core.IntermediateInfo;
import org.liuyehcf.compile.engine.hua.core.io.HuaClassOutputStream;

import java.io.*;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNotNull;
import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertTrue;
import static org.liuyehcf.compile.engine.hua.core.io.HClassConstant.HCLASS_SUFFIX;

/**
 * Hua编译命令行工具
 *
 * @author hechenfeng
 * @date 2018/6/24
 */
public class Huac extends BaseCmd {

    /**
     * 源文件路径
     */
    private String filePath;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 输出目录，默认当前路径
     */
    private String targetPath = ".";

    /**
     * 编译器
     */
    private HuaCompiler huaCompiler;

    private Huac(String[] args) {
        super(args);
        registerOption("f", "source", false, true, "Source file path", (optValue) -> filePath = optValue);
        registerOption("d", "target", false, true, "Target directory path", (optValue) -> targetPath = optValue);
    }

    public static void main(String[] args) {
        Huac huac = new Huac(args);

        try {
            huac.init();
            huac.compile();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
    }

    private void init() throws ParseException {
        parseCmd();

        check();

        String[] segments = filePath.split(java.io.File.separator);
        this.fileName = segments[segments.length - 1].substring(0, segments[segments.length - 1].length() - 4);

        huaCompiler = HuaCompiler.getHuaCompiler();
    }

    private void check() {
        File file, target;

        assertNotNull(filePath == null, "Please input the source file path by -f option, use -help see more options");

        file = new File(filePath);
        target = new File(targetPath);


        assertTrue(file.exists(), "Source file '" + filePath + "' is not exist");
        assertTrue(file.isFile(), "Source file '" + filePath + "' is not file");

        assertTrue(target.exists(), "Target directory '" + targetPath + "' is not exist");
        assertTrue(target.isDirectory(), "Target directory '" + targetPath + "' is not directory");
    }

    private void compile() {
        String fileContent = loadContent();

        CompileResult<IntermediateInfo> result = huaCompiler.compile(fileContent);

        if (!result.isSuccess()) {
            System.err.println(result.getMessage());
            System.exit(1);
        }

        store(result.getResult());
    }

    private String loadContent() {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = fileReader.readLine()) != null) {
                sb.append(line)
                        .append('\n');
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void store(IntermediateInfo intermediateInfo) {

        try (HuaClassOutputStream outputStream = new HuaClassOutputStream(new FileOutputStream(targetPath + File.separator + fileName + HCLASS_SUFFIX))) {

            outputStream.writeHClass(intermediateInfo);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
