package com.github.liuyehcf.framework.language.hua.cmd;

/**
 * 选项执行的动作
 *
 * @author hechenfeng
 * @date 2018/6/28
 */
@FunctionalInterface
public interface OptAction {
    /**
     * 选项执行的动作
     *
     * @param optValue 选项参数值
     */
    void execute(String optValue);
}
