package org.liuyehcf.compile.engine.core.cfg;

import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;

/**
 * 词法解析Token
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class Token {

    public static final Token DOLLAR = new Token(Symbol.DOLLAR, "__$__");

    /**
     * <p>token的id，与文法中的文法符号一一对应</p>
     * <p>若type不是REGEX，那么id.value与value相同</p>
     */
    private final Symbol id;

    /**
     * token具体的值
     */
    private final String value;

    public Token(Symbol id, String value) {
        this.id = id;
        this.value = value;
    }

    public Symbol getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + '\"' +
                ", \"value\":\"" + value + '\"' +
                '}';
    }
}
