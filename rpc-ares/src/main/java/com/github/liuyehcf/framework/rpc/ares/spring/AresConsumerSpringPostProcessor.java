package com.github.liuyehcf.framework.rpc.ares.spring;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.rpc.ares.*;
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
import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/11/8
 */
public class AresConsumerSpringPostProcessor implements BeanFactoryPostProcessor {

    private static final String FIELD_OBJECT_TYPE = "objectType";
    private static final String FIELD_SCHEMA = "schema";
    private static final String FIELD_HOST = "host";
    private static final String FIELD_PORT = "port";
    private static final String FIELD_PARAMS_CONVERTERS = "paramsConverters";
    private static final String FIELD_REQUEST_BODY_CONVERTERS = "requestBodyConverters";
    private static final String FIELD_RESPONSE_BODY_CONVERTERS = "responseBodyConverters";
    private static final String FIELD_RESPONSE_HANDLERS = "responseHandlers";

    private final List<ParamsConverter<?>> paramsConverters;
    private final List<RequestBodyConverter<?>> requestBodyConverters;
    private final List<ResponseBodyConverter<?>> responseBodyConverters;
    private final List<ResponseHandler> responseHandlers;

    public AresConsumerSpringPostProcessor(List<ParamsConverter<?>> paramsConverters,
                                           List<RequestBodyConverter<?>> requestBodyConverters,
                                           List<ResponseBodyConverter<?>> responseBodyConverters,
                                           List<ResponseHandler> responseHandlers) {
        this.paramsConverters = paramsConverters;
        this.requestBodyConverters = requestBodyConverters;
        this.responseBodyConverters = responseBodyConverters;
        this.responseHandlers = responseHandlers;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition definition = beanFactory.getBeanDefinition(beanDefinitionName);

            String beanClassName = definition.getBeanClassName();

            // 当用 @Bean 返回的类型是Object时，beanClassName是 null
            if (beanClassName != null) {
                Class<?> beanClass = ClassUtils.resolveClassName(definition.getBeanClassName(), beanFactory.getBeanClassLoader());

                ReflectionUtils.doWithFields(beanClass, field -> AresConsumerSpringPostProcessor.this.processHttpConsumerBean(field, (DefaultListableBeanFactory) beanFactory));
            }
        }
    }

    private void processHttpConsumerBean(Field field, DefaultListableBeanFactory beanFactory) {
        AresConsumer annotation = AnnotationUtils.getAnnotation(field, AresConsumer.class);

        if (annotation == null) {
            return;
        }

        Class<?> fieldType = field.getType();
        Assert.assertTrue(fieldType.isInterface(), "The type of the field to which '@AresConsumer' belongs must be the interface type.");

        String schema = beanFactory.resolveEmbeddedValue(annotation.schema().name());
        String domain = beanFactory.resolveEmbeddedValue(annotation.host());
        String port = beanFactory.resolveEmbeddedValue(annotation.port());

        Assert.assertNotBlank(schema, "'@AresConsumer' missing schema");
        Assert.assertNotBlank(domain, "'@AresConsumer' missing domain");
        Assert.assertNotBlank(port, "'@AresConsumer' missing port");

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(AresSpringConsumerBean.class);

        builder.addPropertyValue(FIELD_PARAMS_CONVERTERS, paramsConverters);
        builder.addPropertyValue(FIELD_REQUEST_BODY_CONVERTERS, requestBodyConverters);
        builder.addPropertyValue(FIELD_RESPONSE_BODY_CONVERTERS, responseBodyConverters);
        builder.addPropertyValue(FIELD_RESPONSE_HANDLERS, responseHandlers);
        builder.addPropertyValue(FIELD_OBJECT_TYPE, fieldType);
        builder.addPropertyValue(FIELD_SCHEMA, schema);
        builder.addPropertyValue(FIELD_HOST, domain);
        builder.addPropertyValue(FIELD_PORT, Integer.parseInt(port));
        builder.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);

        beanFactory.registerBeanDefinition(field.getName(), builder.getBeanDefinition());
    }
}
