package com.github.liuyehcf.framework.rpc.maple.test.consumer;


import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rpc.maple.MapleConst;
import com.github.liuyehcf.framework.rpc.maple.MapleSpringConsumerBean;
import com.github.liuyehcf.framework.rpc.maple.register.DefaultServiceMeta;
import com.github.liuyehcf.framework.rpc.maple.register.ZookeeperConfigClient;
import com.github.liuyehcf.framework.rpc.maple.test.common.BizRequest;
import com.github.liuyehcf.framework.rpc.maple.test.common.GreetService;
import com.github.liuyehcf.framework.rpc.maple.util.JavaBeanInitializer;
import com.github.liuyehcf.framework.rpc.maple.util.TimeUtils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hechenfeng
 * @date 2019/3/23
 */
public class MainConsumer {
    public static void main(String[] args) throws Exception {
        MapleSpringConsumerBean consumerBean = new MapleSpringConsumerBean();
        consumerBean.setServiceMeta(
                DefaultServiceMeta.builder()
                        .serviceInterface(GreetService.class.getName())
                        .serviceGroup(MapleConst.DEFAULT_GROUP)
                        .serviceVersion(MapleConst.DEFAULT_VERSION)
                        .build()
        );

        ZookeeperConfigClient configClient = new ZookeeperConfigClient();
        configClient.setHost("localhost");
        configClient.setPort(2181);
        configClient.afterPropertiesSet();

        consumerBean.setConfigClient(configClient);
        consumerBean.setObjectType(GreetService.class);
        consumerBean.afterPropertiesSet();

        GreetService greetService = (GreetService) consumerBean.getTarget();
        BizRequest request = JavaBeanInitializer.createJavaBean(new JavaBeanInitializer.TypeReference<BizRequest>() {
        });

        AtomicInteger cnt = new AtomicInteger();

        for (int i = 0; i < 4; i++) {
            Thread thread_i = new Thread(() -> {
                while (true) {
                    try {
                        String name = UUID.randomUUID().toString();
                        String greet = greetService.sayHello(name);
                        Assert.assertTrue(greet.startsWith("Hello, " + name));

                        greetService.request(request);

                        cnt.incrementAndGet();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
            thread_i.setName("liuye_thread_" + i);
            System.out.println("Thread Id=" + thread_i.getId());
            thread_i.start();
        }

        while (true) {
            TimeUtils.sleepMillis(1000);
            System.out.println("tps=" + cnt.get());
            cnt.set(0);
        }
    }
}
