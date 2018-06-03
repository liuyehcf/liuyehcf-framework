package org.liuyehcf.compile.engine.hua.action;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.semantic.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author hechenfeng
 * @date 2018/6/2
 */
public class BlockAction {
    public static final List<AbstractSemanticAction> ACTION_139_1 = Arrays.asList(
            new ExitNamespace()
    );

    public static final List<AbstractSemanticAction> ACTION_139_1_1 = Arrays.asList(
            new EnterNamespace()
    );

    public static final List<AbstractSemanticAction> ACTION_144_1 = Arrays.asList(

    );

    public static final List<AbstractSemanticAction> ACTION_146_1 = Arrays.asList(

    );

    public static final List<AbstractSemanticAction> ACTION_146_1_1 = Arrays.asList(
            new AddFutureSyntaxNode(1),
            new AssignAttr(
                    0,
                    AttrName.TYPE.getName(),
                    1,
                    AttrName.TYPE.getName()
            ),
            new AssignAttr(
                    0,
                    AttrName.WIDTH.getName(),
                    1,
                    AttrName.WIDTH.getName()
            )
    );

    public static final List<AbstractSemanticAction> ACTION_188_1 = Arrays.asList(
            new ExitNamespace()
    );
}
