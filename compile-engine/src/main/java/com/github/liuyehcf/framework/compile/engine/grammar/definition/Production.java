package com.github.liuyehcf.framework.compile.engine.grammar.definition;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.compile.engine.utils.ListUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 具有相同左部的产生式集合
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class Production implements Comparable<Production>, Serializable {

    private static final String OR = "|";

    /**
     * 产生式左部
     */
    private final Symbol left;

    /**
     * 子产生式集合
     */
    private final List<PrimaryProduction> primaryProductions;

    private Production(List<PrimaryProduction> primaryProductions) {
        this.primaryProductions = Collections.unmodifiableList(ListUtils.sort(primaryProductions));

        Symbol left = null;
        for (PrimaryProduction pp : primaryProductions) {
            if (left == null) {
                left = pp.getLeft();
            } else {
                Assert.assertTrue(left.equals(pp.getLeft()));
            }
        }
        this.left = left;
    }

    public static Production create(PrimaryProduction... primaryProductions) {
        return create(ListUtils.of(primaryProductions));
    }

    public static Production create(List<PrimaryProduction> primaryProductions) {
        return new Production(primaryProductions);
    }

    public Symbol getLeft() {
        return left;
    }

    public List<PrimaryProduction> getPrimaryProductions() {
        return primaryProductions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append('\"');
        sb.append(left)
                .append(" → ");

        for (PrimaryProduction pp : primaryProductions) {
            sb.append(pp.getRight())
                    .append(' ')
                    .append(OR)
                    .append(' ');
        }

        Assert.assertFalse(primaryProductions.isEmpty());
        sb.setLength(sb.length() - 3);

        sb.append('\"');

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Production that = (Production) o;
        return Objects.equals(left, that.left) &&
                Objects.equals(primaryProductions, that.primaryProductions);
    }

    @Override
    public int hashCode() {

        return Objects.hash(left, primaryProductions);
    }

    @Override
    public int compareTo(Production o) {
        int res = this.left.compareTo(o.left);
        if (res == 0) {
            int i = 0;
            while (i < this.primaryProductions.size()
                    && i < o.primaryProductions.size()) {
                res = this.primaryProductions.get(i).compareTo(o.primaryProductions.get(i));
                if (res != 0) {
                    return res;
                }
                i++;
            }
            if (i < this.primaryProductions.size()) {
                return 1;
            } else if (i < o.primaryProductions.size()) {
                return -1;
            } else {
                return 0;
            }
        }
        return res;
    }
}
