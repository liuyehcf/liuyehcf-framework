# BUG

1. 当一个规则只有一个condition，且抛出了linkExecutionException的时候，算不算到达叶子节点？

# FEATURE

1. ListenerEvent.failure 的拓扑以及trace测试用例
2. if() 的trace以及拓扑测试用例
3. 规则引擎线程的拒绝策略要做限制
4. 拦截器是否执行到的测试用例
5. DefaultRuleEngine的构造方法
6. @ActionBean等注解，增加ruleEngine过滤，根据匹配结果注册到不同的ruleEngine实例中去
7. 增加SerializeWriter的测试用例
8. 解析ClusterConfig时，校验self的ip是否为本机网卡的ip
9. 时钟同步
10. 当一个节点被误判inactive后，如何恢复
11. shutdown 会报错
12. onHeartBeat和onSyncTopology补充日志
13. Topology的并发操作会使得整个members在某些时刻存在两个以上leader
14. 某些情况会将节点标记为MemberStatus.unreachable，这个如何用状态机解决

# README

1. 监听的行为，是否会相互影响
2. 监听的执行时机，global执行次数，如何执行，是否会merge所有的变量


# 高可用

__要解决的问题__

1. 灰度发布时，由于重启导致的规则执行中断的问题，需要在另一台机器上重新执行
1. 集群中的节点如何感知其他节点的存在
1. 一致性问题

## 集群感知

一般而言，做法有如下几种

1. 静态配置，这种方式下，需要事先知道节点的ip，当动态缩容、扩容时，需要动态调整配置，较为不便，但实现简单
1. 种子节点（类似akka-cluster），通过一个种子节点接入集群，言下之意需要配置种子节点（单个或一组），但是可以实现缩容扩容，实现难度同第一种方式
1. 注册中心，需要配置注册中心的地址，且需要适配不同的注册中心实现（redis，zookeeper等），可以动态实现缩容扩容，实现较为复杂

最后选择：种子节点

1. 种子节点可以看做一个小集群，相互有心跳感知
1. 每个普通节点和某一个种子节点保持心跳
1. 普通节点加入集群时，遍历种子节点，直到加入集群
1. 普通节点与种子节点之间的心跳断开后，需要将集群状态推送到所有节点，这里涉及到分布式一致性问题
1. 普通节点只能被动感知集群变化
1. 除了接入功能外，种子节点也可以作为普通节点使用
1. 种子节点需要感知的状态
    * 哪个普通节点和哪个种子节点之间有心跳
    * 集群中处于存活状态的节点列表
1. 心跳
    * 两个种子节点之间需要几个心跳？1个还是2个？
    * 心跳抢占，如果两个种子节点或多个种子节点同时向一个普通节点发起心跳连接，那么该普通节点需要任意接受一个连接，拒绝其他连接
1. 类似akka，节点的生命周期（active，removed，等等这些状态）
1. 种子集群也需要leader，所有的状态更新由leader发起（从这个角度来避免竞争），换言之，需要进行选举
1. 连通图
    * 普通节点只跟种子节点连
    * 种子节点之间有连
1. seed1和seed2同时启动，启动的时候都发现其他seed尚未存活，这个时候如何恢复（不恢复的话会形成两个孤岛）
1. dns解析，可以配置域名而不仅是ip

状态图：https://doc.akka.io/docs/akka/current/common/cluster.html#membership

为了便于测试，需要将单例模式改成实例模式

# 分布式问题

1. 何时进行leader选举
1. 节点状态只能通过leader来进行修改，其他节点只能发现而不能修改
1. 用时钟作为version
1. 当一个节点被探测为unreachable之后，如果真的只是网络抖动（其他节点与该节点的通信正常），那么如何恢复（通过heart beat）
1. hook method
1. 校验每个member的order是不同的（避免极端情况造成的问题）
1. 两个leader如何纠正
1. 当有两个节点时，且需要选主，一个发现之前的leader断了，因此发起选举，但是另一个尚未检测到leader挂了，因此断开
1. 既要避免一个节点检测到某个节点unreachable后发起选举的过程，又要保持活性
    * 考虑这样的场景，有节点A、B、C，其中A是leader
    * A挂了
    * B检测到A挂了，发起选举的预提案，B收到通过，C收到，此时由于尚未发现A unreachable，因此拒绝
    * B的预提案被拒绝
    * C检测到A挂了，发起选举的预提案，C收到通过，B收到后，由于C-mid小于B-mid，因此拒绝
    * C的预提案被拒绝
    * leader选举终止，
1. 这样一种case
    * 一个seed，其余节点正常加入集群
    * seed退出，其他节点选举leader
    * seed重启，此时变成了两个集群了
    * 要修复这个问题，必须让其他普通节点不断地来连seed节点
1. 多种子节点避免形成多个孤岛
1. 多个孤岛如何合并
    * 取tid大的为孤岛为主
    * 若tid一样，则memid大的为主
    * 若memid一样，那么以server侧为主
1. 如何保证leader非local active的时候，不将其状态推给其他节点
1. 如何保证member.id唯一
1. 异常状态下，如何拒绝选举
1. 这样的case
    * 一个10个节点的集群正常后
    * leader退出，因此开始leader选举
    * 1号发起竞选，但是被拒绝  总票 4，5，9
    * 但是由于receivedFirstStateVoteMessages没有清空，导致一个accept直接通过，投票累计 5，5，9
1. 保证参与投票或者发起竞选的节点都是正常的
    * 这会导致，当一批节点同时加入集群，但是尚未达到稳定的时候，无法选出新的leader
1. 发起选举时，要确保超过一半的节点都认为leader挂了，这样就不会因为leader存活而拒绝选举
1. accept以及reject的判断工具，以及测试用例
1. 这样一种case
    * 1、2、3三节点，1是leader,2.id>3.id
    * 1挂了
    * 2检测到1挂了，发起选举
    * 2accept，3reject（因为尚未检测到挂了），2.max=2.id,3.max=null
    * 因此2的选举终止
    * 3检测到1挂了，3发起选举
    * 2reject 3accept，2.max=2.id  3.max=3.id
    * 因此3的选举终止
    * 2检测到1挂了，重新发起选举
    * 2 reject 3reject
    * 3检测到1挂了，重新发起选举，2reject 3 reject
1. 两个seed节点如果probe间隔一样，会与对方保持心跳，如果这两个几乎同时启动，那么很可能会将对方断开
1. 这样一种B、C的拓扑是正确的，且C是leader，且A与C无连接
    * A认为B是leader
        * A发心跳给B，B看到对方认为leader是自己，因此发一个SyncTopology给对方
        * B发心跳给A，A本该请求B的leader，即C，但是并无连接，因此向B发送SyncTopologyRequest
    * A没有leader
        * A发心跳给B，B看到对方没有leader，因此，发送一个SyncTopology给对象
        * B发心跳给A，A本该请求B的leader，即C，但是并无连接，因此向B发送SyncTopologyRequest

# 重要原则

## 何时进行leader选举

1. 当某个节点a检测到leader unreachable之后，向其他节点发起询问