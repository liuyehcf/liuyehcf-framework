package com.github.liuyehcf.framework.flow.engine.spring.boot.starter;

import com.github.liuyehcf.framework.flow.engine.FlowEngine;
import com.github.liuyehcf.framework.flow.engine.runtime.DefaultFlowEngine;
import com.github.liuyehcf.framework.flow.engine.spring.boot.starter.collector.ActionDelegateCollector;
import com.github.liuyehcf.framework.flow.engine.spring.boot.starter.collector.ConditionDelegateCollector;
import com.github.liuyehcf.framework.flow.engine.spring.boot.starter.collector.DelegateInterceptorCollector;
import com.github.liuyehcf.framework.flow.engine.spring.boot.starter.collector.ListenerDelegateCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
@Configuration
public class AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FlowEngine engine() {
        return new DefaultFlowEngine();
    }

    @Bean
    public DelegateInterceptorCollector delegateInterceptorSelector(@Autowired List<FlowEngine> engines) {
        return new DelegateInterceptorCollector(engines);
    }

    @Bean
    public ActionDelegateCollector actionDelegateSelector(@Autowired List<FlowEngine> engines) {
        return new ActionDelegateCollector(engines);
    }

    @Bean
    public ConditionDelegateCollector conditionDelegateSelector(@Autowired List<FlowEngine> engines) {
        return new ConditionDelegateCollector(engines);
    }

    @Bean
    public ListenerDelegateCollector listenerDelegateSelector(@Autowired List<FlowEngine> engines) {
        return new ListenerDelegateCollector(engines);
    }
}
