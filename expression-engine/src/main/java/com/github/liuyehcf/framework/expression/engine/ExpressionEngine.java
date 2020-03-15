package com.github.liuyehcf.framework.expression.engine;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.expression.engine.compile.ExpressionCompiler;
import com.github.liuyehcf.framework.expression.engine.core.ExpressionCode;
import com.github.liuyehcf.framework.expression.engine.core.ExpressionException;
import com.github.liuyehcf.framework.expression.engine.core.function.DelegateOperatorFunction;
import com.github.liuyehcf.framework.expression.engine.core.function.Function;
import com.github.liuyehcf.framework.expression.engine.core.function.OperatorFunction;
import com.github.liuyehcf.framework.expression.engine.core.function.collection.CollectionIncludeFunction;
import com.github.liuyehcf.framework.expression.engine.core.function.collection.CollectionSizeFunction;
import com.github.liuyehcf.framework.expression.engine.core.function.date.DateTimestampFunction;
import com.github.liuyehcf.framework.expression.engine.core.function.math.*;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.add.AddOperatorFunctionForDouble;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.add.AddOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.add.AddOperatorFunctionForString;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.bitand.BitAndOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.bitor.BitOrOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.bitxor.BitXorOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.cmp.CmpOperatorFunctionForComparableObject;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.cmp.CmpOperatorFunctionForDouble;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.cmp.CmpOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.cmp.CmpOperatorFunctionForString;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.div.DivOperatorFunctionForDouble;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.div.DivOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.mul.MulOperatorFunctionForDouble;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.mul.MulOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.neg.NegOperatorFunctionForDouble;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.neg.NegOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.rem.RemOperatorFunctionForDouble;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.rem.RemOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.shl.ShlOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.shr.ShrOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.sub.SubOperatorFunctionForDouble;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.sub.SubOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.operator.ushr.UshrOperatorFunctionForLong;
import com.github.liuyehcf.framework.expression.engine.core.function.string.*;
import com.github.liuyehcf.framework.expression.engine.core.model.OperatorType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;

/**
 * @author hechenfeng
 * @date 2018/9/26
 */
public abstract class ExpressionEngine {

    /**
     * Expression Compiler
     */
    private static final ExpressionCompiler EXPRESSION_COMPILER = ExpressionCompiler.getInstance();

    /**
     * Normal Function Map
     */
    private static final Map<String, Function> NORMAL_FUNCTIONS = Maps.newHashMap();

    /**
     * Operator Function Map
     */
    private static final Map<OperatorType, List<OperatorFunction>> OPERATOR_FUNCTIONS = Maps.newHashMap();

    /**
     * Expression Engine Option Map
     */
    private static final Map<Option, Boolean> OPTIONS = Maps.newHashMap();

    static {
        loadLib();
        initOption();
    }

    private static void loadLib() {
        // load collection functions
        addFunction(new CollectionIncludeFunction());
        addFunction(new CollectionSizeFunction());

        // load time functions
        addFunction(new DateTimestampFunction());

        // load math functions
        addFunction(new MathAbsFunction());
        addFunction(new MathCosFunction());
        addFunction(new MathLog10Function());
        addFunction(new MathLogFunction());
        addFunction(new MathMaxFunction());
        addFunction(new MathMinFunction());
        addFunction(new MathPowFunction());
        addFunction(new MathRandDoubleFunction());
        addFunction(new MathRandLongFunction());
        addFunction(new MathSinFunction());
        addFunction(new MathSqrtFunction());
        addFunction(new MathTanFunction());

        // load string functions
        addFunction(new StringEndsWithFunction());
        addFunction(new StringIndexOfFunction());
        addFunction(new StringJoinFunction());
        addFunction(new StringLengthFunction());
        addFunction(new StringReplaceAllFunction());
        addFunction(new StringReplaceFirstFunction());
        addFunction(new StringSplitFunction());
        addFunction(new StringStartsWithFunction());
        addFunction(new StringSubStringFunction());

        for (OperatorType value : OperatorType.values()) {
            OPERATOR_FUNCTIONS.put(value, Lists.newArrayList());
        }

        // load add operator functions
        addOperatorFunction(new AddOperatorFunctionForLong());
        addOperatorFunction(new AddOperatorFunctionForDouble());
        addOperatorFunction(new AddOperatorFunctionForString());

        // load bit and operator functions
        addOperatorFunction(new BitAndOperatorFunctionForLong());

        // load bit or operator functions
        addOperatorFunction(new BitOrOperatorFunctionForLong());

        // load bit xor operator functions
        addOperatorFunction(new BitXorOperatorFunctionForLong());

        // load cmp operator functions
        addOperatorFunction(new CmpOperatorFunctionForComparableObject());
        addOperatorFunction(new CmpOperatorFunctionForLong());
        addOperatorFunction(new CmpOperatorFunctionForDouble());
        addOperatorFunction(new CmpOperatorFunctionForString());

        // load div operator functions
        addOperatorFunction(new DivOperatorFunctionForLong());
        addOperatorFunction(new DivOperatorFunctionForDouble());

        // load mul operator functions
        addOperatorFunction(new MulOperatorFunctionForLong());
        addOperatorFunction(new MulOperatorFunctionForDouble());

        // load neg operator functions
        addOperatorFunction(new NegOperatorFunctionForLong());
        addOperatorFunction(new NegOperatorFunctionForDouble());

        // load rem operator functions
        addOperatorFunction(new RemOperatorFunctionForLong());
        addOperatorFunction(new RemOperatorFunctionForDouble());

        // load shl operator functions
        addOperatorFunction(new ShlOperatorFunctionForLong());

        // load shr operator functions
        addOperatorFunction(new ShrOperatorFunctionForLong());

        // load sub operator functions
        addOperatorFunction(new SubOperatorFunctionForLong());
        addOperatorFunction(new SubOperatorFunctionForDouble());

        // LOAD ushr operator functions
        addOperatorFunction(new UshrOperatorFunctionForLong());
    }

