package com.github.liuyehcf.framework.compile.engine.rg.nfa;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;

import java.util.*;

/**
 * Nfa状态
 * <p>该类会作为Hash表的键值，直接利用Object的equals方法与hashCode方法</p>
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class NfaState {

    private static int count = 1;
    private final int id = count++;
    private final Set<NfaState> NONE = Collections.unmodifiableSet(new HashSet<>());

    /**
     * 当前节点作为 group i 的起始节点，那么i位于groupStart中
     */
    private Set<Integer> groupStart = new HashSet<>();

    /**
     * 当前节点作为 group i 的接收节点，那么i位于groupReceive中
     */
    private Set<Integer> groupReceive = new HashSet<>();

    /**
     * <p>输入符号 -> 后继节点集合 的映射表。这里用LinkedHashMap保证了节点加入的先后顺序，详见"NfaBuildIterator.parallel"方法</p>
     * <p>此外，如果对于一个输入符号有多个后继节点，那么这些后继节点的先后次序也需要维护，因此用的是LinkedHashSet</p>
     */
    private Map<Symbol, Set<NfaState>> nextNfaStatesMap = new LinkedHashMap<>();

    public int getId() {
        return id;
    }

    public Set<Integer> getGroupStart() {
        return groupStart;
    }

    public Set<Integer> getGroupReceive() {
        return groupReceive;
    }

    public void setStart(int group) {
        Assert.assertFalse(groupStart.contains(group));
        groupStart.add(group);
    }

    public boolean isStart(int group) {
        return groupStart.contains(group);
    }

    void setReceive(int group) {
        Assert.assertFalse(groupReceive.contains(group));
        groupReceive.add(group);
    }

    boolean canReceive(int group) {
        return groupReceive.contains(group);
    }

    boolean canReceive() {
        return canReceive(0);
    }

    public Set<Symbol> getAllInputSymbol() {
        return nextNfaStatesMap.keySet();
    }

    public Set<NfaState> getNextNfaStatesWithInputSymbol(Symbol symbol) {
        return nextNfaStatesMap.getOrDefault(symbol, NONE);
    }

    void addInputSymbolAndNextNfaState(Symbol symbol, NfaState nextNfaState) {
        if (!nextNfaStatesMap.containsKey(symbol)) {
            /*
             * 同理，这里也需要使用LinkedHashSet来保证同一个符号下的节点先后次序，详见"NfaBuildIterator.parallel"方法
             */
            nextNfaStatesMap.put(symbol, new LinkedHashSet<>());
        }
        nextNfaStatesMap.get(symbol).add(nextNfaState);
    }

    @Override
    public String toString() {
        return "NfaState[" + id + "]";
    }

    String getStatus() {
        return toString() + '\n' +
                ", groupStart" + groupStart + '\n' +
                ", groupReceive" + groupReceive + '\n';
    }

    @Override
    public int hashCode() {
        return 31 * id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NfaState) {
            NfaState that = (NfaState) obj;
            return this.id == that.id;
        }
        return false;
    }
}
