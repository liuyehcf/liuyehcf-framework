package com.github.liuyehcf.framework.language.hua.compile.definition.model;

/**
 * 回填类型
 *
 * @author hechenfeng
 * @date 2018/6/14
 */
public enum BackFillType {
    /**
     * true时跳转
     * 反向逻辑
     */
    TRUE,

    /**
     * false时跳转
     * 正向逻辑
     */
    FALSE,

    /**
     * goto
     * 仅出现在if else then语句中
     */
    NEXT
}
