package com.github.liuyehcf.framework.compile.engine.rg.dfa;

import com.github.liuyehcf.framework.compile.engine.rg.Matcher;
import com.github.liuyehcf.framework.compile.engine.rg.utils.SymbolUtils;
import com.github.liuyehcf.framework.compile.engine.utils.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Dfa匹配器
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class DfaMatcher implements Matcher {

    /**
     * Dfa自动机
     */
    private final Dfa dfa;

    /**
     * 待匹配的输入符号
     */
    private final String input;

    /**
     * group i --> 起始索引 的映射表，闭集
     */
    private Map<Integer, Integer> groupStartIndexes = new HashMap<>();

    /**
     * group i --> 接收索引 的映射表，闭集
     */
    private Map<Integer, Integer> groupEndIndexes = new HashMap<>();

    DfaMatcher(Dfa dfa, String input) {
        if (dfa == null || input == null) {
            throw new NullPointerException();
        }
        this.dfa = dfa;
        this.input = input;
    }

    @Override
    public boolean matches() {
        DfaState curDfaState = dfa.getStartDfaState();
        Assert.assertNotNull(curDfaState);
        for (int i = 0; i < input.length(); i++) {
            for (int group : curDfaState.getGroupStart()) {
                groupStartIndexes.put(group, i);
            }

            for (int group : curDfaState.getGroupReceive()) {
                groupEndIndexes.put(group, i);
            }

            DfaState nextDfaState = curDfaState.getNextDfaStateWithSymbol(
                    SymbolUtils.getAlphabetSymbolWithChar(input.charAt(i))
            );
            if (nextDfaState == null) {
                return false;
            }
            curDfaState = nextDfaState;
        }

        for (int group : curDfaState.getGroupStart()) {
            groupStartIndexes.put(group, input.length());
        }

        for (int group : curDfaState.getGroupReceive()) {
            groupEndIndexes.put(group, input.length());
        }

        Set<Integer> keySets = groupStartIndexes.keySet();
        for (int group : keySets.toArray(new Integer[0])) {
            if (!groupEndIndexes.containsKey(group)) {
                groupStartIndexes.remove(group);
            }
        }
        return curDfaState.canReceive();
    }

    @Override
    public boolean find() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String group(int group) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int groupCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int start(int group) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int end(int group) {
        throw new UnsupportedOperationException();
    }
}
