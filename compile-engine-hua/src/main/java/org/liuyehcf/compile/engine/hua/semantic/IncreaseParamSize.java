package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * @author chenlu
 * @date 2018/6/7
 */
public class IncreaseParamSize extends AbstractSemanticAction {
    private final int stackOffset;

    public IncreaseParamSize(int stackOffset) {
        this.stackOffset = stackOffset;
    }

    public int getStackOffset() {
        return stackOffset;
    }
}
