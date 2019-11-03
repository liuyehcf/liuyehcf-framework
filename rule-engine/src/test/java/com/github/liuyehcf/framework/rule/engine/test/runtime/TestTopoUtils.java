package com.github.liuyehcf.framework.rule.engine.test.runtime;

import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.util.TopoUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/10/28
 */
public class TestTopoUtils extends TestRuntimeBase {

    @Test
    public void testSingle() {
        Rule rule = compile("{\n" +
                "    actionA()\n" +
                "}");

        Assert.assertTrue(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    actionA(){\n" +
                "        actionB(){\n" +
                "            actionC(){\n" +
                "                actionD(){\n" +
                "                    actionE()\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Assert.assertTrue(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        if(conditionB()){\n" +
                "            actionA()\n" +
                "        }else{\n" +
                "            actionB(){\n" +
                "                if(conditionC()){\n" +
                "                    actionC()\n" +
                "                } else {\n" +
                "                    actionE()\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Assert.assertTrue(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    select {\n" +
                "        if(conditionA()){\n" +
                "            actionB(){\n" +
                "                if(conditionB()){\n" +
                "                    actionD()\n" +
                "                }else{\n" +
                "                    actionC()\n" +
                "                }\n" +
                "            }\n" +
                "        } \n" +
                "    }\n" +
                "}");

        Assert.assertTrue(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    join {\n" +
                "        actionA()&\n" +
                "    } then {\n" +
                "        actionB()\n" +
                "    }\n" +
                "}");

        Assert.assertTrue(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    sub{ \n" +
                "        actionA(),\n" +
                "        actionB()\n" +
                "    } then {\n" +
                "        actionC()\n" +
                "    } else{\n" +
                "        actionD()\n" +
                "    }\n" +
                "}");

        Assert.assertFalse(TopoUtils.isSingleLinkRule(rule));
    }

    @Test
    public void testNotSingle() {
        Rule rule = compile("{\n" +
                "    actionA(),\n" +
                "    actionB()\n" +
                "}");

        Assert.assertFalse(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    actionA(){\n" +
                "        actionB(){\n" +
                "            actionC(){\n" +
                "                actionD(){\n" +
                "                    actionE(),\n" +
                "                    actionF()\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Assert.assertFalse(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    actionA(){\n" +
                "        actionB(){\n" +
                "            actionC(){\n" +
                "                actionD(){\n" +
                "                    actionE()\n" +
                "                },\n" +
                "                actionF()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Assert.assertFalse(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    actionA(){\n" +
                "        actionB(){\n" +
                "            actionC(){\n" +
                "                actionD(){\n" +
                "                    actionE()\n" +
                "                }\n" +
                "            },\n" +
                "            actionF()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Assert.assertFalse(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    actionA(){\n" +
                "        actionB(){\n" +
                "            actionC(){\n" +
                "                actionD(){\n" +
                "                    actionE()\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        actionF()\n" +
                "    }\n" +
                "}");

        Assert.assertFalse(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    actionA(){\n" +
                "        actionB(){\n" +
                "            actionC(){\n" +
                "                actionD(){\n" +
                "                    actionE()\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    actionF()\n" +
                "}");

        Assert.assertFalse(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        if(conditionB()){\n" +
                "            actionA()\n" +
                "        }else{\n" +
                "            actionB(){\n" +
                "                if(conditionC()){\n" +
                "                    actionC()\n" +
                "                } else {\n" +
                "                    actionE()\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        if(conditionD()){\n" +
                "            actionF()\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Assert.assertFalse(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        if(conditionB()){\n" +
                "            actionA(),\n" +
                "            if(conditionD()){\n" +
                "                actionF()\n" +
                "            }\n" +
                "        }else{\n" +
                "            actionB(){\n" +
                "                if(conditionC()){\n" +
                "                    actionC()\n" +
                "                } else {\n" +
                "                    actionE()\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Assert.assertFalse(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        if(conditionB()){\n" +
                "            actionA()\n" +
                "        }else{\n" +
                "            actionB(){\n" +
                "                if(conditionC()){\n" +
                "                    actionC()\n" +
                "                } else {\n" +
                "                    actionE()\n" +
                "                }\n" +
                "            },\n" +
                "            if(conditionD()){\n" +
                "                actionF()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Assert.assertFalse(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        if(conditionB()){\n" +
                "            actionA()\n" +
                "        }else{\n" +
                "            actionB(){\n" +
                "                if(conditionC()){\n" +
                "                    actionC(),\n" +
                "                    actionD()\n" +
                "                } else {\n" +
                "                    actionE()\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Assert.assertFalse(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    if(conditionA()){\n" +
                "        if(conditionB()){\n" +
                "            actionA()\n" +
                "        }else{\n" +
                "            actionB(){\n" +
                "                if(conditionC()){\n" +
                "                    actionC()\n" +
                "                } else {\n" +
                "                    actionE(),\n" +
                "                    actionD()\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        Assert.assertFalse(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    select {\n" +
                "        if(conditionA()){\n" +
                "            actionB(){\n" +
                "                if(conditionB()){\n" +
                "                    actionD()\n" +
                "                }else{\n" +
                "                    actionC()\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        if(conditionC())\n" +
                "    }\n" +
                "}");

        Assert.assertFalse(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    select {\n" +
                "        if(conditionA()){\n" +
                "            actionB(){\n" +
                "                if(conditionB()){\n" +
                "                    actionD()\n" +
                "                }else{\n" +
                "                    actionC()\n" +
                "                }\n" +
                "            },\n" +
                "            actionE()\n" +
                "        } \n" +
                "    }\n" +
                "}");

        Assert.assertFalse(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    select {\n" +
                "        if(conditionA()){\n" +
                "            actionB(){\n" +
                "                if(conditionB()){\n" +
                "                    actionD(),\n" +
                "                    actionE()\n" +
                "                }else{\n" +
                "                    actionC()\n" +
                "                }\n" +
                "            }\n" +
                "        } \n" +
                "    }\n" +
                "}");

        Assert.assertFalse(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    select {\n" +
                "        if(conditionA()){\n" +
                "            actionB(){\n" +
                "                if(conditionB()){\n" +
                "                    actionD()\n" +
                "                }else{\n" +
                "                    actionC(),\n" +
                "                    actionE()\n" +
                "                }\n" +
                "            }\n" +
                "        } \n" +
                "    }\n" +
                "}");

        Assert.assertFalse(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    sub{ \n" +
                "        actionA(),\n" +
                "        actionB()\n" +
                "    } then {\n" +
                "        actionC(),\n" +
                "        actionE()\n" +
                "    } else{\n" +
                "        actionD()\n" +
                "    }\n" +
                "}");

        Assert.assertFalse(TopoUtils.isSingleLinkRule(rule));

        rule = compile("{\n" +
                "    sub{ \n" +
                "        actionA(),\n" +
                "        actionB()\n" +
                "    } then {\n" +
                "        actionC()\n" +
                "    } else{\n" +
                "        actionD(),\n" +
                "        actionE()\n" +
                "    }\n" +
                "}");

        Assert.assertFalse(TopoUtils.isSingleLinkRule(rule));
    }
}
