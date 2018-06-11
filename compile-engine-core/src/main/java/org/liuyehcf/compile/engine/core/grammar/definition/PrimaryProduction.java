package org.liuyehcf.compile.engine.core.grammar.definition;

import org.liuyehcf.compile.engine.core.utils.ListUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>文法产生式 只研究二型文法（包括三型文法）</p>
 * <p>等式左边是非终结符，等式右边是文法符号串</p>
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class PrimaryProduction implements Comparable<PrimaryProduction> {
    /**
     * 产生式左侧非终结符
     */
    private final Symbol left;

    /**
     * 产生式右部的文法符号串
     */
    private final SymbolString right;

    /**
     * 当前产生式的规约操作（针对LR文法）
     */
    private final List<SemanticAction> semanticActions;

    public PrimaryProduction(Symbol left, SymbolString right,
                             List<SemanticAction> semanticActions) {
        this.left = left;
        this.right = right;
        if (semanticActions == null) {
            this.semanticActions = Collections.unmodifiableList(Collections.emptyList());
        } else {
            this.semanticActions = Collections.unmodifiableList(semanticActions);
        }
    }

    public static PrimaryProduction create(Symbol left, SymbolString right, SemanticAction semanticAction,
                                           SemanticAction... semanticActions) {
        return create(left, right, ListUtils.of(semanticAction, ListUtils.of(semanticActions)));
    }

    public static PrimaryProduction create(Symbol left, SymbolString right,
                                           List<SemanticAction> semanticActions) {
        return new PrimaryProduction(left, right, semanticActions);
    }

    public Symbol getLeft() {
        return left;
    }

    public SymbolString getRight() {
        return right;
    }

    public List<SemanticAction> getSemanticActions() {
        return semanticActions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrimaryProduction that = (PrimaryProduction) o;
        return Objects.equals(left, that.left) &&
                Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {

        return Objects.hash(left, right);
    }

    @Override
    public String toString() {
        return left + " → " + right;
    }

    @Override
    public int compareTo(PrimaryProduction o) {
        int res = this.left.compareTo(o.left);
        if (res == 0) {
            return this.right.compareTo(o.right);
        }
        return res;
    }
}
