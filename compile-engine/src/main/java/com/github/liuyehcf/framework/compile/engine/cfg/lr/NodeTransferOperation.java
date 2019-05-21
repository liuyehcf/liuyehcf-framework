package com.github.liuyehcf.framework.compile.engine.cfg.lr;

import com.github.liuyehcf.framework.compile.engine.grammar.definition.PrimaryProduction;

import java.io.Serializable;
import java.util.Objects;

/**
 * 用于封装状态转移
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
class NodeTransferOperation implements Serializable {
    /**
     * 下一跳项目集闭包id
     */
    private final int nextClosureId;

    /**
     * 规约操作时对应的产生式，仅用于规约操作
     */
    private final PrimaryProduction primaryProduction;

    /**
     * 对应的操作
     */
    private final NodeTransferOperator operator;

    NodeTransferOperation(int nextClosureId, PrimaryProduction primaryProduction, NodeTransferOperator operator) {
        this.nextClosureId = nextClosureId;
        this.primaryProduction = primaryProduction;
        this.operator = operator;
    }

    int getNextClosureId() {
        return nextClosureId;
    }

    PrimaryProduction getPrimaryProduction() {
        return primaryProduction;
    }

    NodeTransferOperator getOperator() {
        return operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NodeTransferOperation nodeTransferOperation = (NodeTransferOperation) o;
        return nextClosureId == nodeTransferOperation.nextClosureId &&
                Objects.equals(primaryProduction, nodeTransferOperation.primaryProduction) &&
                operator == nodeTransferOperation.operator;
    }

    @Override
    public int hashCode() {

        return Objects.hash(nextClosureId, primaryProduction, operator);
    }
}
