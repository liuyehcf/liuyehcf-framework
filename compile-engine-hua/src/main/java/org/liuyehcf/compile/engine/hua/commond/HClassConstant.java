package org.liuyehcf.compile.engine.hua.commond;

/**
 * .hclass文件的常量信息
 *
 * @author chenlu
 * @date 2018/6/24
 */
abstract class HClassConstant {
    static final String HCLASS_SUFFIX = ".hclass";

    static final String MAGIC = "CHENLU_NIUBI";

    static final int MAGIC_LENGHT = 4;

    public static void main(String[] args) {
        System.out.println(MAGIC.getBytes());
    }
}
