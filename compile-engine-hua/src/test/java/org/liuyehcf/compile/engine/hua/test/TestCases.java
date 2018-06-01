package org.liuyehcf.compile.engine.hua.test;

/**
 * @author chenlu
 * @date 2018/6/1
 */
public class TestCases {
    static final String[] WHILE_CASES = {
            "void testWhile(){\n" +
                    "  while(true||false){\n" +
                    "    ;\n" +
                    "  }\n" +
                    "  while(a<b){\n" +
                    "    int a=3;\n" +
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
                    "  for(;;i++,j++)\n" +
                    "    something();\n" +
                    "\n" +
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
                    "\n" +
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

    };

    static final String[] OPERATOR_CASES = {

    };

    static final String[] CLASSIC_CASES = {

    };
}
