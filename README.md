# 使用

1. 下载本工程
1. 利用maven工具打包`mvn clean package -Dmaven.test.skip=true`
1. 打包完成后，找到`compile-engine/compile-engine-hua/target/compile-engine-hua-1.0-SNAPSHOT.jar`文件
1. 设置环境变量`HUA_PATH`，其值为`compile-engine-hua-1.0-SNAPSHOT.jar`所在目录的绝对路径
1. hua语言提供了3个命令行工具，在`compile-engine/cmd`目录下，分别是
    * `huac`：编译源代码(.hua文件)，生成字节码文件(.hcalss)
    * `hua`：运行程序
    * `huap`：翻译字节码文件(.hcalss)的内容
1. 编写.hua源文件，例如test.hua（详见下方示例）
1. 编译，执行命令`huac -f test.hua`，会在当前路径下生成同名的`.hclass`文件
    * 如果要指定输出目录，可以使用`-d`参数，可以使用`huac -h`命令查看帮助文档
1. 运行，执行命令`hua -f test.hclass`
    * 后面可以跟上参数，参数将会作为main方法的入参
1. 查看编译后的字节码，执行命令`huap -f test.hclass`

## 语法

1. 关键字
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
    * new
    * return
    * true
    * false
    * sizeof
1. 语法
    * 支持函数定义
    * 支持函数声明
    * 支持函数调用，要求被调用的函数之前必须声明或者定义过（有点像C++）
    * 支持变量声明、变量初始化。语法参考java
    * 支持任意维度的数组
    * 支持`new`语句创建数组。语法参考java
    * 支持`for`循环、`while`循环、`do while`循环。语法参考java
    * 支持二元运算符`*`、`/`、`%`、`+`、`-`、`<<`、`>>`、`>>>`、`&`、`^`、`|`，目前仅支持char、int、long类型
    * 支持前置、后置递增递减`--`、`++`，仅支持int类型
    * 支持一元运算符`+`、`-`、`~`、`!`
    * 支持条件语句，语法参考java
    * 支持比较运算符`==`、`!+`、`<`、`<=`、`>`、`>=`
    * 支持sizeof表达式，其作用是求数组长度
    * 支持赋值运算符`=`
    * 支持复合赋值运算符`*=`、`/=`、`%=`、`+=`、`-=`、`<<=`、`>>=`、`>>>=`、`&=`、`^=`、`|=`
    * 支持int类型字面值，包括十进制、十六进制、八进制字面值
    * 支持boolean类型字面值
    * 支持char类型字面值
    * 支持char[]类型字面值（等价于Java中的String）
1. 系统函数
    * `print(boolean)`
    * `print(char)`
    * `print(int)`
    * `print(long)`
    * `print(boolean[])`
    * `print(char[])`
    * `print(int[])`
    * `print(long[])`
    * `println(boolean)`
    * `println(char)`
    * `println(int)`
    * `println(long)`
    * `println(boolean[])`
    * `println(char[])`
    * `println(int[])`
    * `println(long[])`
    * `nextBoolean()`
    * `nextInt(int,int)`
    * `nextInt()`
    * `nextLong()`

1. 一个可执行的.hclass文件必须包含`main(char[][])`方法，这是整个程序执行的入口


# 编译原理

