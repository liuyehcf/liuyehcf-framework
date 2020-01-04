package com.github.liuyehcf.framework.rule.engine.spring.boot.starter.test;

import com.github.liuyehcf.framework.rule.engine.FlowEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.DefaultFlowEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.config.RuleProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hechenfeng
 * @date 2019/9/5
 */
@Configuration
public class BeanConfig {

    @Bean
    public FlowEngine defaultRuleEngine() {
        return new DefaultFlowEngine();
    }

    @Bean
    public FlowEngine specialRuleEngine() {
        RuleProperties properties = new RuleProperties();
        properties.setName("specialRuleEngine");
        return new DefaultFlowEngine(properties);
    }
}
