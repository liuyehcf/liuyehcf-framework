package org.liuyehcf.compile.engine.core.cfg;

/**
 * 词法分析器接口
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public interface LexicalAnalyzer {

    /**
     * 获取Token迭代器
     *
     * @param input 输入
     * @return Token迭代器
     */
    TokenIterator iterator(String input);

    interface TokenIterator {
        /**
         * 是否还有后续Token
         *
         * @return 是否有下一个Token
         */
        boolean hasNext();

        /**
         * 获取下一个Token
         *
         * @return Token
         */
        Token next();

        /**
         * 是否完成了整个匹配
         *
         * @return 是否到达文末
         */
        boolean reachesEof();
    }
}
