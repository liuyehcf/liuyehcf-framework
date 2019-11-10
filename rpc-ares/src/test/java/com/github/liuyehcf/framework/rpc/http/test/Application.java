package com.github.liuyehcf.framework.rpc.http.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * @author hechenfeng
 * @date 2019/11/8
 */
@SpringBootApplication(scanBasePackages = "com.github.liuyehcf.framework.rpc.http.test")
@PropertySource("classpath:application-test.properties")
public class Application {
}
