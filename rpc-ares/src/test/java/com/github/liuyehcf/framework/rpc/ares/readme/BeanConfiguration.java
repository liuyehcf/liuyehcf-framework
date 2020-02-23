package com.github.liuyehcf.framework.rpc.ares.readme;

import com.github.liuyehcf.framework.rpc.ares.AresConsumer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @AresConsumer(host = "127.0.0.1", port = "8080")
    private UserService userService;
}
