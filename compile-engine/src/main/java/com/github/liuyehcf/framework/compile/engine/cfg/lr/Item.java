package com.github.liuyehcf.framework.compile.engine.cfg.lr;

import com.github.liuyehcf.framework.compile.engine.grammar.definition.PrimaryProduction;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;
import com.github.liuyehcf.framework.compile.engine.utils.Assert;

import java.io.Serializable;
import java.util.*;

/**
 * 项目集
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
class Item implements Comparable<Item>, Serializable {

    /**
     * 产生式
     */
    private final PrimaryProduction primaryProduction;

    /**
     * 展望符
     */
    private final Set<Symbol> lookAHeads;

    Item(PrimaryProduction primaryProduction, Set<Symbol> lookAHeads) {
        this.primaryProduction = primaryProduction;
        if (lookAHeads == null) {
            this.lookAHeads = null;
        } else {
            this.lookAHeads = Collections.unmodifiableSet(new TreeSet<>(lookAHeads));
        }
    }

    PrimaryProduction getPrimaryProduction() {
        return primaryProduction;
    }

    Set<Symbol> getLookAHeads() {
        return lookAHeads;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return Objects.equals(primaryProduction, item.primaryProduction) &&
                Objects.equals(lookAHeads, item.lookAHeads);
    }

    @Override
    public int hashCode() {

        return Objects.hash(primaryProduction, lookAHeads);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(primaryProduction);

        if (lookAHeads != null) {
            Assert.assertFalse(lookAHeads.isEmpty());
            sb.append(", ");
            sb.append(lookAHeads);
        }

        return sb.toString();
    }

    @Override
    public int compareTo(Item o) {
        int res = this.primaryProduction.compareTo(o.primaryProduction);
        if (res == 0) {
            Iterator<Symbol> thisIterator = this.lookAHeads.iterator();
            Iterator<Symbol> oIterator = o.lookAHeads.iterator();

            while (thisIterator.hasNext()
                    && oIterator.hasNext()) {
                res = thisIterator.next().compareTo(oIterator.next());
                if (res != 0) {
                    return res;
                }
            }

            if (thisIterator.hasNext()) {
                return 1;
            } else if (oIterator.hasNext()) {
                return -1;
            } else {
                return 0;
            }
        }
        return res;
    }
}
