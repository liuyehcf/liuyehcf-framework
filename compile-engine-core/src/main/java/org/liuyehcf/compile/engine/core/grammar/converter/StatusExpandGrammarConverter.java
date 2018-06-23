package org.liuyehcf.compile.engine.core.grammar.converter;

import org.liuyehcf.compile.engine.core.grammar.definition.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>状态扩展文法转换器</p>
 * <p>将形如 A → a的文法转换成 1. A → ·a 2. A → a·的文法转换器</p>
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class StatusExpandGrammarConverter extends AbstractGrammarConverter {

    public StatusExpandGrammarConverter(Grammar originalGrammar) {
        super(originalGrammar);
    }

    @Override
    protected Grammar doConvert() {
        List<Production> newProductions = new ArrayList<>();

        /*
         * A → B
         *   |
         *   v
         * A → ·B
         * A → B·
         */
        for (Production p : originalGrammar.getProductions()) {
            Symbol a = p.getLeft();

            for (PrimaryProduction pp : p.getPrimaryProductions()) {

                int length = pp.getRight().getSymbols().size();

                /*
                 * 构造新的  length+1个 PrimaryProduction
                 */
                for (int i = 0; i < length + 1; i++) {
                    if (i == 0 && SymbolString.EPSILON_RAW.equals(pp.getRight())) {
                        /*
                         * 特殊处理一下ε产生式。只保留 "A → ε ·"，丢弃"A → · ε"
                         */
                        continue;
                    }
                    newProductions.add(
                            Production.create(
                                    PrimaryProduction.create(
                                            a,
                                            SymbolString.create(
                                                    pp.getRight().getSymbols(),
                                                    i
                                            ),
                                            pp.getSemanticActions()
                                    )

                            )
                    );
                }
            }
        }

        return Grammar.create(
                originalGrammar.getStart(),
                newProductions
        );
    }
}
