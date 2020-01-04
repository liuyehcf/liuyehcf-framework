package com.github.liuyehcf.framework.rule.engine.spring.boot.starter.collector;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.FlowEngine;
import com.github.liuyehcf.framework.rule.engine.runtime.config.RuleProperties;
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
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author hechenfeng
 * @date 2019/5/8
 */
abstract class AbstractDelegateCollector<T extends Annotation, D extends Delegate> implements BeanFactoryPostProcessor {

    final List<FlowEngine> engines;

    AbstractDelegateCollector(List<FlowEngine> engines) {
        this.engines = engines;
    }

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
                    String engineNameRegex;

                    if (annotation instanceof ActionBean) {
                        names = ((ActionBean) annotation).names();
                        engineNameRegex = ((ActionBean) annotation).engineNameRegex();
                    } else if (annotation instanceof ConditionBean) {
                        names = ((ConditionBean) annotation).names();
                        engineNameRegex = ((ConditionBean) annotation).engineNameRegex();
                    } else if (annotation instanceof ListenerBean) {
                        names = ((ListenerBean) annotation).names();
                        engineNameRegex = ((ListenerBean) annotation).engineNameRegex();
                    } else {
                        names = new String[]{sourceBeanName};
                        engineNameRegex = ".*";
                    }

                    for (String name : names) {
                        if (NameUtils.isValidExecutableName(name)) {
                            executableNames.add(name);
                        }
                    }

                    Assert.assertFalse(executableNames.isEmpty(), "missing illegal executable name");

                    for (FlowEngine engine : engines) {
                        RuleProperties properties = engine.getProperties();
                        String name = properties.getName();
                        if (Pattern.matches(engineNameRegex, name)) {
                            for (String executableName : executableNames) {
                                register(engine, executableName, () -> (D) beanFactory.getBean(beanClass));
                            }
                        }
                    }
                }
            }
        }
    }

    abstract Class<D> getDelegateClass();

    abstract Class<T> getAnnotationClass();

    abstract void register(FlowEngine engine, String executableName, Factory<D> factory);
}
