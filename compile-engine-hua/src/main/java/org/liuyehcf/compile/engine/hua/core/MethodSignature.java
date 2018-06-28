package org.liuyehcf.compile.engine.hua.core;

import java.util.Objects;

/**
 * 该类需要实现equals方法与hashCode方法（需要作为map的key）
 *
 * @author hechenfeng
 * @date 2018/6/6
 */
public class MethodSignature {

    /**
     * 方法名
     */
    private final String methodName;

    /**
     * 类型描述
     */
    private final String[] typeDescriptions;

    /**
     * 方法签名
     */
    private final String signature;

    MethodSignature(String methodName, String[] typeDescriptions) {
        if (typeDescriptions == null) {
            typeDescriptions = new String[]{};
        }
        this.methodName = methodName;
        this.typeDescriptions = typeDescriptions;

        StringBuilder sb = new StringBuilder();
        sb.append(methodName)
                .append('(');
        if (typeDescriptions.length != 0) {
            sb.append(typeDescriptions[0]);
            for (int i = 1; i < typeDescriptions.length; i++) {
                sb.append(',')
                        .append(typeDescriptions[i]);
            }
        }
        sb.append(')');

        signature = sb.toString();
    }

    public static MethodSignature parse(String methodSignature) {
        int indexOfLeftSmallParenthesis = methodSignature.indexOf('(');
        int indexOfRightSmallParenthesis = methodSignature.indexOf(')');

        String methodName = methodSignature.substring(0, indexOfLeftSmallParenthesis);

        if (methodSignature.endsWith("()")) {
            return new MethodSignature(methodName, null);
        }

        String[] typeDescriptions = methodSignature.substring(indexOfLeftSmallParenthesis + 1, indexOfRightSmallParenthesis).split(",");
        return new MethodSignature(methodName, typeDescriptions);
    }

    public String getMethodName() {
        return methodName;
    }

    public String[] getTypeDescriptions() {
        return typeDescriptions;
    }

    public String getSignature() {
        return signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodSignature that = (MethodSignature) o;
        return Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(signature);
    }

    @Override
    public String toString() {
        return "MethodSignature{" +
                "signature='" + signature + '\'' +
                '}';
    }
}
