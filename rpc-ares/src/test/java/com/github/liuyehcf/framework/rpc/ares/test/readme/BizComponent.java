package com.github.liuyehcf.framework.rpc.ares.test.readme;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BizComponent {

    @Resource
    private UserService userService;

    public void business() {
        UserService.UserInfo user = userService.getUser(1);
    }
}
