   * [1 Overview](#1-overview)
   * [2 Basic Abilities](#2-basic-abilities)
      * [2.1 Introduction to Core Interfaces](#21-introduction-to-core-interfaces)
      * [2.2 LinkType](#22-linktype)
      * [2.3 Node](#23-node)
      * [2.4 Executable](#24-executable)
         * [2.4.1 Action](#241-action)
         * [2.4.2 Condition](#242-condition)
         * [2.4.3 Listener](#243-listener)
         * [2.4.4 Property Injection](#244-property-injection)
         * [2.4.5 Thread Pool Isolation](#245-thread-pool-isolation)
      * [2.5 Gateway](#25-gateway)
         * [2.5.1 ExclusiveGateway](#251-exclusivegateway)
         * [2.5.2 JoinGateway](#252-joingateway)
      * [2.6 Sub Flow](#26-sub-flow)
      * [2.7 Exception](#27-exception)
   * [3 Flow Description Language](#3-flow-description-language)
      * [3.1 Cascade](#31-cascade)
      * [3.2 Parallel](#32-parallel)
      * [3.3 if Statement](#33-if-statement)
      * [3.4 if else Statement](#34-if-else-statement)
      * [3.5 select Statement](#35-select-statement)
      * [3.6 join Statement](#36-join-statement)
      * [3.7 join then Statement](#37-join-then-statement)
      * [3.8 sub Statement](#38-sub-statement)
      * [3.9 sub then Statement](#39-sub-then-statement)
      * [3.10 sub then else Statement](#310-sub-then-else-statement)
      * [3.11 Listener](#311-listener)
      * [3.12 Node Parameters](#312-node-parameters)
   * [4 Asynchronous](#4-asynchronous)
      * [4.1 Promise](#41-promise)
      * [4.2 PromiseListener](#42-promiselistener)
      * [4.3 Pause and Resume of ExecutionLink](#43-pause-and-resume-of-executionlink)
   * [5 Interceptors](#5-interceptors)
   * [6 Data Statistics](#6-data-statistics)
   * [7 Spring-Boot-Starter](#7-spring-boot-starter)
      * [7.1 Configure ActionDelegate](#71-configure-actiondelegate)
      * [7.2 Configure ConditionDelegate](#72-configure-conditiondelegate)
      * [7.3 Configure ListenerDelegate](#73-configure-listenerdelegate)
      * [7.4 Configure DelegateInterceptor](#74-configure-delegateinterceptor)
      * [7.5 Execution](#75-execution)
   * [8 Non-Spring Environment](#8-non-spring-environment)

# 1 Overview

## Preface

The `flow-engine` aims to provide orchestration capabilities for functional nodes. Below is a brief introduction to the use of flow-engine using the example of smart homes: Mr. Wang has a smart thermometer and a smart air conditioner connected to the IoT platform. The thermometer reports the temperature every minute. The following functionalities are required: when the temperature exceeds 30 degrees Celsius, turn on the air conditioner; when the temperature drops below 10 degrees Celsius, turn on the heater; for other temperatures, turn off the air conditioner. We may need to write the following logic:

```
if (temperature > 30) {
    control(airCondition, open, coldMode)
} else if (temperature < 10) {
    control(airCondition, open, heatMode)
} else {
    control(airCondition, close)
}
```

Later, Mr. Wang added a smart floor heating system and wanted to turn on the floor heating instead of the air conditioner when the temperature drops below 10 degrees Celsius. **At this point, we have to modify the code and redeploy it, which is very cumbersome.**

```
if (temperature > 30) {
    control(airCondition, open, coldMode)
} else if (temperature < 10) {
    control(floorHeating, open)
} else {
    control(airCondition, close)
    control(floorHeating, close)
}
```

With the help of `flow-engine`, we can separate the temperature judgment and device control into two independent functional nodes. Then, we can use the descriptive language of `flow-engine` to orchestrate these functional nodes. Real-time deployment can be achieved using the interfaces provided by `flow-engine`.

**The following will introduce the basic concepts and usage of `flow-engine`.**

## Feature List

1. **Rich atomic capabilities**
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
1. **Concise flow description language**
    * Cascade
    * Parallel
    * Aggregation
    * Listener
    * `if` statement
    * `if else` statement
    * `select` statement
    * `join` statement
    * `join then` statement
    * `sub` statement
    * `sub then` statement
    * `sub then else` statement
1. **Rich mechanisms**
    * Promise
    * PromiseListener
    * DelegateInterceptor
    * DelegateField
    * Pause and resume of ExecutionLink
    * Thread pool isolation
    * Asynchronous execution timeout setting
1. **Integration with Spring-Boot-Starter**
1. **Data statistics**
    * Execution link
    * Variable update records
    * Node execution time
    * Attribute

# 2 Basic Abilities

## 2.1 Introduction to Core Interfaces

![elements](images/elements.png) 

1. `Element`: Defines the basic capabilities of elements in the flow, including
    * `id`: The unique id at the flow level
    * `type`: The type of the element, see `ElementType` for details
    * `flow`: Associated flow itself
1. `Attachable`: Can be attached to a node, including
    * `attachedId`: The id of the attached node
1. `Executable`: An element that can execute Java logic, including
    * `name`: The name of the element
    * `argumentNames`: The names of the arguments
    * `argumentValues`: The values of the arguments
1. `Node`: Defines nodes in the flow. Nodes are the smallest topological units in the flow, including
    * `predecessors`: Predecessor collection
    * `successors`: Successor collection
    * `listeners`: Listener collection
1. `Conditional`: Nodes in the flow that can be followed by conditional nodes, including
    * `linktype`: Link type, including `NORMAL`, `TRUE`, and `FALSE`
1. `Listener`: Listener, including
    * `scope`: Listener scope, including `global` and `node`
    * `event`: Event type, including `before`, `success`, and `failure`
1. `Activity`: The definition of this concept is referenced from `flowable`
1. `Action`: A normal execution node
1. `Condition`: A conditional execution node
1. `Gateway`: Gateway node
1. `ExclusiveGateway`: Exclusive gateway, only allows the first `Condition` that satisfies the condition to pass
1. `JoinGateway`: Join gateway, aggregates multiple execution branches into one branch
    * `joinMode`: Aggregation mode, including `hard` mode, `soft` mode, and `or` mode
    * `hard` means that all predecessor nodes must be executed normally for the join to pass
    * `soft` means that all reachable predecessor nodes must be executed normally for the join to pass (in other words, unreachable branches are allowed to exist)
    * `or` means that the join can pass if any predecessor node is executed normally
1. `Flow`: The flow itself can also exist as a node in the top-level flow topology (i.e., sub-flow)

## 2.2 LinkType

**In the flow engine, the connections between nodes are called `Links`, and there are three types of links:**

* `LinkType.TRUE`: True branch
* `LinkType.FALSE`: False branch
* `LinkType.NORMAL`: Default branch

**Among them, the types of links between `Condition` and subsequent nodes are `LinkType.TRUE` or `LinkType.FALSE`. For other types of nodes, the types of links with subsequent nodes are `LinkType.NORMAL`. Please refer to the following diagram for example:**

![linktype_example](images/linktype_example.png) 

**In the following text, we refer to the connection between a node and its subsequent node as a `successor Link`.**

## 2.3 Node

**The `Node` is the most basic element in the flow topology structure, and other elements, such as `Listener`, can only exist by attaching to a `Node`. `Node` includes:**

* `Action`
* `Condition`
* `JoinGateway`
* `ExclusiveGateway`
* `Flow`

## 2.4 Executable

`Executable` is an element that can execute Java code, including `Action`, `Condition`, and `Listener`.

**Registration**

1. In a non-Spring environment, registration needs to be done through the static methods of `com.github.liuyehcf.framework.flow.engine.FlowEngine`
    * `registerActionDelegateFactory`
    * `registerConditionDelegateFactory`
    * `registerListenerDelegateFactory`
1. In a Spring environment, no configuration is required. The flow engine will automatically complete the registration work.
    * `@Component`, the node name is the name of the bean
    * `@ActionBean`, this annotation is marked with `@Component` by default. The node name is specified by `names` and can have multiple aliases.
    * `@ConditionBean`, this annotation is marked with `@Component` by default. The node name is specified by `names` and can have multiple aliases.
    * `@ListenerBean`, this annotation is marked with `@Component` by default. The node name is specified by `names` and can have multiple aliases.

**Naming**

1. Identifier, such as `actionA`, `action1`, `_action_2`
1. Identifier combined with `.`, such as `my.test.condition3`
1. Identifier combined with `/`, such as `my/test/listener5`

### 2.4.1 Action

**`Action` is a normal node that can execute Java code. `Action` only allows successor Links of type `LinkType.NORMAL`. The inheritance relationship is as follows:**

![action](images/action.png) 

To create an `Action` that can execute business logic, you only need to implement the `ActionDelegate` interface.

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

**The `context` can be used to**

1. Get property values
1. Set property values (Setting property values through this method will be recorded by the engine and will be applied to aggregation logic. The change information will be saved in the execution Trace)
1. Get environment variables (Directly manipulate environment variables to set property values, which will not be recorded by the engine. Use with caution)
1. Add attributes (Add property values for storing business data, isolated from environment variables)

### 2.4.2 Condition

**`Condition` is a conditional node that can execute Java code. `Condition` only allows successor Links of type `LinkType.TRUE` or `LinkType.FALSE`.**

![condition](images/condition.png) 

To create a `Condition` that can execute business logic, you only need to implement the `ConditionDelegate` interface. **The return value of the `onCondition` method determines the direction of the entire flow. If `true` is returned, the `LinkType.TRUE` branch will be taken. If `false` is returned, the `LinkType.FALSE` branch will be taken.**

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

**The `context` can be used to**

1. Get property values
1. Set property values (Setting property values through this method will be recorded by the engine and will be applied to aggregation logic. The change information will be saved in the execution Trace)
1. Get environment variables (Directly manipulate environment variables to set property values, which will not be recorded by the engine. Use with caution)
1. Add attributes (Add property values for storing business data, isolated from environment variables)

### 2.4.3 Listener

**`Listener` is not a node in the flow topology structure, but it must exist attached to a node. It includes:**

1. `Flow` (scope: `global/node`)
    * Only `node` listeners can be configured for `Sub Flow`
    * `global` listeners can be configured for `Flow` or `Sub Flow`
1. `Activity` (scope: `node`)
    * `Action`
    * `Condition`
1. `Gateway` (scope: `node`)
    * `JoinGateway`
    * `ExclusiveGateway`

**event: Listener triggers**

1. `before`: Triggered when the execution link has just reached the node where the listener is attached (the attached node has not executed related logic at this time)
1. `success`: Triggered when the node where the listener is attached is executed successfully
    * For nodes such as `Action` and `Condition`, the `success` listener is triggered only when the node execution does not throw an exception
    * For nodes such as `JoinGateway` and `ExclusiveGateway`, they do not include execution logic, which means they cannot throw exceptions. Therefore, the `success` listener will always be triggered.
1. `failure`: Triggered when the attached node throws an exception
    * This type of listener can only be used to perceive exceptional situations, but cannot handle exceptions. In other words, after this listener is processed, the exception will still be thrown to the upper layer.
    * If an exception is thrown in the processing logic of the listener, there will be two exceptions: the original exception thrown by the attached node (referred to as `original-exception`) and the exception thrown by the listener (referred to as `listener-exception`). The final exception thrown follows the following logic:
        1. If the exception thrown by the listener is `LinkExecutionTerminateException`, the final exception thrown will be the `original-exception`.
        1. Otherwise, the final exception thrown will be the `listener-exception`.

**scope: Scope of the Listener**

1. `node`: Node-level listener, which can be attached to `Action`, `Condition`, and `Gateway`
1. `global`: Flow-level listener, which means it is triggered before/after successful execution or when an execution exception occurs
    * If the flow has multiple execution branches, the `success/failure` listener will also be executed only once
    * **For example, in the diagram below, if `actionA`, `actionB`, and `actionC` are all executed successfully, the `GlobalSuccessListener` will also be executed only once**

![listener](images/global_success_example.png)  

![listener](images/listener.png)  

To create a `Listener` that can execute business logic, you only need to implement the `ListenerDelegate` interface.

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

**The `context` can be used to**

1. Get property values
1. Set property values (Setting property values through this method will be recorded by the engine and will be applied to aggregation logic. The change information will be saved in the execution Trace)
1. Get environment variables (Directly manipulate environment variables to set property values, which will not be recorded by the engine. Use with caution)
1. Add attributes (Add property values for storing business data, isolated from environment variables)

### 2.4.4 Property Injection

`ActionDelegate`, `ConditionDelegate`, and `ListenerDelegate` all inherit the `Delegate` interface. In this section, `Delegate` is used as a generic term for these three specific interfaces.

![delegate](images/delegate.png)

**If a `Delegate` needs to configure some variables, it must use `DelegateField`, which will be automatically injected by the flow engine. There are two ways to inject variables:**

1. `set` method: This method is preferred and recommended (not affected by Spring-AOP).
1. Field injection: This method is not recommended. In a Spring environment, if AOP is configured, this method will be ineffective, and the fields will be injected into the wrapper class.
* **If a node does not specify a parameter, the value obtained through `DelegateField.getValue` method will be null, but the `DelegateField` itself will not be null.**

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

### 2.4.5 Thread Pool Isolation

When a node's business logic is complex, it is recommended to use a business thread pool to handle the business logic instead of executing it in the flow engine's thread pool.

`ActionDelegate`, `ConditionDelegate`, and `ListenerDelegate` can be set to execute asynchronously using the `isAsync` method. The timeout can be set using the `getAsyncTimeout` method. The business thread pool can be set using the `getAsyncExecutor` method. These methods are defined in the top-level `Delegate` interface and are non-asynchronous by default.

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

There are two types of gateways: ExclusiveGateway and JoinGateway. The following sections will introduce these two gateways and their specific use cases.

### 2.5.1 ExclusiveGateway

For ordinary nodes, such as `Action` or `Condition`, when there are multiple successor nodes, the default behavior is concurrent execution. For example, `node 0` has n successor nodes: `node 1`, `node 2`, ..., `node n`. After `node 0` is executed, **it will fork n execution paths to continue execution, and after forking, each execution path will have its own environment variables and will not interfere with each other.**

![default_parallel](images/default_parallel.png)

However, sometimes we want only one of these n successor nodes to be executed. In this case, we need to use the ExclusiveGateway.

![exclusiveGateway](images/exclusiveGateway.png)

**The successor nodes of `ExclusiveGateway` must be `Condition` nodes. `ExclusiveGateway` ensures that at most one successor path is executed, and the execution order is exactly the same as the order of the nodes.** In the diagram above, `ExclusiveGateway` has n successor `Condition` nodes: `condition 1`, `condition 2`, ..., `condition n`. First, `condition 1` will be executed, and if the result is true, the subsequent nodes of `condition 1` will be executed. If the result is false, `condition 2` will be executed, and so on.

![exclusive_parallel](images/exclusive_parallel.png)

### 2.5.2 JoinGateway

For ordinary nodes that have only one predecessor node (excluding the start node), the `JoinGateway` allows multiple predecessor nodes and performs aggregation processing, including:

1. Aggregation of environment variables: The `JoinGateway` aggregates the environment variables of all preceding branches. **If different branches simultaneously modify the value of the same variable, the aggregated value of that variable may be from any branch.**
1. Aggregation of execution paths: The `JoinGateway` aggregates the trace information of all preceding branches according to the execution time order.

![joinGateway](images/joinGateway.png)

The `JoinGateway` has three aggregation modes: `hard`, `soft`, and `or`. Here are the explanations for each mode:

1. `hard` mode: Only when all preceding nodes have arrived, the gateway allows passage.
1. `soft` mode: Only when all `reachable` preceding nodes have arrived, the gateway allows passage.
1. `or` mode: The gateway allows passage when `any` preceding node arrives, and it only allows passage once.

![joinMode](images/joinMode.png)

* `actionB` is on the `TRUE` branch of `conditionA`, which means `actionB` will only be executed if the result of `conditionA` is true.

**hard mode:**

1. If the result of `conditionA` is true, the gateway allows passage.
1. If the result of `conditionB` is false, `actionB` is unreachable. Therefore, in the `hard` mode, the gateway does not allow passage.

**soft mode:**

1. If the result of `conditionA` is true, the gateway allows passage.
1. If the result of `conditionB` is false, `actionB` is unreachable. Therefore, in the `soft` mode, the gateway allows passage for all reachable preceding nodes (`actionA` only).

**or mode:**

1. The gateway allows passage when either `actionA` or `actionB` arrives.

## 2.6 Sub Flow

The flow engine provides the concept of `sub flow`, which greatly enriches the flexibility of flow orchestration. We can put a group of nodes in a `sub flow` and easily perceive the execution result of this group of nodes.

Consider the following example:

![sub_flow_joingateway_cmp](images/sub_flow_joingateway_cmp.png)

**In this scenario, the `sub flow` is very similar to the `or` mode of `JoinGateway`, but there is a significant difference: the `sub flow` can perceive the execution status of this group of nodes, while the `or` mode of `JoinGateway` cannot perceive the execution status of this group of nodes.**

* When at least one node (`conditionA`, `conditionB`, `conditionC`) has a true execution result, the `or` mode of `JoinGateway` allows passage, and `actionB` is executed. If all the execution results of `conditionA`, `conditionB`, `conditionC` are false, `actionB` is unreachable, and this cannot be perceived.
* When the `sub flow` is executed successfully, `actionB` is executed. When the `sub flow` fails, `actionC` is executed. As you can see, the execution result of the `sub flow` can be easily perceived.

**How to define the success of the `sub flow` execution: When any branch successfully executes (i.e., any branch reaches the leaf node), the `sub flow` is considered successful; otherwise, the `sub flow` is considered failed.**

**Listeners for `sub flow`: We can configure `global` and `node` level listeners for the `sub flow`.**

* `Global` level listeners behave the same as global level listeners for flows.
* `Node` level listeners behave the same as listeners for ordinary nodes.
* For `sub flow`, `node` level listeners and `global` level listeners are equivalent.

## 2.7 Exception Handling

For `Executable`, including `Action`, `Condition`, and `Listener`:

* If a `LinkExecutionTerminateException` is thrown during execution, all subsequent nodes of that node will be marked as unreachable, and the flow will remain in a normal state.
* If any other exception is thrown during execution, the flow execution enters an exceptional state, which means all nodes of all branches of the flow terminate execution, and `promise.isFailure` is true.

![linkTerminateException](images/link_terminate_exception.png)

![unknownException](images/unknown_exception.png)

# 3 Flow Description Language

## 3.1 Cascade

![cascade](images/cascade.png)

```
{
  actionA() {
    actionB()
  }
}
```

## 3.2 Parallel

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

## 3.3 `if` statement

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

## 3.4 `if else` statement

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

## 3.5 `select` statement

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

## 3.6 `join` Statement

**Note:**

1. Use `&` to mark the nodes that need to be aggregated.
1. Use `*` to indicate the `hard` mode.

![join_1](images/join_1.png)

**hard mode**

```
{
    join & {
        actionA()&,
        actionB(),
        actionC()&
    }
}
```

**soft mode**

```
{
    join {
        actionA()&,
        actionB(),
        actionC()&
    }
}
```

**or mode**

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

**hard mode**

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

**soft mode**

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

**or mode**

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

## 3.7 `join then` statement

**Note: Use `&` to indicate the nodes to be aggregated.**

![join_then_1](images/join_then_1.png)

**hard mode**

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

**soft mode**

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

**or mode**

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

**hard mode**

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

**soft mode**

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

**or mode**

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

## 3.8 `sub` statement

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

## 3.9 `sub then` statement

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

## 3.10 `sub then else` statement

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

## 3.11 Listeners

**Global listeners**

```
{
    actionA()
} [listenerA(event="before")]
```

**Action listeners**

```
{
    actionA()[listenerA(event="before")]
}
```

**Condition listeners**

```
{
    if(conditionA() [listenerA(event="before")]) {
        actionA()
    }
}
```

**ExclusiveGateway listeners**

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

**JoinGateway listeners**

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

**SubFlow listeners**

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

**Multiple listeners are supported, separated by commas.**

```
{
    actionA()[listenerA(event="before"), listenerB(event="success")]
}
```

## 3.12 Node Parameters

Parameter format: `<name>=<value>`

`<value>` supports placeholders and literals, including int, long, hexadecimal, octal, float, double, and String.

Placeholder format: `${xxx.yyy.zzz}`

# 4 Asynchronous

## 4.1 Promise

**After the stream is triggered, it will be executed asynchronously through a thread pool. Promises are provided to perceive the execution status of the stream and obtain the execution results of the stream (similar to Netty's ChannelFuture/ChannelPromise). It includes the following functions (all thread-safe):**

1. `boolean isCancelled()`: Determines if the stream has been canceled.
1. `boolean isDone()`: Determines if the stream has completed (successfully/failed/canceled).
1. `boolean isSuccess()`: Determines if the stream has executed successfully.
1. `boolean isFailure()`: Determines if the stream has executed failed.
1. `Throwable cause()`: Retrieves the exception when the stream execution fails.
1. `boolean tryCancel()`: Tries to cancel the stream, only succeeds if the stream is not yet completed.
1. `boolean trySuccess(T outcome)`: Tries to mark the stream as successful, only succeeds if the stream is not yet completed and successfully competes with other attempts.
1. `boolean tryFailure(Throwable cause)`: Tries to mark the stream as failed, only succeeds if the stream is not yet completed and successfully competes with other attempts.
1. `Promise addListener(PromiseListener listener)`: Adds a listener, ensuring that the listener is always executed (regardless of whether it is added during the stream execution or after completion).
1. `void sync()`: Synchronously blocks and waits for the stream to complete without throwing an exception.
1. `boolean await(long timeout, TimeUnit unit)`: Synchronously blocks for a specified time. Returns true if the stream completes (including success/failure/cancellation) within the specified time, otherwise returns false.
1. `T get()`: Synchronously blocks and waits for the execution result of the stream. Throws an exception if the stream execution fails or is canceled.
1. `T get(long timeout, TimeUnit unit)`: Synchronously blocks for a specified time and waits for the execution result of the stream. Throws an exception if the stream execution fails or is canceled within the specified time.

## 4.2 PromiseListener

**Promise listeners can be configured based on Promises, and they are triggered when the stream terminates normally or abnormally. Similar to Netty's ChannelFuture/ChannelPromise.**

1. When a `Promise` is completed, each `PromiseListener` is executed in the order they are added, and only executed once.
1. Regardless of the timing of adding `PromiseListener` (whether before or after `Promise` completion), all `PromiseListeners` are guaranteed to be triggered when the `Promise` completes.

## 4.3 Pause and Resume of ExecutionLink

The stream engine provides the ability to pause and resume an ExecutionLink. We can pause the current execution branch using `ExecutableContext#pauseExecutionLink`, which returns an `ExecutionLinkPausePromise` object. Later, we can restart the execution branch by calling `ExecutionLinkPausePromise#trySuccess(null)`. Alternatively, you can cancel the pause by calling `ExecutionLinkPausePromise#tryCancel` (which will cause the entire flow execution to be abnormal), or throw an exception by calling `ExecutionLinkPausePromise#tryFailure(exception)` (when the exception type is `LinkExecutionTerminateException`, it can terminate the execution of the current branch; otherwise, it will cause the entire flow execution to be abnormal).

**Note that pausing does not affect the execution of `Delegate` (`Action`, `Condition`, `Listener`) and all `DelegateInterceptor`. These two are atomic execution units. The pause point of the current execution branch is just before the next `Element`, as shown in the diagram below:**

* `elementA`: can be `Action`, `Condition`, `Listener`
* `elementB`: can be `Action`, `Condition`, `Listener`, `JoinGateway`, `ExclusiveGateway`, `SubFlow`

![pause_example](images/pause_example.png)

# 5 Interceptors

The stream engine provides an interceptor mechanism similar to Spring AOP. The core interfaces include `DelegateInterceptor` and `DelegateInvocation`. With interceptors, we can easily implement various business capabilities such as execution statistics and logging.

`DelegateInterceptor` is similar to `MethodInterceptor` and also provides the ability to match nodes by using the `matches` method.

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

`DelegateInvocation` is similar to `MethodInvocation`.

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

# 6 Data Statistics

1. Execution Links: The number of execution links is equal to the number of parallel links.
1. Execution Trace: Each execution link contains multiple node traces.
   * Unique execution ID
   * Execution node name
   * Execution node parameters
   * Execution node start time
   * Execution node end time
   * Set of variables changed by the execution node
   * Exceptions captured during execution
   * Collection of attributes of the execution node

# 7 Spring-Boot-Starter

Integration with Spring Boot Starter allows integrating the stream engine without any configuration.

**Maven Dependency**

```xml
<dependency>
    <groupId>com.github.liuyehcf</groupId>
    <artifactId>flow-engine-spring-boot-starter</artifactId>
    <version>1.4.4</version>
</dependency>
```

## 7.1 Configuring `ActionDelegate`

1. If using Spring's `@Component` or related annotations, the node name will be the bean name.
1. If using `@ActionBean` annotation, the node name will be `ActionBean.name()`.
* If `DelegateField` is configured, the type of the bean must be marked as `@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)`.

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

## 7.2 Configuring `ConditionDelegate`

1. If using Spring's `@Component` or related annotations, the node name will be the bean name.
1. If using `@ConditionBean` annotation, the node name will be `ConditionBean.name()`.
* If `DelegateField` is configured, the type of the bean must be marked as `@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)`.

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

## 7.3 Configuring `ListenerDelegate`

1. If using Spring's `@Component` or related annotations, the node name will be the bean name.
1. If using `@ListenerBean` annotation, the node name will be `ListenerBean.name()`.
* If `DelegateField` is configured, the type of the bean must be marked as `@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)`.

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

## 7.4 Configuring `DelegateInterceptor`

**Configuring `DelegateInterceptor` is similar to Spring-Aop. The order of interceptors can be specified using the `@Scope` annotation, and it behaves the same as Spring-Aop.**

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

## 7.5 Execution

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

**Output**

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

# 8 Non-Spring Environment

**Maven Dependency**

```xml
<dependency>
    <groupId>com.github.liuyehcf</groupId>
    <artifactId>flow-engine</artifactId>
    <version>1.4.4</version>
</dependency>
```

**Example Code**

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

**Output**

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