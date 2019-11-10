package com.github.liuyehcf.framework.rpc.maple.test.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/3/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BizTask implements Serializable {
    private static final long serialVersionUID = 2239983993152599833L;

    private String resourceId;

    private String resourceType;

    private String taskId;

    private List<BizDetail> details;
}
