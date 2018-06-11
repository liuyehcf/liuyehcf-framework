package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;

/**
 * @author chenlu
 * @date 2018/6/11
 */
public class VariableInitialization extends AbstractSemanticAction {
    public static final int INITIALIZATION_EXPRESSION_STACK_OFFSET = 0;
    public static final int VARIABLE_ID_STACK_OFFSET = -2;
}
