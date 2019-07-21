package com.github.liuyehcf.framework.rpc.maple.util;

import java.util.regex.Pattern;

/**
 * @author hechenfeng
 * @date 2019/3/25
 */
public class MetaFormatUtils {
    private static final String SERVICE_INTERFACE_PATTERN_STRING = "[a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*";
    private static final String SERVICE_GROUP_PATTERN_STRING = "[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*";
    private static final String SERVICE_VERSION_INTERFACE_PATTERN_STRING = "[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*";

    private static final Pattern SERVICE_INTERFACE_PATTERN = Pattern.compile(SERVICE_INTERFACE_PATTERN_STRING);
    private static final Pattern SERVICE_GROUP_PATTERN = Pattern.compile(SERVICE_GROUP_PATTERN_STRING);
    private static final Pattern SERVICE_VERSION_PATTERN = Pattern.compile(SERVICE_VERSION_INTERFACE_PATTERN_STRING);

}
