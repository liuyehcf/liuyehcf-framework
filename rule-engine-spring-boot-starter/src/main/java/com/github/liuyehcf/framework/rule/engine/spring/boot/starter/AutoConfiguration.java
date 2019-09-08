package com.github.liuyehcf.framework.rule.engine.spring.boot.starter;

import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.DefaultRuleEngine;
import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.collector.ActionDelegateCollector;
import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.collector.ConditionDelegateCollector;
import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.collector.DelegateInterceptorCollector;
import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.collector.ListenerDelegateCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
@Configuration
public class AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RuleEngine engine() {
        return new DefaultRuleEngine();
    }

    @Bean
    public DelegateInterceptorCollector delegateInterceptorSelector(@Autowired RuleEngine engine) {
        return new DelegateInterceptorCollector(engine);
    }

    @Bean
    public ActionDelegateCollector actionDelegateSelector(@Autowired RuleEngine engine) {
        return new ActionDelegateCollector(engine);
    }

    @Bean
    public ConditionDelegateCollector conditionDelegateSelector(@Autowired RuleEngine engine) {
        return new ConditionDelegateCollector(engine);
    }

    @Bean
    public ListenerDelegateCollector listenerDelegateSelector(@Autowired RuleEngine engine) {
        return new ListenerDelegateCollector(engine);
    }
}
