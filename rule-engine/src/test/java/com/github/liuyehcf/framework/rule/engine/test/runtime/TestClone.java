package com.github.liuyehcf.framework.rule.engine.test.runtime;

import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.util.CloneUtils;
import org.junit.Test;

/**
 * @author hechenfeng
 * @date 2019/5/24
 */
public class TestClone extends TestRuntimeBase {

    @Test
    public void test() {
        Rule rule = compile("{\n" +
                "    join{\n" +
                "        select {\n" +
                "            if(conditionA()){\n" +
                "                actionA(arg1=\"hehe\")&\n" +
                "            } \n" +
                "        }[listenerA(event=\"start\")]\n" +
                "    } then {\n" +
                "        if(conditionA()){\n" +
                "            actionB()\n" +
                "        }\n" +
                "    } \n" +
                "}[listenerB(event=\"start\")]");

        byte[] bytes;
        Rule clonedRule;

        bytes = CloneUtils.hessianSerialize(rule);
        clonedRule = CloneUtils.hessianDeserialize(bytes);

        bytes = CloneUtils.javaSerialize(rule);
        clonedRule = CloneUtils.javaDeserialize(bytes);

        CloneUtils.hessianClone(rule);
        CloneUtils.javaClone(rule);
    }
}
