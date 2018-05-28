package org.liuyehcf.compile.engine.core.rg.nfa;

import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.rg.Matcher;
import org.liuyehcf.compile.engine.core.rg.utils.SymbolUtils;
import org.liuyehcf.compile.engine.core.utils.AssertUtils;
import org.liuyehcf.compile.engine.core.utils.Pair;
import org.liuyehcf.compile.engine.core.utils.Tuple;

import java.util.*;

/**
 * Nfa匹配器
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class NfaMatcher implements Matcher {

    /**
     * Nfa自动机
     */
    private final Nfa nfa;

    /**
     * 待匹配的输入字符串
     */
    private final String input;

    /**
     * groupStartIndexes[i]: group i 的起始索引，闭
     */
    private int[] groupStartIndexes = null;

    /**
     * groupEndIndexes[i]: group i 的终止索引，开
     */
    private int[] groupEndIndexes = null;

    /**
     * <p>当前状态所在捕获组的信息集合（某个时刻可以位于多个捕获组中）</p>
     * <p>curGroupStatus[i]: 捕获组i的起始和终止索引(groupStartIndex, groupEndIndex)；若为null，则说明不在捕获组i中</p>
     * <p>这个数据结构为了解决"(a*)+"匹配"a"时，捕获组的问题（正确情况下应该返回""）</p>
     */
    private Pair<Integer, Integer>[] curGroupStatus = null;

    /**
     * 匹配的区间集合
     */
    private List<Pair<Integer, Integer>> matchIntervals;

    /**
     * 目前进行匹配操作的子串
     */
    private String subInput;

    /**
     * subInput对于input的偏移量
     */
    private int offset;

    /**
     * 匹配子串索引
     */
    private int indexOfMatchIntervals;

    NfaMatcher(Nfa nfa, String input) {
        if (nfa == null || input == null) {
            throw new NullPointerException();
        }
        this.nfa = nfa;
        this.input = input;
    }

    @Override
    public boolean matches() {
        return doMatch(0, input.length()) != null;
    }

    private NfaState doMatch(int start, int end) {

        beforeMatch(start, end);

        NfaState result = isMatchDfs(nfa.getNfaClosure().getStartNfaState(), 0, new HashSet<>());

        afterMatch(result);

        return result;
    }

    @SuppressWarnings("unchecked")
    private void beforeMatch(int start, int end) {
        this.subInput = input.substring(start, end);
        this.offset = start;

        groupStartIndexes = new int[groupCount() + 1];
        groupEndIndexes = new int[groupCount() + 1];
        curGroupStatus = new Pair[groupCount() + 1];

        Arrays.fill(groupStartIndexes, -1);
        Arrays.fill(groupEndIndexes, -1);
    }

    private void afterMatch(NfaState result) {
        if (result == null) {
            groupStartIndexes = null;
            groupEndIndexes = null;
        } else {
            for (int group = 0; group <= groupCount(); group++) {
                if (groupEndIndexes[group] == -1) {
                    groupStartIndexes[group] = -1;
                }
            }
        }

        curGroupStatus = null;
    }

    private NfaState isMatchDfs(NfaState curNfaState, int index, Set<String> visitedNfaState) {
        Tuple<int[], int[], Pair<Integer, Integer>[]> tuple = dfsStatusMark(curNfaState, index);

        // 首先走非ε边，贪婪模式
        if (index != subInput.length()) {
            // 从当前节点出发，经过非ε边的next节点集合
            Set<NfaState> nextStates = curNfaState.getNextNfaStatesWithInputSymbol(
                    SymbolUtils.getAlphabetSymbolWithChar(subInput.charAt(index)));

            for (NfaState nextState : nextStates) {

                NfaState result;
                if ((result = isMatchDfs(nextState, index + 1, visitedNfaState)) != null) {
                    return result;
                }
            }
        }

        if (index == subInput.length() && curNfaState.canReceive()) {
            return curNfaState;
        } else {

            // 从当前节点出发，经过ε边的后继节点集合
            Set<NfaState> epsilonNextStates = curNfaState.getNextNfaStatesWithInputSymbol(
                    Symbol.EPSILON
            );
            for (NfaState nextState : epsilonNextStates) {
                // 为了避免重复经过相同的 ε边，每次访问ε边，给一个标记
                // 在匹配目标字符串的不同位置时，允许经过相同的ε边
                String curStateString = statusInfo(nextState, index);

                if (visitedNfaState.add(curStateString)) {
                    NfaState result;
                    if ((result = isMatchDfs(nextState, index, visitedNfaState)) != null) {
                        return result;
                    }
                    visitedNfaState.remove(curStateString);
                }
            }

            // 仅仅失败时需要回溯
            dfsStatusBackTrace(tuple);
            return null;
        }
    }

    private Tuple<int[], int[], Pair<Integer, Integer>[]> dfsStatusMark(NfaState curNfaState, int index) {
        // 由于需要回溯，因此保留一下原始状态
        Tuple<int[], int[], Pair<Integer, Integer>[]> tuple = new Tuple<>(
                groupStartIndexes.clone(),
                groupEndIndexes.clone(),
                curGroupStatus.clone()
        );

        if (!curNfaState.getGroupStart().isEmpty()) {
            for (int group : curNfaState.getGroupStart()) {
                groupStartIndexes[group] = index;
            }
        }

        if (!curNfaState.getGroupReceive().isEmpty()) {
            for (int group : curNfaState.getGroupReceive()) {
                groupEndIndexes[group] = index;
            }
        }

        Arrays.fill(curGroupStatus, null);

        for (int group = 0; group <= groupCount(); group++) {
            int startIndex = groupStartIndexes[group];
            int endIndex = groupEndIndexes[group];

            if (startIndex != -1) {
                curGroupStatus[group] = new Pair<>(startIndex, endIndex);
            }
        }

        return tuple;
    }

    private void dfsStatusBackTrace(Tuple<int[], int[], Pair<Integer, Integer>[]> tuple) {
        groupStartIndexes = tuple.getFirst();
        groupEndIndexes = tuple.getSecond();
        curGroupStatus = tuple.getThird();
    }

    private String statusInfo(NfaState nfaState, int index) {
        StringBuilder sb = new StringBuilder();

        // 这里构造的节点状态信息包含了当前所在的NfaState，目标串的位置，当前所在的group以及其起始和终止索引

        sb.append(nfaState.toString())
                .append(',')
                .append(index)
                .append(',');

        sb.append('[');
        for (int group = 0; group <= groupCount(); group++) {
            if (curGroupStatus[group] == null) {
                continue;
            }

            int startIndex = curGroupStatus[group].getFirst();
            int endIndex = curGroupStatus[group].getSecond();

            sb.append('(')
                    .append(group)
                    .append(',')
                    .append(startIndex)
                    .append(',')
                    .append(endIndex)
                    .append(',')
                    .append(')')
                    .append(',');
        }
        sb.append(']');

        return sb.toString();
    }

    @Override
    public boolean find() {
        if (matchIntervals == null) {
            initMatchIntervals();
        }

        if (indexOfMatchIntervals < matchIntervals.size()) {

            Pair<Integer, Integer> interval = matchIntervals.get(indexOfMatchIntervals);

            doMatch(interval.getFirst(),
                    interval.getSecond()
            );

            indexOfMatchIntervals++;

            return true;
        }
        return false;
    }

    private void initMatchIntervals() {
        /*
         * 该映射表用来解决"(a)|(b)|(ab)" 匹配 "ab" 的问题
         * 以 regex="(a)|(b)|(ab)" 为例解释一下，这个正则表达式构造成NfaClosure之后，有三个终止节点
         * 字符串"ab"会有三个匹配子串 "a"[0,1)  "b"[1,2)  "ab"[0,2)，虽然"ab"完全包含了"a"和"b"，
         * 但是由于"a"先出现，且"ab"并不是"a"的贪婪子串（这两个子串在不同的状态被接受），因此优先选取"a"，因此"ab"子串就被丢弃了
         *
         * 对于"a+"匹配"aa"的问题
         * 字符串"aa"会有三个匹配子串 "a"[0,1)  "a"[1,2)  "aa"[0,2)，此时，这三个子串在相同的状态被接受，即
         * "aa"[0,2) 是 "a"[0,1)  "a"[1,2) 的贪婪子串，故而"aa"被选取，其余两个"a"被丢弃
         */
        Map<Pair<Integer, Integer>, NfaState> intervalNfaStateMap = new HashMap<>(16);

        matchIntervals = new ArrayList<>();

        if (input.length() == 0) {
            if (matches()) {
                matchIntervals.add(new Pair<>(0, 0));
            }
        }

        for (int startIndex = 0; startIndex < input.length(); startIndex++) {
            for (int endIndex = startIndex + 1; endIndex <= input.length(); endIndex++) {
                NfaState result;
                if ((result = doMatch(startIndex, endIndex)) != null) {
                    Pair<Integer, Integer> interval = new Pair<>(startIndex, endIndex);
                    matchIntervals.add(interval);
                    intervalNfaStateMap.put(interval, result);
                }
            }
        }

        if (matchIntervals.isEmpty()) {
            return;
        }

        // 目前仅支持以贪婪模式查询匹配的子串
        // 首先排序
        matchIntervals.sort((o1, o2) -> {
            if (o1.getFirst() < o2.getFirst()) {
                return -1;
            } else if (o1.getFirst() > o2.getFirst()) {
                return 1;
            } else {
                if (o1.getSecond() < o2.getSecond()) {
                    return -1;
                } else if (o1.getSecond() > o2.getSecond()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        // 然后合并包含的区间，例如[1,6)包含着[1,2] [3,5)，那么删去[1,2] [3,5)两个区间
        List<Pair<Integer, Integer>> filteredMatchIntervals = new ArrayList<>();

        Pair<Integer, Integer> preInterval = matchIntervals.get(0);

        for (int i = 1; i < matchIntervals.size(); i++) {
            Pair<Integer, Integer> nextInterval = matchIntervals.get(i);

            // 当区间包含时
            if (preInterval.getFirst() >= nextInterval.getFirst()
                    && preInterval.getSecond() <= nextInterval.getSecond()) {
                // 如果终止状态不同，说明不是*或者+之类，可能是"(a)|(b)|(ab)"匹配"ab"这种情况，那么需要保留小的区间；否则就是"a*"，匹配aa，保留大的区间
                if (intervalNfaStateMap.get(preInterval).equals(intervalNfaStateMap.get(nextInterval))) {
                    preInterval = nextInterval;
                }
            }

            // 若区间完全分离
            if (nextInterval.getFirst() >= preInterval.getSecond()) {
                filteredMatchIntervals.add(preInterval);
                preInterval = nextInterval;
            }

        }

        filteredMatchIntervals.add(preInterval);

        matchIntervals = filteredMatchIntervals;
    }

    @Override
    public String group(int group) {
        if (groupStartIndexes == null) {
            AssertUtils.assertNull(groupEndIndexes);
            throw new IllegalStateException("No match found");
        }
        if (group < 0 || group > groupCount()) {
            throw new IndexOutOfBoundsException("No group " + group);
        }
        if (groupStartIndexes[group] == -1
                || groupEndIndexes[group] == -1) {
            return null;
        }
        return subInput.substring(
                groupStartIndexes[group],
                groupEndIndexes[group]
        );
    }

    @Override
    public int groupCount() {
        return nfa.groupCount();
    }

    @Override
    public int start(int group) {
        if (groupStartIndexes == null) {
            AssertUtils.assertNull(groupEndIndexes);
            throw new IllegalStateException("No match available");
        }
        if (group < 0 || group > groupCount()) {
            throw new IndexOutOfBoundsException("No group " + group);
        }
        return groupStartIndexes[group] + offset;
    }

    @Override
    public int end(int group) {
        if (groupStartIndexes == null) {
            AssertUtils.assertNull(groupEndIndexes);
            throw new IllegalStateException("No match available");
        }
        if (group < 0 || group > groupCount()) {
            throw new IndexOutOfBoundsException("No group " + group);
        }
        return groupEndIndexes[group] + offset;
    }
}
