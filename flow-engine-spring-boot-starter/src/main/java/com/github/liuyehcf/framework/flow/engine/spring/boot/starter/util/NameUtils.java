package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.util;

import java.util.regex.Pattern;

/**
 * @author hechenfeng
 * @date 2019/5/8
 */
public abstract class NameUtils {

    private static final Pattern DOT_IDENTIFIER = Pattern.compile("[a-zA-Z_]([a-zA-Z_]|[0-9])*(\\.[a-zA-Z_][0-9a-zA-Z_]*)+");
    private static final Pattern SLASH_IDENTIFIER = Pattern.compile("[a-zA-Z_]([a-zA-Z_]|[0-9])*(/[a-zA-Z_][0-9a-zA-Z_]*)+");
    private static final Pattern IDENTIFIER = Pattern.compile("[a-zA-Z_][0-9a-zA-Z_]*");

    public static boolean isValidExecutableName(String name) {
        if (name == null) {
            return false;
        }

        return DOT_IDENTIFIER.matcher(name).matches()
                || SLASH_IDENTIFIER.matcher(name).matches()
                || IDENTIFIER.matcher(name).matches();
    }
}
