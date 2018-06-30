package org.liuyehcf.compile.engine.hua.compile.definition;

import org.liuyehcf.compile.engine.core.cfg.Token;
import org.liuyehcf.compile.engine.core.cfg.TokenContext;
import org.liuyehcf.compile.engine.core.cfg.TokenIdentifier;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;

import java.util.*;

/**
 * 词法识别器
 *
 * @author chenlu
 * @date 2018/6/30
 */
public abstract class TokenIdentifiers {

    /**
     * 允许转义的字符
     */
    private static final Map<Character, Integer> ESCAPE_CHARS;

    static {
        ESCAPE_CHARS = new HashMap<>();
        ESCAPE_CHARS.put('b', 8);
        ESCAPE_CHARS.put('f', 12);
        ESCAPE_CHARS.put('n', 10);
        ESCAPE_CHARS.put('r', 13);
        ESCAPE_CHARS.put('t', 9);
        ESCAPE_CHARS.put('\\', 92);
        ESCAPE_CHARS.put('\'', 39);
        ESCAPE_CHARS.put('\"', 34);
        ESCAPE_CHARS.put('0', 0);
    }

    static final TokenIdentifier IDENTIFIER_CHAR_LITERAL = (tokenContext) -> {
        Symbol id = tokenContext.getId();
        String remainInput = tokenContext.getRemainInput();

        if (0 < remainInput.length() && remainInput.charAt(0) == '\'') {
            /*
             * 如果是'\n'这种转义字符
             */
            if (1 < remainInput.length() && remainInput.charAt(1) == '\\') {
                if (3 < remainInput.length() && ESCAPE_CHARS.containsKey(remainInput.charAt(2)) && remainInput.charAt(3) == '\'') {
                    return new Token(id, "\'" + (char) (int) ESCAPE_CHARS.get(remainInput.charAt(2)) + "\'");
                } else {
                    return null;
                }
            }
            /*
             * 普通非'的字符
             */
            else if (2 < remainInput.length() && remainInput.charAt(1) != '\'' && remainInput.charAt(2) == '\'') {
                return new Token(id, remainInput.substring(0, 3));
            }
            /*
             * 非法的字符
             */
            else {
                return null;
            }
        }
        return null;

    };
    static final TokenIdentifier IDENTIFIER_STRING_LITERAL = (tokenContext) -> {
        Symbol id = tokenContext.getId();
        String remainInput = tokenContext.getRemainInput();

        if (0 < remainInput.length() && remainInput.charAt(0) == '"') {
            int i = 1;
            StringBuilder sb = new StringBuilder();
            sb.append('"');
            while (i < remainInput.length()) {
                /*
                 * 转义字符
                 */
                if (remainInput.charAt(i) == '\\') {
                    if (i + 1 >= remainInput.length() || !ESCAPE_CHARS.containsKey(remainInput.charAt(i + 1))) {
                        return null;
                    }
                    sb.append((char) (int) ESCAPE_CHARS.get(remainInput.charAt(i + 1)));
                    i += 2;
                }
                /*
                 * "
                 */
                else if (remainInput.charAt(i) == '"') {
                    sb.append('"');
                    return new Token(id, sb.toString());
                }
                /*
                 * 普通字符
                 */
                else {
                    sb.append(remainInput.charAt(i));
                    i++;
                }
            }
        }
        return null;
    };
    /**
     * 非0十进制数字
     */
    private static final Set<Character> NON_ZERO_DECIMAL_INTEGER_DIGIT = new HashSet<>(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9'));
    /**
     * 十进制数字
     */
    private static final Set<Character> DECIMAL_INTEGER_DIGIT = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
    /**
     * 十进制后缀
     */
    private static final Set<Character> DECIMAL_INTEGER_SUFFIX = new HashSet<>(Arrays.asList('l', 'L'));
    static final TokenIdentifier IDENTIFIER_DECIMAL_INTEGER_LITERAL = new TokenIdentifier() {
        @Override
        public Token identify(TokenContext tokenContext) {
            Symbol id = tokenContext.getId();
            String remainInput = tokenContext.getRemainInput();

            int i = 0;

            /*
             * 第一位如果是0
             */
            if (i < remainInput.length() && remainInput.charAt(i) == '0') {
                i++;

                /*
                 * 检查后继是否合法
                 */
                if (nextIsIllegal(i, remainInput)) {
                    return null;
                }
                return new Token(id, remainInput.substring(0, i));
            }

            /*
             * 第一位必须是非0十进制数
             */
            if (i >= remainInput.length() || !NON_ZERO_DECIMAL_INTEGER_DIGIT.contains(remainInput.charAt(i))) {
                return null;
            }

            i++;

            /*
             * 后面连续的数字
             */
            while (i < remainInput.length() && DECIMAL_INTEGER_DIGIT.contains(remainInput.charAt(i))) {
                i++;
            }

            /*
             * 到了文末，那么这是一个合法的数字，至于位数，先不管
             */
            if (i == remainInput.length()) {
                return new Token(id, remainInput.substring(0, i));
            }
            /*
             * 整数后缀
             */
            else if (DECIMAL_INTEGER_SUFFIX.contains(remainInput.charAt(i))) {
                i++;
            }

            /*
             * 检查后继是否合法
             */
            if (nextIsIllegal(i, remainInput)) {
                return null;
            }
            return new Token(id, remainInput.substring(0, i));
        }

        private boolean nextIsIllegal(int i, String s) {
            return i < s.length() && (
                    DECIMAL_INTEGER_DIGIT.contains(s.charAt(i))
                            || Character.isAlphabetic(s.charAt(i))
                            || s.charAt(i) == '_');
        }
    };
}
