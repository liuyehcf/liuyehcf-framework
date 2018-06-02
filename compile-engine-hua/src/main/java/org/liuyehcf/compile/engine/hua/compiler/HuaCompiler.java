package org.liuyehcf.compile.engine.hua.compiler;

import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.lr.LALR;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.hua.semantic.AddFutureSyntaxNode;
import org.liuyehcf.compile.engine.hua.semantic.AssignAttr;
import org.liuyehcf.compile.engine.hua.semantic.SetSynAttrFromLexical;
import org.liuyehcf.compile.engine.hua.semantic.SetSynAttrFromSystem;

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
        return new HuaEngine(input);
    }

    private class HuaEngine extends Engine {

        HuaEngine(String input) {
            super(input);
        }

        @Override
        protected void before() {
            super.before();
        }

        @Override
        protected void after() {
            super.after();
        }

        @Override
        protected void action(PrimaryProduction ppReduction, FutureSyntaxNodeStack stack) {
            List<AbstractSemanticAction> semanticActions = ppReduction.getSemanticActions();
            if (semanticActions == null) {
                return;
            }

            for (AbstractSemanticAction semanticAction : semanticActions) {
                if (semanticAction instanceof AddFutureSyntaxNode) {
                    processAddFutureSyntaxNode(stack, (AddFutureSyntaxNode) semanticAction);
                } else if (semanticAction instanceof AssignAttr) {
                    processAssignAttr(stack, (AssignAttr) semanticAction);
                } else if (semanticAction instanceof SetSynAttrFromLexical) {
                    processSetSynAttrFromLexical(stack, (SetSynAttrFromLexical) semanticAction);
                } else if (semanticAction instanceof SetSynAttrFromSystem) {
                    processSetSynAttrFromSystem(stack, (SetSynAttrFromSystem) semanticAction);
                }
            }
        }

        private void processAddFutureSyntaxNode(FutureSyntaxNodeStack stack, AddFutureSyntaxNode semanticAction) {
            int offset = semanticAction.getOffset();
            stack.addFutureSyntaxNode(offset);
        }


        private void processAssignAttr(FutureSyntaxNodeStack stack, AssignAttr semanticAction) {
            int fromPos = semanticAction.getFromPos();
            int toPos = semanticAction.getToPos();
            String fromAttrName = semanticAction.getFromAttrName();
            String toAttrName = semanticAction.getToAttrName();

            SyntaxNode fromNode = stack.get(fromPos);
            SyntaxNode toNode = stack.get(toPos);

            assertNotNull(fromNode.get(fromAttrName));
            toNode.put(toAttrName, fromNode.get(fromAttrName));
        }

        private void processSetSynAttrFromLexical(FutureSyntaxNodeStack stack, SetSynAttrFromLexical semanticAction) {
            int fromPos = semanticAction.getFromPos();
            int toPos = semanticAction.getToPos();
            String toAttrName = semanticAction.getToAttrName();

            SyntaxNode fromNode = stack.get(fromPos);
            SyntaxNode toNode = stack.get(toPos);

            assertNotNull(fromNode.getValue());
            toNode.put(toAttrName, fromNode.getValue());
        }

        private void processSetSynAttrFromSystem(FutureSyntaxNodeStack stack, SetSynAttrFromSystem semanticAction) {
            int pos = semanticAction.getPos();
            String attrName = semanticAction.getAttrName();
            Object attrValue = semanticAction.getAttrValue();

            stack.get(pos).put(attrName, attrValue);
        }
    }


}
