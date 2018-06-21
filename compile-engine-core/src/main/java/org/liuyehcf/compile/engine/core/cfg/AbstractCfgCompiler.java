package org.liuyehcf.compile.engine.core.cfg;

import org.liuyehcf.compile.engine.core.CompileResult;
import org.liuyehcf.compile.engine.core.grammar.converter.GrammarConverterPipeline;
import org.liuyehcf.compile.engine.core.grammar.definition.*;
import org.liuyehcf.compile.engine.core.utils.AssertUtils;
import org.liuyehcf.compile.engine.core.utils.SetUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * CFG文法编译器抽象基类
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public abstract class AbstractCfgCompiler<T> implements CfgCompiler<T> {

    /**
     * 原始文法
     */
    private final Grammar originalGrammar;

    /**
     * 词法分析器
     */
    protected final LexicalAnalyzer lexicalAnalyzer;

    /**
     * 文法转换pipeline
     */
    private final GrammarConverterPipeline grammarConverterPipeline;

    /**
     * 转换后的文法
     */
    protected Grammar grammar;

    /**
     * 非终结符->产生式的映射
     */
    private Map<Symbol, Production> productionMap;

    /**
     * first集
     */
    private Map<Symbol, Set<Symbol>> firsts;

    /**
     * follow集
     */
    private Map<Symbol, Set<Symbol>> follows;

    private boolean isLegal;

    protected AbstractCfgCompiler(Grammar originalGrammar, LexicalAnalyzer lexicalAnalyzer,
                                  GrammarConverterPipeline grammarConverterPipeline) {
        if (originalGrammar == null || lexicalAnalyzer == null) {
            throw new NullPointerException();
        }
        this.originalGrammar = originalGrammar;
        this.lexicalAnalyzer = lexicalAnalyzer;
        this.grammarConverterPipeline = grammarConverterPipeline;

        init();
    }

    protected Map<Symbol, Production> getProductionMap() {
        return productionMap;
    }

    protected Map<Symbol, Set<Symbol>> getFirsts() {
        return firsts;
    }

    protected Map<Symbol, Set<Symbol>> getFollows() {
        return follows;
    }

    @Override
    public final CompileResult<T> compile(String input) {
        if (!isLegal) {
            throw new RuntimeException(this.getClass().getSimpleName() + " can't support this Grammar");
        }

        return doCompile(input);
    }

    /**
     * 执行具体的编译操作，交由子类实现
     *
     * @param input 待匹配的输入
     * @return 编译结果
     */
    protected abstract CompileResult<T> doCompile(String input);

    @Override
    public final Grammar getGrammar() {
        return grammar;
    }

    private void init() {
        // 转换给定文法，包括消除直接/间接左递归；提取公因子
        convertGrammar();

        // 计算first集
        calculateFirst();

        // 计算follow集
        calculateFollow();
    }

    private void convertGrammar() {
        this.grammar = grammarConverterPipeline.convert(originalGrammar);
        this.productionMap = new HashMap<>(16);

        for (Production p : grammar.getProductions()) {
            AssertUtils.assertFalse(productionMap.containsKey(p.getLeft()));
            productionMap.put(p.getLeft(), p);
        }

    }

    private void calculateFirst() {
        firsts = new HashMap<>(16);

        // 首先，处理所有的终结符
        for (Symbol symbol : this.grammar.getTerminators()) {
            firsts.put(symbol, SetUtils.of(symbol));
        }

        // 处理非终结符
        boolean canBreak = false;
        while (!canBreak) {
            Map<Symbol, Set<Symbol>> newFirsts = copyFirst();

            for (Symbol x : this.grammar.getNonTerminators()) {
                Production px = productionMap.get(x);

                AssertUtils.assertNotNull(px);

                // 如果X是一个非终结符，且X→Y1...Yk∈P(k≥1)
                // 那么如果对于某个i，a在FIRST(Yi)中且ε在所有的FIRST(Y1),...,FIRST(Yi−1)中(即Y1...Yi−1⇒∗ε)，就把a加入到FIRST(X)中
                // 如果对于所有的j=1,2,...,k，ε在FIRST(Yj)中，那么将ε加入到FIRST(X)

                // 这里需要遍历每个子产生式
                for (PrimaryProduction ppx : px.getPrimaryProductions()) {
                    boolean canReachEpsilon = true;

                    for (int i = 0; i < ppx.getRight().getSymbols().size(); i++) {
                        Symbol yi = ppx.getRight().getSymbols().get(i);
                        if (!newFirsts.containsKey(yi)) {
                            // 说明该符号的first集尚未计算，因此跳过当前子表达式
                            canReachEpsilon = false;
                            break;
                        } else {
                            // 首先，将_Y的first集(除了ε)添加到_X的first集中
                            if (!newFirsts.containsKey(x)) {
                                newFirsts.put(x, new HashSet<>());
                            }
                            newFirsts.get(x).addAll(
                                    SetUtils.extract(
                                            newFirsts.get(yi),
                                            Symbol.EPSILON
                                    )
                            );

                            // 若_Y的first集不包含ε，那么到子表达式循环结束
                            if (!newFirsts.get(yi).contains(Symbol.EPSILON)) {
                                canReachEpsilon = false;
                                break;
                            }
                        }
                    }

                    if (canReachEpsilon) {
                        newFirsts.get(x).add(Symbol.EPSILON);
                    }
                }
            }

            if (newFirsts.equals(this.firsts)) {
                canBreak = true;
            } else {
                this.firsts = newFirsts;
                canBreak = false;
            }
        }
    }

    private Map<Symbol, Set<Symbol>> copyFirst() {
        Map<Symbol, Set<Symbol>> copy = new HashMap<>(16);
        for (Map.Entry<Symbol, Set<Symbol>> entry : firsts.entrySet()) {
            copy.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return copy;
    }

    private void calculateFollow() {
        follows = new HashMap<>(16);

        // 将$放入FOLLOW(S)中，其中S是开始符号，$是输入右端的结束标记
        follows.put(this.grammar.getStart(), SetUtils.of(Symbol.DOLLAR));

        boolean canBreak = false;
        while (!canBreak) {
            Map<Symbol, Set<Symbol>> newFollows = copyFollow();

            for (Symbol a : this.grammar.getNonTerminators()) {
                Production pa = productionMap.get(a);

                AssertUtils.assertNotNull(pa);

                for (PrimaryProduction ppa : pa.getPrimaryProductions()) {
                    for (int i = 0; i < ppa.getRight().getSymbols().size(); i++) {
                        Symbol b = ppa.getRight().getSymbols().get(i);
                        SymbolString beta = null;

                        if (b.isTerminator()) {
                            continue;
                        }

                        if (i < ppa.getRight().getSymbols().size() - 1) {
                            beta = ppa.getRight().getSubSymbolString(i + 1);
                        }

                        // 如果存在一个产生式A→αBβ，那么FIRST(β)中除ε之外的所有符号都在FOLLOW(B)中
                        if (beta != null) {
                            if (!newFollows.containsKey(b)) {
                                newFollows.put(b, new HashSet<>());
                            }

                            Set<Symbol> firstsOfBeta = firstOfSymbolString(beta);
                            AssertUtils.assertNotNull(firstsOfBeta);

                            newFollows.get(b).addAll(
                                    SetUtils.extract(
                                            firstsOfBeta,
                                            Symbol.EPSILON)
                            );
                        }

                        // 如果存在一个产生式A→αB，或存在产生式A→αBβ且FIRST(β)包含ε，那么FOLLOW(A)中的所有符号都在FOLLOW(B)中
                        if (beta == null
                                || firstContainsEpsilon(beta)) {
                            if (newFollows.containsKey(a)) {

                                if (!newFollows.containsKey(b)) {
                                    newFollows.put(b, new HashSet<>());
                                }

                                newFollows.get(b).addAll(
                                        newFollows.get(a)
                                );
                            }
                        }
                    }
                }
            }

            if (newFollows.equals(this.follows)) {
                canBreak = true;
            } else {
                this.follows = newFollows;
                canBreak = false;
            }
        }

        // 检查一下是否所有的非终结符都有了follow集
        for (Symbol nonTerminator : this.grammar.getNonTerminators()) {
            AssertUtils.assertFalse(follows.get(nonTerminator) == null
                    || follows.get(nonTerminator).isEmpty());
        }
    }

    private Map<Symbol, Set<Symbol>> copyFollow() {
        Map<Symbol, Set<Symbol>> copy = new HashMap<>(16);
        for (Map.Entry<Symbol, Set<Symbol>> entry : follows.entrySet()) {
            copy.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return copy;
    }

    @Override
    public final String getFirstJSONString() {
        return getJSONStringFor(this.getFirsts(), true);
    }

    @Override
    public final String getFollowJSONString() {
        return getJSONStringFor(this.getFollows(), false);
    }

    private String getJSONStringFor(Map<Symbol, Set<Symbol>> map, boolean containsTerminator) {
        StringBuilder sb = new StringBuilder();

        sb.append('{');

        if (containsTerminator) {
            sb.append("\"terminator\":");
            sb.append('{');

            appendAttr(sb, map, this.grammar.getTerminators());

            AssertUtils.assertFalse(this.grammar.getTerminators().isEmpty());
            sb.setLength(sb.length() - 1);

            sb.append('}');
        }

        if (containsTerminator) {
            sb.append(',');
        }

        sb.append("\"nonTerminator\":");
        sb.append('{');

        appendAttr(sb, map, this.grammar.getNonTerminators());

        AssertUtils.assertFalse(this.grammar.getNonTerminators().isEmpty());
        sb.setLength(sb.length() - 1);

        sb.append('}');
        sb.append('}');

        return sb.toString();
    }

    private void appendAttr(StringBuilder sb, Map<Symbol, Set<Symbol>> map, Set<Symbol> symbols) {
        for (Symbol symbol : symbols) {
            sb.append('\"').append(symbol).append("\":");
            sb.append('\"');

            AssertUtils.assertFalse(map.get(symbol).isEmpty());

            for (Symbol firstSymbol : map.get(symbol)) {
                sb.append(firstSymbol).append(',');
            }

            sb.setLength(sb.length() - 1);

            sb.append('\"');
            sb.append(',');
        }
    }

    @Override
    public boolean isLegal() {
        return isLegal;
    }

    protected void setLegal(boolean isLegal) {
        this.isLegal = isLegal;
    }

    protected boolean firstContainsEpsilon(SymbolString symbolString) {
        for (Symbol symbol : symbolString.getSymbols()) {
            if (!this.firsts.get(symbol).contains(Symbol.EPSILON)) {
                return false;
            }
        }
        return true;
    }

    protected Set<Symbol> firstOfSymbolString(SymbolString symbolString) {
        Set<Symbol> firstList = new HashSet<>();
        for (Symbol symbol : symbolString.getSymbols()) {
            /*
             * 非ε的符号都在first集合中
             */
            firstList.addAll(
                    SetUtils.extract(
                            this.firsts.get(symbol),
                            Symbol.EPSILON)
            );

            /*
             * 如果当前符号不包含ε，那么first集合计算到此结束
             */
            if (!this.firsts.get(symbol).contains(Symbol.EPSILON)) {
                return firstList;
            }
        }

        /*
         * 所有符号的first集合都包含ε，则整个符号串的first集合包含ε
         */
        firstList.add(Symbol.EPSILON);
        return firstList;
    }
}
