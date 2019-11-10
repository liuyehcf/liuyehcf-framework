package com.github.liuyehcf.framework.rpc.maple.test.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author hechenfeng
 * @date 2019/3/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BizDetail implements Serializable {
    private static final long serialVersionUID = -2599848800841811365L;

    private String detailId;

    private String detailName;

    private String config;
}
