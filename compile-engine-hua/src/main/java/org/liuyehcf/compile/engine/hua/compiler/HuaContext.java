package org.liuyehcf.compile.engine.hua.compiler;

import org.liuyehcf.compile.engine.core.cfg.lr.Context;

/**
 * Hua上下文
 *
 * @author hechenfeng
 * @date 2018/6/19
 */
public class HuaContext extends Context {
    private final HuaCompiler.HuaEngine huaEngine;

    public HuaContext(Context context, HuaCompiler.HuaEngine huaEngine) {
        super(context.getRawPrimaryProduction(), context.getStack(), context.getLeftNode());
        this.huaEngine = huaEngine;
    }

    public HuaCompiler.HuaEngine getHuaEngine() {
        return huaEngine;
    }
}
