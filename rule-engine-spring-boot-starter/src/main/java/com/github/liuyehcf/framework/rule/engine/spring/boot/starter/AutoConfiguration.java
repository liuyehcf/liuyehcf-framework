package com.github.liuyehcf.framework.rule.engine.spring.boot.starter;

import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.collector.ActionDelegateCollector;
import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.collector.ConditionDelegateCollector;
import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.collector.DelegateInterceptorCollector;
import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.collector.ListenerDelegateCollector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
@Configuration
public class AutoConfiguration {

    @Bean
    public DelegateInterceptorCollector delegateInterceptorSelector() {
        return new DelegateInterceptorCollector();
    }

    @Bean
    public ActionDelegateCollector actionDelegateSelector() {
        return new ActionDelegateCollector();
    }

    @Bean
    public ConditionDelegateCollector conditionDelegateSelector() {
        return new ConditionDelegateCollector();
    }

    @Bean
    public ListenerDelegateCollector listenerDelegateSelector() {
        return new ListenerDelegateCollector();
    }
}
