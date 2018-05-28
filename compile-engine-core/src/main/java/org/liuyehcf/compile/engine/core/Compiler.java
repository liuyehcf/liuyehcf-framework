package org.liuyehcf.compile.engine.core;

/**
 * 编译器接口
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public interface Compiler extends GrammarHolder {
    /**
     * 编译给定输入字符串
     *
     * @param input 输入
     * @return 编译结果
     */
    CompileResult compile(String input);
}