    private static void initOption() {
        for (Option value : Option.values()) {
            OPTIONS.put(value, true);
        }
    }

    /**
     * set expression engine option
     *
     * @param option option
     * @param status status
     */
    public synchronized static void setOption(Option option, boolean status) {
        OPTIONS.put(option, status);
    }

    /**
     * get status of specified option
     *
     * @param option option
     * @return status of option
     */
    public static boolean getOption(Option option) {
        return OPTIONS.get(option);
    }

    /**
     * return all expression engine options
     *
     * @return options
     */
    public static Map<Option, Boolean> getOptions() {
        return OPTIONS;
    }

    /**
     * add normal function
     *
     * @param function function
     */
    public synchronized static void addFunction(Function function) {
        String functionName = function.getName();
        if (NORMAL_FUNCTIONS.containsKey(functionName)) {
            throw new ExpressionException("duplicate function '" + functionName + "'");
        }

        NORMAL_FUNCTIONS.put(functionName, function);
    }

    /**
     * remove normal function
     *
     * @param functionName function name
     * @return the function removed
     */
    public synchronized static Function removeFunction(String functionName) {
        return NORMAL_FUNCTIONS.remove(functionName);
    }

    /**
     * ge normal function by name
     *
     * @param functionName function name
     * @return normal function
     */
    public static Function getFunction(String functionName) {
        return NORMAL_FUNCTIONS.get(functionName);
    }

    /**
     * clean default operator functions of specified type
     *
     * @param type operator type
     */
    public synchronized static void cleanOperatorFunctions(OperatorType type) {
        OPERATOR_FUNCTIONS.get(type).clear();
    }

    /**
     * clean default operator functions of all type
     */
    public synchronized static void cleanOperatorFunctions() {
        for (OperatorType type : OperatorType.values()) {
            cleanOperatorFunctions(type);
        }
    }

    /**
     * add operator function
     *
     * @param function operator function
     * @return id of added operator function of related type
     */
    public synchronized static String addOperatorFunction(OperatorFunction function) {
        Assert.assertNotNull(function);
        OperatorType type = function.getType();
        String id = UUID.randomUUID().toString();
        OPERATOR_FUNCTIONS.get(type).add(new DelegateOperatorFunction(id, function));
        Collections.sort(OPERATOR_FUNCTIONS.get(type));
        return id;
    }

    /**
     * remove operator function
     *
     * @param type operator type
     * @param id   operator function id
     * @return operator function removed
     */
    public synchronized static Function removeOperatorFunction(OperatorType type, String id) {
        Iterator<OperatorFunction> iterator = OPERATOR_FUNCTIONS.get(type).iterator();
        while (iterator.hasNext()) {
            DelegateOperatorFunction next = (DelegateOperatorFunction) iterator.next();
            if (Objects.equals(next.getId(), id)) {
                iterator.remove();
                return next;
            }
        }
        return null;
    }

    /**
     * get operator functions byte operator type
     *
     * @param type operator type
     * @return operator function list
     */
    public static List<OperatorFunction> getOperatorFunctions(OperatorType type) {
        return OPERATOR_FUNCTIONS.get(type);
    }

    /**
     * compile expression
     *
     * @param expression expression
     * @return expression compiled code
     */
    public static ExpressionCode compile(String expression) {
        return EXPRESSION_COMPILER.parse(expression);
    }

    /**
     * execute expression with env
     *
     * @param expression expression
     * @param env        env
     * @param <T>        return type
     * @return execution result
     */
    public static <T> T execute(String expression, Map<String, Object> env) {
        return execute(compile(expression), env);
    }

    /**
     * execute expression without env
     *
     * @param expression expression
     * @param <T>        return type
     * @return execution result
     */
    public static <T> T execute(String expression) {
        return execute(expression, null);
    }

    /**
     * execute expression code with env
     *
     * @param expressionCode expression code
     * @param env            env
     * @param <T>            return type
     * @return execution result
     */
    public static <T> T execute(ExpressionCode expressionCode, Map<String, Object> env) {
        return expressionCode.execute(env);
    }

    /**
     * execute expression code without env
     *
     * @param expressionCode expression code
     * @param <T>            return type
     * @return execution result
     */
    public static <T> T execute(ExpressionCode expressionCode) {
        return execute(expressionCode, null);
    }

    /**
     * execute expression with properties
     *
     * @param expression expression
     * @param properties properties
     * @param <T>        return type
     * @return execute result
     */
    public static <T> T exec(String expression, Object... properties) {
        return exec(compile(expression), properties);
    }

    /**
     * execute expression code with properties
     *
     * @param expressionCode expression code
     * @param properties     properties
     * @param <T>            return type
     * @return execute result
     */
    public static <T> T exec(ExpressionCode expressionCode, Object... properties) {
        return expressionCode.exec(properties);
    }
}
