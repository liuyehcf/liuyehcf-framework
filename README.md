# 使用

1. 下载demo文件夹
1. 设置环境变量`HUA_PATH`，其值为`demo/lib`文件夹的绝对路径
1. 编写.hua源文件，例如test.hua
1. 编译，执行命令`huac -f test.hua`，会在当前路径下生成同名的`.hclass`文件
1. 运行，执行命令`hua -f test.hclass`
1. 查看代码信息，执行命令`huap -f test.hclass`

## 语法

1. 关键字
    * if
    * else
    * while
    * for
    * do
    * void
    * int
    * boolean
    * char
    * new
    * return
    * true
    * false
    * sizeof
1. 语法
    * 支持函数定义
    * 支持变量声明、变量初始化。语法参考java
    * 支持任意维度的数组
    * 支持new语句创建数组。语法参考java
    * 支持for循环、while循环、do while循环。语法参考java
    * 支持二元运算符`*`、`/`、`%`、`+`、`-`、`<<`、`>>`、`>>>`、`&`、`^`、`|`，目前仅支持int类型
    * 支持前置、后置递增递减`--`、`++`
    * 支持条件语句，语法参考java
    * 支持sizeof表达式，其作用是求数组长度
    * 支持赋值运算符`=`
    * 支持复合赋值运算符`*=`、`/=`、`%=`、`+=`、`-=`、`<<=`、`>>=`、`>>>=`、`&=`、`^=`、`|=`
    * 支持int类型字面值
    * 支持boolean类型字面值
    * 支持char类型字面值
    * 支持char[]类型字面值（等价于Java中的String）
1. 系统函数
    * print(int)
    * print(char)
    * print(boolean)
    * print(int[])
    * print(char[])
    * print(boolean[])
    * println(int)
    * println(char)
    * println(boolean)
    * println(int[])
    * println(char[])
    * println(boolean[])
    * nextInt(int,int)
    * nextInt()
    * nextBoolean()
1. 一个可执行的.hclass文件必须包含`main(char[][])`方法，这是整个程序执行的入口

# 编译原理

