package com.github.liuyehcf.framework.rpc.maple.test.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hechenfeng
 * @date 2019/3/23
 */
@Data
public class MapleResult<DataType> implements Serializable {

    private static final long serialVersionUID = -4396903314034090831L;

    private int code;

    private String message;

    private DataType data;
}
