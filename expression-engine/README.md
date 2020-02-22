
# 1 简介

`ExpressionEngine`是一个高性能、轻量级的`Java`语言实现的表达式求值引擎，主要用于各种表达式的动态求值。现在已经有很多开源可用的`Java`表达式求值引擎，为什么还需要`ExpressionEngine`呢?

`ExpressionEngine`的设计目标是轻量级和高性能，相比于`Groovy`、`JRuby`的笨重，`ExpressionEngine`非常小，不算依赖包的话不到`100K`。当然，`ExpressionEngine`的语法是受限的，它不是一门完整的语言，而只是语言的一小部分集合

其次，`ExpressionEngine`的实现思路与其他轻量级的求值器很不相同，其他求值器一般都是通过解释的方式运行，而`ExpressionEngine`则是直接将表达式编译成`字节码`（非`Java`字节码，而是自研的一套字节码，基本与`Java`字节码对标）。简单来说，`ExpressionEngine`的定位是介于`Groovy`这样的重量级脚本语言和`IKExpression`这样的轻量级表达式引擎之间

__ExpressionEngine的特性：__

1. 支持绝大多数运算操作符，包括算术操作符、关系运算符、逻辑操作符、位运算符、三元表达式`(?:)`
1. 逻辑运算符支持短路运算
1. 支持丰富的字面值类型，例如`null`、整数`long`和浮点数`double`、字符串`String`、数组等
1. 支持自动类型转换，例如，在`1L+1.0D`表达式执行时，会自动将`1L`提升为`long`，然后执行`double+double`并返回`double`类型的结果
1. 内置一套强大的常用函数库
1. 可自定义函数，易于扩展
1. 可重载运算符（该重载的含义与一般语言中重载的含义的不同，下文详细介绍）
1. 性能优秀

# 2 Maven依赖

```xml
<dependency>
    <groupId>com.github.liuyehcf</groupId>
    <artifactId>expression-engine</artifactId>
    <version>${version}</version>
</dependency>
```

目前最新版本：`1.0.1`

# 3 使用手册

## 3.1 执行表达式

`ExpressionEngine`的使用都是集中通过`com.github.liuyehcf.framework.expression.engine.ExpressionEngine`这个入口类来处理，最简单的例子，执行一个计算`1+2+3`的表达式

```java
    public static void main(String[] args) {
        long result = ExpressionEngine.execute("1+2+3");
        System.out.println(result); // 6
    }
```

细心的朋友肯定注意到结果是`Long`，而不是`Integer`。这是因为`ExpressionEngine`的数值类型仅支持`Long`和`Double`，任何整数都将转换成`Long`，任何浮点数都将转换为`Double`，包括用户传入的变量数值。这个例子的打印结果将是正确答案`6`

## 3.2 使用变量

想让`ExpressionEngine`对你say hello吗？很简单，传入你的名字，让`ExpressionEngine`负责字符串的相加

```java
    public static void main(String[] args) {
        String yourName = "liuye";
        Map<String, Object> env = new HashMap<>();
        env.put("yourName", yourName);
        String result = ExpressionEngine.execute(" 'hello ' + yourName ", env);
        System.out.println(result);  // hello liuye
    }
```

上面的例子演示了怎么向表达式传入变量值，表达式中的`yourName`是一个变量，默认为null，通过传入`Map<String,Object>`的变量绑定环境，将`yourName`设置为你输入的名称。`env`的`key`是变量名，`value`是变量的值

上面例子中的`'hello '`是一个`ExpressionEngine`的String，`ExpressionEngine`的String是`双引号`或`单引号`括起来的字符序列，String可以与任何对象相加，任何对象与String相加结果为String。String中也可以有转义字符，如`\n`、`\\`、`\"`等

```java
    public static void main(String[] args) {
        System.out.println((String) ExpressionEngine.execute(" 'a\"b' "));           // 字符串 a"b
        System.out.println((String) ExpressionEngine.execute(" \"a\'b\" "));         // 字符串 a'b
        System.out.println((String) ExpressionEngine.execute(" 'hello ' + 3 "));     // 字符串 hello 3
        System.out.println((String) ExpressionEngine.execute(" 'hello '+ unknow ")); // 字符串 hello null
    }
```

