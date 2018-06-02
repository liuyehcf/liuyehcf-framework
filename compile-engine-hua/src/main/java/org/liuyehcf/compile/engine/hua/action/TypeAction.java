package org.liuyehcf.compile.engine.hua.action;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.semantic.AssignAttr;
import org.liuyehcf.compile.engine.hua.semantic.SetAttr;
import org.liuyehcf.compile.engine.hua.type.AttrName;

import java.util.Arrays;
import java.util.List;

import static org.liuyehcf.compile.engine.hua.constant.TypeWidth.BOOLEAN_WIDTH;
import static org.liuyehcf.compile.engine.hua.constant.TypeWidth.INTEGER_WIDTH;
import static org.liuyehcf.compile.engine.hua.production.Type.NORMAL_BOOLEAN;
import static org.liuyehcf.compile.engine.hua.production.Type.NORMAL_INT;

/**
 * @author hechenfeng
 * @date 2018/6/2
 */
public class TypeAction {
    public static final List<AbstractSemanticAction> ACTION_119_1 = Arrays.asList(
            new AssignAttr(
                    1,
                    AttrName.TYPE.getName(),
                    0,
                    AttrName.TYPE.getName()
            ),
            new AssignAttr(
                    1,
                    AttrName.WIDTH.getName(),
                    0,
                    AttrName.WIDTH.getName())
    );

    public static final List<AbstractSemanticAction> ACTION_120_1 = Arrays.asList(
            new AssignAttr(
                    1,
                    AttrName.TYPE.getName(),
                    0,
                    AttrName.TYPE.getName()
            ),
            new AssignAttr(
                    1,
                    AttrName.WIDTH.getName(),
                    0,
                    AttrName.WIDTH.getName())
    );

    public static final List<AbstractSemanticAction> ACTION_120_2 = Arrays.asList(
            new SetAttr(
                    0,
                    AttrName.TYPE.getName(),
                    NORMAL_BOOLEAN
            ),
            new SetAttr(
                    0,
                    AttrName.WIDTH.getName(),
                    BOOLEAN_WIDTH
            )
    );

    public static final List<AbstractSemanticAction> ACTION_122_1 = Arrays.asList(
            new AssignAttr(
                    1,
                    AttrName.TYPE.getName(),
                    0,
                    AttrName.TYPE.getName()
            ),
            new AssignAttr(
                    1,
                    AttrName.WIDTH.getName(),
                    0,
                    AttrName.WIDTH.getName())
    );

    public static final List<AbstractSemanticAction> ACTION_124_3 = Arrays.asList(
            new SetAttr(
                    0,
                    AttrName.TYPE.getName(),
                    NORMAL_INT
            ),
            new SetAttr(
                    0,
                    AttrName.WIDTH.getName(),
                    INTEGER_WIDTH
            )
    );
}
