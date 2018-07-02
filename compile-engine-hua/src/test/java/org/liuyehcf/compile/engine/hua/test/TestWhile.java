package org.liuyehcf.compile.engine.hua.test;

import org.junit.Test;
import org.liuyehcf.compile.engine.core.CompileResult;
import org.liuyehcf.compile.engine.hua.core.IntermediateInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.liuyehcf.compile.engine.hua.test.TestGrammar.getCompiler;

/**
 * @author chenlu
 * @date 2018/7/1
 */
public class TestWhile {

    @Test
    public void testWhile() {
        String text = "void func1(int i){\n" +
                "\twhile(i<10){\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func1(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":5,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testWhileNestedIfThen() {
        String text = "void func(int i) {\n" +
                "\twhile(i<10){\n" +
                "\t\tif(i>5){\n" +
                "\t\t\ti++;\n" +
                "\t\t}\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":9,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":7,\"name\":\"_if_icmple\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testWhileNestedIfThenElse() {
        String text = "void func(int i) {\n" +
                "\twhile(i<10){\n" +
                "\t\tif(i>5){\n" +
                "\t\t\ti++;\n" +
                "\t\t}else{\n" +
                "\t\t\ti+=2;\n" +
                "\t\t}\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":14,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":8,\"name\":\"_if_icmple\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":12,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testWhileNestedWhile() {
        String text = "void func(int i) {\n" +
                "\twhile(i<10){\n" +
                "\t\twhile(i>5){\n" +
                "\t\t\ti++;\n" +
                "\t\t}\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":10,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":8,\"name\":\"_if_icmple\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":3,\"name\":\"_goto\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testWhileNestedDoWhile() {
        String text = "void func(int i) {\n" +
                "\twhile(i<10){\n" +
                "\t\tdo{\n" +
                "\t\t\ti++;\n" +
                "\t\t}while(i>5);\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":9,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":3,\"name\":\"_if_icmpgt\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testWhileNestedFor() {
        String text = "void func(int i) {\n" +
                "\twhile(i<10){\n" +
                "\t\tfor(;i<5;){\n" +
                "\t\t\ti++;\n" +
                "\t\t}\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":10,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":8,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":3,\"name\":\"_goto\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

}
