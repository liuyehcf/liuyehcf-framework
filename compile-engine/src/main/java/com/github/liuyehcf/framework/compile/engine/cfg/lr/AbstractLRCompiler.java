package com.github.liuyehcf.framework.compile.engine.cfg.lr;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.compile.engine.CompileResult;
import com.github.liuyehcf.framework.compile.engine.cfg.AbstractCfgCompiler;
import com.github.liuyehcf.framework.compile.engine.cfg.lexical.LexicalAnalyzer;
import com.github.liuyehcf.framework.compile.engine.cfg.lexical.Token;
import com.github.liuyehcf.framework.compile.engine.grammar.CompilerException;
import com.github.liuyehcf.framework.compile.engine.grammar.converter.AugmentedGrammarConverter;
import com.github.liuyehcf.framework.compile.engine.grammar.converter.GrammarConverterPipelineImpl;
import com.github.liuyehcf.framework.compile.engine.grammar.converter.MergeGrammarConverter;
import com.github.liuyehcf.framework.compile.engine.grammar.converter.StatusExpandGrammarConverter;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Grammar;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.PrimaryProduction;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.SymbolString;
import com.github.liuyehcf.framework.compile.engine.utils.ListUtils;
import com.github.liuyehcf.framework.compile.engine.utils.Pair;
import com.github.liuyehcf.framework.compile.engine.utils.SetUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertNotNull;
import static com.github.liuyehcf.framework.compile.engine.utils.CharacterUtil.isBlankChar;

