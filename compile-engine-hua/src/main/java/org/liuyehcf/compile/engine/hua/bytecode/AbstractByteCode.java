package org.liuyehcf.compile.engine.hua.bytecode;

/**
 * @author chenlu
 * @date 2018/6/2
 */
public abstract class AbstractByteCode {

    public static final class _goto extends AbstractByteCode {

    }

    public static final class _iinc extends AbstractByteCode {
        private final int increment;

        public _iinc(int increment) {
            this.increment = increment;
        }

        public int getIncrement() {
            return increment;
        }
    }
}
