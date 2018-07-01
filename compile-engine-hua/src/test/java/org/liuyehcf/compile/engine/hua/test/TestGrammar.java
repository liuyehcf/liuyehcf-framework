package org.liuyehcf.compile.engine.hua.test;

import org.junit.Test;
import org.liuyehcf.compile.engine.core.CompileResult;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.hua.compile.HuaCompiler;
import org.liuyehcf.compile.engine.hua.compile.definition.GrammarDefinition;
import org.liuyehcf.compile.engine.hua.core.IntermediateInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.liuyehcf.compile.engine.hua.compile.HuaCompiler.HUA_PATH_PROPERTY;


/**
 * 测试
 *
 * @author hechenfeng
 * @date 2018/5/31
 */
public class TestGrammar {

    private static LRCompiler<IntermediateInfo> compiler;

    static LRCompiler<IntermediateInfo> getCompiler() {
        if (compiler == null) {
            System.setProperty(HUA_PATH_PROPERTY, "./src/main/resources/");

            long start, end;
            start = System.currentTimeMillis();

            compiler = HuaCompiler.getHuaCompiler();

            end = System.currentTimeMillis();
            System.out.println("build HuaCompiler consume " + (end - start) / 1000 + "s");

            assertTrue(compiler.isLegal());
        }
        return compiler;
    }

    @Test
    public void testProductions() {
        System.out.println(GrammarDefinition.GRAMMAR);
    }

    @Test
    public void testFirstJSONString() {
        System.out.println(compiler.getFirstJSONString());
    }

    @Test
    public void testFollowJSONString() {
        System.out.println(compiler.getFollowJSONString());
    }

    @Test
    public void testClosureJSONString() {
        System.out.println(compiler.getClosureJSONString());
    }

    @Test
    public void testClosureTransferTableJSONString() {
        System.out.println(compiler.getClosureTransferTableJSONString());
    }

    @Test
    public void testMarkDownString() {
        System.out.println(compiler.getAnalysisTableMarkdownString());
    }

