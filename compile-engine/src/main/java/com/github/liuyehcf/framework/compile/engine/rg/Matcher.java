package com.github.liuyehcf.framework.compile.engine.rg;

/**
 * 正则匹配器接口
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public interface Matcher {

    /**
     * 正则表达式是否匹配整个字符串
     *
     * @return 是否匹配
     */
    boolean matches();

    /**
     * 查询'下一个'匹配的子串
     *
     * @return 是否有下一个匹配子串
     */
    boolean find();

    /**
     * 返回第i组匹配的内容
     *
     * @param group 组id
     * @return 匹配内容
     */
    String group(int group);

    /**
     * 返回捕获组的数量
     *
     * @return 捕获组的数量
     */
    int groupCount();

    /**
     * 指定捕获组的起始索引，闭
     *
     * @param group 组id
     * @return 起始索引
     */
    int start(int group);

    /**
     * 指定不获取的终止索引，开
     *
     * @param group 组id
     * @return 终止索引
     */
    int end(int group);
}
