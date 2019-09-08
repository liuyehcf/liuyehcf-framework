package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.protocol;

/**
 * @author hechenfeng
 * @date 2019/9/4
 */
public enum OptionType {

    TOTAL_FRAME(1, 1),
    FRAME_INDEX(2, 1),
    PAYLOAD_TYPE(3, 0);

    private int type;
    private int length;

    OptionType(int type, int length) {
        this.type = type;
        this.length = length;
    }

    public static OptionType typeOf(int type) {
        for (OptionType optionType : values()) {
            if (optionType.getType() == type) {
                return optionType;
            }
        }

        throw new UnsupportedOperationException(String.format("unknown option type='%s'", type));
    }

    public int getType() {
        return type;
    }

    public int getLength() {
        return length;
    }
}
