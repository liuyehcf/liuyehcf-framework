package com.github.liuyehcf.framework.rpc.ares;

import com.github.liuyehcf.framework.common.tools.type.TypeMatcher;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2020/3/14
 */
public abstract class Codes<PLAIN, CIPHER> implements Comparable<Codes<?, ?>> {

    private final Class<?> plainType;
    private final TypeMatcher plainMatcher;

    protected Codes() {
        plainType = TypeMatcher.fetchType(this, Codes.class, "PLAIN");
        this.plainMatcher = TypeMatcher.create(plainType);
    }

    /**
     * order of this codes
     */
    public abstract int order();

    /**
     * this method decide whether use encode method
     *
     * @param plainObj plain object to be encoded
     * @see this#encode(Object)
     */
    public boolean matchEncodeObject(Object plainObj) {
        return plainMatcher.match(plainObj);
    }

    /**
     * this method decide whether use decode method
     *
     * @param plainType the type of plain object
     * @see this#decode(Object, Type)
     */
    public boolean matchDecodeType(Type plainType) {
        return Objects.equals(this.plainType, plainType);
    }

    /**
     * encode plain object to cipher object
     *
     * @param plainObj plain object to be encoded
     */
    public abstract CIPHER encode(PLAIN plainObj);

    /**
     * decode cipher object to plain object
     *
     * @param cipherObj cipher object to be decoded
     * @param plainType expected type of plain object
     */
    public abstract PLAIN decode(CIPHER cipherObj, Type plainType);

    @Override
    public int compareTo(@Nonnull Codes<?, ?> o) {
        return Integer.compare(o.order(), this.order());
    }
}
