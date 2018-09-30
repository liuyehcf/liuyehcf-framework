package org.liuyehcf.compile.engine.core.cfg.lexical.identifier.impl;

import org.liuyehcf.compile.engine.core.cfg.lexical.Token;
import org.liuyehcf.compile.engine.core.cfg.lexical.TokenContext;
import org.liuyehcf.compile.engine.core.cfg.lexical.identifier.TokenIdentifier;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.liuyehcf.compile.engine.core.cfg.lexical.identifier.impl.IntegerIdentifier.DECIMAL_INTEGER_DIGIT;

/**
 * @author hechenfeng
 * @date 2018/7/17
 */
public class FloatIdentifier implements TokenIdentifier {

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
         * 如果包含浮点后缀
         */
        if (FLOAT_SUFFIX.contains(getChar(remainInput, i))) {
            i++;
        }

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
}
