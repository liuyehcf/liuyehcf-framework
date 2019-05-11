package org.liuyehcf.compile.engine.core.rg.utils;

import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.utils.Assert;
import org.liuyehcf.compile.engine.core.utils.ListUtils;
import org.liuyehcf.compile.engine.core.utils.Pair;

import java.util.*;

/**
 * 正则测试用例创建类
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public abstract class AbstractTestCaseBuilder {

    private static char[] alphabetCharacters = new char[256];

    static {
        for (char c = 0; c < 256; c++) {
            alphabetCharacters[c] = c;
        }
    }

    protected final String regex;
    int index = 0;
    String curContent;
    Set<String> testCases = new HashSet<>();
    private LinkedList<String> contentStack = new LinkedList<>();
    private LinkedList<LinkedList<String>> revokeHelper = new LinkedList<>();

    AbstractTestCaseBuilder(String regex) {
        this.regex = regex;
    }

    public static Set<String> createAllOptionalTestCasesWithRegex(String regex) {
        return EachCaseBuilder.getEachTestCasesWithRegex(regex);
    }

    public static Set<String> createRandomTestCasesWithRegex(String regex, int times) {
        Set<String> randomTestCases = new HashSet<>();
        for (int time = 0; time < times; time++) {
            randomTestCases.addAll(RandomCaseBuilder.getRandomTestCasesWithRegex(regex));
        }
        return randomTestCases;
    }

    private static Set<Character> getOppositeChars(Set<Character> excludedChars) {
        Set<Character> oppositeChars = new HashSet<>();
        for (char c : alphabetCharacters) {
            oppositeChars.add(c);
        }
        oppositeChars.removeAll(excludedChars);
        return oppositeChars;
    }

    void build() {
        backtracking();
    }

    void backtracking() {
        if (!hasNext()) {
            addTestCase();
            return;
        }
        switch (getCurChar()) {
            case '.':
                processWhenEncounteredAny();
                break;
            case '|':
                processWhenEncounteredOr();
                break;
            case '?':
                processWhenEncounteredUnKnow();
                break;
            case '*':
                processWhenEncounteredStar();
                break;
            case '+':
                processWhenEncounteredAdd();
                break;
            case '{':
                processWhenEncounteredLeftBigParenthesis();
                break;
            case '\\':
                processWhenEncounteredEscaped();
                break;
            case '[':
                processWhenEncounteredLeftMiddleParenthesis();
                break;
            case '(':
                processWhenEncounteredLeftSmallParenthesis();
                break;
            default:
                processWhenEncounteredNormal();
        }
    }

    private boolean hasNext() {
        return index < regex.length();
    }

    private void addTestCase() {
        pushCurStackUnion();
        testCases.add(getStackString());
        popToCurStackUnion();
    }

    private String getStackString() {
        Iterator<String> it = contentStack.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            sb.insert(0, it.next());
        }
        return sb.toString();
    }

    char getCurChar() {
        return regex.charAt(index);
    }

    private boolean preCharIsNotEscaped() {
        if (index > 0) {
            return regex.charAt(index - 1) != '\\';
        } else {
            return true;
        }
    }

    /**
     * '.'时的处理操作
     */
    protected abstract void processWhenEncounteredAny();

    /**
     * '|'时的处理操作
     */
    protected abstract void processWhenEncounteredOr();

    /**
     * '?'时的处理操作
     */
    protected abstract void processWhenEncounteredUnKnow();

    /**
     * '*'时的处理操作
     */
    protected abstract void processWhenEncounteredStar();

    /**
     * '+'时的处理操作
     */
    protected abstract void processWhenEncounteredAdd();

    /**
     * '{'时的处理操作
     */
    protected abstract void processWhenEncounteredLeftBigParenthesis();

    /**
     * '\'时的处理操作
     */
    protected abstract void processWhenEncounteredEscaped();

    /**
     * '['时的处理操作
     */
    protected abstract void processWhenEncounteredLeftMiddleParenthesis();

    /**
     * '('时的处理操作
     */
    protected abstract void processWhenEncounteredLeftSmallParenthesis();

    /**
     * 普通字符时的处理操作
     */
    protected abstract void processWhenEncounteredNormal();

    /**
     * 给定正则表达式，返回匹配的测试用例
     *
     * @param regex 正则表达式
     * @return 测试用例集合
     */
    protected abstract Set<String> getTestCasesWithRegex(String regex);

    void pushCurStackUnion() {
        if (curContent != null) {
            contentStack.push(curContent);
            curContent = null;
        }
    }

    void popToCurStackUnion() {
        if (!contentStack.isEmpty()) {
            curContent = contentStack.pop();
        } else {
            curContent = null;
        }
    }

    String getCombinedStringOfCurGroup() {
        LinkedList<String> curRevokeStack = new LinkedList<>();

        StringBuilder sb = new StringBuilder();

        while (!contentStack.isEmpty()) {
            String peekContent = contentStack.pop();
            sb.insert(0, peekContent);
            curRevokeStack.push(peekContent);
        }

        revokeHelper.push(curRevokeStack);

        return sb.toString();
    }

    List<List<String>> getTestCasesOfAllParts(String combinedStringOfCurGroup) {

        List<List<String>> testCasesOfAllParts = new ArrayList<>();
        testCasesOfAllParts.add(ListUtils.of(combinedStringOfCurGroup));

        addAllAdjacentOrParts(testCasesOfAllParts);

        return testCasesOfAllParts;
    }

    private void addAllAdjacentOrParts(List<List<String>> testCasesOfAllParts) {
        do {
            testCasesOfAllParts.add(getTestCasesOfNextPart());
        } while (hasNext() && preCharIsNotEscaped() && getCurChar() == '|');
    }

    void revokeCombinedStringOfCurGroup() {
        LinkedList<String> curRevokeStack = revokeHelper.pop();

        while (!curRevokeStack.isEmpty()) {
            contentStack.push(curRevokeStack.pop());
        }
    }

    String copy(String origin, int times) {
        StringBuilder sb = new StringBuilder();
        while (times-- > 0) {
            sb.append(origin);
        }
        return sb.toString();
    }

    Pair<Integer, Integer> getRepeatInterval() {
        index++;

        Integer leftNumber = null, rightNumber = null;

        char c;
        StringBuilder sb = new StringBuilder();
        while ((c = getCurChar()) != '}') {

            if (c == ',') {
                Assert.assertNull(leftNumber);
                leftNumber = Integer.parseInt(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }

            index++;
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
            Assert.assertTrue(rightNumber >= leftNumber);
        }

        index++;

        return new Pair<>(leftNumber, rightNumber);
    }

    List<Character> getAllOptionalChars() {
        index++;

        boolean isNot = (getCurChar() == '^');
        if (isNot) {
            index++;
        }

        Set<Character> optionalChars = new HashSet<>();

        int pre = -1;
        boolean hasTo = false;

        do {
            if (getCurChar() == '\\') {
                index++;
                for (Symbol symbol : EscapedUtils.getSymbolsOfEscapedCharInMiddleParenthesis(getCurChar())) {
                    optionalChars.add(symbol.getValue().charAt(0));
                }
                pre = -1;
            }
            /*
             * '-'前面存在有效字符时
             */
            else if (pre != -1 && getCurChar() == '-') {
                Assert.assertFalse(hasTo);
                hasTo = true;
            } else {
                if (hasTo) {
                    Assert.assertTrue(pre != -1);
                    Assert.assertTrue(pre <= getCurChar());
                    /*
                     * pre在上一次已经添加过了，本次从pre+1开始
                     */
                    for (char c = (char) (pre + 1); c <= getCurChar(); c++) {
                        optionalChars.add(c);
                    }
                    pre = -1;
                    hasTo = false;
                } else {
                    pre = getCurChar();
                    optionalChars.add(getCurChar());
                }

            }
            index++;
        } while (getCurChar() != ']');

        /*
         * 最后一个'-'当做普通字符
         */
        if (hasTo) {
            optionalChars.add('-');
        }

        index++;

        if (isNot) {
            optionalChars = getOppositeChars(optionalChars);
        }

        return new ArrayList<>(optionalChars);
    }

    List<String> getTestCasesOfNextPart() {
        int count = 1;
        index++;
        int startIndex = index, endIndex;
        while (hasNext() && count > 0) {
            if (preCharIsNotEscaped() && getCurChar() == '(') {
                count++;
            } else if (preCharIsNotEscaped() && getCurChar() == ')') {
                count--;
            }
            index++;
        }

        if (count == 0) {
            endIndex = index - 1;
        } else {
            endIndex = index;
        }

        return new ArrayList<>(getTestCasesWithRegex(regex.substring(startIndex, endIndex)));
    }

    private static final class EachCaseBuilder extends AbstractTestCaseBuilder {

        private EachCaseBuilder(String regex) {
            super(regex);
        }

        private static Set<String> getEachTestCasesWithRegex(String regex) {
            EachCaseBuilder testCaseBuilder = new EachCaseBuilder(regex);
            testCaseBuilder.build();
            return testCaseBuilder.testCases;
        }

        @Override
        protected void processWhenEncounteredAny() {
            pushCurStackUnion();
            for (char c = 0; c < 256; c++) {
                if (!SymbolUtils.isLegalCharMatchesAny(c)) {
                    continue;
                }
                curContent = "" + c;
                index++;
                backtracking();
                index--;
            }
            popToCurStackUnion();
        }

        @Override
        protected void processWhenEncounteredOr() {
            int tempIndex = index;
            pushCurStackUnion();
            String combinedStringOfCurGroup = getCombinedStringOfCurGroup();

            List<List<String>> testCasesOfAllParts = getTestCasesOfAllParts(combinedStringOfCurGroup);

            for (List<String> testCases : testCasesOfAllParts) {
                for (String testCase : testCases) {
                    curContent = testCase;
                    backtracking();
                }
            }

            revokeCombinedStringOfCurGroup();
            popToCurStackUnion();
            index = tempIndex;
        }

        @Override
        protected void processWhenEncounteredUnKnow() {
            makeDifferentRepeatCases(ListUtils.of(0, 1));
        }

        @Override
        protected void processWhenEncounteredStar() {
            makeDifferentRepeatCases(ListUtils.of(0, 1, 2, 4, 8));
        }

        private void makeDifferentRepeatCases(List<Integer> repeatTimes) {
            String tempCurStackUnion = curContent;
            for (int repeatTime : repeatTimes) {
                makeSpecificRepeatCase(repeatTime, tempCurStackUnion);
            }
        }

        private void makeSpecificRepeatCase(int repeatTime, String tempCurStackUnion) {
            index++;

            curContent = copy(tempCurStackUnion, repeatTime);
            backtracking();

            index--;
        }

        @Override
        protected void processWhenEncounteredAdd() {
            makeDifferentRepeatCases(ListUtils.of(1, 2, 4, 8));
        }

        @Override
        protected void processWhenEncounteredLeftBigParenthesis() {
            int tempIndex = index;
            Pair<Integer, Integer> repeatInterval = getRepeatInterval();

            if (repeatInterval.getSecond() == null) {
                repeatInterval = new Pair<>(repeatInterval.getFirst(), repeatInterval.getFirst() + 8);
            }

            List<Integer> repeatTimes = new ArrayList<>();
            for (int i = repeatInterval.getFirst(); i <= repeatInterval.getSecond(); i++) {
                repeatTimes.add(i);
            }

            makeDifferentRepeatCases(repeatTimes);

            index = tempIndex;
        }

        @Override
        protected void processWhenEncounteredEscaped() {
            int tempIndex = index;
            pushCurStackUnion();

            index++;

            List<Symbol> symbols = EscapedUtils.getSymbolsOfEscapedChar(getCurChar());

            index++;
            for (Symbol symbol : symbols) {
                curContent = symbol.getValue();
                backtracking();
            }

            popToCurStackUnion();
            index = tempIndex;
        }

        @Override
        protected void processWhenEncounteredLeftMiddleParenthesis() {
            int tempIndex = index;
            pushCurStackUnion();

            List<Character> optionalChars = getAllOptionalChars();

            for (char optionChar : optionalChars) {
                curContent = "" + optionChar;
                backtracking();
            }

            popToCurStackUnion();
            index = tempIndex;
        }

        @Override
        protected void processWhenEncounteredLeftSmallParenthesis() {
            int tempIndex = index;
            pushCurStackUnion();

            List<String> nextPartTestCases = getTestCasesOfNextPart();

            chooseEachCases(nextPartTestCases);

            popToCurStackUnion();
            index = tempIndex;
        }

        private void chooseEachCases(List<String> nextPartTestCases) {
            for (String testCase : nextPartTestCases) {
                curContent = testCase;
                backtracking();
            }
        }

        @Override
        protected void processWhenEncounteredNormal() {
            int tempIndex = index;
            pushCurStackUnion();

            curContent = "" + getCurChar();
            index++;
            backtracking();

            popToCurStackUnion();
            index = tempIndex;
        }

        @Override
        protected Set<String> getTestCasesWithRegex(String regex) {
            return getEachTestCasesWithRegex(regex);
        }
    }

    private static final class RandomCaseBuilder extends AbstractTestCaseBuilder {
        private static final Random RANDOM = new Random();

        private RandomCaseBuilder(String regex) {
            super(regex);
        }

        private static Set<String> getRandomTestCasesWithRegex(String regex) {
            RandomCaseBuilder testCaseBuilder = new RandomCaseBuilder(regex);
            testCaseBuilder.backtracking();
            return testCaseBuilder.testCases;
        }

        @Override
        protected void processWhenEncounteredAny() {
            char c = (char) RANDOM.nextInt(256);
            while (!SymbolUtils.isLegalCharMatchesAny(c)) {
                c = (char) RANDOM.nextInt(256);
            }

            pushCurStackUnion();
            curContent = "" + c;
            index++;

            backtracking();
        }

        @Override
        protected void processWhenEncounteredOr() {
            pushCurStackUnion();
            String combinedStringOfCurGroup = getCombinedStringOfCurGroup();

            List<List<String>> testCasesOfAllParts = getTestCasesOfAllParts(combinedStringOfCurGroup);

            int textCaseIndex = RANDOM.nextInt(testCasesOfAllParts.size());

            Assert.assertTrue(testCasesOfAllParts.get(textCaseIndex).size() == 1);
            String testCase = testCasesOfAllParts.get(textCaseIndex).iterator().next();

            pushCurStackUnion();
            curContent = testCase;

            backtracking();
        }

        @Override
        protected void processWhenEncounteredUnKnow() {
            index++;

            curContent = copy(curContent, RANDOM.nextInt(2));

            backtracking();
        }

        @Override
        protected void processWhenEncounteredStar() {
            index++;

            curContent = copy(curContent, RANDOM.nextInt(8));

            backtracking();
        }

        @Override
        protected void processWhenEncounteredAdd() {
            index++;

            curContent = copy(curContent, RANDOM.nextInt(8) + 1);

            backtracking();
        }

        @Override
        protected void processWhenEncounteredLeftBigParenthesis() {
            Pair<Integer, Integer> repeatInterval = getRepeatInterval();

            if (repeatInterval.getSecond() == null) {
                repeatInterval = new Pair<>(repeatInterval.getFirst(), repeatInterval.getFirst() + 8);
            }

            curContent = copy(curContent,
                    RANDOM.nextInt(repeatInterval.getSecond() - repeatInterval.getFirst() + 1) + +repeatInterval
                            .getFirst());

            backtracking();
        }

        @Override
        protected void processWhenEncounteredEscaped() {
            pushCurStackUnion();

            index++;

            List<Symbol> symbols = EscapedUtils.getSymbolsOfEscapedChar(getCurChar());

            index++;
            int charIndex = RANDOM.nextInt(symbols.size());
            curContent = symbols.get(charIndex).getValue();

            backtracking();
        }

        @Override
        protected void processWhenEncounteredLeftMiddleParenthesis() {
            pushCurStackUnion();

            List<Character> optionalChars = getAllOptionalChars();
            int charIndex = RANDOM.nextInt(optionalChars.size());

            curContent = "" + optionalChars.get(charIndex);

            backtracking();
        }

        @Override
        protected void processWhenEncounteredLeftSmallParenthesis() {
            pushCurStackUnion();

            List<String> nextPartTestCases = new ArrayList<>(getTestCasesOfNextPart());

            if (nextPartTestCases.size() != 0) {
                int testCaseIndex = RANDOM.nextInt(nextPartTestCases.size());

                curContent = nextPartTestCases.get(testCaseIndex);
            }

            backtracking();
        }

        @Override
        protected void processWhenEncounteredNormal() {
            pushCurStackUnion();

            curContent = "" + getCurChar();
            index++;

            backtracking();
        }

        @Override
        protected Set<String> getTestCasesWithRegex(String regex) {
            return getRandomTestCasesWithRegex(regex);
        }
    }
}

