package com.github.liuyehcf.framework.compile.engine.test.example.calculator.test;

import com.github.liuyehcf.framework.compile.engine.test.example.calculator.CalculatorCompiler;

public class TestCalculator {
    public static void main(String[] args) {
        System.out.println(CalculatorCompiler.execute("1+2+3"));
        System.out.println(CalculatorCompiler.execute("1+2*3"));
        System.out.println(CalculatorCompiler.execute("1+2/3"));
        System.out.println(CalculatorCompiler.execute("(1+2)/3"));
    }
}
