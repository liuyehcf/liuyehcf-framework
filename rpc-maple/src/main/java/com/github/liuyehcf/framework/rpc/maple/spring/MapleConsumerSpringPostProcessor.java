package com.github.liuyehcf.framework.rpc.maple.spring;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.rpc.maple.MapleConst;
import com.github.liuyehcf.framework.rpc.maple.MapleConsumer;
import com.github.liuyehcf.framework.rpc.maple.MapleSpringConsumerBean;
import com.github.liuyehcf.framework.rpc.maple.register.DefaultServiceMeta;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @author hechenfeng
 * @date 2019/3/22
 */
public class MapleConsumerSpringPostProcessor implements BeanFactoryPostProcessor {

    private static final String configClient = "configClient";
    private static final String serviceMeta = "serviceMeta";
    private static final String objectType = "objectType";

    @Override
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (final String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            final BeanDefinition definition = beanFactory.getBeanDefinition(beanDefinitionName);

            final String beanClassName = definition.getBeanClassName();

            // 当用 @Bean 返回的类型是Object时，beanClassName是 null
            if (beanClassName != null) {
                final Class<?> beanClass = ClassUtils.resolveClassName(definition.getBeanClassName(), beanFactory.getBeanClassLoader());
                ReflectionUtils.doWithFields(beanClass, field -> MapleConsumerSpringPostProcessor.this.processMapleConsumerBean(field, (DefaultListableBeanFactory) beanFactory));
            }
        }
    }

    private void processMapleConsumerBean(final Field field, final DefaultListableBeanFactory beanFactory) {
        final MapleConsumer annotation = AnnotationUtils.getAnnotation(field, MapleConsumer.class);

        if (annotation == null) {
            return;
        }

        final Class<?> fieldType = field.getType();
        Assert.assertTrue(fieldType.isInterface(), "The type of the field to which '@MapleConsumer' belongs must be the interface type.");

        final String serviceInterface = fieldType.getName();

        final String serviceGroup;
        final String serviceVersion;
        final long clientTimeout;

        if (StringUtils.isBlank(annotation.serviceGroup())) {
            serviceGroup = MapleConst.DEFAULT_GROUP;
        } else {
            serviceGroup = beanFactory.resolveEmbeddedValue(annotation.serviceGroup());
        }

        if (StringUtils.isBlank(annotation.serviceVersion())) {
            serviceVersion = MapleConst.DEFAULT_VERSION;
        } else {
            serviceVersion = beanFactory.resolveEmbeddedValue(annotation.serviceVersion());
        }

        if (annotation.clientTimeout() < 0) {
            clientTimeout = MapleConst.DEFAULT_CLIENT_TIMEOUT;
        } else {
            clientTimeout = annotation.clientTimeout();
        }

        final BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapleSpringConsumerBean.class);

        builder.addPropertyValue(
                serviceMeta,
                DefaultServiceMeta.builder()
                        .serviceInterface(serviceInterface)
                        .serviceGroup(serviceGroup)
                        .serviceVersion(serviceVersion)
                        .clientTimeout(clientTimeout)
                        .build()
        );
        builder.addPropertyReference(configClient, SpringConst.CONFIG_CLIENT_BEAN_NAME);
        builder.addPropertyValue(objectType, fieldType);
        builder.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);

        beanFactory.registerBeanDefinition(field.getName(), builder.getBeanDefinition());
    }

}
