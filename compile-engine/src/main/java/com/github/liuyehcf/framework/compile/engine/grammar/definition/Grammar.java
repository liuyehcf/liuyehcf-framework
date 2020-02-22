package com.github.liuyehcf.framework.compile.engine.grammar.definition;

import com.github.liuyehcf.framework.compile.engine.utils.ListUtils;

import java.io.Serializable;
import java.util.*;

import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertFalse;

/**
 * 文法定义
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class Grammar implements Serializable {

    /**
     * 文法开始符号
     */
    private final Symbol start;

    /**
     * 文法包含的所有产生式
     */
    private final List<Production> productions;

    /**
     * 终结符集合
     */
    private final Set<Symbol> terminators;

    /**
     * 非终结符集合
     */
    private final Set<Symbol> nonTerminators;

    private Grammar(Symbol start, List<Production> productions) {
        this.start = start;
        this.productions = Collections.unmodifiableList(ListUtils.sort(productions));

        Set<Symbol> terminators = new TreeSet<>();
        Set<Symbol> nonTerminators = new TreeSet<>();

        for (Production p : productions) {
            for (PrimaryProduction pp : p.getPrimaryProductions()) {
                assertFalse(pp.getLeft().isTerminator());
                nonTerminators.add(pp.getLeft());

                for (Symbol symbol : pp.getRight().getSymbols()) {
                    if (symbol.isTerminator()) {
                        terminators.add(symbol);
                    } else {
                        nonTerminators.add(symbol);
                    }
                }
            }
        }

        this.terminators = Collections.unmodifiableSet(terminators);
        this.nonTerminators = Collections.unmodifiableSet(nonTerminators);
    }

    public static Grammar create(Symbol start, Production... productions) {
        return new Grammar(start, ListUtils.of(productions));
    }

    @SuppressWarnings("unchecked")
    public static Grammar create(Symbol start, Object... objects) {
        List<Production> productions = new ArrayList<>();
        for (Object obj : objects) {
            if (obj instanceof Production) {
                productions.add((Production) obj);
            } else if (obj instanceof Production[]) {
                productions.addAll(Arrays.asList((Production[]) obj));
            } else if (obj instanceof List) {
                productions.addAll((List<Production>) obj);
            }
        }
        return new Grammar(start, ListUtils.of(productions));
    }

    public static Grammar create(Symbol start, List<Production> productions) {
        return new Grammar(start, productions);
    }

    public Symbol getStart() {
        return start;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public Set<Symbol> getTerminators() {
        return terminators;
    }

    public Set<Symbol> getNonTerminators() {
        return nonTerminators;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append('{')
                .append("\"productions\":")
                .append('[');

        for (Production p : productions) {
            sb.append(p)
                    .append(",");
        }

        assertFalse(productions.isEmpty());
        sb.setLength(sb.length() - 1);

        sb.append(']')
                .append('}');

        return sb.toString();
    }
}
