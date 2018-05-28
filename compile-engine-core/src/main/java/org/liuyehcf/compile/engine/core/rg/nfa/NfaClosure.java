package org.liuyehcf.compile.engine.core.rg.nfa;

import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;

import java.util.*;

/**
 * Nfa闭包
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class NfaClosure {
    private static int count = 1;
    private final int id = count++;

    /**
     * 起始节点
     */
    private final NfaState startNfaState;

    /**
     * 结束节点集合
     */
    private List<NfaState> endNfaStates;

    /**
     * 所在的group
     */
    private int group;

    NfaClosure(NfaState startNfaState, List<NfaState> endNfaStates, int initialGroup) {
        if (startNfaState == null) {
            throw new RuntimeException();
        }
        this.startNfaState = startNfaState;
        this.endNfaStates = endNfaStates;
        this.group = initialGroup;
    }

    static NfaClosure getEmptyClosureForGroup(int group) {
        NfaState startNfaState = new NfaState();
        List<NfaState> endNfaStates = new ArrayList<>();
        startNfaState.addInputSymbolAndNextNfaState(
                Symbol.EPSILON, startNfaState
        );
        endNfaStates.add(startNfaState);

        return new NfaClosure(
                startNfaState,
                endNfaStates,
                group);
    }

    public NfaState getStartNfaState() {
        return startNfaState;
    }

    List<NfaState> getEndNfaStates() {
        return endNfaStates;
    }

    void setEndNfaStates(List<NfaState> endNfaStates) {
        this.endNfaStates = endNfaStates;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    void setStartAndReceive(int group) {
        // 设置起始节点
        startNfaState.setStart(group);

        for (NfaState endNfaState : endNfaStates) {
            endNfaState.setReceive(group);
        }
    }

    @Override
    public NfaClosure clone() {
        Map<NfaState, NfaState> oldAndNewNfaStateMap = new HashMap<>(16);

        // 镜像创建NfaState节点，属性值先不拷贝
        dfsMirrorCopy(getStartNfaState(), oldAndNewNfaStateMap);

        // 这里拷贝所有NfaState的属性值，并创建NfaClosure
        return mirrorCopyStatus(oldAndNewNfaStateMap);
    }

    private void dfsMirrorCopy(NfaState curNfaState, Map<NfaState, NfaState> oldAndNewNfaStateMap) {
        if (oldAndNewNfaStateMap.containsKey(curNfaState)) {
            return;
        }
        oldAndNewNfaStateMap.put(curNfaState, new NfaState());

        for (Symbol inputSymbol : curNfaState.getAllInputSymbol()) {
            for (NfaState nextNfaState : curNfaState.getNextNfaStatesWithInputSymbol(inputSymbol)) {
                dfsMirrorCopy(nextNfaState, oldAndNewNfaStateMap);
            }
        }
    }

    private NfaClosure mirrorCopyStatus(Map<NfaState, NfaState> oldAndNewNfaStateMap) {
        for (Map.Entry<NfaState, NfaState> entry : oldAndNewNfaStateMap.entrySet()) {

            NfaState curNfaState = entry.getKey();
            NfaState clonedCurNfaState = entry.getValue();

            // 复制 nextNfaStatesMap
            for (Symbol inputSymbol : curNfaState.getAllInputSymbol()) {
                for (NfaState nextNfaState : curNfaState.getNextNfaStatesWithInputSymbol(inputSymbol)) {
                    NfaState clonedNextNfaState = oldAndNewNfaStateMap.get(nextNfaState);
                    assert clonedCurNfaState != null;
                    clonedCurNfaState.addInputSymbolAndNextNfaState(inputSymbol, clonedNextNfaState);
                }
            }

            // 复制 groupStart以及groupReceive
            for (int group : curNfaState.getGroupStart()) {
                clonedCurNfaState.setStart(group);
            }

            for (int group : curNfaState.getGroupReceive()) {
                clonedCurNfaState.setReceive(group);
            }
        }

        NfaState clonedStartNfaState = oldAndNewNfaStateMap.get(startNfaState);
        List<NfaState> clonedEndNfaStates = new ArrayList<>();

        for (NfaState endNfaState : endNfaStates) {
            NfaState copiedEndNfaState = oldAndNewNfaStateMap.get(endNfaState);
            clonedEndNfaStates.add(copiedEndNfaState);
        }

        return new NfaClosure(clonedStartNfaState, clonedEndNfaStates, group);
    }

    void print() {
        Set<NfaState> visited = new HashSet<>();

        LinkedList<NfaState> stack = new LinkedList<>();

        if (getStartNfaState() != null) {
            visited.add(getStartNfaState());
            stack.push(getStartNfaState());
        }

        while (!stack.isEmpty()) {
            NfaState curNfaState = stack.pop();

            for (Symbol inputSymbol : curNfaState.getAllInputSymbol()) {
                for (NfaState nextNfaState : curNfaState.getNextNfaStatesWithInputSymbol(inputSymbol)) {
                    System.out.println(curNfaState + " (" + inputSymbol + ")-> " + nextNfaState);

                    if (visited.add(nextNfaState)) {
                        stack.push(nextNfaState);
                    }
                }
            }
        }

        System.out.print("EndStates: ");
        getEndNfaStates().forEach(nfaState -> System.out.print(nfaState + ", "));
        System.out.println("\n");

        visited.forEach(nfaState -> System.out.println(nfaState.getStatus()));
    }

    @Override
    public String toString() {
        return "NfaClosure[" + id + "]";
    }
}
