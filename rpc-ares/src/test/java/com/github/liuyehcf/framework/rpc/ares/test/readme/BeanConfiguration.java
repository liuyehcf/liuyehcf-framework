package com.github.liuyehcf.framework.rpc.ares.test.readme;

import com.github.liuyehcf.framework.rpc.ares.AresConsumer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @AresConsumer(host = "192.168.0.1", port = "8080")
    private UserService userService;
}
