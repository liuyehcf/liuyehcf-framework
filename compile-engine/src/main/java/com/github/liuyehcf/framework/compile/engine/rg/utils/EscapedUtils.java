package com.github.liuyehcf.framework.compile.engine.rg.utils;

import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;
import com.github.liuyehcf.framework.compile.engine.utils.ListUtils;

import java.util.*;

/**
 * 转义符号工具类
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public abstract class EscapedUtils {
    private static List<Symbol> escaped_any;
    private static List<Symbol> escaped_or;
    private static List<Symbol> escaped_star;
    private static List<Symbol> escaped_add;
    private static List<Symbol> escaped_opposite;
    private static List<Symbol> escaped_leftMiddleParenthesis;
    private static List<Symbol> escaped_rightMiddleParenthesis;
    private static List<Symbol> escaped_leftSmallParenthesis;
    private static List<Symbol> escaped_rightSmallParenthesis;
    private static List<Symbol> escaped_d;
    private static List<Symbol> escaped_D;
    private static List<Symbol> escaped_w;
    private static List<Symbol> escaped_W;
    private static List<Symbol> escaped_s;
    private static List<Symbol> escaped_S;

    static {
        initializeEscapedAny();
        initializeEscapedOr();
        initializeEscapedStar();
        initializeEscapedAdd();
        initializeEscapedOpposite();
        initializeEscapedLeftMiddleParenthesis();
        initializeEscapedRightMiddleParenthesis();
        initializeEscapedLeftSmallParenthesis();
        initializeEscapedRightSmallParenthesis();
        initializeEscapedSmallD();
        initializeEscapedBigD();
        initializeEscapedSmallW();
        initializeEscapedBigW();
        initializeEscapedSmallS();
        initializeEscapedBigS();
    }

    public static List<Symbol> getSymbolsOfEscapedChar(char c) {
        switch (c) {
            case '.':
                return escaped_any;
            case '|':
                return escaped_or;
            case '*':
                return escaped_star;
            case '+':
                return escaped_add;
            case '[':
                return escaped_leftMiddleParenthesis;
            case ']':
                return escaped_rightMiddleParenthesis;
            case '(':
                return escaped_leftSmallParenthesis;
            case ')':
                return escaped_rightSmallParenthesis;
            case 'd':
                return escaped_d;
            case 'D':
                return escaped_D;
            case 'w':
                return escaped_w;
            case 'W':
                return escaped_W;
            case 's':
                return escaped_s;
            case 'S':
                return escaped_S;
            default:
                throw new RuntimeException();
        }
    }

    public static List<Symbol> getSymbolsOfEscapedCharInMiddleParenthesis(char c) {
        switch (c) {
            case '^':
                return escaped_opposite;
            case '[':
                return escaped_leftMiddleParenthesis;
            case ']':
                return escaped_rightMiddleParenthesis;
            case 'd':
                return escaped_d;
            case 'D':
                return escaped_D;
            case 'w':
                return escaped_w;
            case 'W':
                return escaped_W;
            case 's':
                return escaped_s;
            case 'S':
                return escaped_S;
            case '-':
                return ListUtils.of(SymbolUtils.TO);
            default:
                throw new RuntimeException();
        }
    }

    private static void initializeEscapedAny() {
        escaped_any = Collections.unmodifiableList(
                Collections.singletonList(
                        SymbolUtils.getAlphabetSymbolWithChar('.')));
    }

    private static void initializeEscapedOr() {
        escaped_or = Collections.unmodifiableList(
                Collections.singletonList(
                        SymbolUtils.getAlphabetSymbolWithChar('|')));
    }

    private static void initializeEscapedStar() {
        escaped_star = Collections.unmodifiableList(
                Collections.singletonList(
                        SymbolUtils.getAlphabetSymbolWithChar('*')));
    }

    private static void initializeEscapedAdd() {
        escaped_add = Collections.unmodifiableList(
                Collections.singletonList(
                        SymbolUtils.getAlphabetSymbolWithChar('+')));
    }

    private static void initializeEscapedOpposite() {
        escaped_opposite = Collections.unmodifiableList(
                Collections.singletonList(
                        SymbolUtils.getAlphabetSymbolWithChar('^')));
    }

    private static void initializeEscapedLeftMiddleParenthesis() {
        escaped_leftMiddleParenthesis = Collections.unmodifiableList(
                Collections.singletonList(
                        SymbolUtils.getAlphabetSymbolWithChar('[')));
    }

    private static void initializeEscapedRightMiddleParenthesis() {
        escaped_rightMiddleParenthesis = Collections.unmodifiableList(
                Collections.singletonList(SymbolUtils.getAlphabetSymbolWithChar(']')));
    }

    private static void initializeEscapedLeftSmallParenthesis() {
        escaped_leftSmallParenthesis = Collections.unmodifiableList(
                Collections.singletonList(
                        SymbolUtils.getAlphabetSymbolWithChar('(')));
    }

    private static void initializeEscapedRightSmallParenthesis() {
        escaped_rightSmallParenthesis = Collections.unmodifiableList(
                Collections.singletonList(
                        SymbolUtils.getAlphabetSymbolWithChar(')')));
    }

    private static void initializeEscapedSmallD() {
        List<Symbol> symbols = new ArrayList<>();
        for (char c = '0'; c <= '9'; c++) {
            symbols.add(SymbolUtils.getAlphabetSymbolWithChar(c));
        }
        escaped_d = Collections.unmodifiableList(symbols);
    }

    private static void initializeEscapedBigD() {
        Set<Symbol> symbols = new HashSet<>(SymbolUtils.getAlphabetSymbols());
        symbols.removeAll(escaped_d);
        escaped_D = Collections.unmodifiableList(new ArrayList<>(symbols));
    }

    private static void initializeEscapedSmallW() {
        List<Symbol> symbols = new ArrayList<>();
        for (char c = '0'; c <= '9'; c++) {
            symbols.add(SymbolUtils.getAlphabetSymbolWithChar(c));
        }
        for (char c = 'a'; c <= 'z'; c++) {
            symbols.add(SymbolUtils.getAlphabetSymbolWithChar(c));
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            symbols.add(SymbolUtils.getAlphabetSymbolWithChar(c));
        }
        symbols.add(SymbolUtils.getAlphabetSymbolWithChar('_'));
        escaped_w = Collections.unmodifiableList(symbols);
    }

    private static void initializeEscapedBigW() {
        Set<Symbol> symbols = new HashSet<>(SymbolUtils.getAlphabetSymbols());
        symbols.removeAll(escaped_w);
        escaped_W = Collections.unmodifiableList(new ArrayList<>(symbols));
    }

    private static void initializeEscapedSmallS() {
        List<Symbol> symbols = new ArrayList<>();
        symbols.add(SymbolUtils.getAlphabetSymbolWithChar((char) 9));
        symbols.add(SymbolUtils.getAlphabetSymbolWithChar((char) 10));
        symbols.add(SymbolUtils.getAlphabetSymbolWithChar((char) 11));
        symbols.add(SymbolUtils.getAlphabetSymbolWithChar((char) 12));
        symbols.add(SymbolUtils.getAlphabetSymbolWithChar((char) 13));
        symbols.add(SymbolUtils.getAlphabetSymbolWithChar((char) 32));
        escaped_s = Collections.unmodifiableList(new ArrayList<>(symbols));
    }

    private static void initializeEscapedBigS() {
        Set<Symbol> symbols = new HashSet<>(SymbolUtils.getAlphabetSymbols());
        symbols.removeAll(escaped_s);
        escaped_S = Collections.unmodifiableList(new ArrayList<>(symbols));
    }
}
