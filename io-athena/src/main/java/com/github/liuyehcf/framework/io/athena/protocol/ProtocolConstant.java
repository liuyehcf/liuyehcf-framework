package com.github.liuyehcf.framework.io.athena.protocol;

/**
 * @author hechenfeng
 * @date 2020/2/6
 */
public interface ProtocolConstant {

    /**
     * version of protocol
     * two adjacent versions must be compatible
     */
    int PROTOCOL_VERSION = 1;

    /**
     * total length offset
     */
    int TOTAL_LENGTH_OFFSET = 2;

    /**
     * minimum length of header, including
     * version, headerLength, totalLength, hasNextFrame
     */
    int MIN_HEADER_LENGTH = 5;

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
     * indicates that the current frame is the last frame of package
     */
    int HAS_NOT_NEXT_FRAME = 0;

    /**
     * indicates that the current frame is not the last frame of package
     */
    int HAS_NEXT_FRAME = 1;
}