__[编译原理请移步博客](https://liuyehcf.github.io/categories/%E7%BC%96%E8%AF%91/)__

# 编译引擎介绍

`compile-engine-core`模块主要包含以下几个部分

1. 文法定义（仅针对2-型文法以及3-型文法）
    * `Symbol`：文法符号，包括终结符和非终结符
    * `SymbolString`：文法符号串
    * `PrimaryProduction`：产生式
    * `Production`：具有相同左部的产生式集合
    * `Grammar`：文法，包含了文法开始符号，以及一系列产生式
1. regex grammar（rg），即正则表达式的实现
    * 实现了NFA自动机（Nfa）与DFA（Dfa）自动机
    * 其中NFA自动机支持捕获组，DFA自动机尚不支持捕获组
1. context-free grammar（cfg），即上下文无关文法
    * LL1自动机
    * LR0自动机
    * SLR自动机
    * LR1自动机
    * LALR自动机
    * 就解析能力而言，LR1=LALR>SLR>LL1，SLR>LR0
    * 就状态数量而言，LALR<LR1
    * 就错误分析能力，LALR<LR1
1. 词法分析器（DefaultLexicalAnalyzer）
    * 支持普通词素，即全量匹配
    * 正则表达式词素
    * 自定义匹配过程

编译引擎主要封装了词法分析、语法分析的过程，可以为任意无二义性CFG语言生成状态自动机

# hua语言

HuaCompiler继承了LALR分析器，其文法定义参照[Java BNF定义](http://www.daimi.au.dk/dRegAut/JavaBNF.html)
1. hua属于面向过程的语言，不支持类的定义
1. 基本语法与java十分相似
1. 由于面向过程，因此增加一个运算符`sizeof`用于计算数组大小

## `compile-engine-hua`模块介绍

1. 文法定义，包路径为`org.liuyehcf.compile.engine.hua.compile.definition`
1. 与文法定义相关的语义动作，包路径为`org.liuyehcf.compile.engine.hua.compile.definition.semantic`
1. 字节码定义，这里的字节码大部分与java对标，且含义完全一致。部分有区别的指令如下
    * 加载int、boolean、char类型的常量，统一用iconst指令，与java有所区别
    * invokestatic指令，用于进行方法调用
    * sizeof指令，用于计算数组大小
1. 运行环境，这部分比较简单，主要就是能够执行编译后的指令序列即可
    * 内存管理，一开始分配一定大小的内存空间（byte数组），没有垃圾回收机制
    * 系统方法，封装了Java的部分能力，主要是标准输STD OUT
    * 与Java类似，每个方法执行过程中，会有一个操作数栈
    * 数组的内存在堆中分配，其他变量在栈中分配
    * 对数组的地址进行封装，记录数组的大小
1. 命令行
    * 利用三方库`commons-cli`

## 实现难点

1. 继承属性的传递
    * 当前语法树节点，需要向未来的语法树节点传递一些属性值
    * 涉及一个特殊的数据结构即可，参考`FutureSyntaxNodeStack`
1. 布尔表达式的翻译
    * 正向/反向的转移指令。在识别布尔表达式的时候，填入正向的转移指令类型。在插入转移指令时，设置正向或者反向
        * __所谓正向就是不成立时需要跳转，反向就是成立时需要跳转__
        * 例如`if(<bool expression>)`、`while(<bool expression>)`、`for(;<bool expression>;)`就是正向
        * `do {} while(<bool expression>)`就是反向
    * 布尔表达式回填，生成转移指令时，并不知道跳转的代码段起始位置在哪里，需要进行到特定的位置进行回填
        * 回填类型包括，`TRUE`回填、`FALSE`回填、`NEXT`回填。其中NEXT回填仅出现在`if then else`语句中
    * 布尔表达式取反
        * 需要将回填的`TRUE`、`FALSE`转移指令进行交换
        * 需要将转移指令的类型取反
1. 关于词法分析器
    * 要注意`<=`的优先级必须比`<`高，其他类似
    * 字符、字符串、整数字面值目前需要作为整体来扫描，利用词法分析器的自定义处理流程接口。否则会出现如下问题：
        * 如果字符串拆分成单个字符来扫描，那么将无法区分字符串中的空白与非字符串区域的空白
    * 词法分析器构建详见`GrammarDefinition`

## `if then`语句布尔值回填

`if then`语句可以表示为如下形式

`if(<condition expression>) { <statements for true> } <other statements>` 

1. 当`<condition expression>`为真时，接着执行`<statements for true>`处的指令
1. 当`<condition expression>`为假时，需要跳转到`<other statements>`的起始位置，并继续执行。此时就需要插入一条控制转移指令`ifeq`，称为`FALSE型回填`，逻辑为正向的
    * `ifeq`指令的含义是指，当`<condition expression>`的值等于0（即false）时，跳转到目标代码处
    * 我们在添加`ifeq`指令时，并不知道需要跳转到哪里（字节码偏移量）去执行，只有分析到`<other statements>`的位置时，才能对刚才的`ifeq`指令回填转移的代码偏移量

## `if then else`语句布尔值回填

`if then else`语句可以表示为如下形式

`if(<condition expression>) { <statements for true> } else { <statements for false> } <other statements>` 

1. 当`<condition expression>`为真时，接着执行`<statements for true>`处的指令
1. 当`<condition expression>`为假时，需要跳转到`<statements for false>`的起始位置，并继续执行。此时就需要插入一条控制转移指令`ifeq`，称为`FALSE型回填`，逻辑为正向的
1. 当`<statements for true>`执行完毕后，需要跳转到`<other statements>`的起始位置，并继续执行。此时就需要插入一条控制转移指令`goto`，称为`NEXT型回填`

## `while`语句布尔值回填

`while`语句可以表示为如下形式

`while(<condition expression>) { <statements for true> } <other statements>`

1. 当`<condition expression>`为真时，接着执行`<statements for true>`处的指令
1. 当`<condition expression>`为假时，需要跳转到`<other statements>`的起始位置，并继续执行。此时就需要插入一条控制转移指令`ifeq`，称为`FALSE型回填`，逻辑为正向的
1. 当`<statements for true>`执行完毕后，需要跳转到`<condition expression>`的起始位置，并继续执行。此时就需要插入一条控制转移指令`goto`，称为`NEXT型回填`

## `do while`语句布尔值回填

`do while`语句可以表示为如下形式

`do { <loop statements> } while(<condition expression>); <other statements>`

1. 当`<condition expression>`为真时，需要跳转到`<loop statements>`的起始位置，并继续执行。此时就需要插入一条控制转移指令`ifne`，称为`TRUE型回填`，逻辑为反向的

## `&&`语句布尔值回填

逻辑与语句可以表示为如下形式

`<condition expression1> && <condition expression2> <other statements>`

1. 当`<condition expression1>`为真时，接着执行`<condition expression2>`处的指令
1. 当`<condition expression1>`为假时，需要跳转到`<other statements>`的起始位置，并继续执行。此时就需要插入一条控制转移指令`ifeq`，称为`FALSE型回填`，逻辑为正向的

## `||`语句布尔值回填

逻辑或语句可以表示为如下形式

`<condition expression1> || <condition expression2> <other statements>`

1. 当`<condition expression1>`为真时，需要跳转到`<other statements>`的起始位置，并继续执行。此时就需要插入一条控制转移指令`ifne`，称为`TRUE型回填`，逻辑为反向的
1. 当`<condition expression1>`为假时，接着执行`<condition expression2>`处的指令

# 示例

## 编写源码

我们用hua语言实现一下快速排序，文件名为`test.hua`，源码内容如下

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

## 编译

执行`huac -f test.hua`，在当前路径下生成`test.hclass`

第一次编译时需要生成状态自动机，会比较慢，大约10s左右

第一次执行过后，会在`HUA_PATH`路径下生成`compiler.obj`文件，该文件是编译器状态自动机序列化之后的内容。之后运行将会从该文件中读取状态自动机，因此速度大幅度提升

## 运行

执行`hua -f test.hclass`，运行程序，输出如下

```
before quickSort:

[55, 88, 66, 76, 56, 75, 99, 82, 58, 76, 78, 62, 91, 92, 56, 51, 89, 95, 89, 55, 99, 75, 71, 62, 74, 96, 61, 71, 93, 89, 92, 95, 92, 94, 84, 79, 76, 72, 75, 90, 86, 84, 63, 98, 66, 95, 67, 63, 57, 75, 86, 74, 70, 62, 70, 95, 96, 66, 74, 96, 99, 83, 69, 66, 65, 80, 63, 69, 97, 76, 61, 80, 53, 91, 98, 79, 67, 77, 50, 99, 52, 51, 72, 52, 98, 71, 59, 66, 55, 89, 98, 83, 57, 85, 57, 84, 80, 99, 61, 96, 57, 54, 63, 96, 82, 97, 78, 63, 60, 98, 55, 71, 92, 73, 54, 88, 77, 92, 63, 58, 50, 52, 96, 86, 84, 75, 92, 64, 88, 54, 59, 85, 86, 91, 87, 88, 96, 99, 50, 83, 64, 87, 52, 94, 53, 55, 73, 85, 77, 85, 86, 95, 85, 93, 75, 92, 88, 50, 80, 76, 67, 97, 54, 60, 50, 81, 97, 67, 83, 96, 66, 93, 69, 86, 81, 85, 59, 74, 97, 50, 90, 55, 90, 64, 97, 82, 62, 76, 88, 57, 74, 77, 83, 80, 92, 52, 81, 58, 95, 51]
after quickSort:

[50, 50, 50, 50, 50, 50, 51, 51, 51, 52, 52, 52, 52, 52, 53, 53, 54, 54, 54, 54, 55, 55, 55, 55, 55, 55, 56, 56, 57, 57, 57, 57, 57, 58, 58, 58, 59, 59, 59, 60, 60, 61, 61, 61, 62, 62, 62, 62, 63, 63, 63, 63, 63, 63, 64, 64, 64, 65, 66, 66, 66, 66, 66, 66, 67, 67, 67, 67, 69, 69, 69, 70, 70, 71, 71, 71, 71, 72, 72, 73, 73, 74, 74, 74, 74, 74, 75, 75, 75, 75, 75, 75, 76, 76, 76, 76, 76, 76, 77, 77, 77, 77, 78, 78, 79, 79, 80, 80, 80, 80, 80, 81, 81, 81, 82, 82, 82, 83, 83, 83, 83, 83, 84, 84, 84, 84, 85, 85, 85, 85, 85, 85, 86, 86, 86, 86, 86, 86, 87, 87, 88, 88, 88, 88, 88, 88, 89, 89, 89, 89, 90, 90, 90, 91, 91, 91, 92, 92, 92, 92, 92, 92, 92, 92, 93, 93, 93, 94, 94, 95, 95, 95, 95, 95, 95, 96, 96, 96, 96, 96, 96, 96, 96, 97, 97, 97, 97, 97, 97, 98, 98, 98, 98, 98, 99, 99, 99, 99, 99, 99]
```

## 查看编译后生成的字节码文件内容

执行`huap -f test.hclass`，得到如下输出

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