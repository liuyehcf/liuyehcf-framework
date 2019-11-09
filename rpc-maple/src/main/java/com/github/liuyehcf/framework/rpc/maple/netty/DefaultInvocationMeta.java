package com.github.liuyehcf.framework.rpc.maple.netty;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rpc.maple.register.ServiceMeta;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.regex.Pattern;

/**
 * @author hechenfeng
 * @date 2019/3/22
 */
@Data
@EqualsAndHashCode
@Builder
public class DefaultInvocationMeta implements InvocationMeta {

    private static final long serialVersionUID = -6743592962597638901L;

    private static final String METHOD_NAME_PATTERN_STRING = "[a-zA-Z_][a-zA-Z0-9_]*";

    private static final Pattern METHOD_NAME_PATTERN = Pattern.compile(METHOD_NAME_PATTERN_STRING);

    private ServiceMeta serviceMeta;

    private String methodName;

    private String[] parameterTypes;

    public DefaultInvocationMeta(final ServiceMeta serviceMeta, final String methodName, final String[] parameterTypes) {
        setServiceMeta(serviceMeta);
        setMethodName(methodName);
        setParameterTypes(parameterTypes);
    }

    @Override
    public ServiceMeta getServiceMeta() {
        return serviceMeta;
    }

    public void setServiceMeta(final ServiceMeta serviceMeta) {
        Assert.assertNotNull(serviceMeta, "serviceMeta required");

        this.serviceMeta = serviceMeta;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(final String methodName) {
        Assert.assertNotBlank(methodName, "methodName required");
        Assert.assertTrue(isLegalFormat(methodName),
                String.format("methodName format illegal, expected regex format is '%s'", METHOD_NAME_PATTERN_STRING));

        this.methodName = methodName;
    }

    @Override
    public String[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(final String[] parameterTypes) {
        Assert.assertNotNull(parameterTypes, "parameterTypes is null");
        this.parameterTypes = parameterTypes;
    }

    private boolean isLegalFormat(final String content) {
        return METHOD_NAME_PATTERN.matcher(content).matches();
    }
}
