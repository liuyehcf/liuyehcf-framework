package com.github.liuyehcf.framework.expression.engine.core;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.ByteCode;
import com.github.liuyehcf.framework.expression.engine.runtime.ExpressionVirtualMachine;
import com.github.liuyehcf.framework.expression.engine.utils.EnvBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author hechenfeng
 * @date 2018/9/28
 */
public class ExpressionCode {

    /**
     * 属性名
     */
    private final Set<String> propertyNames = Sets.newLinkedHashSet();

    /**
     * 字节码
     */
    private List<ByteCode> byteCodes;

    public ExpressionCode() {
        this.byteCodes = Lists.newArrayList();
    }

    public ExpressionCode(List<ByteCode> byteCodes) {
        Assert.assertNotNull(byteCodes);
        this.byteCodes = byteCodes;
    }

    public Set<String> getPropertyNames() {
        return propertyNames;
    }

    public List<ByteCode> getByteCodes() {
        return byteCodes;
    }

    public void setByteCodes(List<ByteCode> byteCodes) {
        this.byteCodes = byteCodes;
    }

    public void addByteCode(ByteCode byteCode) {
        byteCodes.add(byteCode);
    }

    public void addPropertyName(String propertyName) {
        propertyNames.add(propertyName);
    }

    /**
     * execute expression with properties
     *
     * @param properties properties
     * @param <T>        return type
     * @return execute result
     */
    public <T> T exec(Object... properties) {
        Iterator<String> iterator = propertyNames.iterator();
        EnvBuilder envBuilder = EnvBuilder.builder();
        int i = 0;
        while (i < properties.length && iterator.hasNext()) {
            String key = iterator.next();
            Object value = properties[i++];
            envBuilder.put(key, value);
        }
        return execute(envBuilder.build());
    }

    /**
     * execute expression code without env
     *
     * @param <T> return type
     * @return execute result
     */
    public <T> T execute() {
        return execute(null);
    }

    /**
     * execute expression code with env
     *
     * @param env env
     * @param <T> return type
     * @return execute result
     */
    @SuppressWarnings("unchecked")
    public <T> T execute(Map<String, Object> env) {
        return (T) ExpressionVirtualMachine.execute(this, env).getValue();
    }

    public String toReadableString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteCodes.size(); i++) {
            sb.append(String.format("\t%-4d%-3s%s\n", i, ":", byteCodes.get(i)));
        }
        return sb.toString();
    }
}
