package com.github.liuyehcf.framework.rpc.http.test.ares;

import com.github.liuyehcf.framework.rpc.http.AresConsumer;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenfeng.hcf
 * @date 2019/11/8
 */
@Configuration
public class AresConfig {

    @AresConsumer(host = "127.0.0.1", port = "${server.port}")
    private TestClient testClient;
}
