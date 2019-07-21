package com.github.liuyehcf.framework.rpc.maple.spring;

import com.github.liuyehcf.framework.rpc.maple.MapleConst;
import com.github.liuyehcf.framework.rpc.maple.MapleProvider;
import com.github.liuyehcf.framework.rpc.maple.MapleSpringProviderBean;
import com.github.liuyehcf.framework.rpc.maple.SerializeType;
import com.github.liuyehcf.framework.rpc.maple.register.DefaultServiceMeta;
import com.github.liuyehcf.framework.rpc.maple.util.Assert;
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

/**
 * @author hechenfeng
 * @date 2019/3/22
 */
public class MapleProviderSpringPostProcessor implements BeanFactoryPostProcessor {

    private static final String MAPLE_PROVIDER_BEAN_NAME_PREFIX = "MAPLE_PROVIDER_";

    private static final String CONFIG_CLIENT = "configClient";
    private static final String SERVICE_META = "serviceMeta";
    private static final String TARGET = "target";

    @Override
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (final String sourceBeanName : beanFactory.getBeanDefinitionNames()) {
            final BeanDefinition definition = beanFactory.getBeanDefinition(sourceBeanName);

            final String beanClassName = definition.getBeanClassName();

            // 当用 @Bean 返回的类型是Object时，beanClassName是 null
            if (beanClassName != null) {
                final Class<?> beanClass = ClassUtils.resolveClassName(definition.getBeanClassName(), beanFactory.getBeanClassLoader());
                processMapleProviderBean(beanClass, sourceBeanName, (DefaultListableBeanFactory) beanFactory);
            }
        }
    }

    private void processMapleProviderBean(final Class<?> beanClass, final String sourceBeanName, final DefaultListableBeanFactory beanFactory) {
        final MapleProvider annotation = AnnotationUtils.getAnnotation(beanClass, MapleProvider.class);
        if (annotation == null) {
            return;
        }

        Assert.assertNotNull(annotation.serviceInterface(), "Annotation '@MapleProvider' missing mandatory filed serviceInterface");
        final String serviceInterface = annotation.serviceInterface().getName();

        final String serviceGroup;
        final String serviceVersion;
        final byte serializeType;

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

        if (StringUtils.isBlank(annotation.serializeType())) {
            serializeType = SerializeType.HESSIAN.getCode();
        } else {
            final SerializeType type = SerializeType.of(beanFactory.resolveEmbeddedValue(annotation.serializeType()));
            serializeType = type.getCode();
        }

        final BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapleSpringProviderBean.class);

        builder.addPropertyValue(
                SERVICE_META,
                DefaultServiceMeta.builder()
                        .serviceInterface(serviceInterface)
                        .serviceGroup(serviceGroup)
                        .serviceVersion(serviceVersion)
                        .serializeType(serializeType)
                        .build()
        );
        builder.addPropertyReference(CONFIG_CLIENT, SpringConst.CONFIG_CLIENT_BEAN_NAME);
        builder.addPropertyReference(TARGET, sourceBeanName);

        builder.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);

        beanFactory.registerBeanDefinition(MAPLE_PROVIDER_BEAN_NAME_PREFIX + sourceBeanName, builder.getBeanDefinition());
    }
}
