package com.github.liuyehcf.framework.rpc.maple.test.consumer;

import com.github.liuyehcf.framework.rpc.maple.MapleConsumer;
import com.github.liuyehcf.framework.rpc.maple.test.common.GreetService;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenlu
 * @date 2019/3/22
 */
@Configuration
public class MapleConfig {
    @MapleConsumer(serviceGroup = "${maple.consumer.group}", serviceVersion = "${maple.consumer.version}")
    private GreetService greetService_MAPLE_CONSUMER_BEAN;
}
