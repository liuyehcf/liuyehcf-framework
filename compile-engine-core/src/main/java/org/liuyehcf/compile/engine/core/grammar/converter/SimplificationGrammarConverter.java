package org.liuyehcf.compile.engine.core.grammar.converter;

import org.liuyehcf.compile.engine.core.grammar.definition.*;
import org.liuyehcf.compile.engine.core.rg.utils.SymbolUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.*;


/**
 * 转换成只有一个产生式的文法转换器，一般用于正则文法
 *
 * @author hechenfeng
 * @date 2018/04/16
 */

public class SimplificationGrammarConverter extends AbstractGrammarConverter {

    /**
     * 从非终结符 -> PrimaryProduction 的映射表
     */
    private Map<Symbol, SymbolString> symbolStringMap = new HashMap<>();

    /**
     * 按依赖关系排序的非终结符集合
     */
    private List<Symbol> sortedNonTerminators = new ArrayList<>();

    /**
     * 从非终结符 -> Production 的映射表
     */
    private Map<Symbol, Production> productionMap = new HashMap<>();

    public SimplificationGrammarConverter(Grammar originalGrammar) {
        super(originalGrammar);
    }

    @Override
    protected Grammar doConvert() {
        init();

        checkIfFirstProductionContainsNonTerminator();

        iterativeReplacement();

        Symbol lastNonTerminator = sortedNonTerminators.get(sortedNonTerminators.size() - 1);

        return Grammar.create(
                lastNonTerminator,
                Production.create(
                        PrimaryProduction.create(
                                lastNonTerminator,
                                symbolStringMap.get(lastNonTerminator),
                                // todo 正则文法无需语义动作
                                null
                        )
                )
        );
    }

    /**
     * 初始化一些字段，包括非终结符集合，Production映射表
     */
    private void init() {
        for (Production p : originalGrammar.getProductions()) {
            Symbol nonTerminator = p.getLeft();

            assertFalse(productionMap.containsKey(nonTerminator));

            this.sortedNonTerminators.add(nonTerminator);
            productionMap.put(nonTerminator, p);
        }

        initSortedNonTerminators();
    }

    private void initSortedNonTerminators() {
        /*
         * 有向边，从key指向value
         */
        Map<Symbol, List<Symbol>> edges = new HashMap<>(16);

        /*
         * 顶点的度，度为0的顶点才能访问
         */
        Map<Symbol, Integer> degrees = new HashMap<>(16);

        /*
         * 初始化edges以及degrees
         */
        for (Symbol symbol : productionMap.keySet()) {
            edges.put(symbol, new ArrayList<>());
            degrees.put(symbol, 0);
        }

        for (Map.Entry<Symbol, Production> entry : productionMap.entrySet()) {
            Symbol toSymbol = entry.getKey();

            for (PrimaryProduction pp : entry.getValue().getPrimaryProductions()) {
                for (Symbol fromSymbol : pp.getRight().getSymbols()) {
                    if (!fromSymbol.isTerminator()) {
                        edges.get(fromSymbol).add(toSymbol);

                        degrees.put(toSymbol, degrees.get(toSymbol) + 1);
                    }
                }
            }
        }

        List<Symbol> visitedSymbol = new ArrayList<>();

        traverseDirectedGraph(edges, degrees, visitedSymbol);

        assertTrue(visitedSymbol.size() == productionMap.size());

        this.sortedNonTerminators = visitedSymbol;
    }

    /**
     * 检查正则文法第一条语句是否合法 TODO
     */
    private void checkIfFirstProductionContainsNonTerminator() {
        Production p = productionMap.get(sortedNonTerminators.get(0));

        assertTrue(p.getPrimaryProductions().size() == 1);
        PrimaryProduction pp = p.getPrimaryProductions().get(0);

        for (Symbol symbol : pp.getRight().getSymbols()) {
            /*
             * 正则语法第一条产生式不能包含非终结符
             */
            assertTrue(symbol.isTerminator());
        }
    }

    /**
     * 用第1~(i-1) 个产生式替换掉第i个产生式中的非终结符
     */
    private void iterativeReplacement() {
        for (Symbol nonTerminator : sortedNonTerminators) {

            List<Symbol> modifiedSymbols = new ArrayList<>();

            Production p = productionMap.get(nonTerminator);
            assertTrue(p.getPrimaryProductions().size() == 1);
            PrimaryProduction pp = p.getPrimaryProductions().get(0);

            for (Symbol symbol : pp.getRight().getSymbols()) {
                if (!symbol.isTerminator()) {
                    modifiedSymbols.add(SymbolUtils.LEFT_SMALL_PARENTHESIS);

                    /*
                     * 当前产生式包含的非终结符必定在之前循环中已经出现过
                     */
                    assertNotNull(symbolStringMap.get(symbol));

                    modifiedSymbols.addAll(symbolStringMap.get(symbol).getSymbols());

                    modifiedSymbols.add(SymbolUtils.RIGHT_SMALL_PARENTHESIS);
                } else {
                    modifiedSymbols.add(symbol);
                }
            }

            symbolStringMap.put(nonTerminator, SymbolString.create(modifiedSymbols));
        }
    }
}
