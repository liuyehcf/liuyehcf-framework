package com.github.liuyehcf.framework.rule.engine.spring.boot.starter.test;

import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.DefaultRuleEngine;
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
    public RuleEngine defaultRuleEngine() {
        return new DefaultRuleEngine();
    }

    @Bean
    public RuleEngine specialRuleEngine() {
        RuleProperties properties = new RuleProperties();
        properties.setName("specialRuleEngine");
        return new DefaultRuleEngine(properties);
    }
}
