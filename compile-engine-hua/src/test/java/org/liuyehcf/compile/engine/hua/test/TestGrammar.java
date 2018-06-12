package org.liuyehcf.compile.engine.hua.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.liuyehcf.compile.engine.core.CompileResult;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.compiler.HuaResult;
import org.liuyehcf.compile.engine.hua.definition.GrammarDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.liuyehcf.compile.engine.hua.test.TestCases.*;


/**
 * @author hechenfeng
 * @date 2018/5/31
 */
public class TestGrammar {

    private static final List<String> RIGHT_CASES = new ArrayList<>();
    private static LRCompiler<HuaResult> compiler;

    static {
        RIGHT_CASES.addAll(Arrays.asList(WHILE_CASES));
        RIGHT_CASES.addAll(Arrays.asList(FOR_CASES));
        RIGHT_CASES.addAll(Arrays.asList(DO_CASES));
        RIGHT_CASES.addAll(Arrays.asList(IF_CASES));
        RIGHT_CASES.addAll(Arrays.asList(VARIABLE_DECLARATION_CASES));
        RIGHT_CASES.addAll(Arrays.asList(OPERATOR_CASES));
        RIGHT_CASES.addAll(Arrays.asList(CLASSIC_CASES));
    }

    @BeforeClass
    public static void init() {
        long start, end;
        start = System.currentTimeMillis();
        compiler = new HuaCompiler(GrammarDefinition.GRAMMAR, GrammarDefinition.LEXICAL_ANALYZER);
        end = System.currentTimeMillis();
        System.out.println("build HuaCompiler consume " + (end - start) / 1000 + "s");

        assertTrue(compiler.isLegal());
    }

    public void testRightCases() {
        for (String rightCase : RIGHT_CASES) {
            System.out.println(rightCase);
            assertTrue(compiler.compile(rightCase).isSuccess());
        }
    }

    public void printMarkdown() {
        System.out.println(compiler.getAnalysisTableMarkdownString());
    }


