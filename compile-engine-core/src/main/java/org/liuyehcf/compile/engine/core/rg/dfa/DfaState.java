package org.liuyehcf.compile.engine.core.rg.dfa;

import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.rg.nfa.NfaState;
import org.liuyehcf.compile.engine.core.utils.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Dfa状态
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class DfaState {

    private static int count = 1;

    private final int id = count++;

    /**
     * DfaState描述符，与当前DfaState包含的所有NfaState的id集合相关
     */
    private DfaStateDescription description = new DfaStateDescription(new HashSet<>());

    /**
     * 当前DfaState是否被标记过
     */
    private boolean isMarked = false;

    /**
     * 当前DfaState包含的所有NfaState
     */
    private Set<NfaState> nfaStates = new HashSet<>();

    /**
     * 当前DfaState包含的所有NfaState的所有下一跳输入符号，构建时会用到。当构建完成时，与"nextDfaStateMap.keySet()"一致
     */
    private Set<Symbol> inputSymbols = new HashSet<>();

    /**
     * 邻接节点映射表
     */
    private Map<Symbol, DfaState> nextDfaStateMap = new HashMap<>();

    /**
     * 当前节点作为 group i 的起始节点，那么i位于groupStart中
     */
    private Set<Integer> groupStart = new HashSet<>();

    /**
     * 当前节点作为 group i 的接收节点，那么i位于groupReceive中
     */
    private Set<Integer> groupReceive = new HashSet<>();

    static DfaState createDfaStateWithNfaStates(List<NfaState> nfaStates) {
        LinkedList<NfaState> stack = new LinkedList<>();
        stack.addAll(nfaStates);

        DfaState dfaState = new DfaState();
        dfaState.addNfaStates(nfaStates);

        while (!stack.isEmpty()) {
            NfaState nfaState = stack.pop();

            for (NfaState nextNfaState : nfaState.getNextNfaStatesWithInputSymbol(Symbol.EPSILON)) {
                if (dfaState.addNfaState(nextNfaState)) {
                    stack.push(nextNfaState);
                }
            }
        }

        dfaState.setDescription();

        return dfaState;
    }

    DfaStateDescription getDescription() {
        return description;
    }

    boolean isMarked() {
        return isMarked;
    }

    void setMarked() {
        isMarked = true;
    }

    Set<Integer> getGroupStart() {
        return groupStart;
    }

    Set<Integer> getGroupReceive() {
        return groupReceive;
    }

    public void setStart(int group) {
        Assert.assertFalse(groupStart.contains(group));
        groupStart.add(group);
    }

    public boolean isStart(int group) {
        return groupStart.contains(group);
    }

    public void setReceive(int group) {
        Assert.assertFalse(groupReceive.contains(group));
        groupReceive.add(group);
    }

    boolean canReceive(int group) {
        return groupReceive.contains(group);
    }

    boolean canReceive() {
        return canReceive(0);
    }

    private boolean addNfaState(NfaState nfaState) {
        boolean flag = nfaStates.add(nfaState);
        if (flag) {
            groupStart.addAll(nfaState.getGroupStart());
            groupReceive.addAll(nfaState.getGroupReceive());

            inputSymbols.addAll(nfaState.getAllInputSymbol());
            inputSymbols.remove(Symbol.EPSILON);
        }
        return flag;
    }

    private void addNfaStates(List<NfaState> nfaStates) {
        for (NfaState nfaState : nfaStates) {
            addNfaState(nfaState);
        }
    }

    Set<Symbol> getAllInputSymbols() {
        return inputSymbols;
    }

    List<NfaState> getNextNfaStatesWithInputSymbol(Symbol inputSymbol) {
        List<NfaState> nextNfaStates = new ArrayList<>();
        for (NfaState nfaState : nfaStates) {
            nextNfaStates.addAll(nfaState.getNextNfaStatesWithInputSymbol(inputSymbol));
        }
        return nextNfaStates;
    }

    void addInputSymbolAndNextDfaState(Symbol symbol, DfaState dfaState) {
        Assert.assertFalse(nextDfaStateMap.containsKey(symbol));
        nextDfaStateMap.put(symbol, dfaState);
    }

    DfaState getNextDfaStateWithSymbol(Symbol symbol) {
        if (!nextDfaStateMap.containsKey(symbol)) {
            return null;
        }
        return nextDfaStateMap.get(symbol);
    }

    @Override
    public String toString() {
        return "DfaState[" + id + "]";
    }

    private void setDescription() {
        description = new DfaStateDescription(
                this.nfaStates.stream().map(NfaState::getId).collect(Collectors.toSet())
        );
    }

    void print() {
        Set<DfaState> visited = new HashSet<>();

        LinkedList<DfaState> stack = new LinkedList<>();

        stack.push(this);

        List<DfaState> endDfaStates = new ArrayList<>();

        while (!stack.isEmpty()) {
            DfaState curDfaState = stack.pop();
            if (curDfaState.canReceive()) {
                endDfaStates.add(curDfaState);
            }

            for (Symbol inputSymbol : curDfaState.getAllInputSymbols()) {
                DfaState nextDfaState = curDfaState.getNextDfaStateWithSymbol(inputSymbol);

                Assert.assertNotNull(nextDfaState);

                System.out.println(curDfaState + " (" + inputSymbol + ")-> " + nextDfaState);

                if (visited.add(nextDfaState)) {
                    stack.push(nextDfaState);
                }
            }
        }

        System.out.print("EndState: ");
        endDfaStates.forEach(dfaState -> System.out.print(dfaState + ", "));
        System.out.println("\n");
    }

    @Override
    public int hashCode() {
        return this.description.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DfaState) {
            DfaState that = (DfaState) obj;
            return that.description.equals(this.description);
        }
        return false;
    }
}
