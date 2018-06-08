package org.liuyehcf.compile.engine.hua.compiler;

import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.lr.LALR;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.core.grammar.definition.AbstractSemanticAction;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.hua.bytecode.*;
import org.liuyehcf.compile.engine.hua.production.AttrName;
import org.liuyehcf.compile.engine.hua.semantic.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            System.out.println(methodInfoTable);
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
                } else if (semanticAction instanceof Assignment) {
                    processAssignment(stack, (Assignment) semanticAction);
                } else if (semanticAction instanceof BinaryOperator) {
                    processBinaryOperator(stack, (BinaryOperator) semanticAction);
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
                } else if (semanticAction instanceof SetAssignOperator) {
                    processSetAssignOperator(stack, (SetAssignOperator) semanticAction);
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

        private void processAssignment(FutureSyntaxNodeStack stack, Assignment semanticAction) {
            int fromStackOffset = semanticAction.getFromStackOffset();
            int toStackOffset = semanticAction.getToStackOffset();
            int operatorOffset = semanticAction.getOperatorOffset();

            SetAssignOperator.Operator assignOperator = stack.get(operatorOffset).get(AttrName.ASSIGN_OPERATOR.name());

            switch (assignOperator) {
                case NORMAL_ASSIGN:
                    int fromVariableOffset = stack.get(fromStackOffset).get(AttrName.ADDRESS.name());
                    int toVariableOffset = stack.get(toStackOffset).get(AttrName.ADDRESS.name());
                    stack.get(toStackOffset).put(AttrName.ADDRESS.name(), fromVariableOffset);
                    methodInfoTable.getCurMethodInfo().pushByteCode(new _store(toVariableOffset));
                    break;
            }
        }

        private void processBinaryOperator(FutureSyntaxNodeStack stack, BinaryOperator semanticAction) {
            int leftStackOffset = semanticAction.getLeftStackOffset();
            int rightStackOffset = semanticAction.getRightStackOffset();
            BinaryOperator.Operator operator = semanticAction.getOperator();

            int leftVariableOffset = stack.get(leftStackOffset).get(AttrName.ADDRESS.name());
            int rightVariableOffset = stack.get(rightStackOffset).get(AttrName.ADDRESS.name());

            VariableSymbol leftVariable = variableSymbolTable.getVariableSymbolByOffset(leftVariableOffset);
            VariableSymbol rightVariable = variableSymbolTable.getVariableSymbolByOffset(rightVariableOffset);

            VariableSymbol newVariableSymbol;

            switch (operator) {
                case ADDITION:
                    checkIfTypeMatches(leftVariable, rightVariable, operator);
                    newVariableSymbol = variableSymbolTable.enter(
                            offset,
                            UUID.randomUUID().toString(),
                            leftVariable.getType(),
                            leftVariable.getWidth());
                    offset += leftVariable.getWidth();

                    stack.get(-2).put(AttrName.ADDRESS.name(), newVariableSymbol.getOffset());
                    methodInfoTable.getCurMethodInfo().pushByteCode(new _add(leftVariableOffset, rightVariableOffset, newVariableSymbol.getOffset()));
                    break;
                case SUBTRACTION:
                    checkIfTypeMatches(leftVariable, rightVariable, operator);
                    newVariableSymbol = variableSymbolTable.enter(
                            offset,
                            UUID.randomUUID().toString(),
                            leftVariable.getType(),
                            leftVariable.getWidth());
                    offset += leftVariable.getWidth();

                    stack.get(-2).put(AttrName.ADDRESS.name(), newVariableSymbol.getOffset());
                    methodInfoTable.getCurMethodInfo().pushByteCode(new _sub(leftVariableOffset, rightVariableOffset, newVariableSymbol.getOffset()));
                    break;
            }
        }

        private void processCreateVariable(FutureSyntaxNodeStack stack, CreateVariable semanticAction) {
            int stackOffset = semanticAction.getStackOffset();

            SyntaxNode node = stack.get(stackOffset);

            String name = node.getValue();
            String type = node.get(AttrName.TYPE.name());
            int width = node.get(AttrName.WIDTH.name());

            if (variableSymbolTable.enter(this.offset, name, type, width) == null) {
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
            int stackOffset = semanticAction.getStackOffset();
            String methodName = stack.get(stackOffset).getValue();
            methodInfoTable.setMethodNameOfCurrentMethod(methodName);
        }

        private void processGetVariableSymbolFromIdentifier(FutureSyntaxNodeStack stack) {
            String identifierName = stack.get(0).getValue();
            VariableSymbol variableSymbol = variableSymbolTable.getVariableSymbolByName(identifierName);
            if (variableSymbol == null) {
                throw new RuntimeException("标志符 " + identifierName + " 尚未定义");
            }
            stack.get(0).put(AttrName.ADDRESS.name(), variableSymbol.getOffset());
            methodInfoTable.getCurMethodInfo().pushOperand(variableSymbol.getOffset());
        }

        private void processIncreaseArrayTypeDim(FutureSyntaxNodeStack stack, IncreaseArrayTypeDim increaseArrayTypeDim) {
            int stackOffset = increaseArrayTypeDim.getStackOffset();
            String originType = stack.get(stackOffset).get(AttrName.TYPE.name());
            originType = originType + "[]";
            stack.get(stackOffset).put(AttrName.TYPE.name(), originType);
        }

        private void processIncreaseParamSize(FutureSyntaxNodeStack stack, IncreaseParamSize semanticAction) {
            int stackOffset = semanticAction.getStackOffset();
            int paramSize = stack.get(stackOffset).get(AttrName.PARAM_SIZE.name());
            stack.get(stackOffset).put(AttrName.PARAM_SIZE.name(), paramSize + 1);
        }

        @SuppressWarnings("unchecked")
        private void processPostDecrement(FutureSyntaxNodeStack stack) {
            if (stack.get(0).get(AttrName.CODES.name()) == null) {
                stack.get(0).put(AttrName.CODES.name(), new ArrayList<>());
            }
            ((List<ByteCode>) stack.get(0).get(AttrName.CODES.name())).add(new _iinc(1));
        }

        @SuppressWarnings("unchecked")
        private void processPostIncrement(FutureSyntaxNodeStack stack) {
            if (stack.get(0).get(AttrName.CODES.name()) == null) {
                stack.get(0).put(AttrName.CODES.name(), new ArrayList<>());
            }
            ((List<ByteCode>) stack.get(0).get(AttrName.CODES.name())).add(new _iinc(1));
        }

        private void processSaveParamInfo(FutureSyntaxNodeStack stack, SaveParamInfo semanticAction) {
            int stackOffset = semanticAction.getStackOffset();
            String type = stack.get(stackOffset).get(AttrName.TYPE.name());
            int width = stack.get(stackOffset).get(AttrName.WIDTH.name());
            methodInfoTable.addParamInfoToCurrentMethod(new ParamInfo(type, width));
        }

        private void processSetAssignOperator(FutureSyntaxNodeStack stack, SetAssignOperator semanticAction) {
            int stackOffset = semanticAction.getStackOffset();
            SetAssignOperator.Operator operator = semanticAction.getOperator();
            stack.get(stackOffset).put(AttrName.ASSIGN_OPERATOR.name(), operator);
        }

        private void processSetParamSize(FutureSyntaxNodeStack stack, SetParamSize semanticAction) {
            int stackOffset = semanticAction.getStackOffset();
            int value = semanticAction.getValue();
            assertNull(stack.get(stackOffset).get(AttrName.PARAM_SIZE.name()));
            stack.get(stackOffset).put(AttrName.PARAM_SIZE.name(), value);
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

        private void enterNamespace() {
            variableSymbolTable.enterNamespace();
        }

        private void exitNamespace() {
            variableSymbolTable.exitNamespace();
        }

        private void checkIfTypeMatches(VariableSymbol leftVariable, VariableSymbol rightVariable, BinaryOperator.Operator operator) {
            if (!leftVariable.getType().equals(rightVariable.getType())) {
                throw new RuntimeException(operator.getSign() + " 运算符两侧类型不匹配");
            }
        }
    }
}