    @Test
    public void testNameWithKeyWord() {
        String text = "void testNameWithKeyWord() {\n" +
                "    doSomething();\n" +
                "}\n" +
                "\n" +
                "void doSomething() {\n" +
                "\n" +
                "}";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"doSomething()\":{\"byteCodes\":[],\"methodName\":\"doSomething\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}},\"testNameWithKeyWord()\":{\"byteCodes\":[{\"argumentSize\":0,\"methodName\":\"doSomething\",\"name\":\"_invokestatic\"}],\"methodName\":\"testNameWithKeyWord\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[3, 0]\":{},\"[1, 0]\":{},\"[2, 1]\":{},\"[0, -1]\":{},\"[4, 3]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testPostIncrement() {
        String text = "void testPostIncrement(){\n" +
                "  int a;\n" +
                "  a++;\n" +
                "}";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"testPostIncrement()\":{\"byteCodes\":[{\"name\":\"_iload\",\"offset\":0}],\"methodName\":\"testPostIncrement\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{},\"[2, 1]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testParamSize() {
        String text = "void add(int i, int j, int k) {\n" +
                "        int temp = i;\n" +
                "        j = k;\n" +
                "        k = temp;\n" +
                "    }";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"add(int,int,int)\":{\"byteCodes\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_iload\",\"offset\":16},{\"name\":\"_istore\",\"offset\":8},{\"name\":\"_iload\",\"offset\":24},{\"name\":\"_istore\",\"offset\":16}],\"methodName\":\"add\",\"offset\":0,\"paramSize\":3,\"paramTypeList\":[{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8},{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8},{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{\"i\":{\"name\":\"i\",\"namespace\":{\"id\":1,\"pid\":0},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"j\":{\"name\":\"j\",\"namespace\":{\"id\":1,\"pid\":0},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"k\":{\"name\":\"k\",\"namespace\":{\"id\":1,\"pid\":0},\"offset\":16,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[2, 1]\":{\"temp\":{\"name\":\"temp\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":24,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testAdd() {
        String text = "void func() {\n" +
                "        int a,b,c;\n" +
                "        c=a+b;\n" +
                "    }";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"func()\":{\"byteCodes\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iadd\"},{\"name\":\"_istore\",\"offset\":16}],\"methodName\":\"func\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{},\"[2, 1]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"b\":{\"name\":\"b\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"c\":{\"name\":\"c\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":16,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testSub() {
        String text = "void func() {\n" +
                "        int a,b,c;\n" +
                "        c=a-b;\n" +
                "    }";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"func()\":{\"byteCodes\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"offset\":16}],\"methodName\":\"func\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{},\"[2, 1]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"b\":{\"name\":\"b\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"c\":{\"name\":\"c\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":16,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testMul() {
        String text = "void func() {\n" +
                "        int a,b,c;\n" +
                "        c=a*b;\n" +
                "    }";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"func()\":{\"byteCodes\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_imul\"},{\"name\":\"_istore\",\"offset\":16}],\"methodName\":\"func\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{},\"[2, 1]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"b\":{\"name\":\"b\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"c\":{\"name\":\"c\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":16,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testDiv() {
        String text = "void func() {\n" +
                "        int a,b,c;\n" +
                "        c=a/b;\n" +
                "    }";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"func()\":{\"byteCodes\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_idiv\"},{\"name\":\"_istore\",\"offset\":16}],\"methodName\":\"func\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{},\"[2, 1]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"b\":{\"name\":\"b\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"c\":{\"name\":\"c\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":16,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testRem() {
        String text = "void func() {\n" +
                "        int a,b,c;\n" +
                "        c=a%b;\n" +
                "    }";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"func()\":{\"byteCodes\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_irem\"},{\"name\":\"_istore\",\"offset\":16}],\"methodName\":\"func\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{},\"[2, 1]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"b\":{\"name\":\"b\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"c\":{\"name\":\"c\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":16,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testShl() {
        String text = "void func() {\n" +
                "        int a,b,c;\n" +
                "        c=a<<b;\n" +
                "    }";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"func()\":{\"byteCodes\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_ishl\"},{\"name\":\"_istore\",\"offset\":16}],\"methodName\":\"func\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{},\"[2, 1]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"b\":{\"name\":\"b\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"c\":{\"name\":\"c\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":16,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testShr() {
        String text = "void func() {\n" +
                "        int a,b,c;\n" +
                "        c=a>>b;\n" +
                "    }";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"func()\":{\"byteCodes\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_ishr\"},{\"name\":\"_istore\",\"offset\":16}],\"methodName\":\"func\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{},\"[2, 1]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"b\":{\"name\":\"b\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"c\":{\"name\":\"c\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":16,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testUshr() {
        String text = "void func() {\n" +
                "        int a,b,c;\n" +
                "        c=a>>>b;\n" +
                "    }";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"func()\":{\"byteCodes\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iushr\"},{\"name\":\"_istore\",\"offset\":16}],\"methodName\":\"func\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{},\"[2, 1]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"b\":{\"name\":\"b\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"c\":{\"name\":\"c\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":16,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testBitAnd() {
        String text = "int func() {\n" +
                "        int a,b,c;\n" +
                "        c=a&b;\n" +
                "    }";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"func()\":{\"byteCodes\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iand\"},{\"name\":\"_istore\",\"offset\":16}],\"methodName\":\"func\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{},\"[2, 1]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"b\":{\"name\":\"b\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"c\":{\"name\":\"c\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":16,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testBitOr() {
        String text = "boolean func() {\n" +
                "        int a,b,c;\n" +
                "        c=a|b;\n" +
                "    }";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"func()\":{\"byteCodes\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_ior\"},{\"name\":\"_istore\",\"offset\":16}],\"methodName\":\"func\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"boolean\",\"typeWidth\":4}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{},\"[2, 1]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"b\":{\"name\":\"b\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"c\":{\"name\":\"c\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":16,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testBitXor() {
        String text = "void func() {\n" +
                "        int a,b,c;\n" +
                "        c=a^b;\n" +
                "    }";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"func()\":{\"byteCodes\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_ixor\"},{\"name\":\"_istore\",\"offset\":16}],\"methodName\":\"func\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{},\"[2, 1]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"b\":{\"name\":\"b\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"c\":{\"name\":\"c\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":16,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testNormalAssign() {
        String text = "void func() {\n" +
                "  int a,b,c;\n" +
                "  c=a+b-a;\n" +
                "  boolean d=true,f;\n" +
                "  f=d;\n" +
                "  f=false;\n" +
                "}";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"func()\":{\"byteCodes\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"offset\":16},{\"name\":\"_ldc\",\"type\":\"boolean\",\"value\":\"true\"},{\"name\":\"_istore\",\"offset\":24},{\"name\":\"_iload\",\"offset\":24},{\"name\":\"_istore\",\"offset\":28},{\"name\":\"_ldc\",\"type\":\"boolean\",\"value\":\"false\"},{\"name\":\"_istore\",\"offset\":28}],\"methodName\":\"func\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{},\"[2, 1]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"b\":{\"name\":\"b\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"c\":{\"name\":\"c\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":16,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"d\":{\"name\":\"d\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":24,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"boolean\",\"typeWidth\":4}},\"f\":{\"name\":\"f\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":28,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"boolean\",\"typeWidth\":4}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testMethodInvocation() {
        String text = "void testMethodInvocation() {\n" +
                "    int a,b,c;\n" +
                "    func1();\n" +
                "    func2(a+b,c);\n" +
                "}";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"testMethodInvocation()\":{\"byteCodes\":[{\"argumentSize\":0,\"methodName\":\"func1\",\"name\":\"_invokestatic\"},{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"offset\":16},{\"argumentSize\":2,\"methodName\":\"func2\",\"name\":\"_invokestatic\"}],\"methodName\":\"testMethodInvocation\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{},\"[2, 1]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"b\":{\"name\":\"b\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"c\":{\"name\":\"c\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":16,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testVariableInitialize() {
        String text = "void testVariableInitialize() {\n" +
                "    int a,b,c;\n" +
                "    int d=a+b-c;\n" +
                "}";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"testVariableInitialize()\":{\"byteCodes\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"offset\":16},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"offset\":24}],\"methodName\":\"testVariableInitialize\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{},\"[2, 1]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"b\":{\"name\":\"b\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"c\":{\"name\":\"c\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":16,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"d\":{\"name\":\"d\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":24,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testMixBinaryOperator() {
        String text = "void testMixBinaryOperator() {\n" +
                "    int a, b, c, d, e, f, g, h, i;\n" +
                "    int j = a + b - c * d / e % f & g ^ h | i;\n" +
                "}";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"testMixBinaryOperator()\":{\"byteCodes\":[{\"name\":\"_iload\",\"offset\":0},{\"name\":\"_iload\",\"offset\":8},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"offset\":16},{\"name\":\"_iload\",\"offset\":24},{\"name\":\"_imul\"},{\"name\":\"_iload\",\"offset\":32},{\"name\":\"_idiv\"},{\"name\":\"_iload\",\"offset\":40},{\"name\":\"_irem\"},{\"name\":\"_isub\"},{\"name\":\"_iload\",\"offset\":48},{\"name\":\"_iand\"},{\"name\":\"_iload\",\"offset\":56},{\"name\":\"_ixor\"},{\"name\":\"_iload\",\"offset\":64},{\"name\":\"_ior\"},{\"name\":\"_istore\",\"offset\":72}],\"methodName\":\"testMixBinaryOperator\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{},\"[2, 1]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"b\":{\"name\":\"b\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"c\":{\"name\":\"c\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":16,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"d\":{\"name\":\"d\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":24,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"e\":{\"name\":\"e\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":32,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"f\":{\"name\":\"f\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":40,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"g\":{\"name\":\"g\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":48,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"h\":{\"name\":\"h\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":56,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"i\":{\"name\":\"i\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":64,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"j\":{\"name\":\"j\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":72,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testDecimalLiteral() {
        String text = "void testDecimalLiteral() {\n" +
                "    int a=5,b=100000;\n" +
                "}";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"testDecimalLiteral()\":{\"byteCodes\":[{\"name\":\"_ldc\",\"type\":\"int\",\"value\":\"5\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_ldc\",\"type\":\"int\",\"value\":\"100000\"},{\"name\":\"_istore\",\"offset\":8}],\"methodName\":\"testDecimalLiteral\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{},\"[2, 1]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"b\":{\"name\":\"b\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testBooleanLiteral() {
        String text = "void testBooleanLiteral() {\n" +
                "    boolean a=true,b=false;\n" +
                "}";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"testBooleanLiteral()\":{\"byteCodes\":[{\"name\":\"_ldc\",\"type\":\"boolean\",\"value\":\"true\"},{\"name\":\"_istore\",\"offset\":0},{\"name\":\"_ldc\",\"type\":\"boolean\",\"value\":\"false\"},{\"name\":\"_istore\",\"offset\":4}],\"methodName\":\"testBooleanLiteral\",\"offset\":0,\"paramSize\":0,\"paramTypeList\":[],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{},\"[2, 1]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"boolean\",\"typeWidth\":4}},\"b\":{\"name\":\"b\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":4,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"boolean\",\"typeWidth\":4}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testArrayLoad() {
        String text = "void testArrayLoad(int[] a) {\n" +
                "    int b=a[1];\n" +
                "}";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"testArrayLoad(int[])\":{\"byteCodes\":[{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_ldc\",\"type\":\"int\",\"value\":\"1\"},{\"name\":\"_iaload\"},{\"name\":\"_istore\",\"offset\":8}],\"methodName\":\"testArrayLoad\",\"offset\":0,\"paramSize\":1,\"paramTypeList\":[{\"arrayType\":true,\"dim\":1,\"typeName\":\"int\",\"typeWidth\":8}],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[1, 0]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":1,\"pid\":0},\"offset\":0,\"type\":{\"arrayType\":true,\"dim\":1,\"typeName\":\"int\",\"typeWidth\":8}}},\"[2, 1]\":{\"b\":{\"name\":\"b\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }

    @Test
    public void testArrayStore() {
        String text = "void func(int a,int b) {\n" +
                "    int[] c;\n" +
                "    c[5] = 10000;\n" +
                "}\n" +
                "void testArrayStore(int[] a) {\n" +
                "    a[5]=100;\n" +
                "    a[5]=a[200];\n" +
                "}";

        CompileResult<HuaResult> result = compiler.compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"jSONTable\":{\"func(int,int)\":{\"byteCodes\":[{\"name\":\"_aload\",\"offset\":16},{\"name\":\"_ldc\",\"type\":\"int\",\"value\":\"5\"},{\"name\":\"_ldc\",\"type\":\"int\",\"value\":\"10000\"},{\"name\":\"_iastore\"}],\"methodName\":\"func\",\"offset\":0,\"paramSize\":2,\"paramTypeList\":[{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8},{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}},\"testArrayStore(int[])\":{\"byteCodes\":[{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_ldc\",\"type\":\"int\",\"value\":\"5\"},{\"name\":\"_ldc\",\"type\":\"int\",\"value\":\"100\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_ldc\",\"type\":\"int\",\"value\":\"5\"},{\"name\":\"_aload\",\"offset\":0},{\"name\":\"_ldc\",\"type\":\"int\",\"value\":\"200\"},{\"name\":\"_iaload\"},{\"name\":\"_iastore\"}],\"methodName\":\"testArrayStore\",\"offset\":0,\"paramSize\":1,\"paramTypeList\":[{\"arrayType\":true,\"dim\":1,\"typeName\":\"int\",\"typeWidth\":8}],\"resultType\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"void\",\"typeWidth\":0}}}}",
                result.getResult().getMethodInfoTable().toString()
        );
        assertEquals(
                "{\"jSONTable\":{\"[3, 0]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":3,\"pid\":0},\"offset\":0,\"type\":{\"arrayType\":true,\"dim\":1,\"typeName\":\"int\",\"typeWidth\":8}}},\"[1, 0]\":{\"a\":{\"name\":\"a\",\"namespace\":{\"id\":1,\"pid\":0},\"offset\":0,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}},\"b\":{\"name\":\"b\",\"namespace\":{\"id\":1,\"pid\":0},\"offset\":8,\"type\":{\"arrayType\":false,\"dim\":0,\"typeName\":\"int\",\"typeWidth\":8}}},\"[2, 1]\":{\"c\":{\"name\":\"c\",\"namespace\":{\"id\":2,\"pid\":1},\"offset\":16,\"type\":{\"arrayType\":true,\"dim\":1,\"typeName\":\"int\",\"typeWidth\":8}}},\"[0, -1]\":{},\"[4, 3]\":{}}}",
                result.getResult().getVariableSymbolTable().toString()
        );
    }
}