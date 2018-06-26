package org.liuyehcf.compile.engine.hua.core;

import org.liuyehcf.compile.engine.core.cfg.lr.Context;
import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;
import org.liuyehcf.compile.engine.hua.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.definition.model.Type;

import java.util.List;

/**
 * Hua上下文
 *
 * @author hechenfeng
 * @date 2018/6/19
 */
public class HuaContext extends Context {

    /**
     * hua引擎
     */
    private final HuaCompiler.HuaEngine huaEngine;

    public HuaContext(Context context, HuaCompiler.HuaEngine huaEngine) {
        super(context.getRawPrimaryProduction(), context.getStack(), context.getLeftNode());
        this.huaEngine = huaEngine;
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

    private void addConstant(String constant) {
        huaEngine.getConstantPool().addConstant(constant);
    }

    public int getConstantOffset(String constant) {
        return huaEngine.getConstantPool().getConstantOffset(constant);
    }

    /**
     * 为当前方法设置方法名
     *
     * @param methodName 方法名
     */
    public void setMethodNameOfCurrentMethod(String methodName) {
        huaEngine.getMethodInfoTable().getCurMethodInfo().setMethodName(methodName);
    }

    /**
     * 为当前方法设置参数类型列表
     *
     * @param paramTypeList 参数类型列表
     */
    public void setParamTypeListOfCurrentMethod(List<Type> paramTypeList) {
        huaEngine.getMethodInfoTable().getCurMethodInfo().setParamTypeList(paramTypeList);
    }

    /**
     * 获取当前方法的返回类型
     *
     * @return 返回类型
     */
    public Type getResultTypeOfCurrentMethod() {
        return huaEngine.getMethodInfoTable().getCurMethodInfo().getResultType();
    }

    /**
     * 为当前方法设置返回类型
     *
     * @param resultType 返回类型
     */
    public void setResultTypeOfCurrentMethod(Type resultType) {
        huaEngine.getMethodInfoTable().getCurMethodInfo().setResultType(resultType);
    }

    /**
     * 为当前方法增加指令
     *
     * @param code 指令
     */
    public void addByteCodeToCurrentMethod(ByteCode code) {
        huaEngine.getMethodInfoTable().getCurMethodInfo().addByteCode(code);
    }

    /**
     * 获取当前方法的指令偏移量
     *
     * @return 指令偏移量
     */
    public int getByteCodeSizeOfCurrentMethod() {
        return huaEngine.getMethodInfoTable().getCurMethodInfo().getByteCodes().size();
    }

    /**
     * 获取当前方法的所有指令
     *
     * @return 指令序列
     */
    public List<ByteCode> getByteCodesOfOfCurrentMethod() {
        return huaEngine.getMethodInfoTable().getCurMethodInfo().getByteCodes();
    }

    /**
     * 是否包含给定的方法
     *
     * @param methodSignature 方法签名
     * @return 是否包含
     */
    public boolean containsMethod(MethodSignature methodSignature) {
        return huaEngine.getMethodInfoTable().containsMethod(methodSignature);
    }

    /**
     * 根据方法签名查找方法信息
     *
     * @param methodSignature 方法签名
     * @return 方法信息
     */
    public MethodInfo getMethodByMethodSignature(MethodSignature methodSignature) {
        return huaEngine.getMethodInfoTable().getMethodByMethodSignature(methodSignature);
    }

    /**
     * 进入方法
     */
    public void enterMethod() {
        huaEngine.getMethodInfoTable().enterMethod();
    }

    /**
     * 完成方法签名的扫描
     */
    public void finishMethodDeclarator() {
        MethodSignature methodSignature = huaEngine.getMethodInfoTable().finishMethodDeclarator();
        addConstant(methodSignature.getSignature());
    }

    /**
     * 退出方法
     */
    public void exitMethod() {
        huaEngine.getMethodInfoTable().exitMethod();
    }

    /**
     * 进入命名空间
     */
    public void enterNamespace() {
        huaEngine.getVariableSymbolTable().enterNamespace();
        huaEngine.getMethodInfoTable().getCurMethodInfo().enterNamespace();
    }

    /**
     * 退出命名空间
     */
    public void exitNamespace() {
        huaEngine.getVariableSymbolTable().exitNamespace();
        huaEngine.getMethodInfoTable().getCurMethodInfo().exitNamespace();
    }

    /**
     * 创建一个符号
     *
     * @param name 标志符名称
     * @param type 标志符类型
     * @return 新创建的符号
     */
    public VariableSymbol createVariableSymbol(String name, Type type) {
        int order = huaEngine.getMethodInfoTable().getCurMethodInfo().getOrder();
        int offset = huaEngine.getMethodInfoTable().getCurMethodInfo().getOffset();

        VariableSymbol variableSymbol = huaEngine.getVariableSymbolTable().createVariableSymbol(order, offset, name, type);

        huaEngine.getMethodInfoTable().getCurMethodInfo().increaseOffset(variableSymbol.getType().getTypeWidth());
        huaEngine.getMethodInfoTable().getCurMethodInfo().increaseOrder();
        return variableSymbol;
    }

    /**
     * 根据标志符名称获取符号
     *
     * @param identifierName 标志符名称
     * @return 符号
     */
    public VariableSymbol getVariableSymbolByName(String identifierName) {
        return huaEngine.getVariableSymbolTable().getVariableSymbolByName(identifierName);
    }
}
