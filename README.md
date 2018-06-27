# 编译原理

该项目的终极目标是 DIY 一个简单的语言

## 词法分析

__正则表达式引擎__

* base package: `org.liuyehcf.grammar.rg`
* 包括nfa自动机以及dfa自动机
* 支持以下正则表达式通配符: `.`、`?`、`*`、`+`、`|`
* 支持量词`{}`
* 支持部分转义，包括`\d`、`\D`、`\w`、`\W`、`\s`、`\S`
* 支持`[]`
* nfa自动机支持捕获组（nfa转dfa时，捕获组信息会丢失，因此dfa自动机尚不支持捕获组。目前还没有很好的解决方法）

## 语法分析__

实现如下分析方法

* base package: `org.liuyehcf.grammar.cfg`
1. __LL1分析法__
1. __LR0分析法__
1. __SLR分析法__
1. __LR1分析法__
1. __LALR分析法__

```flow
st=>start: Start
op1=>operation: Calculate first set
op2=>operation: Calculate follow set
cond=>condition: Yes or No?
e=>end

st->op->cond
cond(yes)->e
cond(no)->op
```

## 语法制导翻译

未完待续

## 中间代码生成

未完待续

## 存储分配

### 栈内存

1. 根据方法中最大的有效变量数量来分配栈内存

### 堆内存

1. 运行前分配一定大小的堆内存
1. 不进行垃圾回收

## 代码优化

1. 跳转代码优化，例如
```
2  :ifge 10
...
10 :goto 20

优化后变为
2  :ifge 20
...
10 :goto 20
```
1. 删除不可达代码 

## 代码生成

类似于Java字节码
