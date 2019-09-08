package com.github.liuyehcf.framework.rule.engine;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import org.apache.commons.lang3.StringUtils;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public class RuleException extends RuntimeException {

    private final RuleErrorCode code;

    public RuleException(RuleErrorCode code) {
        super(code.getReason());
        this.code = code;
    }

    public RuleException(RuleErrorCode code, String message) {
        super(concat(code, message, null));
        this.code = code;
    }

    public RuleException(RuleErrorCode code, Throwable cause) {
        super(concat(code, null, cause), cause);
        this.code = code;
    }

    public RuleException(RuleErrorCode code, String message, Throwable cause) {
        super(concat(code, message, cause), cause);
        this.code = code;
    }

    private static String concat(RuleErrorCode code, String message, Throwable cause) {
        Assert.assertNotNull(code, "code");
        if (message == null && cause == null) {
            return String.format("%s", code.getReason());
        } else if (message == null) {
            if (!StringUtils.isBlank(cause.getMessage())) {
                return String.format("%s - %s", code.getReason(), cause.getMessage());
            } else {
                return String.format("%s", code.getReason());
            }
        } else if (cause == null) {
            return String.format("%s - %s", code.getReason(), message);
        } else {
            if (!StringUtils.isBlank(cause.getMessage())) {
                return String.format("%s - %s - %s", code.getReason(), message, cause.getMessage());
            } else {
                return String.format("%s - %s", code.getReason(), message);
            }
        }
    }

    public RuleErrorCode getCode() {
        return code;
    }
}
