package org.liuyehcf.compile.engine.hua.compile;

import org.liuyehcf.compile.engine.core.cfg.lr.Context;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.core.*;
import org.liuyehcf.compile.engine.hua.core.bytecode.ByteCode;

import java.util.List;

/**
 * Hua上下文
 *
 * @author hechenfeng
 * @date 2018/6/19
 */
public class HuaContext extends Context {

    /**
     * 常量池
     */
    private final ConstantPool constantPool;

    /**
     * 方法表
     */
    private final MethodInfoTable methodInfoTable;

    public HuaContext(Context context, ConstantPool constantPool, MethodInfoTable methodInfoTable) {
        super(context.getRawPrimaryProduction(), context.getStack(), context.getLeftNode());
        this.constantPool = constantPool;
        this.methodInfoTable = methodInfoTable;
    }

    /**
     * 设置属性
     *
     * @param stackOffset 偏移量，相对于语法树栈
     * @param attrName    属性名
     * @param value       属性值
     */
    public void setAttr(int stackOffset, AttrName attrName, Object value) {
        if (attrName == null) {
            throw new NullPointerException();
        }
        getStack().get(stackOffset).put(attrName.name(), value);
    }

    /**
     * 为产生之左部的语法树节点设置属性
     *
     * @param attrName 属性名
     * @param value    属性值
     */
    public void setAttrToLeftNode(AttrName attrName, Object value) {
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
     * 添加常量
     *
     * @param constant 常量
     * @return 偏移量
     */
    public int addConstant(String constant) {
        return constantPool.addConstant(constant);
    }

    /**
     * 获取常量的偏移量
     *
     * @param constant 常量
     * @return 偏移量
     */
    public int getConstantOffset(String constant) {
        return constantPool.getConstantOffset(constant);
    }

    /**
     * 为当前方法设置方法名
     *
     * @param methodName 方法名
     */
    public void setMethodNameOfCurrentMethod(String methodName) {
        methodInfoTable.getCurMethodInfo().setMethodName(methodName);
    }

    /**
     * 为当前方法设置参数类型列表
     *
     * @param paramTypeList 参数类型列表
     */
    public void setParamTypeListOfCurrentMethod(List<Type> paramTypeList) {
        methodInfoTable.getCurMethodInfo().setParamTypeList(paramTypeList);
    }

    /**
     * 获取当前方法的返回类型
     *
     * @return 返回类型
     */
    public Type getResultTypeOfCurrentMethod() {
        return methodInfoTable.getCurMethodInfo().getResultType();
    }

    /**
     * 为当前方法设置返回类型
     *
     * @param resultType 返回类型
     */
    public void setResultTypeOfCurrentMethod(Type resultType) {
        methodInfoTable.getCurMethodInfo().setResultType(resultType);
    }

    /**
     * 为当前方法增加指令
     *
     * @param code 指令
     */
    public void addByteCodeToCurrentMethod(ByteCode code) {
        methodInfoTable.getCurMethodInfo().addByteCode(code);
    }

    /**
     * 获取当前方法的指令偏移量
     *
     * @return 指令偏移量
     */
    public int getByteCodeSizeOfCurrentMethod() {
        return methodInfoTable.getCurMethodInfo().getByteCodes().size();
    }

    /**
     * 获取当前方法的所有指令
     *
     * @return 指令序列
     */
    public List<ByteCode> getByteCodesOfOfCurrentMethod() {
        return methodInfoTable.getCurMethodInfo().getByteCodes();
    }

    /**
     * 是否包含给定的方法
     *
     * @param methodSignature 方法签名
     * @return 是否包含
     */
    public boolean containsMethod(MethodSignature methodSignature) {
        return methodInfoTable.containsMethod(methodSignature);
    }

    /**
     * 根据方法签名查找方法信息
     *
     * @param methodSignature 方法签名
     * @return 方法信息
     */
    public MethodInfo getMethodByMethodSignature(MethodSignature methodSignature) {
        return methodInfoTable.getMethodByMethodSignature(methodSignature);
    }

    /**
     * 进入方法
     */
    public void enterMethod() {
        methodInfoTable.enterMethod();
    }

    /**
     * 完成方法签名的扫描
     */
    public void addMethodSignatureToConstantPool() {
        MethodSignature methodSignature = methodInfoTable.finishMethodDeclarator();
        addConstant(methodSignature.getSignature());
    }

    /**
     * 退出方法
     */
    public void exitMethod() {
        methodInfoTable.exitMethod();
    }

    /**
     * 进入命名空间
     */
    public void enterNamespace() {
        methodInfoTable.getCurMethodInfo().enterNamespace();
    }

    /**
     * 退出命名空间
     */
    public void exitNamespace() {
        methodInfoTable.getCurMethodInfo().exitNamespace();
    }

    /**
     * 创建一个符号
     *
     * @param name 标志符名称
     * @param type 标志符类型
     * @return 新创建的符号
     */
    public VariableSymbol createVariableSymbol(String name, Type type) {
        int order = methodInfoTable.getCurMethodInfo().getOrder();

        return methodInfoTable.getCurMethodInfo().createVariableSymbol(order, name, type);
    }

    /**
     * 根据标志符名称获取符号
     *
     * @param identifierName 标志符名称
     * @return 符号
     */
    public VariableSymbol getVariableSymbolByName(String identifierName) {
        return methodInfoTable.getCurMethodInfo().getVariableSymbolByName(identifierName);
    }
}
