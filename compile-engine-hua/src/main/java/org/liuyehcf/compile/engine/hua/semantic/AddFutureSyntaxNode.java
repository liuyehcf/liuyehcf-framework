package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * @author chenlu
 * @date 2018/6/2
 */
public class AddFutureSyntaxNode extends AbstractSemanticAction {

    /**
     * offset 相对于top的偏移量
     */
    private final int offset;

    public AddFutureSyntaxNode(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }
}
