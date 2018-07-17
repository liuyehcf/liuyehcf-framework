package org.liuyehcf.compile.engine.core.cfg.lexical;

import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;

/**
 * Token解析上下文
 *
 * @author hechenfeng
 * @date 2018/6/30
 */
public class TokenContext {

    /**
     * token对应的id
     */
    private final Symbol id;

    /**
     * 输入串
     */
    private final String remainInput;

    /**
     * 移动的长度
     */
    private int moveLength;

    TokenContext(Symbol id, String remainInput) {
        this.id = id;
        this.remainInput = remainInput;
    }

    public Symbol getId() {
        return id;
    }

    public String getRemainInput() {
        return remainInput;
    }

    public int getMoveLength() {
        return moveLength;
    }

    public void setMoveLength(int moveLength) {
        this.moveLength = moveLength;
    }
}
