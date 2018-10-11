package org.liuyehcf.compile.engine.expression.utils;

import org.liuyehcf.compile.engine.expression.ExpressionEngine;
import org.liuyehcf.compile.engine.expression.core.ExpressionException;
import org.liuyehcf.compile.engine.expression.core.function.Function;
import org.liuyehcf.compile.engine.expression.runtime.ExpressionValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hechenfeng
 * @date 2018/9/26
 */
public class FunctionUtils {
    public static Function getFunctionByName(String functionName) {
        return ExpressionEngine.getFunction(functionName);
    }

    public static ExpressionValue invoke(Function function, ExpressionValue[] args) {
        int argSize = args.length;

        try {
            final String methodName = "call";
            final Class<?> argClass = ExpressionValue.class;
            final Class<?> argArrayClass = ExpressionValue[].class;

            final Method method;
            final Object[] actualArgs;

            if (argSize == 0) {
                method = function.getClass().getMethod(methodName);
                actualArgs = args;
            } else if (argSize == 1) {
                method = function.getClass().getMethod(methodName, argClass);
                actualArgs = args;
            } else if (argSize == 2) {
                method = function.getClass().getMethod(methodName, argClass, argClass);
                actualArgs = args;
            } else if (argSize == 3) {
                method = function.getClass().getMethod(methodName, argClass, argClass, argClass);
                actualArgs = args;
            } else if (argSize == 4) {
                method = function.getClass().getMethod(methodName, argClass, argClass, argClass, argClass);
                actualArgs = args;
            } else if (argSize == 5) {
                method = function.getClass().getMethod(methodName, argClass, argClass, argClass, argClass, argClass);
                actualArgs = args;
            } else if (argSize == 6) {
                method = function.getClass().getMethod(methodName, argClass, argClass, argClass, argClass, argClass, argClass);
                actualArgs = args;
            } else if (argSize == 7) {
                method = function.getClass().getMethod(methodName, argClass, argClass, argClass, argClass, argClass, argClass, argClass);
                actualArgs = args;
            } else if (argSize == 8) {
                method = function.getClass().getMethod(methodName, argClass, argClass, argClass, argClass, argClass, argClass, argClass, argClass);
                actualArgs = args;
            } else if (argSize == 9) {
                method = function.getClass().getMethod(methodName, argClass, argClass, argClass, argClass, argClass, argClass, argClass, argClass, argClass);
                actualArgs = args;
            } else if (argSize == 10) {
                method = function.getClass().getMethod(methodName, argClass, argClass, argClass, argClass, argClass, argClass, argClass, argClass, argClass, argClass);
                actualArgs = args;
            } else {
                method = function.getClass().getMethod(methodName, argClass, argClass, argClass, argClass, argClass, argClass, argClass, argClass, argClass, argClass, argArrayClass);
                actualArgs = new Object[11];
                System.arraycopy(args, 0, actualArgs, 0, 10);

                final int varargSize = argSize - 10;
                ExpressionValue[] varargs = new ExpressionValue[varargSize];
                System.arraycopy(args, 10, varargs, 0, varargSize);

                actualArgs[10] = varargs;
            }

            method.setAccessible(true);

            return (ExpressionValue) method.invoke(function, actualArgs);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ExpressionException) {
                throw (ExpressionException) cause;
            }
            throw new ExpressionException("unexpected function invocation error", e);
        } catch (Throwable e) {
            throw new ExpressionException("unexpected function invocation error", e);
        }
    }
}
