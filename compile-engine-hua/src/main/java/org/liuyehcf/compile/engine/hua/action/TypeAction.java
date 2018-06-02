package org.liuyehcf.compile.engine.hua.action;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.semantic.CopySynAttr;
import org.liuyehcf.compile.engine.hua.semantic.SetSynAttr;
import org.liuyehcf.compile.engine.hua.type.AttrName;

import java.util.Arrays;
import java.util.List;

import static org.liuyehcf.compile.engine.hua.constant.TypeWidth.INTEGER_WIDTH;
import static org.liuyehcf.compile.engine.hua.production.Type.NORMAL_INT;

/**
 * @author hechenfeng
 * @date 2018/6/2
 */
public class TypeAction {
    public static final List<AbstractSemanticAction> ACTION_122_1 = Arrays.asList(
            new CopySynAttr(
                    1,
                    AttrName.TYPE.name(),
                    0,
                    AttrName.TYPE.name()
            ),
            new CopySynAttr(
                    1,
                    AttrName.WIDTH.name(),
                    0,
                    AttrName.WIDTH.name())
    );

    public static final List<AbstractSemanticAction> ACTION_124_3 = Arrays.asList(
            new SetSynAttr(
                    0,
                    AttrName.TYPE.name(),
                    NORMAL_INT
            ),
            new SetSynAttr(
                    0,
                    AttrName.WIDTH.name(),
                    INTEGER_WIDTH
            )
    );
}
