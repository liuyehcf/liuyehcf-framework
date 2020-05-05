package com.github.liuyehcf.framework.rpc.ares.test.ares;

import com.github.liuyehcf.framework.rpc.ares.AresConsumer;
import org.springframework.context.annotation.Configuration;

/**
 * @author hechenfeng
 * @date 2019/11/8
 */
@Configuration
public class AresConfig {

    @AresConsumer(host = "127.0.0.1", port = "${server.port}")
    private FeatureClient featureClient;

    @AresConsumer(host = "127.0.0.1", port = "${server.port}")
    private TypeClient typeClient;

    @AresConsumer(host = "127.0.0.1", port = "${server.port}")
    private MethodClient methodClient;
}
