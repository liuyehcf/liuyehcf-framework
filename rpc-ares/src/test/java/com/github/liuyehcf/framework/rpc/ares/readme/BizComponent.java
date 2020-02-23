package com.github.liuyehcf.framework.rpc.ares.readme;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class BizComponent {

    @Resource
    private UserService userService;

    @PostConstruct
    public void business() {
        UserService.UserInfo user = userService.getUser(1);
        System.out.println(user);
    }
}
