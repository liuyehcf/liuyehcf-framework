package org.liuyehcf.compile.engine.hua.bytecode.ir;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * @author chenlu
 * @date 2018/6/25
 */
public abstract class Invoke extends ByteCode {
    Invoke(int operatorCode, int operatorNum, Class<?>[] operatorClasses) {
        super(operatorCode, operatorNum, operatorClasses);
    }
}
