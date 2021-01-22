package com.github.liuyehcf.framework.common.tools.io.netty;

import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;

/**
 * @author hechenfeng
 * @date 2021/1/16
 */
public abstract class CommonUtils {

    public static int getRefCnt(Object msg) {
        if (msg instanceof ReferenceCounted) {
            return ((ReferenceCounted) msg).refCnt();
        }
        return -1;
    }

    public static void releaseToExpectedRefCnt(Object msg, int expectedRefCnt) {
        if (msg instanceof ReferenceCounted) {
            while (((ReferenceCounted) msg).refCnt() >= expectedRefCnt) {
                ReferenceCountUtil.release(msg);
            }
        } else {
            ReferenceCountUtil.release(msg);
        }
    }
}
