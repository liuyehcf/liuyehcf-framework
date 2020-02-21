package com.github.liuyehcf.framework.io.athena;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2020/2/8
 */
public class ReceiverBuilder {

    private final List<Receiver.Unit<?>> units = Lists.newArrayList();

    private ReceiverBuilder() {

    }

    public static ReceiverBuilder create() {
        return new ReceiverBuilder();
    }

    public <I> ReceiverBuilder match(Class<I> clazz, Apply<I> apply) {
        Assert.assertNotNull(clazz, "clazz");
        Assert.assertNotNull(apply, "apply");
        Predicate predicate = new Predicate() {
            @Override
            public boolean match(Object o) {
                return clazz.isInstance(o);
            }
        };

        units.add(new Receiver.Unit<I>(predicate, apply));

        return this;
    }

    public Receiver build() {
        return new Receiver(units);
    }
}
