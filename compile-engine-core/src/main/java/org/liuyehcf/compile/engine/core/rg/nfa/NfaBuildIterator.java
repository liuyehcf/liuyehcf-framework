package org.liuyehcf.compile.engine.core.rg.nfa;

import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.rg.utils.EscapedUtil;
import org.liuyehcf.compile.engine.core.rg.utils.SymbolUtils;
import org.liuyehcf.compile.engine.core.utils.AssertUtils;
import org.liuyehcf.compile.engine.core.utils.ListUtils;
import org.liuyehcf.compile.engine.core.utils.Pair;

import java.util.*;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNotNull;

/**
 * <p>Nfa自动机构造器</p>
 * <p>todo 目前还不支持：1. 匹配模式的选择</p>
 * <p>图形符号：┐ ┘ └ ┌   ┴ ├ ┬ ┤  ┼   ─  │</p>
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class NfaBuildIterator {

    /**
     * 所有正则表达式语法中的符号
     */
    private List<Symbol> symbols;

    /**
     * 当前解析的位置
     */
    private int index;

    /**
     * 辅助栈
     */
    private LinkedList<StackUnion> unions;

    /**
     * 当前NfaClosure
     */
    private NfaClosure curNfaClosure;

    /**
     * 最终NfaClosure
     */
    private NfaClosure nfaClosure;

    /**
     * group辅助工具
     */
    private GroupUtil groupUtil;

    private NfaBuildIterator(List<Symbol> symbols) {
        this.symbols = symbols;
        index = 0;
        unions = new LinkedList<>();
        curNfaClosure = null;
        groupUtil = new GroupUtil();
    }

    static Pair<NfaClosure, Integer> createNfaClosure(List<Symbol> symbols) {
        NfaBuildIterator buildIterator = new NfaBuildIterator(symbols);

        while (buildIterator.hasNext()) {
            buildIterator.processEachSymbol();
        }

        buildIterator.finishWork();

        return new Pair<>(buildIterator.nfaClosure, buildIterator.groupUtil.getMaxGroup());
    }

    private StackUnion createStackUnitWithNfaClosure(NfaClosure nfaClosure) {
        return this.new StackUnion(nfaClosure);
    }

    private StackUnion createStackUnitWithParallelGroup() {
        return this.new StackUnion(null);
    }

    private void moveForward() {
        index += 1;
    }

    private Symbol getCurSymbol() {
        return symbols.get(index);
    }

    private Symbol getNextSymbol() {
        if (index + 1 < symbols.size()) {
            return symbols.get(index + 1);
        }
        return null;
    }

    private boolean hasNext() {
        return index < symbols.size();
    }

    private int getCurGroup() {
        return groupUtil.getCurGroup();
    }

    private void enterGroup() {
        groupUtil.enterGroup();
    }

    private void exitGroup() {
        groupUtil.exitGroup();
    }

    private void pushNfaClosure(NfaClosure nfaClosure) {
        unions.push(createStackUnitWithNfaClosure(nfaClosure));
    }

    private void pushCurNfaClosure() {
        if (curNfaClosure != null) {
            pushNfaClosure(curNfaClosure);
            curNfaClosure = null;
        }
    }

    private StackUnion popStackUnion() {
        if (unions.isEmpty()) {
            return null;
        }
        return unions.pop();
    }

    private void pushParallel() {
        unions.push(createStackUnitWithParallelGroup());
    }

    private void finishWork() {
        combineNfaClosuresOfCurGroup();

        AssertUtils.assertTrue(unions.isEmpty());

        if (curNfaClosure == null) {
            curNfaClosure = NfaClosure.getEmptyClosureForGroup(0);
        }

        setStartAndReceiveOfCurNfaClosure();

        nfaClosure = curNfaClosure;
    }

    private void processEachSymbol() {
        AssertUtils.assertTrue(getCurSymbol().isTerminator());
        Symbol curSymbol = getCurSymbol();

        if (SymbolUtils.ANY.equals(curSymbol)) {
            processWhenEncounteredAny();
        } else if (SymbolUtils.OR.equals(curSymbol)) {
            processWhenEncounteredOr();
        } else if (SymbolUtils.UN_KNOW.equals(curSymbol)) {
            processWhenEncounteredUnKnow();
        } else if (SymbolUtils.STAR.equals(curSymbol)) {
            processWhenEncounteredStar();
        } else if (SymbolUtils.ADD.equals(curSymbol)) {
            processWhenEncounteredAdd();
        } else if (SymbolUtils.LEFT_BIG_PARENTHESIS.equals(curSymbol)) {
            processWhenEncounteredLeftBigParenthesis();
        } else if (SymbolUtils.ESCAPED.equals(curSymbol)) {
            processWhenEncounteredEscaped();
        } else if (SymbolUtils.LEFT_MIDDLE_PARENTHESIS.equals(curSymbol)) {
            processWhenEncounteredLeftMiddleParenthesis();
        } else if (SymbolUtils.LEFT_SMALL_PARENTHESIS.equals(curSymbol)) {
            processWhenEncounteredLeftSmallParenthesis();
        } else if (SymbolUtils.RIGHT_SMALL_PARENTHESIS.equals(curSymbol)) {
            processWhenEncounteredRightSmallParenthesis();
        } else {
            processWhenEncounteredNormalSymbol();
        }
    }

    private void processWhenEncounteredAny() {
        pushCurNfaClosure();

        // 创建一个新的NfaClosure
        buildNfaClosureForAnyAsCurNfaClosure();

        moveForward();
    }

    private void buildNfaClosureForAnyAsCurNfaClosure() {
        curNfaClosure = buildNfaClosureWithSymbols(SymbolUtils.getAlphabetSymbols());
    }

    private void processWhenEncounteredOr() {
        // 合并左侧的NfaClosure
        combineNfaClosuresOfCurGroup();

        pushCurNfaClosure();

        // 插入一个parallel操作的占位符
        pushParallel();

        moveForward();
    }

    private void processWhenEncounteredUnKnow() {
        // 用一个新的NfaClosure封装当前NfaClosure
        wrapCurNfaClosureForUnKnow();

        pushCurNfaClosure();

        moveForward();
    }

    private void wrapCurNfaClosureForUnKnow() {
        curNfaClosure = createUnKnowWrappedNfaClosureFor(curNfaClosure);
    }

    private NfaClosure createUnKnowWrappedNfaClosureFor(NfaClosure inner) {
        /*
         * Inner: 内层NfaClosure
         * Outer: 外层NfaClosure
         * S: 开始节点
         * E(i): 第i个终止节点
         * P,Q: 外层NfaClosure的特殊节点
         * --*>: 经过多步跳转
         * -->: 经过一步跳转
         *
         *                                  Inner.S ───────*> Inner.E(1)
         *                                     ├───────────*> Inner.E(2)
         *                                     │       ...
         *                                     └───────────*> Inner.E(n)
         *
         *                                             ||
         *                                             ||
         *                                             ||
         *                                            \  /
         *                                             \/
         *
         *
         *                   请注意，步骤1，2的相对顺序不可乱，步骤的次序即匹配的策略（贪婪or勉强or占有），这里的连线都是ε边
         *
         *
         *                ┌────────────────────────────────────────── 2 ───────────────────────────────────────────────┐
         *                │                                                                                            │
         *                │                                                                                            V
         *             Outer.S ──── 1 ───> Inner.S ───────*> Inner.E(1) ──────── 4 ─────> Outer.Q ──────── 3 ─────> Outer.E
         *                                    │                                             Λ
         *                                    │                                             │
         *                                    ├───────────*> Inner.E(2) ──────── 4 ─────────┤
         *                                    │                 ...                         │
         *                                    └───────────*> Inner.E(n) ──────── 4 ─────────┘
         *
         */

        NfaClosure wrapNfaClosure = buildWrapNfaClosure();

        // 必须保证wrapNfaClosure单入单出，否则group匹配会出现边界问题（ "(a)?" 与 "(a?)" ）
        NfaState outerS = wrapNfaClosure.getStartNfaState();
        NfaState outerQ = new NfaState();
        NfaState outerE = wrapNfaClosure.getEndNfaStates().get(0);

        assertNotNull(inner);
        NfaState innerS = inner.getStartNfaState();

        // (1)
        outerS.addInputSymbolAndNextNfaState(Symbol.EPSILON, innerS);

        // (2)
        outerS.addInputSymbolAndNextNfaState(Symbol.EPSILON, outerE);

        // (3)
        outerQ.addInputSymbolAndNextNfaState(Symbol.EPSILON, outerE);

        for (NfaState innerE : inner.getEndNfaStates()) {
            // (4)
            innerE.addInputSymbolAndNextNfaState(Symbol.EPSILON, outerQ);
        }

        return wrapNfaClosure;
    }

    private NfaClosure buildWrapNfaClosure() {
        NfaState startNfaState = new NfaState();
        NfaState endNfaState = new NfaState();

        return new NfaClosure(startNfaState, ListUtils.of(endNfaState), getCurGroup());
    }

    private void processWhenEncounteredStar() {
        // 用一个新的NfaClosure封装当前NfaClosure
        wrapCurNfaClosureForStar();

        pushCurNfaClosure();

        moveForward();
    }

    private void wrapCurNfaClosureForStar() {
        curNfaClosure = createStarWrappedNfaClosureFor(curNfaClosure);
    }

    private NfaClosure createStarWrappedNfaClosureFor(NfaClosure inner) {
        /*
         * Inner: 内层NfaClosure
         * Outer: 外层NfaClosure
         * S: 开始节点
         * E(i): 第i个终止节点
         * P,Q: 外层NfaClosure的特殊节点
         * --*>: 经过多步跳转
         * -->: 经过一步跳转
         *
         *                                  Inner.S ───────*> Inner.E(1)
         *                                     ├───────────*> Inner.E(2)
         *                                     │       ...
         *                                     └───────────*> Inner.E(n)
         *
         *                                             ||
         *                                             ||
         *                                             ||
         *                                            \  /
         *                                             \/
         *
         *                   请注意，步骤2，3的相对顺序不可乱，步骤的次序即匹配的策略（贪婪or勉强or占有），这里的连线都是ε边
         *
         *                           ┌───────────────────────────────── 3 ──────────────────────────────┐
         *                           │                                                                  │
         *                           │                                                                  V
         *    Outer.S ─── 1 ───> Outer.P ───── 2 ───> Inner.S ───────*> Inner.E(1) ─────────┐        Outer.E
         *                           Λ                   ├───────────*> Inner.E(2) ─────┐   │
         *                           │                   │       ...                    │   │
         *                           │                   └───────────*> Inner.E(n) ──┐  │   │
         *                           │                                               │  │   │
         *                           │                                               │  │   │
         *                           ├───────────────────── 4 ───────────────────────┘  │   │
         *                           │                                                  │   │
         *                           ├───────────────────── 4 ──────────────────────────┘   │
         *                           │                                                      │
         *                           └───────────────────── 4 ──────────────────────────────┘
         *
         */

        NfaClosure outer = buildWrapNfaClosure();

        // 必须保证wrapNfaClosure单入单出，否则group匹配会出现边界问题（ "(a)*" 与 "(a*)" ）
        NfaState outerS = outer.getStartNfaState();
        NfaState outerE = outer.getEndNfaStates().get(0);
        NfaState outerP = new NfaState();

        assertNotNull(inner);
        NfaState innerS = inner.getStartNfaState();

        // (1)
        outerS.addInputSymbolAndNextNfaState(Symbol.EPSILON, outerP);

        // (2)
        outerP.addInputSymbolAndNextNfaState(Symbol.EPSILON, innerS);

        // (3)
        outerP.addInputSymbolAndNextNfaState(Symbol.EPSILON, outerE);

        for (NfaState innerE : inner.getEndNfaStates()) {
            // (4)
            innerE.addInputSymbolAndNextNfaState(Symbol.EPSILON, outerP);
        }

        return outer;
    }

    private void processWhenEncounteredAdd() {
        // 用一个新的NfaClosure封装当前NfaClosure
        wrapCurNfaClosureForAdd();

        pushCurNfaClosure();

        moveForward();
    }

    private void wrapCurNfaClosureForAdd() {
        curNfaClosure = createAddWrappedNfaClosureFor(curNfaClosure);
    }

    private NfaClosure createAddWrappedNfaClosureFor(NfaClosure inner) {
        /*
         * Inner: 内层NfaClosure
         * Outer: 外层NfaClosure
         * S: 开始节点
         * E(i): 第i个终止节点
         * P,Q: 外层NfaClosure的特殊节点
         * --*>: 经过多步跳转
         * -->: 经过一步跳转
         *
         *                                  Inner.S ───────*> Inner.E(1)
         *                                     ├───────────*> Inner.E(2)
         *                                     │       ...
         *                                     └───────────*> Inner.E(n)
         *
         *                                             ||
         *                                             ||
         *                                             ||
         *                                            \  /
         *                                             \/
         *
         *
         *                   请注意，步骤4，5的相对顺序不可乱，步骤的次序即匹配的策略（贪婪or勉强or占有），这里的连线都是ε边
         *
         *
         *    Outer.S ─── 1 ───> Outer.P ──── 2 ───> Inner.S ───────*> Inner.E(1) ─────────┬──────── 5 ──────> Outer.Q ───── 3 ───> Outer.E
         *                           Λ                  │                                  │                     Λ
         *                           │                  │                                  │                     │
         *                           │                  ├───────────*> Inner.E(2) ─────┬───┼──────── 5 ──────────┤
         *                           │                  │       ...                    │   │                     │
         *                           │                  └───────────*> Inner.E(n) ──┬──┼───┼──────── 5 ──────────┘
         *                           │                                              │  │   │
         *                           │                                              │  │   │
         *                           ├───────────────────── 4 ──────────────────────┘  │   │
         *                           │                                                 │   │
         *                           ├───────────────────── 4 ─────────────────────────┘   │
         *                           │                                                     │
         *                           └───────────────────── 4 ─────────────────────────────┘
         *
         *
         */

        NfaClosure wrapNfaClosure = buildWrapNfaClosure();

        // 必须保证wrapNfaClosure单入单出，否则group匹配会出现边界问题（ "(a)+" 与 "(a+)" ）
        NfaState outerS = wrapNfaClosure.getStartNfaState();
        NfaState outerP = new NfaState();
        NfaState outerQ = new NfaState();
        NfaState outerE = wrapNfaClosure.getEndNfaStates().get(0);

        assertNotNull(inner);
        NfaState innerS = inner.getStartNfaState();

        // (1)
        outerS.addInputSymbolAndNextNfaState(Symbol.EPSILON, outerP);

        // (2)
        outerP.addInputSymbolAndNextNfaState(Symbol.EPSILON, innerS);

        // (3)
        outerQ.addInputSymbolAndNextNfaState(Symbol.EPSILON, outerE);

        for (NfaState innerE : inner.getEndNfaStates()) {
            // (4)
            innerE.addInputSymbolAndNextNfaState(Symbol.EPSILON, outerP);

            // (5)
            innerE.addInputSymbolAndNextNfaState(Symbol.EPSILON, outerQ);
        }

        return wrapNfaClosure;
    }

    private void processWhenEncounteredLeftBigParenthesis() {
        // 用一个新的NfaClosure封装当前NfaClosure
        wrapCurNfaClosureForLeftBigParenthesis();

        pushCurNfaClosure();
    }

    private void wrapCurNfaClosureForLeftBigParenthesis() {
        curNfaClosure = createLeftBigParenthesisWrappedNfaClosureFor(getRepeatInterval());
    }

    private NfaClosure createLeftBigParenthesisWrappedNfaClosureFor(Pair<Integer, Integer> repeatInterval) {
        // 依据指定的重复区间，构建NfaClosure
        return buildNfaClosureForRepeatInterval(repeatInterval);
    }

    private NfaClosure buildNfaClosureForRepeatInterval(Pair<Integer, Integer> repeatInterval) {
        assertNotNull(repeatInterval.getFirst());

        NfaClosure newNfaClosure;

        // a{1,}
        if (repeatInterval.getSecond() == null) {
            newNfaClosure = buildRepeatedNfaClosureFor(curNfaClosure, repeatInterval.getFirst());

            combineTwoClosure(
                    newNfaClosure,
                    createStarWrappedNfaClosureFor(curNfaClosure)
            );
            return newNfaClosure;
        } else {
            newNfaClosure = buildRepeatedNfaClosureFor(curNfaClosure, repeatInterval.getFirst());
            for (int repeatTime = repeatInterval.getFirst() + 1; repeatTime <= repeatInterval.getSecond();
                 repeatTime++) {
                parallel(
                        newNfaClosure,
                        buildRepeatedNfaClosureFor(curNfaClosure, repeatTime)
                );
            }
        }

        return newNfaClosure;
    }

    private NfaClosure buildRepeatedNfaClosureFor(NfaClosure originalNfaClosure, int repeatTime) {
        int count = 1;

        NfaClosure repeatedNfaClosure = originalNfaClosure.clone();

        while (count < repeatTime) {
            NfaClosure newClosure = originalNfaClosure.clone();
            combineTwoClosure(repeatedNfaClosure, newClosure);
            count++;
        }

        return repeatedNfaClosure;
    }

    private Pair<Integer, Integer> getRepeatInterval() {
        moveForward();

        Integer leftNumber = null, rightNumber = null;

        StringBuilder sb = new StringBuilder();
        while (!SymbolUtils.RIGHT_BIG_PARENTHESIS.equals(getCurSymbol())) {

            if (SymbolUtils.getChar(getCurSymbol()) == ',') {
                AssertUtils.assertNull(leftNumber);
                leftNumber = Integer.parseInt(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(SymbolUtils.getChar(getCurSymbol()));
            }

            moveForward();
        }

        if (leftNumber == null) {
            leftNumber = Integer.parseInt(sb.toString());
            rightNumber = leftNumber;
        } else {
            if (sb.length() > 0) {
                rightNumber = Integer.parseInt(sb.toString());
            }
        }

        if (rightNumber != null) {
            AssertUtils.assertTrue(rightNumber >= leftNumber);
        }

        moveForward();

        return new Pair<>(leftNumber, rightNumber);
    }

    private void processWhenEncounteredEscaped() {
        pushCurNfaClosure();

        // 创建一个新的NfaClosure
        buildNfaClosureForEscapedAsCurNfaClosure();

        moveForward();
    }

    private void buildNfaClosureForEscapedAsCurNfaClosure() {
        moveForward();

        curNfaClosure = buildNfaClosureWithSymbols(
                EscapedUtil.getSymbolsOfEscapedChar(
                        SymbolUtils.getChar(getCurSymbol())));
    }

    private void processWhenEncounteredLeftMiddleParenthesis() {
        pushCurNfaClosure();

        // 创建一个新的NfaClosure
        buildNfaClosureForMiddleParenthesisAsCurNfaClosure();
    }

    private void buildNfaClosureForMiddleParenthesisAsCurNfaClosure() {
        curNfaClosure = buildNfaClosureWithSymbols(getOptionalSymbols());
    }

    private Set<Symbol> getOptionalSymbols() {
        moveForward();
        boolean isNot = SymbolUtils.MIDDLE_PARENTHESIS_NOT.equals(getCurSymbol());
        if (isNot) {
            moveForward();
        }

        Set<Symbol> optionalSymbols = new HashSet<>();

        int pre = -1;
        boolean hasTo = false;

        do {
            if (SymbolUtils.ESCAPED.equals(getCurSymbol())) {
                moveForward();
                optionalSymbols.addAll(
                        EscapedUtil.getSymbolsOfEscapedCharInMiddleParenthesis(
                                SymbolUtils.getChar(getCurSymbol())));
                pre = -1;
            }
            // '-'前面存在有效字符时
            else if (pre != -1 && SymbolUtils.TO.equals(getCurSymbol())) {
                AssertUtils.assertFalse(hasTo);
                hasTo = true;
            } else {
                if (hasTo) {
                    AssertUtils.assertTrue(pre != -1);
                    AssertUtils.assertTrue(pre <= SymbolUtils.getChar(getCurSymbol()));
                    // pre在上一次已经添加过了，本次从pre+1开始
                    for (char c = (char) (pre + 1); c <= SymbolUtils.getChar(getCurSymbol()); c++) {
                        optionalSymbols.add(SymbolUtils.getAlphabetSymbolWithChar(c));
                    }
                    pre = -1;
                    hasTo = false;
                } else {
                    pre = SymbolUtils.getChar(getCurSymbol());
                    optionalSymbols.add(getCurSymbol());
                }
            }
            moveForward();
        } while (!SymbolUtils.RIGHT_MIDDLE_PARENTHESIS.equals(getCurSymbol()));

        // 最后一个'-'当做普通字符
        if (hasTo) {
            optionalSymbols.add(SymbolUtils.TO);
        }

        moveForward();

        if (isNot) {
            return SymbolUtils.getOppositeSymbols(optionalSymbols);
        } else {
            return optionalSymbols;
        }
    }

    private void processWhenEncounteredLeftSmallParenthesis() {
        enterGroup();

        // 如果出现了()这种情况，那么才特殊处理一下。不加这个条件将会出现多余的ε边
        if (SymbolUtils.RIGHT_SMALL_PARENTHESIS.equals(getNextSymbol())) {
            pushCurNfaClosure();
            buildNonOrdinaryNfaClosure();
        }

        moveForward();
    }

    private void processWhenEncounteredRightSmallParenthesis() {
        // 合并NfaClosure
        combineNfaClosuresOfCurGroup();

        // 为当前组设置接受以及起始状态标记
        setStartAndReceiveOfCurNfaClosure();

        exitGroup();

        // 修改当前NfaClosure的组
        changeGroupOfCurNfaClosure();

        moveForward();
    }

    private void combineNfaClosuresOfCurGroup() {
        pushCurNfaClosure();

        StackUnion topStackUnion;
        StackUnion secondTopStackUnion;
        StackUnion thirdTopStackUnion;

        while ((topStackUnion = popStackUnion()) != null
                && (secondTopStackUnion = popStackUnion()) != null) {
            AssertUtils.assertTrue(topStackUnion.isNfaClosure());

            if (secondTopStackUnion.isNfaClosure()) {
                if (secondTopStackUnion.getNfaClosure().getGroup()
                        != topStackUnion.getNfaClosure().getGroup()) {
                    // case "a(b)"
                    unions.push(secondTopStackUnion);
                    break;
                }
                combineTwoClosure(
                        secondTopStackUnion.getNfaClosure(),
                        topStackUnion.getNfaClosure());
                unions.push(secondTopStackUnion);
            } else {
                AssertUtils.assertFalse(unions.isEmpty());
                thirdTopStackUnion = unions.pop();
                AssertUtils.assertTrue(thirdTopStackUnion.isNfaClosure());
                if (thirdTopStackUnion.getNfaClosure().getGroup()
                        != topStackUnion.getNfaClosure().getGroup()) {
                    // case "((a)|(b))"，将parallel操作滞后到group变更后
                    unions.push(thirdTopStackUnion);
                    unions.push(secondTopStackUnion);
                    break;
                }

                parallel(
                        thirdTopStackUnion.getNfaClosure(),
                        topStackUnion.getNfaClosure());
                unions.push(thirdTopStackUnion);
            }
        }

        if (topStackUnion == null) {
            // when "()"
            topStackUnion = createStackUnitWithNfaClosure(
                    NfaClosure.getEmptyClosureForGroup(getCurGroup()));
        }

        curNfaClosure = topStackUnion.getNfaClosure();
    }

    private void setStartAndReceiveOfCurNfaClosure() {
        assertNotNull(curNfaClosure);
        curNfaClosure.setStartAndReceive(getCurGroup());
    }

    private void changeGroupOfCurNfaClosure() {
        assertNotNull(curNfaClosure);
        curNfaClosure.setGroup(getCurGroup());
    }

    private void processWhenEncounteredNormalSymbol() {
        pushCurNfaClosure();

        // 创建一个新的NfaClosure
        buildNfaClosureForNormalSymbol();

        moveForward();
    }

    private void buildNfaClosureForNormalSymbol() {
        curNfaClosure = buildNfaClosureWithSymbols(Collections.singletonList(getCurSymbol()));
    }

    private void buildNonOrdinaryNfaClosure() {
        NfaState startNfaState = new NfaState();
        List<NfaState> endNfaStates = new ArrayList<>();

        // todo 这里创建了一个多余的EPSILON边
        startNfaState.addInputSymbolAndNextNfaState(Symbol.EPSILON, startNfaState);
        endNfaStates.add(startNfaState);

        curNfaClosure = new NfaClosure(startNfaState, endNfaStates, getCurGroup());
    }

    private NfaClosure buildNfaClosureWithSymbols(Collection<Symbol> symbols) {
        AssertUtils.assertFalse(symbols.isEmpty());

        NfaState startNfaState = new NfaState();
        List<NfaState> endNfaStates = new ArrayList<>();

        for (Symbol symbol : symbols) {
            NfaState curNfaState = new NfaState();
            startNfaState.addInputSymbolAndNextNfaState(symbol, curNfaState);
            endNfaStates.add(curNfaState);
        }

        return new NfaClosure(startNfaState, endNfaStates, getCurGroup());
    }

    private void combineTwoClosure(NfaClosure pre, NfaClosure next) {
        AssertUtils.assertTrue(pre.getGroup() == next.getGroup());

        /*
         *
         * Pre: 左侧NfaClosure
         * Next: 右侧NfaClosure
         * S: 开始节点
         * E(i): 第i个终止节点
         * S.N(i): 开始节点的第i个后继节点
         * --*>: 经过多步跳转
         * -->: 经过一步跳转
         *
         *            ┌──────────*> Pre.E(1)                            ┌───────────*> Next.E(1)
         *            │                                                 │
         *          Pre.S────────*> Pre.E(2)                        Next.S ─────────*> Next.E(2)
         *            │       ...                                       │       ...
         *            └──────────*> Pre.E(n)                            └───────────*> Next.E(m)
         *
         *                                             ||
         *                                             ||
         *                                             ||
         *                                            \  /
         *                                             \/
         *
         *            ┌──────────*> Pre.E(1) ────────── 1 ──────────┐   ┌───────────*> Next.E(1)
         *            │                                             V   │
         *          Pre.S────────*> Pre.E(2) ────────── 1 ────────> Next.S ─────────*> Next.E(2)
         *            │               ...                           Λ   │       ...
         *            └──────────*> Pre.E(n) ────────── 1 ──────────┘   └───────────*> Next.E(m)
         *
         */

        NfaState nextS = next.getStartNfaState();

        for (NfaState preE : pre.getEndNfaStates()) {
            // (1)
            preE.addInputSymbolAndNextNfaState(Symbol.EPSILON, nextS);
        }

        pre.setEndNfaStates(next.getEndNfaStates());
    }

    private void parallel(NfaClosure pre, NfaClosure next) {
        /*
         *
         * Pre: 左侧NfaClosure
         * Next: 右侧NfaClosure
         * S: 开始节点
         * E(i): 第i个终止节点
         * S.N(i): 开始节点的第i个后继节点
         * --*>: 经过多步跳转
         * -->: 经过一步跳转
         *
         *            ┌──────────*> Pre.E(1)                            ┌───────────*> Next.E(1)
         *            │                                                 │
         *          Pre.S────────*> Pre.E(2)                        Next.S ─────────*> Next.E(2)
         *            │       ...                                       │       ...
         *            └──────────*> Pre.E(n)                            └───────────*> Next.E(m)
         *
         *                                             ||
         *                                             ||
         *                                             ||
         *                                            \  /
         *                                             \/
         *
         *            ┌──────────*> Pre.E(1)                            ┌───────────*> Next.E(1)
         *            │                                                 │
         *          Pre.S────────*> Pre.E(2)            ┌─────────> Next.S ─────────*> Next.E(2)
         *            │       ...                       │               │       ...
         *            ├──────────*> Pre.E(n)            │               └───────────*> Next.E(m)
         *            │                                 │
         *            └──────────────────────── 1 ──────┘
         *
         *   ******************************************************************************************
         *   **                                                                                      **
         *   **   parallel不能用一个新的单入单出的NfaClosure包裹起来，这样会导致"(a)|(b)|(ab)"只有一个出口   **
         *   **       必须保证多出的特性("NfaMatcher.initMatchIntervals"方法会利用该特性实现贪婪find)      **
         *   **                                                                                      **
         *   ******************************************************************************************
         */

        NfaState preS = pre.getStartNfaState();
        NfaState nextS = next.getStartNfaState();

        /*
         * (1)
         * NfaState中的邻接节点用的是LinkedHashMap，且对于同一个输入符号的不同后继邻接节点用的是LinkedHashSet，保证了邻接节点的相对顺序
         * 结合"NfaMatcher.isMatchDfs"方法的实现，就可以保证 "(a)|(a*)" 匹配"a"时，group(1)="a", group(2)=null
         */
        preS.addInputSymbolAndNextNfaState(Symbol.EPSILON, nextS);

        // 更新Pre的终止节点
        pre.getEndNfaStates().addAll(next.getEndNfaStates());
    }

    private static class GroupUtil {
        private int groupCount = 0;
        private int maxGroup = 0;
        private LinkedList<Integer> groupStack;

        private GroupUtil() {
            groupStack = new LinkedList<>();
            groupStack.push(0);
        }

        private int getCurGroup() {
            Integer peek = groupStack.peek();
            assertNotNull(peek);
            return peek;
        }

        private int getMaxGroup() {
            return maxGroup;
        }

        private void enterGroup() {
            groupCount++;
            maxGroup = Math.max(maxGroup, groupCount);
            groupStack.push(groupCount);
        }

        private void exitGroup() {
            groupStack.pop();
        }
    }

    /**
     * 栈元素，元素为NfaClosure，或者一个占位符 当nfaClosure不为空时，就持有了一个NfaClosure 当nfaClosure为空时，即遇到'|'符号，压入了一个占位符
     */
    private class StackUnion {
        private NfaClosure nfaClosure;

        private StackUnion(NfaClosure nfaClosure) {
            this.nfaClosure = nfaClosure;
        }

        private boolean isNfaClosure() {
            return nfaClosure != null;
        }

        private boolean isParallel() {
            return !isNfaClosure();
        }

        private NfaClosure getNfaClosure() {
            AssertUtils.assertTrue(isNfaClosure());
            return nfaClosure;
        }

        @Override
        public String toString() {
            return isNfaClosure() ?
                    getNfaClosure().toString() + " group[" + getNfaClosure().getGroup() + "]"
                    : "Parallel";
        }
    }
}