## 3.3 调用函数

`ExpressionEngine`支持函数调用，函数调用的风格类似`lua`，下面的例子获取字符串的长度

```java
    public static void main(String[] args) {
        System.out.println((long) ExpressionEngine.execute("string.length('hello')"));  // 5
    }
```

`string.length('hello')`是一个函数调用，`string.length`是一个函数，`'hello'`是调用的参数

## 3.4 自定义函数

`ExpressionEngine`除了内置的函数之外，还允许用户自定义函数，只要实现`com.github.liuyehcf.framework.expression.engine.core.function.Function`接口，并注册到`ExpressionEngine`即可使用。Function接口十分庞大，通常来说你并不需要实现所有的方法，只要根据你的方法的参数个数，继承Function类并override相应方法即可

可以看一个例子，我们实现一个add函数来做数值的相加

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

__注意到，函数的所有入参类型都是`ExpressionValue`，返回的结果类型也是`ExpressionValue`__

注册函数通过`ExpressionEngine.addFunction`方法，移除可以通过`removeFunction`

如果你的参数个数不确定，可以继承`com.github.liuyehcf.framework.expression.engine.core.function.VarargsFunction`类，只要实现其中的`variadicCall`方法即可，比如我们实现拼接所有参数的方法

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

## 3.5 重载运算符

`ExpressionEngine`支持的`运算符`参见操作符一节。部分用户可能有重载这些内置运算符的需求，例如在`Excel`里，`&`不是位运算，而是字符串连接符，那么你可以通过运算符重载来实现

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

请注意，运算符重载使用不当，一定程度上会带来混乱

__`ExpressionEngine`中的运算符，其运行时的行为是由一系列的运算符函数来完成的（函数的顺序通过`OperatorFunction.getOrder()`方法的返回值来控制，数值越小，优先级越高），因此上面说的重载，其实并不是真正意义上的重载，而是增加一种额外的行为，并不会覆盖已有的行为（对于上面的示例，`long & long`还是能够正常执行）__

* 内建的运算符函数，其order数值为`Integer.MAX_VALUE or Integer.MAX_VALUE >> 1`
* 新增的运算符函数，默认的order为0（可以覆盖方法修改其返回值），因此默认就会排在内建的运算符函数之前

以`&`运算符为例，介绍一下运算符的执行过程，具体代码可以参考`com.github.liuyehcf.framework.expression.engine.core.bytecode.cp._and`

1. 据运算符的类型，取出所有的运算符函数
1. 取出栈顶的两个操作数
1. 遍历所有运算符函数
1. 找到第一个能够兼容value1和value2的类型的运算符函数
1. 执行函数，并将结果压入操作数栈
1. 递增字节码序号，执行结束

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

如果要清除运算符所有默认的行为，那么调用`ExpressionEngine.cleanOperatorFunctions`或`ExpressionEngine.cleanOperatorFunction(type)`即可

__`ExpressionEngine`允许重载的运算符以及对应的基类如下__

* 单目运算符
    1. `-`：NegOperatorFunction
* 双目运算符
    1. `+`：AddOperatorFunction
    1. `-`：SubOperatorFunction
    1. `*`：MulOperatorFunction
    1. `/`：DivOperatorFunction
    1. `%`：RemOperatorFunction
    1. `&`：BitAndOperatorFunction
    1. `|`：BitOrOperatorFunction
    1. `^`：BitXorOperatorFunction
    1. `<<`：ShlOperatorFunction
    1. `>>`：ShrOperatorFunction
    1. `>>>`：UshrOperatorFunction
* 比较运算符：
    1. `<`：CmpOperatorFunction
    1. `<=`：CmpOperatorFunction
    1. `>`：CmpOperatorFunction
    1. `>=`：CmpOperatorFunction
    1. `==`：CmpOperatorFunction
    1. `!=`：CmpOperatorFunction
    * 上述比较运算符共享同一个基类，即：CmpOperatorFunction

