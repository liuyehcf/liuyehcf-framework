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

    public boolean matchDecodeType(Type type) {
        return Objects.equals(this.plainType, type);
    }

    public boolean matchEncodeObject(Object obj) {
        return plainMatcher.match(obj);
    }

    /**
     * order of this codes
     */
    public abstract int order();

    /**
     * encode plain to cipher
     *
     * @param obj object to be encoded
     */
    public abstract CIPHER encode(PLAIN obj);

    /**
     * decode cipher to plain
     *
     * @param obj               object to be decoded
     * @param expectedPlainType expected plaint
     */
    public abstract PLAIN decode(CIPHER obj, Type expectedPlainType);

    @Override
    public int compareTo(@Nonnull Codes<?, ?> o) {
        return Integer.compare(o.order(), this.order());
    }
}
