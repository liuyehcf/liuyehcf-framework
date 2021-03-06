package com.github.liuyehcf.framework.compile.engine.rg.dfa;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Dfa状态描述符
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class DfaStateDescription {

    /**
     * hash值
     */
    private final int hash;

    /**
     * DfaState包含的所有NfaState的id集合
     */
    private final Set<Integer> nfaStateIds;

    DfaStateDescription(Set<Integer> nfaStateIds) {
        Assert.assertNotNull(nfaStateIds);

        this.nfaStateIds = Collections.unmodifiableSet(new HashSet<>(nfaStateIds));

        int shiftOffset = 0;

        int sum = 0;

        for (int id : nfaStateIds) {
            sum += (id << (shiftOffset++));
        }

        this.hash = sum;
    }

    @Override
    public String toString() {
        return nfaStateIds.toString();
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DfaStateDescription) {
            DfaStateDescription that = (DfaStateDescription) obj;
            return that.hash == this.hash
                    && that.nfaStateIds.equals(this.nfaStateIds);

        }
        return false;
    }
}
