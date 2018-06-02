package org.liuyehcf.compile.engine.hua.action;

import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.semantic.AddFutureSyntaxNode;
import org.liuyehcf.compile.engine.hua.semantic.AssignAttr;
import org.liuyehcf.compile.engine.hua.semantic.CreateVariable;

import java.util.Arrays;
import java.util.List;

/**
 * @author hechenfeng
 * @date 2018/6/2
 */
public class ProgramAction {
    public static final List<AbstractSemanticAction> ACTION_50_1_1 = Arrays.asList(
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

    public static final List<AbstractSemanticAction> ACTION_66_1 = Arrays.asList(

    );

    public static final List<AbstractSemanticAction> ACTION_66_2 = Arrays.asList(

    );

    public static final List<AbstractSemanticAction> ACTION_66_2_1 = Arrays.asList(
            new AddFutureSyntaxNode(1),
            new AssignAttr(
                    -1,
                    AttrName.TYPE.getName(),
                    1,
                    AttrName.TYPE.getName()
            ),
            new AssignAttr(
                    -1,
                    AttrName.WIDTH.getName(),
                    1,
                    AttrName.WIDTH.getName()
            )
    );

    public static final List<AbstractSemanticAction> ACTION_70_1 = Arrays.asList(
            new CreateVariable(0)
    );
}
