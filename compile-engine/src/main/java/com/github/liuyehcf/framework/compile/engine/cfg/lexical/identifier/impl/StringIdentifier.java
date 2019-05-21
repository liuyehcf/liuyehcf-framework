package com.github.liuyehcf.framework.compile.engine.cfg.lexical.identifier.impl;

import com.github.liuyehcf.framework.compile.engine.cfg.lexical.Token;
import com.github.liuyehcf.framework.compile.engine.cfg.lexical.TokenContext;
import com.github.liuyehcf.framework.compile.engine.cfg.lexical.identifier.TokenIdentifier;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;

import static com.github.liuyehcf.framework.compile.engine.cfg.lexical.identifier.impl.CharIdentifier.ESCAPE_CHARS;

/**
 * @author hechenfeng
 * @date 2018/7/17
 */
public class StringIdentifier implements TokenIdentifier {
    @Override
    public Token identify(TokenContext tokenContext) {
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
    }
}
