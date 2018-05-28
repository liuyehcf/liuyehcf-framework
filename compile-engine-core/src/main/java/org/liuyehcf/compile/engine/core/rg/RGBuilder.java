package org.liuyehcf.compile.engine.core.rg;

import org.liuyehcf.compile.engine.core.GrammarHolder;
import org.liuyehcf.compile.engine.core.grammar.converter.GrammarConverterPipeline;
import org.liuyehcf.compile.engine.core.grammar.converter.GrammarConverterPipelineImpl;
import org.liuyehcf.compile.engine.core.grammar.converter.MergeGrammarConverter;
import org.liuyehcf.compile.engine.core.grammar.converter.SimplificationGrammarConverter;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.rg.dfa.Dfa;
import org.liuyehcf.compile.engine.core.rg.nfa.Nfa;
import org.liuyehcf.compile.engine.core.rg.utils.GrammarUtils;

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
