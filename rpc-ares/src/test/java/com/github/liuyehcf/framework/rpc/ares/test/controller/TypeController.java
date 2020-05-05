package com.github.liuyehcf.framework.rpc.ares.test.controller;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author chenfeng.hcf
 * @date 2020/5/2
 */
@RestController
public class TypeController {

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

    @RequestMapping("/primitiveInteger")
    @ResponseBody
    public int primitiveInteger(@RequestParam(value = "param1", required = false) int param1) {
        return param1;
    }

    @RequestMapping("/primitiveWrapperInteger")
    @ResponseBody
    public Integer primitiveWrapperInteger(@RequestParam(value = "param1", required = false) Integer param1) {
        return param1;
    }

    @RequestMapping("/primitiveLong")
    @ResponseBody
    public long primitiveLong(@RequestParam(value = "param1", required = false) long param1) {
        return param1;
    }

    @RequestMapping("/primitiveWrapperLong")
    @ResponseBody
    public Long primitiveWrapperLong(@RequestParam(value = "param1", required = false) Long param1) {
        return param1;
    }

    @RequestMapping("/primitiveFloat")
    @ResponseBody
    public float primitiveFloat(@RequestParam(value = "param1", required = false) float param1) {
        return param1;
    }

    @RequestMapping("/primitiveWrapperFloat")
    @ResponseBody
    public Float primitiveWrapperFloat(@RequestParam(value = "param1", required = false) Float param1) {
        return param1;
    }

    @RequestMapping("/primitiveDouble")
    @ResponseBody
    public double primitiveDouble(@RequestParam(value = "param1", required = false) double param1) {
        return param1;
    }

    @RequestMapping("/primitiveWrapperDouble")
    @ResponseBody
    public Double primitiveWrapperDouble(@RequestParam(value = "param1", required = false) Double param1) {
        return param1;
    }

    @RequestMapping("/bigInteger")
    @ResponseBody
    public BigInteger bigInteger(@RequestParam(value = "param1", required = false) BigInteger param1) {
        return param1;
    }

    @RequestMapping("/bigDecimal")
    @ResponseBody
    public BigDecimal bigDecimal(@RequestParam(value = "param1", required = false) BigDecimal param1) {
        return param1;
    }

    @RequestMapping("/primitiveBytes")
    @ResponseBody
    public byte[] primitiveBytes(@RequestBody byte[] data) {
        return data;
    }
}
