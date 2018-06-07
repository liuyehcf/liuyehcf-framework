package org.liuyehcf.compile.engine.hua.compiler;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlu
 * @date 2018/6/6
 */
public class MethodInfo {

    private final int offset;

    private final int paramNum;

    private final List<ByteCode> byteCodes = new ArrayList<>();

    public MethodInfo(int offset, int paramNum) {
        this.offset = offset;
        this.paramNum = paramNum;
    }

    public void addByteCode(ByteCode byteCode) {
        byteCodes.add(byteCode);
    }
}
