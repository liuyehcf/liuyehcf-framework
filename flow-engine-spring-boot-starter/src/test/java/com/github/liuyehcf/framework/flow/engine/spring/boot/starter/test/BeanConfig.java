package com.github.liuyehcf.framework.flow.engine.spring.boot.starter.test;

import com.github.liuyehcf.framework.flow.engine.FlowEngine;
import com.github.liuyehcf.framework.flow.engine.runtime.DefaultFlowEngine;
import com.github.liuyehcf.framework.flow.engine.runtime.config.FlowProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hechenfeng
 * @date 2019/9/5
 */
@Configuration
public class BeanConfig {

    @Bean
    public FlowEngine defaultFlowEngine() {
        return new DefaultFlowEngine();
    }

    @Bean
    public FlowEngine specialFlowEngine() {
        FlowProperties properties = new FlowProperties();
        properties.setName("specialFlowEngine");
        return new DefaultFlowEngine(properties);
    }
}
