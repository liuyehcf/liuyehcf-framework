package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;

/**
 * @author chenlu
 * @date 2018/6/11
 */
public class CombineAttr extends AbstractSemanticAction {

    private final int mainStackOffset;

    private final int subStackOffset;

    private final String attrName;

    public CombineAttr(int mainStackOffset, int subStackOffset, String attrName) {
        this.mainStackOffset = mainStackOffset;
        this.subStackOffset = subStackOffset;
        this.attrName = attrName;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        String main = context.getStack().get(mainStackOffset).get(attrName);
        String sub = context.getStack().get(subStackOffset).get(attrName);

        main = main + sub;

        context.getStack().get(mainStackOffset).put(attrName, main);
    }
}