__再举一个比较运算符的例子，我们只需注册一个OperationFunction（通过继承CmpOperatorFunction，会更方便些），然后所有的比较运算符（`<`、`<=`、`>`、`>=`、`==`、`!=`、）均可生效__

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

## 3.6 三元操作符

`ExpressionEngine`暂时不提供`if else`语句（引入lambda表达式的时候，一并支持`if else`），但是提供了三元操作符`?:`用于条件判断，其含义与该操作符在`Java`中的含义相同

```java
    public static void main(String[] args) {
        System.out.println((String)ExpressionEngine.execute("-1>0?'yes':'no'"));  // no
    }
```

## 3.7 数组初始化表达式

`ExpressionEngine`允许以一种列表初始化的方式构建一个数组

```java
    public static void main(String[] args) {
        Map<String, Object> env = new HashMap<>();
        env.put("yourName", "liuye");
        Object[] array =ExpressionEngine.execute("[1L,2.0d,'hello',null,yourName]", env);
        System.out.println(Arrays.toString(array));// [1, 2.0, hello, null, liuye]
    }
```

数组初始化表达式可以出现在任何语法结构处，例如方法参数，三元运算符等等

```java
    public static void main(String[] args) {
        System.out.println((String)ExpressionEngine.execute("string.join(['a','b','c'],'#')")); // a#b#c
    }
```

上面`string.join`用于将一个集合用分隔符`#`拼接起来，返回一个String

## 3.8 访问数组和集合

可以通过中括号去访问`数组`和`java.util.List`对象，可以通过`map.key`访问`java.util.Map`中`key`对应的`value`

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

## 3.9 编译表达式

上面提到的例子都是直接执行表达式，事实上`ExpressionEngine`背后都帮你做了编译并执行的工作。你可以自己先编译表达式，返回一个编译的结果，然后传入不同的`env`来复用编译结果，提高性能（除非表达式非常大，否则编译是极快的，这样做的性能提升有限），并且`ExpressionEngine`会主动帮你做缓存，对于相同的表达式，会先从缓存中取出之前编译的结果，如果取不到，才会进行编译操作

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

## 3.10 代码优化

为了进一步提升执行效率，`ExpressionEngine`提供了代码优化的功能，默认开启，通过`ExpressionEngine.setOption(Option.OPTIMIZE_CODE, false);`进行关闭

__代码优化的主要作用__：

1. 清除不可达指令，减少字节码总体数量
1. 计算常量表达式的值，若对于同一表达式多次执行，那么会有显著的性能提升

对于一个常量表达式，我们来看下开启代码优化前后，输出的字节码的数量

```java
    public static void main(String[] args) {
        ExpressionEngine.setOption(Option.OPTIMIZE_CODE, false);
//       ExpressionEngine.setOption(Option.OPTIMIZE_CODE, true);
        ExpressionCode code =ExpressionEngine.compile("(1+3/4 > 0|| true)&& !a");
        System.out.println(code.toReadableString());
    }
```

__关闭代码优化时，编译得到的字节码序列如下__

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

__开启代码优化时，编译得到的字节码序列如下__

```
    0   :  _pload a
    1   :  _ifne 4
    2   :  _bconst true
    3   :  _goto 5
    4   :  _bconst false
    5   :  _return
```

经过代码优化后，字节码的数量减少了75%之多，表达式中常量子表达式部分越多，优化的字节码占比越高

# 4 语法手册

## 4.1 数据类型

1. `Number`：`long`和`double`统称为Number类型
    * `long`：长整型，其字面值包括java中所有的int类型字面值（包括8进制以及16进制）以及long型字面值，例如：`1`、`1L`、`0777`、`0XFFF`等
    * `double`：双精度浮点数，其字面值包括java中所有float类型字面值以及double类型字面值，例如：`1.0f`、`1.0d`、`1.0`、`1e-10`、`.1`、`1.`等
