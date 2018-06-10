package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * @author chenlu
 * @date 2018/6/7
 */
public class AddParamInfo extends AbstractSemanticAction {
    private final int listStackOffset;

    private final int paramStackOffset;

    public AddParamInfo(int listStackOffset, int paramStackOffset) {
        this.listStackOffset = listStackOffset;
        this.paramStackOffset = paramStackOffset;
    }

    public int getListStackOffset() {
        return listStackOffset;
    }

    public int getParamStackOffset() {
        return paramStackOffset;
    }
}
