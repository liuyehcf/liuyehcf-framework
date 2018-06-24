package org.liuyehcf.compile.engine.hua.commond;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * @author hechenfeng
 * @date 2018/6/24
 */
public class HuaClassInputStream extends BufferedInputStream {
    HuaClassInputStream(InputStream in) {
        super(in);
    }


}
