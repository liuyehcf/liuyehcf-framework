package com.github.liuyehcf.framework.compile.engine.rg;

import com.github.liuyehcf.framework.compile.engine.GrammarHolder;
import com.github.liuyehcf.framework.compile.engine.grammar.converter.GrammarConverterPipeline;
import com.github.liuyehcf.framework.compile.engine.grammar.converter.GrammarConverterPipelineImpl;
import com.github.liuyehcf.framework.compile.engine.grammar.converter.MergeGrammarConverter;
import com.github.liuyehcf.framework.compile.engine.grammar.converter.SimplificationGrammarConverter;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Grammar;
import com.github.liuyehcf.framework.compile.engine.rg.dfa.Dfa;
import com.github.liuyehcf.framework.compile.engine.rg.nfa.Nfa;
import com.github.liuyehcf.framework.compile.engine.rg.utils.GrammarUtils;

/**
 * 正则构建器
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class RGBuilder implements GrammarHolder {

    /**
     * 文法转换流水线
     */
    private static final GrammarConverterPipeline GRAMMAR_CONVERTER_PIPELINE;

    static {
        GRAMMAR_CONVERTER_PIPELINE = GrammarConverterPipelineImpl
                .builder()
                .registerGrammarConverter(MergeGrammarConverter.class)
                .registerGrammarConverter(SimplificationGrammarConverter.class)
                .build();
    }

    /**
     * 正则文法
     */
    private final Grammar grammar;

    /**
     * nfa自动机
     */
    private Nfa nfa;

    /**
     * dfa自动机
     */
    private Dfa dfa;

    private RGBuilder(Grammar grammar) {
        this.grammar = grammar;
        this.nfa = null;
        this.dfa = null;
    }

    public static RGBuilder compile(Grammar grammar) {

        Grammar convertedGrammar = GRAMMAR_CONVERTER_PIPELINE.convert(grammar);

        return new RGBuilder(convertedGrammar);
    }

    public static RGBuilder compile(String regex) {

        Grammar grammar = GRAMMAR_CONVERTER_PIPELINE.convert(
                GrammarUtils.createGrammarWithRegex(regex)
        );

        return new RGBuilder(grammar);
    }

    @Override
    public Grammar getGrammar() {
        return grammar;
    }

    public Pattern buildNfa() {
        if (nfa == null) {
            nfa = new Nfa(grammar);
        }
        return nfa;
    }

    public Pattern buildDfa() {
        if (dfa == null) {
            if (nfa == null) {
                buildNfa();
            }
            dfa = new Dfa(nfa);
        }
        return dfa;
    }
}
