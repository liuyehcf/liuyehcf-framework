* [Usage](#usage)
   * [Syntax](#syntax)
* [Compiler Principles](#compiler-principles)
* [Introduction to Compilation Engine](#introduction-to-compilation-engine)
* [Hua Language](#hua-language)
   * [Introduction to compile-engine-hua Module](#introduction-to-compile-engine-hua-module)
   * [Challenges](#challenges)
   * [Boolean Backpatching for if then Statements](#boolean-backpatching-for-if-then-statements)
   * [Boolean Backpatching for if then else Statements](#boolean-backpatching-for-if-then-else-statements)
   * [Boolean Backpatching for while Statements](#boolean-backpatching-for-while-statements)
   * [Boolean Backpatching for do while Statements](#boolean-backpatching-for-do-while-statements)
   * [Boolean Backpatching for && Statements](#boolean-backpatching-for-ampamp-statements)
   * [Boolean Backpatching for || Statements](#boolean-backpatching-for-ampamp-statements-1)
* [Examples](#examples)
   * [Writing Source Code](#writing-source-code)
   * [Compiling](#compiling)
   * [Running](#running)
   * [Viewing the Content of Compiled Bytecode File](#viewing-the-content-of-compiled-bytecode-file)

# Usage

1. Download this project
1. Package using Maven tool: `mvn clean package -Dmaven.test.skip=true`
1. After packaging is completed, locate the `language-hua/target/language-hua.jar` file
1. Set the environment variable `HUA_PATH` to the absolute path of the directory where `language-hua.jar` is located
1. The hua language provides three command-line tools in the `language-hua/cmd` directory:
   * `huac`: Compiles source code (.hua file) and generates bytecode files (.hcalss)
   * `hua`: Runs the program
   * `huap`: Translates the content of bytecode files (.hcalss)
1. Write a .hua source file, for example, `test.hua` (see example below)
1. Compile by executing the command `huac -f test.hua`, which will generate a `.hclass` file with the same name in the current directory
   * If you want to specify the output directory, you can use the `-d` parameter. You can use the `huac -h` command to view the help documentation.
1. Run by executing the command `hua -f test.hclass`
   * You can provide additional arguments after the command, which will be passed as input to the `main` method
1. View the compiled bytecode by executing the command `huap -f test.hclass`

## Syntax

1. Keywords
   * if
   * else
   * while
   * for
   * do
   * void
   * boolean
   * char
   * int
   * long
   * float
   * double
   * new
   * return
   * true
   * false
   * sizeof
1. Syntax
   * Supports function definition
   * Supports function declaration
   * Supports function invocation, the called function must be declared or defined beforehand (similar to C++)
   * Supports variable declaration and initialization. Syntax is similar to Java
   * Supports arrays of any dimension
   * Supports creating arrays using the `new` statement. Syntax is similar to Java
   * Supports `for` loop, `while` loop, and `do while` loop. Syntax is similar to Java
   * Supports binary operators `*`, `/`, `%`, `+`, `-`, `<<`, `>>`, `>>>`, `&`, `^`, `|`
   * Supports prefix and postfix increment and decrement `--`, `++`, but only for int type
   * Supports unary operators `+`, `-`, `~`, `!`
   * Supports conditional statements, syntax is similar to Java
   * Supports comparison operators `==`, `!=`, `<`, `<=`, `>`, `>=`
   * Supports `sizeof` expression, which calculates the length of an array
   * Supports assignment operator `=`
   * Supports compound assignment operators `*=`, `/=`, `%=`, `+=`, `-=`, `<<=`, `>>=`, `>>>=`, `&=`, `^=`, `|=`
   * Supports boolean literals
   * Supports int literals, including decimal, hexadecimal, and octal literals
   * Supports long literals with `l/L` suffix
   * Supports float literals with `f/F` suffix
      * `0f`
      * `1.f`
      * `.1f`
      * `1e10f`
      * `1e+10f`
      * `1e-10f`
      * `3.14e-3f`
   * Supports double literals with `d/D` suffix
      * `0d`
      * `1.d`
      * `.1d`
      * `1e10d`
      * `1e+10d`
      * `1e-10d`
      * `3.14e-3d`
   * Supports double literals
   * Supports char literals
   * Supports char[] literals (equivalent to String in Java)
   * Supports implicit type conversion (assignment, initialization, binary arithmetic operators, binary comparison operators)
      * `char -> int`
      * `char/int -> long`
      * `char/int/long -> float`
      * `char/int/long/float -> double`
1. System functions
   * `print(boolean)`
   * `print(char)`
   * `print(int)`
   * `print(long)`
   * `print(float)`
   * `print(double)`
   * `print(boolean[])`
   * `print(char[])`
   * `print(int[])`
   * `print(long[])`
   * `print(float[])`
   * `print(double[])`
   * `println(boolean)`
   * `println(char)`
   * `println(int)`
   * `println(long)`
   * `println(float)`
   * `println(double)`
   * `println(boolean[])`
   * `println(char[])`
   * `println(int[])`
   * `println(long[])`
   * `println(float[])`
   * `println(double[])`
   * `nextBoolean()`
   * `nextInt(int,int)`
   * `nextInt()`
   * `nextLong()`
   * `nextFloat()`
   * `nextDouble()`

1. An executable .hclass file must include the `main(char[][])` method, which is the entry point of the entire program.

# Compiler Principles

**[Please visit the blog for Compiler Principles](https://liuyehcf.github.io/categories/%E7%BC%96%E8%AF%91/)**

# Introduction to Compilation Engine

The `compile-engine-core` module mainly includes the following parts:

1. Grammar definition (for 2nd and 3rd type grammars only)
   * `Symbol`: Grammar symbol, including terminal and non-terminal symbols
   * `SymbolString`: Grammar symbol string
   * `PrimaryProduction`: Production rule
   * `Production`: Collection of production rules with the same left-hand side
   * `Grammar`: Grammar, including the start symbol and a series of production rules
1. Regex grammar (rg), which implements regular expressions
   * Implements NFA (Nfa) and DFA (Dfa) automata
   * The NFA automaton supports capture groups, while the DFA automaton currently does not support capture groups
1. Context-free grammar (cfg)
   * LL1 automaton
   * LR0 automaton
   * SLR automaton
   * LR1 automaton
   * LALR automaton
   * In terms of parsing ability, LR1=LALR>SLR>LL1, SLR>LR0
   * In terms of the number of states, LALR<LR1
   * In terms of error analysis ability, LALR<LR1
1. Lexical analyzer (DefaultLexicalAnalyzer)
   * Supports normal lexemes, which are fully matched
   * Supports regular expression lexemes
   * Custom matching process

The compilation engine encapsulates the process of lexical analysis and syntax analysis and can generate state automata for any unambiguous CFG language.

# Hua Language

HuaCompiler inherits the LALR parser, and its grammar is defined based on [Java BNF Definition](http://www.daimi.au.dk/dRegAut/JavaBNF.html).

1. Hua is a procedural language and does not support class definitions.
1. The basic syntax is very similar to Java.
1. Due to its procedural nature, a new operator `sizeof` is added to calculate the size of an array.

## Introduction to compile-engine-hua Module

1. Grammar definitions, package path: `com.github.liuyehcf.framework.language.hua.compile.definition`
1. Semantic actions related to grammar definitions, package path: `com.github.liuyehcf.framework.language.hua.compile.definition.semantic`
1. Bytecode definitions, most of which align with Java and have the same meanings. Some different instructions include:
   * Loading constants for int, boolean, and char types is unified using the `iconst` instruction, which differs from Java.
   * `invokestatic` instruction is used for method invocation.
   * `sizeof` instruction is used to calculate the size of an array.
1. Runtime environment, this part is relatively simple and mainly focuses on executing the compiled instruction sequence:
   * Memory management: Allocates a certain amount of memory space (byte array) initially, without garbage collection.
   * System methods: Wraps some of Java's capabilities, mainly standard output (STD OUT).
   * Similar to Java, each method has an operand stack during execution.
   * Memory for arrays is allocated on the heap, while other variables are allocated on the stack.
   * Arrays are encapsulated with their addresses, which also store the size of the array.
1. Command line:
   * Utilizes the `commons-cli` library.

## Challenges

1. Inheritance of properties
    * The current syntax tree node needs to pass certain attribute values to future syntax tree nodes.
    * This can be achieved by using a special data structure, referred to as `FutureSyntaxNodeStack`.
1. Translation of boolean expressions
    * Forward/backward transfer instructions: When identifying boolean expressions, insert forward transfer instruction types. When inserting transfer instructions, set them as forward or backward.
        * **Forward means jumping when the condition is false, and backward means jumping when the condition is true.**
        * For example, `if (<bool expression>)`, `while (<bool expression>)`, `for (; <bool expression>;)` are forward.
        * `do {} while (<bool expression>)` is backward.
    * Backpatching of boolean expressions: When generating transfer instructions, the starting position of the code segment to jump to is unknown. Backpatching needs to be performed at specific locations.
        * Backpatching types include `TRUE` backpatching, `FALSE` backpatching, and `NEXT` backpatching. `NEXT` backpatching only occurs in `if then else` statements.
    * Negation of boolean expressions
        * The backpatched `TRUE` and `FALSE` transfer instructions need to be swapped.
        * The type of transfer instructions needs to be negated.
1. About the lexer
    * Attention must be paid to the higher priority of `<=` compared to `<`, and similar cases.
    * Characters, strings, and integer literals currently need to be scanned as a whole, using the custom processing flow interface of the lexer. Otherwise, the following problem may occur:
        * If the string is split into individual characters for scanning, it will be unable to distinguish between whitespace inside the string and whitespace outside the string.
    * For details on constructing the lexer, refer to `GrammarDefinition`.

## Boolean Backpatching for if then Statements

The `if then` statement can be represented as follows:

`if (<condition expression>) { <statements for true> } <other statements>`

1. When `<condition expression>` is true, the instructions at `<statements for true>` are executed.
1. When `<condition expression>` is false, it needs to jump to the starting position of `<other statements>` and continue executing. In this case, we need to insert a control transfer instruction `ifeq`, called `FALSE-type backpatching`, with a forward logic.
   * The `ifeq` instruction means that when the value of `<condition expression>` is equal to 0 (i.e., false), it jumps to the target code.
   * When adding the `ifeq` instruction, we don't know where to jump (bytecode offset) to execute until we reach the position of `<other statements>`. Then we can backpatch the `ifeq` instruction with the bytecode offset of the jump target.

## Boolean Backpatching for if then else Statements

The `if then else` statement can be represented as follows:

`if (<condition expression>) { <statements for true> } else { <statements for false> } <other statements>`

1. When `<condition expression>` is true, the instructions at `<statements for true>` are executed.
1. When `<condition expression>` is false, it needs to jump to the starting position of `<statements for false>` and continue executing. In this case, we need to insert a control transfer instruction `ifeq`, called `FALSE-type backpatching`, with a forward logic.
1. After `<statements for true>` are executed, it needs to jump to the starting position of `<other statements>` and continue executing. In this case, we need to insert a control transfer instruction `goto`, called `NEXT-type backpatching`.

## Boolean Backpatching for while Statements

The `while` statement can be represented as follows:

`while (<condition expression>) { <statements for true> } <other statements>`

1. When `<condition expression>` is true, the instructions at `<statements for true>` are executed.
1. When `<condition expression>` is false, it needs to jump to the starting position of `<other statements>` and continue executing. In this case, we need to insert a control transfer instruction `ifeq`, called `FALSE-type backpatching`, with a forward logic.
1. After `<statements for true>` are executed, it needs to jump to the starting position of `<condition expression>` and continue executing. In this case, we need to insert a control transfer instruction `goto`, called `NEXT-type backpatching`.

## Boolean Backpatching for do while Statements

The `do while` statement can be represented as follows:

`do { <loop statements> } while (<condition expression>); <other statements>`

1. When `<condition expression>` is true, it needs to jump to the starting position of `<loop statements>` and continue executing. In this case, we need to insert a control transfer instruction `ifne`, called `TRUE-type backpatching`, with a backward logic.

## Boolean Backpatching for && Statements

The logical AND statement can be represented as follows:

`<condition expression1> && <condition expression2> <other statements>`

1. When `<condition expression1>` is true, the instructions at `<condition expression2>` are executed.
1. When `<condition expression1>` is false, it needs to jump to the starting position of `<other statements>` and continue executing. In this case, we need to insert a control transfer instruction `ifeq`, called `FALSE-type backpatching`, with a forward logic.

## Boolean Backpatching for || Statements

The logical OR statement can be represented as follows:

`<condition expression1> || <condition expression2> <other statements>`

1. When `<condition expression1>` is true, it needs to jump to the starting position of `<other statements>` and continue executing. In this case, we need to insert a control transfer instruction `ifne`, called `TRUE-type backpatching`, with a backward logic.
1. When `<condition expression1>` is false, the instructions at `<condition expression2>` are executed.

# Example

## Writing the Source Code

Let's implement quicksort in the Hua language with the file name `test.hua`. The source code is as follows:

```
void quickSort(int[] nums);
void quickSort(int[] nums, int lo, int hi);
int partition(int[] nums, int lo, int hi);
void exchange(int[] nums, int i, int j);

void main(char[][] args){
	int[] nums=new int[200];

	for(int i=0;i<200;i++){
		nums[i]=nextInt(50,100);
	}

	println("before quickSort:\n");
	println(nums);

	quickSort(nums);

	println("after quickSort:\n");
	println(nums);
}

void quickSort(int[] nums) {
	quickSort(nums, 0, sizeof nums -1);
}

void quickSort(int[] nums, int lo, int hi) {
	if (lo < hi) {
		int mid = partition(nums, lo, hi);
		quickSort(nums, lo, mid - 1);
		quickSort(nums, mid + 1, hi);
	}
}

int partition(int[] nums, int lo, int hi) {
	int i = lo - 1;
	int pivot = nums[hi];
	
	for (int j = lo; j < hi; j++) {
		if (nums[j] < pivot) {
			exchange(nums, ++i, j);
		}
	}

	exchange(nums, ++i, hi);
	
	return i;
}

void exchange(int[] nums, int i, int j) {
	int temp = nums[i];
	nums[i] = nums[j];
	nums[j] = temp;
}
```

## Compilation

Execute `huac -f test.hua` to generate `test.hclass` in the current directory.

The first compilation generates state automata, which may be slow, taking approximately 2 seconds.

## Execution

Execute `hua -f test.hclass` to run the program. The output is as follows:

```
before quickSort:

[55, 88, 66, 76, 56, 75, 99, 82, 58, 76, 78, 62, 91, 92, 56, 51, 89, 95, 89, 55, 99, 75, 71, 62, 74, 96, 61, 71, 93, 89, 92, 95, 92, 94, 84, 79, 76, 72, 75, 90, 86, 84, 63, 98, 66, 95, 67, 63, 57, 75, 86, 74, 70, 62, 70, 95, 96, 66, 74, 96, 99, 83, 69, 66, 65, 80, 63, 69, 97, 76, 61, 80, 53, 91, 98, 79, 67, 77, 50, 99, 52, 51, 72, 52, 98, 71, 59, 66, 55, 89, 98, 83, 57, 85, 57, 84, 80, 99, 61, 96, 57, 54, 63, 96, 82, 97, 78, 63, 60, 98, 55, 71, 92, 73, 54, 88, 77, 92, 63, 58, 50, 52, 96, 86, 84, 75, 92, 64, 88, 54, 59, 85, 86, 91, 87, 88, 96, 99, 50, 83, 64, 87, 52, 94, 53, 55, 73, 85, 77, 85, 86, 95, 85, 93, 75, 92, 88, 50, 80, 76, 67, 97, 54, 60, 50, 81, 97, 67, 83, 96, 66, 93, 69, 86, 81, 85, 59, 74, 97, 50, 90, 55, 90, 64, 97, 82, 62, 76, 88, 57, 74, 77, 83, 80, 92, 52, 81, 58, 95, 51]
after quickSort:

[50, 50, 50, 50, 50, 50, 51, 51, 51, 52, 52, 52, 52, 52, 53, 53, 54, 54, 54, 54, 55, 55, 55, 55, 55, 55, 56, 56, 57, 57, 57, 57, 57, 58, 58, 58, 59, 59, 59, 60, 60, 61, 61, 61, 62, 62, 62, 62, 63, 63, 63, 63, 63, 63, 64, 64, 64, 65, 66, 66, 66, 66, 66, 66, 67, 67, 67, 67, 69, 69, 69, 70, 70, 71, 71, 71, 71, 72, 72, 73, 73, 74, 74, 74, 74, 74, 75, 75, 75, 75, 75, 75, 76, 76, 76, 76, 76, 76, 77, 77, 77, 77, 78, 78, 79, 79, 80, 80, 80, 80, 80, 81, 81, 81, 82, 82, 82, 83, 83, 83, 83, 83, 84, 84, 84, 84, 85, 85, 85, 85, 85, 85, 86, 86, 86, 86, 86, 86, 87, 87, 88, 88, 88, 88, 88, 88, 89, 89, 89, 89, 90, 90, 90, 91, 91, 91, 92, 92, 92, 92, 92, 92, 92, 92, 93, 93, 93, 94, 94, 95, 95, 95, 95, 95, 95, 96, 96, 96, 96, 96, 96, 96, 96, 97, 97, 97, 97, 97, 97, 98, 98, 98, 98, 98, 99, 99, 99, 99, 99, 99]
```

## Viewing the Compiled Bytecode File

Execute `huap -f test.hclass` to obtain the following output:

```
Compiled from "Huap.java"

Constant pool:
	#0  =  print(boolean)
	#1  =  print(char)
	#2  =  print(int)
	#3  =  print(long)
	#4  =  print(boolean[])
	#5  =  print(char[])
	#6  =  print(int[])
	#7  =  print(long[])
	#8  =  println(boolean)
	#9  =  println(char)
	#10 =  println(int)
	#11 =  println(long)
	#12 =  println(boolean[])
	#13 =  println(char[])
	#14 =  println(int[])
	#15 =  println(long[])
	#16 =  nextInt(int,int)
	#17 =  nextBoolean()
	#18 =  nextInt()
	#19 =  quickSort(int[])
	#20 =  quickSort(int[],int,int)
	#21 =  partition(int[],int,int)
	#22 =  exchange(int[],int,int)
	#23 =  main(char[][])
	#24 =  before quickSort:

	#25 =  after quickSort:


main(char[][])
	Return type: void
	Param type: char[][]
	Code:
		0   :  _iconst 200
		1   :  _newarray int
		2   :  _astore 1
		3   :  _iconst 0
		4   :  _istore 2
		5   :  _iload 2
		6   :  _iconst 200
		7   :  _if_icmpge 16
		8   :  _aload 1
		9   :  _iload 2
		10  :  _iconst 50
		11  :  _iconst 100
		12  :  _invokestatic #16
		13  :  _iastore
		14  :  _iinc 2, 1
		15  :  _goto 5
		16  :  _ldc
		17  :  _invokestatic #13
		18  :  _aload 1
		19  :  _invokestatic #14
		20  :  _aload 1
		21  :  _invokestatic #19
		22  :  _ldc
		23  :  _invokestatic #13
		24  :  _aload 1
		25  :  _invokestatic #14
		26  :  _return

quickSort(int[])
	Return type: void
	Param type: int[]
	Code:
		0   :  _aload 0
		1   :  _iconst 0
		2   :  _sizeof 0
		3   :  _iconst 1
		4   :  _isub
		5   :  _invokestatic #20
		6   :  _return

quickSort(int[],int,int)
	Return type: void
	Param type: int[], int, int
	Code:
		0   :  _iload 1
		1   :  _iload 2
		2   :  _if_icmpge 20
		3   :  _aload 0
		4   :  _iload 1
		5   :  _iload 2
		6   :  _invokestatic #21
		7   :  _istore 3
		8   :  _aload 0
		9   :  _iload 1
		10  :  _iload 3
		11  :  _iconst 1
		12  :  _isub
		13  :  _invokestatic #20
		14  :  _aload 0
		15  :  _iload 3
		16  :  _iconst 1
		17  :  _iadd
		18  :  _iload 2
		19  :  _invokestatic #20
		20  :  _return

partition(int[],int,int)
	Return type: int
	Param type: int[], int, int
	Code:
		0   :  _iload 1
		1   :  _iconst 1
		2   :  _isub
		3   :  _istore 3
		4   :  _aload 0
		5   :  _iload 2
		6   :  _iaload
		7   :  _istore 4
		8   :  _iload 1
		9   :  _istore 5
		10  :  _iload 5
		11  :  _iload 2
		12  :  _if_icmpge 25
		13  :  _aload 0
		14  :  _iload 5
		15  :  _iaload
		16  :  _iload 4
		17  :  _if_icmpge 23
		18  :  _aload 0
		19  :  _iinc 3, 1
		20  :  _iload 3
		21  :  _iload 5
		22  :  _invokestatic #22
		23  :  _iinc 5, 1
		24  :  _goto 10
		25  :  _aload 0
		26  :  _iinc 3, 1
		27  :  _iload 3
		28  :  _iload 2
		29  :  _invokestatic #22
		30  :  _iload 3
		31  :  _ireturn

exchange(int[],int,int)
	Return type: void
	Param type: int[], int, int
	Code:
		0   :  _aload 0
		1   :  _iload 1
		2   :  _iaload
		3   :  _istore 3
		4   :  _aload 0
		5   :  _iload 1
		6   :  _aload 0
		7   :  _iload 2
		8   :  _iaload
		9   :  _iastore
		10  :  _aload 0
		11  :  _iload 2
		12  :  _iload 3
		13  :  _iastore
		14  :  _return
```