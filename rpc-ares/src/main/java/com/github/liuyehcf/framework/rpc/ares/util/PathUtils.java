package com.github.liuyehcf.framework.rpc.ares.util;

import java.util.Map;

/**
 * @author hechenfeng
 * @date 2019/11/11
 */
public abstract class PathUtils {

    public static String render(String template, Map<String, String> context) {
        int len = template.length();
        int index = 0;

        StringBuilder sb = new StringBuilder();

        while (index < len) {

            int segBegin = index;

            if (getChar(template, index) == '{') {
                index++;


                char c = '\0';
                while (index < len
                        && (c = getChar(template, index)) != '{'
                        && c != '}') {
                    index++;
                }

                if (c == '{') {
                    sb.append(template, segBegin, index);
                    continue;
                } else if (c == '}') {
                    String keyName = template.substring(segBegin + 1, index);
                    if (context.containsKey(keyName)) {
                        sb.append(context.get(keyName));
                        index++;
                        continue;
                    } else {
                        sb.append(template, segBegin, index + 1);
                    }
                } else {
                    sb.append(template, segBegin, index);
                }
            } else {
                sb.append(getChar(template, index));
            }

            index++;
        }

        return sb.toString();
    }

    private static char getChar(String input, int index) {
        if (index < 0 || index >= input.length()) {
            return '\0';
        }

        return input.charAt(index);
    }
}
