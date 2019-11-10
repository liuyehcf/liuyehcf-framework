package com.github.liuyehcf.framework.rpc.maple.test.consumer;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.rpc.maple.test.common.BizRequest;
import com.github.liuyehcf.framework.rpc.maple.test.common.GreetService;
import com.github.liuyehcf.framework.rpc.maple.util.JavaBeanInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author hechenfeng
 * @date 2019/3/22
 */
@Component
@Slf4j
public class InvokerGreetService {

    @Resource
    private GreetService greetService;

    @PostConstruct
    public void init() {
        log.error(greetService.sayHello("liuye"));

        log.error(JSON.toJSONString(greetService.request(JavaBeanInitializer.createJavaBean(new JavaBeanInitializer.TypeReference<BizRequest>() {
        }))));

        log.error(JSON.toJSONString(greetService.request(null)));

        try {
            greetService.throwException();
        } catch (Throwable e) {
            log.error("catch error", e);
        }
    }
}
