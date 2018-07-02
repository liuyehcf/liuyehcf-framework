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
    public void testSingleForWhileEmptyInitEmptyConditionEmptyUpdateEmptyBody() {
        String text = "void func(int i) {\n" +
                "\tfor(;;){\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"codeOffset\":0,\"name\":\"_goto\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testSingleForWhileEmptyInitEmptyConditionEmptyUpdate() {
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
    public void testSingleForWhileEmptyInitEmptyCondition() {
        String text = "void func(int i) {\n" +
                "\tfor(;;i++){\n" +
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
    public void testSingleForWhileEmptyInitEmptyUpdate() {
        String text = "void func(int i) {\n" +
                "\tfor(;i<10;){\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":5,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testSingleForWhileEmptyConditionEmptyUpdate() {
        String text = "void func() {\n" +
                "\tfor(int i=0;;){\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":2,\"name\":\"_goto\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testSingleForWhileEmptyInit() {
        String text = "void func(int i) {\n" +
                "\tfor(;i<10;i++){\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":6,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":0,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testSingleForWhileEmptyCondition() {
        String text = "void func() {\n" +
                "\tfor(int i=0;;i++){\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":2,\"name\":\"_goto\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testSingleForWithEmptyBody() {
        String text = "void func() {\n" +
                "\tfor(int i=0;i<10;i++){\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":7,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testSingleForWithMultiInit() {
        String text = "void func() {\n" +
                "\tfor(int i=0,j=1;i<10;i++){\n" +
                "\t\tj++;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":10,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":4,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testSingleForWithMultiUpdate() {
        String text = "void func(int j) {\n" +
                "\tfor(int i=0;i<10;i++,i+=1,j--){\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":12,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":1},{\"increment\":-1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testForNestedIfThen() {
        String text = "void func(int j) {\n" +
                "\tfor(int i=0;i<10;i++){\n" +
                "\t\tif(i>5){\n" +
                "\t\t\ti++;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":11,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":9,\"name\":\"_if_icmple\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testForNestedIfThenElse() {
        String text = "void func(int j) {\n" +
                "\tfor(int i=0;i<10;i++){\n" +
                "\t\tif(i>5){\n" +
                "\t\t\ti++;\n" +
                "\t\t}else{\n" +
                "\t\t\ti+=2;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":16,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":10,\"name\":\"_if_icmple\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"codeOffset\":14,\"name\":\"_goto\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"order\":1},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testForNestedWhile() {
        String text = "void func(int j) {\n" +
                "\tfor(int i=0;i<10;i++){\n" +
                "\t\twhile(i<=5){\n" +
                "\t\t\ti++;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":12,\"name\":\"_if_icmpge\"},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":10,\"name\":\"_if_icmpgt\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"codeOffset\":5,\"name\":\"_goto\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testForNestedDoWhile() {
        String text = "void func(int j) {\n" +
                "\tfor(int i=0;i<10;i++){\n" +
                "\t\tdo{\n" +
                "\t\t\ti++;\n" +
                "\t\t}while(i>=5);\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":11,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":5},{\"codeOffset\":5,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testForNestedFor() {
        String text = "void func() {\n" +
                "\tfor(int i=0;i<10;i++){\n" +
                "\t\tfor(int j=0;j<100;j++){\n" +
                "\t\t\ti++;\n" +
                "\t\t}\n" +
                "\t\ti++;\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":10},{\"codeOffset\":16,\"name\":\"_if_icmpge\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":100},{\"codeOffset\":13,\"name\":\"_if_icmpge\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"codeOffset\":7,\"name\":\"_goto\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":0},{\"codeOffset\":2,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

}
