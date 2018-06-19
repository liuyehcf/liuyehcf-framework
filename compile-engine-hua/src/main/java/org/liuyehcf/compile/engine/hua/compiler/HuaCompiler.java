package org.liuyehcf.compile.engine.hua.compiler;

import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.lr.Context;
import org.liuyehcf.compile.engine.core.cfg.lr.LALR;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.grammar.definition.SemanticAction;
import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;
import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.bytecode.cf._goto;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.util.*;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertFalse;

/**
 * Hua编译器
 *
 * @author hechenfeng
 * @date 2018/6/2
 */
public class HuaCompiler extends LALR<HuaResult> {
    public HuaCompiler(Grammar originalGrammar, LexicalAnalyzer lexicalAnalyzer) {
        super(originalGrammar, lexicalAnalyzer);
    }

    @Override
    protected Engine createCompiler(String input) {
        return new HuaEngine(input);
    }

    public class HuaEngine extends Engine {

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

        /**
         * 状态信息
         */
        private StatusInfo statusInfo = new StatusInfo();

        HuaEngine(String input) {
            super(input);
        }

        public int getOffset() {
            return offset;
        }

        public VariableSymbolTable getVariableSymbolTable() {
            return variableSymbolTable;
        }

        public MethodInfoTable getMethodInfoTable() {
            return methodInfoTable;
        }

        public StatusInfo getStatusInfo() {
            return statusInfo;
        }

        public void increaseOffset(int step) {
            offset += step;
        }

        public void resetOffset() {
            offset = 0;
        }

        @Override
        protected void before() {

        }

        @Override
        protected void after() {
            /*
             * 代码优化
             */
            optimize();

            /*
             * 设置编译结果
             */
            setResult(new HuaResult(variableSymbolTable, methodInfoTable));
        }

        private void optimize() {
            Map<MethodDescription, MethodInfo> table = methodInfoTable.getTable();

            for (MethodInfo methodInfo : table.values()) {
                optimize(methodInfo);
            }
        }

        private void optimize(MethodInfo methodInfo) {
            List<ByteCode> byteCodes = methodInfo.getByteCodes();

            /*
             * 将多级跳转指令改成一个跳转指令
             */
            simplifyControlTransferCode(byteCodes);

            /*
             * 删除多余的跳转指令（不可达）
             */
            removeRedundantControlTransferCode(byteCodes);
        }

        private void simplifyControlTransferCode(List<ByteCode> byteCodes) {
            for (ByteCode code : byteCodes) {
                if (!(code instanceof ControlTransfer)) {
                    continue;
                }

                ControlTransfer controlTransferCode = (ControlTransfer) code;

                int codeOffset = controlTransferCode.getCodeOffset();

                codeOffset = getFinalCodeOffset(byteCodes, codeOffset);

                controlTransferCode.setCodeOffset(codeOffset);
            }
        }

        private int getFinalCodeOffset(List<ByteCode> byteCodes, final int codeOffset) {
            int optimizedCodeOffset = codeOffset;

            ByteCode code;
            while ((code = byteCodes.get(optimizedCodeOffset)) instanceof ControlTransfer) {
                optimizedCodeOffset = ((ControlTransfer) code).getCodeOffset();
            }

            return optimizedCodeOffset;
        }

        private void removeRedundantControlTransferCode(List<ByteCode> byteCodes) {
            Set<Integer> visitedCodes = new HashSet<>();

            visitCode(0, visitedCodes, byteCodes);

            List<Integer> unvisitedCodeOffsets = new ArrayList<>();
            for (int i = 0; i < byteCodes.size(); i++) {
                unvisitedCodeOffsets.add(i);
            }
            unvisitedCodeOffsets.removeAll(visitedCodes);

            Collections.sort(unvisitedCodeOffsets);

            List<ControlTransfer> controlTransferCodes = new ArrayList<>();
            for (ByteCode code : byteCodes) {
                if (code instanceof ControlTransfer) {
                    controlTransferCodes.add((ControlTransfer) code);
                }
            }

            for (int codeOffset : unvisitedCodeOffsets) {
                /*
                 * 如果转移指令跳转目标代码的序号大于codeOffset，由于codeOffset处的指令会被删除
                 * 因此该跳转指令的目标代码序号需要-1
                 */
                for (ControlTransfer controlTransferCode : controlTransferCodes) {
                    assertFalse(controlTransferCode.getCodeOffset() == codeOffset);
                    if (controlTransferCode.getCodeOffset() > codeOffset) {
                        controlTransferCode.setCodeOffset(controlTransferCode.getCodeOffset() - 1);
                    }
                }

                byteCodes.remove(codeOffset);
            }
        }

        private void visitCode(int codeOffset, Set<Integer> visitedCodes, List<ByteCode> byteCodes) {
            if (codeOffset >= byteCodes.size() || visitedCodes.contains(codeOffset)) {
                return;
            }

            visitedCodes.add(codeOffset);
            ByteCode code = byteCodes.get(codeOffset);

            if (code instanceof ControlTransfer) {
                visitCode(((ControlTransfer) code).getCodeOffset(), visitedCodes, byteCodes);
                if (!(code instanceof _goto)) {
                    visitCode(codeOffset + 1, visitedCodes, byteCodes);
                }
            } else {
                visitCode(codeOffset + 1, visitedCodes, byteCodes);
            }
        }

        @Override
        protected void onReduction(Context context) {
            List<SemanticAction> semanticActions = context.getRawPrimaryProduction().getSemanticActions();
            if (semanticActions == null) {
                return;
            }

            for (SemanticAction semanticAction : semanticActions) {
                ((AbstractSemanticAction) semanticAction).onAction(new HuaContext(context, this));
            }
        }
    }
}
