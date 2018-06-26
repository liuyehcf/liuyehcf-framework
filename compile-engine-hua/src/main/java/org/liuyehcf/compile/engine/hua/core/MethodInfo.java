package org.liuyehcf.compile.engine.hua.core;

import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.core.bytecode.ByteCode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.*;

/**
 * 方法信息
 *
 * @author hechenfeng
 * @date 2018/6/6
 */
public class MethodInfo {

    /**
     * 符号表
     */
    private final VariableSymbolTable variableSymbolTable = new VariableSymbolTable();
    /**
     * 字节码
     */
    private List<ByteCode> byteCodes = new ArrayList<>();
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 返回类型
     */
    private Type resultType;
    /**
     * 参数类型
     */
    private List<Type> paramTypeList;
    /**
     * 最大偏移量（用于分配栈内存）
     */
    private int maxOffset;
    /**
     * 符号序号栈
     */
    private LinkedList<Integer> orderStack = new LinkedList<>();
    /**
     * 符号偏移量栈
     */
    private LinkedList<Integer> offsetStack = new LinkedList<>();

    /**
     * 创建方法签名
     *
     * @param methodName 方法名字
     * @param types      参数类型列表
     * @return 方法签名
     */
    public static MethodSignature buildMethodSignature(String methodName, List<Type> types) {
        if (types == null || types.isEmpty()) {
            return new MethodSignature(methodName, null);
        }
        String[] typeStrings = new String[types.size()];
        for (int i = 0; i < typeStrings.length; i++) {
            typeStrings[i] = types.get(i).toTypeDescription();
        }
        return new MethodSignature(methodName, typeStrings);
    }

    public List<ByteCode> getByteCodes() {
        return byteCodes;
    }

    public void setByteCodes(List<ByteCode> byteCodes) {
        this.byteCodes = byteCodes;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Type getResultType() {
        return resultType;
    }

    public void setResultType(Type resultType) {
        this.resultType = resultType;
    }

    public List<Type> getParamTypeList() {
        return paramTypeList;
    }

    public void setParamTypeList(List<Type> paramTypeList) {
        this.paramTypeList = paramTypeList;
    }

    public int getParamSize() {
        return paramTypeList.size();
    }

    /**
     * 创建新符号
     *
     * @param order  符号顺序
     * @param offset 偏移量
     * @param name   标志符名称
     * @param type   标志符类型
     * @return 新创建的符号
     */
    public VariableSymbol createVariableSymbol(int order, int offset, String name, Type type) {
        VariableSymbol variableSymbol = variableSymbolTable.createVariableSymbol(order, offset, name, type);

        increaseOffset(variableSymbol.getType().getTypeWidth());
        increaseOrder();

        return variableSymbol;
    }

    /**
     * 根据标志符名称获取符号
     *
     * @param identifierName 标志符名称
     * @return 符号
     */
    public VariableSymbol getVariableSymbolByName(String identifierName) {
        return variableSymbolTable.getVariableSymbolByName(identifierName);
    }

    public void enterNamespace() {
        if (offsetStack.isEmpty()) {
            assertTrue(orderStack.isEmpty());

            offsetStack.push(0);
            orderStack.push(0);
        } else {
            int curOffset = getOffset();
            offsetStack.push(curOffset);

            int curOrder = getOrder();
            orderStack.push(curOrder);
        }

        variableSymbolTable.enterNamespace();
    }

    public void exitNamespace() {
        assertFalse(offsetStack.isEmpty());
        assertFalse(orderStack.isEmpty());

        offsetStack.pop();
        orderStack.pop();

        variableSymbolTable.exitNamespace();
    }

    public void addByteCode(ByteCode byteCode) {
        byteCodes.add(byteCode);
    }

    private void increaseOffset(int step) {
        Integer top = offsetStack.pop();
        assertNotNull(top);
        offsetStack.push(top + step);
        maxOffset = Math.max(maxOffset, top + step);
    }

    private void increaseOrder() {
        Integer top = orderStack.pop();
        assertNotNull(top);
        orderStack.push(top + 1);
    }

    public int getOffset() {
        Integer peek = offsetStack.peek();
        assertNotNull(peek);
        return peek;
    }

    public int getOrder() {
        Integer peek = orderStack.peek();
        assertNotNull(peek);
        return peek;
    }

    public int getMaxOffset() {
        return maxOffset;
    }

    public void setMaxOffset(int maxOffset) {
        this.maxOffset = maxOffset;
    }

    /**
     * 创建方法签名
     *
     * @return 方法签名
     */
    MethodSignature buildMethodSignature() {
        return buildMethodSignature(this.methodName, this.paramTypeList);
    }
}
