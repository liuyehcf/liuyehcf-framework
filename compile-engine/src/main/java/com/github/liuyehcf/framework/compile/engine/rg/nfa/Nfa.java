package com.github.liuyehcf.framework.compile.engine.rg.nfa;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.compile.engine.GrammarHolder;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Grammar;
import com.github.liuyehcf.framework.compile.engine.rg.Matcher;
import com.github.liuyehcf.framework.compile.engine.rg.Pattern;
import com.github.liuyehcf.framework.compile.engine.rg.utils.GrammarUtils;
import com.github.liuyehcf.framework.compile.engine.utils.Pair;

/**
 * Nfa自动机
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class Nfa implements Pattern, GrammarHolder {

    /**
     * 正则文法
     */
    private final Grammar grammar;

    /**
     * NfaClosure
     */
    private NfaClosure nfaClosure;

    /**
     * 捕获组数量
     */
    private int groupCount;

    public Nfa(Grammar grammar) {
        this.grammar = grammar;
        init();
    }

    public NfaClosure getNfaClosure() {
        return nfaClosure;
    }

    int groupCount() {
        return groupCount;
    }

    private void init() {
        Pair<NfaClosure, Integer> pair = NfaBuildIterator.createNfaClosure(
                GrammarUtils.extractSymbolsFromGrammar(grammar));

        nfaClosure = pair.getFirst();
        this.groupCount = pair.getSecond();
    }

    @Override
    public Grammar getGrammar() {
        return grammar;
    }

    @Override
    public Matcher matcher(String input) {
        return new NfaMatcher(this, input);
    }

    public void print() {
        Assert.assertNotNull(nfaClosure);
        nfaClosure.print();
    }
}
