package org.liuyehcf.compile.engine.hua.compiler;

import java.util.Objects;

/**
 * 该类需要实现equals方法与hashCode方法（需要作为map的key）
 *
 * @author hechenfeng
 * @date 2018/6/6
 */
public class MethodSignature {

    /**
     * 方法签名
     */
    private final String signature;

    MethodSignature(String name, String[] types) {
        StringBuilder sb = new StringBuilder();
        sb.append(name)
                .append('(');
        if (types != null && types.length != 0) {
            sb.append(types[0]);
            for (int i = 1; i < types.length; i++) {
                sb.append(',')
                        .append(types[i]);
            }
        }
        sb.append(')');
        signature = sb.toString();
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
}
