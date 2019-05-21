package com.github.liuyehcf.framework.compile.engine.grammar.converter;

import com.github.liuyehcf.framework.compile.engine.grammar.definition.*;
import com.github.liuyehcf.framework.compile.engine.utils.ListUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.liuyehcf.framework.compile.engine.utils.Assert.assertFalse;
import static com.github.liuyehcf.framework.compile.engine.utils.Assert.assertTrue;

/**
 * 左递归消除以及提取左公因子文法转换 LRE：Left recursive elimination ELF：Extract Left Factory
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class LreElfGrammarConverter extends AbstractGrammarConverter implements Serializable {

    /**
     * 非终结符 -> 产生式 的映射表
     */
    private Map<Symbol, Production> productionMap;

    /**
     * 根据依赖关系将非终结符进行排序后的结果（有向图遍历）
     */
    private List<Symbol> sortedNonTerminators;

    public LreElfGrammarConverter(Grammar originalGrammar) {
        super(originalGrammar);
    }

    @Override
    protected Grammar doConvert() {
        check();

        init();

        for (int i = 0; i < sortedNonTerminators.size(); i++) {
            Symbol ai = sortedNonTerminators.get(i);
            for (int j = 0; j < i; j++) {
                Symbol aj = sortedNonTerminators.get(j);
                /*
                 * 如果非终结符I的产生式里第一个非终结符是J，那么用J的产生式替换掉非终结符J
                 */
                substitutionNonTerminator(ai, aj);
            }
            /*
             * 消除非终结符I的直接左递归
             */
            eliminateDirectLeftRecursion(ai);

            /*
             * 提取左公因子
             */
            extractLeftCommonFactor(ai);
        }

        return createNewGrammar();
    }

    /**
     * 检查待转换的文法是否符合LL1文法的要求
     */
    private void check() {

    }

    /**
     * 初始化一些变量
     */
    private void init() {
        if (productionMap == null) {
            productionMap = new HashMap<>(16);
            for (Production p : originalGrammar.getProductions()) {
                Symbol nonTerminator = p.getLeft();
                assertFalse(nonTerminator.isTerminator());

                /*
                 * 必然不包含相同左部的产生式（在Grammar构造时已经合并过了）
                 */
                assertFalse(productionMap.containsKey(nonTerminator));
                productionMap.put(nonTerminator, p);
            }
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
                List<Symbol> symbols = pp.getRight().getSymbols();

                assertFalse(symbols.isEmpty());

                if (!symbols.get(0).isTerminator()
                        && !symbols.get(0).equals(toSymbol)) {
                    Symbol fromSymbol = symbols.get(0);

                    assertTrue(edges.containsKey(fromSymbol));

                    edges.get(fromSymbol).add(toSymbol);

                    degrees.put(toSymbol, degrees.get(toSymbol) + 1);
                }
            }
        }

        List<Symbol> visitedSymbol = new ArrayList<>();

        traverseDirectedGraph(edges, degrees, visitedSymbol);

        assertTrue(visitedSymbol.size() == productionMap.size());

        this.sortedNonTerminators = visitedSymbol;
    }

    /**
     * 将已消除直接/间接左递归的非终结符_B的产生式，代入左部为非终结符_A的产生式中 A → Bα
     *
     * @param a 被替换的非终结符
     * @param b 已消除左递归的非终结符
     */
    private void substitutionNonTerminator(Symbol a, Symbol b) {

        Production pa = productionMap.get(a);
        Production pb = productionMap.get(b);

        /*
         * 标记是否发生替换
         */
        boolean isSubstituted = false;

        List<PrimaryProduction> ppaModified = new ArrayList<>();

        /*
         * 遍历产生式I的每一个子产生式
         */
        for (PrimaryProduction ppa : pa.getPrimaryProductions()) {
            List<Symbol> symbolsOfPPA = ppa.getRight().getSymbols();

            assertFalse(symbolsOfPPA.isEmpty());

            /*
             * 如果子产生式第一个符号是symbolJ，那么进行替换
             */
            if (symbolsOfPPA.get(0).equals(b)) {
                isSubstituted = true;

                /*
                 * 遍历终结符J的每个子产生式
                 */
                for (PrimaryProduction ppb : pb.getPrimaryProductions()) {

                    ppaModified.add(
                            PrimaryProduction.create(
                                    a,
                                    SymbolString.create(
                                            ListUtils.of(
                                                    ppb.getRight().getSymbols(),
                                                    ListUtils.subListExceptFirstElement(symbolsOfPPA)
                                            )
                                    ),
                                    // todo 由于LL1分析法会改造文法，这样一来定义文法时的Operator就无效了（有位置信息）
                                    null
                            )
                    );
                }

            } else {
                ppaModified.add(ppa);
            }
        }

        if (isSubstituted) {
            productionMap.put(a,
                    Production.create(
                            ppaModified)
            );
        }
    }

    /**
     * 消除直接左递归，将产生式p1改造成p2 p1: A  → Aα1|Aα2|...|Aαn|β1|β2|...|βm p2: A  → β1A′|β2A′|...|βmA′ p3: A′ →
     * α1A′|α2A′|...|αnA′|ε
     *
     * @param a 产生式左侧非终结符
     */
    private void eliminateDirectLeftRecursion(Symbol a) {
        Production p1 = productionMap.get(a);

        /*
         * β1|β2|...|βm
         */
        List<PrimaryProduction> betas = new ArrayList<>();

        /*
         * Aα1|Aα2|...|Aαn
         */
        List<PrimaryProduction> alphas = new ArrayList<>();

        for (PrimaryProduction pp1 : p1.getPrimaryProductions()) {
            List<Symbol> symbols = pp1.getRight().getSymbols();

            assertFalse(symbols.isEmpty());

            if (symbols.get(0).equals(a)) {
                alphas.add(pp1);
            } else {
                betas.add(pp1);
            }
        }

        if (alphas.isEmpty()) {
            return;
        }

        List<PrimaryProduction> pp3 = new ArrayList<>();

        Symbol aPrimed = createPrimedSymbolFor(a);

        for (PrimaryProduction ppAlpha : alphas) {

            pp3.add(
                    /*
                     * αiA′
                     */
                    PrimaryProduction.create(
                            aPrimed,
                            SymbolString.create(
                                    ListUtils.of(
                                            /*
                                             * αi
                                             */
                                            ListUtils.subListExceptFirstElement(ppAlpha.getRight().getSymbols()),
                                            /*
                                             * A′
                                             */
                                            aPrimed
                                    )
                            ),
                            // todo 由于LL1分析法会改造文法，这样一来定义文法时的Operator就无效了（有位置信息）
                            null
                    )
            );
        }

        /*
         * ε
         */
        pp3.add(
                PrimaryProduction.create(
                        aPrimed,
                        SymbolString.create(Symbol.EPSILON),
                        // todo 由于LL1分析法会改造文法，这样一来定义文法时的Operator就无效了（有位置信息）
                        null
                )
        );

        Production p3 = Production.create(
                pp3
        );

        List<PrimaryProduction> pp2 = new ArrayList<>();

        for (PrimaryProduction ppBeta : betas) {

            /*
             * 构造βmA′
             */
            pp2.add(
                    PrimaryProduction.create(
                            a,
                            SymbolString.create(
                                    ListUtils.of(
                                            /*
                                             * βm
                                             */
                                            ppBeta.getRight().getSymbols(),
                                            /*
                                             * A′
                                             */
                                            aPrimed
                                    )
                            ),
                            // todo 由于LL1分析法会改造文法，这样一来定义文法时的Operator就无效了（有位置信息）
                            null
                    )

            );
        }

        productionMap.put(aPrimed, p3);

        Production p2 = Production.create(
                pp2
        );

        productionMap.put(a, p2);
    }

    /**
     * 提取左公因子，直到任意两个子产生式没有公共前缀 p1: A  → aβ1|aβ2|...|aβn|γ1|γ2|...|γm p2: A  → aA′|γ1|γ2|...|γm p3: A′ → β1|β2|...|βn
     *
     * @param a 产生式左部的非终结符
     */
    private void extractLeftCommonFactor(Symbol a) {

        Production p1 = productionMap.get(a);

        /*
         * 所有平凡前缀计数
         */
        Map<Symbol, Integer> commonPrefixes = new HashMap<>(16);

        /*
         * 初始化commonPrefixes
         */
        for (PrimaryProduction pp1 : p1.getPrimaryProductions()) {
            List<Symbol> symbols = pp1.getRight().getSymbols();

            assertTrue(!symbols.isEmpty() && symbols.get(0).isTerminator());

            Symbol firstSymbol = symbols.get(0);

            /*
             * 跳过非平凡前缀
             */
            if (Symbol.EPSILON.equals(firstSymbol)) {
                continue;
            }

            if (!commonPrefixes.containsKey(firstSymbol)) {
                commonPrefixes.put(firstSymbol, 0);
            }

            commonPrefixes.put(firstSymbol, commonPrefixes.get(firstSymbol) + 1);
        }

        filterPrefixWithSingleCount(commonPrefixes);

        /*
         * 循环提取左公因子
         */
        if (!commonPrefixes.isEmpty()) {

            /*
             * 产生一个异变符号（例如，A′），同时需要保证是没被使用过的
             */
            Symbol aPrimed = createPrimedSymbolFor(a);

            Symbol prefixSymbol = commonPrefixes.keySet().iterator().next();

            List<PrimaryProduction> betas = new ArrayList<>();
            List<PrimaryProduction> gammas = new ArrayList<>();

            for (PrimaryProduction pp1 : p1.getPrimaryProductions()) {
                if (pp1.getRight().getSymbols().get(0).equals(prefixSymbol)) {
                    if (pp1.getRight().getSymbols().size() > 1) {
                        betas.add(
                                PrimaryProduction.create(
                                        /*
                                         * A′
                                         */
                                        aPrimed,
                                        SymbolString.create(
                                                ListUtils.subListExceptFirstElement(pp1.getRight().getSymbols())
                                        ),
                                        // todo 由于LL1分析法会改造文法，这样一来定义文法时的Operator就无效了（有位置信息）
                                        null
                                )
                        );
                    } else {
                        betas.add(
                                PrimaryProduction.create(
                                        /*
                                         * A′
                                         */
                                        aPrimed,
                                        SymbolString.create(
                                                Symbol.EPSILON
                                        ),
                                        // todo 由于LL1分析法会改造文法，这样一来定义文法时的Operator就无效了（有位置信息）
                                        null
                                )
                        );
                    }
                } else {
                    gammas.add(pp1);
                }
            }

            Production p3 = Production.create(
                    /*
                     * β1|β2|...|βn
                     */
                    betas
            );

            assertFalse(productionMap.containsKey(aPrimed));
            productionMap.put(aPrimed, p3);

            Production p2 = Production.create(
                    ListUtils.of(
                            PrimaryProduction.create(
                                    /*
                                     * A
                                     */
                                    a,
                                    /*
                                     * aA′
                                     */
                                    SymbolString.create(
                                            prefixSymbol,
                                            aPrimed
                                    ),
                                    // todo 由于LL1分析法会改造文法，这样一来定义文法时的Operator就无效了（有位置信息）
                                    null
                            ),
                            /*
                             * γ1|γ2|...|γm
                             */
                            gammas
                    )
            );

            productionMap.put(a, p2);

            /*
             * 递归调用
             */
            extractLeftCommonFactor(a);
            extractLeftCommonFactor(aPrimed);
        }

    }

    /**
     * 除去计数值为1的公共前缀
     *
     * @param commonPrefixes 公共前缀映射表
     */
    private void filterPrefixWithSingleCount(Map<Symbol, Integer> commonPrefixes) {
        List<Symbol> removeKeys = new ArrayList<>();
        for (Map.Entry<Symbol, Integer> entry : commonPrefixes.entrySet()) {
            if (entry.getValue() == 1) {
                removeKeys.add(entry.getKey());
            }
        }
        for (Symbol key : removeKeys) {
            commonPrefixes.remove(key);
        }
    }

    private Symbol createPrimedSymbolFor(Symbol symbol) {
        Symbol primedSymbol = symbol;

        do {
            primedSymbol = primedSymbol.getPrimedSymbol();
        } while (productionMap.containsKey(primedSymbol));

        return primedSymbol;
    }

    private Grammar createNewGrammar() {
        return Grammar.create(
                originalGrammar.getStart(),
                productionMap.entrySet()
                        .stream()
                        .map(Map.Entry::getValue)
                        .collect(Collectors.toList())
        );
    }
}
