package com.github.liuyehcf.framework.rpc.ares.readme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = "com.github.liuyehcf.framework.rpc.ares.readme")
@PropertySource("classpath:application-test.properties")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}

