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
            ReferenceCounted ref = (ReferenceCounted) msg;
            int oldRefCnt = ref.refCnt();
            while (oldRefCnt >= expectedRefCnt) {
                ReferenceCountUtil.release(msg);
                int curRefCnt = ref.refCnt();
                // EmptyByteBuf
                if (curRefCnt >= oldRefCnt) {
                    break;
                }
                oldRefCnt = curRefCnt;
            }
        } else {
            ReferenceCountUtil.release(msg);
        }
    }
}
