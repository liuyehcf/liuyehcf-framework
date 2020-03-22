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
     * bytes --decode--> target object
     * if the target object matches the expectedPlainType
     * then use decode method to decode
     *
     * @param expectedPlainType expected plain type
     */
    public boolean matchDecodeType(Type expectedPlainType) {
        return Objects.equals(this.plainType, expectedPlainType);
    }

    /**
     * source object --encode--> bytes
     * if the source object matches the codes
     * then use encode method to encode
     *
     * @param obj source object
     */
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
