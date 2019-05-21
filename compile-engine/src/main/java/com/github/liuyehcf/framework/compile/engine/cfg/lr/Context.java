package com.github.liuyehcf.framework.compile.engine.cfg.lr;

import com.github.liuyehcf.framework.compile.engine.grammar.definition.PrimaryProduction;

/**
 * 上下文信息
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class Context {
    /**
     * 规约时的产生式
     */
    private final PrimaryProduction rawPrimaryProduction;

    /**
     * 语法树节点站
     */
    private final FutureSyntaxNodeStack stack;

    /**
     * 产生式左部非终结符对应的语法树节点
     */
    private final SyntaxNode leftNode;

    public Context(PrimaryProduction rawPrimaryProduction, FutureSyntaxNodeStack stack, SyntaxNode leftNode) {
        this.rawPrimaryProduction = rawPrimaryProduction;
        this.stack = stack;
        this.leftNode = leftNode;
    }

    public PrimaryProduction getRawPrimaryProduction() {
        return rawPrimaryProduction;
    }

    public FutureSyntaxNodeStack getStack() {
        return stack;
    }

    public SyntaxNode getLeftNode() {
        return leftNode;
    }
}
