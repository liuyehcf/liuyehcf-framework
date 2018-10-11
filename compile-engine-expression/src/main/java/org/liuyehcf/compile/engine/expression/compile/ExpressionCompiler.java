package org.liuyehcf.compile.engine.expression.compile;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;
import org.liuyehcf.compile.engine.core.CompileResult;
import org.liuyehcf.compile.engine.core.cfg.lr.Context;
import org.liuyehcf.compile.engine.core.cfg.lr.LALR;
import org.liuyehcf.compile.engine.core.grammar.definition.SemanticAction;
import org.liuyehcf.compile.engine.core.utils.AssertUtils;
import org.liuyehcf.compile.engine.expression.compile.optimize.OptimizerPipeline;
import org.liuyehcf.compile.engine.expression.compile.optimize.impl.ConstantExpressionOptimizer;
import org.liuyehcf.compile.engine.expression.compile.optimize.impl.ControlTransferOptimizer;
import org.liuyehcf.compile.engine.expression.core.ExpressionCode;
import org.liuyehcf.compile.engine.expression.core.ExpressionException;
import org.liuyehcf.compile.engine.expression.utils.OptionUtils;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;

import static org.liuyehcf.compile.engine.expression.compile.definition.GrammarDefinition.GRAMMAR;
import static org.liuyehcf.compile.engine.expression.compile.definition.GrammarDefinition.LEXICAL_ANALYZER;

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
            AssertUtils.assertNotNull(resourceStream);
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
