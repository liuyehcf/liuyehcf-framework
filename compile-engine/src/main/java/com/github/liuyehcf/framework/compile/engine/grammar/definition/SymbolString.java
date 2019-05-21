package com.github.liuyehcf.framework.compile.engine.grammar.definition;

import com.github.liuyehcf.framework.compile.engine.utils.ListUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 文法符号串
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class SymbolString implements Comparable<SymbolString>, Serializable {
    /**
     * 仅包含ε的文法符号串
     */
    public static final SymbolString EPSILON_RAW = create(Symbol.EPSILON);

    /**
     * "ε ·"
     */
    public static final SymbolString EPSILON_END = create(ListUtils.of(Symbol.EPSILON), 1);

    /**
     * 文法符号串
     */
    private final List<Symbol> symbols;

    /**
     * 状态索引（仅用于LR文法）
     */
    private final int indexOfDot;

    private SymbolString(List<Symbol> symbols, int indexOfDot) {
        this.symbols = Collections.unmodifiableList(symbols);
        this.indexOfDot = indexOfDot;
    }

    public static SymbolString create(Symbol... symbols) {
        return new SymbolString(ListUtils.of(symbols), -1);
    }

    public static SymbolString create(List<Symbol> symbols, int indexOfDot) {
        return new SymbolString(symbols, indexOfDot);
    }

    public static SymbolString create(List<Symbol> symbols) {
        return new SymbolString(symbols, -1);
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }

    public SymbolString getSubSymbolString(int start) {
        if (start < 0 || start >= symbols.size()) {
            throw new IllegalArgumentException();
        }
        return SymbolString.create(symbols.subList(start, symbols.size()));
    }

    public int getIndexOfDot() {
        return indexOfDot;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < symbols.size(); i++) {
            if (i != 0) {
                sb.append(' ');
            }

            if (indexOfDot == i) {
                sb.append("· ");
            }
            sb.append(symbols.get(i));
        }

        if (indexOfDot == symbols.size()) {
            sb.append(" ·");
        }

        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 0;

        hash += indexOfDot;

        for (Symbol symbol : symbols) {
            hash += symbol.hashCode();
        }

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SymbolString) {
            SymbolString that = (SymbolString) obj;
            if (that.indexOfDot == this.indexOfDot
                    && that.symbols.size() == this.symbols.size()) {
                for (int i = 0; i < this.symbols.size(); i++) {
                    if (!that.symbols.get(i).equals(this.symbols.get(i))) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public int compareTo(SymbolString o) {
        int res;
        int i = 0;
        while (i < this.symbols.size()
                && i < o.symbols.size()) {
            res = this.symbols.get(i).compareTo(o.symbols.get(i));
            if (res != 0) {
                return res;
            }
            i++;
        }
        if (i < this.symbols.size()) {
            return 1;
        } else if (i < o.symbols.size()) {
            return -1;
        } else {
            return this.indexOfDot - o.indexOfDot;
        }
    }
}
