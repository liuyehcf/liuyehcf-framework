package org.liuyehcf.compile.engine.core.rg.nfa;

import org.liuyehcf.compile.engine.core.GrammarHolder;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.rg.Matcher;
import org.liuyehcf.compile.engine.core.rg.Pattern;
import org.liuyehcf.compile.engine.core.rg.utils.GrammarUtils;
import org.liuyehcf.compile.engine.core.utils.AssertUtils;
import org.liuyehcf.compile.engine.core.utils.Pair;

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
        AssertUtils.assertNotNull(nfaClosure);
        nfaClosure.print();
    }
}
