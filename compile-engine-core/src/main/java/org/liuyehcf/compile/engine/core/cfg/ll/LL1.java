package org.liuyehcf.compile.engine.core.cfg.ll;


import org.liuyehcf.compile.engine.core.CompileResult;
import org.liuyehcf.compile.engine.core.cfg.AbstractCfgCompiler;
import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.grammar.CompilerException;
import org.liuyehcf.compile.engine.core.grammar.converter.GrammarConverterPipelineImpl;
import org.liuyehcf.compile.engine.core.grammar.converter.LreElfGrammarConverter;
import org.liuyehcf.compile.engine.core.grammar.converter.MergeGrammarConverter;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.utils.AssertUtils;
import org.liuyehcf.compile.engine.core.utils.SetUtils;

import java.util.*;


/**
 * LL1文法编译器
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class LL1 extends AbstractCfgCompiler implements LLCompiler {

    /**
     * select集
     */
    private Map<Symbol, Map<PrimaryProduction, Set<Symbol>>> selects = new HashMap<>();

    private LL1(LexicalAnalyzer lexicalAnalyzer, Grammar originalGrammar) {
        super(lexicalAnalyzer, originalGrammar, GrammarConverterPipelineImpl
                .builder()
                .registerGrammarConverter(MergeGrammarConverter.class)
                .registerGrammarConverter(LreElfGrammarConverter.class)
                .build());
    }

    public static LLCompiler create(LexicalAnalyzer lexicalAnalyzer, Grammar originalGrammar) {
        LL1 compiler = new LL1(lexicalAnalyzer, originalGrammar);

        compiler.init();

        return compiler;
    }

    /**
     * 初始化方法
     */
    @Override
    protected void postInit() {
        // 计算select集
        calculateSelect();
    }

    @SuppressWarnings("unchecked")
    private void calculateSelect() {

        for (Symbol a : this.grammar.getNonTerminators()) {
            Production pa = getProductionMap().get(a);

            for (PrimaryProduction ppa : pa.getPrimaryProductions()) {
                Symbol firstAlpha = ppa.getRight().getSymbols().get(0);

                if (!selects.containsKey(a)) {
                    selects.put(a, new HashMap<>(16));
                }
                AssertUtils.assertFalse(selects.get(a).containsKey(ppa));

                selects.get(a).put(ppa, new HashSet<>());

                // 如果ε∉FIRST(α)，那么SELECT(A→α)=FIRST(α)
                if (!getFirsts().get(firstAlpha).contains(Symbol.EPSILON)) {

                    selects.get(a).get(ppa).addAll(
                            getFirsts().get(firstAlpha)
                    );
                }
                // 如果ε∈FIRST(α)，那么SELECT(A→α)=(FIRST(α)−{ε})∪FOLLOW(A)
                else {
                    selects.get(a).get(ppa).addAll(
                            SetUtils.of(
                                    SetUtils.extract(getFirsts().get(firstAlpha), Symbol.EPSILON),
                                    getFollows().get(a)
                            )
                    );
                }
            }
        }
    }

    @Override
    protected void checkIsLegal() {
        // 检查select集的唯一性：具有相同左部的产生式其SELECT集不相交
        for (Symbol a : this.grammar.getNonTerminators()) {
            Map<PrimaryProduction, Set<Symbol>> map = selects.get(a);
            AssertUtils.assertNotNull(map);

            Set<Symbol> selectsOfA = new HashSet<>();

            for (Map.Entry<PrimaryProduction, Set<Symbol>> entry : map.entrySet()) {
                for (Symbol eachSelectSymbol : entry.getValue()) {
                    if (!selectsOfA.add(eachSelectSymbol)) {
                        setLegal(false);
                        return;
                    }
                }
            }
        }

        setLegal(true);
    }

    @Override
    protected CompileResult doCompile(String input) {
        return new Engine(input).compile();
    }

    private PrimaryProduction findProductionByToken(Symbol symbol, Symbol tokenId) throws CompilerException {
        Map<PrimaryProduction, Set<Symbol>> map = selects.get(symbol);

        PrimaryProduction ppSelected = null;

        for (Map.Entry<PrimaryProduction, Set<Symbol>> entry : map.entrySet()) {
            for (Symbol selectedSymbol : entry.getValue()) {
                if (selectedSymbol.equals(tokenId)) {
                    ppSelected = entry.getKey();
                }
            }
        }

        if (ppSelected == null) {
            throw new CompilerException();
        }

        return ppSelected;
    }

    @Override
    public String getSelectJSONString() {
        StringBuilder sb = new StringBuilder();

        sb.append('{');

        for (Map.Entry<Symbol, Map<PrimaryProduction, Set<Symbol>>> outerEntry : selects.entrySet()) {
            sb.append('\"');
            sb.append(outerEntry.getKey());
            sb.append('\"');
            sb.append(':');

            sb.append('{');

            for (Map.Entry<PrimaryProduction, Set<Symbol>> innerEntry : outerEntry.getValue().entrySet()) {
                sb.append('\"');
                sb.append(outerEntry.getKey())
                        .append(" → ")
                        .append(innerEntry.getKey().getRight());
                sb.append('\"');
                sb.append(':');

                sb.append('\"');

                for (Symbol firstSymbol : innerEntry.getValue()) {
                    sb.append(firstSymbol).append(',');
                }

                AssertUtils.assertFalse(innerEntry.getValue().isEmpty());
                sb.setLength(sb.length() - 1);

                sb.append('\"');
                sb.append(',');
            }

            AssertUtils.assertFalse(outerEntry.getValue().entrySet().isEmpty());
            sb.setLength(sb.length() - 1);
            sb.append('}');
            sb.append(',');
        }

        AssertUtils.assertFalse(selects.isEmpty());
        sb.setLength(sb.length() - 1);
        sb.append('}');

        return sb.toString();
    }

    @Override
    public String getAnalysisTableMarkdownString() {
        StringBuilder sb = new StringBuilder();

        String separator = "|";

        // 第一行：表头，各个终结符符号
        sb.append(separator)
                .append(' ')
                .append("非终结符\\终结符")
                .append(' ');

        for (Symbol terminator : this.grammar.getTerminators()) {
            sb.append(separator)
                    .append(' ')
                    .append(terminator)
                    .append(' ');
        }

        sb.append(separator).append('\n');

        // 第二行：对齐格式
        sb.append(separator);

        for (int i = 0; i < this.grammar.getTerminators().size(); i++) {
            sb.append(":--")
                    .append(separator);
        }
        sb.append(":--")
                .append(separator);

        sb.append('\n');

        // 其余行：每一行代表某个非终结符在不同终结符下的产生式
        // A → α
        for (Symbol a : this.grammar.getNonTerminators()) {
            // 第一列，产生式
            sb.append(separator)
                    .append(' ')
                    .append(a)
                    .append(' ');

            for (Symbol terminator : this.grammar.getTerminators()) {

                PrimaryProduction ppa = null;

                for (Map.Entry<PrimaryProduction, Set<Symbol>> entry : selects.get(a).entrySet()) {

                    Set<Symbol> selectsOfA = entry.getValue();

                    if (selectsOfA.contains(terminator)) {
                        ppa = entry.getKey();
                        break;
                    }
                }

                if (ppa != null) {
                    sb.append(separator)
                            .append(' ')
                            .append(a)
                            .append(" → ")
                            .append(ppa.getRight())
                            .append(' ');
                } else {
                    sb.append(separator)
                            .append(' ')
                            .append('\\')
                            .append(' ');
                }
            }

            sb.append(separator).append('\n');
        }

        return sb.toString();
    }

    protected class Engine {

        /**
         * 输入
         */
        private final String input;

        /**
         * 编译返回参数
         */
        protected Object result = null;

        private Engine(String input) {
            this.input = input;
        }

        private CompileResult compile() {
            LexicalAnalyzer.TokenIterator tokenIterator = lexicalAnalyzer.iterator(input);

            // 取出全部的TokenId
            Queue<Symbol> tokenIds = new LinkedList<>();
            while (tokenIterator.hasNext()) {
                tokenIds.offer(tokenIterator.next().getId());
            }
            if (!tokenIterator.reachesEof()) {
                return new CompileResult(false, null);
            }
            tokenIds.offer(Symbol.DOLLAR);

            // 符号栈
            LinkedList<Symbol> symbolStack = new LinkedList<>();
            symbolStack.push(Symbol.DOLLAR);
            symbolStack.push(grammar.getStart());

            Symbol tokenId = null;
            Symbol symbol;

            try {
                while (!(symbolStack.isEmpty() && tokenIds.isEmpty())) {

                    if (symbolStack.isEmpty()) {
                        throw new CompilerException();
                    }

                    // 每次迭代都会消耗一个symbol
                    symbol = symbolStack.pop();

                    // 每次迭代未必会消耗一个token
                    if (tokenId == null) {
                        if (tokenIds.isEmpty()) {
                            throw new CompilerException();
                        }
                        tokenId = tokenIds.poll();
                    }

                    if (symbol.isTerminator()) {
                        // 若当前符号是ε则不消耗token
                        if (!Symbol.EPSILON.equals(symbol)) {

                            if (tokenId == null) {
                                throw new CompilerException();
                            }

                            if (!tokenId.equals(symbol)) {
                                throw new CompilerException();
                            }

                            // 消耗一个token
                            tokenId = null;
                        }
                    } else {
                        PrimaryProduction pp = findProductionByToken(symbol, tokenId);

                        List<Symbol> reversedSymbols = new ArrayList<>(pp.getRight().getSymbols());

                        Collections.reverse(reversedSymbols);

                        for (Symbol nextSymbol : reversedSymbols) {
                            symbolStack.push(nextSymbol);
                        }
                    }
                }
            } catch (CompilerException e) {
                return new CompileResult(false, null);
            }

            return new CompileResult(true, result);
        }
    }
}
