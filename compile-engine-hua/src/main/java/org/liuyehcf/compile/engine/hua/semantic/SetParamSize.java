package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * @author chenlu
 * @date 2018/6/7
 */
public class SetParamSize extends AbstractSemanticAction {

    private final int offset;

    private final int value;

    public SetParamSize(int offset, int value) {
        this.offset = offset;
        this.value = value;
    }

    public int getOffset() {
        return offset;
    }

    public int getValue() {
        return value;
    }
}