    @Test
    public void testParamSize() {
        String text = "void func(int i, int j, int k) {\n" +
                "\tint temp = i;\n" +
                "\tj = k;\n" +
                "\tk = temp;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func(int,int,int)\":[{\"name\":\"_iload\",\"order\":0},{\"name\":\"_istore\",\"order\":3},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_istore\",\"order\":2},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testTypeLong1() {
        String text = "void func() {\n" +
                "\tlong[] a=new long[5];\n" +
                "\ta[1]=1L;\n" +
                "\tlong b=a[2];\n" +
                "\tb=1000000L;\n" +
                "\tb+=a[3];\n" +
                "\ta[3]=a[2]<<a[1];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_newarray\",\"type\":\"long\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_lconst\",\"value\":1},{\"name\":\"_lastore\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_laload\"},{\"name\":\"_lstore\",\"order\":1},{\"name\":\"_lconst\",\"value\":1000000},{\"name\":\"_lstore\",\"order\":1},{\"name\":\"_lload\",\"order\":1},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_laload\"},{\"name\":\"_ladd\"},{\"name\":\"_lstore\",\"order\":1},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_laload\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_laload\"},{\"name\":\"_lshl\"},{\"name\":\"_lastore\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testTypeLong2() {
        String text = "void func() {\n" +
                "\tlong a=1;\n" +
                "\ta=0X999;\n" +
                "\ta+=0777777;\n" +
                "\ta=a-0xff1ad;\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_iconst\",\"value\":2457},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_iconst\",\"value\":262143},{\"name\":\"_ladd\"},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_lload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1044909},{\"name\":\"_lsub\"},{\"name\":\"_lstore\",\"order\":0},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testTypeFloat() {
        String text = "void func() {\n" +
                "\tfloat[] a=new float[5];\n" +
                "\ta[1]=1f;\n" +
                "\tfloat b=a[2];\n" +
                "\tb=1e+3f;\n" +
                "\tb+=a[3]-1f;\n" +
                "\tb/=0f%a[1];\n" +
                "\tb*=0.1f*a[0];\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"func()\":[{\"name\":\"_iconst\",\"value\":5},{\"name\":\"_newarray\",\"type\":\"float\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_fconst\",\"value\":1.0},{\"name\":\"_fastore\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":2},{\"name\":\"_faload\"},{\"name\":\"_fstore\",\"order\":1},{\"name\":\"_fconst\",\"value\":1000.0},{\"name\":\"_fstore\",\"order\":1},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":3},{\"name\":\"_faload\"},{\"name\":\"_fconst\",\"value\":1.0},{\"name\":\"_fsub\"},{\"name\":\"_fadd\"},{\"name\":\"_fstore\",\"order\":1},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fconst\",\"value\":0.0},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_faload\"},{\"name\":\"_frem\"},{\"name\":\"_fdiv\"},{\"name\":\"_fstore\",\"order\":1},{\"name\":\"_fload\",\"order\":1},{\"name\":\"_fconst\",\"value\":0.1},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_faload\"},{\"name\":\"_fmul\"},{\"name\":\"_fmul\"},{\"name\":\"_fstore\",\"order\":1},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }

    @Test
    public void testQuickSort() {
        String text = "void exchange(int[] nums, int i, int j) {\n" +
                "\tint temp = nums[i];\n" +
                "\tnums[i] = nums[j];\n" +
                "\tnums[j] = temp;\n" +
                "}\n" +
                "\n" +
                "int partition(int[] nums, int lo, int hi) {\n" +
                "\tint i = lo - 1;\n" +
                "\tint pivot = nums[hi];\n" +
                "\t\n" +
                "\tfor (int j = lo; j < hi; j++) {\n" +
                "\t\tif (nums[j] < pivot) {\n" +
                "\t\t\texchange(nums, ++i, j);\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\n" +
                "\texchange(nums, ++i, hi);\n" +
                "\t\n" +
                "\treturn i;\n" +
                "}\n" +
                "\n" +
                "void sort(int[] nums, int lo, int hi) {\n" +
                "\tif (lo < hi) {\n" +
                "\t\tint mid = partition(nums, lo, hi);\n" +
                "\t\tsort(nums, lo, mid - 1);\n" +
                "\t\tsort(nums, mid + 1, hi);\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "void sort(int[] nums, int size) {\n" +
                "\tsort(nums, 0, size-1);\n" +
                "}\n" +
                "\n" +
                "void main(){\n" +
                "\tint[] nums=new int[200];\n" +
                "\n" +
                "\tfor(int i=0;i<200;i++){\n" +
                "\t\tnums[i]=nextInt(0,25);\n" +
                "\t}\n" +
                "\n" +
                "\tfor(int i=0;i<200;i++){\n" +
                "\t\tprint(nums[i]);\n" +
                "\t}\n" +
                "\n" +
                "\tsort(nums,200);\n" +
                "\n" +
                "\tfor(int i=0;i<200;i++){\n" +
                "\t\tprint(nums[i]);\n" +
                "\t}\n" +
                "}";

        System.out.println(text);

        CompileResult<IntermediateInfo> result = getCompiler().compile(text);
        assertTrue(result.isSuccess());
        assertEquals(
                "{\"exchange(int[],int,int)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iaload\"},{\"name\":\"_istore\",\"order\":3},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iaload\"},{\"name\":\"_iastore\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_iastore\"},{\"name\":\"_return\"}],\"partition(int[],int,int)\":[{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_isub\"},{\"name\":\"_istore\",\"order\":3},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":2},{\"name\":\"_iaload\"},{\"name\":\"_istore\",\"order\":4},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_istore\",\"order\":5},{\"name\":\"_iload\",\"order\":5},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":25,\"name\":\"_if_icmpge\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":5},{\"name\":\"_iaload\"},{\"name\":\"_iload\",\"order\":4},{\"codeOffset\":23,\"name\":\"_if_icmpge\"},{\"name\":\"_aload\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":3},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_iload\",\"order\":5},{\"constantPoolOffset\":19,\"name\":\"_invokestatic\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":5},{\"codeOffset\":10,\"name\":\"_goto\"},{\"name\":\"_aload\",\"order\":0},{\"increment\":1,\"name\":\"_iinc\",\"order\":3},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_iload\",\"order\":2},{\"constantPoolOffset\":19,\"name\":\"_invokestatic\"},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_ireturn\"}],\"sort(int[],int,int)\":[{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iload\",\"order\":2},{\"codeOffset\":20,\"name\":\"_if_icmpge\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iload\",\"order\":2},{\"constantPoolOffset\":20,\"name\":\"_invokestatic\"},{\"name\":\"_istore\",\"order\":3},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_isub\"},{\"constantPoolOffset\":21,\"name\":\"_invokestatic\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":3},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_iadd\"},{\"name\":\"_iload\",\"order\":2},{\"constantPoolOffset\":21,\"name\":\"_invokestatic\"},{\"name\":\"_return\"}],\"sort(int[],int)\":[{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":1},{\"name\":\"_isub\"},{\"constantPoolOffset\":21,\"name\":\"_invokestatic\"},{\"name\":\"_return\"}],\"main()\":[{\"name\":\"_iconst\",\"value\":200},{\"name\":\"_newarray\",\"type\":\"int\"},{\"name\":\"_astore\",\"order\":0},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":200},{\"codeOffset\":16,\"name\":\"_if_icmpge\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_iconst\",\"value\":25},{\"constantPoolOffset\":16,\"name\":\"_invokestatic\"},{\"name\":\"_iastore\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"codeOffset\":5,\"name\":\"_goto\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":200},{\"codeOffset\":27,\"name\":\"_if_icmpge\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iaload\"},{\"constantPoolOffset\":2,\"name\":\"_invokestatic\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"codeOffset\":18,\"name\":\"_goto\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iconst\",\"value\":200},{\"constantPoolOffset\":22,\"name\":\"_invokestatic\"},{\"name\":\"_iconst\",\"value\":0},{\"name\":\"_istore\",\"order\":1},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iconst\",\"value\":200},{\"codeOffset\":41,\"name\":\"_if_icmpge\"},{\"name\":\"_aload\",\"order\":0},{\"name\":\"_iload\",\"order\":1},{\"name\":\"_iaload\"},{\"constantPoolOffset\":2,\"name\":\"_invokestatic\"},{\"increment\":1,\"name\":\"_iinc\",\"order\":1},{\"codeOffset\":32,\"name\":\"_goto\"},{\"name\":\"_return\"}]}",
                result.getResult().getMethodInfoTable().toSimpleJSONString()
        );
    }
}