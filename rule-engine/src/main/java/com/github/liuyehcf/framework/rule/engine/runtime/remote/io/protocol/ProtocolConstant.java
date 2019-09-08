package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.protocol;

/**
 * @author hechenfeng
 * @date 2019/9/5
 */
public interface ProtocolConstant {

    /**
     * version of protocol
     * two adjacent versions must be compatible
     */
    int PROTOCOL_VERSION = 1;

    /**
     * minimum length of option, including
     * type, length
     */
    int MIN_OPTION_LENGTH = 2;

    /**
     * total length offset
     */
    int TOTAL_LENGTH_OFFSET = 2;

    /**
     * minimum length of header, including
     * version, headerLength, totalLength
     */
    int MIN_HEADER_LENGTH = 4;

    /**
     * maximum length of header, because header length is recorded by only 1 byte
     */
    int MAX_HEADER_LENGTH = 256;

    /**
     * maximum length of frame, because frame length is recorded by only 2 byte
     */
    int MAX_TOTAL_LENGTH = 65536;

    /**
     * maximum length of payload
     */
    int MAX_PAYLOAD_LENGTH = MAX_TOTAL_LENGTH - MAX_HEADER_LENGTH;

    /**
     * maximum length of options
     */
    int MAX_OPTION_LENGTH = MAX_HEADER_LENGTH - MIN_HEADER_LENGTH;
}
