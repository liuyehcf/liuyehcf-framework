package org.liuyehcf.compile.engine.hua.compiler;

import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.lr.LALR;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.hua.semantic.AssignAttr;
import org.liuyehcf.compile.engine.hua.semantic.SetAttr;

import java.util.List;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNotNull;

/**
 * @author chenlu
 * @date 2018/6/2
 */
public class HuaCompiler extends LALR {
    private HuaCompiler(LexicalAnalyzer lexicalAnalyzer, Grammar originalGrammar) {
        super(lexicalAnalyzer, originalGrammar);
    }

    public static LRCompiler create(LexicalAnalyzer lexicalAnalyzer, Grammar originalGrammar) {
        HuaCompiler compiler = new HuaCompiler(lexicalAnalyzer, originalGrammar);

        compiler.init();

        return compiler;
    }

    @Override
    protected Engine createCompiler(String input) {
        return new Engine(input) {
            @Override
            protected void action(PrimaryProduction ppReduction, List<SyntaxNode> syntaxNodes) {
                List<AbstractSemanticAction> semanticActions = ppReduction.getSemanticActions();
                if (semanticActions == null) {
                    return;
                }

                for (AbstractSemanticAction semanticAction : semanticActions) {
                    if (semanticAction instanceof SetAttr) {
                        processSetSynAttr(syntaxNodes, (SetAttr) semanticAction);
                    } else if (semanticAction instanceof AssignAttr) {
                        processCopySynAttr(syntaxNodes, (AssignAttr) semanticAction);
                    }
                }
            }
        };
    }

    private static void processSetSynAttr(List<SyntaxNode> syntaxNodes, SetAttr semanticAction) {
        int pos = semanticAction.getPos();
        String attrName = semanticAction.getAttrName();
        String attrValue = semanticAction.getAttrValue();

        syntaxNodes.get(pos).put(attrName, attrValue);
    }

    private static void processCopySynAttr(List<SyntaxNode> syntaxNodes, AssignAttr semanticAction) {
        int fromPos = semanticAction.getFromPos();
        String fromAttrName = semanticAction.getFromAttrName();
        int toPos = semanticAction.getToPos();
        String toAttrName = semanticAction.getToAttrName();

        Object value = syntaxNodes.get(fromPos).get(fromAttrName);
        assertNotNull(value);
        syntaxNodes.get(toPos).put(toAttrName, value);
    }
}
