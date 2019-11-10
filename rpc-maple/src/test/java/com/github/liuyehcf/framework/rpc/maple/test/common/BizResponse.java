package com.github.liuyehcf.framework.rpc.maple.test.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/3/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BizResponse implements Serializable {

    private static final long serialVersionUID = -1275917796624336226L;

    private String requestId;

    private String serializeId;

    private String regionId;

    private String scope;

    private String bizCode;

    private String name;

    private String description;

    private List<BizTask> tasks;

}
