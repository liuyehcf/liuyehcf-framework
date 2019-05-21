package com.github.liuyehcf.framework.compile.engine;

/**
 * 编译器接口
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public interface Compiler<T> extends GrammarHolder {
    /**
     * 编译给定输入字符串
     *
     * @param input 输入
     * @return 编译结果
     */
    CompileResult<T> compile(String input);
}
