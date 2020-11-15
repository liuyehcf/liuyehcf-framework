package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.demo;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.common.tools.promise.Promise;
import com.github.liuyehcf.framework.common.tools.promise.PromiseListener;
import com.github.liuyehcf.framework.flow.engine.ExecutionCondition;
import com.github.liuyehcf.framework.flow.engine.FlowEngine;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.ExecutionInstance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author hechenfeng
 * @date 2020/1/4
 */
@SpringBootApplication(scanBasePackages = {"com.github.liuyehcf.framework.flow.engine.spring.boot.starter.demo"})
public class DemoApplication {

    @Resource
    private FlowEngine flowEngine;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class);
    }

    @PostConstruct
    public void flow() {
        String dsl = "{\n" +
                "   if(printCondition(content=\"hello, \")) {\n" +
                "       printAction(content=\"hechenfeng\")\n" +
                "   }\n" +
                "}";

        Promise<ExecutionInstance> promise = flowEngine.startFlow(new ExecutionCondition(dsl));

        // 注册监听
        promise.addListener(new PromiseListener<ExecutionInstance>() {
            @Override
            public void operationComplete(Promise<ExecutionInstance> promise) {
                System.out.println("trigger promise listener");
                if (promise.isSuccess()) {
                    System.out.println(JSON.toJSONString(promise.get()));
                } else if (promise.isFailure() && promise.cause() != null) {
                    promise.cause().printStackTrace();
                }
            }
        });
    }
}
