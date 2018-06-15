package org.liuyehcf.compile.engine.hua.semantic;

import org.liuyehcf.compile.engine.hua.compiler.HuaCompiler;
import org.liuyehcf.compile.engine.hua.definition.AttrName;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertNull;

/**
 * @author chenlu
 * @date 2018/6/15
 */
public class AssignAllAttr extends AbstractSemanticAction {
    private final int fromStackOffset;

    private final int toStackOffset;

    public AssignAllAttr(int fromStackOffset, int toStackOffset) {
        this.fromStackOffset = fromStackOffset;
        this.toStackOffset = toStackOffset;
    }

    @Override
    public void onAction(HuaCompiler.HuaContext context) {
        for (AttrName attrName : AttrName.values()) {
            Object value = context.getStack().get(fromStackOffset).get(attrName.name());
            if (value == null) {
                continue;
            }
            Object origin = context.getStack().get(toStackOffset).putIfAbsent(attrName.name(), value);
            assertNull(origin);
        }
    }
}
