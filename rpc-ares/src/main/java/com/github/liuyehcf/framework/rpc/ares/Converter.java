package com.github.liuyehcf.framework.rpc.ares;

import com.github.liuyehcf.framework.common.tools.type.TypeMatcher;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;

/**
 * @author hechenfeng
 * @date 2020/4/9
 */
public abstract class Converter<I, O> implements Comparable<Converter<I, O>> {

    protected final Class<?> inputType;
    protected final Class<?> outputType;
    protected final TypeMatcher inputMatcher;

    protected Converter() {
        inputType = TypeMatcher.fetchType(this, Converter.class, "I");
        outputType = TypeMatcher.fetchType(this, Converter.class, "O");
        this.inputMatcher = TypeMatcher.create(inputType);
    }

    @Override
    public final int compareTo(@Nonnull Converter<I, O> o) {
        return Integer.compare(o.order(), this.order());
    }

    /**
     * whether inputObject and outputType matches this converter
     *
     * @param inputObject input object
     * @param outputType  type of output
     */
    public abstract boolean match(Object inputObject, Type outputType);

    /**
     * convert from input to output
     *
     * @param input      object to be converted
     * @param outputType type of output
     * @return output
     */
    public abstract O convert(I input, Type outputType);

    /**
     * order of this converter
     */
    public abstract int order();
}
