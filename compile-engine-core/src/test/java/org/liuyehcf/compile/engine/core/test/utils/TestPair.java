package org.liuyehcf.compile.engine.core.test.utils;

import org.junit.Assert;
import org.junit.Test;
import org.liuyehcf.compile.engine.core.utils.Pair;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestPair {
    @Test
    public void testEquals() {
        Object first = new Object();
        Object second = new Object();

        Assert.assertNotEquals(
                new Pair<>(first, second)
                , first
        );
        assertNotEquals(
                first,
                new Pair<>(first, second)
        );

        assertEquals(
                new Pair<>(first, second),
                new Pair<>(first, second)
        );
        assertEquals(
                new Pair<>(first, null),
                new Pair<>(first, null)
        );
        assertEquals(
                new Pair<>(null, second),
                new Pair<>(null, second)
        );
        assertEquals(
                new Pair<>(null, null),
                new Pair<>(null, null)
        );

        assertNotEquals(
                new Pair<>(first, second),
                new Pair<>(first, null)
        );
        assertNotEquals(
                new Pair<>(first, second),
                new Pair<>(null, second)
        );
        assertNotEquals(
                new Pair<>(first, second),
                new Pair<>(null, null)
        );

        assertNotEquals(
                new Pair<>(first, null),
                new Pair<>(first, second)
        );
        assertNotEquals(
                new Pair<>(first, null),
                new Pair<>(null, second)
        );
        assertNotEquals(
                new Pair<>(first, null),
                new Pair<>(null, null)
        );

        assertNotEquals(
                new Pair<>(null, second),
                new Pair<>(first, second)
        );
        assertNotEquals(
                new Pair<>(null, second),
                new Pair<>(first, null)
        );
        assertNotEquals(
                new Pair<>(null, second),
                new Pair<>(null, null)
        );

        assertNotEquals(
                new Pair<>(null, null),
                new Pair<>(first, second)
        );
        assertNotEquals(
                new Pair<>(null, null),
                new Pair<>(first, null)
        );
        assertNotEquals(
                new Pair<>(null, null),
                new Pair<>(null, second)
        );
    }
}
