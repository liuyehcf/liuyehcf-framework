package org.liuyehcf.compile.engine.hua.commond;

import org.liuyehcf.compile.engine.hua.compiler.ConstantPool;
import org.liuyehcf.compile.engine.hua.compiler.IntermediateInfo;
import org.liuyehcf.compile.engine.hua.compiler.MethodInfo;
import org.liuyehcf.compile.engine.hua.compiler.MethodInfoTable;
import org.liuyehcf.compile.engine.hua.model.Type;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/6/24
 */
class HuaClassInputStream extends DataInputStream {

    HuaClassInputStream(InputStream in) {
        super(in);
    }

    /**
     * 读出中间代码相关数据结构
     */
    IntermediateInfo readHClass() throws IOException {
        /*
         * 1. 读魔数
         */
        String magic = readString();
        assertEquals(magic, HClassConstant.MAGIC);

        /*
         * 2. 读魔数
         */
        ConstantPool constantPool = readConstantPool();

        /*
         * 3. 读魔数
         */
        MethodInfoTable methodInfoTable = readMethodInfoTable();

        return new IntermediateInfo(constantPool, null, methodInfoTable);
    }

    /**
     * 读出常量池
     */
    private ConstantPool readConstantPool() throws IOException {

        /*
         * 1. 读常量池大小
         */
        int constantSize = readInt();

        ConstantPool constantPool = new ConstantPool();

        for (int i = 0; i < constantSize; i++) {

            /*
             * 1. 读常量
             */
            constantPool.addConstant(readString());

        }

        return constantPool;
    }

    /**
     * 读出方法表
     */
    private MethodInfoTable readMethodInfoTable() throws IOException {

        int methodSize = readInt();

        List<MethodInfo> methodInfoList = new ArrayList<>();
        for (int i = 0; i < methodSize; i++) {

            /*
             * 读方法信息
             */
            methodInfoList.add(readMethodInfo());

        }

        return new MethodInfoTable(methodInfoList);
    }

    private MethodInfo readMethodInfo() throws IOException {
        /*
         * 1. 读方法名字
         */
        String methodName = readString();

        /*
         * 2. 读返回类型
         */
        Type resultType = readType();

        /*
         * 3. 读方法参数类型列表
         */
        int paramSize = readInt();
        List<Type> paramTypeList = new ArrayList<>();
        for (int i = 0; i < paramSize; i++) {
            paramTypeList.add(readType());
        }

        /*
         * 4. 读偏移量
         */
        int offset = readInt();

        /*
         * 5. 读字节码
         */

        MethodInfo methodInfo = new MethodInfo();
        methodInfo.setMethodName(methodName);
        methodInfo.setResultType(resultType);
        methodInfo.setParamTypeList(null);
        methodInfo.setOffset(offset);

        return methodInfo;
    }

    private String readString() throws IOException {
        int len = readInt();
        byte[] bytes = new byte[len];
        int cnt = read(bytes);
        assertEquals(cnt, len);
        return new String(bytes);
    }

    private Type readType() throws IOException {
        /*
         * 1. 读类型名
         */
        String typeName = readString();

        /*
         * 2. 读类型宽度
         */
        int typeWidth = readInt();

        /*
         * 3. 读维度
         */
        int dim = readInt();

        return Type.createType(typeName, typeWidth, dim);
    }
}
