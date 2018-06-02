package org.liuyehcf.compile.engine.hua.action;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.semantic.SetSynAttr;
import org.liuyehcf.compile.engine.hua.type.AttrName;

import static org.liuyehcf.compile.engine.hua.production.Type.NORMAL_INT;

/**
 * @author hechenfeng
 * @date 2018/6/2
 */
public class TypeAction {
    public static final AbstractSemanticAction[] ACTION_124_3 = {
            new SetSynAttr(
                    0,
                    AttrName.TYPE.name(),
                    NORMAL_INT
            ),
            new SetSynAttr(
                    0,
                    AttrName.WIDTH.name(),
                    "4"
            )
    };
}
