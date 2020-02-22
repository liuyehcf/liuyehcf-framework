package com.github.liuyehcf.framework.flow.engine;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import org.apache.commons.lang3.StringUtils;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public class FlowException extends RuntimeException {

    private final FlowErrorCode code;

    public FlowException(FlowErrorCode code) {
        super(code.getReason());
        this.code = code;
    }

    public FlowException(FlowErrorCode code, String message) {
        super(concat(code, message, null));
        this.code = code;
    }

    public FlowException(FlowErrorCode code, Throwable cause) {
        super(concat(code, null, cause), cause);
        this.code = code;
    }

    public FlowException(FlowErrorCode code, String message, Throwable cause) {
        super(concat(code, message, cause), cause);
        this.code = code;
    }

    private static String concat(FlowErrorCode code, String message, Throwable cause) {
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

    public FlowErrorCode getCode() {
        return code;
    }
}
