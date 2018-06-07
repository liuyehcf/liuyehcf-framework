package org.liuyehcf.compile.engine.hua.compiler;

/**
 * 方法参数信息
 *
 * @author chenlu
 * @date 2018/6/7
 */
public class ParamInfo {

    /**
     * 符号类型
     */
    private final String type;

    /**
     * 符号宽度
     */
    private final int width;

    public ParamInfo(String type, int width) {
        this.type = type;
        this.width = width;
    }

    public String getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

}
