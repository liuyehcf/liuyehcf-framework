package com.github.liuyehcf.framework.rpc.maple.test.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author chenlu
 * @date 2019/3/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BizResource implements Serializable {

    private static final long serialVersionUID = 3733580217692259052L;

    private String resourceId;

    private String resourceType;
}
