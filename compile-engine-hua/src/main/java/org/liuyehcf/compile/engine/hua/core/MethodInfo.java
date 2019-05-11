package org.liuyehcf.compile.engine.hua.core;

import com.alibaba.fastjson.annotation.JSONField;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.core.bytecode.ByteCode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.liuyehcf.compile.engine.core.utils.Assert.assertFalse;
import static org.liuyehcf.compile.engine.core.utils.Assert.assertNotNull;

/**
 * 方法信息
 *
 * @author hechenfeng
 * @date 2018/6/6
 */
public class MethodInfo extends BasicMethodInfo {

    /**
     * 符号表
     */
    @JSONField(serialize = false)
    private final VariableSymbolTable variableSymbolTable = new VariableSymbolTable();

    /**
     * 方法签名
     */
    @JSONField(serialize = false)
    private MethodSignature methodSignature;

    /**
     * 字节码
     */
    private List<ByteCode> byteCodes = new ArrayList<>();

    /**
     * 最大序号（用于分配栈内存）
     */
    @JSONField(serialize = false)
    private int maxOrder;

    /**
     * 符号序号栈
     */
    @JSONField(serialize = false)
    private LinkedList<Integer> orderStack = new LinkedList<>();

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

    public int getParamSize() {
        return getParamTypeList().size();
    }

    public List<ByteCode> getByteCodes() {
        return byteCodes;
    }

    public void setByteCodes(List<ByteCode> byteCodes) {
        this.byteCodes = byteCodes;
    }

    public int getMaxOrder() {
        return maxOrder;
    }

    public void setMaxOrder(int maxOrder) {
        this.maxOrder = maxOrder;
    }

    /**
     * 创建新符号
     *
     * @param order 符号顺序
     * @param name  标志符名称
     * @param type  标志符类型
     * @return 新创建的符号
     */
    public VariableSymbol createVariableSymbol(int order, String name, Type type) {
        VariableSymbol variableSymbol = variableSymbolTable.createVariableSymbol(order, name, type);

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
        if (orderStack.isEmpty()) {
            orderStack.push(0);
        } else {
            int curOrder = getOrder();
            orderStack.push(curOrder);
        }

        variableSymbolTable.enterNamespace();
    }

    public void exitNamespace() {
        assertFalse(orderStack.isEmpty());

        orderStack.pop();

        variableSymbolTable.exitNamespace();
    }

    public void addByteCode(ByteCode byteCode) {
        byteCodes.add(byteCode);
    }

    private void increaseOrder() {
        Integer top = orderStack.pop();
        assertNotNull(top);
        orderStack.push(top + 1);
        maxOrder = Math.max(maxOrder, top + 1);
    }

    @JSONField(serialize = false)
    public int getOrder() {
        Integer peek = orderStack.peek();
        assertNotNull(peek);
        return peek;
    }

    /**
     * 创建方法签名
     *
     * @return 方法签名
     */
    public MethodSignature getMethodSignature() {
        if (methodSignature == null) {
            methodSignature = buildMethodSignature(getMethodName(), getParamTypeList());
        }
        return methodSignature;
    }
}
