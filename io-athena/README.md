   * [Design](#design)
      * [Symbol Explanation](#symbol-explanation)
      * [Event Handling Mechanism](#event-handling-mechanism)
         * [Sending Validation Principles](#sending-validation-principles)
         * [Receiving Validation Principles](#receiving-validation-principles)
      * [State Machine](#state-machine)
      * [Cluster Specification](#cluster-specification)
   * [Process](#process)
      * [Normal Node Joining Process (NORMAL_JOINING_PROCESS)](#normal-node-joining-processnormal_joining_process)
      * [Seed Node Joining Process (SEED_JOINING_PROCESS)](#seed-node-joining-processseed_joining_process)
      * [Leader Conflict Detection and Handling Process (LEADER_CONFLICT_PROCESS)](#leader-conflict-detection-and-handling-processleader_conflict_process)
      * [Leader Election](#leader-election)
      * [Node Handshake Conflict](#node-handshake-conflict)
   * [Exceptional Cases](#exceptional-cases)
      * [Case 1](#case1)
      * [Case 2](#case2)
      * [Case 3](#case3)
      * [Case 4](#case4)

# Design

## Symbol Explanation

| Symbol | Meaning |
|:--|:--|
| `N` | Total number of cluster nodes, at least 1. **The total number of clusters cannot be dynamically expanded** |
| `M` | Minimum number of cluster high availability nodes, `M = N/2 + 1`. **It is considered passed only when the number of votes is at least `M`** |
| `seed` | Seed node, the entry point for cluster access |
| `leader` | The manager in the cluster, only the leader can modify the status of members |
| `follower` | Ordinary members in the cluster, including `seed` and `non-seed`, can participate in voting |
| `member` | A general term for cluster nodes, can be `seed`, `follower`, `leader` |
| `version` | The version of the cluster, each time a new `leader` is generated, the version increases |
| `receiver` | Specifically refers to `com.github.liuyehcf.framework.io.athena.Receiver` |
| `heartbeatInterval` | Heartbeat interval |
| `heartbeatTimeout` | Heartbeat timeout |
| `ttlTimeout` | Maximum downtime allowed for a node (including network exceptions) |
| `retryInterval` | Retry interval, including rejoining the cluster, querying the status of the `leader`, etc. |
| `proposal` | Proposal, divided into pre-proposal stage (`pre`) and formal proposal stage (`formal`) |
| `vote` | Vote, corresponding to `proposal`, divided into pre-proposal stage (`pre`) and formal proposal stage (`formal`) |
| `proposer` | The member who proposes the proposal |
| `candidate` | The member who hopes to be elected as the `leader` by proposing |

## Event Handling Mechanism

Events are divided into two categories, system events (`system-event`) and (`custom-event`).

1. `system-event` is handled by the system framework and will not be passed to the user's `receiver`.
1. `custom-event` is passed to the user's `receiver` and processed according to the matching logic.

### Sending Validation Principles

The sender of an event must ensure that the event is sent accurately, i.e., the information expressed by the event matches the state of the cluster when the event is sent (only focus on that moment).

* For example, when sending `LeaderKeepAliveAck`, it is necessary to verify whether the sender is the leader, and only send it after successful verification.

### Receiving Validation Principles

Suppose two nodes `A` and `B` in the cluster. Node `A` observes that the state of node `B` is `λ1`, and based on this observation, node `A` sends the observation result `λ1` and the processing method `θ` to `B`.

When `B` receives the processing method `θ`, it needs to validate whether the current state is `λ1`.

* If the observed result of `B` is also `λ1`, then execute `θ`.
* If the observed result of `B` is not `λ1`, then do not process it.

## State Machine

**Node states and their meanings are as follows**

| State | Description |
|:--|:--|
| `joining` | Initialization state. When a node communicates with the `leader` for the first time to request joining the cluster, the `leader` will mark it as `joining` state. |
| `active` | After a node successfully joins the cluster, the `leader` will mark it as `active` state. |
| `unreachable` | When the `leader` does not receive the heartbeat packet from the node after `heartbeatTimeout`, it will mark the node as `unreachable`. |
| `leaving` | When the `leader` does not receive the heartbeat from the node after `ttlTimeout`, it will mark the node as `leaving`. |
| `removed` | After marking the node as `leaving`, the `leader` will perform some cleanup work. After the cleanup is completed, the node will be marked as `removed`. |

**State Machine**

| Current State | Condition | Next State |
|:--|:--|:--|
| / | Join the cluster | `joining` |
| `joining` | Finish initialization | `active` |
| `active` | The `leader` does not receive the heartbeat after `heartbeatTimeout` | `unreachable` |
| `unreachable` | The `heartbeatTimeout` is reached and the heartbeat is received again | `active` |
| `unreachable` | The `leader` does not receive the heartbeat after `ttlTimeout` | `leaving` |
| `leaving` | Finish cleanup | `removed` |

## Cluster Specification

**Cluster Startup**

* `seed` startup:
	1. Iterate through all other `seed` nodes and attempt to join the cluster.
	1. If the joining is successful, end.
	1. If the joining fails, directly become the `leader`, `version = 1`, and end.
* `non-seed` startup:
	1. Iterate through all `seed` nodes and attempt to join the cluster.
	1. If the joining is successful, end.
	1. If the joining fails, wait for `retryInterval` and repeat the process until the end.

**Leader Recovery After Failure**

1. When the number of live nodes is at least `M`, a new `leader` can be elected, and the cluster is restored to a normal state.
1. When the number of live nodes is less than `M`, the cluster becomes unavailable, and the nodes themselves still attempt to initiate the election process, but it will inevitably fail.
   * **`seed`: If no `leader` is elected after `3 * ttlTimeout`, clean up the cluster, increase the version number, and directly become the `leader`.**
   * **`non-seed`: If no `leader` is elected after `3 * ttlTimeout`, clean up the cluster, reset the version number to 1, and reattempt to join the cluster.**

**KeepAlive**

1. **`follower` needs to maintain a heartbeat with the `leader`.**
   * `follower` sends `LeaderKeepAlive`.
   * `leader` replies with `LeaderKeepAliveAck`.
   * If the `leader` does not receive the `LeaderKeepAlive` event from the `follower` after `heartbeatTime`, it marks the status of the `follower` as `unreachable` and notifies other `followers`.
   * If the `leader` does not receive the `LeaderKeepAlive` event from the follower after `ttlTimeout`, it marks the status of the `follower` as `leaving` and `removed`, then removes the `follower` from the cluster and notifies other `followers`.
   * If the `follower` does not receive the `LeaderKeepAliveAck` event from the `leader` after `ttlTimeout`, it resets the local `leader`, initiates the `leader` election process, and nominates itself as the `leader`.
1. **`leader` needs to maintain a heartbeat with all `seed` nodes to avoid isolated islands.**
   * `leader` sends `SeedKeepAlive`.
   * `seed` replies with `SeedKeepAliveAck`.
   * If a `seed` finds that the observed `leader` is different from the local observed `leader`, it sends `LeaderRelieve` based on certain principles to force one `leader` to step down.
   * When the `leader` receives the message, it notifies other `followers` to rejoin the cluster (clears the cluster and resets the version to 1), and it also attempts to rejoin the cluster (clears the cluster and resets the version to 1).
   * **Conflict resolution strategy for `leader`: If the versions are different, take the one with the larger version number. If the versions are the same, take the one with the larger `Address` (compare host strings first, then compare ports).**

**Follower Probe**

1. Maintain a heartbeat with the `leader` at intervals of `heartbeatInterval` and check the status of the `leader`.

**Leader Probe**

1. Maintain a heartbeat with the `follower` and handle exceptions. The detailed process is not described here.
1. Maintain a heartbeat with all `seed` nodes to avoid isolated islands.
   * **If all nodes have the same seed configuration, isolation can be completely avoided.**
   * **If all nodes have different seed configurations, there is a certain probability of isolated islands.**

**Paxos**

1. In one election, the `id` of each proposal must be different.
1. After the election, to avoid receiving proposals from the previous election and polluting the `candidate`.
1. After a new `leader` is generated, the local state needs to be cleared, including the maximum proposal number and the `candidate` corresponding to that proposal.
1. The validity period of the local state is `ttlTimeout * 3`.

# Process

## Normal Node Joining Process (NORMAL_JOINING_PROCESS)

**Flowchart Source Code**

```plantuml
skinparam backgroundColor #EEEBDC
skinparam handwritten true

skinparam sequence {
	ArrowColor DeepSkyBlue
	ActorBorderColor DeepSkyBlue
	LifeLineBorderColor blue
	LifeLineBackgroundColor #A9DCDF
	
	ParticipantBorderColor DeepSkyBlue
	ParticipantBackgroundColor DodgerBlue
	ParticipantFontName Impact
	ParticipantFontSize 17
	ParticipantFontColor #A9DCDF
	
	ActorBackgroundColor aqua
	ActorFontColor DeepSkyBlue
	ActorFontSize 17
	ActorFontName Aapex
}

participant member
participant seed
participant leader

member -> seed: LeaderQuery
alt leader exist
seed --> member: LeaderResponse(leaderAddress)
member -> leader: JoinClusterRequest(address)
alt is leader
leader --> member: JoinClusterResponse(accept)
leader -> leader: moving member status to <joining>\nand broadcast to all active members
leader -> leader: moving member status to <active>\nand broadcast to all active members
end
alt is not leader
leader --> member: JoinClusterResponse(reject)
...
note right member: repeat this process from start\n(ask other seed)
end
end
alt leader not exist
seed --> member: LeaderResponse(not exist)
...
note right member: repeat this process from start\n(ask other seed)
end
```

**Flowchart**

![NORMAL_JOINING_PROCESS](images/NORMAL_JOINING_PROCESS.png)

## Seed Node Joining Process (SEED_JOINING_PROCESS)

**Flowchart Source Code**

```plantuml
skinparam backgroundColor #EEEBDC
skinparam handwritten true

skinparam sequence {
	ArrowColor DeepSkyBlue
	ActorBorderColor DeepSkyBlue
	LifeLineBorderColor blue
	LifeLineBackgroundColor #A9DCDF
	
	ParticipantBorderColor DeepSkyBlue
	ParticipantBackgroundColor DodgerBlue
	ParticipantFontName Impact
	ParticipantFontSize 17
	ParticipantFontColor #A9DCDF
	
	ActorBackgroundColor aqua
	ActorFontColor DeepSkyBlue
	ActorFontSize 17
	ActorFontName Aapex
}

participant seed1
participant seed2

seed1 -> seed2: LeaderQuery
alt leader exist
seed2 --> seed1: LeaderResponse(leaderAddress)
note right seed1: continue with steps in NORMAL_JOINING_PROCESS
end
alt leader not exist\nhas other seeds to try
note right seed1: repeat this process from start\n(ask other seed)
end
alt leader not exist\nhas no other seeds to try
note right seed1: let itself as leader
end
```

**Flowchart**

![SEED_JOINING_PROCESS](images/SEED_JOINING_PROCESS.png)

## Leader Conflict Detection and Handling Process (LEADER_CONFLICT_PROCESS)

`leader1` sends `SeedKeepAlive` event to `seed`, and the locally observed `leader` of `seed` is `leader2`.

**Flowchart Source Code**

```plantuml
skinparam backgroundColor #EEEBDC
skinparam handwritten true

skinparam sequence {
	ArrowColor DeepSkyBlue
	ActorBorderColor DeepSkyBlue
	LifeLineBorderColor blue
	LifeLineBackgroundColor #A9DCDF
	
	ParticipantBorderColor DeepSkyBlue
	ParticipantBackgroundColor DodgerBlue
	ParticipantFontName Impact
	ParticipantFontSize 17
	ParticipantFontColor #A9DCDF
	
	ActorBackgroundColor aqua
	ActorFontColor DeepSkyBlue
	ActorFontSize 17
	ActorFontName Aapex
}

participant leader1
participant seed
participant leader2

leader1 -> seed: SeedKeepAlive(leader1, version1)\nfrom the local perspective
seed -> seed: check whether the leader1 and version1 \nare consistent with the local observations
seed --> leader1: SeedKeepAliveAck
alt inconsistent
note right seed: assume leader1 win the competition
seed -> leader2: LeaderRelieve(leader1)
leader2 -> leader2: rejoin the cluster\nand broadcast to all active members to do so
end
alt other case
note right seed: do nothing
end
```

**Flowchart**

![LEADER_CONFLICT_PROCESS](images/LEADER_CONFLICT_PROCESS.png)

## Leader Election

**Before starting the election, it is necessary to inquire other nodes whether the `leader` is normal from their perspectives. The election process begins only if at least `M` replies indicate that the leader is not normal. This is to avoid unnecessary `leader` elections due to a node's recovery from disconnection.**

**Flowchart Source Code**

```plantuml
skinparam backgroundColor #EEEBDC
skinparam handwritten true

skinparam sequence {
	ArrowColor DeepSkyBlue
	ActorBorderColor DeepSkyBlue
	LifeLineBorderColor blue
	LifeLineBackgroundColor #A9DCDF
	
	ParticipantBorderColor DeepSkyBlue
	ParticipantBackgroundColor DodgerBlue
	ParticipantFontName Impact
	ParticipantFontSize 17
	ParticipantFontColor #A9DCDF
	
	ActorBackgroundColor aqua
	ActorFontColor DeepSkyBlue
	ActorFontSize 17
	ActorFontName Aapex
}

participant follower
participant otherFollowers

follower -> otherFollowers: LeaderStatusQuery
otherFollowers --> follower: LeaderStatusQueryResponse(isHealthy)
alt leader is healthy
follower -> follower: in all replies, if the number of unhealthy < M,\nthen stop shis process
end
alt leader is not healthy
follower -> follower: in all replies, if the number of unhealthy >= M,\nthen start the following steps
follower -> otherFollowers: Proposal(pre, self, self)
otherFollowers --> follower: Vote(pre, isAccept, candidate),\nthe candidate has the largest accepted formal-proposal id
alt pre-proposal reject
follower -> follower: in all pre-votes, if the number of accept < M,\nthen stop this process
end
alt pre-proposal accept
follower -> follower: in all pre-votes, if the number of accept >= M,\nthen choose the candidate with largest formal-proposal id
follower -> otherFollowers: Proposal(formal, self, candidate)
otherFollowers --> follower: Vote(formal, isAccept)
alt formal-proposal reject
follower -> follower: in all formal-votes, if the number of accept < M,\nthen stop this process
end
alt formal-proposal accept
follower -> follower: in all formal-votes, if the number of accept >= M,\nthen the candidate is elected as leader
end
end
end
```

**Flowchart**

![LEADER_ELECT](images/LEADER_ELECT.png)

## Node Handshake Conflict

**Core Principles**

1. The party initiating the connection will send `active-greet`.
1. The passive party receiving `active-greet` will reply with `passive-greet`.
1. The mapping of `address` and `channel` is cached only when receiving the corresponding `greet` from the other party.
1. If a mapping conflict is detected during caching, only the connection initiated by the party with the smaller `address` is kept.

In the following flow, assuming `address(member1)<address(member2)`

* The red lines represent connections initiated by `member1`.
* The green lines represent connections initiated by `member2`.

```plantuml
skinparam backgroundColor #EEEBDC
skinparam handwritten true

skinparam sequence {
	ArrowColor DeepSkyBlue
	ActorBorderColor DeepSkyBlue
	LifeLineBorderColor blue
	LifeLineBackgroundColor #A9DCDF
	
	ParticipantBorderColor DeepSkyBlue
	ParticipantBackgroundColor DodgerBlue
	ParticipantFontName Impact
	ParticipantFontSize 17
	ParticipantFontColor #A9DCDF
	
	ActorBackgroundColor aqua
	ActorFontColor DeepSkyBlue
	ActorFontSize 17
	ActorFontName Aapex
}

participant timeline

timeline-[#red]>timeline: member1 try to talk to member2,\nbut cannot find connection with member2,\nthen connect member2 and send active-greet
timeline-[#green]>timeline: member2 try to talk to member1,\nbut cannot find connection with member1,\nthen connect member1 and send active-greet
...
alt active-greet from member2 to member1 arrive first
timeline-[#green]>timeline: member1 received the active-greet from member2,\nand save member2->channel(green one) mapping,\nthen send passive-greet to member2
...
alt active-greet from member1 to member2 arrive first
timeline-[#red]>timeline: member2 received the active-greet from member1,\nand save member1->channel(red one) mapping,\nthen send passive-greet to member1
note right timeline: No matter the following two steps in which order the results will not be affected 
timeline-[#green]>timeline: member2 received the passive-greet from member1,\nand try to save member1->channel(green one) mapping but find mapping exists,\nthen save member1->channel(red one) according address comparation policy
timeline-[#red]>timeline: member1 received the passive-greet from member2,\nand try to save member2->channel(red one) mapping but find mapping exists,\nthen save member2->channel(red one) according address comparation policy
end
alt passive-greet from member1 to member2 arrive first
timeline-[#green]>timeline: member2 received the passive-greet from member1,\nand save member1->channel(green one) mapping
timeline-[#red]>timeline: member2 received the active-greet from member1,\nand try to save member1->channel(red one) mapping but find mapping exists,\nthen save member1-channel(red one) according address comparation policy\nsend passive-greet to member1
timeline-[#red]>timeline: member1 received the passive-greet from member2,\nand try to save member2->channel(red one) mapping but find mapping exists,\nthen save member2->channel(red one) according address comparation policy
end
end
alt active-greet from member1 to member2 arrive first
note right timeline: this case is opposite with the upper case
end
```

**Flowchart**

![HANDSHAKE_CONFLICT](images/HANDSHAKE_CONFLICT.png)

# Exceptional Cases

## Case 1

1. `member1` and `member2` simultaneously detect the leader offline and inquire other members, receiving replies that the leader is not online.
1. Both `member1` and `member2` simultaneously initiate leader elections, each nominating themselves as the leader.
1. The proposal from `member1` reaches other members first, so they all agree, and `member1` becomes the leader.
1. At this point, `member2` has already accepted `member1` as the leader, but due to the previous investigation where everyone said the leader is offline, it also initiates a leader election.

How to avoid it: The version when initiating the election must be the version when initiating the leader status query + 1.

## Case 2

System messages are out of order.

1. The leader sends `JoinClusterResponse` first and then sends `MemberStatusUpdate`.
1. The member receives `JoinClusterResponse` first and then receives `MemberStatusUpdate`.
1. But due to completely asynchronous processing, `MemberStatusUpdate` may be executed before `JoinClusterResponse`.
1. This causes the member's state to become `joining`, although it will reply automatically, it is unnecessary.

## Case 3

Does ClusterAlignment only synchronize with active nodes?

## Case 4

1. A member is in the joining process.
1. The leader handling the joining process receives a LeaderRelieve.
1. Since the member is not in the active state, it does not send ReJoinCluster to the member.
1. Therefore, after the member exceeds 3 times the ttlTimeout, it will attempt to rejoin the cluster.