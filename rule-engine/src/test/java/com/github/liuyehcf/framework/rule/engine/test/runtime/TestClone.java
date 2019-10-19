package com.github.liuyehcf.framework.rule.engine.test.runtime;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.github.liuyehcf.framework.rule.engine.util.BeanUtils;
import com.github.liuyehcf.framework.rule.engine.util.CloneUtils;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

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
                "        }[listenerA(event=\"before\")]\n" +
                "    } then {\n" +
                "        if(conditionA()){\n" +
                "            actionB()\n" +
                "        }\n" +
                "    } \n" +
                "}[listenerB(event=\"before\")]");

        byte[] bytes;
        Rule clonedRule;

        bytes = CloneUtils.hessianSerialize(rule);
        clonedRule = CloneUtils.hessianDeserialize(bytes);

        bytes = CloneUtils.javaSerialize(rule);
        clonedRule = CloneUtils.javaDeserialize(bytes);

        CloneUtils.hessianClone(rule);
        CloneUtils.javaClone(rule);
        CloneUtils.kryoClone(Maps.newHashMap());
    }

    @Test
    public void testEnvClone() {
        testEnvClone(Maps.newLinkedHashMap(), 10);
        testEnvClone(new TreeMap<>(), 11);
        testEnvClone(Maps.newConcurrentMap(), 12);
        testEnvClone(Maps.newHashMap(), 13);
    }

    private void testEnvClone(Map<Object, Object> origin, int num) {
        for (int i = 0; i < num; i++) {
            origin.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        }

        Map<Object, Object> clone = BeanUtils.clone(origin);

        Assert.assertEquals(origin.getClass(), clone.getClass());

        Assert.assertEquals(
                JSON.toJSONString(origin, SerializerFeature.SortField, SerializerFeature.MapSortField),
                JSON.toJSONString(clone, SerializerFeature.SortField, SerializerFeature.MapSortField)
        );
    }
}
