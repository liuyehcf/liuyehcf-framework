package com.github.liuyehcf.framework.rpc.http.test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hechenfeng
 * @date 2019/11/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {

    private String country;
    private String name;
    private int age;
}
