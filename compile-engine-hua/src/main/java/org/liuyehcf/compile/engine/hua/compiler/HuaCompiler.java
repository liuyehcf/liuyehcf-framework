package org.liuyehcf.compile.engine.hua.compiler;

import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.lr.LALR;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.hua.action.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.*;

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

        /**
         * 地址偏移量，初始化为0
         */
        private int offset = 0;

        /**
         * 变量符号表
         */
        private VariableSymbolTable variableSymbolTable = new VariableSymbolTable();

        HuaEngine(String input) {
            super(input);
        }

        @Override
        protected void before() {
            super.before();
        }

        @Override
        protected void after() {
            System.out.println(variableSymbolTable);
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
                } else if (semanticAction instanceof CreateVariable) {
                    processCreateVariable(stack, (CreateVariable) semanticAction);
                } else if (semanticAction instanceof EnterNamespace) {
                    processEnterNamespace(stack);
                } else if (semanticAction instanceof ExitNamespace) {
                    processExitNamespace();
                } else if (semanticAction instanceof IgnoreNextEnterNamespace) {
                    processIgnoreNextEnterNamespace(stack, (IgnoreNextEnterNamespace) semanticAction);
                } else if (semanticAction instanceof SetSynAttrFromLexical) {
                    processSetSynAttrFromLexical(stack, (SetSynAttrFromLexical) semanticAction);
                } else if (semanticAction instanceof SetSynAttrFromSystem) {
                    processSetSynAttrFromSystem(stack, (SetSynAttrFromSystem) semanticAction);
                }
            }
        }

        private void processAddFutureSyntaxNode(FutureSyntaxNodeStack stack, AddFutureSyntaxNode semanticAction) {
            int stackOffset = semanticAction.getStackOffset();
            stack.addFutureSyntaxNode(stackOffset);
        }


        private void processAssignAttr(FutureSyntaxNodeStack stack, AssignAttr semanticAction) {
            int fromStackOffset = semanticAction.getFromStackOffset();
            int toStackOffset = semanticAction.getToStackOffset();
            String fromAttrName = semanticAction.getFromAttrName();
            String toAttrName = semanticAction.getToAttrName();

            SyntaxNode fromNode = stack.get(fromStackOffset);
            SyntaxNode toNode = stack.get(toStackOffset);

            assertNotNull(fromNode.get(fromAttrName));
            toNode.put(toAttrName, fromNode.get(fromAttrName));
        }

        private void processCreateVariable(FutureSyntaxNodeStack stack, CreateVariable semanticAction) {
            int stackOffset = semanticAction.getStackOffset();

            SyntaxNode node = stack.get(stackOffset);

            String name = node.getValue();
            String type = (String) node.get(AttrName.TYPE.getName());
            int width = (int) node.get(AttrName.WIDTH.getName());

            if (!variableSymbolTable.enter(this.offset, name, type, width)) {
                throw new RuntimeException("标志符 " + name + " 已存在，请勿重复定义");
            }
            this.offset += width;
        }

        private void processEnterNamespace(FutureSyntaxNodeStack stack) {
            if (stack.get(0).get(AttrName.IGNORE_NEXT_ENTER_NAMESPACE.getName()) != null) {
                stack.get(0).put(AttrName.IGNORE_NEXT_ENTER_NAMESPACE.getName(), null);
                return;
            }
            enterNamespace();
        }

        private void processExitNamespace() {
            exitNamespace();
        }

        private void processIgnoreNextEnterNamespace(FutureSyntaxNodeStack stack, IgnoreNextEnterNamespace semanticAction) {
            int stackOffset = semanticAction.getStackOffset();

            stack.get(stackOffset).put(AttrName.IGNORE_NEXT_ENTER_NAMESPACE.getName(), AttrName.IGNORE_NEXT_ENTER_NAMESPACE.getName());
        }

        private void processSetSynAttrFromLexical(FutureSyntaxNodeStack stack, SetSynAttrFromLexical semanticAction) {
            int fromStackOffset = semanticAction.getFromStackOffset();
            int toStackOffset = semanticAction.getToStackOffset();
            String toAttrName = semanticAction.getToAttrName();

            SyntaxNode fromNode = stack.get(fromStackOffset);
            SyntaxNode toNode = stack.get(toStackOffset);

            assertNotNull(fromNode.getValue());
            toNode.put(toAttrName, fromNode.getValue());
        }

        private void processSetSynAttrFromSystem(FutureSyntaxNodeStack stack, SetSynAttrFromSystem semanticAction) {
            int stackOffset = semanticAction.getStackOffset();
            String attrName = semanticAction.getAttrName();
            Object attrValue = semanticAction.getAttrValue();

            stack.get(stackOffset).put(attrName, attrValue);
        }

        public void enterNamespace() {
            variableSymbolTable.enterNamespace();
        }

        public void exitNamespace() {
            variableSymbolTable.exitNamespace();
        }

        private void lookup(String name) {

        }

        private void gen() {
        }

        private void newTemp() {

        }
    }


}
