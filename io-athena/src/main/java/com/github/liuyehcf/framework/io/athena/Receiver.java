package com.github.liuyehcf.framework.io.athena;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * @author hechenfeng
 * @date 2020/2/8
 */
public class Receiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    private List<Unit<?>> units;

    Receiver(List<Unit<?>> units) {
        this.units = Collections.unmodifiableList(units);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public final void receive(Object event) {
        for (Unit unit : units) {
            if (unit.predicate.match(event)) {
                try {
                    unit.apply.apply(event);
                } catch (Throwable e) {
                    LOGGER.error("receiver catch exception, errorMsg={}", e.getMessage(), e);
                }
            }
        }
    }

    static final class Unit<I> {

        private final Predicate predicate;
        private final Apply<I> apply;

        Unit(Predicate predicate, Apply<I> apply) {
            this.predicate = predicate;
            this.apply = apply;
        }
    }
}
