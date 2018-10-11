package org.liuyehcf.compile.engine.expression.core.function;

import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public abstract class VarargsFunction extends Function {
    public abstract ExpressionValue variadicCall(ExpressionValue... args);

    @Override
    public ExpressionValue call() {
        return this.variadicCall();
    }

    @Override
    public ExpressionValue call(ExpressionValue arg) {
        return this.variadicCall(arg);
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        return this.variadicCall(arg1, arg2);
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3) {
        return this.variadicCall(arg1, arg2, arg3);
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4) {
        return this.variadicCall(arg1, arg2, arg3, arg4);
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5) {
        return this.variadicCall(arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5, ExpressionValue arg6) {
        return this.variadicCall(arg1, arg2, arg3, arg4, arg5, arg6);
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5, ExpressionValue arg6, ExpressionValue arg7) {
        return this.variadicCall(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5, ExpressionValue arg6, ExpressionValue arg7, ExpressionValue arg8) {
        return this.variadicCall(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5, ExpressionValue arg6, ExpressionValue arg7, ExpressionValue arg8, ExpressionValue arg9) {
        return this.variadicCall(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5, ExpressionValue arg6, ExpressionValue arg7, ExpressionValue arg8, ExpressionValue arg9, ExpressionValue arg10) {
        return this.variadicCall(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
    }

    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2, ExpressionValue arg3, ExpressionValue arg4, ExpressionValue arg5, ExpressionValue arg6, ExpressionValue arg7, ExpressionValue arg8, ExpressionValue arg9, ExpressionValue arg10, ExpressionValue... args) {
        if (args == null || args.length == 0) {
            return this.variadicCall(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
        }

        ExpressionValue[] allArgs = new ExpressionValue[10 + args.length];
        allArgs[0] = arg1;
        allArgs[1] = arg2;
        allArgs[2] = arg3;
        allArgs[3] = arg4;
        allArgs[4] = arg5;
        allArgs[5] = arg6;
        allArgs[6] = arg7;
        allArgs[7] = arg8;
        allArgs[8] = arg9;
        allArgs[9] = arg10;
        System.arraycopy(args, 0, allArgs, 10, args.length);

        return this.variadicCall(allArgs);
    }
}
