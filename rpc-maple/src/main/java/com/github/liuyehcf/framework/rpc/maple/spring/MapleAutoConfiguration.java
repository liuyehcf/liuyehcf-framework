package com.github.liuyehcf.framework.rpc.maple.spring;

import com.github.liuyehcf.framework.rpc.maple.register.ConfigClient;
import com.github.liuyehcf.framework.rpc.maple.register.ZookeeperConfigClient;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenlu
 * @date 2019/3/22
 */
@Configuration
public class MapleAutoConfiguration {

    @Bean(name = SpringConst.CONFIG_CLIENT_BEAN_NAME)
    @ConfigurationProperties(prefix = "maple.register.zookeeper")
    public ConfigClient configClient() {
        return new ZookeeperConfigClient();
    }

    @Bean
    public BeanFactoryPostProcessor providerProcessor() {
        return new MapleProviderSpringPostProcessor();
    }

    @Bean
    public BeanFactoryPostProcessor consumerProcessor() {
        return new MapleConsumerSpringPostProcessor();
    }
}
