package org.liuyehcf.compile.engine.core.cfg.lexical.identifier.impl;

import org.liuyehcf.compile.engine.core.cfg.lexical.Token;
import org.liuyehcf.compile.engine.core.cfg.lexical.TokenContext;
import org.liuyehcf.compile.engine.core.cfg.lexical.identifier.TokenIdentifier;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hechenfeng
 * @date 2018/7/17
 */
public class IntegerIdentifier implements TokenIdentifier {

    /**
     * 十进制数字
     */
    static final Set<Character> DECIMAL_INTEGER_DIGIT = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
    /**
     * 非0十进制数字
     */
    private static final Set<Character> NON_ZERO_DECIMAL_INTEGER_DIGIT = new HashSet<>(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9'));
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
     *
     * @param remain 剩余输入
     * @param index  下标
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
}
