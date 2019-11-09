package com.github.liuyehcf.framework.rpc.http.test.controller;

import com.alibaba.fastjson.JSON;
import com.github.liuyehcf.framework.rpc.http.test.model.Person;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author chenfeng.hcf
 * @date 2019/11/8
 */
@RestController
public class TestController {

    @RequestMapping("/zeroRequestParam")
    @ResponseBody
    public String zeroRequestParam() {
        return "zeroRequestParam()[]";
    }

    @RequestMapping("/oneRequestParam")
    @ResponseBody
    public String oneRequestParam(@RequestParam("param1") String param1) {
        return String.format("oneRequestParam(%s)[]", param1);
    }

    @RequestMapping("/twoRequestParam")
    @ResponseBody
    public String twoRequestParam(@RequestParam("param1") String param1,
                                  @RequestParam("param2") String param2) {
        return String.format("twoRequestParam(%s, %s)[]", param1, param2);
    }

    @RequestMapping("/requestBody")
    @ResponseBody
    public String requestBody(@RequestBody String requestBody) {
        return String.format("requestBody()[%s]", requestBody);
    }

    @RequestMapping("/oneRequestParamOneRequestBody")
    @ResponseBody
    public String oneRequestParamOneRequestBody(@RequestParam("param1") String param1, @RequestBody String requestBody) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(param1, Person.builder()
                .name(requestBody)
                .build());
        return JSON.toJSONString(map);
    }

    @RequestMapping("/nullableQueryParamAndRequestBody")
    @ResponseBody
    public String nullableQueryParamAndRequestBody(@RequestParam(value = "param1", required = false) String param1, @RequestBody(required = false) String requestBody) {
        if (param1 == null && requestBody == null) {
            return "both null";
        } else if (param1 == null) {
            return "param1 null";
        } else if (requestBody == null) {
            return "requestBody null";
        } else {
            return "both not null";
        }
    }

    @RequestMapping("/returnNull")
    @ResponseBody
    public String returnNull() {
        return null;
    }

    @RequestMapping("/primitiveBoolean")
    @ResponseBody
    public boolean primitiveBoolean(@RequestParam(value = "param1", required = false) boolean param1) {
        return param1;
    }

    @RequestMapping("/primitiveWrapperBoolean")
    @ResponseBody
    public Boolean primitiveWrapperBoolean(@RequestParam(value = "param1", required = false) Boolean param1) {
        return param1;
    }

    @RequestMapping("/primitiveByte")
    @ResponseBody
    public byte primitiveByte(@RequestParam(value = "param1", required = false) byte param1) {
        return param1;
    }

    @RequestMapping("/primitiveWrapperByte")
    @ResponseBody
    public Byte primitiveWrapperByte(@RequestParam(value = "param1", required = false) Byte param1) {
        return param1;
    }

    @RequestMapping("/primitiveShort")
    @ResponseBody
    public short primitiveShort(@RequestParam(value = "param1", required = false) short param1) {
        return param1;
    }

    @RequestMapping("/primitiveWrapperShort")
    @ResponseBody
    public Short primitiveWrapperShort(@RequestParam(value = "param1", required = false) Short param1) {
        return param1;
    }
}
