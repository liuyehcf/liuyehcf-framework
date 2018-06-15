package org.liuyehcf.compile.engine.hua.compiler;

import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.lr.LALR;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.grammar.definition.SemanticAction;
import org.liuyehcf.compile.engine.hua.semantic.AbstractSemanticAction;

import java.util.List;

/**
 * @author chenlu
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

    public static final class HuaContext extends Context {

        private final HuaEngine huaEngine;

        public HuaContext(Context context, HuaEngine huaEngine) {
            super(context.getRawPrimaryProduction(), context.getStack(), context.getLeftNode());
            this.huaEngine = huaEngine;
        }

        public HuaEngine getHuaEngine() {
            return huaEngine;
        }
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
            this.result = new HuaResult(variableSymbolTable, methodInfoTable);
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
