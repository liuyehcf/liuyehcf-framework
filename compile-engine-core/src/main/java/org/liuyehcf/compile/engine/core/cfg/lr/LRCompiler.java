package org.liuyehcf.compile.engine.core.cfg.lr;

import org.liuyehcf.compile.engine.core.cfg.CfgCompiler;

/**
 * LR文法编译器接口
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public interface LRCompiler<T> extends CfgCompiler<T> {
    /**
     * 获取Closure的JSON串
     *
     * @return Closure的JSON串
     */
    String getClosureJSONString();

    /**
     * 获取状态转移表的JSON串
     *
     * @return 状态转移表的JSON串
     */
    String getClosureTransferTableJSONString();
}
