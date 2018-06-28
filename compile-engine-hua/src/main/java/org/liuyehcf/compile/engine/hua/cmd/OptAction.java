package org.liuyehcf.compile.engine.hua.cmd;

/**
 * 选项执行的动作
 *
 * @author hechenfeng
 * @date 2018/6/28
 */
@FunctionalInterface
public interface OptAction {
    void execute(String optValue);
}
