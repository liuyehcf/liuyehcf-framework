package org.liuyehcf.compile.engine.hua.compiler;

/**
 * @author hechenfeng
 * @date 2018/6/3
 */
public class Namespace {
    /**
     * 表示没有父命名空间，即当前命名空间是最大的命名空间
     */
    public static final int NO_PARENT_NAMESPACE = -1;

    /**
     * 命名空间编号
     */
    private final int id;

    /**
     * 父命名空间
     */
    private final int pid;

    public Namespace(int id, int pid) {
        this.id = id;
        this.pid = pid;
    }

    public int getId() {
        return id;
    }

    public int getPid() {
        return pid;
    }

    boolean hasParentNamespace() {
        return pid != NO_PARENT_NAMESPACE;
    }
}
