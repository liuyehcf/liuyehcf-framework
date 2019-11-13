package com.github.liuyehcf.framework.rpc.ares.test;

import com.github.liuyehcf.framework.rpc.ares.util.PathUtils;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/11/11
 */
public class TestPathUtils {

    @Test
    public void test1() {
        Map<String, String> context = Maps.newHashMap();

        Assert.assertEquals("/asdfasdf/asdfasdf", PathUtils.render("/asdfasdf/asdfasdf", context));
        Assert.assertEquals("/asdfasdf/{asdfasdf", PathUtils.render("/asdfasdf/{asdfasdf", context));
        Assert.assertEquals("/{asdfasdf/asdfasdf", PathUtils.render("/{asdfasdf/asdfasdf", context));
        Assert.assertEquals("/asdfasdf/asdfasdf}", PathUtils.render("/asdfasdf/asdfasdf}", context));
        Assert.assertEquals("/asdfasdf}/asdfasdf", PathUtils.render("/asdfasdf}/asdfasdf", context));
        Assert.assertEquals("/{abc}/{bcd}", PathUtils.render("/{abc}/{bcd}", context));
    }

    @Test
    public void test2() {
        Map<String, String> context = Maps.newHashMap();

        context.put("name", "xiaoming");
        Assert.assertEquals("/asdfasdf/xiaoming", PathUtils.render("/asdfasdf/{name}", context));
        Assert.assertEquals("/asdfasdf/xiaominghaha", PathUtils.render("/asdfasdf/{name}haha", context));
        Assert.assertEquals("/asdfasdf/hahaxiaoming", PathUtils.render("/asdfasdf/haha{name}", context));
    }

    @Test
    public void test3() {
        Map<String, String> context = Maps.newHashMap();

        context.put("a", "b");
        Assert.assertEquals("/b/b", PathUtils.render("/{a}/{a}", context));
        Assert.assertEquals("/b/b}", PathUtils.render("/{a}/{a}}", context));
        Assert.assertEquals("/b}/b", PathUtils.render("/{a}}/{a}", context));
        Assert.assertEquals("/b/{b", PathUtils.render("/{a}/{{a}", context));
        Assert.assertEquals("/{b/b", PathUtils.render("/{{a}/{a}", context));
    }

    @Test
    public void test4() {
        Map<String, String> context = Maps.newHashMap();

        context.put("a", "b");
        context.put("c", "d");
        Assert.assertEquals("/{{{b", PathUtils.render("/{{{{a}", context));
        Assert.assertEquals("d}}", PathUtils.render("{c}}}", context));
        Assert.assertEquals("}}{{", PathUtils.render("}}{{", context));
        Assert.assertEquals("{b}", PathUtils.render("{{a}}", context));
    }
}
