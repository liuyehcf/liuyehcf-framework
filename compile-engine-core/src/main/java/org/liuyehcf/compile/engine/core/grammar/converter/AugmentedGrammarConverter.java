package org.liuyehcf.compile.engine.core.grammar.converter;

import org.liuyehcf.compile.engine.core.grammar.definition.*;
import org.liuyehcf.compile.engine.core.utils.Assert;
import org.liuyehcf.compile.engine.core.utils.ListUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 增广文法转换器
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class AugmentedGrammarConverter extends AbstractGrammarConverter implements Serializable {

    public AugmentedGrammarConverter(Grammar originalGrammar) {
        super(originalGrammar);
    }

    @Override
    protected Grammar doConvert() {
        List<Production> newProductions = new ArrayList<>();

        Assert.assertFalse(originalGrammar.getProductions().isEmpty());

        newProductions.add(
                Production.create(
                        PrimaryProduction.create(
                                Symbol.START,
                                SymbolString.create(
                                        ListUtils.of(originalGrammar.getStart())
                                ),
                                // todo 增广产生式无需语义动作
                                null
                        )
                )
        );

        newProductions.addAll(originalGrammar.getProductions());

        return Grammar.create(
                Symbol.START,
                newProductions
        );
    }
}
