package com.github.liuyehcf.framework.rpc.maple.test.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * @author hechenfeng
 * @date 2019/3/22
 */
@SpringBootApplication(scanBasePackages = {"com.github.liuyehcf.framework.rpc.maple.test.consumer"})
@PropertySource("classpath:application-consumer.properties")
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