/**
 * LR文法编辑器抽象基类
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public abstract class AbstractLRCompiler<T> extends AbstractCfgCompiler<T> implements LRCompiler<T>, Serializable {

    /**
     * 是否合并同心闭包（只有LALR才是true）
     */
    private final boolean needMerge;

    /**
     * 项目集闭包 closureId -> Closure
     */
    private Map<Integer, Closure> closures;

    /**
     * 状态转移表 [ClosureId, Symbol] -> ClosureId
     */
    private Map<Integer, Map<Symbol, Integer>> closureTransferTable;

    /**
     * 预测分析表 [ClosureId, Symbol] -> NodeTransferOperation。正常情况下，表项只有一个，若出现多个，则说明有冲突
     */
    private Map<Integer, Map<Symbol, LinkedHashSet<NodeTransferOperation>>> analysisTable;

    /**
     * 预测分析表中的所有终结符，与Grammar中的符号有些不一样，具体请看初始化方法initAnalysisTable
     */
    private List<Symbol> analysisTerminators;

    /**
     * 预测分析表中的所有文法符号，与Grammar中的符号有些不一样，具体请看初始化方法initAnalysisTable
     */
    private List<Symbol> analysisSymbols;

    /**
     * 项目集闭包的id
     */
    private int closureCnt;

    AbstractLRCompiler(Grammar originalGrammar, LexicalAnalyzer lexicalAnalyzer, boolean needMerge) {
        super(originalGrammar, lexicalAnalyzer, GrammarConverterPipelineImpl
                .builder()
                .registerGrammarConverter(AugmentedGrammarConverter.class)
                .registerGrammarConverter(StatusExpandGrammarConverter.class)
                .registerGrammarConverter(MergeGrammarConverter.class)
                .build());

        this.closures = new LinkedHashMap<>();
        this.needMerge = needMerge;
        this.closureTransferTable = new HashMap<>();
        this.analysisTable = new HashMap<>();
        this.analysisTerminators = new ArrayList<>();
        this.analysisSymbols = new ArrayList<>();
        this.closureCnt = 0;

        init();
    }

    static Item successor(Item preItem) {
        PrimaryProduction pp = preItem.getPrimaryProduction();

        Assert.assertTrue(pp.getRight().getIndexOfDot() != -1);

        if (pp.getRight().getIndexOfDot() == pp.getRight().getSymbols().size()) {
            return null;
        }

        return new Item(
                PrimaryProduction.create(
                        pp.getLeft(),
                        SymbolString.create(
                                pp.getRight().getSymbols(),
                                pp.getRight().getIndexOfDot() + 1
                        ),
                        pp.getSemanticActions()
                ),
                preItem.getLookAHeads()
        );
    }

    static Symbol nextSymbol(Item preItem) {
        PrimaryProduction pp = preItem.getPrimaryProduction();

        Assert.assertTrue(pp.getRight().getIndexOfDot() != -1);

        if (pp.getRight().getIndexOfDot() == pp.getRight().getSymbols().size()) {
            return null;
        }

        return pp.getRight().getSymbols().get(pp.getRight().getIndexOfDot());
    }

    static SymbolString nextSymbolString(Item preItem) {
        PrimaryProduction pp = preItem.getPrimaryProduction();

        Assert.assertTrue(pp.getRight().getIndexOfDot() != -1);

        if (pp.getRight().getIndexOfDot() == pp.getRight().getSymbols().size()) {
            return null;
        }

        return pp.getRight().getSubSymbolString(pp.getRight().getIndexOfDot());
    }

    static PrimaryProduction removeDot(PrimaryProduction pp) {
        Assert.assertTrue(pp.getRight().getIndexOfDot() != -1);

        return PrimaryProduction.create(
                pp.getLeft(),
                SymbolString.create(
                        pp.getRight().getSymbols()
                ),
                pp.getSemanticActions()
        );
    }

    private static int closureIdOf(Map<Integer, Closure> closures, List<Item> coreItems) {
        for (Closure closure : closures.values()) {
            if (closure.isSame(coreItems)) {
                return closure.getId();
            }
        }
        return -1;
    }

    List<Symbol> getAnalysisTerminators() {
        return analysisTerminators;
    }

    @Override
    protected final CompileResult<T> doCompile(String input) {
        return createCompiler(input).compile();
    }

    protected Engine createCompiler(String input) {
        return new Engine(input);
    }

    @Override
    public final String getClosureJSONString() {
        String sb = "";

        sb += '{';

        sb += "\"closures:\":";

        sb += closures.values();

        sb += '}';

        return sb;
    }

    @Override
    public final String getAnalysisTableMarkdownString() {
        StringBuilder sb = new StringBuilder();

        String separator = "|";

        /*
         * 第一行：表头，各个终结符符号以及非终结符号
         */
        sb.append(separator)
                .append(' ')
                .append("状态\\文法符号")
                .append(' ');

        for (Symbol symbol : analysisTerminators) {
            sb.append(separator)
                    .append(' ')
                    .append(wrapMarkdownKeywords(symbol.toString()))
                    .append(' ');
        }

        for (Symbol symbol : this.grammar.getNonTerminators()) {
            if (Symbol.START.equals(symbol)) {
                continue;
            }
            sb.append(separator)
                    .append(' ')
                    .append(wrapMarkdownKeywords(symbol.toString()))
                    .append(' ');
        }

        sb.append(separator).append('\n');

        /*
         * 第二行：对齐格式
         */
        sb.append(separator);

        for (int i = 0; i < analysisSymbols.size(); i++) {
            sb.append(":--")
                    .append(separator);
        }
        sb.append(":--")
                .append(separator);

        sb.append('\n');

        /*
         * 其余行：转义表
         */
        for (Closure closure : closures.values()) {
            sb.append(separator)
                    .append(' ')
                    .append(closure.getId())
                    .append(' ');

            for (Symbol symbol : analysisSymbols) {
                LinkedHashSet<NodeTransferOperation> nodeTransferOperations = analysisTable.get(closure.getId()).get(
                        symbol);
                if (nodeTransferOperations.isEmpty()) {
                    sb.append(separator)
                            .append(' ')
                            .append("\\")
                            .append(' ');
                } else {
                    sb.append(separator);
                    for (NodeTransferOperation nodeTransferOperation : nodeTransferOperations) {
                        if (nodeTransferOperation.getOperator() == NodeTransferOperator.ACCEPT
                                || nodeTransferOperation.getOperator() == NodeTransferOperator.REDUCTION) {
                            sb.append(' ')
                                    .append(wrapMarkdownKeywords(nodeTransferOperation.getOperator().toString()))
                                    .append(" \"")
                                    .append(wrapMarkdownKeywords(nodeTransferOperation.getPrimaryProduction().toString()))
                                    .append('\"')
                                    .append(" /");
                        } else {
                            sb.append(' ')
                                    .append(wrapMarkdownKeywords(nodeTransferOperation.getOperator().toString()))
                                    .append(" \"")
                                    .append(nodeTransferOperation.getNextClosureId())
                                    .append('\"')
                                    .append(" /");
                        }
                    }
                    Assert.assertTrue(sb.charAt(sb.length() - 1) == '/');
                    sb.setLength(sb.length() - 1);
                }
            }

            sb.append(separator).append('\n');
        }

        return sb.toString();
    }

    private String wrapMarkdownKeywords(String s) {
        s = s.replaceAll("\\|", "\\\\|");
        s = s.replaceAll("<", "\\\\<");
        s = s.replaceAll(">", "\\\\>");
        return s;
    }

    @Override
    public final String getClosureTransferTableJSONString() {
        StringBuilder sb = new StringBuilder();
        int cnt = 1;

        sb.append('{');

        for (Closure closure : closures.values()) {

            for (Symbol symbol : analysisSymbols) {
                if (closureTransferTable.get(closure.getId()) != null
                        && closureTransferTable.get(closure.getId()).get(symbol) != null) {
                    sb.append('\"')
                            .append(cnt++)
                            .append("\"")
                            .append(":")
                            .append('\"')
                            .append('[')
                            .append(closure.getId())
                            .append(", ")
                            .append(symbol)
                            .append(']')
                            .append(" → ")
                            .append(closureTransferTable.get(closure.getId()).get(symbol))
                            .append('\"')
                            .append(',');
                }
            }

            sb.setLength(sb.length() - 1);

            sb.append(',');
        }

        sb.setLength(sb.length() - 1);

        sb.append('}');

        return sb.toString();
    }

    private void init() {
        /*
         * 初始化项目集闭包
         */
        initClosure();

        /*
         * 合并同心闭包
         */
        mergeConcentricClosure();

        /*
         * 初始化分析表
         */
        initAnalysisTable();

        /*
         * 检查正确性
         */
        checkIsLegal();
    }

    private void initClosure() {
        /*
         * 初始化，添加闭包0
         */
        Closure firstClosure = closure(ListUtils.of(createFirstItem()));
        closures.put(firstClosure.getId(), firstClosure);

        boolean canBreak = false;

        while (!canBreak) {
            canBreak = true;

            Map<Integer, Closure> newClosures = new LinkedHashMap<>(closures);

            for (Closure preClosure : closures.values()) {

                /*
                 * 同一个闭包下的不同项目，如果下一个符号相同，那么这些项目的后继项目作为下一个闭包的核心项目集合
                 * 这个Map就是用于保存: 输入符号 -> 后继闭包的核心项目集合 的映射关系
                 */
                Map<Symbol, List<Item>> successorMap = new LinkedHashMap<>();

                /*
                 * 遍历闭包中的产生式，初始化successorMap
                 */
                for (Item preItem : preClosure.getItems()) {
                    Item nextItem = successor(preItem);

                    /*
                     * 有后继
                     */
                    if (nextItem != null) {
                        Symbol nextSymbol = nextSymbol(preItem);
                        Assert.assertNotNull(nextSymbol);

                        if (!successorMap.containsKey(nextSymbol)) {
                            successorMap.put(nextSymbol, new ArrayList<>());
                        }

                        successorMap.get(nextSymbol).add(nextItem);
                    }
                }

                /*
                 * 创建Closure，维护状态转移表
                 */
                for (Map.Entry<Symbol, List<Item>> entry : successorMap.entrySet()) {
                    Symbol nextSymbol = entry.getKey();
                    List<Item> coreItemsOfNextClosure = entry.getValue();

                    Closure nextClosure;
                    int existsClosureId;

                    if ((existsClosureId = closureIdOf(newClosures, coreItemsOfNextClosure)) == -1) {
                        nextClosure = closure(coreItemsOfNextClosure);
                        newClosures.put(nextClosure.getId(), nextClosure);
                    } else {
                        nextClosure = newClosures.get(existsClosureId);
                    }

                    if (!closureTransferTable.containsKey(preClosure.getId())) {
                        closureTransferTable.put(preClosure.getId(), new HashMap<>(16));
                    }

                    Assert.assertTrue(!closureTransferTable.get(preClosure.getId()).containsKey(nextSymbol)
                            || closureTransferTable.get(preClosure.getId()).get(nextSymbol).equals(nextClosure.getId()));

                    if (!closureTransferTable.get(preClosure.getId()).containsKey(nextSymbol)) {
                        closureTransferTable.get(preClosure.getId()).put(nextSymbol, nextClosure.getId());
                        canBreak = false;
                    }
                }
            }

            closures = newClosures;
        }
    }

    /**
     * 初始化第一个项目集闭包，不同类型的编译器操作不同
     *
     * @return 第一个项目集闭包
     */
    abstract Item createFirstItem();

    private Closure closure(List<Item> coreItems) {
        Set<Item> items = new LinkedHashSet<>(coreItems);

        boolean canBreak = false;

        while (!canBreak) {
            int preSize = items.size();

            /*
             * 遍历Set的时候不能进行写操作
             */
            List<Item> newAddedItems = new ArrayList<>();
            for (Item item : items) {
                Symbol nextSymbol = nextSymbol(item);

                /*
                 * '·'后面跟的是非终结符
                 */
                if (nextSymbol != null
                        && !nextSymbol.isTerminator()) {

                    newAddedItems.addAll(findEqualItems(item));
                }
            }

            items.addAll(newAddedItems);

            if (preSize == items.size()) {
                canBreak = true;
            }
        }

        /*
         * 合并具有相同产生式的Item，即合并展望符。同时保持Item的顺序，因此用LinkedHashMap
         * 形如 "[B → · γ, b]"与"[B → · γ, c]" 合并成 "[B → · γ, b/c]"
         */
        Map<PrimaryProduction, Item> helpMap = new LinkedHashMap<>();

        for (Item item : items) {
            PrimaryProduction pp = item.getPrimaryProduction();
            if (!helpMap.containsKey(pp)) {
                helpMap.put(pp, item);
            } else {
                helpMap.put(
                        pp,
                        new Item(
                                pp,
                                SetUtils.of(helpMap.get(pp).getLookAHeads(), item.getLookAHeads())
                        )
                );
            }
        }

        return new Closure(
                closureCnt++,
                coreItems,
                new ArrayList<>(
                        helpMap.values().stream().filter(item -> !coreItems.contains(item)).collect(Collectors.toList())
                )
        );
    }

    /**
     * <p>查找等价项目集合</p>
     * <p>对于项目 A -> · E 而言，E -> ·F 就是它的等价项目</p>
     *
     * @param item 项目集
     * @return 等价项目集合
     */
    abstract List<Item> findEqualItems(Item item);

    private void mergeConcentricClosure() {
        if (!needMerge) {
            return;
        }

        /*
         * 同心闭包集合列表
         * pair.first 保留的闭包
         * pair.second 删除的闭包列表
         */
        List<Pair<Closure, Set<Closure>>> concentricClosures = new ArrayList<>();

        /*
         * 如果某个Closure需要合并（即存在同心闭包），那么将该Closure映射到该Closure位于concentricClosures中的index
         */
        Map<Closure, Integer> concentricIndexes = new HashMap<>(16);

        List<Integer> closureIds = ListUtils.sort(new ArrayList<>(closures.keySet()));

        for (int i = 0; i < closureIds.size(); i++) {
            int closureIdI = closureIds.get(i);
            for (int j = i + 1; j < closures.size(); j++) {
                int closureIdJ = closureIds.get(j);

                Closure closureI = closures.get(closureIdI);
                Closure closureJ = closures.get(closureIdJ);

                if (Closure.isConcentric(closureI, closureJ)) {
                    if (!concentricIndexes.containsKey(closureI)
                            && !concentricIndexes.containsKey(closureJ)) {
                        int index = concentricClosures.size();
                        concentricIndexes.put(closureI, index);
                        concentricIndexes.put(closureJ, index);
                        concentricClosures.add(new Pair<>(closureI, SetUtils.of(closureJ)));
                    } else if (concentricIndexes.containsKey(closureI)) {
                        int index = concentricIndexes.get(closureI);
                        concentricIndexes.put(closureJ, index);
                        concentricClosures.get(index).getSecond().add(closureJ);
                    } else {
                        int index = concentricIndexes.get(closureJ);
                        concentricIndexes.put(closureI, index);
                        concentricClosures.get(index).getSecond().add(closureI);
                    }
                }
            }
        }

        /*
         * 修改状态转移表
         * 1. fromClosure是需要合并的同心闭包中的闭包
         * 2. fromClosure是普通的闭包，即不存在其他同心闭包
         */
        Map<Integer, Map<Symbol, Integer>> newClosureTransferTable = new HashMap<>(16);

        for (Closure fromClosure : closures.values()) {
            int actualFromClosureId;

            /*
             * 是同心闭包
             */
            if (concentricIndexes.containsKey(fromClosure)) {
                int index = concentricIndexes.get(fromClosure);
                actualFromClosureId = concentricClosures.get(index).getFirst().getId();
            }
            /*
             * 普通闭包
             */
            else {
                actualFromClosureId = fromClosure.getId();
            }

            newClosureTransferTable.putIfAbsent(actualFromClosureId, new HashMap<>(16));

            /*
             * 有些状态（闭包）是没有下一跳状态的
             */
            if (closureTransferTable.containsKey(actualFromClosureId)) {
                for (Map.Entry<Symbol, Integer> entry : closureTransferTable.get(actualFromClosureId).entrySet()) {
                    Symbol nextSymbol = entry.getKey();
                    int toClosureId = entry.getValue();
                    Closure toClosure = closures.get(toClosureId);

                    int actualToClosureId;

                    /*
                     * toClosure也可能是需要被移除的，那么找到这个toClosure所在的同心闭包集合，取出保留的closure的id作为真正的toClosureId
                     */
                    if (concentricIndexes.containsKey(toClosure)) {
                        int indexOfToClosure = concentricIndexes.get(toClosure);
                        actualToClosureId = concentricClosures.get(indexOfToClosure).getFirst().getId();
                    } else {
                        actualToClosureId = toClosureId;
                    }

                    /*
                     * 断言含义：
                     * 转移  "savedClosureId --(nextSymbol)--> actualToClosureId"  要么尚未建立，要么已经存在
                     * 不可能存在   "savedClosureId --(nextSymbol)--> otherToClosureId"
                     */
                    Assert.assertTrue(!newClosureTransferTable.get(actualFromClosureId).containsKey(nextSymbol)
                            || newClosureTransferTable.get(actualFromClosureId).get(nextSymbol).equals(actualToClosureId));

                    newClosureTransferTable.get(actualFromClosureId)
                            .put(nextSymbol, actualToClosureId);
                }
            }
        }

        /*
         * 合并同心闭包的展望符集合
         */
        for (Pair<Closure, Set<Closure>> pair : concentricClosures) {
            Closure savedClosure = pair.getFirst();
            for (Closure removedClosure : pair.getSecond()) {
                List<Item> coreItems = new ArrayList<>();
                List<Item> equalItems = new ArrayList<>();

                Assert.assertTrue(savedClosure.getCoreItems().size() == removedClosure.getCoreItems().size());
                Assert.assertTrue(savedClosure.getEqualItems().size() == removedClosure.getEqualItems().size());
                Assert.assertTrue(savedClosure.getItems().size() == removedClosure.getItems().size());

                for (int i = 0; i < savedClosure.getCoreItems().size(); i++) {
                    Assert.assertTrue(savedClosure.getCoreItems().get(i).getPrimaryProduction().equals(
                            removedClosure.getCoreItems().get(i).getPrimaryProduction()
                    ));
                    coreItems.add(
                            new Item(
                                    savedClosure.getCoreItems().get(i).getPrimaryProduction(),
                                    SetUtils.of(
                                            savedClosure.getCoreItems().get(i).getLookAHeads(),
                                            removedClosure.getCoreItems().get(i).getLookAHeads()
                                    )
                            )
                    );
                }

                for (int i = 0; i < savedClosure.getEqualItems().size(); i++) {
                    Assert.assertTrue(savedClosure.getEqualItems().get(i).getPrimaryProduction().equals(
                            removedClosure.getEqualItems().get(i).getPrimaryProduction()
                    ));
                    equalItems.add(
                            new Item(
                                    savedClosure.getEqualItems().get(i).getPrimaryProduction(),
                                    SetUtils.of(
                                            savedClosure.getEqualItems().get(i).getLookAHeads(),
                                            removedClosure.getEqualItems().get(i).getLookAHeads()
                                    )
                            )
                    );
                }

                /*
                 * 更新合并后的闭包
                 */
                savedClosure = new Closure(
                        savedClosure.getId(),
                        coreItems,
                        equalItems);
            }

            /*
             * 将最终的闭包替换掉closures中原来的闭包
             */
            closures.put(savedClosure.getId(), savedClosure);
        }

        this.closureTransferTable = newClosureTransferTable;

        /*
         * 从closures中移除哪些已经被删除的闭包
         */
        concentricClosures.forEach(
                (pair) -> pair.getSecond().forEach(
                        (closure -> closures.remove(closure.getId()))));
    }

    private void initAnalysisTable() {
        analysisTerminators.addAll(
                ListUtils.sort(
                        ListUtils.of(
                                /*
                                 * 除Symbol.EPSILON之外的所有终结符
                                 */
                                this.grammar.getTerminators().stream().filter(symbol -> !Symbol.EPSILON.equals(symbol))
                                        .collect(Collectors.toList()),
                                Symbol.DOLLAR
                        )
                )
        );
        analysisSymbols.addAll(
                ListUtils.sort(
                        ListUtils.of(
                                analysisTerminators,
                                /*
                                 * 除Symbol.START之外的所有非终结符
                                 */
                                (List<Symbol>) this.grammar.getNonTerminators().stream()
                                        .filter((symbol -> !Symbol.START.equals(symbol))).collect(Collectors.toList())
                        )
                )
        );

        /*
         * 初始化
         */
        for (Closure closure : closures.values()) {
            analysisTable.put(closure.getId(), new HashMap<>(16));
            for (Symbol symbol : analysisSymbols) {
                analysisTable.get(closure.getId()).put(symbol, new LinkedHashSet<>());
            }
        }

        /*
         * 遍历每个Closure
         */
        for (Closure closure : closures.values()) {
            /*
             * 遍历Closure中的每个项目
             */
            for (Item item : closure.getItems()) {
                Symbol nextSymbol = nextSymbol(item);

                if (nextSymbol == null) {
                    initAnalysisTableWithReduction(closure, item);
                } else if (nextSymbol.isTerminator()) {
                    initAnalysisTableWithMoveIn(closure, nextSymbol);
                } else {
                    initAnalysisTableWithJump(closure, nextSymbol);
                }
            }
        }
    }

    private void checkIsLegal() {
        /*
         * 检查合法性，即检查表项动作是否唯一
         */
        for (Closure closure : closures.values()) {
            for (Symbol symbol : analysisSymbols) {
                if (analysisTable.get(closure.getId()).get(symbol).size() > 1) {
                    setLegal(false);
                    return;
                }
            }
        }
        setLegal(true);
    }

    /**
     * 规约时的状态转移规则，不同类型的编译器有不同的实现方式
     *
     * @param closure 项目集闭包
     * @param item    项目
     */
    abstract void initAnalysisTableWithReduction(Closure closure, Item item);

    private void initAnalysisTableWithMoveIn(Closure closure, Symbol nextSymbol) {
        analysisTable.get(closure.getId())
                .get(nextSymbol)
                .add(new NodeTransferOperation(
                        closureTransferTable.get(closure.getId()).get(nextSymbol),
                        null,
                        NodeTransferOperator.MOVE_IN));
    }

    private void initAnalysisTableWithJump(Closure closure, Symbol nextSymbol) {
        analysisTable.get(closure.getId())
                .get(nextSymbol)
                .add(new NodeTransferOperation(
                        closureTransferTable.get(closure.getId()).get(nextSymbol),
                        null,
                        NodeTransferOperator.JUMP));
    }

    private NodeTransferOperation getOperationFromAnalysisTable(int closureId, Symbol symbol) {
        if (analysisTable.get(closureId).get(symbol).isEmpty()) {
            return null;
        }
        Assert.assertTrue(analysisTable.get(closureId).get(symbol).size() == 1);
        return analysisTable.get(closureId).get(symbol).iterator().next();
    }

    void addOperationToAnalysisTable(int closureId, Symbol symbol, NodeTransferOperation nodeTransferOperation) {
        analysisTable.get(closureId).get(symbol).add(nodeTransferOperation);
    }

    public class Engine {
        /**
         * 输入
         */
        private final String input;

        /**
         * 异常信息
         */
        private Throwable error = null;

        /**
         * 编译返回参数
         */
        private T result = null;

        /**
         * 状态栈，状态即项目集闭包的id
         */
        private LinkedList<Integer> statusStack;

        /**
         * 语法树节点栈，语法树节点包括文法符号，实际值，各种属性
         */
        private FutureSyntaxNodeStack nodeStack;

        /**
         * Token迭代器
         */
        private LexicalAnalyzer.TokenIterator tokenIterator;

        /**
         * 当前Token
         */
        private Token currentToken;

        /**
         * 是否获取了Token.DOLLAR;
         */
        private boolean hasReachDollar = false;

        /**
         * 是否是成功接收
         */
        private boolean canReceive = false;

        /**
         * message
         */
        private String message = "";

        /**
         * 语法树节点转移动作
         */
        private NodeTransferOperation nodeTransferOperation;

        protected Engine(String input) {
            this.input = input;
        }

        protected void setResult(T result) {
            this.result = result;
        }

        private CompileResult<T> compile() {
            try {
                before();

                doCompile();

                after();
            } catch (Throwable e) {
                error = e;
                canReceive = false;
                message = getFriendlyPrompt(e.getMessage() == null ? "system error" : e.getMessage());
            }
            return new CompileResult<>(canReceive, message, error, result);
        }

        /**
         * 钩子方法，解析前执行
         */
        protected void before() {

        }

        /**
         * 钩子方法，解析后执行
         */
        protected void after() {

        }

        private void doCompile() {
            tokenIterator = lexicalAnalyzer.iterator(input);

            statusStack = new LinkedList<>();
            nodeStack = new FutureSyntaxNodeStack();

            statusStack.push(0);
            nodeStack.addNormalSyntaxNode(Symbol.DOLLAR, null);

            currentToken = nextToken();

            boolean canBreak = false;
            while (!canBreak) {
                Integer peekStatus = statusStack.peek();
                assertNotNull(peekStatus);
                assertNotNull(currentToken);

                nodeTransferOperation = getOperationFromAnalysisTable(peekStatus, currentToken.getId());

                if (nodeTransferOperation == null) {
                    throw new CompilerException("Error LR status");
                } else {
                    switch (nodeTransferOperation.getOperator()) {
                        case MOVE_IN:
                            moveIn();
                            break;
                        case REDUCTION:
                            reduction();
                            jump();
                            break;
                        case JUMP:
                            throw new CompilerException("Error transferOperation");
                        case ACCEPT:
                            canReceive = true;
                            canBreak = true;
                            break;
                        default:
                            throw new CompilerException("Unknown transferOperation");
                    }
                }
            }
        }

        private Token nextToken() {
            if (tokenIterator.hasNext()) {
                return tokenIterator.next();
            }
            if (tokenIterator.reachesEof()) {
                if (!hasReachDollar) {
                    hasReachDollar = true;
                    return Token.DOLLAR;
                } else {
                    throw new CompilerException("Reach EOF second time");
                }
            } else {
                throw new CompilerException("Lexical error");
            }
        }

        private void moveIn() {
            Assert.assertTrue(statusStack.size() == nodeStack.size());
            Assert.assertFalse(nodeTransferOperation.getNextClosureId() == -1);

            statusStack.push(nodeTransferOperation.getNextClosureId());

            assertNotNull(currentToken);
            nodeStack.addNormalSyntaxNode(currentToken.getId(), currentToken.getValue());

            currentToken = nextToken();
        }

        private void reduction() {
            Assert.assertTrue(statusStack.size() == nodeStack.size());

            PrimaryProduction ppReduction = nodeTransferOperation.getPrimaryProduction();
            Assert.assertNotNull(ppReduction);

            SyntaxNode leftNode;

            if (SymbolString.EPSILON_RAW.equals(ppReduction.getRight())) {
                leftNode = new SyntaxNode(ppReduction.getLeft(), null);
            } else {
                leftNode = nodeStack.get(-ppReduction.getRight().getSymbols().size() + 1);
            }

            onReduction(new Context(ppReduction, nodeStack, leftNode));

            /*
             * 如果是形如 "A → ε"这样的产生式，那么特殊处理一下（不进行出栈操作）
             */
            if (!SymbolString.EPSILON_RAW.equals(ppReduction.getRight())) {
                for (int i = 0; i < ppReduction.getRight().getSymbols().size(); i++) {
                    statusStack.pop();
                    nodeStack.pop();
                }
            }

            leftNode.setId(ppReduction.getLeft());
            leftNode.setValue(null);

            nodeStack.pushNormalSyntaxNode(leftNode);
        }

        /**
         * 规约时具体的语义动作，交由子类扩展
         *
         * @param context 上下文信息
         */
        protected void onReduction(Context context) {

        }

        private void jump() {
            Assert.assertTrue(statusStack.size() + 1 == nodeStack.size());

            Integer peekStatus = statusStack.peek();
            SyntaxNode peekNode = nodeStack.peek();

            assertNotNull(peekStatus);
            assertNotNull(peekNode);

            NodeTransferOperation nextNodeTransferOperation = getOperationFromAnalysisTable(peekStatus, peekNode.getId());
            Assert.assertNotNull(nextNodeTransferOperation);
            Assert.assertFalse(nextNodeTransferOperation.getNextClosureId() == -1);

            statusStack.push(nextNodeTransferOperation.getNextClosureId());
        }

        private String getFriendlyPrompt(String errorMsg) {
            StringBuilder sb = new StringBuilder();


            int lineIndex = 0;
            int lineFirstPos = 0;
            for (int i = 0; i < tokenIterator.position(); i++) {
                if (input.charAt(i) == '\n') {
                    sb.append(input, lineFirstPos, i + 1);
                    lineIndex = 0;
                    lineFirstPos = i + 1;
                } else {
                    lineIndex++;
                }
            }
            if (lineFirstPos < tokenIterator.position()) {
                sb.append(input, lineFirstPos, tokenIterator.position())
                        .append('\n');
            }

            int i = 0;
            while (i < lineIndex && isBlankChar(input.charAt(i + lineFirstPos))) {
                sb.append(' ');
                i++;
            }
            for (; i <= lineIndex; i++) {
                sb.append('~');
            }

            sb.append(" : ")
                    .append(errorMsg);

            return sb.toString();
        }
    }
}
