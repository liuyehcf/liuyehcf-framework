package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.test.interceptor;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.DelegateInterceptor;
import com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor.DelegateInvocation;
import org.springframework.stereotype.Component;

/**
 * @author hechenfeng
 * @date 2019/5/8
 */
@Component
public class TestInterceptor implements DelegateInterceptor {

    @Override
    public Object invoke(DelegateInvocation delegateInvocation) throws Throwable {
        System.out.println(String.format("type=%s", delegateInvocation.getType()));
        System.out.println(String.format("name=%s", delegateInvocation.getExecutableContext().getName()));
        System.out.println(String.format("argumentNames=%s", JSON.toJSONString(delegateInvocation.getArgumentNames())));
        System.out.println(String.format("argumentValues=%s", JSON.toJSONString(delegateInvocation.getArgumentValues())));

        return delegateInvocation.proceed();
    }
}
