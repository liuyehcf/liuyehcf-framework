package org.liuyehcf.compile.engine.hua.compiler;

import org.liuyehcf.compile.engine.core.cfg.lr.Context;
import org.liuyehcf.compile.engine.core.cfg.lr.LALR;
import org.liuyehcf.compile.engine.core.grammar.definition.SemanticAction;
import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;
import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.bytecode.cf._goto;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.io.*;
import java.util.*;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertFalse;
import static org.liuyehcf.compile.engine.hua.definition.GrammarDefinition.GRAMMAR;
import static org.liuyehcf.compile.engine.hua.definition.GrammarDefinition.LEXICAL_ANALYZER;

/**
 * Hua编译器
 *
 * @author hechenfeng
 * @date 2018/6/2
 */
public class HuaCompiler extends LALR<HuaResult> implements Serializable {
    public HuaCompiler() {
        super(GRAMMAR, LEXICAL_ANALYZER);
    }

    @Override
    protected Engine createCompiler(String input) {
        return new HuaEngine(input);
    }

    public class HuaEngine extends Engine {

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

        VariableSymbolTable getVariableSymbolTable() {
            return variableSymbolTable;
        }

        MethodInfoTable getMethodInfoTable() {
            return methodInfoTable;
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
            Map<ControlTransfer, Integer> controlTransferCodeOffsetMap = new HashMap<>();
            for (int offset = 0; offset < byteCodes.size(); offset++) {
                ByteCode code = byteCodes.get(offset);
                if (code instanceof ControlTransfer) {
                    controlTransferCodes.add((ControlTransfer) code);
                    controlTransferCodeOffsetMap.put((ControlTransfer) code, offset);
                }
            }

            for (int unvisitedCodeOffset : unvisitedCodeOffsets) {
                /*
                 * 如果转移指令跳转目标代码的序号大于codeOffset，由于codeOffset处的指令会被删除
                 * 因此该跳转指令的目标代码序号需要-1
                 */
                for (ControlTransfer controlTransferCode : controlTransferCodes) {
                    int controlTransferCodeOffset = controlTransferCodeOffsetMap.get(controlTransferCode);

                    /*
                     * 这个跳转指令也不可达，跳过不处理
                     */
                    if (unvisitedCodeOffsets.contains(controlTransferCodeOffset)) {
                        continue;
                    }

                    /*
                     * 若跳转偏移量比当前不可达指令的偏移量要大，那么跳转偏移量-1
                     */
                    if (controlTransferCode.getCodeOffset() > unvisitedCodeOffset) {
                        assertFalse(controlTransferCode.getCodeOffset() == unvisitedCodeOffset);
                        controlTransferCode.setCodeOffset(controlTransferCode.getCodeOffset() - 1);
                    }
                }
            }

            /*
             * 清理不可达指令
             */
            List<ByteCode> copyByteCodes = new ArrayList<>(byteCodes);
            byteCodes.clear();

            for (int i = 0; i < copyByteCodes.size(); i++) {
                if (!unvisitedCodeOffsets.contains(i)) {
                    byteCodes.add(copyByteCodes.get(i));
                }
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

    public static void main(String[] args) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("/Users/hechenfeng/Desktop/hua.obj"));
            objectOutputStream.writeObject(new HuaCompiler());

            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("/Users/hechenfeng/Desktop/hua.obj"));
            HuaCompiler compiler = (HuaCompiler) objectInputStream.readObject();

            System.out.println("here");
        } catch (Exception e) {
            e.printStackTrace();
            // ignore
        }
    }
}
