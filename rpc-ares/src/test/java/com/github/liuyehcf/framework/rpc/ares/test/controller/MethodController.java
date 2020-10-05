package com.github.liuyehcf.framework.rpc.ares.test.controller;

import com.github.liuyehcf.framework.rpc.ares.constant.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hechenfeng
 * @date 2020/5/2
 */
@RestController
public class MethodController {

    @RequestMapping(value = "/method/options", method = {RequestMethod.OPTIONS})
    @ResponseBody
    public String methodOptions() {
        return HttpMethod.OPTIONS.name();
    }

    @RequestMapping(value = "/method/get", method = {RequestMethod.GET})
    @ResponseBody
    public String methodGet() {
        return HttpMethod.GET.name();
    }

    @RequestMapping(value = "/method/head", method = {RequestMethod.HEAD})
    @ResponseBody
    public String methodHead() {
        return HttpMethod.HEAD.name();
    }

    @RequestMapping(value = "/method/post", method = {RequestMethod.POST})
    @ResponseBody
    public String methodPost() {
        return HttpMethod.POST.name();
    }

    @RequestMapping(value = "/method/put", method = {RequestMethod.PUT})
    @ResponseBody
    public String methodPut() {
        return HttpMethod.PUT.name();
    }

    @RequestMapping(value = "/method/patch", method = {RequestMethod.PATCH})
    @ResponseBody
    public String methodPatch() {
        return HttpMethod.PATCH.name();
    }

    @RequestMapping(value = "/method/delete", method = {RequestMethod.DELETE})
    @ResponseBody
    public String methodDelete() {
        return HttpMethod.DELETE.name();
    }
}
