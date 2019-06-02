package com.github.liuyehcf.framework.compile.engine.test.example.calculator.semantic;

import com.github.liuyehcf.framework.compile.engine.test.example.calculator.CompilerContext;
import com.github.liuyehcf.framework.compile.engine.test.example.calculator.code.Compute;

public class AddComputeCode extends AbstractSemanticAction {

    private final Compute code;

    public AddComputeCode(Compute code) {
        this.code = code;
    }

    @Override
    public void onAction(CompilerContext context) {
        context.addCode(code);
    }
}
