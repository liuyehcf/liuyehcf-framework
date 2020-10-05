# BUG

1. 当一个流只有一个condition，且抛出了linkExecutionException的时候，算不算到达叶子节点？
1. ListenerEvent.failure 的拓扑以及trace测试用例
1. if() 的trace以及拓扑测试用例
1. 流引擎线程的拒绝策略要做限制
1. trace引入startTimestamp以及endTimestamp、useTimeNano、删除startNano、endNano

# test

1. pause可重入
1. action pause end
1. action pause listener(success)
1. action pause listener(failure)
1. action pause condition
1. action pause action
1. condition pause end
1. condition pause listener(success)
1. condition pause listener(failure)
1. condition pause condition
1. condition pause action
1. listener pause end
1. listener pause action(before, success, failure)
1. listener pause condition(before, success, failure)
1. listener pause listener(before, success, failure)
1. pause 抛异常，或cancel