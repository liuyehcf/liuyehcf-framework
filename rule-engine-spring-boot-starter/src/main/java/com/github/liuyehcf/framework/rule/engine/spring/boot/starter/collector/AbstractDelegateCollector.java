package com.github.liuyehcf.framework.rule.engine.spring.boot.starter.collector;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.Delegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.factory.Factory;
import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.annotation.ActionBean;
import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.annotation.ConditionBean;
import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.annotation.ListenerBean;
import com.github.liuyehcf.framework.rule.engine.spring.boot.starter.util.NameUtils;
import com.google.common.collect.Sets;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author hechenfeng
 * @date 2019/5/8
 */
abstract class AbstractDelegateCollector<T extends Annotation, D extends Delegate> implements BeanFactoryPostProcessor {

    @Override
    @SuppressWarnings("unchecked")
    public final void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String sourceBeanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition definition = beanFactory.getBeanDefinition(sourceBeanName);

            String beanClassName = definition.getBeanClassName();

            // 当用 @Bean 返回的类型是Object时，beanClassName是 null
            if (beanClassName != null) {
                Class<?> beanClass = ClassUtils.resolveClassName(definition.getBeanClassName(), beanFactory.getBeanClassLoader());

                if (getDelegateClass().isAssignableFrom(beanClass)) {

                    Set<String> executableNames = Sets.newHashSet();

                    T annotation = AnnotationUtils.getAnnotation(beanClass, getAnnotationClass());

                    String[] names;

                    if (annotation instanceof ActionBean) {
                        names = ((ActionBean) annotation).names();
                    } else if (annotation instanceof ConditionBean) {
                        names = ((ConditionBean) annotation).names();
                    } else if (annotation instanceof ListenerBean) {
                        names = ((ListenerBean) annotation).names();
                    } else {
                        names = new String[]{sourceBeanName};
                    }

                    for (String name : names) {
                        if (NameUtils.isValidExecutableName(name)) {
                            executableNames.add(name);
                        }
                    }

                    Assert.assertFalse(executableNames.isEmpty(), "missing illegal executable name");

                    for (String executableName : executableNames) {
                        register(executableName, () -> (D) beanFactory.getBean(beanClass));
                    }
                }
            }
        }
    }

    abstract Class<D> getDelegateClass();

    abstract Class<T> getAnnotationClass();

    abstract void register(String executableName, Factory<D> factory);
}
