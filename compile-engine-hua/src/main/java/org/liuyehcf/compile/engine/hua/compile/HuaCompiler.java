package org.liuyehcf.compile.engine.hua.compile;

import org.liuyehcf.compile.engine.core.cfg.lr.Context;
import org.liuyehcf.compile.engine.core.cfg.lr.LALR;
import org.liuyehcf.compile.engine.core.grammar.definition.SemanticAction;
import org.liuyehcf.compile.engine.core.utils.Pair;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.*;
import org.liuyehcf.compile.engine.hua.core.bytecode.ByteCode;
import org.liuyehcf.compile.engine.hua.core.bytecode.cf.ControlTransfer;
import org.liuyehcf.compile.engine.hua.core.bytecode.cf._goto;

import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;

import static org.liuyehcf.compile.engine.hua.compile.definition.GrammarDefinition.GRAMMAR;
import static org.liuyehcf.compile.engine.hua.compile.definition.GrammarDefinition.LEXICAL_ANALYZER;

/**
 * Hua编译器
 *
 * @author hechenfeng
 * @date 2018/6/2
 */
public class HuaCompiler extends LALR<IntermediateInfo> implements Serializable {

    /**
     * 环境变量名
     */
    public static final String HUA_SERIALIZATION_FILE = "HUA_COMPILER";

    private HuaCompiler() {
        super(GRAMMAR, LEXICAL_ANALYZER);
    }

    public static HuaCompiler getHuaCompiler() {
        HuaCompiler huaCompiler;
        File compilerFile = null;

        /*
         * 首先从序列化文件中加载编译器
         */
        try {
            InputStream resourceStream = ClassLoader.getSystemClassLoader().getResourceAsStream(HUA_SERIALIZATION_FILE);
            if (resourceStream == null) {
                throw new NullPointerException();
            }
            ObjectInputStream inputStream = new ObjectInputStream(resourceStream);
            huaCompiler = (HuaCompiler) inputStream.readObject();
            return huaCompiler;
        } catch (Throwable e) {
            // ignore
        }

        /*
         * 重新构建编译器
         */
        huaCompiler = new HuaCompiler();

        return huaCompiler;
    }

    @Override
    protected Engine createCompiler(String input) {
        return new HuaEngine(input);
    }

    public class HuaEngine extends Engine {

        /**
         * 常量池
         */
        private ConstantPool constantPool = new ConstantPool();

        /**
         * 方法定义表
         */
        private MethodInfoTable methodInfoTable = new MethodInfoTable();

        HuaEngine(String input) {
            super(input);
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
            setResult(new IntermediateInfo(constantPool, methodInfoTable));
        }

        private void optimize() {
            Map<MethodSignature, MethodInfo> table = methodInfoTable.getTable();

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

            Set<ByteCode> visitedCodes = new HashSet<>();

            ByteCode code;
            while ((code = byteCodes.get(optimizedCodeOffset)) instanceof _goto) {
                /*
                 * 对于for(;;){}这样的循环，只有一个goto语句，且回到原点
                 * 跳转链路的终点是一个goto self
                 */
                if (!visitedCodes.add(code)) {
                    return optimizedCodeOffset;
                }
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
            Map<ControlTransfer, Pair<Integer, Integer>> controlTransferCodeOffsetMap = new HashMap<>();
            for (int offset = 0; offset < byteCodes.size(); offset++) {
                ByteCode code = byteCodes.get(offset);
                if (code instanceof ControlTransfer) {
                    controlTransferCodes.add((ControlTransfer) code);
                    controlTransferCodeOffsetMap.put((ControlTransfer) code, new Pair<>(offset, ((ControlTransfer) code).getCodeOffset()));
                }
            }

            for (int unvisitedCodeOffset : unvisitedCodeOffsets) {
                /*
                 * 如果转移指令跳转目标代码的序号大于codeOffset，由于codeOffset处的指令会被删除
                 * 因此该跳转指令的目标代码序号需要-1
                 */
                for (ControlTransfer controlTransferCode : controlTransferCodes) {
                    Pair<Integer, Integer> pair = controlTransferCodeOffsetMap.get(controlTransferCode);
                    int offset = pair.getFirst();
                    int toOffset = pair.getSecond();

                    /*
                     * 这个跳转指令也不可达，跳过不处理
                     */
                    if (unvisitedCodeOffsets.contains(offset)) {
                        continue;
                    }

                    /*
                     * 若跳转偏移量比当前不可达指令的偏移量要大，那么跳转偏移量-1
                     */
                    if (toOffset > unvisitedCodeOffset) {
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
                ((AbstractSemanticAction) semanticAction).onAction(new CompilerContext(context, constantPool, methodInfoTable));
            }
        }
    }
}
