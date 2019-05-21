package com.github.liuyehcf.framework.compile.engine.cfg;

import com.github.liuyehcf.framework.compile.engine.Compiler;

/**
 * CFG文法编译器接口
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public interface CfgCompiler<T> extends Compiler<T> {
    /**
     * 获取First集的JSON串
     *
     * @return First集合的JSON串
     */
    String getFirstJSONString();

    /**
     * 获取Follow集的JSON串
     *
     * @return Follow集合的JSON串
     */
    String getFollowJSONString();

    /**
     * 获取预测分析表的的Markdown格式的字符串
     *
     * @return 预测分析表的Markdown格式的串
     */
    String getAnalysisTableMarkdownString();

    /**
     * 当前文法是否合法（是否是当前文法分析器支持的文法）
     *
     * @return 是否合法
     */
    boolean isLegal();
}
