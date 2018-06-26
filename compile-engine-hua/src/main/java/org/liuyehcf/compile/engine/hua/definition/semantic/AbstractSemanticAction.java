package org.liuyehcf.compile.engine.hua.definition.semantic;

import org.liuyehcf.compile.engine.core.grammar.definition.SemanticAction;
import org.liuyehcf.compile.engine.hua.core.HuaContext;

import java.io.Serializable;

/**
 * 语义动作抽象基类
 *
 * @author hechenfeng
 * @date 2018/6/11
 */
public abstract class AbstractSemanticAction implements SemanticAction<HuaContext>, Serializable {

    public static final String NOT_NULL = "NOT_NULL";

    @Override
    public void onAction(HuaContext context) {

    }
}