1. `boolean`：布尔值，其字面值包括`true`和`false`
1. `String`：字符串，其字面值为由`单引号`或`双引号`包围起来的字符序列
1. `null`：空指针，其字面值为`null`
1. `Object`：通过环境变量传递进来的对象，无字面值

## 4.2 操作符

### 4.2.1 算数运算符

`ExpressionEngine`支持常见的算术运算符，包括`+`、`-`、`*`、`/`、`%`五个二元运算符，和一元运算符`-`(负)。其中`-`、`*`、`/`、`%`和一元的`-`仅能作用于`Number`类型

`+`不仅能用于`Number`类型，还可以用于`String`的相加，或者字符串与其他对象的相加

`ExpressionEngine`规定，任何类型与`String`相加，结果为`String`

### 4.2.2 逻辑运算符

`ExpressionEngine`的支持的逻辑运算符包括，一元否定运算符`!`以及逻辑与的`&&`，逻辑或的`||`。逻辑运算符的操作数只能为`Boolean`，且不支持重载

`&&`和`||`都执行短路规则

### 4.2.3 关系运算符

`ExpressionEngine`支持的关系运算符包括`<`，`<=`，`>`，`>=`以及`==`和`!=`

关系运算符可以作用于Number之间、String之间

### 4.2.4 位运算符

`ExpressionEngine`支持所有的`Java`位运算符，包括`&`，`|`，`^`，`~`，`>>`，`<<`，`>>>`

### 4.2.5 三元运算符

`ExpressionEngine`没有提供`if else`语句，但是提供了三元运算符`?:`，形式为`condition ? exp1: exp2`。其中condition必须为`Boolean`类型的表达式，而exp1和exp2可以为任何合法的`ExpressionEngine`表达式，并且`不要求`exp1和exp2返回的结果类型一致

## 4.3 内置函数

| 函数名称 | 说明 |
|:--|:--|
| `collection.include(seq, item)` | 判断集合seq中是否包含元素item。seq可以是数组类型或者List |
| `collection.size(seq)` | 计算集合seq的长度，seq可以是数组类型或者List |
| `date.timestamp()` | 返回当前时间戳，等效于`System.currentTimeMillis()` |
| `math.abs(a)` | 计算绝对值值，a的类型可以是long或double |
| `math.cos(a)` | 计算cos值，a的类型可以是long或double |
| `math.log10(a)` | 计算以10为底的对数，a的类型可以是long或double |
| `math.log(a)` | 计算以2为底的对数，a的类型可以是long或double |
| `math.max(a, b)` | 取a、b中较大者，a和b的类型可以是long或double |
| `math.min(a, b)` | 取a、b中较小者，a和b的类型可以是long或double |
| `math.pow(a, b)` | 求以a为底，以b为指数的值，a和b的类型可以是long或double |
| `math.randomDouble()` | 返回一个随机的double值 |
| `math.randomLong()` | 返回一个随机的long值 |
| `math.randomLong(limit)` | 返回一个随机的long型正值，不超过limit |
| `math.sin(a)` | 计算sin值，a的类型可以是long或double |
| `math.tan(a)` | 计算tan值，a的类型可以是long或double |
| `string.startsWith(s, prefix)` | 同 Java 里的 String.startsWith 方法 |
| `string.endsWith(s, suffix)` | 同 Java 里的 String.endsWith 方法 |
| `string.indexOf(s, item)` | 同 Java 里的  String.indexOf 方法 |
| `string.join(seq, separator)` | 将seq中的元素以分隔符separator进行拼接，返回一个String |
| `string.length(s)` | 同 Java 里的 String.length 方法 |
| `string.replaceAll(s, regex, replacement)` | 同 Java 里的 String.replaceAll 方法 |
| `string.replaceFirst(s, regex, replacement)` | 同 Java 里的 String.replaceFirst 方法 |
| `string.split(regex)` | 同 Java 里的 String.split 方法 |