__[编译原理请移步博客](https://liuyehcf.github.io/categories/%E7%BC%96%E8%AF%91/)__

## 编译引擎介绍

`compile-engine-core`主要包含以下几个部分

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
    * 该词法分析器实现比较简单

编译引擎主要封装了词法分析、语法分析的过程，可以为任意无二义性CFG语言生成状态自动机

## hua语言

HuaCompiler继承了LALR分析器，其文法定义参照[Java BNF定义](http://www.daimi.au.dk/dRegAut/JavaBNF.html)

# 示例

例如编写源文件`test.hua`，源文件的内容是冒泡排序、插入排序以及快速排序，源码内容如下：

```
void printSplit(){
	println("\n-----------------------------\n");
}

void exchange(int[] nums, int i, int j) {
	int temp = nums[i];
	nums[i] = nums[j];
	nums[j] = temp;
}

void compareAndExchange(int[] nums, int left, int right) {
    if (nums[left] > nums[right]) {
        exchange(nums, left, right);
    }
}

void bubbleSort(int[] nums) {
	int size=sizeof nums;
    for (int i = 0; i < size; i++) {
        for (int j = size - 1; j > i; j--) {
            compareAndExchange(nums, j - 1, j);
        }
    }
}

void testBubbleSort(){
	int[] nums=new int[200];

	for(int i=0;i<200;i++){
		nums[i]=nextInt(50,100);
	}

	println("before bubbleSort:\n");
	println(nums);

	bubbleSort(nums);

	println("after bubbleSort:\n");
	println(nums);

	printSplit();
}


void insertSort(int[] nums) {
	int size=sizeof nums;
    for (int i = 1; i < size; i++) {
        int pivot = nums[i];
        int j = i - 1;
        while (j >= 0 && nums[j] > pivot) {
            nums[j + 1] = nums[j];
            j--;
        }
        nums[j + 1] = pivot;
    }
}

void testInsertSort(){
	int[] nums=new int[200];

	for(int i=0;i<200;i++){
		nums[i]=nextInt(50,100);
	}

	println("before insertSort:\n");
	println(nums);

	insertSort(nums);

	println("after insertSort:\n");
	println(nums);

	printSplit();
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

void quickSort(int[] nums, int lo, int hi) {
	if (lo < hi) {
		int mid = partition(nums, lo, hi);
		quickSort(nums, lo, mid - 1);
		quickSort(nums, mid + 1, hi);
	}
}

void quickSort(int[] nums) {
	quickSort(nums, 0, sizeof nums -1);
}

void testQuickSort(){
	int[] nums=new int[200];

	for(int i=0;i<200;i++){
		nums[i]=nextInt(50,100);
	}

	println("before quickSort:\n");
	println(nums);

	quickSort(nums);

	println("after quickSort:\n");
	println(nums);

	printSplit();
}


void main(char[][] args){
	testBubbleSort();
	testInsertSort();
	testQuickSort();
}
```

执行`huac -f test.hua`，在当前路径下生成`test.hclass`

执行`huap -f test.hclass`，得到如下输出

```
Compiled from "Huap.java"

Constant pool:
	#0  =  print(int)
	#1  =  print(char)
	#2  =  print(boolean)
	#3  =  print(int[])
	#4  =  print(char[])
	#5  =  print(boolean[])
	#6  =  println(int)
	#7  =  println(char)
	#8  =  println(boolean)
	#9  =  println(int[])
	#10 =  println(char[])
	#11 =  println(boolean[])
	#12 =  nextInt(int,int)
	#13 =  nextInt()
	#14 =  nextBoolean()
	#15 =  printSplit()
	#16 =
-----------------------------

	#17 =  exchange(int[],int,int)
	#18 =  compareAndExchange(int[],int,int)
	#19 =  bubbleSort(int[])
	#20 =  testBubbleSort()
	#21 =  before bubbleSort:

	#22 =  after bubbleSort:

	#23 =  insertSort(int[])
	#24 =  testInsertSort()
	#25 =  before insertSort:

	#26 =  after insertSort:

	#27 =  partition(int[],int,int)
	#28 =  quickSort(int[],int,int)
	#29 =  quickSort(int[])
	#30 =  testQuickSort()
	#31 =  before quickSort:

	#32 =  after quickSort:

	#33 =  main(char[][])

printSplit()
	Return type: void
	Param type:
	Code:
		0   :  _ldc
		1   :  _invokestatic #10
		2   :  _return

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

compareAndExchange(int[],int,int)
	Return type: void
	Param type: int[], int, int
	Code:
		0   :  _aload 0
		1   :  _iload 1
		2   :  _iaload
		3   :  _aload 0
		4   :  _iload 2
		5   :  _iaload
		6   :  _if_icmple 11
		7   :  _aload 0
		8   :  _iload 1
		9   :  _iload 2
		10  :  _invokestatic #17
		11  :  _return

bubbleSort(int[])
	Return type: void
	Param type: int[]
	Code:
		0   :  _sizeof
		1   :  _istore 1
		2   :  _iconst 0
		3   :  _istore 2
		4   :  _iload 2
		5   :  _iload 1
		6   :  _if_icmpge 24
		7   :  _iload 1
		8   :  _iconst 1
		9   :  _isub
		10  :  _istore 3
		11  :  _iload 3
		12  :  _iload 2
		13  :  _if_icmple 22
		14  :  _aload 0
		15  :  _iload 3
		16  :  _iconst 1
		17  :  _isub
		18  :  _iload 3
		19  :  _invokestatic #18
		20  :  _iinc 3, -1
		21  :  _goto 11
		22  :  _iinc 2, 1
		23  :  _goto 4
		24  :  _return

testBubbleSort()
	Return type: void
	Param type:
	Code:
		0   :  _iconst 200
		1   :  _newarray int
		2   :  _astore 0
		3   :  _iconst 0
		4   :  _istore 1
		5   :  _iload 1
		6   :  _iconst 200
		7   :  _if_icmpge 16
		8   :  _aload 0
		9   :  _iload 1
		10  :  _iconst 50
		11  :  _iconst 100
		12  :  _invokestatic #12
		13  :  _iastore
		14  :  _iinc 1, 1
		15  :  _goto 5
		16  :  _ldc
		17  :  _invokestatic #10
		18  :  _aload 0
		19  :  _invokestatic #9
		20  :  _aload 0
		21  :  _invokestatic #19
		22  :  _ldc
		23  :  _invokestatic #10
		24  :  _aload 0
		25  :  _invokestatic #9
		26  :  _invokestatic #15
		27  :  _return

insertSort(int[])
	Return type: void
	Param type: int[]
	Code:
		0   :  _sizeof
		1   :  _istore 1
		2   :  _iconst 1
		3   :  _istore 2
		4   :  _iload 2
		5   :  _iload 1
		6   :  _if_icmpge 41
		7   :  _aload 0
		8   :  _iload 2
		9   :  _iaload
		10  :  _istore 3
		11  :  _iload 2
		12  :  _iconst 1
		13  :  _isub
		14  :  _istore 4
		15  :  _iload 4
		16  :  _iconst 0
		17  :  _if_icmplt 33
		18  :  _aload 0
		19  :  _iload 4
		20  :  _iaload
		21  :  _iload 3
		22  :  _if_icmple 33
		23  :  _aload 0
		24  :  _iload 4
		25  :  _iconst 1
		26  :  _iadd
		27  :  _aload 0
		28  :  _iload 4
		29  :  _iaload
		30  :  _iastore
		31  :  _iinc 4, -1
		32  :  _goto 15
		33  :  _aload 0
		34  :  _iload 4
		35  :  _iconst 1
		36  :  _iadd
		37  :  _iload 3
		38  :  _iastore
		39  :  _iinc 2, 1
		40  :  _goto 4
		41  :  _return

testInsertSort()
	Return type: void
	Param type:
	Code:
		0   :  _iconst 200
		1   :  _newarray int
		2   :  _astore 0
		3   :  _iconst 0
		4   :  _istore 1
		5   :  _iload 1
		6   :  _iconst 200
		7   :  _if_icmpge 16
		8   :  _aload 0
		9   :  _iload 1
		10  :  _iconst 50
		11  :  _iconst 100
		12  :  _invokestatic #12
		13  :  _iastore
		14  :  _iinc 1, 1
		15  :  _goto 5
		16  :  _ldc
		17  :  _invokestatic #10
		18  :  _aload 0
		19  :  _invokestatic #9
		20  :  _aload 0
		21  :  _invokestatic #23
		22  :  _ldc
		23  :  _invokestatic #10
		24  :  _aload 0
		25  :  _invokestatic #9
		26  :  _invokestatic #15
		27  :  _return

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
		22  :  _invokestatic #17
		23  :  _iinc 5, 1
		24  :  _goto 10
		25  :  _aload 0
		26  :  _iinc 3, 1
		27  :  _iload 3
		28  :  _iload 2
		29  :  _invokestatic #17
		30  :  _iload 3
		31  :  _ireturn

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
		6   :  _invokestatic #27
		7   :  _istore 3
		8   :  _aload 0
		9   :  _iload 1
		10  :  _iload 3
		11  :  _iconst 1
		12  :  _isub
		13  :  _invokestatic #28
		14  :  _aload 0
		15  :  _iload 3
		16  :  _iconst 1
		17  :  _iadd
		18  :  _iload 2
		19  :  _invokestatic #28
		20  :  _return

quickSort(int[])
	Return type: void
	Param type: int[]
	Code:
		0   :  _aload 0
		1   :  _iconst 0
		2   :  _sizeof
		3   :  _iconst 1
		4   :  _isub
		5   :  _invokestatic #28
		6   :  _return

testQuickSort()
	Return type: void
	Param type:
	Code:
		0   :  _iconst 200
		1   :  _newarray int
		2   :  _astore 0
		3   :  _iconst 0
		4   :  _istore 1
		5   :  _iload 1
		6   :  _iconst 200
		7   :  _if_icmpge 16
		8   :  _aload 0
		9   :  _iload 1
		10  :  _iconst 50
		11  :  _iconst 100
		12  :  _invokestatic #12
		13  :  _iastore
		14  :  _iinc 1, 1
		15  :  _goto 5
		16  :  _ldc
		17  :  _invokestatic #10
		18  :  _aload 0
		19  :  _invokestatic #9
		20  :  _aload 0
		21  :  _invokestatic #29
		22  :  _ldc
		23  :  _invokestatic #10
		24  :  _aload 0
		25  :  _invokestatic #9
		26  :  _invokestatic #15
		27  :  _return

main(char[][])
	Return type: void
	Param type: char[][]
	Code:
		0   :  _invokestatic #20
		1   :  _invokestatic #24
		2   :  _invokestatic #30
		3   :  _return
```

执行`hua -f test.hclass`，运行程序，输出如下

```
before bubbleSort:

[85, 84, 61, 79, 99, 70, 73, 78, 81, 91, 62, 98, 76, 85, 83, 77, 77, 65, 55, 67, 90, 80, 62, 55, 72, 87, 65, 59, 65, 81, 55, 55, 83, 71, 68, 51, 81, 62, 90, 65, 66, 71, 54, 62, 79, 75, 69, 61, 64, 67, 57, 80, 59, 61, 91, 61, 89, 87, 92, 60, 94, 74, 58, 69, 55, 72, 60, 96, 60, 95, 69, 68, 79, 54, 51, 62, 52, 89, 96, 86, 59, 71, 52, 76, 55, 91, 59, 73, 76, 86, 94, 87, 79, 70, 73, 87, 74, 92, 71, 63, 90, 71, 80, 83, 93, 61, 83, 83, 62, 95, 71, 83, 68, 75, 76, 57, 56, 67, 57, 74, 58, 65, 55, 92, 64, 67, 54, 64, 51, 73, 63, 84, 77, 84, 53, 62, 82, 64, 58, 96, 83, 95, 54, 84, 94, 65, 52, 67, 98, 67, 57, 87, 70, 90, 88, 59, 99, 62, 51, 99, 81, 93, 70, 88, 89, 69, 61, 54, 67, 55, 59, 89, 85, 76, 55, 95, 95, 50, 89, 57, 65, 80, 97, 51, 95, 69, 73, 81, 60, 58, 99, 68, 74, 97, 95, 97, 82, 93, 81, 97]
after bubbleSort:

[50, 51, 51, 51, 51, 51, 52, 52, 52, 53, 54, 54, 54, 54, 54, 55, 55, 55, 55, 55, 55, 55, 55, 55, 56, 57, 57, 57, 57, 57, 58, 58, 58, 58, 59, 59, 59, 59, 59, 59, 60, 60, 60, 60, 61, 61, 61, 61, 61, 61, 62, 62, 62, 62, 62, 62, 62, 62, 63, 63, 64, 64, 64, 64, 65, 65, 65, 65, 65, 65, 65, 66, 67, 67, 67, 67, 67, 67, 67, 68, 68, 68, 68, 69, 69, 69, 69, 69, 70, 70, 70, 70, 71, 71, 71, 71, 71, 71, 72, 72, 73, 73, 73, 73, 73, 74, 74, 74, 74, 75, 75, 76, 76, 76, 76, 76, 77, 77, 77, 78, 79, 79, 79, 79, 80, 80, 80, 80, 81, 81, 81, 81, 81, 81, 82, 82, 83, 83, 83, 83, 83, 83, 83, 84, 84, 84, 84, 85, 85, 85, 86, 86, 87, 87, 87, 87, 87, 88, 88, 89, 89, 89, 89, 89, 90, 90, 90, 90, 91, 91, 91, 92, 92, 92, 93, 93, 93, 94, 94, 94, 95, 95, 95, 95, 95, 95, 95, 96, 96, 96, 97, 97, 97, 97, 98, 98, 99, 99, 99, 99]

-----------------------------

before insertSort:

[78, 70, 87, 58, 52, 92, 92, 89, 54, 74, 59, 57, 79, 60, 56, 98, 51, 73, 80, 51, 77, 91, 59, 89, 84, 75, 62, 95, 53, 52, 78, 87, 84, 97, 78, 83, 68, 92, 83, 65, 56, 54, 58, 51, 53, 53, 83, 65, 94, 75, 57, 67, 86, 54, 90, 69, 76, 89, 63, 56, 96, 74, 89, 99, 72, 61, 52, 75, 88, 83, 94, 64, 78, 51, 59, 69, 87, 94, 57, 75, 93, 93, 84, 96, 81, 67, 84, 71, 88, 89, 56, 64, 54, 69, 58, 80, 63, 74, 93, 73, 83, 88, 92, 73, 58, 93, 50, 82, 94, 86, 80, 52, 83, 54, 50, 69, 94, 71, 95, 78, 88, 71, 66, 88, 72, 80, 71, 89, 87, 78, 60, 76, 63, 92, 58, 80, 56, 82, 67, 54, 67, 85, 84, 99, 77, 78, 94, 65, 78, 50, 56, 79, 65, 97, 82, 77, 76, 88, 98, 65, 60, 59, 86, 51, 67, 97, 73, 58, 75, 55, 97, 73, 60, 82, 60, 55, 95, 98, 69, 92, 65, 84, 97, 82, 77, 85, 62, 89, 77, 83, 92, 98, 83, 65, 99, 84, 99, 86, 77, 65]
after insertSort:

[50, 50, 50, 51, 51, 51, 51, 51, 52, 52, 52, 52, 53, 53, 53, 54, 54, 54, 54, 54, 54, 55, 55, 56, 56, 56, 56, 56, 56, 57, 57, 57, 58, 58, 58, 58, 58, 58, 59, 59, 59, 59, 60, 60, 60, 60, 60, 61, 62, 62, 63, 63, 63, 64, 64, 65, 65, 65, 65, 65, 65, 65, 65, 66, 67, 67, 67, 67, 67, 68, 69, 69, 69, 69, 69, 70, 71, 71, 71, 71, 72, 72, 73, 73, 73, 73, 73, 74, 74, 74, 75, 75, 75, 75, 75, 76, 76, 76, 77, 77, 77, 77, 77, 77, 78, 78, 78, 78, 78, 78, 78, 78, 79, 79, 80, 80, 80, 80, 80, 81, 82, 82, 82, 82, 82, 83, 83, 83, 83, 83, 83, 83, 83, 84, 84, 84, 84, 84, 84, 84, 85, 85, 86, 86, 86, 86, 87, 87, 87, 87, 88, 88, 88, 88, 88, 88, 89, 89, 89, 89, 89, 89, 89, 90, 91, 92, 92, 92, 92, 92, 92, 92, 93, 93, 93, 93, 94, 94, 94, 94, 94, 94, 95, 95, 95, 96, 96, 97, 97, 97, 97, 97, 98, 98, 98, 98, 99, 99, 99, 99]

-----------------------------

before quickSort:

[88, 50, 51, 96, 74, 76, 75, 78, 82, 86, 77, 98, 72, 88, 71, 61, 65, 68, 75, 82, 88, 51, 86, 62, 87, 91, 84, 69, 74, 62, 50, 98, 71, 67, 81, 57, 66, 58, 99, 53, 69, 51, 63, 89, 95, 60, 61, 93, 95, 76, 59, 74, 73, 53, 91, 56, 66, 55, 61, 82, 98, 58, 74, 98, 78, 74, 77, 97, 88, 77, 54, 63, 71, 72, 92, 62, 51, 79, 93, 58, 62, 75, 62, 93, 98, 85, 55, 77, 78, 59, 68, 73, 72, 95, 84, 83, 98, 57, 75, 62, 58, 68, 70, 64, 56, 99, 83, 53, 74, 69, 96, 84, 54, 56, 51, 66, 69, 86, 70, 74, 78, 89, 50, 84, 78, 79, 61, 80, 80, 52, 52, 64, 78, 69, 59, 87, 69, 54, 61, 56, 58, 60, 56, 67, 98, 54, 57, 57, 88, 63, 83, 92, 99, 79, 51, 63, 76, 53, 84, 60, 53, 70, 89, 51, 74, 87, 57, 67, 86, 96, 51, 50, 81, 99, 70, 68, 61, 65, 82, 73, 71, 68, 65, 59, 52, 96, 75, 69, 58, 58, 56, 50, 63, 73, 78, 79, 60, 71, 89, 53]
after quickSort:

[50, 50, 50, 50, 50, 51, 51, 51, 51, 51, 51, 51, 51, 52, 52, 52, 53, 53, 53, 53, 53, 53, 54, 54, 54, 54, 55, 55, 56, 56, 56, 56, 56, 56, 57, 57, 57, 57, 57, 58, 58, 58, 58, 58, 58, 58, 59, 59, 59, 59, 60, 60, 60, 60, 61, 61, 61, 61, 61, 61, 62, 62, 62, 62, 62, 62, 63, 63, 63, 63, 63, 64, 64, 65, 65, 65, 66, 66, 66, 67, 67, 67, 68, 68, 68, 68, 68, 69, 69, 69, 69, 69, 69, 69, 70, 70, 70, 70, 71, 71, 71, 71, 71, 72, 72, 72, 73, 73, 73, 73, 74, 74, 74, 74, 74, 74, 74, 74, 75, 75, 75, 75, 75, 76, 76, 76, 77, 77, 77, 77, 78, 78, 78, 78, 78, 78, 78, 79, 79, 79, 79, 80, 80, 81, 81, 82, 82, 82, 82, 83, 83, 83, 84, 84, 84, 84, 84, 85, 86, 86, 86, 86, 87, 87, 87, 88, 88, 88, 88, 88, 89, 89, 89, 89, 91, 91, 92, 92, 93, 93, 93, 95, 95, 95, 96, 96, 96, 96, 97, 98, 98, 98, 98, 98, 98, 98, 99, 99, 99, 99]

-----------------------------

```