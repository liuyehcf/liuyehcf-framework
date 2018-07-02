package org.liuyehcf.compile.engine.hua.compile.definition;

import org.liuyehcf.compile.engine.core.cfg.Token;
import org.liuyehcf.compile.engine.core.cfg.TokenContext;
import org.liuyehcf.compile.engine.core.cfg.TokenIdentifier;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;

import java.util.*;

/**
 * 词法识别器
 *
 * @author hechenfeng
 * @date 2018/6/30
 */
public abstract class TokenIdentifiers {

    /**
     * 允许转义的字符
     */
    private static final Map<Character, Integer> ESCAPE_CHARS = initEscapeChars();

    /**
     * char型Token Identifier
     */
    static final TokenIdentifier IDENTIFIER_CHARACTER_LITERAL = (tokenContext) -> {
        Symbol id = tokenContext.getId();
        String remainInput = tokenContext.getRemainInput();

        if (0 < remainInput.length() && remainInput.charAt(0) == '\'') {
            /*
             * 如果是'\n'这种转义字符
             */
            if (1 < remainInput.length() && remainInput.charAt(1) == '\\') {
                if (3 < remainInput.length() && ESCAPE_CHARS.containsKey(remainInput.charAt(2)) && remainInput.charAt(3) == '\'') {
                    tokenContext.setMoveLength(4);
                    return new Token(id, "\'" + (char) (int) ESCAPE_CHARS.get(remainInput.charAt(2)) + "\'");
                } else {
                    return null;
                }
            }
            /*
             * 普通非'的字符
             */
            else if (2 < remainInput.length() && remainInput.charAt(1) != '\'' && remainInput.charAt(2) == '\'') {
                tokenContext.setMoveLength(3);
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

    /**
     * string型Token Identifier
     */
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
                    tokenContext.setMoveLength(i + 1);
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

    /**
     * 十六进制数字
     */
    private static final Set<Character> HEX_INTEGER_DIGIT = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'));

    /**
     * 十六进制前缀
     */
    private static final Set<Character> HEX_INTEGER_PREFIX = new HashSet<>(Arrays.asList('x', 'X'));

    /**
     * 八进制数字
     */
    private static final Set<Character> OCTAL_INTEGER_DIGIT = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7'));

    /**
     * 非八进制数字
     */
    private static final Set<Character> NON_OCTAL_INTEGER_DIGIT = new HashSet<>(Arrays.asList('8', '9'));

    /**
     * 整型Token Identifier
     */
    static final TokenIdentifier IDENTIFIER_INTEGER_LITERAL = new TokenIdentifier() {
        @Override
        public Token identify(TokenContext tokenContext) {
            Symbol id = tokenContext.getId();
            String remainInput = tokenContext.getRemainInput();

            /*
             * 类别标记
             * 1. 十进制
             * 2. 十六进制
             * 3. 八进制
             */
            int typeIndex = -1;

            int i = 0;

            /*
             * 第一位是0，有四种情况
             * 1. 数字0、0L、0l，此时为十进制
             * 2. 十六进制
             * 3. 八进制
             */
            if (getChar(remainInput, i) == '0') {
                i++;

                /*
                 * 情况1
                 */
                if (DECIMAL_INTEGER_SUFFIX.contains(getChar(remainInput, i))) {
                    typeIndex = 1;
                    i++;
                }
                if (decimalLegal(remainInput, i)) {
                    tokenContext.setMoveLength(i);
                    typeIndex = 1;
                    return new Token(id, typeIndex + remainInput.substring(0, i));
                }
                if (typeIndex == 1) {
                    return null;
                }

                /*
                 * 情况2
                 */
                if (HEX_INTEGER_PREFIX.contains(getChar(remainInput, i))) {
                    typeIndex = 2;

                    i++;

                    /*
                     * 后面连续的数字
                     */
                    while (i < remainInput.length() && HEX_INTEGER_DIGIT.contains(remainInput.charAt(i))) {
                        i++;
                    }

                    /*
                     * 后面不可接任何除了a-f之外的字母
                     * 这里可以直接用Character.isAlphabetic方法，虽然这个方法包含了a-f，但是前面的while循环保证了不可能包含a-f
                     */
                    if (i >= remainInput.length()
                            || !Character.isAlphabetic(remainInput.charAt(i))
                            && '_' != remainInput.charAt(i)
                            && '.' != remainInput.charAt(i)) {
                        tokenContext.setMoveLength(i);
                        return new Token(id, typeIndex + remainInput.substring(0, i));
                    }

                    return null;
                }

                /*
                 * 情况3
                 */
                typeIndex = 3;

                while (i < remainInput.length() && OCTAL_INTEGER_DIGIT.contains(remainInput.charAt(i))) {
                    i++;
                }

                /*
                 * 后面不可接任何字母，数字8 9
                 */
                if (i >= remainInput.length()
                        || !Character.isAlphabetic(remainInput.charAt(i))
                        && !NON_OCTAL_INTEGER_DIGIT.contains(remainInput.charAt(i))
                        && '_' != remainInput.charAt(i)
                        && '.' != remainInput.charAt(i)) {
                    tokenContext.setMoveLength(i);
                    return new Token(id, typeIndex + remainInput.substring(0, i));
                }

                return null;
            }


            /*
             * 执行到这里，必定是十进制
             * 至少有一位数字
             * 且第一位数字，必须是非0十进制数
             */
            typeIndex = 1;

            if (!NON_ZERO_DECIMAL_INTEGER_DIGIT.contains(getChar(remainInput, i))) {
                return null;
            }

            i++;

            /*
             * 后面连续的数字
             */
            while (DECIMAL_INTEGER_DIGIT.contains(getChar(remainInput, i))) {
                i++;
            }

            /*
             * 整数后缀
             */
            if (DECIMAL_INTEGER_SUFFIX.contains(getChar(remainInput, i))) {
                i++;
            }

            /*
             * 检查后继是否合法
             */
            if (decimalLegal(remainInput, i)) {
                tokenContext.setMoveLength(i);
                return new Token(id, typeIndex + remainInput.substring(0, i));
            }

            return null;
        }

        private char getChar(String s, int index) {
            if (index < s.length()) {
                return s.charAt(index);
            }
            return '\0';
        }

        /**
         * 当前十进制是否合法
         * @param remain 剩余输入
         * @param index 下标
         * @return 是否合法
         */
        private boolean decimalLegal(String remain, int index) {
            char c;
            return index >= remain.length()
                    || !Character.isAlphabetic(c = remain.charAt(index))
                    && !DECIMAL_INTEGER_DIGIT.contains(c)
                    && '_' != c
                    && '.' != c;
        }
    };

    /**
     * 浮点型指数标记
     */
    private static final Set<Character> FLOAT_EXPONENT = new HashSet<>(Arrays.asList('e', 'E'));

    /**
     * 浮点型指数符号标记
     */
    private static final Set<Character> FLOAT_EXPONENT_SIGN = new HashSet<>(Arrays.asList('+', '-'));

    /**
     * 小数点
     */
    private static final char FLOAT_POINT = '.';

    /**
     * 浮点后缀
     */
    private static final Set<Character> FLOAT_SUFFIX = new HashSet<>(Arrays.asList('f', 'F', 'd', 'D'));

    /**
     * 浮点型Token Identifier
     */
    static final TokenIdentifier IDENTIFIER_FLOATING_POINT_LITERAL = new TokenIdentifier() {
        @Override
        public Token identify(TokenContext tokenContext) {
            Symbol id = tokenContext.getId();
            String remainInput = tokenContext.getRemainInput();

            /*
             * 是否有指数符号
             */
            boolean canHaveExponent = true;

            /*
             * 是否有小数点
             */
            boolean canHavePoint = true;

            /*
             * 是否有必须包含数字的区域
             * 比如位置i出现了e，那么将mustHaveNumberStartIndex设为i+1
             * return时，需要检查[mustHaveNumberFromIndex,i)是否包含数字
             */
            int mustHaveNumberFromIndex = 0;

            int i = 0;
            char c;

            /*
             * 浮点字面值允许包含的符号（不包括后缀f、F、d、D）
             * 1. 指数: e、E
             * 2. 小数点: .
             * 3. 整数: 0-9
             */
            while (FLOAT_EXPONENT.contains(c = getChar(remainInput, i))
                    || FLOAT_POINT == c
                    || DECIMAL_INTEGER_DIGIT.contains(c)) {
                /*
                 * 情况1
                 */
                if (FLOAT_EXPONENT.contains(c)) {
                    /*
                     * 最多允许一个指数符号
                     */
                    if (!canHaveExponent) {
                        return null;
                    }

                    /*
                     * 指数符号出现后，不允许出现指数符号以及小数点
                     */
                    canHaveExponent = false;
                    canHavePoint = false;

                    i++;

                    /*
                     * 允许后面有指数正负号
                     */
                    if (FLOAT_EXPONENT_SIGN.contains(getChar(remainInput, i))) {
                        i++;
                    }

                    /*
                     * 位置i开始到结束必须包含数字
                     */
                    mustHaveNumberFromIndex = i;
                } else if (FLOAT_POINT == c) {

                    if (!canHavePoint) {
                        return null;
                    }

                    canHavePoint = false;

                    /*
                     * 如果第一位就是'.'，那么之后必须包含数字
                     */
                    if (i == 0) {
                        i++;
                        mustHaveNumberFromIndex = i;
                    }
                    /*
                     * 否则，不要求'.'之后有数字
                     */
                    else {
                        i++;
                    }
                } else {
                    /*
                     * 数字
                     */
                    i++;
                }
            }

            /*
             * 检查[mustHaveNumberFromIndex,i)是否为空
             */
            if (remainInput.substring(mustHaveNumberFromIndex, i).isEmpty()) {
                return null;
            }

            /*
             * 必须包含浮点后缀
             */
            if (!FLOAT_SUFFIX.contains(getChar(remainInput, i))) {
                return null;
            }

            i++;

            if (i >= remainInput.length()
                    || !DECIMAL_INTEGER_DIGIT.contains(c = getChar(remainInput, i))
                    && !Character.isAlphabetic(c)
                    && FLOAT_POINT != c) {
                tokenContext.setMoveLength(i);
                return new Token(id, remainInput.substring(0, i));
            }

            return null;
        }

        private char getChar(String s, int index) {
            if (index < s.length()) {
                return s.charAt(index);
            }
            return '\0';
        }
    };

    private static Map<Character, Integer> initEscapeChars() {
        Map<Character, Integer> map = new HashMap<>();
        map.put('b', 8);
        map.put('f', 12);
        map.put('n', 10);
        map.put('r', 13);
        map.put('t', 9);
        map.put('\\', 92);
        map.put('\'', 39);
        map.put('\"', 34);
        map.put('0', 0);
        return map;
    }
}
