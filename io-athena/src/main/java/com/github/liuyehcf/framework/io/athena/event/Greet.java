package com.github.liuyehcf.framework.io.athena.event;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.io.athena.Identifier;

/**
 * @author hechenfeng
 * @date 2020/2/7
 */
public class Greet implements SystemEvent {

    private final Identifier identifier;

    /**
     * true means the one who init the greet process
     */
    private final boolean isActive;

    public Greet(Identifier identifier, boolean isActive) {
        Assert.assertNotNull(identifier, "identifier");
        this.identifier = identifier.copy();
        this.isActive = isActive;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public boolean isActive() {
        return isActive;
    }
}
