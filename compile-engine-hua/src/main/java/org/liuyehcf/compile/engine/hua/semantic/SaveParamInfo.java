package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * @author chenlu
 * @date 2018/6/7
 */
public class SaveParamInfo extends AbstractSemanticAction {
    private final int offset;

    public SaveParamInfo(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }
}
