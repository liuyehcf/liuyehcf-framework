# FEATURE

1. ListenerEvent.failure 的拓扑以及trace测试用例
2. if() 的trace以及拓扑测试用例
3. global级别的listener如何定义failureEvent
4. 异步化后condition如何获取结果
5. 确保condition的optPromise能够结束
6. LinkTermination异常的测试用例
7. 检查所有的processAsyncPromise是否合理
8. ListenerPromise多次绑定的问题
9. globalFailureListener抛异常，以及第一个，中间，最后一个抛异常（终止链路以及其他异常），流程是否正常的测试用例

# README

1. 监听的行为，是否会相互影响
2. 监听的执行时机，global执行次数，如何执行，是否会merge所有的变量