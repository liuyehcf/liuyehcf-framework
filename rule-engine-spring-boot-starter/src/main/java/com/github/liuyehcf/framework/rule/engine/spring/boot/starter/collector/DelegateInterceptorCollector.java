package com.github.liuyehcf.framework.rule.engine.spring.boot.starter.collector;

import com.github.liuyehcf.framework.rule.engine.RuleEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.factory.Factory;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor.DelegateInterceptor;
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

/**
 * @author hechenfeng
 * @date 2019/4/28
 */
public class DelegateInterceptorCollector implements BeanFactoryPostProcessor {

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

                    Order annotation = AnnotationUtils.getAnnotation(beanClass, Order.class);

                    if (annotation != null) {
                        order = annotation.value();
                    } else {
                        order = 0;
                    }

                    orderedFactories.add(new OrderedDelegateInterceptorFactory(
                            () -> ((DelegateInterceptor) beanFactory.getBean(beanClass)),
                            order)
                    );
                }
            }
        }

        orderedFactories.sort(Comparator.comparingInt(OrderedDelegateInterceptorFactory::getOrder));

        for (OrderedDelegateInterceptorFactory factory : orderedFactories) {
            RuleEngine.registerDelegateInterceptorFactory(factory.target);
        }
    }

    private static final class OrderedDelegateInterceptorFactory {

        private final Factory<DelegateInterceptor> target;
        private final int order;

        private OrderedDelegateInterceptorFactory(Factory<DelegateInterceptor> target, int order) {
            this.target = target;
            this.order = order;
        }

        private int getOrder() {
            return order;
        }
    }
}
