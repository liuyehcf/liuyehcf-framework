package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * @author chenlu
 * @date 2018/6/7
 */
public class SetParamSize extends AbstractSemanticAction {

    private final int stackOffset;

    private final int value;

    public SetParamSize(int stackOffset, int value) {
        this.stackOffset = stackOffset;
        this.value = value;
    }

    public int getStackOffset() {
        return stackOffset;
    }

    public int getValue() {
        return value;
    }
}
