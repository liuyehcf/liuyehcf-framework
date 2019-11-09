package com.github.liuyehcf.framework.rpc.maple.test.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * @author chenlu
 * @date 2019/3/22
 */
@SpringBootApplication(scanBasePackages = {"com.github.liuyehcf.framework.rpc.maple.test.provider"})
@PropertySource("classpath:application-provider.properties")
public class ProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }
}
