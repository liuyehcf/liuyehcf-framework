package com.github.liuyehcf.framework.expression.engine.compile.definition.identifier;

import com.github.liuyehcf.framework.compile.engine.cfg.lexical.Token;
import com.github.liuyehcf.framework.compile.engine.cfg.lexical.TokenContext;
import com.github.liuyehcf.framework.compile.engine.cfg.lexical.identifier.TokenIdentifier;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hechenfeng
 * @date 2018/9/29
 */
public class ExpressionStringIdentifier implements TokenIdentifier {

    /**
     * 允许转义的字符
     */
    private static final Map<Character, Integer> ESCAPE_CHARS = initEscapeChars();

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

        char quotation;

        if (0 < remainInput.length() && ((quotation = remainInput.charAt(0)) == '"' || quotation == '\'')) {
            int i = 1;
            StringBuilder sb = new StringBuilder();
            sb.append(quotation);
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
                 * quotation
                 */
                else if (remainInput.charAt(i) == quotation) {
                    sb.append(quotation);
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
    }
}
