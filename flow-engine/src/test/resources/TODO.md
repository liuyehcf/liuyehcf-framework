# BUG

1. 当一个流只有一个condition，且抛出了linkExecutionException的时候，算不算到达叶子节点？
1. ListenerEvent.failure 的拓扑以及trace测试用例
1. if() 的trace以及拓扑测试用例
1. 流引擎线程的拒绝策略要做限制

# TODO

failure的异常处理原则：

1. 异常可以升级，但是不能降级
    * 当failure-Listener抛出LinkExecutionTerminateException时，以Listener依附的节点抛出的异常为主
    * 当failure-Listener抛出其他异常时，以Listener抛出的异常为主