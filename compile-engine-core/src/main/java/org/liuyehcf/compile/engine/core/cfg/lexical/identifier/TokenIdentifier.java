package org.liuyehcf.compile.engine.core.cfg.lexical.identifier;

import org.liuyehcf.compile.engine.core.cfg.lexical.Token;
import org.liuyehcf.compile.engine.core.cfg.lexical.TokenContext;

import java.io.Serializable;

/**
 * Token识别器
 *
 * @author hechenfeng
 * @date 2018/6/30
 */
public interface TokenIdentifier extends Serializable {

    /**
     * 识别出下一个Token
     * 无合法Token时，返回null
     *
     * @param tokenContext 上下文信息
     * @return Token
     */
    Token identify(TokenContext tokenContext);
}
