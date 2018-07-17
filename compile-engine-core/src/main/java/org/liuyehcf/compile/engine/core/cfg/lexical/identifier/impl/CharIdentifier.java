package org.liuyehcf.compile.engine.core.cfg.lexical.identifier.impl;

import org.liuyehcf.compile.engine.core.cfg.lexical.Token;
import org.liuyehcf.compile.engine.core.cfg.lexical.TokenContext;
import org.liuyehcf.compile.engine.core.cfg.lexical.identifier.TokenIdentifier;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2018/7/17
 */
public class CharIdentifier implements TokenIdentifier {

    /**
     * 允许转义的字符
     */
    static final Map<Character, Integer> ESCAPE_CHARS = initEscapeChars();

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

    @Override
    public Token identify(TokenContext tokenContext) {
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
    }
}
