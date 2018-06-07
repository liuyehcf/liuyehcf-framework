package org.liuyehcf.compile.engine.hua.compiler;

import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.lr.LALR;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;
import org.liuyehcf.compile.engine.hua.bytecode._iinc;
import org.liuyehcf.compile.engine.hua.production.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.*;

import java.util.ArrayList;
import java.util.List;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNotNull;
import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNull;

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

        /**
         * 方法定义表
         */
        private MethodInfoTable methodInfoTable = new MethodInfoTable();

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
                } else if (semanticAction instanceof EnterMethod) {
                    processEnterMethod();
                } else if (semanticAction instanceof EnterNamespace) {
                    processEnterNamespace();
                } else if (semanticAction instanceof ExitMethod) {
                    processExitMethod();
                } else if (semanticAction instanceof ExitNamespace) {
                    processExitNamespace();
                } else if (semanticAction instanceof GetMethodNameFromIdentifier) {
                    processGetMethodNameFromIdentifier(stack, (GetMethodNameFromIdentifier) semanticAction);
                } else if (semanticAction instanceof GetVariableSymbolFromIdentifier) {
                    processGetVariableSymbolFromIdentifier(stack);
                } else if (semanticAction instanceof IncreaseArrayTypeDim) {
                    processIncreaseArrayTypeDim(stack, (IncreaseArrayTypeDim) semanticAction);
                } else if (semanticAction instanceof IncreaseParamSize) {
                    processIncreaseParamSize(stack, (IncreaseParamSize) semanticAction);
                } else if (semanticAction instanceof PostDecrement) {
                    processPostDecrement(stack);
                } else if (semanticAction instanceof PostIncrement) {
                    processPostIncrement(stack);
                } else if (semanticAction instanceof SaveParamInfo) {
                    processSaveParamInfo(stack, (SaveParamInfo) semanticAction);
                } else if (semanticAction instanceof SetParamSize) {
                    processSetParamSize(stack, (SetParamSize) semanticAction);
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

        private void processEnterMethod() {
            methodInfoTable.enterMethod();
        }

        private void processEnterNamespace() {
            enterNamespace();
        }

        private void processExitMethod() {
            methodInfoTable.exitMethod();
        }

        private void processExitNamespace() {
            exitNamespace();
        }

        private void processGetMethodNameFromIdentifier(FutureSyntaxNodeStack stack, GetMethodNameFromIdentifier semanticAction) {
            int offset = semanticAction.getOffset();
            String methodName = stack.get(offset).getValue();
            methodInfoTable.setMethodNameOfCurrentMethod(methodName);
        }

        private void processGetVariableSymbolFromIdentifier(FutureSyntaxNodeStack stack) {
            String identifierName = stack.get(0).getValue();
            VariableSymbol variableSymbol = variableSymbolTable.getVariableSymbol(identifierName);
            if (variableSymbol == null) {
                throw new RuntimeException("标志符 " + identifierName + " 尚未定义");
            }
            stack.get(0).put(AttrName.ADDRESS.getName(), variableSymbol);
        }

        private void processIncreaseArrayTypeDim(FutureSyntaxNodeStack stack, IncreaseArrayTypeDim increaseArrayTypeDim) {
            int stackOffset = increaseArrayTypeDim.getStackOffset();
            String originType = (String) stack.get(stackOffset).get(AttrName.TYPE.getName());
            originType = originType + "[]";
            stack.get(stackOffset).put(AttrName.TYPE.getName(), originType);
        }

        private void processIncreaseParamSize(FutureSyntaxNodeStack stack, IncreaseParamSize semanticAction) {
            int offset = semanticAction.getOffset();
            int paramSize = (int) stack.get(offset).get(AttrName.PARAM_SIZE.getName());
            stack.get(offset).put(AttrName.PARAM_SIZE.getName(), paramSize + 1);
        }

        @SuppressWarnings("unchecked")
        private void processPostDecrement(FutureSyntaxNodeStack stack) {
            if (stack.get(0).get(AttrName.CODES.getName()) == null) {
                stack.get(0).put(AttrName.CODES.getName(), new ArrayList<>());
            }
            ((List<ByteCode>) stack.get(0).get(AttrName.CODES.getName())).add(new _iinc(1));
        }

        @SuppressWarnings("unchecked")
        private void processPostIncrement(FutureSyntaxNodeStack stack) {
            if (stack.get(0).get(AttrName.CODES.getName()) == null) {
                stack.get(0).put(AttrName.CODES.getName(), new ArrayList<>());
            }
            ((List<ByteCode>) stack.get(0).get(AttrName.CODES.getName())).add(new _iinc(1));
        }

        private void processSaveParamInfo(FutureSyntaxNodeStack stack, SaveParamInfo saveParamInfo) {
            int offset = saveParamInfo.getOffset();
            String name = stack.get(offset).getValue();
            String type = (String) stack.get(offset).get(AttrName.TYPE.getName());
            int width = (int) stack.get(offset).get(AttrName.WIDTH.getName());
            methodInfoTable.addParamInfoToCurrentMethod(new ParamInfo(type, width));
        }

        private void processSetParamSize(FutureSyntaxNodeStack stack, SetParamSize semanticAction) {
            int offset = semanticAction.getOffset();
            int value = semanticAction.getValue();
            assertNull(stack.get(offset).get(AttrName.PARAM_SIZE.getName()));
            stack.get(offset).put(AttrName.PARAM_SIZE.getName(), value);
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

    }
}
