package com.github.liuyehcf.framework.rpc.maple.netty;

import com.github.liuyehcf.framework.rpc.maple.SerializeType;

/**
 * @author chenlu
 * @date 2019/3/24
 */
public interface MaplePacket {
    /**
     * return package header
     */
    Header getHeader();

    /**
     * return package body
     */
    byte[] getBody();

    /**
     * serialize to byte array
     */
    byte[] serialize();

    interface Header {

        int HEADER_LENGTH = 8 + 1 + 1 + 4;

        /**
         * return requestId
         */
        long getRequestId();

        /**
         * return the type of serialize
         *
         * @see SerializeType
         */
        byte getSerializeType();

        /**
         * status
         */
        byte getStatus();

        /**
         * return the length of body
         */
        int getBodyLength();

        /**
         * serialize to byte array
         */
        byte[] serialize();
    }
}
