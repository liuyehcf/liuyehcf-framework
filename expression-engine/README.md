* [1 Introduction](#1-introduction)
* [2 Maven Dependency](#2-maven-dependency)
* [3 User Manual](#3-user-manual)
   * [3.1 Executing Expressions](#31-executing-expressions)
   * [3.2 Using Variables](#32-using-variables)
   * [3.3 Function Invocation](#33-function-invocation)
   * [3.4 Custom Functions](#34-custom-functions)
   * [3.5 Operator Overloading](#35-operator-overloading)
   * [3.6 Ternary Operator](#36-ternary-operator)
   * [3.7 Array Initialization Expression](#37-array-initialization-expression)
   * [3.8 Accessing Arrays and Collections](#38-accessing-arrays-and-collections)
   * [3.9 Compiling Expressions](#39-compiling-expressions)
   * [3.10 Code Optimization](#310-code-optimization)
* [4 Grammar Manual](#4-grammar-manual)
   * [4.1 Data Types](#41-data-types)
   * [4.2 Operators](#42-operators)
      * [4.2.1 Arithmetic Operators](#421-arithmetic-operators)
      * [4.2.2 Logical Operators](#422-logical-operators)
      * [4.2.3 Relational Operators](#423-relational-operators)
      * [4.2.4 Bitwise Operators](#424-bitwise-operators)
      * [4.2.5 Ternary Operator](#425-ternary-operator)
   * [4.3 Built-in Functions](#43-built-in-functions)

# 1 Introduction

`ExpressionEngine` is a high-performance, lightweight expression evaluation engine implemented in Java. It is mainly used for dynamic evaluation of various expressions. There are already many open-source Java expression evaluation engines available, so why do we need `ExpressionEngine`?

The design goal of `ExpressionEngine` is lightweight and high performance. Compared to heavyweight options like `Groovy` and `JRuby`, `ExpressionEngine` is very small, less than `100K` if you don't count the dependency packages. However, the syntax of `ExpressionEngine` is limited. It is not a complete language but rather a small subset of a language.

Furthermore, the implementation approach of `ExpressionEngine` is different from other lightweight evaluators. Other evaluators generally run expressions through interpretation, while `ExpressionEngine` directly compiles expressions into bytecode (not Java bytecode, but a custom bytecode system that is comparable to Java bytecode). In simple terms, `ExpressionEngine` is positioned between heavyweight scripting languages like `Groovy` and lightweight expression engines like `IKExpression`.

**Features of ExpressionEngine:**

1. Supports most operators, including arithmetic, relational, logical, bitwise operators, and the ternary expression `?:`.
2. Short-circuit evaluation is supported for logical operators.
3. Supports various literal value types, such as `null`, `long`, `double`, `String`, arrays, etc.
4. Supports automatic type conversion. For example, when executing the expression `1L+1.0D`, it automatically promotes `1L` to `long`, then performs `double+double`, and returns the result of type `double`.
5. Includes a powerful built-in function library.
6. Allows custom functions for easy extensibility.
7. Supports operator overloading (the meaning of overloading in `ExpressionEngine` is different from general-purpose languages; it will be explained in detail later).
8. Excellent performance.

# 2 Maven Dependency

Latest version: `1.0.2`

```xml
<dependency>
    <groupId>com.github.liuyehcf</groupId>
    <artifactId>expression-engine</artifactId>
    <version>${version}</version>
</dependency>
```

# 3 User Manual

## 3.1 Executing Expressions

The usage of `ExpressionEngine` is primarily done through the entry class `com.github.liuyehcf.framework.expression.engine.ExpressionEngine`. The simplest example is executing an expression that calculates `1+2+3`.

```java
    public static void main(String[] args) {
        long result = ExpressionEngine.execute("1+2+3");
        System.out.println(result); // 6
    }
```

Observant readers may notice that the result is `Long` instead of `Integer`. This is because the numeric types supported by `ExpressionEngine` are `Long` and `Double`. Any integer will be converted to `Long`, and any floating-point number will be converted to `Double`, including the variable values passed by the user. The result of this example will be the correct answer, `6`.

## 3.2 Using Variables

Want `ExpressionEngine` to say hello to you? It's simple. Pass in your name and let `ExpressionEngine` handle the string concatenation.

```java
    public static void main(String[] args) {
        String yourName = "liuye";
        Map<String, Object> env = new HashMap<>();
        env.put("yourName", yourName);
        String result = ExpressionEngine.execute(" 'hello ' + yourName ", env);
        System.out.println(result);  // hello liuye
    }
```

The above example demonstrates how to pass variable values to an expression. The variable `yourName` in the expression is a variable that defaults to null. By passing a variable binding environment of type `Map<String, Object>`, you can set `yourName` to the name you input. The `key` in the `env` is the variable name, and the `value` is the variable's value.

The `'hello '` in the example is an `ExpressionEngine` String. An `ExpressionEngine` String is a sequence of characters enclosed in either `double quotes` or `single quotes`. A String can be concatenated with any object, and the result of concatenating any object with a String is a String. The String can also contain escape characters, such as `\n`, `\\`, `\"`, etc.

```java
    public static void main(String[] args) {
        System.out.println((String) ExpressionEngine.execute(" 'a\"b' "));           // 字符串 a"b
        System.out.println((String) ExpressionEngine.execute(" \"a\'b\" "));         // 字符串 a'b
        System.out.println((String) ExpressionEngine.execute(" 'hello ' + 3 "));     // 字符串 hello 3
        System.out.println((String) ExpressionEngine.execute(" 'hello '+ unknow ")); // 字符串 hello null
    }
```

## 3.3 Function Invocation

`ExpressionEngine` supports function invocation, similar to `Lua`. The following example retrieves the length of a string.

```java
    public static void main(String[] args) {
        System.out.println((long) ExpressionEngine.execute("string.length('hello')"));  // 5
    }
```

`string.length('hello')` is a function invocation. `string.length` is a function, and `'hello'` is the argument passed to the function.

## 3.4 Custom Functions

In addition to the built-in functions, `ExpressionEngine` allows users to define custom functions. You just need to implement the `com.github.liuyehcf.framework.expression.engine.core.function.Function` interface and register it with `ExpressionEngine` to use it. The Function interface is extensive, but in most cases, you don't need to implement all the methods. You only need to inherit the Function class and override the corresponding methods based on the number of parameters in your function.

Here's an example. We'll implement an `add` function to perform addition of numbers.

```java
public class TestExpressionEngine {
    public static void main(String[] args) {
        //注册函数
        ExpressionEngine.addFunction(new AddFunction());
        System.out.println((double)ExpressionEngine.execute("add(1, 2)"));           // 3.0
        System.out.println((double)ExpressionEngine.execute("add(add(1, 2), 100)")); // 103.0
    }
}

class AddFunction extends Function {
    @Override
    public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
        Number left = arg1.getValue();
        Number right = arg2.getValue();
        return ExpressionValue.valueOf(left.doubleValue() + right.doubleValue());
    }

    /**
     * 该方法返回值就是函数的名称
     */
    @Override
    public String getName() {
        return "add";
    }
}
```

**Note that all function parameters have the type `ExpressionValue`, and the return type is also `ExpressionValue`.**

Functions can be registered using the `ExpressionEngine.addFunction` method and removed using `removeFunction`.

If the number of your function parameters is uncertain, you can inherit the `com.github.liuyehcf.framework.expression.engine.core.function.VarargsFunction` class and implement the `variadicCall` method. For example, we can implement a method that concatenates all the parameters.

```java
public class TestExpressionEngine {
    public static void main(String[] args) {
        ExpressionEngine.addFunction(new Combine());
        System.out.println((String) ExpressionEngine.execute("combine(1)")); // 1
        System.out.println((String) ExpressionEngine.execute("combine(1,2,3,4,null,5)")); // 1 2 3 4 null 5
        System.out.println((String) ExpressionEngine.execute("combine(a,b,c,d)")); // null null null null
    }
}

class Combine extends VarargsFunction {

    @Override
    public ExpressionValue variadicCall(ExpressionValue... args) {
        StringBuilder sb = new StringBuilder();

        for (ExpressionValue arg : args) {
            sb.append(Objects.toString(arg.getValue())).append(' ');
        }

        return ExpressionValue.valueOf(sb.toString());
    }

    @Override
    public String getName() {
        return "combine";
    }
}
```

## 3.5 Operator Overloading

`ExpressionEngine` supports the operators listed in the Operators section. Some users may have the need to overload these built-in operators. For example, in `Excel`, `&` is not a bitwise operator but a string concatenation operator. You can achieve this by overloading operators.

```java
    public static void main(String[] args) {
        ExpressionEngine.addOperatorFunction(new BitAndOperatorFunction() {

            /**
             * 该 operator function的order，越小优先级越高
             */
            @Override
            public int getOrder() {
                return 0;
            }

            /**
             * 该方法一般根据参数类型进行过滤
             * 如果accept返回false，那么该方法将不会执行
             */
            @Override
            public boolean accept(ExpressionValue arg1, ExpressionValue arg2) {
                return arg1.getValue() instanceof String && arg2.getValue() instanceof String;
            }

            @Override
            public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
                String value1 = arg1.getValue();
                String value2 = arg2.getValue();

                return ExpressionValue.valueOf(value1 + value2);
            }
        });

        Object result = ExpressionEngine.execute("'liu' & 'ye'");
        System.out.println(result); // liuye
    }
```

Please note that improper use of operator overloading can lead to confusion.

**In `ExpressionEngine`, the behavior of operators at runtime is accomplished through a series of operator functions (the order of the functions is controlled by the return value of the `OperatorFunction.getOrder()` method; the smaller the value, the higher the priority). Therefore, the mentioned overloading is not true overloading in the general sense. It adds an additional behavior and does not override existing behaviors (in the above example, `long & long` can still be executed correctly).**

* The built-in operator functions have an order value of `Integer.MAX_VALUE or Integer.MAX_VALUE >> 1`.
* The newly added operator functions have a default order value of 0 (which can be overridden by modifying the method to return a different value), so they are placed before the built-in operator functions by default.

Taking the `&` operator as an example, let's explain the execution process of operators. You can refer to the specific code in `com.github.liuyehcf.framework.expression.engine.core.bytecode.cp._and`.

1. Retrieve all operator functions based on the operator type.
2. Retrieve the top two operands from the operand stack.
3. Iterate through all operator functions.
4. Find the first operator function that is compatible with `value1` and `value2`.
5. Execute the function and push the result onto the operand stack.
6. Increment the bytecode index, and the execution is complete.

```java
    public void operate(RuntimeContext context) {
        operateForTwoOperand(context, OperatorType.BIT_AND);
    }

    final void operateForTwoOperand(RuntimeContext context, OperatorType type) {
        // 根据运算符的类型，取出所有的运算符函数
        List<OperatorFunction> operatorFunctions = ExpressionEngine.getOperatorFunctions(type);

        // 取出栈顶的两个操作数
        ExpressionValue value2 = context.pop();
        ExpressionValue value1 = context.pop();

        // 遍历所有运算符函数
        for (OperatorFunction operatorFunction : operatorFunctions) {
            // 如果该函数能够兼容value1和value2的类型
            if (operatorFunction.accept(value1, value2)) {
                // 那么执行该方法
                ExpressionValue result = operatorFunction.call(value1, value2);

                /*
                 * 特殊处理一下Cmp，将operator function的返回值改造成 ComparableValue
                 */
                if (this instanceof _cmp) {
                    Object resultValue = result.getValue();

                    if (!(resultValue instanceof Long)) {
                        throw new ExpressionException("CmpOperatorFunction's return type must be 'java.lang.Long' (or compatible type 'java.lang.Byte', 'java.lang.Short', 'java.lang.Integer')");
                    }

                    // 方法结果入操作数栈
                    context.push(ExpressionValue.valueOf(ComparableValue.valueOf((long) resultValue)));
                } else {
                    // 方法结果入操作数栈
                    context.push(result);
                }

                // 递增字节码序号
                context.increaseCodeOffset();
                return;
            }
        }

        throw createExpressionException(type, value1, value2);
    }
```

If you want to clear all the default behaviors of an operator, you can use `ExpressionEngine.cleanOperatorFunctions` or `ExpressionEngine.cleanOperatorFunction(type)`.

**The overloaded operators allowed by `ExpressionEngine` and their corresponding base classes are as follows:**

* Unary operators:
    1. `-`: NegOperatorFunction
* Binary operators:
    1. `+`: AddOperatorFunction
    2. `-`: SubOperatorFunction
    3. `*`: MulOperatorFunction
    4. `/`: DivOperatorFunction
    5. `%`: RemOperatorFunction
    6. `&`: BitAndOperatorFunction
    7. `|`: BitOrOperatorFunction
    8. `^`: BitXorOperatorFunction
    9. `<<`: ShlOperatorFunction
    10. `>>`: ShrOperatorFunction
    11. `>>>`: UshrOperatorFunction
* Comparison operators:
    1. `<`: CmpOperatorFunction
    2. `<=`: CmpOperatorFunction
    3. `>`: CmpOperatorFunction
    4. `>=`: CmpOperatorFunction
    5. `==`: CmpOperatorFunction
    6. `!=`: CmpOperatorFunction
    * The above comparison operators share the same base class: CmpOperatorFunction

**Here's another example with comparison operators. We only need to register an OperationFunction (which inherits CmpOperatorFunction for convenience), and all comparison operators (`<`, `<=`, `>`, `>=`, `==`, `!=`) will work.**

```java
public class TestExpressionEngine {
    public static void main(String[] args) {
        ExpressionEngine.addOperatorFunction(new CmpOperatorFunction() {
            /**
             * 类型过滤
             */
            @Override
            public boolean accept(ExpressionValue arg1, ExpressionValue arg2) {
                return arg1.getValue() instanceof MyValue && arg2.getValue() instanceof MyValue;
            }

            /**
             * 返回负数表示  arg1 小于 arg2
             * 返回0表示    arg1 等于 arg2
             * 返回正数表示  arg1 大于 arg2
             */
            @Override
            public ExpressionValue call(ExpressionValue arg1, ExpressionValue arg2) {
                MyValue value1 = arg1.getValue();
                MyValue value2 = arg2.getValue();
                return ExpressionValue.valueOf(Integer.compare(value1.getValue(), value2.getValue()));
            }
        });

        Map<String, Object> env = Maps.newHashMap();
        env.put("a", new MyValue(1));
        env.put("b", new MyValue(2));
        System.out.println((boolean) ExpressionEngine.execute("a == b", env)); //false
        System.out.println((boolean) ExpressionEngine.execute("a != b", env)); //true
        System.out.println((boolean) ExpressionEngine.execute("a < b", env)); //true
        System.out.println((boolean) ExpressionEngine.execute("a <= b", env)); //true
        System.out.println((boolean) ExpressionEngine.execute("a > b", env)); //false
        System.out.println((boolean) ExpressionEngine.execute("a >= b", env)); //false
    }

    private static final class MyValue {
        private int value;

        public MyValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
```

## 3.6 Ternary Operator

`ExpressionEngine` does not currently provide an `if-else` statement (if-else support will be included when lambda expressions are introduced), but it does provide the ternary operator `?:` for conditional evaluation. Its meaning is the same as in `Java`.

```java
    public static void main(String[] args) {
        System.out.println((String)ExpressionEngine.execute("-1>0?'yes':'no'"));  // no
    }
```

## 3.7 Array Initialization Expression

`ExpressionEngine` allows you to construct an array using list initialization syntax.

```java
    public static void main(String[] args) {
        Map<String, Object> env = new HashMap<>();
        env.put("yourName", "liuye");
        Object[] array =ExpressionEngine.execute("[1L,2.0d,'hello',null,yourName]", env);
        System.out.println(Arrays.toString(array));// [1, 2.0, hello, null, liuye]
    }
```

Array initialization expressions can appear in any syntax structure, such as method parameters, ternary operators, etc.

```java
    public static void main(String[] args) {
        System.out.println((String)ExpressionEngine.execute("string.join(['a','b','c'],'#')")); // a#b#c
    }
```

The `string.join` function in the example is used to concatenate elements in a collection using the separator `#` and returns a String.

## 3.8 Accessing Arrays and Collections

You can access `arrays` and `java.util.List` objects using square brackets. You can access the `value` corresponding to the `key` in `java.util.Map` using `map.key`.

```java
    public static void main(String[] args) {
        final List<String> list = new ArrayList<>();
        list.add("hello");
        list.add(" world");
        final int[] array = new int[3];
        array[0] = 0;
        array[1] = 1;
        array[2] = 3;
        final Map<String, Date> map = new HashMap<>();
        map.put("date", new Date());
        Map<String, Object> env = new HashMap<>();
        env.put("list", list);
        env.put("array", array);
        env.put("mmap", map);
        System.out.println((Object)ExpressionEngine.execute("list[0]+list[1]", env));   // hello world
        System.out.println((Object)ExpressionEngine.execute("'array[0]+array[1]+array[2]=' + (array[0]+array[1]+array[2])", env));  // array[0]+array[1]+array[2]=4
        System.out.println((Object)ExpressionEngine.execute("'today is ' + mmap['date'] ", env));  // today is Sat Sep 29 16:46:26 CST 2018
    }
```

## 3.9 Compiling Expressions

The examples mentioned above directly execute the expressions, but in fact, `ExpressionEngine` compiles and executes the expressions for you. You can compile the expressions yourself, returning a compilation result, and then reuse the compilation result with different `env` to improve performance (compilation is extremely fast unless the expression is very large). `ExpressionEngine` will automatically cache the compiled result. For the same expression, it will first retrieve the previously compiled result from the cache, and if not found, it will perform the compilation operation.

```java
    public static void main(String[] args) {
        String expression = "a-(b-c)>100";
        // 编译表达式
        ExpressionCode code =ExpressionEngine.compile(expression);
        Map<String, Object> env = new HashMap<>();
        env.put("a", 100.3);
        env.put("b", 45);
        env.put("c", -199.100);
        // 执行表达式
        boolean result =ExpressionEngine.execute(code, env);
        System.out.println(result);  // false
    }
```

## 3.10 Code Optimization

To further improve execution efficiency, `ExpressionEngine` provides code optimization functionality, which is enabled by default. You can disable it using `ExpressionEngine.setOption(Option.OPTIMIZE_CODE, false);`.

**The main purposes of code optimization are**:

1. Removing unreachable instructions to reduce the overall number of bytecode instructions.
2. Computing the values of constant expressions. If an expression is executed multiple times, significant performance improvements can be achieved.

For a constant expression, let's compare the number of bytecode instructions generated before and after enabling code optimization.

```java
    public static void main(String[] args) {
        ExpressionEngine.setOption(Option.OPTIMIZE_CODE, false);
//       ExpressionEngine.setOption(Option.OPTIMIZE_CODE, true);
        ExpressionCode code =ExpressionEngine.compile("(1+3/4 > 0|| true)&& !a");
        System.out.println(code.toReadableString());
    }
```

**When code optimization is disabled, the generated bytecode sequence is as follows:**

```
    0   :  _lconst 1
    1   :  _lconst 3
    2   :  _lconst 4
    3   :  _div
    4   :  _add
    5   :  _lconst 0
    6   :  _cmp
    7   :  _ifgt 10
    8   :  _bconst true
    9   :  _ifeq 14
    10  :  _pload a
    11  :  _ifne 14
    12  :  _bconst true
    13  :  _goto 15
    14  :  _bconst false
    15  :  _return
```

**When code optimization is enabled, the generated bytecode sequence is as follows:**

```
    0   :  _pload a
    1   :  _ifne 4
    2   :  _bconst true
    3   :  _goto 5
    4   :  _bconst false
    5   :  _return
```

After optimization, the number of bytecode instructions is reduced by up to 75%. The more constant subexpressions in the expression, the higher the percentage of optimized bytecode.

# 4 Grammar Manual

## 4.1 Data Types

1. `Number`: `long` and `double` are collectively referred to as Number types.
   * `long`: Long integer. Its literal values include all int literals in Java (including octal and hexadecimal) as well as long literals, for example: `1`, `1L`, `0777`, `0XFFF`, etc.
   * `double`: Double-precision floating-point number. Its literal values include all float literals and double literals in Java, for example: `1.0f`, `1.0d`, `1.0`, `1e-10`, `.1`, `1.`, etc.
2. `boolean`: Boolean value. Its literal values are `true` and `false`.
3. `String`: String. Its literal values are character sequences enclosed in `single quotes` or `double quotes`.
4. `null`: Null pointer. Its literal value is `null`.
5. `Object`: Objects passed through the environment variables. No literal value.

## 4.2 Operators

### 4.2.1 Arithmetic Operators

`ExpressionEngine` supports common arithmetic operators, including `+`, `-`, `*`, `/`, and `%` as binary operators, and `-` as a unary operator. The unary operator `-`, `*`, `/`, `%` can only be applied to `Number` types.

The `+` operator can be used not only for `Number` types but also for string concatenation or concatenating strings with other objects.

`ExpressionEngine` specifies that when any type is concatenated with `String`, the result is `String`.

### 4.2.2 Logical Operators

`ExpressionEngine` supports logical operators: unary negation `!`, logical AND `&&`, and logical OR `||`. The operands of logical operators must be of type `Boolean`, and overloading is not supported.

The `&&` and `||` operators perform short-circuit evaluation.

### 4.2.3 Relational Operators

`ExpressionEngine` supports the following relational operators: `<`, `<=`, `>`, `>=`, `==`, and `!=`.

Relational operators can be applied to Number types and String types.

### 4.2.4 Bitwise Operators

`ExpressionEngine` supports all Java bitwise operators, including `&`, `|`, `^`, `~`, `>>`, `<<`, and `>>>`.

### 4.2.5 Ternary Operator

`ExpressionEngine` does not provide an `if-else` statement. However, it does provide the ternary operator `?:` in the form of `condition ? exp1 : exp2`. The condition must be a `Boolean` expression, while exp1 and exp2 can be any valid `ExpressionEngine` expressions, and the return types of exp1 and exp2 are not required to be the same.

## 4.3 Built-in Functions

| Function Name | Description |
|:--|:--|
| `collection.include(seq, item)` | Determines whether the collection `seq` contains the element `item`. `seq` can be an array type or a List. |
| `collection.size(seq)` | Computes the length of the collection `seq`, which can be an array type or a List. |
| `date.timestamp()` | Returns the current timestamp, equivalent to `System.currentTimeMillis()`. |
| `math.abs(a)` | Computes the absolute value. The type of `a` can be `long` or `double`. |
| `math.cos(a)` | Computes the cosine value. The type of `a` can be `long` or `double`. |
| `math.log10(a)` | Computes the logarithm to the base 10. The type of `a` can be `long` or `double`. |
| `math.log(a)` | Computes the logarithm to the base 2. The type of `a` can be `long` or `double`. |
| `math.max(a, b)` | Returns the larger value between `a` and `b`. The types of `a` and `b` can be `long` or `double`. |
| `math.min(a, b)` | Returns the smaller value between `a` and `b`. The types of `a` and `b` can be `long` or `double`. |
| `math.pow(a, b)` | Computes `a` raised to the power of `b`. The types of `a` and `b` can be `long` or `double`. |
| `math.randomDouble()` | Returns a random `double` value. |
| `math.randomLong()` | Returns a random `long` value. |
| `math.randomLong(limit)` | Returns a random positive `long` value that does not exceed `limit`. |
| `math.sin(a)` | Computes the sine value. The type of `a` can be `long` or `double`. |
| `math.tan(a)` | Computes the tangent value. The type of `a` can be `long` or `double`. |
| `string.startsWith(s, prefix)` | Same as the `startsWith` method in Java's String class. |
| `string.endsWith(s, suffix)` | Same as the `endsWith` method in Java's String class. |
| `string.indexOf(s, item)` | Same as the `indexOf` method in Java's String class. |
| `string.join(seq, separator)` | Concatenates the elements in `seq` using the separator `separator` and returns a String. |
| `string.length(s)` | Same as the `length` method in Java's String class. |
| `string.replaceAll(s, regex, replacement)` | Same as the `replaceAll` method in Java's String class. |
| `string.replaceFirst(s, regex, replacement)` | Same as the `replaceFirst` method in Java's String class. |
| `string.split(regex)` | Same as the `split` method in Java's String class. |

