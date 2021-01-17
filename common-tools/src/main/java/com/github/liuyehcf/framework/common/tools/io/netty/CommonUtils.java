package com.github.liuyehcf.framework.common.tools.io.netty;

import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;

/**
 * @author hechenfeng
 * @date 2021/1/16
 */
public abstract class CommonUtils {

    public static void releaseAll(Object msg) {
        if (msg instanceof ReferenceCounted) {
            while (((ReferenceCounted) msg).refCnt() > 0) {
                ReferenceCountUtil.release(msg);
            }
        } else {
            ReferenceCountUtil.release(msg);
        }
    }
}
