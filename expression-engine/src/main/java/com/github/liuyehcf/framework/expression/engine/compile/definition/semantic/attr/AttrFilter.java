package com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.attr;

import com.github.liuyehcf.framework.expression.engine.compile.CompilerContext;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.expression.engine.compile.definition.semantic.AbstractSemanticAction;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 属性过滤（规约后产生式左部的语法树节点），设置保留的属性值
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class AttrFilter extends AbstractSemanticAction {

    /**
     * 保留的属性名集合
     */
    private final Set<String> attrNames;

    public AttrFilter(AttrName... attrNames) {
        this.attrNames = new HashSet<>();

        for (AttrName attrName : attrNames) {
            this.attrNames.add(attrName.name());
        }
    }

    @Override
    public void onAction(CompilerContext context) {
        Map<String, Object> attrs = context.getLeftNode().getAttrs();

        Set<String> removedAttrNames = new HashSet<>();

        for (String attrName : attrs.keySet()) {
            if (!attrNames.contains(attrName)) {
                removedAttrNames.add(attrName);
            }
        }

        for (String attrName : removedAttrNames) {
            attrs.remove(attrName);
        }
    }
}
