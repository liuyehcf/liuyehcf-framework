package org.liuyehcf.compile.engine.hua.bytecode.sl;

/**
 * 加载常量
 *
 * @author chenlu
 * @date 2018/6/10
 */
public class _ldc extends StoreLoad {

    private final String type;

    private final Object value;

    public _ldc(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public void operate() {

    }

}
