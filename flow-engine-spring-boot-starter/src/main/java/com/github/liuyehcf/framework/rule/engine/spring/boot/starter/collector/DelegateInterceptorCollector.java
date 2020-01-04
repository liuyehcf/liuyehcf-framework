package com.github.liuyehcf.framework.rule.engine.spring.boot.starter.collector;

import com.github.liuyehcf.framework.rule.engine.FlowEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.config.RuleProperties;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.factory.Factory;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateInterceptor;
import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.annotation.InterceptorBean;
import com.google.common.collect.Lists;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.ClassUtils;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
public class DelegateInterceptorCollector implements BeanFactoryPostProcessor {

    private final List<FlowEngine> engines;

    public DelegateInterceptorCollector(List<FlowEngine> engines) {
        this.engines = engines;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        List<OrderedDelegateInterceptorFactory> orderedFactories = Lists.newArrayList();

        for (String sourceBeanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition definition = beanFactory.getBeanDefinition(sourceBeanName);

            String beanClassName = definition.getBeanClassName();

            // 当用 @Bean 返回的类型是Object时，beanClassName是 null
            if (beanClassName != null) {
                Class<?> beanClass = ClassUtils.resolveClassName(definition.getBeanClassName(), beanFactory.getBeanClassLoader());

                if (DelegateInterceptor.class.isAssignableFrom(beanClass)) {

                    int order;

                    Order orderAnnotation = AnnotationUtils.getAnnotation(beanClass, Order.class);

                    if (orderAnnotation != null) {
                        order = orderAnnotation.value();
                    } else {
                        order = 0;
                    }

                    InterceptorBean interceptorBeanAnnotation = AnnotationUtils.getAnnotation(beanClass, InterceptorBean.class);

                    String engineNameRegex;

                    if (interceptorBeanAnnotation != null) {
                        engineNameRegex = interceptorBeanAnnotation.engineNameRegex();
                    } else {
                        engineNameRegex = ".*";
                    }

                    orderedFactories.add(new OrderedDelegateInterceptorFactory(
                            () -> ((DelegateInterceptor) beanFactory.getBean(beanClass)),
                            order,
                            engineNameRegex)
                    );
                }
            }
        }

        orderedFactories.sort(Comparator.comparingInt(OrderedDelegateInterceptorFactory::getOrder));

        for (OrderedDelegateInterceptorFactory factory : orderedFactories) {
            for (FlowEngine engine : engines) {
                RuleProperties properties = engine.getProperties();
                String name = properties.getName();

                if (Pattern.matches(factory.engineNameRegex, name)) {
                    engine.registerDelegateInterceptorFactory(factory.target);
                }
            }
        }
    }

    private static final class OrderedDelegateInterceptorFactory {

        private final Factory<DelegateInterceptor> target;
        private final int order;
        private final String engineNameRegex;

        private OrderedDelegateInterceptorFactory(Factory<DelegateInterceptor> target, int order, String engineNameRegex) {
            this.target = target;
            this.order = order;
            this.engineNameRegex = engineNameRegex;
        }

        private int getOrder() {
            return order;
        }
    }
}
