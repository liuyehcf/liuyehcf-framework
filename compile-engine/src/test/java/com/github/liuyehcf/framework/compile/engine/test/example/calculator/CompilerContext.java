package com.github.liuyehcf.framework.compile.engine.test.example.calculator;

import com.github.liuyehcf.framework.compile.engine.cfg.lr.Context;
import com.github.liuyehcf.framework.compile.engine.test.example.calculator.code.Code;

public class CompilerContext extends Context {

    private final CalculatorCode calculatorCode;

    CompilerContext(Context context, CalculatorCode calculatorCode) {
        super(context.getRawPrimaryProduction(), context.getStack(), context.getLeftNode());
        this.calculatorCode = calculatorCode;
    }

    public void addCode(Code code) {
        calculatorCode.addCode(code);
    }

}
