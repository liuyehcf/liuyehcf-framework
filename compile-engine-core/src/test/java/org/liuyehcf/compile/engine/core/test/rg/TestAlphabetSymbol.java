package org.liuyehcf.compile.engine.core.test.rg;

import org.junit.Test;
import org.liuyehcf.compile.engine.core.rg.utils.SymbolUtils;

import static org.junit.Assert.assertTrue;

/**
 * Created by Liuye on 2017/10/26.
 */
public class TestAlphabetSymbol {
    @Test
    public void testAlphabetSymbols() {
        assertTrue(SymbolUtils.getAlphabetSymbols().size() == 256);
    }
}
