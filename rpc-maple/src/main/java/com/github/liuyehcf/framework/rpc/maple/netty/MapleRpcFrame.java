package com.github.liuyehcf.framework.rpc.maple.netty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hechenfeng
 * @date 2019/3/18
 */
@Data
@AllArgsConstructor
@Builder
public class MapleRpcFrame implements Serializable {

    private static final long serialVersionUID = 2199807803171770612L;

    private final Object[] args;

    private final InvocationMeta invocationMeta;
}
