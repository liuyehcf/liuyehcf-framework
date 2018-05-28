package org.liuyehcf.compile.engine.core.rg;

/**
 * Pattern，类似于java.util.regex.Pattern
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public interface Pattern {
    /**
     * 返回一个Matcher对象
     *
     * @param input 输入
     * @return 匹配器
     */
    Matcher matcher(String input);
}
