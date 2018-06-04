package org.liuyehcf.compile.engine.hua.test;

/**
 * @author hechenfeng
 * @date 2018/6/1
 */
public class TestCases {
    static final String[] WHILE_CASES = {
            "void testWhile(){\n" +
                    "  int a,b;\n" +
                    "  while(true||false){\n" +
                    "    ;\n" +
                    "  }\n" +
                    "  while(a<b){\n" +
                    "    something();\n" +
                    "  }\n" +
                    "  while(a==3)\n" +
                    "    something();\n" +
                    "}"
    };

    static final String[] FOR_CASES = {
            "void testFor(){\n" +
                    "  for(;;)\n" +
                    "    something();\n" +
                    "\n" +
                    "  for(;;){\n" +
                    "    something();\n" +
                    "  }\n" +
                    "\n" +
                    "  for(int a,b;;)\n" +
                    "    something();\n" +
                    "\n" +
                    "  for(int b=3;;){\n" +
                    "    something();\n" +
                    "  }\n" +
                    "\n" +
                    "  for(;true;)\n" +
                    "    something();\n" +
                    "\n" +
                    "  int i,j;\n" +
                    "\n" +
                    "  for(;;i++,j++)\n" +
                    "    something();\n" +
                    "\n" +
                    "  int b;\n" +
                    "  \n" +
                    "  for(int a=3;b<6;i++,j++){\n" +
                    "    something();\n" +
                    "  }\n" +
                    "}"
    };

    static final String[] DO_CASES = {
            "void testDo(){\n" +
                    "  do {\n" +
                    "    something();\n" +
                    "  }while(true);\n" +
                    "  do {\n" +
                    "    ;\n" +
                    "  }while(false&&true);\n" +
                    "}"
    };

    static final String[] IF_CASES = {
            "void testIf(){\n" +
                    "  if(true)\n" +
                    "    something();\n" +
                    "\n" +
                    "  if(true){\n" +
                    "    something();\n" +
                    "  }\n" +
                    "\n" +
                    "  if(true){\n" +
                    "    something();\n" +
                    "  }else{\n" +
                    "    something();\n" +
                    "  }\n" +
                    "\n" +
                    "  int a;\n" +
                    "\n" +
                    "  if(a<3){\n" +
                    "    something();\n" +
                    "  }else\n" +
                    "    something();\n" +
                    "  \n" +
                    "  if(true){\n" +
                    "    something();\n" +
                    "  }else{\n" +
                    "    something();\n" +
                    "  }\n" +
                    "\n" +
                    "  int b;\n" +
                    "  \n" +
                    "  if(true){\n" +
                    "    if(b>4){\n" +
                    "      something();\n" +
                    "    }\n" +
                    "  }else{\n" +
                    "    something();\n" +
                    "  }\n" +
                    "\n" +
                    "  if(true){\n" +
                    "    while(false){\n" +
                    "      if(true){\n" +
                    "        ;\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }else\n" +
                    "    something();\n" +
                    "\n" +
                    "  if(true){\n" +
                    "    for(;;)\n" +
                    "      if(true)\n" +
                    "        ;\n" +
                    "      else{\n" +
                    "        while(false){\n" +
                    "          do {\n" +
                    "            something();\n" +
                    "          }while(false);\n" +
                    "        }\n" +
                    "      }\n" +
                    "  }else\n" +
                    "   something();\n" +
                    "}"
    };

    static final String[] VARIABLE_DECLARATION_CASES = {
            "void testDeclaration(){\n" +
                    "  int a,b,c,d;\n" +
                    "  int[][] e=new int[5][];\n" +
                    "  int f=something();\n" +
                    "}"
    };

    static final String[] OPERATOR_CASES = {
            "void testOperator() {\n" +
                    "  int a,b,z;\n" +
                    "  z = a * b;\n" +
                    "  z = a / b;\n" +
                    "  z = a % b;\n" +
                    "  z = a + b;\n" +
                    "  z = a - b;\n" +
                    "  --a;\n" +
                    "  a--;\n" +
                    "  ++a;\n" +
                    "  a++;\n" +
                    "  z = a << 1;\n" +
                    "  z = a >> 1;\n" +
                    "  z = a >>> 1;\n" +
                    "  z = a & 1;\n" +
                    "  z = a | 1;\n" +
                    "  z = a ^ 1;\n" +
                    "  z = a || b;\n" +
                    "  z = a && b;\n" +
                    "  z = b = -a;\n" +
                    "  z = b = ~a;\n" +
                    "  z = b = !a;\n" +
                    "  int c,d,e,f,g,h,j;\n" +
                    "  int k = ++a * b++ / c-- % --d + e - f << g >> h >>> ~j;\n" +
                    "}\n"
    };

    static final String[] CLASSIC_CASES = {
            "void sort(int[] nums, int size) {\n" +
                    "        sort(nums, 0, size-1);\n" +
                    "    }\n" +
                    "\n" +
                    "    void sort(int[] nums, int lo, int hi) {\n" +
                    "        if (lo < hi) {\n" +
                    "            int mid = partition(nums, lo, hi);\n" +
                    "            sort(nums, lo, mid - 1);\n" +
                    "            sort(nums, mid + 1, hi);\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    int partition(int[] nums, int lo, int hi) {\n" +
                    "        int i = lo - 1;\n" +
                    "        int pivot = nums[hi];\n" +
                    "\n" +
                    "        for (int j = lo; j < hi; j++) {\n" +
                    "            if (nums[j] < pivot) {\n" +
                    "                exchange(nums, ++i, j);\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "        exchange(nums, ++i, hi);\n" +
                    "\n" +
                    "        return i;\n" +
                    "    }\n" +
                    "\n" +
                    "    void exchange(int[] nums, int i, int j) {\n" +
                    "        int temp = nums[i];\n" +
                    "        nums[i] = nums[j];\n" +
                    "        nums[j] = temp;\n" +
                    "    }"
    };
}
