package com.github.liuyehcf.framework.compile.engine.test.example.calculator.semantic;

import com.github.liuyehcf.framework.compile.engine.test.example.calculator.CompilerContext;
import com.github.liuyehcf.framework.compile.engine.test.example.calculator.code.Load;

import java.math.BigInteger;

public class AddLoad extends AbstractSemanticAction {

    private static final int LITERAL_STACK_OFFSET = 0;

    @Override
    public void onAction(CompilerContext context) {
        String literal = context.getStack().get(LITERAL_STACK_OFFSET).getValue();

        char firstChar = literal.charAt(0);
        BigInteger bigInteger;
        switch (firstChar) {
            case '1':
                if (literal.endsWith("l") || literal.endsWith("L")) {
                    bigInteger = new BigInteger(literal.substring(1, literal.length() - 1), 10);
                    if (bigInteger.bitLength() > 63) {
                        throw new IllegalArgumentException("illegal integer literal='" + literal + "'");
                    }
                    context.addCode(new Load(bigInteger.longValue()));
                    return;
                }
                bigInteger = new BigInteger(literal.substring(1), 10);
                break;
            case '2':
                bigInteger = new BigInteger(literal.substring(3), 16);
                break;
            case '3':
                bigInteger = new BigInteger(literal.substring(2), 8);
                break;
            default:
                throw new IllegalArgumentException("lexer parsing integer literal error");
        }
        if (bigInteger.bitLength() < 64) {
            context.addCode(new Load(bigInteger.longValue()));
        } else {
            throw new IllegalArgumentException("illegal integer literal='" + literal + "'");
        }
    }
}
