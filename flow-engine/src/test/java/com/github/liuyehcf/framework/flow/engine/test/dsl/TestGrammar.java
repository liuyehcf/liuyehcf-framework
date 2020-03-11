package com.github.liuyehcf.framework.flow.engine.test.dsl;

import com.github.liuyehcf.framework.compile.engine.CompileResult;
import com.github.liuyehcf.framework.flow.engine.dsl.DslCompiler;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/4/23
 */
public class TestGrammar {

    private Flow compile(String input) {
        System.out.println(input);
        CompileResult<Flow> result = DslCompiler.getInstance().compile(input);

        if (result.getError() != null) {
            result.getError().printStackTrace();
        }
        Assert.assertTrue(result.isSuccess());
        return result.getResult();
    }

    @Test
    public void testGrammarLegality() {
        DslCompiler compiler = DslCompiler.getInstance();

        Assert.assertTrue(compiler.isLegal());

        System.out.println(compiler.getAnalysisTableMarkdownString());
    }

    @Test
    public void testCascade() {
        compile("{\n" +
                "    setProperty(pk=\"product1\",dn=\"deviceName1\",property=\"height\",value=\"${height}\"){\n" +
                "        setProperty(pk=\"product2\",dn=\"deviceName2\",property=\"wet\",value=1e-2)\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testParallel() {
        compile("{\n" +
                "    setProperty(pk=\"product1\",dn=\"deviceName1\",property=\"height\",value=\"${height}\"),\n" +
                "    setProperty(pk=\"product2\",dn=\"deviceName2\",property=\"wet\",value=1e-2)\n" +
                "}");
    }

    @Test
    public void testCondition() {
        compile("{\n" +
                "    if(expression(exp=\"event.value>10\")){\n" +
                "        setProperty(pk=\"${pd}\",dn=\"${dn}\",property=\"${property}\",value=\"${value}\")\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    if(expression(exp=\"event.value>10\")){\n" +
                "        setProperty(pk=\"${pd}\",dn=\"${dn}\",property=\"${property}\",value=\"${value}\")\n" +
                "    }else{\n" +
                "        setProperty(pk=\"${pd}\",dn=\"${dn}\",property=\"${property}\",value=\"${value}\")\n" +
                "    }\n" +
                "}");

        // if then with {}
        compile("{\n" +
                "    if(expression(exp=\"event.value>10\")){\n" +
                "        if(expression(exp=\"event.value>10\")){\n" +
                "            setProperty(pk=\"${pd}\",dn=\"${dn}\",property=\"${property}\",value=\"${value}\")\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    if(expression(exp=\"event.value>10\")){\n" +
                "        if(expression(exp=\"event.value>10\")){\n" +
                "            setProperty(pk=\"${pd}\",dn=\"${dn}\",property=\"${property}\",value=\"${value}\")\n" +
                "        }else{\n" +
                "            setProperty(pk=\"${pd}\",dn=\"${dn}\",property=\"${property}\",value=\"${value}\")\n" +
                "        }\n" +
                "    }\n" +
                "}");

        // if then with {} else with {}
        compile("{\n" +
                "    if(expression(exp=\"event.value>10\")){\n" +
                "        if(expression(exp=\"event.value>10\")){\n" +
                "            setProperty(pk=\"${pd}\",dn=\"${dn}\",property=\"${property}\",value=\"${value}\")\n" +
                "        }else{\n" +
                "            setProperty(pk=\"${pd}\",dn=\"${dn}\",property=\"${property}\",value=\"${value}\")\n" +
                "        }\n" +
                "    }\n" +
                "    else{\n" +
                "        if(expression(exp=\"event.value>10\")){\n" +
                "            setProperty(pk=\"${pd}\",dn=\"${dn}\",property=\"${property}\",value=\"${value}\")\n" +
                "        }\n" +
                "        else{\n" +
                "            setProperty(pk=\"${pd}\",dn=\"${dn}\",property=\"${property}\",value=\"${value}\")\n" +
                "        }\n" +
                "    }\n" +
                "}");
        compile("{\n" +
                "    if(expression(exp=\"event.value>10\")){\n" +
                "        if(expression(exp=\"event.value>10\")){\n" +
                "            setProperty(pk=\"${pd}\",dn=\"${dn}\",property=\"${property}\",value=\"${value}\")\n" +
                "        }\n" +
                "    }\n" +
                "    else{\n" +
                "        if(expression(exp=\"event.value>10\")){\n" +
                "            setProperty(pk=\"${pd}\",dn=\"${dn}\",property=\"${property}\",value=\"${value}\")\n" +
                "        }\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testHardAndJoin() {
        compile("{\n" +
                "    join & {\n" +
                "        nodeA()&,\n" +
                "        nodeB()\n" +
                "    }\n" +
                "}");
        compile("{\n" +
                "    join & {\n" +
                "        nodeA(),\n" +
                "        nodeB()&\n" +
                "    }\n" +
                "}");
        compile("{\n" +
                "    join & {\n" +
                "        nodeA()&,\n" +
                "        nodeB()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        g(){\n" +
                "            h()&\n" +
                "        },\n" +
                "        join & {\n" +
                "            a(){\n" +
                "                b(),\n" +
                "                c()&\n" +
                "            },\n" +
                "            d(){\n" +
                "                e()&\n" +
                "            }\n" +
                "        } then {\n" +
                "            f()&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(c1())&,\n" +
                "        if(c2())&,\n" +
                "        if(c3())&,\n" +
                "        if(c4()){\n" +
                "            a1()&\n" +
                "        },\n" +
                "        if(c5()){\n" +
                "            a2()&\n" +
                "        }else{\n" +
                "            a3()&\n" +
                "        }\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testSoftAndJoin() {
        compile("{\n" +
                "    join {\n" +
                "        nodeA()&,\n" +
                "        nodeB()\n" +
                "    }\n" +
                "}");
        compile("{\n" +
                "    join {\n" +
                "        nodeA(),\n" +
                "        nodeB()&\n" +
                "    }\n" +
                "}");
        compile("{\n" +
                "    join {\n" +
                "        nodeA()&,\n" +
                "        nodeB()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join {\n" +
                "        g(){\n" +
                "            h()&\n" +
                "        },\n" +
                "        join {\n" +
                "            a(){\n" +
                "                b(),\n" +
                "                c()&\n" +
                "            },\n" +
                "            d(){\n" +
                "                e()&\n" +
                "            }\n" +
                "        } then {\n" +
                "            f()&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join {\n" +
                "        if(c1())&,\n" +
                "        if(c2())&,\n" +
                "        if(c3())&,\n" +
                "        if(c4()){\n" +
                "            a1()&\n" +
                "        },\n" +
                "        if(c5()){\n" +
                "            a2()&\n" +
                "        }else{\n" +
                "            a3()&\n" +
                "        }\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testOrJoin() {
        compile("{\n" +
                "    join | {\n" +
                "        nodeA()&,\n" +
                "        nodeB()\n" +
                "    }\n" +
                "}");
        compile("{\n" +
                "    join | {\n" +
                "        nodeA(),\n" +
                "        nodeB()&\n" +
                "    }\n" +
                "}");
        compile("{\n" +
                "    join | {\n" +
                "        nodeA()&,\n" +
                "        nodeB()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join | {\n" +
                "        g(){\n" +
                "            h()&\n" +
                "        },\n" +
                "        join | {\n" +
                "            a(){\n" +
                "                b(),\n" +
                "                c()&\n" +
                "            },\n" +
                "            d(){\n" +
                "                e()&\n" +
                "            }\n" +
                "        } then {\n" +
                "            f()&\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join | {\n" +
                "        if(c1())&,\n" +
                "        if(c2())&,\n" +
                "        if(c3())&,\n" +
                "        if(c4()){\n" +
                "            a1()&\n" +
                "        },\n" +
                "        if(c5()){\n" +
                "            a2()&\n" +
                "        }else{\n" +
                "            a3()&\n" +
                "        }\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testConditionHardAndJoin() {
        // one selected
        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        // two selected
        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        // three selected
        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        // four selected
        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        // all selected
        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testConditionSoftAndJoin() {
        // one selected
        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        // two selected
        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        // three selected
        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        // four selected
        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        // all selected
        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testConditionOrJoin() {
        // one selected
        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        // two selected
        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        // three selected
        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        // four selected
        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    }\n" +
                "}");

        // all selected
        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testHardAndJoinThen() {
        compile("{\n" +
                "    join & {\n" +
                "        nodeA()&,\n" +
                "        nodeB()\n" +
                "    } then {\n" +
                "        nodeC()\n" +
                "    }\n" +
                "}");
        compile("{\n" +
                "    join & {\n" +
                "        nodeA(),\n" +
                "        nodeB()&\n" +
                "    } then {\n" +
                "        nodeC()\n" +
                "    }\n" +
                "}");
        compile("{\n" +
                "    join & {\n" +
                "        nodeA()&,\n" +
                "        nodeB()&\n" +
                "    } then {\n" +
                "        nodeC()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        g(){\n" +
                "            h()&\n" +
                "        },\n" +
                "        join & {\n" +
                "            a(){\n" +
                "                b(),\n" +
                "                c()&\n" +
                "            },\n" +
                "            d(){\n" +
                "                e()&\n" +
                "            }\n" +
                "        } then {\n" +
                "            f()&\n" +
                "        }\n" +
                "    } then {\n" +
                "        j()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(c1())&,\n" +
                "        if(c2())&,\n" +
                "        if(c3())&,\n" +
                "        if(c4()){\n" +
                "            a1()&\n" +
                "        },\n" +
                "        if(c5()){\n" +
                "            a2()&\n" +
                "        }else{\n" +
                "            a3()&\n" +
                "        }\n" +
                "    } then {\n" +
                "        a5()\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testSoftAndJoinThen() {
        compile("{\n" +
                "    join  {\n" +
                "        nodeA()&,\n" +
                "        nodeB()\n" +
                "    } then {\n" +
                "        nodeC()\n" +
                "    }\n" +
                "}");
        compile("{\n" +
                "    join  {\n" +
                "        nodeA(),\n" +
                "        nodeB()&\n" +
                "    } then {\n" +
                "        nodeC()\n" +
                "    }\n" +
                "}");
        compile("{\n" +
                "    join  {\n" +
                "        nodeA()&,\n" +
                "        nodeB()&\n" +
                "    } then {\n" +
                "        nodeC()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        g(){\n" +
                "            h()&\n" +
                "        },\n" +
                "        join  {\n" +
                "            a(){\n" +
                "                b(),\n" +
                "                c()&\n" +
                "            },\n" +
                "            d(){\n" +
                "                e()&\n" +
                "            }\n" +
                "        } then {\n" +
                "            f()&\n" +
                "        }\n" +
                "    } then {\n" +
                "        j()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(c1())&,\n" +
                "        if(c2())&,\n" +
                "        if(c3())&,\n" +
                "        if(c4()){\n" +
                "            a1()&\n" +
                "        },\n" +
                "        if(c5()){\n" +
                "            a2()&\n" +
                "        }else{\n" +
                "            a3()&\n" +
                "        }\n" +
                "    } then {\n" +
                "        a5()\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testOrJoinThen() {
        compile("{\n" +
                "    join | {\n" +
                "        nodeA()&,\n" +
                "        nodeB()\n" +
                "    } then {\n" +
                "        nodeC()\n" +
                "    }\n" +
                "}");
        compile("{\n" +
                "    join | {\n" +
                "        nodeA(),\n" +
                "        nodeB()&\n" +
                "    } then {\n" +
                "        nodeC()\n" +
                "    }\n" +
                "}");
        compile("{\n" +
                "    join | {\n" +
                "        nodeA()&,\n" +
                "        nodeB()&\n" +
                "    } then {\n" +
                "        nodeC()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join | {\n" +
                "        g(){\n" +
                "            h()&\n" +
                "        },\n" +
                "        join | {\n" +
                "            a(){\n" +
                "                b(),\n" +
                "                c()&\n" +
                "            },\n" +
                "            d(){\n" +
                "                e()&\n" +
                "            }\n" +
                "        } then {\n" +
                "            f()&\n" +
                "        }\n" +
                "    } then {\n" +
                "        j()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join | {\n" +
                "        if(c1())&,\n" +
                "        if(c2())&,\n" +
                "        if(c3())&,\n" +
                "        if(c4()){\n" +
                "            a1()&\n" +
                "        },\n" +
                "        if(c5()){\n" +
                "            a2()&\n" +
                "        }else{\n" +
                "            a3()&\n" +
                "        }\n" +
                "    } then {\n" +
                "        a5()\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testConditionHardAndJoinThen() {
        // one selected
        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        // two selected
        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        // three selected
        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        // four selected
        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        // all selected
        compile("{\n" +
                "    join & {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testConditionSoftAndJoinThen() {
        // one selected
        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        // two selected
        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        // three selected
        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        // four selected
        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        // all selected
        compile("{\n" +
                "    join  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testConditionOrJoinThen() {
        // one selected
        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        // two selected
        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        // three selected
        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        // four selected
        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA(),\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC(),\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");

        // all selected
        compile("{\n" +
                "    join |  {\n" +
                "        if(expression(exp=\"a<1\")){\n" +
                "            nodeA()&,\n" +
                "            nodeB()&\n" +
                "        }else{\n" +
                "            nodeC()&,\n" +
                "            nodeD()&\n" +
                "        },\n" +
                "        nodeE()&\n" +
                "    } then {\n" +
                "        nodeF()\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testSelect() {
        // simple select
        compile("{\n" +
                "    select {\n" +
                "        if(c()){\n" +
                "            d()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    select {\n" +
                "        if(c()){\n" +
                "            d()\n" +
                "        },\n" +
                "        if(c()){\n" +
                "            d()\n" +
                "        }\n" +
                "    }\n" +
                "}[l(event=\"before\")]");

        compile("{\n" +
                "    select {\n" +
                "        if(c()){\n" +
                "            d()\n" +
                "        },\n" +
                "        if(c()){\n" +
                "            d()\n" +
                "        },\n" +
                "        if(c()){\n" +
                "            d()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        // parallel and select
        compile("{\n" +
                "    a(),\n" +
                "    select {\n" +
                "        if(c()){\n" +
                "            d()\n" +
                "        },\n" +
                "        if(c()){\n" +
                "            d()\n" +
                "        },\n" +
                "        if(c()){\n" +
                "            d()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    select {\n" +
                "        if(c()){\n" +
                "            d()\n" +
                "        },\n" +
                "        if(c()){\n" +
                "            d()\n" +
                "        },\n" +
                "        if(c()){\n" +
                "            d()\n" +
                "        }\n" +
                "    },\n" +
                "    a()\n" +
                "}[l(event=\"before\")]");

        compile("{\n" +
                "    b(),\n" +
                "    select {\n" +
                "        if(c()){\n" +
                "            d()\n" +
                "        },\n" +
                "        if(c()){\n" +
                "            d()\n" +
                "        },\n" +
                "        if(c()){\n" +
                "            d()\n" +
                "        }\n" +
                "    },\n" +
                "    a()\n" +
                "}");

        // cascade and select
        compile("{\n" +
                "    a(){\n" +
                "        select{\n" +
                "            if(c()){\n" +
                "                a()\n" +
                "            },\n" +
                "            if(d()){\n" +
                "                f()\n" +
                "            }\n" +
                "        }[l(event=\"before\")]\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    b(){\n" +
                "        a(){\n" +
                "            select{\n" +
                "                if(c()){\n" +
                "                    a()\n" +
                "                },\n" +
                "                if(d()){\n" +
                "                    f()\n" +
                "                }\n" +
                "            }[l(event=\"before\")]\n" +
                "        }\n" +
                "    }\n" +
                "}");

        // cascade and parallel and select
        compile("{\n" +
                "    b(){\n" +
                "        a(){\n" +
                "            e(),\n" +
                "            select{\n" +
                "                if(c()){\n" +
                "                    a()\n" +
                "                },\n" +
                "                if(d()){\n" +
                "                    f()\n" +
                "                }\n" +
                "            }[l(event=\"before\")],\n" +
                "            f()\n" +
                "        },\n" +
                "        d()\n" +
                "    },\n" +
                "    c()\n" +
                "}");

        // if then and select
        compile("{\n" +
                "    if(c()){\n" +
                "        select {\n" +
                "            if(c()){\n" +
                "                d()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        // if else then and select
        compile("{\n" +
                "    if(c()){\n" +
                "        select {\n" +
                "            if(c()){\n" +
                "                d()\n" +
                "            }\n" +
                "        }\n" +
                "    }else{\n" +
                "        select {\n" +
                "            if(c()){\n" +
                "                d()\n" +
                "            }\n" +
                "        }[s(event=\"success\")]\n" +
                "    }\n" +
                "}");

        // join and select
        compile("{\n" +
                "    a(),\n" +
                "    join & {\n" +
                "        g()&,\n" +
                "        select{\n" +
                "            if(c()){\n" +
                "                d()&\n" +
                "            },\n" +
                "            if(f()){\n" +
                "                e()&\n" +
                "            },\n" +
                "            if(f()){\n" +
                "                g()&\n" +
                "            }\n" +
                "        }[ls(event=\"before\")],\n" +
                "        a()&,\n" +
                "        b()\n" +
                "    }[ls(event=\"before\")],\n" +
                "    b()\n" +
                "}");

        compile("{\n" +
                "    a(),\n" +
                "    select{\n" +
                "        if(c()){\n" +
                "            join & {\n" +
                "                a()&,\n" +
                "                if(b()){\n" +
                "                    d()&\n" +
                "                },\n" +
                "                c()\n" +
                "            }\n" +
                "        },\n" +
                "        if(f()){\n" +
                "            e()\n" +
                "        },\n" +
                "        if(f()){\n" +
                "            g()\n" +
                "        }\n" +
                "    }[ls(event=\"before\")],\n" +
                "    b()\n" +
                "}");

        // join then and select
        compile("{\n" +
                "    a(),\n" +
                "    join & {\n" +
                "        g()&,\n" +
                "        select{\n" +
                "            if(c()){\n" +
                "                d()&\n" +
                "            },\n" +
                "            if(f()){\n" +
                "                e()&\n" +
                "            },\n" +
                "            if(f()){\n" +
                "                g()&\n" +
                "            }\n" +
                "        }[ls(event=\"before\")],\n" +
                "        a()&,\n" +
                "        b()\n" +
                "    }[ls(event=\"before\")] then{\n" +
                "        c(),\n" +
                "        select{\n" +
                "            if(c()){\n" +
                "                d()\n" +
                "            },\n" +
                "            if(f()){\n" +
                "                e()\n" +
                "            },\n" +
                "            if(f()){\n" +
                "                g()\n" +
                "            }\n" +
                "        }[ls(event=\"before\")]\n" +
                "    },\n" +
                "    b()\n" +
                "}");

        compile("{\n" +
                "    a(),\n" +
                "    select{\n" +
                "        if(c()){\n" +
                "            join & {\n" +
                "                a()&,\n" +
                "                if(b()){\n" +
                "                    d()&\n" +
                "                },\n" +
                "                c()\n" +
                "            } [li(event=\"before\")] then{\n" +
                "                a()\n" +
                "            }\n" +
                "        },\n" +
                "        if(f()){\n" +
                "            e()\n" +
                "        },\n" +
                "        if(f()){\n" +
                "            g()\n" +
                "        }\n" +
                "    }[ls(event=\"before\")],\n" +
                "    b()\n" +
                "}");
    }

    @Test
    public void testListener() {
        compile("{\n" +
                "    nodeA()[listenerA(event=\"before\")]\n" +
                "}");

        compile("{\n" +
                "    nodeA()[listenerA(event=\"before\"),listenerB(event=\"before\", id=${id})]\n" +
                "}");

        compile("{\n" +
                "    nodeA()[listenerA(event=\"before\"),listenerB(event=\"success\", id=${id})]{\n" +
                "        nodeB()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    nodeA()[listenerA(event=\"before\"),listenerB(event=\"before\", id=${id})]{\n" +
                "        nodeB()[listenerC(event=\"before\")]\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        nodeA()[listenerA(event=\"before\"),listenerB(event=\"before\", id=${id})]&,\n" +
                "        nodeB(){\n" +
                "            nodeC()[listenerA(event=\"before\", id=45)]\n" +
                "        }\n" +
                "    } then {\n" +
                "        nodeD()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        nodeA()[listenerA(event=\"before\"),listenerB(event=\"before\", id=${id})]&,\n" +
                "        if(expression(exp=\"a<4\")){\n" +
                "            nodeB(){\n" +
                "                nodeC()[listenerA(event=\"before\", id=45)]&\n" +
                "            }\n" +
                "        }\n" +
                "    } then {\n" +
                "        nodeD()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    testAction()\n" +
                "}[listenerA(event=\"before\"), listenerB(event=\"success\")]");
    }

    @Test
    public void testSubFlow() {
        compile("{\n" +
                "    nodeA(){\n" +
                "        sub{\n" +
                "            nodeB()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    nodeA(){\n" +
                "        sub{\n" +
                "            nodeB(){\n" +
                "                nodeC()\n" +
                "            }\n" +
                "        },\n" +
                "        sub{\n" +
                "            nodeB(){\n" +
                "                nodeC()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    if(nodeA()){\n" +
                "        nodeB()\n" +
                "    }else{\n" +
                "        sub{\n" +
                "            nodeB(){\n" +
                "                nodeC()\n" +
                "            }\n" +
                "        },\n" +
                "        sub{\n" +
                "            nodeB(){\n" +
                "                nodeC()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join {\n" +
                "        nodeC()&\n" +
                "    } then {\n" +
                "        sub{\n" +
                "            nodeA(){\n" +
                "                nodeB()\n" +
                "            }\n" +
                "        },\n" +
                "        sub{\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    nodeA(){\n" +
                "        sub{\n" +
                "            nodeB()\n" +
                "        } [LA(event=\"before\")]\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    nodeA(){\n" +
                "        sub{\n" +
                "            nodeB(){\n" +
                "                nodeC()\n" +
                "            }\n" +
                "        }[LA(event=\"before\")],\n" +
                "        sub{\n" +
                "            nodeB(){\n" +
                "                nodeC()\n" +
                "            }\n" +
                "        }[LA(event=\"before\")]\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    if(nodeA()){\n" +
                "        nodeB()\n" +
                "    }else{\n" +
                "        sub{\n" +
                "            nodeB(){\n" +
                "                nodeC()\n" +
                "            }\n" +
                "        }[LA(event=\"before\")],\n" +
                "        sub{\n" +
                "            nodeB(){\n" +
                "                nodeC()\n" +
                "            }\n" +
                "        }[LA(event=\"before\")]\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join {\n" +
                "        nodeC()&\n" +
                "    } then {\n" +
                "        sub{\n" +
                "            nodeA(){\n" +
                "                nodeB()\n" +
                "            }\n" +
                "        }[LA(event=\"before\")],\n" +
                "        sub{\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }[LA(event=\"before\")]\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testSubThenFlow() {
        compile("{\n" +
                "    nodeA(){\n" +
                "        sub{\n" +
                "            nodeB()\n" +
                "        } then {\n" +
                "            nodeC()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    nodeA(){\n" +
                "        sub{\n" +
                "            nodeB(){\n" +
                "                nodeC()\n" +
                "            } \n" +
                "        }then {\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        sub{\n" +
                "            nodeB(){\n" +
                "                nodeC()\n" +
                "            }\n" +
                "        }then {\n" +
                "            nodeD()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    if(nodeA()){\n" +
                "        nodeB()\n" +
                "    }else{\n" +
                "        sub{\n" +
                "            nodeB(){\n" +
                "                nodeC()\n" +
                "            }\n" +
                "        }then {\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        sub{\n" +
                "            nodeB(){\n" +
                "                nodeC()\n" +
                "            }\n" +
                "        }then {\n" +
                "            nodeD()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join {\n" +
                "        nodeC()&\n" +
                "    } then {\n" +
                "        sub{\n" +
                "            nodeA(){\n" +
                "                nodeB()\n" +
                "            }\n" +
                "        }then {\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        sub{\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        }then {\n" +
                "            nodeD()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    nodeA(){\n" +
                "        sub{\n" +
                "            nodeB()\n" +
                "        } [LA(event=\"before\")] then {\n" +
                "            nodeC()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    nodeA(){\n" +
                "        sub{\n" +
                "            nodeB(){\n" +
                "                nodeC()\n" +
                "            } \n" +
                "        } [LA(event=\"before\")] then {\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        sub{\n" +
                "            nodeB(){\n" +
                "                nodeC()\n" +
                "            }\n" +
                "        } [LA(event=\"before\")] then {\n" +
                "            nodeD()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    if(nodeA()){\n" +
                "        nodeB()\n" +
                "    }else{\n" +
                "        sub{\n" +
                "            nodeB(){\n" +
                "                nodeC()\n" +
                "            }\n" +
                "        } [LA(event=\"before\")] then {\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        sub{\n" +
                "            nodeB(){\n" +
                "                nodeC()\n" +
                "            }\n" +
                "        } [LA(event=\"before\")] then {\n" +
                "            nodeD()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join {\n" +
                "        nodeC()&\n" +
                "    } then {\n" +
                "        sub{\n" +
                "            nodeA(){\n" +
                "                nodeB()\n" +
                "            }\n" +
                "        } [LA(event=\"before\")] then {\n" +
                "            nodeD()\n" +
                "        },\n" +
                "        sub{\n" +
                "            nodeA(),\n" +
                "            nodeB()\n" +
                "        } [LA(event=\"before\")] then {\n" +
                "            nodeD()\n" +
                "        }\n" +
                "    }\n" +
                "}");
    }

    @Test
    public void testFlowNameAndFlowId() {
        Flow flow;

        flow = compile("(aaa,bbb){\n" +
                "    nodeA()\n" +
                "}");
        Assert.assertEquals("aaa", flow.getName());
        Assert.assertEquals("bbb", flow.getId());

        flow = compile("(aaa,bbb-bb-ccc){\n" +
                "    nodeA()\n" +
                "}");
        Assert.assertEquals("aaa", flow.getName());
        Assert.assertEquals("bbb-bb-ccc", flow.getId());

        flow = compile("(aaa-bb-ccc,bbb){\n" +
                "    nodeA()\n" +
                "}");
        Assert.assertEquals("aaa-bb-ccc", flow.getName());
        Assert.assertEquals("bbb", flow.getId());

        flow = compile("(aaa-bb-ccc,aaa-bb-ccc){\n" +
                "    nodeA()\n" +
                "}");
        Assert.assertEquals("aaa-bb-ccc", flow.getName());
        Assert.assertEquals("aaa-bb-ccc", flow.getId());
    }

    @Test
    public void testReadMe() {
        compile("{\n" +
                "  actionA() {\n" +
                "    actionB()\n" +
                "  }\n" +
                "}");

        compile("{\n" +
                "    actionA(),\n" +
                "    actionB(){\n" +
                "        actionD(),\n" +
                "        actionE()\n" +
                "    },\n" +
                "    actionC()\n" +
                "}");

        compile("{\n" +
                "    if(conditionA())\n" +
                "}");

        compile("{\n" +
                "    if(conditionA()) {\n" +
                "        actionA()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionA(),\n" +
                "        actionB(),\n" +
                "        actionC()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    if(conditionA()) {\n" +
                "        actionA()\n" +
                "    } else {\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    if(conditionA()){\n" +
                "        actionA(),\n" +
                "        actionB()\n" +
                "    } else {\n" +
                "        actionC(),\n" +
                "        actionD()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    select {\n" +
                "        if(conditionA()){\n" +
                "            actionA()\n" +
                "        },\n" +
                "        if(conditionB()){\n" +
                "            actionB()\n" +
                "        },\n" +
                "        if(conditionC()){\n" +
                "            actionC()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        actionA()&,\n" +
                "        actionB(),\n" +
                "        actionC()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB(),\n" +
                "        actionC()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join | {\n" +
                "        actionA()&,\n" +
                "        actionB(),\n" +
                "        actionC()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(conditionA()){\n" +
                "            actionC()\n" +
                "        } else {\n" +
                "            actionD()&\n" +
                "        },\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join {\n" +
                "        if(conditionA()){\n" +
                "            actionC()\n" +
                "        } else {\n" +
                "            actionD()&\n" +
                "        },\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join | {\n" +
                "        if(conditionA()){\n" +
                "            actionC()\n" +
                "        } else {\n" +
                "            actionD()&\n" +
                "        },\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        actionA()&,\n" +
                "        actionB(),\n" +
                "        actionC()&\n" +
                "    } then {\n" +
                "        actionD()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB(),\n" +
                "        actionC()&\n" +
                "    } then {\n" +
                "        actionD()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join | {\n" +
                "        actionA()&,\n" +
                "        actionB(),\n" +
                "        actionC()&\n" +
                "    } then {\n" +
                "        actionD()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join & {\n" +
                "        if(conditionA()){\n" +
                "            actionC()\n" +
                "        } else {\n" +
                "            actionD()&\n" +
                "        },\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join {\n" +
                "        if(conditionA()){\n" +
                "            actionC()\n" +
                "        } else {\n" +
                "            actionD()&\n" +
                "        },\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    join | {\n" +
                "        if(conditionA()){\n" +
                "            actionC()\n" +
                "        } else {\n" +
                "            actionD()&\n" +
                "        },\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } then {\n" +
                "        actionE()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    actionA(){\n" +
                "        sub {\n" +
                "            actionB(),\n" +
                "            actionC(),\n" +
                "            actionD()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    actionA(){\n" +
                "        sub {\n" +
                "            join{\n" +
                "                actionB()&,\n" +
                "                actionC()&\n" +
                "            }then {\n" +
                "                actionD()\n" +
                "            }\n" +
                "        } then {\n" +
                "            actionE()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    actionA(){\n" +
                "        sub{\n" +
                "            select{\n" +
                "                if(conditionA()){\n" +
                "                    actionB()\n" +
                "                },\n" +
                "                if(conditionB()){\n" +
                "                    actionC()\n" +
                "                }\n" +
                "            }\n" +
                "        } then {\n" +
                "            actionD()\n" +
                "        } else{\n" +
                "            actionE()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    actionA()\n" +
                "} [listenerA(event=\"before\")]");

        compile("{\n" +
                "    actionA()[listenerA(event=\"before\")]\n" +
                "}");

        compile("{\n" +
                "    if(conditionA() [listenerA(event=\"before\")]) {\n" +
                "        actionA()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    select {\n" +
                "        if(conditionA()){\n" +
                "            actionA()\n" +
                "        },\n" +
                "        if(conditionB()){\n" +
                "            actionB()\n" +
                "        }\n" +
                "    } [listenerA(event=\"before\")]\n" +
                "}");

        compile("{\n" +
                "    join {\n" +
                "        actionA()&,\n" +
                "        actionB()&\n" +
                "    } [listenerA(event=\"before\")] then {\n" +
                "        actionD()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    sub {\n" +
                "        actionA()\n" +
                "    }[listenerA(event=\"before\")],\n" +
                "\n" +
                "    sub {\n" +
                "        actionB()\n" +
                "    }[listenerB(event=\"success\")] then {\n" +
                "        actionC()\n" +
                "    },\n" +
                "\n" +
                "    sub {\n" +
                "        actionD()\n" +
                "    }[listenerC(event=\"before\")] then {\n" +
                "        actionE()\n" +
                "    } else {\n" +
                "        actionF()\n" +
                "    }\n" +
                "}");

        compile("{\n" +
                "    actionA()[listenerA(event=\"before\"), listenerB(event=\"success\")]\n" +
                "}");
    }
}
