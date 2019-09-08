package com.github.liuyehcf.framework.rule.engine.spring.boot.starter.test;

import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.DefaultRuleEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hechenfeng
 * @date 2019/9/5
 */
@Configuration
public class BeanConfig {

    @Bean
    public RuleEngine engine() {
        return new DefaultRuleEngine();
    }
}
