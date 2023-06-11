   * [Introduction](#introduction)
   * [Maven Dependency](#maven-dependency)
   * [Quick Start](#quick-start)
      * [Defining the Lexical Analyzer](#defining-the-lexical-analyzer)
   * [Defining Grammar](#defining-grammar)
   * [Semantic Action](#semantic-action)

# Introduction

__The compilation process generally includes the following stages__

1. `Lexical Analysis`: The process of converting the input stream into a `Token` stream
1. `Syntax Analysis`: Determine whether the `Token` stream satisfies the given grammar definition
1. `Semantic Analysis`: During the syntax analysis process, some side effects will be executed, these side effects are semantic analysis
1. `Intermediate Code Generation`: The result of semantic analysis may be intermediate code
1. `Machine Independent Code Optimization`: Intermediate code can undergo some machine-independent code optimizations, such as
    * Deleting common sub-expressions
    * Removing useless code
    * Constant merging
    * Code movement
    * Deleting induction variables
1. `Machine-Dependent Target Code`: Translating intermediate code into machine code for a specific architecture
1. `Machine Dependent Code Optimization`

__The compilation engine provides a complete framework for compilation, abstracting the following steps__

1. `Lexical Analysis`: With the help of the compilation engine, we can create a lexical analyzer with simple construction steps
1. `Syntax Analysis`: We only need to define the specific grammar, the compilation engine will translate the grammar definition into a state automaton
1. `Semantic Analysis`: With the help of the compilation engine, we can easily expand the semantic analysis

__The compilation engine supports various different grammar analysis methods, including__

1. `LL1`: Worst analysis capability
1. `SLR`: Analysis capability slightly stronger than `LL1`
1. `LR0`: Analysis capability slightly stronger than `SLR`
1. `LR1`: Strongest analysis capability
1. `LALR` (recommended): The analysis capability is the same as `LR1`, but the size of the state machine is smaller than `LR1`

# Maven Dependency

```xml
        <dependency>
            <groupId>com.github.liuyehcf</groupId>
            <artifactId>compile-engine</artifactId>
            <version>1.0.3</version>
        </dependency>
```

# Quick Start

__We use the compilation engine to complete a simple calculator, with the following functions__

1. Supports `+`, `-`, `*`, `/`
1. `*`, `/` have higher priority than `+`, `-`
1. Supports `()`
1. Only supports integers

[Detailed Example Code](src/test/java/com/github/liuyehcf/framework/compile/engine/test/example/calculator)

## Defining the Lexical Analyzer

We can use `DefaultLexicalAnalyzer` to build a lexical analyzer. The lexemes of the calculator include

1. Operators, `+`, `-`, `*`, `/`
1. Brackets `()`
1. Numbers

__There are three types of lexemes__

1. `normal`: Normal lexeme, such as `+`, `-`, `*`, `/` here
1. `keyword`: Keywords, which have higher priority than normal lexemes
1. `operator`: Custom parsing process, such as parsing integers here

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

# Defining Grammar

The grammar definition of the calculator is as follows

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

Using the grammar definition tool provided by the compilation engine, we translate the above grammar:

1. `Symbol`: Grammar symbols, including terminators and non-terminators
1. `SymbolString`: Grammar symbol string
1. `PrimaryProduction`: Productions, can contain semantic actions
1. `Production`: A collection of productions with the same left-hand side
1. `Grammar`: Grammar

For example code, refer to `com.github.liuyehcf.framework.compile.engine.test.example.calculator.CalculatorGrammar`

# Semantic Actions

The semantic actions of the calculator are simple:

1. When reducing `<additive expression> → <additive expression> + <multiplicative expression>`, add `Add` opcode
1. When reducing `<additive expression> → <additive expression> - <multiplicative expression>`, add `Sub` opcode
1. When reducing `<multiplicative expression> → <multiplicative expression> * <primary>`, add `Mul` opcode
1. When reducing `<multiplicative expression> → <multiplicative expression> / <primary>`, add `Div` opcode
1. When reducing `<primary> → #integerLiteral`, add `Load` opcode

The generated `product` after compilation is a collection of opcodes containing `Add`, `Sub`, `Mul`, `Div`, `Load`. The result calculation can be done by performing calculations in the order of the opcode arrangement.

For example code, refer to `com.github.liuyehcf.framework.compile.engine.test.example.calculator.CalculatorCode`
