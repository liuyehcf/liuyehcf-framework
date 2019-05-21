package com.github.liuyehcf.framework.compile.engine.test.rg;

import com.github.liuyehcf.framework.compile.engine.rg.utils.SymbolUtils;
import org.junit.Test;

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
