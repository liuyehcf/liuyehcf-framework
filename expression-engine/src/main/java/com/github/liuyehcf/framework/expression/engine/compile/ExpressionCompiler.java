package com.github.liuyehcf.framework.expression.engine.compile;

import com.github.liuyehcf.framework.compile.engine.CompileResult;
import com.github.liuyehcf.framework.compile.engine.cfg.lr.Context;
import com.github.liuyehcf.framework.compile.engine.cfg.lr.LALR;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.SemanticAction;
import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.expression.engine.compile.optimize.OptimizerPipeline;
import com.github.liuyehcf.framework.expression.engine.compile.optimize.impl.ConstantExpressionOptimizer;
import com.github.liuyehcf.framework.expression.engine.compile.optimize.impl.ControlTransferOptimizer;
import com.github.liuyehcf.framework.expression.engine.core.ExpressionCode;
import com.github.liuyehcf.framework.expression.engine.core.ExpressionException;
import com.github.liuyehcf.framework.expression.engine.utils.OptionUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;

import static com.github.liuyehcf.framework.expression.engine.compile.definition.GrammarDefinition.GRAMMAR;
import static com.github.liuyehcf.framework.expression.engine.compile.definition.GrammarDefinition.LEXICAL_ANALYZER;

/**
 * Expression编译器
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class ExpressionCompiler extends LALR<ExpressionCode> {

    static final String COMPILER_SERIALIZATION_FILE = "EXPRESSION_COMPILER";
    private static final ExpressionCompiler INSTANCE = loadCompiler();
    private final Cache<String, ExpressionCode> byteCodeCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build();
    private final OptimizerPipeline optimizerPipeline = OptimizerPipeline.builder()
            .addOptimizer(new ConstantExpressionOptimizer())
            .addOptimizer(new ControlTransferOptimizer())
            .build();

    private ExpressionCompiler() {
        super(GRAMMAR, LEXICAL_ANALYZER);
    }

    private static ExpressionCompiler loadCompiler() {
        try {
            InputStream resourceStream = ClassLoader.getSystemClassLoader().getResourceAsStream(COMPILER_SERIALIZATION_FILE);
            Assert.assertNotNull(resourceStream);
            ObjectInputStream inputStream = new ObjectInputStream(resourceStream);
            return (ExpressionCompiler) inputStream.readObject();
        } catch (Throwable e) {
            //ignore
        }
        return new ExpressionCompiler();
    }

    public static ExpressionCompiler getInstance() {
        return INSTANCE;
    }

    public final ExpressionCode parse(String expression) {
        if (OptionUtils.isCache()) {
            try {
                return byteCodeCache.get(expression, () -> doParse(expression));
            } catch (Throwable e) {
                if (e instanceof UncheckedExecutionException) {
                    Throwable cause = e.getCause();
                    if (cause instanceof ExpressionException) {
                        throw (ExpressionException) cause;
                    }
                }
                throw new ExpressionException("unexpected cache error", e);
            }
        }

        return doParse(expression);
    }

    private ExpressionCode doParse(String expression) {
        CompileResult<ExpressionCode> compile = compile(expression);
        if (!compile.isSuccess()) {
            throw new ExpressionException(compile.getError().getMessage(), compile.getError());
        }
        return compile.getResult();
    }

    @Override
    protected Engine createCompiler(String input) {
        return new HuaEngine(input);
    }

    public class HuaEngine extends Engine {

        /**
         * 字节码
         */
        private ExpressionCode expressionCode = new ExpressionCode();

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
            setResult(expressionCode);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void onReduction(Context context) {
            List<SemanticAction> semanticActions = context.getRawPrimaryProduction().getSemanticActions();
            if (semanticActions == null) {
                return;
            }

            for (SemanticAction semanticAction : semanticActions) {
                semanticAction.onAction(new CompilerContext(context, expressionCode));
            }
        }

        private void optimize() {
            if (!OptionUtils.isOptimize()) {
                return;
            }
            optimizerPipeline.optimize(expressionCode);
        }
    }
}
