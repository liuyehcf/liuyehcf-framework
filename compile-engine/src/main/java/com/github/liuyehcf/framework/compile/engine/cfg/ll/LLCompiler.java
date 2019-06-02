package com.github.liuyehcf.framework.compile.engine.cfg.ll;

import com.github.liuyehcf.framework.compile.engine.cfg.CfgCompiler;

/**
 * LL文法编译器接口
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public interface LLCompiler<T> extends CfgCompiler<T> {

    /**
     * 获取Select的JSON串
     *
     * @return Select集合的JSON串
     */
    String getSelectJSONString();
}
