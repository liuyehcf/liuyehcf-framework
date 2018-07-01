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
public class TestImplicitTypeConversion {
    @Test
    public void testCompatibleType1() {
        String text = "void func(long j) {\n" +
                "\tj=1;\n" +
                "\tj+='1';\n" +
                "\tj='9'+300;\n" +
                "\tj+='a'+0xff;\n" +
                "\tj/='a'*9L;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(long)\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_ladd\"},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_iconst\",\"value\":57},{\"name\":\"_iconst\",\"value\":300},{\"name\":\"_iadd\"},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_iconst\",\"value\":97},{\"name\":\"_iconst\",\"value\":255},{\"name\":\"_iadd\"},{\"name\":\"_ladd\"},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_iconst\",\"value\":97},{\"name\":\"_lconst\",\"value\":9},{\"name\":\"_lmul\"},{\"name\":\"_ldiv\"},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testCompatibleType2() {
        String text = "void func(int i) {\n" +
                "\ti='1';\n" +
                "\ti&='9'+300;\n" +
                "\ti='a'<<005;\n" +
                "\ti^='a'>>>0Xff;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":57},{\"name\":\"_iconst\",\"value\":300},{\"name\":\"_iadd\"},{\"name\":\"_iand\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iconst\",\"value\":97},{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_ishl\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":97},{\"name\":\"_iconst\",\"value\":255},{\"name\":\"_iushr\"},{\"name\":\"_ixor\"},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testCompatibleType3() {
        String text = "void func(int i) {\n" +
                "\ti=(i>1)?'1':0xffff;\n" +
                "\tint j=(i==0||i<-3)?0777:'a';\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"codeOffset\":5,\"name\":\"_if_icmple\"},{\"name\":\"_iconst\",\"value\":49},{\"codeOffset\":6,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":65535},{\"name\":\"_istore\",\"order\":0},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":0},{\"codeOffset\":14,\"name\":\"_if_icmpeq\"},{\"name\":\"_iload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_ineg\"},{\"codeOffset\":16,\"name\":\"_if_icmpge\"},{\"name\":\"_iconst\",\"value\":511},{\"codeOffset\":17,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":97},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testCompatibleType4() {
        String text = "void func(long i) {\n" +
                "\ti=(i>'1')?'1':0xffff;\n" +
                "\tlong j=(i==0x77||i<-3L)?1000000000000000L:'a';\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(long)\":[{\"name\":\"_lload\",\"order\":0},{\"name\":\"_iconst\",\"value\":49},{\"name\":\"_lcmp\"},{\"codeOffset\":6,\"name\":\"_ifle\"},{\"name\":\"_iconst\",\"value\":49},{\"codeOffset\":7,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":65535},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_iconst\",\"value\":119},{\"name\":\"_lcmp\"},{\"codeOffset\":17,\"name\":\"_ifeq\"},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_lconst\",\"value\":3},{\"name\":\"_lneg\"},{\"name\":\"_lcmp\"},{\"codeOffset\":19,\"name\":\"_ifge\"},{\"name\":\"_lconst\",\"value\":1000000000000000},{\"codeOffset\":20,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":97},{\"name\":\"_lstore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }
}
