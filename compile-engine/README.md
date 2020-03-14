# 简介

__编译过程一般包括以下几个阶段__

1. `词法分析`: 将输入流转换为`Token`流的过程
1. `语法分析`: 判断`Token`流是否满足给定的文法定义
1. `语义分析`: 在语法分析的过程中，会执行一些副作用，这些副作用就是语义分析
1. `中间代码生成`: 语义分析的产物可能是中间代码
1. `机器无关代码优化`: 中间代码可以进行一些机器无关的代码优化，比如
    * 删除公共子表达式
    * 删除无用代码
    * 常量合并
    * 代码移动
    * 删除归纳变量
1. `机器相关目标代码`: 将中间代码翻译成特定架构的机器码
1. `机器相关代码优化`

__编译引擎提供了一整套编译的框架，抽象了如下几个步骤__

1. `词法分析`: 借助编译引擎，我们通过简单的构建步骤，就可以创建一个词法分析器
1. `语法分析`: 我们只需要定义具体的文法，编译引擎将会把文法定义翻译成状态自动机
1. `语义分析`: 借助编译引擎，我们可以轻易地扩展语义分析

__编译引擎支持多种不同的文法分析法，包括__

1. `LL1`: 分析能力最差
1. `SLR`: 分析能力稍强于`LL1`
1. `LR0`: 分析能力稍强于`SLR`
1. `LR1`: 分析能力最强
1. `LALR`（推荐）: 分析能力与`LR1`相同，但是状态机的大小小于`LR1`

# Maven 依赖

```xml
        <dependency>
            <groupId>com.github.liuyehcf</groupId>
            <artifactId>compile-engine</artifactId>
            <version>1.0.3</version>
        </dependency>
```

# Quick Start

__我们用编译引擎来完成一个简单的计算器，功能如下__

1. 支持`+`、`-`、`*`、`/`
1. `*`、`/`的优先级高于`+`、`-`
1. 支持`()`
1. 仅支持整数

[详细示例代码](src/test/java/com/github/liuyehcf/framework/compile/engine/test/example/calculator)

## 定义词法分析器

我们可以用`DefaultLexicalAnalyzer`来构建一个词法分析器，计算器的词素包括

1. 运算符，`+`、`-`、`*`、`/`
1. 括号`()`
1. 数字

__词素的类型有如下三种__

1. `normal`: 普通词素，例如这里的`+`、`-`、`*`、`/`
1. `keyword`: 关键词，其优先级会高于普通词素
1. `operator`: 自定义的解析过程，例如这里的整数的解析

```Java
    static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.builder()
            .addTokenOperator(Symbol.createIdentifierTerminator(IDENTIFIER_INTEGER_LITERAL), new IntegerIdentifier())
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES), "(")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES), ")")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_MUL), "*")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_DIV), "/")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_ADD), "+")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SUB), "-")
            .build();
```

# 定义文法

计算器的文法定义如下

```
<program> → <expression>
<expression> → <additive expression>
<additive expression> → <additive expression> + <multiplicative expression>
    | <additive expression> - <multiplicative expression>
    | <multiplicative expression>
<multiplicative expression> → <multiplicative expression> * <primary>
    | <multiplicative expression> * <primary> 
    | <multiplicative expression> / <primary>
    | <primary>
<primary> → #integerLiteral 
    | ( <expression> )
```

利用编译引擎提供的文法定义工具，对上述文法进行翻译

1. `Symbol`: 文法符号，包括终结符、非终结符
1. `SymbolString`: 文法符号串
1. `PrimaryProduction`: 产生式，可以包含语义动作
1. `Production`: 具有相同左部的产生式的集合
1. `Grammar`: 文法

示例代码参考`com.github.liuyehcf.framework.compile.engine.test.example.calculator.CalculatorGrammar`

# 语义动作

计算器的语义动作很简单

1. 在归约`<additive expression> → <additive expression> + <multiplicative expression>`时，添加`Add`操作码
1. 在归约`<additive expression> → <additive expression> - <multiplicative expression>`时，添加`Sub`操作码
1. 在归约`<multiplicative expression> → <multiplicative expression> * <primary>`时，添加`Mul`操作码
1. 在归约`<multiplicative expression> → <multiplicative expression> / <primary>`时，添加`Div`操作码
1. 在归约`<primary> → #integerLiteral`时，添加`Load`操作码

编译后生成`产物`就是包含`Add`、`Sub`、`Mul`、`Div`、`Load`的操作码集合，结果计算就是按照操作码的排列顺序依次进行计算即可

示例代码参考`com.github.liuyehcf.framework.compile.engine.test.example.calculator.CalculatorCode`