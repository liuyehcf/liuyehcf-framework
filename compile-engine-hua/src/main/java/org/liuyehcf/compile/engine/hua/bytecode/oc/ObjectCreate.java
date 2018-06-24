package org.liuyehcf.compile.engine.hua.bytecode.oc;

import org.liuyehcf.compile.engine.hua.bytecode.ByteCode;

/**
 * 对象创建指令
 *
 * @author hechenfeng
 * @date 2018/6/22
 */
public abstract class ObjectCreate extends ByteCode {
    public ObjectCreate(int operatorCode, int operatorNum, Class<?>[] operatorClasses) {
        super(operatorCode, operatorNum, operatorClasses);
    }
}
