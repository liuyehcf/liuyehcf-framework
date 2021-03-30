   * [1 Overview](#1-overview)
   * [2 基础能力](#2-基础能力)
      * [2.1 核心接口介绍](#21-核心接口介绍)
      * [2.2 LinkType](#22-linktype)
      * [2.3 Node](#23-node)
      * [2.4 Executable](#24-executable)
         * [2.4.1 Action](#241-action)
         * [2.4.2 Condition](#242-condition)
         * [2.4.3 Listener](#243-listener)
         * [2.4.4 属性注入](#244-属性注入)
         * [2.4.5 线程池隔离](#245-线程池隔离)
      * [2.5 Gateway](#25-gateway)
         * [2.5.1 ExclusiveGateway](#251-exclusivegateway)
         * [2.5.2 JoinGateway](#252-joingateway)
      * [2.6 Sub Flow](#26-sub-flow)
      * [2.7 异常](#27-异常)
   * [3 流描述语言](#3-流描述语言)
      * [3.1 级联](#31-级联)
      * [3.2 并联](#32-并联)
      * [3.3 if语句](#33-if语句)
      * [3.4 if else语句](#34-if-else语句)
      * [3.5 select语句](#35-select语句)
      * [3.6 join语句](#36-join语句)
      * [3.7 join then语句](#37-join-then语句)
      * [3.8 sub语句](#38-sub语句)
      * [3.9 sub then语句](#39-sub-then语句)
      * [3.10 sub then else语句](#310-sub-then-else语句)
      * [3.11 监听](#311-监听)
      * [3.12 节点参数](#312-节点参数)
   * [4 异步](#4-异步)
      * [4.1 Promise](#41-promise)
      * [4.2 PromiseListener](#42-promiselistener)
      * [4.3 ExecutionLink的暂停与重启](#43-executionlink的暂停与重启)
   * [5 拦截器](#5-拦截器)
   * [6 数据统计](#6-数据统计)
   * [7 Spring-Boot-Starter](#7-spring-boot-starter)
      * [7.1 配置ActionDelegate](#71-配置actiondelegate)
      * [7.2 配置ConditionDelegate](#72-配置conditiondelegate)
      * [7.3 配置ListenerDelegate](#73-配置listenerdelegate)
      * [7.4 配置DelegateInterceptor](#74-配置delegateinterceptor)
      * [7.5 执行](#75-执行)
   * [8 非Spring环境](#8-非spring环境)

# 1 Overview

1. __丰富的原子能力__
    * Action
    * Condition
    * Gateway
        * JoinGateway
            * hard
            * soft
            * or
        * ExclusiveGateway
    * Listener
        * event
            * before
            * success
            * failure
        * scope
            * node
            * global
    * SubFlow
1. __简洁的流描述语言__
    * 级联
    * 并联
    * 聚合
    * 监听
    * `if`语句
    * `if else`语句
    * `select`语句
    * `join`语句
    * `join then`语句
    * `sub`语句
    * `sub then`语句
    * `sub then else`语句
1. __丰富的机制__
    * Promise
    * PromiseListener
    * DelegateInterceptor
    * DelegateField
    * ExecutionLink的暂停与重启
    * 线程池隔离
    * 异步执行超时设置
1. __集成Spring-Boot-Starter__
1. __数据统计__
    * 执行链路
    * 变量更新记录
    * 节点执行时间
    * attribute

# 2 基础能力

## 2.1 核心接口介绍

![elements](images/elements.png) 

1. `Element`: 定义流中元素的基础能力，包含
    * `id`: 流级别唯一的id
    * `type`: 元素的类型，详见`ElementType`
    * `flow`: 关联流本身
1. `Attachable`: 可依附于某个节点，包含
    * `attachedId`: 被依附的节点id
1. `Executable`: 可执行Java逻辑的元素，包含
    * `name`: 元素名称
    * `argumentNames`: 参数名称
    * `argumentValues`: 参数值
1. `Node`: 定义流中的节点，节点是流中的最小拓扑单元，包含
    * `predecessors`: 前继集合
    * `successors`: 后继集合
    * `listeners`: 监听集合
1. `Conditional`: 流中可以作为条件节点后继的节点，包含
    * `linktype`: 链路类型，包括`NORMAL`、`TRUE`、`FALSE`三种
1. `Listener`: 监听，包含
    * `scope`: 监听级别，包括`global`以及`node`
    * `event`: 事件类型，包括`before`、`success`以及`failure`
1. `Activity`: 该概念的定义，参考`flowable`
1. `Action`: 一个普通的执行节点
1. `Condition`: 一个条件执行节点
1. `Gateway`: 网关节点
1. `ExclusiveGateway`: 独占网关，仅允许后继第一个成立的`Condition`通过
1. `JoinGateway`: 聚合网关，聚合多个执行分支成一个分支
    * `joinMode`: 聚合模式，包括`hard`模式、`soft`模式以及`or`模式
    * `hard`表示当且仅当全部的前继节点都正常执行时，才允许通过
    * `soft`表示当且仅当全部的可达前继节点都正常执行时，才允许通过（换言之，允许不可达支路存在）
    * `or`表示当任意前继节点正常执行时，允许通过
1. `Flow`: 流本身也可以作为节点存在于上一级的流拓扑中（即子流）

## 2.2 LinkType

__在流引擎中，节点与节点之间的连线称为`Link`，其类型包含如下三种__

* `LinkType.TRUE`: true分支
* `LinkType.FALSE`: false分支
* `LinkType.NORMAL`: 默认分支

__其中，`Condition`以及`Flow`与后继节点的连线的类型是`LinkType.TRUE`或`LinkType.FALSE`；其余类型的节点与后继节点的连线的类型是`LinkType.NORMAL`，请参考如下示意图__

![linktype_example](images/linktype_example.png) 

__在下文中，我们把某个节点与其后继节点的连线称为`后继Link`__

## 2.3 Node

__`Node`是流拓扑结构中的最基本元素，而其他元素，例如`Listener`只能依附于`Node`而存在，`Node`包括：__

* `Action`
* `Condition`
* `JoinGateway`
* `ExclusiveGateway`
* `Flow`

## 2.4 Executable

`Executable`是可执行Java代码的元素，包括`Action`、`Condition`以及`Listener`

__注册__

1. 非spring环境下，需要通过`com.github.liuyehcf.framework.flow.engine.FlowEngine`的静态方法进行注册
    * `registerActionDelegateFactory`
    * `registerConditionDelegateFactory`
    * `registerListenerDelegateFactory`
1. 在spring环境下，无需任何配置，流引擎会自动完成注册工作
    * `@Component`，节点名称就是bean的名称
    * `@ActionBean`，该注解默认标记了`@Component`，节点名称通过names指定，可以指定多个别名
    * `@ConditionBean`，该注解默认标记了`@Component`，节点名称通过names指定，可以指定多个别名
    * `@ListenerBean`，该注解默认标记了`@Component`，节点名称通过names指定，可以指定多个别名

__命名__

1. 标志符，例如`actionA`、`action1`、`_action_2`
1. 标志符结合`.`，例如`my.test.condition3`
1. 标志符结合`/`，例如`my/test/listener5`

### 2.4.1 Action

__`Action`是一个可执行Java代码的普通节点，`Action`只允许类型为`LinkType.NORMAL`的后继Link，其继承关系如下__

![action](images/action.png) 

如果我们要创建一个可以执行业务逻辑的`Action`，只需要实现`ActionDelegate`接口

```java
package com.github.liuyehcf.framework.flow.engine.runtime.delegate;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ActionContext;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public interface ActionDelegate extends Delegate {

    /**
     * method invoke when action is reached
     *
     * @param context context of execution
     * @throws Exception exceptions
     */
    void onAction(ActionContext context) throws Exception;
}
```

__其中，`context`可用于__

1. 获取属性值
1. 设置属性值（通过该方法设置属性值，会被引擎记录，作用于聚合逻辑，并且变更信息会保存到执行Trace当中）
1. 获取环境变量（直接操作环境变量进行属性值的设置，不会被引擎记录，慎用）
1. 添加attribute（添加属性值，用于存储业务数据，与环境变量隔离）

### 2.4.2 Condition

__`Condition`是一个可执行Java代码的条件节点，`Condition`只允许类型为`LinkType.TRUE`或`LinkType.FALSE`的后继Link__

![condition](images/condition.png) 

如果我们要创建一个可以执行业务逻辑的`Condition`，只需要实现`ConditionDelegate`接口，__`onCondition`方法的返回值决定了整个流程的走向，若返回`true`则会走`LinkType.TRUE`分支，若返回`false`则会走`LinkType.FALSE`分支__

```java
package com.github.liuyehcf.framework.flow.engine.runtime.delegate;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ConditionContext;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public interface ConditionDelegate extends Delegate {

    /**
     * method invoke when condition is reached
     *
     * @param context context of execution
     * @throws Exception exceptions
     */
    boolean onCondition(ConditionContext context) throws Exception;
}
```

__其中，`context`可用于__

1. 获取属性值
1. 设置属性值（通过该方法设置属性值，会被引擎记录，作用于聚合逻辑，并且变更信息会保存到执行Trace当中）
1. 获取环境变量（直接操作环境变量进行属性值的设置，不会被引擎记录，慎用）
1. 添加attribute（添加属性值，用于存储业务数据，与环境变量隔离）

### 2.4.3 Listener  

__`Listener`不是流拓扑结构中的节点，`Listener`必须依附于节点而存在，包括__

1. `Flow`（`global/node` Scope）
    * 只允许对`Sub Flow`配置`node`的监听
    * 可以对`Flow`或`Sub Flow`配置`global`监听
1. `Activity`（`node` Scope）
    * `Action`
    * `Condition`
1. `Gateway`（`node` Scope）
    * `JoinGateway`
    * `ExclusiveGateway`

__event: Listener触发时机__

1. `before`: 执行链路刚刚到达该`Listener`所依附的节点时触发（此时依附的节点尚未执行相关逻辑）
1. `success`: 该`Listener`所依附的节点正常执行时触发
    * 对于`Action`、`Condition`这类节点，当节点执行未抛出异常时，才会触发`success`监听
    * 对于`JoinGateway`、`ExclusiveGateway`这类节点，由于不包含执行逻辑，意味着不可能抛出异常，因此必然触发`success`监听
1. `failure`: 当依附的节点抛出异常时，会触发该类型的监听
    * 该类型的监听只能用于感知异常情况，但无法处理异常。换言之，在该监听处理完毕之后，该异常仍然会向上层继续抛出
    * 如果在监听的处理逻辑中抛出了异常，那么此时会存在两个异常，一个是依附节点抛出的异常（称为`original-exception`，另一个是监听抛出的异常（称为`listener-exception`），那么最后抛出的异常遵循以下逻辑
        1. 如果监听抛出的异常是`LinkExecutionTerminateException`时，那么最终抛出的异常就是`original-exception`
        1. 否则，抛出的异常就是`listener-exception`

__scope: Listener的范围__

1. `node`: 节点级别的监听，该监听可以依附于`Action`、`Condition`、`Gateway`
1. `global`: 流级别的监听，即仅在流开始前/成功执行/执行异常后触发
    * 如果流存在多个执行分支，那么`success/failure`监听也只会执行一次
    * __例如下面的示意图，如果`actionA`、`actionB`和`actionC`都正常执行，那么该`GlobalSuccessListener`同样只会执行一次__

![listener](images/global_success_example.png)  

![listener](images/listener.png)  

如果我们要创建一个可以执行业务逻辑的`Listener`，只需要实现`ListenerDelegate`接口

```java
package com.github.liuyehcf.framework.flow.engine.runtime.delegate;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ListenerContext;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public interface ListenerDelegate extends Delegate {

    /**
     * method invoke when listener is reached
     *
     * @param context context of execution
     * @throws Exception exceptions
     */
    default void onBefore(ListenerContext context) throws Exception {
        // default implementation
    }

    /**
     * method invoke when bound element' execution succeeded
     *
     * @param context context of execution
     * @throws Exception exceptions
     */
    default void onSuccess(ListenerContext context, Object result) throws Exception {
        // default implementation
    }

    /**
     * method invoke when bound element' execution failed
     *
     * @param context context of execution
     * @throws Exception exceptions
     */
    default void onFailure(ListenerContext context, Throwable cause) throws Exception {
        // default implementation
    }
}
```

__其中，`context`可用于__

1. 获取属性值
1. 设置属性值（通过该方法设置属性值，会被引擎记录，作用于聚合逻辑，并且变更信息会保存到执行Trace当中）
1. 获取环境变量（直接操作环境变量进行属性值的设置，不会被引擎记录，慎用）
1. 添加attribute（添加属性值，用于存储业务数据，与环境变量隔离）

### 2.4.4 属性注入

`ActionDelegate`、`ConditionDelegate`、`ListenerDelegate`都继承了`Delegate`接口，本小结以`Delegate`泛指这三种具体的接口

![delegate](images/delegate.png) 

__如果`Delegate`需要配置一些变量，那么必须使用`DelegateField`，流引擎会自动注入，注入有以下两种方式__

1. `set`方法，优先使用该方式，也推荐使用该方式（不会受Spring-Aop的影响）
1. 字段注入（不推荐该方式，在Spring环境中，如果配置了AOP，那么该方式会失效，字段会注入到包装类）
* __如果节点未指定该参数，那么通过`DelegateField.getValue`方法获取到的值为null，但`DelegateField`本身一定不是null__

```java
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ActionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;

/**
 * @author hechenfeng
 * @date 2019/7/10
 */
public class MyAction implements ActionDelegate {

    private DelegateField arg1;

    // arg2 没有定义set方法，那么注入会走字段反射
    private DelegateField arg2;

    // arg1 定义了set方法，那么注入会优先走set方法
    public void setArg1(DelegateField arg1) {
        this.arg1 = arg1;
    }

    @Override
    public void onAction(ActionContext context) {
        // 直接通过getValue获取实际的参数值
        Object value1 = arg1.getValue();
        Object value2 = arg2.getValue();
        // ...
    }
}
```

### 2.4.5 线程池隔离

当节点的业务逻辑较为复杂时，建议用业务线程池来处理业务逻辑，不要在流引擎的线程池中执行业务逻辑

`ActionDelegate`、`ConditionDelegate`、`ListenerDelegate`可以通过`isAsync`方法设置成异步执行；可以通过`getAsyncTimeout`设置超时时间；可以通过`getAsyncExecutor`设置业务线程池，这些方法都定义在顶层接口`Delegate`中，默认是非异步模式

```Java
package com.github.liuyehcf.framework.flow.engine.runtime.delegate;

import java.util.concurrent.ExecutorService;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public interface Delegate {

    /**
     * whether execution in async mode
     * default is sync mode, which means that it will execute in the thread pool of the FlowEngine
     */
    default boolean isAsync() {
        return false;
    }

    /**
     * thread pool for executing asynchronous logic
     * flowEngine's executor will be used if return value is null
     * <p>
     * invalid when isAsync() is false
     */
    default ExecutorService getAsyncExecutor() {
        return null;
    }

    /**
     * timeout of async execution, the unit is milliseconds
     * non-positive value means wait until execution finished, default is 0
     * <p>
     * if the execution time exceeds the specified time, an interrupt signal is emitted,
     * but the response depends on the business code itself.
     * <p>
     * invalid when isAsync() is false
     */
    default long getAsyncTimeout() {
        return 0;
    }
}
```

## 2.5 Gateway

网关包括独占网关和聚合网关两种，下面分别介绍这两个网关，以及具体的使用场景

### 2.5.1 ExclusiveGateway

对于普通的节点，比如`Action`或者`Condition`，当后继节点有多个的时候，其默认的行为就是并发执行，如下图所示，`node 0`有n个后继节点，分别是`node 1`、`node 2`、...、`node n`。在`node 0`执行完毕后，__会`fork`出n个执行链路继续执行，并且在`fork`之后，每个执行链路独享环境变量，互不干扰__

![default_parallel](images/default_parallel.png)

但有时候，我们希望这n个后继节点，有且仅有一个能够执行，这个时候就需要使用独占网关`ExclusiveGateway`

![exclusiveGateway](images/exclusiveGateway.png) 

__`ExclusiveGateway`的后继节点必须是`Condition`，`ExclusiveGateway`能够保证最多只有一条后继链路得以执行，并且执行的顺序与节点执行的顺序完全一致，如下图所示__。`exclusiveGateway`包含n个后继的`Condition`，分别是`condition 1`、`condition 2`、...、`condition n`，首先`condition 1`会优先执行，如果执行结果为true，那么继续执行`condition 1`的后续节点；如果执行结果为false，那么会执行`condition 2`，以此类推

![exclusive_parallel](images/exclusive_parallel.png)

### 2.5.2 JoinGateway

对于普通的节点，包含且仅包含一个前继节点（开始节点除外），但是`JoinGateway`允许包含多个前继节点，并进行聚合处理，包括

1. 环境变量的聚合，`JoinGateway`会聚合所有前置分支的环境变量，__如果不同分支同时修改了同一个变量的值，那么聚合后，该变量的值可能是任意一个分支的值__
1. 执行链路的聚合，`JoinGateway`会按照执行的时间顺序，聚合各前置分支的trace信息

![joinGateway](images/joinGateway.png) 

`JoinGateway`包含三种聚合模式，`hard`模式、`soft`模式以及`or`模式，其中

1. `hard`模式，当且仅当所有前继节点全部到达时，才允许通过
1. `soft`模式，当且仅当所有`可达`的前继节点全部到达时，才允许通过
1. `or`模式，当`任意`前继到达后，即允许通过，且只允许通过一次

![joinMode](images/joinMode.png) 

* `actionB`在`conditionA`的`TRUE`后继分支上，也就是说，只有`conditionA`的执行结果是true，`actionB`才会执行

__hard模式__

1. 如果`conditionA`的执行结果为true，那么gateway允许通过
1. 如果`conditionB`的执行结果为false，那么`actionB`就是不可达的，因此在`hard`的语义下，gateway不允许通过

__soft模式__

1. 如果`conditionA`的执行结果为true，那么gateway允许通过
1. 如果`conditionB`的执行结果为false，那么`actionB`就是不可达的，因此在`soft`的语义下，所有可达的前继节点（只有`actionA`），gateway允许通过

__or模式__

1. `actionA`或`actionB`中任意一个到达后，该gateway允许通过

## 2.6 Sub Flow

__流引擎提供了`sub flow`的概念，极大地丰富了流编排的灵活度。我们可以将一组节点拓扑放到`sub flow`中，并且很容易能够感知这组节点拓扑的执行结果__

考虑这样一个例子

![sub_flow_joingateway_cmp](images/sub_flow_joingateway_cmp.png)

__在这种场景下，`sub flow`与`or`模式的`JoinGateway`非常相似，但是存在一个明显的区别：`sub flow`可以感知这组节点拓扑的执行情况；`or`模式的`JoinGateway`无法感知这组节点拓扑的执行情况__

* 当`conditionA`、`conditionB`、`conditionC`中至少有一个节点的执行结果为true时，`or`模式的`JoinGateway`才允许通过，此时`actionB`才得以执行；若`conditionA`、`conditionB`、`conditionC`执行结果全部为false时，`actionB`不可达，且无法感知这一结果
* 当`sub flow`执行成功时，`actionB`得以执行；当`sub flow`执行失败时，`actionC`得以执行。可以看到，很容易感知到`sub flow`的执行结果

__如何定义`sub flow`执行成功：当存在任意一条分支成功执行（即任意一个分支到达叶子节点时），就认为`sub flow`执行成功，反之则认为`sub flow`执行失败__

__`sub flow`的监听：我们可以为`sub flow`配置`global`以及`node`级别的`Listener`__

* `global`级别的`Listener`，其行为与流的`global`级别的`Listener`的行为一致
* `node`级别的`Listener`，其行为与普通节点的`Listener`行为一致
* 对于`sub flow`而言，`node`级别的监听与`global`级别的监听是等价的

## 2.7 异常

对于`Executable`，包括`Action`、`Condition`、`Listener`

* 如果在执行时抛出了`LinkExecutionTerminateException`异常，那么该节点的后续节点都会被标记为不可达，流仍然处于正常状态
* 如果在执行时抛出了其他异常，那么流执行进入异常状态，即该流的所有分支所有节点终止执行，且`promise.isFailure`为true

![linkTerminateException](images/link_terminate_exception.png)

![unknownException](images/unknown_exception.png)

# 3 流描述语言

## 3.1 级联

![cascade](images/cascade.png) 

```
{
  actionA() {
    actionB()
  }
}
```

## 3.2 并联

![parallel](images/parallel.png) 

```
{
    actionA(),
    actionB(){
        actionD(),
        actionE()
    },
    actionC()
}
```

## 3.3 `if`语句

![if_1](images/if_1.png) 

```
{
    if(conditionA())
}
```

![if_2](images/if_2.png) 

```
{
    if(conditionA()) {
        actionA()
    }
}
```

![if_3](images/if_3.png) 

```
{
    if(conditionA()){
        actionA(),
        actionB(),
        actionC()
    }
}
```

## 3.4 `if else`语句

![if_else_1](images/if_else_1.png) 

```
{
    if(conditionA()) {
        actionA()
    } else {
        actionB()
    }
}
```

![if_else_2](images/if_else_2.png) 

```
{
    if(conditionA()){
        actionA(),
        actionB()
    } else {
        actionC(),
        actionD()
    }
}
```

## 3.5 `select`语句

![select_1](images/select_1.png) 

```
{
    select {
        if(conditionA()){
            actionA()
        },
        if(conditionB()){
            actionB()
        },
        if(conditionC()){
            actionC()
        }
    }
}
```

## 3.6 `join`语句

__注意__

1. 用`&`来标注需要聚合的节点
1. 用`*`表示`hard`模式

![join_1](images/join_1.png) 

__hard__

```
{
    join & {
        actionA()&,
        actionB(),
        actionC()&
    }
}
```

__soft__

```
{
    join {
        actionA()&,
        actionB(),
        actionC()&
    }
}
```

__or__

```
{
    join | {
        actionA()&,
        actionB(),
        actionC()&
    }
}
```

![join_2](images/join_2.png) 

__hard__

```
{
    join & {
        if(conditionA()){
            actionC()
        } else {
            actionD()&
        },
        actionA()&,
        actionB()&
    }
}
```

__soft__

```
{
    join {
        if(conditionA()){
            actionC()
        } else {
            actionD()&
        },
        actionA()&,
        actionB()&
    }
}
```

__or__

```
{
    join | {
        if(conditionA()){
            actionC()
        } else {
            actionD()&
        },
        actionA()&,
        actionB()&
    }
}
```

## 3.7 `join then`语句

__注意：需要用`&`来标注需要聚合的节点__

![join_then_1](images/join_then_1.png) 

__hard__

```
{
    join & {
        actionA()&,
        actionB(),
        actionC()&
    } then {
        actionD()
    }
}
```

__soft__

```
{
    join {
        actionA()&,
        actionB(),
        actionC()&
    } then {
        actionD()
    }
}
```

__or__

```
{
    join | {
        actionA()&,
        actionB(),
        actionC()&
    } then {
        actionD()
    }
}
```

![join_then_2](images/join_then_2.png) 

__hard__

```
{
    join & {
        if(conditionA()){
            actionC()
        } else {
            actionD()&
        },
        actionA()&,
        actionB()&
    } then {
        actionE()
    }
}
```

__soft__

```
{
    join {
        if(conditionA()){
            actionC()
        } else {
            actionD()&
        },
        actionA()&,
        actionB()&
    } then {
        actionE()
    }
}
```

__or__

```
{
    join | {
        if(conditionA()){
            actionC()
        } else {
            actionD()&
        },
        actionA()&,
        actionB()&
    } then {
        actionE()
    }
}
```

## 3.8 `sub`语句

![sub_1](images/sub_1.png) 

```
{
    actionA(){
        sub {
            actionB(),
            actionC(),
            actionD()
        }
    }
}
```

## 3.9 `sub then`语句

![sub_then_1](images/sub_then_1.png) 

```
{
    actionA(){
        sub {
            join{
                actionB()&,
                actionC()&
            }then {
                actionD()
            }
        } then {
            actionE()
        }
    }
}
```

## 3.10 `sub then else`语句

![sub_then_else_1](images/sub_then_else_1.png) 

```
{
    actionA(){
        sub{
            select{
                if(conditionA()){
                    actionB()
                },
                if(conditionB()){
                    actionC()
                }
            }
        } then {
            actionD()
        } else{
            actionE()
        }
    }
}
```

## 3.11 监听

__全局监听__

```
{
    actionA()
} [listenerA(event="before")]
```

__action监听__

```
{
    actionA()[listenerA(event="before")]
}
```

__condition监听__

```
{
    if(conditionA() [listenerA(event="before")]) {
        actionA()
    }
}
```

__exclusiveGateway监听__

```
{
    select {
        if(conditionA()){
            actionA()
        },
        if(conditionB()){
            actionB()
        }
    } [listenerA(event="before")]
}
```

__joinGateway监听__

```
{
    join {
        actionA()&,
        actionB()&
    } [listenerA(event="before")] then {
        actionD()
    }
}
```

__subFlow监听__

```
{
    sub {
        actionA()
    }[listenerA(event="before")],
    
    sub {
        actionB()
    }[listenerB(event="success")] then {
        actionC()
    },
    
    sub {
        actionD()
    }[listenerC(event="before")] then {
        actionE()
    } else {
        actionF()
    }
}
```

__监听均支持多个，以逗号分隔__

```
{
    actionA()[listenerA(event="before"), listenerB(event="success")]
}
```

## 3.12 节点参数

参数格式：`<name>=<value>`

其中`<value>`支持占位符和字面值，其中字面值包括int、long、16进制、8进制、float、double、String

占位符格式`${xxx.yyy.zzz}`

# 4 异步

## 4.1 Promise

__流触发后会通过线程池全异步执行，提供promise用于感知流执行状态以及获取流执行结果（类似Netty的ChannelFuture/ChannelPromise），包含以下功能（全部并发安全）__

1. `boolean isCancelled()`: 判断流是否被取消
1. `boolean isDone()`: 判断流是否已完成（成功/失败/取消）
1. `boolean isSuccess()`: 判断流是否执行成功
1. `boolean isFailure()`: 判断流是否执行失败
1. `Throwable cause()`: 获取流执行失败时的异常
1. `boolean tryCancel()`: 尝试取消流，当流尚未完成时，才会取消成功
1. `boolean trySuccess(T outcome)`: 尝试成功流，当流尚未完成时，且竞争成功时，返回true
1. `boolean tryFailure(Throwable cause)`: 尝试失败流，当流尚未完成时，且竞争成功时，返回true
1. `Promise addListener(PromiseListener listener)`: 添加监听，__保证监听一定执行（无论在流执行时，或执行完成后添加，都会确保触发监听）__
1. `void sync()`: 同步阻塞，等待流执行完毕，不会抛出异常
1. `boolean await(long timeout, TimeUnit unit)`: 同步阻塞指定时间，若在指定时间内流执行结束（包括成功/失败/取消），返回true，否则false
1. `T get()`: 同步阻塞等待流执行结果，若流执行异常或者取消，将会抛出异常
1. `T get(long timeout, TimeUnit unit)`: 同步阻塞指定时间，等待流执行结果，若在指定时间内流执行异常（失败/取消）或者在指定时间内未完成，将会抛出异常

## 4.2 PromiseListener

__可以基于Promise配置监听，当流正常或者异常终止时，会触发监听。类似Netty的ChannelFuture/ChannelPromise__

1. 当`Promise`完成时，每个`PromiseListener`会按照添加的顺序依次执行，并且仅执行一次
1. 无论`PromiseListener`的添加时机（可能在`Promise`完成前添加，也可能在`Promise`完成后添加），都会确保在当`Promise`完成时触发所有的`PromiseListener`

## 4.3 ExecutionLink的暂停与重启

流引擎提供了暂停与重启ExecutionLink的能力，我们可以通过`ExecutableContext#pauseExecutionLink`来暂停当前的执行分支，该方法会返回一个`ExecutionLinkPausePromise`对象，后续可以通过`ExecutionLinkPausePromise#trySuccess(null)`方法来进行执行分支的重启，当然你也可以调用`ExecutionLinkPausePromise#tryCancel`来取消暂停（这会导致整个flow执行异常），或者调用`ExecutionLinkPausePromise#tryFailure(exception)`来抛出异常（当异常类型是`LinkExecutionTerminateException`时，可以终止当前分支的执行；否则会导致整个flow执行异常）

__要注意的是，暂停并不会当前影响`Delegate`（`Action`、`Condition`、`Listener`）以及所有`DelegateInterceptor`的执行，这两者是不可分的原子执行单元。当前执行分支的暂停点为：下一个`Element`开始前，示意图如下：__

* `elementA`：可以是`Action`、`Condition`、`Listener`
* `elementB`：可以是`Action`、`Condition`、`Listener`、`JoinGateway`、`ExclusiveGateway`、`SubFlow`

![pause_example](images/pause_example.png)

# 5 拦截器

流引擎提供了类似spring-aop的拦截器机制，核心接口包括`DelegateInterceptor`以及`DelegateInvocation`，通过拦截器，我们可以轻松地实现一些业务能力，执行统计、日志打印等

`DelegateInterceptor`类似于`MethodInterceptor`，此外还提供了节点匹配的能力，可以通过`matches`方法来选择匹配节点

```java
package com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public interface DelegateInterceptor {

    /**
     * whether given executableName matches this interceptor
     *
     * @param executableName executable name
     * @return whether matches
     */
    default boolean matches(String executableName) {
        return true;
    }

    /**
     * Implement this method to perform extra treatments before and
     * after the invocation.
     *
     * @param delegateInvocation delegate invoker
     * @return result of target delegate call
     */
    Object invoke(DelegateInvocation delegateInvocation) throws Throwable;
}
```

`DelegateInvocation`类似于`MethodInvocation`

```java
package com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor;

import com.github.liuyehcf.framework.flow.engine.model.Element;
import com.github.liuyehcf.framework.flow.engine.model.ElementType;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.Delegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ExecutableContext;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public interface DelegateInvocation {

    /**
     * Proceed to the next interceptor in the chain.
     *
     * @return result of target delegate call
     */
    Object proceed() throws Throwable;

    /**
     * get target delegate
     *
     * @return target delegate
     */
    Delegate getDelegate();

    /**
     * get executable context
     *
     * @return executable context
     */
    ExecutableContext<? extends Element> getExecutableContext();

    /**
     * type of delegate (action/condition/listener)
     *
     * @return delegate type
     * @see com.github.liuyehcf.framework.flow.engine.model.Executable
     * @see com.github.liuyehcf.framework.flow.engine.model.ElementType
     */
    ElementType getType();

    /**
     * get array of argument name
     *
     * @return array of argument name
     */
    String[] getArgumentNames();

    /**
     * get array of argument value
     *
     * @return array of argument value
     */
    Object[] getArgumentValues();
}
```

# 6 数据统计

1. 流执行链路：有多少条并行链路就会有多少个执行链路
1. 执行trace：每个执行链路中包含多个节点trace
    * 唯一的执行id
    * 执行节点名称
    * 执行节点参数
    * 执行节点起始时间
    * 执行节点结束时间
    * 执行节点变更的变量集
    * 执行中捕获的异常
    * 执行节点的attribute集合

# 7 Spring-Boot-Starter

集成了Spring-Boot-Start，无需任何配置即可集成流引擎

__maven依赖__

```xml
<dependency>
    <groupId>com.github.liuyehcf</groupId>
    <artifactId>flow-engine-spring-boot-starter</artifactId>
    <version>1.4.4</version>
</dependency>
```

## 7.1 配置`ActionDelegate`

1. Spring提供的`@Component`以及相关注解，那么节点的名称就是Bean的名称
1. `@ActionBean`注解，那么节点名称就是`ActionBean.name()`
* 如果配置了`DelegateField`，那么该`Bean`的类型务必标注成`@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)`

```java
package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.demo;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ActionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author hechenfeng
 * @date 2020/1/4
 */
@Component(value = "printAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrintAction implements ActionDelegate {

    private DelegateField content;

    public void setContent(DelegateField content) {
        this.content = content;
    }

    @Override
    public void onAction(ActionContext context) {
        System.out.println("printAction. content=" + content.getValue());
    }
}
```

## 7.2 配置`ConditionDelegate`

1. Spring提供的`@Component`以及相关注解，那么节点的名称就是Bean的名称
1. `@ConditionBean`注解，那么节点名称就是`ConditionBean.name()`
* 如果配置了`DelegateField`，那么该`Bean`的类型务必标注成`@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)`

```java
package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.demo;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;
import com.github.liuyehcf.framework.flow.engine.spring.boot.starter.annotation.ConditionBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * @author hechenfeng
 * @date 2020/1/4
 */
@ConditionBean(names = "printCondition")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrintCondition implements ConditionDelegate {

    private DelegateField content;

    public void setContent(DelegateField content) {
        this.content = content;
    }

    @Override
    public boolean onCondition(ConditionContext conditionContext) throws Exception {
        System.out.println("printCondition. content=" + content.getValue());
        return true;
    }
}
```

## 7.3 配置`ListenerDelegate`

1. Spring提供的`@Component`以及相关注解，那么节点的名称就是Bean的名称
1. `@ListenerBean`注解，那么节点名称就是`ListenerBean.name()`
* 如果配置了`DelegateField`，那么该`Bean`的类型务必标注成`@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)`

```java
package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.demo;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;
import com.github.liuyehcf.framework.flow.engine.spring.boot.starter.annotation.ListenerBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * @author hechenfeng
 * @date 2020/1/4
 */
@ListenerBean(names = "printListener")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrintListener implements ListenerDelegate {

    private DelegateField content;

    public void setContent(DelegateField content) {
        this.content = content;
    }

    @Override
    public void onBefore(ListenerContext context) throws Exception {
        System.out.println(String.format("printListener onBefore. content=%s", content.getValue()));
    }

    @Override
    public void onSuccess(ListenerContext context, Object result) throws Exception {
        System.out.println(String.format("printListener onSuccess. content=%s", content.getValue()));
    }

    @Override
    public void onFailure(ListenerContext context, Throwable cause) throws Exception {
        System.out.println(String.format("printListener onFailure. content=%s", content.getValue()));
    }
}
```

## 7.4 配置`DelegateInterceptor`

__配置`DelegateInterceptor`__，可以用`@Scope`注解指定拦截器的先后顺序，其行为与Spring-Aop一致

```java
package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.demo;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.DelegateInterceptor;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.DelegateInvocation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author hechenfeng
 * @date 2020/1/4
 */
@Component
@Order(value = 1)
public class DelegateInterceptorOrder1 implements DelegateInterceptor {
    @Override
    public Object invoke(DelegateInvocation delegateInvocation) throws Throwable {
        System.out.println("enter DelegateInterceptorOrder1");

        Object proceed = delegateInvocation.proceed();

        System.out.println("exit DelegateInterceptorOrder1");
        return proceed;
    }
}
```

```Java
package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.demo;

import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.DelegateInterceptor;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.DelegateInvocation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author hechenfeng
 * @date 2020/1/4
 */
@Component
@Order(value = 2)
public class DelegateInterceptorOrder2 implements DelegateInterceptor {
    @Override
    public Object invoke(DelegateInvocation delegateInvocation) throws Throwable {
        System.out.println("enter DelegateInterceptorOrder2");

        Object proceed = delegateInvocation.proceed();

        System.out.println("exit DelegateInterceptorOrder2");
        return proceed;
    }
}
```

## 7.5 执行

```Java
package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.demo;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.common.tools.promise.PromiseListener;
import com.github.liuyehcf.framework.flow.engine.ExecutionCondition;
import com.github.liuyehcf.framework.flow.engine.FlowEngine;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author hechenfeng
 * @date 2020/1/4
 */
@SpringBootApplication(scanBasePackages = {"com.github.liuyehcf.framework.flow.engine.spring.boot.starter.demo"})
public class DemoApplication {

    @Resource
    private FlowEngine flowEngine;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class);
    }

    @PostConstruct
    public void flow() {
        String dsl = "{\n" +
                "   if(printCondition(content=\"hello, \")) {\n" +
                "       printAction(content=\"hechenfeng\")\n" +
                "   }\n" +
                "}";

        Promise<ExecutionInstance> promise = flowEngine.startFlow(new ExecutionCondition(dsl));

        // 注册监听
        promise.addListener(new PromiseListener<ExecutionInstance>() {
            @Override
            public void operationComplete(Promise<ExecutionInstance> promise) {
                System.out.println("trigger promise listener");
                if (promise.isSuccess()) {
                    System.out.println(JSON.toJSONString(promise.get()));
                } else if (promise.isFailure() && promise.cause() != null) {
                    promise.cause().printStackTrace();
                }
            }
        });
    }
}
```

__输出__

```
enter DelegateInterceptorOrder1
enter DelegateInterceptorOrder2
printCondition. content=hello, 
exit DelegateInterceptorOrder2
exit DelegateInterceptorOrder1
enter DelegateInterceptorOrder1
enter DelegateInterceptorOrder2
printAction. content=hechenfeng
exit DelegateInterceptorOrder2
exit DelegateInterceptorOrder1
trigger promise listener
{"attributes":{},"endTimestamp":1601967730844,"env":{},"flow":{"elements":[{"flow":{"$ref":"$.flow"},"id":"1","linkType":"NORMAL","listeners":[],"predecessors":[],"successors":[{"argumentNames":["content"],"argumentValues":["hello, "],"flow":{"$ref":"$.flow"},"id":"2","linkType":"NORMAL","listeners":[],"name":"printCondition","predecessors":[{"$ref":"$.flow.elements[0]"}],"successors":[{"argumentNames":["content"],"argumentValues":["hechenfeng"],"flow":{"$ref":"$.flow"},"id":"3","linkType":"TRUE","listeners":[],"name":"printAction","predecessors":[{"$ref":"$.flow.elements[0].successors[0]"}],"successors":[],"type":"ACTION"}],"type":"CONDITION"}],"type":"START"},{"$ref":"$.flow.elements[0].successors[0]"},{"$ref":"$.flow.elements[0].successors[0].successors[0]"}],"ends":[{"$ref":"$.flow.elements[0].successors[0].successors[0]"}],"events":[],"id":"88ab739c-6e5f-408e-8f12-81594f8faa8a","linkType":"NORMAL","listeners":[],"name":"e37b560c-6ae9-4ead-a13f-e329ff736747","predecessors":[],"start":{"$ref":"$.flow.elements[0]"},"successors":[],"type":"SUB_FLOW"},"id":"6ea541ba-8890-4b01-b417-bffe89a2c91b","links":[{"env":{"$ref":"$.env"},"id":"a69e2290-1768-4dde-95af-1bc9e59f8fdd","traces":[{"endTimestamp":1601967730786,"executionId":0,"id":"1","startTimestamp":1601967730786,"type":"START","useTimeNanos":0},{"arguments":[{"name":"content","value":"hello, "}],"attributes":{},"endTimestamp":1601967730836,"executionId":1,"id":"2","name":"printCondition","propertyUpdates":[],"result":true,"startTimestamp":1601967730834,"type":"CONDITION","useTimeNanos":2173256},{"arguments":[{"name":"content","value":"hechenfeng"}],"attributes":{},"endTimestamp":1601967730843,"executionId":2,"id":"3","name":"printAction","propertyUpdates":[],"startTimestamp":1601967730842,"type":"ACTION","useTimeNanos":208637}]}],"startTimestamp":1601967730781,"traces":[],"unreachableLinks":[],"useTimeNanos":63322057}
```

# 8 非Spring环境

__maven依赖__

```xml
<dependency>
    <groupId>com.github.liuyehcf</groupId>
    <artifactId>flow-engine</artifactId>
    <version>1.4.4</version>
</dependency>
```

__示例代码__

```java
package com.github.liuyehcf.framework.flow.engine.test.demo;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.common.tools.promise.PromiseListener;
import com.github.liuyehcf.framework.flow.engine.ExecutionCondition;
import com.github.liuyehcf.framework.flow.engine.runtime.DefaultFlowEngine;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.context.ActionContext;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.field.DelegateField;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.DelegateInterceptor;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.DelegateInvocation;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;

/**
 * @author hechenfeng
 * @date 2020/1/4
 */
public class ReadmeDemo {
    public static void main(String[] args) {

        DefaultFlowEngine flowEngine = new DefaultFlowEngine();

        // 注册一个action
        flowEngine.registerActionDelegateFactory("greetAction", () -> {
            return new ActionDelegate() {

                private DelegateField name;

                @Override
                public void onAction(ActionContext context) throws Exception {
                    System.out.println(String.format("Hello, %s. This is flow engine!", (String) name.getValue()));
                }
            };
        });

        // 注册一个interceptor
        flowEngine.registerDelegateInterceptorFactory(() ->
                new DelegateInterceptor() {
                    @Override
                    public Object invoke(DelegateInvocation delegateInvocation) throws Throwable {
                        try {
                            System.err.println("enter MyDelegateInterceptor");

                            System.err.println(delegateInvocation.getExecutableContext().getFlowId());
                            System.err.println(delegateInvocation.getExecutableContext().getFlowName());
                            System.err.println(delegateInvocation.getExecutableContext().getInstanceId());
                            System.err.println(delegateInvocation.getExecutableContext().getName());
                            System.err.println(String.format("argumentNames=%s", JSON.toJSONString(delegateInvocation.getArgumentNames())));
                            System.err.println(String.format("argumentValues=%s", JSON.toJSONString(delegateInvocation.getArgumentValues())));

                            return delegateInvocation.proceed();
                        } finally {
                            System.err.println(String.format("attributes=%s", JSON.toJSONString(delegateInvocation.getExecutableContext().getGlobalAttributes())));
                            System.err.println("exit MyDelegateInterceptor");
                        }
                    }
                });

        String dsl = "{\n" +
                "    greetAction(name=\"hechenfeng\")\n" +
                "}";

        Promise<ExecutionInstance> promise = flowEngine.startFlow(new ExecutionCondition(dsl));

        // 注册监听
        promise.addListener(new PromiseListener<ExecutionInstance>() {
            @Override
            public void operationComplete(Promise<ExecutionInstance> promise) {
                System.out.println("trigger promise listener");
                if (promise.isSuccess()) {
                    System.out.println(JSON.toJSONString(promise.get()));
                } else if (promise.isFailure() && promise.cause() != null) {
                    promise.cause().printStackTrace();
                }
            }
        });

        // 同步阻塞等待流执行完毕
        promise.sync();
        flowEngine.shutdown();
    }
}
```

__输出__

```
a0f9c342-241a-4489-badd-e8a5c9221e43
8f02269a-05cd-4633-964e-f7b7658d385f
greetAction
argumentNames=["name"]
argumentValues=["hechenfeng"]
attributes={}
exit MyDelegateInterceptor
Hello, hechenfeng. This is flow engine!
trigger promise listener
{"attributes":{},"endTimestamp":1601967441173,"env":{},"flow":{"elements":[{"flow":{"$ref":"$.flow"},"id":"1","linkType":"NORMAL","listeners":[],"predecessors":[],"successors":[{"argumentNames":["name"],"argumentValues":["hechenfeng"],"flow":{"$ref":"$.flow"},"id":"2","linkType":"NORMAL","listeners":[],"name":"greetAction","predecessors":[{"$ref":"$.flow.elements[0]"}],"successors":[],"type":"ACTION"}],"type":"START"},{"$ref":"$.flow.elements[0].successors[0]"}],"ends":[{"$ref":"$.flow.elements[0].successors[0]"}],"events":[],"id":"81f6f256-6c96-4a57-aa05-4bb907a8d68c","linkType":"NORMAL","listeners":[],"name":"a0f9c342-241a-4489-badd-e8a5c9221e43","predecessors":[],"start":{"$ref":"$.flow.elements[0]"},"successors":[],"type":"SUB_FLOW"},"id":"8f02269a-05cd-4633-964e-f7b7658d385f","links":[{"env":{"$ref":"$.env"},"id":"f4490b46-13d9-4ab6-a579-29605807e3da","traces":[{"endTimestamp":1601967441081,"executionId":0,"id":"1","startTimestamp":1601967441081,"type":"START","useTimeNanos":0},{"arguments":[{"name":"name","value":"hechenfeng"}],"attributes":{},"endTimestamp":1601967441168,"executionId":1,"id":"2","name":"greetAction","propertyUpdates":[],"startTimestamp":1601967441092,"type":"ACTION","useTimeNanos":75623038}]}],"startTimestamp":1601967441071,"traces":[],"unreachableLinks":[],"useTimeNanos":102691937}
```