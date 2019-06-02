package com.github.liuyehcf.framework.compile.engine.test.example.calculator;

import com.github.liuyehcf.framework.compile.engine.CompileResult;
import com.github.liuyehcf.framework.compile.engine.cfg.lr.Context;
import com.github.liuyehcf.framework.compile.engine.cfg.lr.LALR;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Grammar;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.SemanticAction;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;
import com.github.liuyehcf.framework.compile.engine.test.example.calculator.semantic.AbstractSemanticAction;

import java.util.List;

import static com.github.liuyehcf.framework.compile.engine.test.example.calculator.CalculatorGrammar.*;

public class CalculatorCompiler extends LALR<CalculatorCode> {

    private static final CalculatorCompiler INSTANCE = new CalculatorCompiler();

    private CalculatorCompiler() {
        super(Grammar.create(Symbol.createNonTerminator(PROGRAM), PRODUCTIONS), LEXICAL_ANALYZER);
    }

    public static long execute(String input) {
        CompileResult<CalculatorCode> compile = INSTANCE.compile(input);
        if (!compile.isSuccess()) {
            throw new RuntimeException(compile.getError());
        }

        return compile.getResult().execute();
    }

    @Override
    protected Engine createCompiler(String input) {
        return new CalculatorEngine(input);
    }

    protected class CalculatorEngine extends Engine {

        private CalculatorCode calculatorCode = new CalculatorCode();

        private CalculatorEngine(String input) {
            super(input);
        }

        @Override
        protected void before() {
            super.before();
        }

        @Override
        protected void after() {
            setResult(calculatorCode);
        }

        @Override
        protected void onReduction(Context context) {
            List<SemanticAction> semanticActions = context.getRawPrimaryProduction().getSemanticActions();
            if (semanticActions == null) {
                return;
            }

            for (SemanticAction semanticAction : semanticActions) {
                AbstractSemanticAction abstractSemanticAction = (AbstractSemanticAction) semanticAction;
                abstractSemanticAction.onAction(new CompilerContext(context, calculatorCode));
            }
        }
    }
}
