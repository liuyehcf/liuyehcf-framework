package com.github.liuyehcf.framework.compile.engine.test.utils;

import com.github.liuyehcf.framework.compile.engine.utils.Tuple;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestTuple {
    @Test
    public void testEquals() {
        Object first = new Object();
        Object second = new Object();
        Object third = new Object();

        Assert.assertNotEquals(
                first,
                new Tuple<>(first, second, third)
        );
        assertNotEquals(
                new Tuple<>(first, second, third),
                first
        );

        assertEquals(
                new Tuple<>(first, second, third),
                new Tuple<>(first, second, third)
        );
        assertEquals(
                new Tuple<>(null, second, third),
                new Tuple<>(null, second, third)
        );
        assertEquals(
                new Tuple<>(first, null, third),
                new Tuple<>(first, null, third)
        );
        assertEquals(
                new Tuple<>(first, second, null),
                new Tuple<>(first, second, null)
        );
        assertEquals(
                new Tuple<>(null, null, third),
                new Tuple<>(null, null, third)
        );
        assertEquals(
                new Tuple<>(null, second, null),
                new Tuple<>(null, second, null)
        );
        assertEquals(
                new Tuple<>(first, null, null),
                new Tuple<>(first, null, null)
        );
        assertEquals(
                new Tuple<>(null, null, null),
                new Tuple<>(null, null, null)
        );

        assertNotEquals(
                new Tuple<>(first, second, third),
                new Tuple<>(null, second, third)
        );
        assertNotEquals(
                new Tuple<>(first, second, third),
                new Tuple<>(first, null, third)
        );
        assertNotEquals(
                new Tuple<>(first, second, third),
                new Tuple<>(first, second, null)
        );
        assertNotEquals(
                new Tuple<>(first, second, third),
                new Tuple<>(null, null, third)
        );
        assertNotEquals(
                new Tuple<>(first, second, third),
                new Tuple<>(null, second, null)
        );
        assertNotEquals(
                new Tuple<>(first, second, third),
                new Tuple<>(first, null, null)
        );
        assertNotEquals(
                new Tuple<>(first, second, third),
                new Tuple<>(null, null, null)
        );

        assertNotEquals(
                new Tuple<>(null, second, third),
                new Tuple<>(first, second, third)
        );
        assertNotEquals(
                new Tuple<>(null, second, third),
                new Tuple<>(first, null, third)
        );
        assertNotEquals(
                new Tuple<>(null, second, third),
                new Tuple<>(first, second, null)
        );
        assertNotEquals(
                new Tuple<>(null, second, third),
                new Tuple<>(null, null, third)
        );
        assertNotEquals(
                new Tuple<>(null, second, third),
                new Tuple<>(null, second, null)
        );
        assertNotEquals(
                new Tuple<>(null, second, third),
                new Tuple<>(first, null, null)
        );
        assertNotEquals(
                new Tuple<>(null, second, third),
                new Tuple<>(null, null, null)
        );

        assertNotEquals(
                new Tuple<>(first, null, third),
                new Tuple<>(first, second, third)
        );
        assertNotEquals(
                new Tuple<>(first, null, third),
                new Tuple<>(null, second, third)
        );
        assertNotEquals(
                new Tuple<>(first, null, third),
                new Tuple<>(first, second, null)
        );
        assertNotEquals(
                new Tuple<>(first, null, third),
                new Tuple<>(null, null, third)
        );
        assertNotEquals(
                new Tuple<>(first, null, third),
                new Tuple<>(null, second, null)
        );
        assertNotEquals(
                new Tuple<>(first, null, third),
                new Tuple<>(first, null, null)
        );
        assertNotEquals(
                new Tuple<>(first, null, third),
                new Tuple<>(null, null, null)
        );

        assertNotEquals(
                new Tuple<>(first, second, null),
                new Tuple<>(first, second, third)
        );
        assertNotEquals(
                new Tuple<>(first, second, null),
                new Tuple<>(null, second, third)
        );
        assertNotEquals(
                new Tuple<>(first, second, null),
                new Tuple<>(first, null, third)
        );
        assertNotEquals(
                new Tuple<>(first, second, null),
                new Tuple<>(null, null, third)
        );
        assertNotEquals(
                new Tuple<>(first, second, null),
                new Tuple<>(null, second, null)
        );
        assertNotEquals(
                new Tuple<>(first, second, null),
                new Tuple<>(first, null, null)
        );
        assertNotEquals(
                new Tuple<>(first, second, null),
                new Tuple<>(null, null, null)
        );

        assertNotEquals(
                new Tuple<>(null, null, third),
                new Tuple<>(first, second, third)
        );
        assertNotEquals(
                new Tuple<>(null, null, third),
                new Tuple<>(null, second, third)
        );
        assertNotEquals(
                new Tuple<>(null, null, third),
                new Tuple<>(first, null, third)
        );
        assertNotEquals(
                new Tuple<>(null, null, third),
                new Tuple<>(first, second, null)
        );
        assertNotEquals(
                new Tuple<>(null, null, third),
                new Tuple<>(null, second, null)
        );
        assertNotEquals(
                new Tuple<>(null, null, third),
                new Tuple<>(first, null, null)
        );
        assertNotEquals(
                new Tuple<>(null, null, third),
                new Tuple<>(null, null, null)
        );

        assertNotEquals(
                new Tuple<>(null, second, null),
                new Tuple<>(first, second, third)
        );
        assertNotEquals(
                new Tuple<>(null, second, null),
                new Tuple<>(null, second, third)
        );
        assertNotEquals(
                new Tuple<>(null, second, null),
                new Tuple<>(first, null, third)
        );
        assertNotEquals(
                new Tuple<>(null, second, null),
                new Tuple<>(first, second, null)
        );
        assertNotEquals(
                new Tuple<>(null, second, null),
                new Tuple<>(null, null, third)
        );
        assertNotEquals(
                new Tuple<>(null, second, null),
                new Tuple<>(first, null, null)
        );
        assertNotEquals(
                new Tuple<>(null, second, null),
                new Tuple<>(null, null, null)
        );

        assertNotEquals(
                new Tuple<>(first, null, null),
                new Tuple<>(first, second, third)
        );
        assertNotEquals(
                new Tuple<>(first, null, null),
                new Tuple<>(null, second, third)
        );
        assertNotEquals(
                new Tuple<>(first, null, null),
                new Tuple<>(first, null, third)
        );
        assertNotEquals(
                new Tuple<>(first, null, null),
                new Tuple<>(first, second, null)
        );
        assertNotEquals(
                new Tuple<>(first, null, null),
                new Tuple<>(null, null, third)
        );
        assertNotEquals(
                new Tuple<>(first, null, null),
                new Tuple<>(null, second, null)
        );
        assertNotEquals(
                new Tuple<>(first, null, null),
                new Tuple<>(null, null, null)
        );

        assertNotEquals(
                new Tuple<>(null, null, null),
                new Tuple<>(first, second, third)
        );
        assertNotEquals(
                new Tuple<>(null, null, null),
                new Tuple<>(null, second, third)
        );
        assertNotEquals(
                new Tuple<>(null, null, null),
                new Tuple<>(first, null, third)
        );
        assertNotEquals(
                new Tuple<>(null, null, null),
                new Tuple<>(first, second, null)
        );
        assertNotEquals(
                new Tuple<>(null, null, null),
                new Tuple<>(null, null, third)
        );
        assertNotEquals(
                new Tuple<>(null, null, null),
                new Tuple<>(null, second, null)
        );
        assertNotEquals(
                new Tuple<>(null, null, null),
                new Tuple<>(first, null, null)
        );
    }
}
