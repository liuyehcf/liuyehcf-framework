package org.liuyehcf.compile.engine.core.rg.utils;

import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.utils.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 正则特殊符号工具类
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public abstract class SymbolUtils {

    private static List<Symbol> alphabetSymbols = null;
    private static List<Symbol> alphabetSymbolsMatchesAny = null;
    public static final Symbol UN_KNOW = getAlphabetSymbolWithChar('?');
    public static final Symbol ANY = getAlphabetSymbolWithChar('.');
    public static final Symbol OR = getAlphabetSymbolWithChar('|');
    public static final Symbol STAR = getAlphabetSymbolWithChar('*');
    public static final Symbol ADD = getAlphabetSymbolWithChar('+');
    public static final Symbol ESCAPED = getAlphabetSymbolWithChar('\\');
    public static final Symbol LEFT_MIDDLE_PARENTHESIS = getAlphabetSymbolWithChar('[');
    public static final Symbol RIGHT_MIDDLE_PARENTHESIS = getAlphabetSymbolWithChar(']');
    public static final Symbol MIDDLE_PARENTHESIS_NOT = getAlphabetSymbolWithChar('^');
    public static final Symbol LEFT_SMALL_PARENTHESIS = getAlphabetSymbolWithChar('(');
    public static final Symbol RIGHT_SMALL_PARENTHESIS = getAlphabetSymbolWithChar(')');
    public static final Symbol LEFT_BIG_PARENTHESIS = getAlphabetSymbolWithChar('{');
    public static final Symbol RIGHT_BIG_PARENTHESIS = getAlphabetSymbolWithChar('}');
    public static final Symbol TO = getAlphabetSymbolWithChar('-');

    public static Symbol getAlphabetSymbolWithChar(char symbol) {
        if (alphabetSymbols == null) {
            alphabetSymbols = new ArrayList<>();
            alphabetSymbolsMatchesAny = new ArrayList<>();
            for (char c = 0; c < 256; c++) {
                alphabetSymbols.add(Symbol.createTerminator("" + c));
                /*
                 * jump over undefined chars
                 */
                if (isLegalCharMatchesAny(c)) {
                    alphabetSymbolsMatchesAny.add(alphabetSymbols.get(c));
                }
            }
        }
        return alphabetSymbols.get(symbol);
    }

    public static boolean isLegalCharMatchesAny(char c) {
        return c != 10 && c != 13 && c != 133;
    }

    public static List<Symbol> getAlphabetSymbols() {
        return alphabetSymbols;
    }

    public static List<Symbol> getAlphabetSymbolsMatchesAny() {
        return alphabetSymbolsMatchesAny;
    }

    public static Set<Symbol> getOppositeSymbols(Set<Symbol> excludedSymbols) {
        Set<Symbol> oppositeSymbols = new HashSet<>(getAlphabetSymbols());
        oppositeSymbols.removeAll(excludedSymbols);
        return oppositeSymbols;
    }

    public static char getChar(Symbol symbol) {
        Assert.assertTrue(symbol.getValue().length() == 1);
        return symbol.getValue().charAt(0);
    }
}
