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
public class TestFor {
    @Test
    public void testSimpleFor1() {
        String text = "void func(int i) {\n" +
                "\tfor(;;){\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":0,\"name\":\"_goto\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testSimpleFor2() {
        String text = "void func(int j) {\n" +
                "\tfor(int i=0;i<100;i++){\n" +
                "\t\tj++;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":100},{\"codeOffset\":8,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testSimpleFor3() {
        String text = "void func(int j) {\n" +
                "\tfor(int i=0;i<100;i++,i++,i=i+1){\n" +
                "\t\tj++;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":100},{\"codeOffset\":13,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":1},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }
}
