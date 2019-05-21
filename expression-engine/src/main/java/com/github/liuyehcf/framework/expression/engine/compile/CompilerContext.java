package com.github.liuyehcf.framework.expression.engine.compile;

import com.github.liuyehcf.framework.compile.engine.cfg.lr.Context;
import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.expression.engine.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.expression.engine.core.ExpressionCode;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.ByteCode;

/**
 * 编译时上下文
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class CompilerContext extends Context {

    /**
     * 表达式元数据
     */
    private final ExpressionCode expressionCode;


    public CompilerContext(Context context, ExpressionCode expressionCode) {
        super(context.getRawPrimaryProduction(), context.getStack(), context.getLeftNode());
        this.expressionCode = expressionCode;
    }

    /**
     * 设置属性
     *
     * @param stackOffset 偏移量，相对于语法树栈
     * @param attrName    属性名
     * @param value       属性值
     */
    public void setAttr(int stackOffset, AttrName attrName, Object value) {
        Assert.assertNotNull(attrName);
        getStack().get(stackOffset).put(attrName.name(), value);
    }

    /**
     * 为产生之左部的语法树节点设置属性
     *
     * @param attrName 属性名
     * @param value    属性值
     */
    public void setAttrToLeftNode(AttrName attrName, Object value) {
        Assert.assertNotNull(attrName);
        getLeftNode().put(attrName.name(), value);
    }

    /**
     * 获取属性值
     *
     * @param stackOffset 偏移量，相对于语法树栈
     * @param attrName    属性名
     * @param <T>         属性值类型
     * @return 属性值
     */
    public <T> T getAttr(int stackOffset, AttrName attrName) {
        Assert.assertNotNull(attrName);
        return getStack().get(stackOffset).get(attrName.name());
    }

    /**
     * 获取语法树节点的词法值，该值由词法分析器提供
     *
     * @param stackOffset 偏移量，相对于语法树栈
     * @return 词法值
     */
    public String getValue(int stackOffset) {
        return getStack().get(stackOffset).getValue();
    }

    /**
     * 为当前方法增加指令
     *
     * @param byteCode 指令
     */
    public void addByteCode(ByteCode byteCode) {
        expressionCode.addByteCode(byteCode);
    }

    /**
     * 获取当前方法的指令偏移量
     *
     * @return 指令偏移量
     */
    public int getByteCodeSize() {
        return expressionCode.getByteCodes().size();
    }

    public void addProperty(String propertyName) {
        expressionCode.addPropertyName(propertyName);
    }
}
