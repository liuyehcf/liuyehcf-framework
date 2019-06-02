package com.github.liuyehcf.framework.compile.engine.test.example.calculator;

import com.github.liuyehcf.framework.compile.engine.test.example.calculator.code.Code;
import com.github.liuyehcf.framework.compile.engine.test.example.calculator.code.Compute;
import com.github.liuyehcf.framework.compile.engine.test.example.calculator.code.Load;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CalculatorCode {

    private final List<Code> codes = new ArrayList<>();

    public void addCode(Code code) {
        codes.add(code);
    }

    public long execute() {
        LinkedList<Long> stack = new LinkedList<>();

        for (Code code : codes) {
            if (code instanceof Load) {
                long value = ((Load) code).getValue();

                stack.push(value);
            } else if (code instanceof Compute) {
                long val2 = stack.pop();
                long val1 = stack.pop();
                long res = ((Compute) code).execute(val1, val2);

                stack.push(res);
            }
        }

        return stack.pop();
    }
}
