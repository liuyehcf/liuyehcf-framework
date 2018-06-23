package org.liuyehcf.compile.engine.hua.compiler;

import com.alibaba.fastjson.JSON;
import org.liuyehcf.compile.engine.hua.model.Type;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertFalse;
import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertTrue;

/**
 * 符号表
 *
 * @author hechenfeng
 * @date 2018/6/3
 */
public class VariableSymbolTable {

    /**
     * 命名空间映射
     * id -> Namespace
     */
    private final Map<Integer, Namespace> namespaceMap;
    /**
     * 符号表
     * 标志符名字 -> 符号详细信息
     */
    private final Map<Namespace, Map<String, VariableSymbol>> table;
    /**
     * 符号表
     * 标志符偏移量 -> 符号详细信息
     */
    private final Map<Namespace, Map<Integer, VariableSymbol>> offsetMap;
    /**
     * 命名空间计数值
     */
    private int namespaceCnt;
    /**
     * 当前命名空间，初始化为最大的全局命名空间
     */
    private Namespace currentNamespace;

    VariableSymbolTable() {
        namespaceCnt = 0;
        currentNamespace = new Namespace(namespaceCnt++, Namespace.NO_PARENT_NAMESPACE);
        namespaceMap = new LinkedHashMap<>(16);
        table = new LinkedHashMap<>(16);
        offsetMap = new LinkedHashMap<>(16);

        assertFalse(namespaceMap.containsKey(currentNamespace.getId()));
        namespaceMap.put(currentNamespace.getId(), currentNamespace);
        table.put(currentNamespace, new LinkedHashMap<>());
        offsetMap.put(currentNamespace, new LinkedHashMap<>());
    }

    /**
     * 进入新的命名空间
     */
    void enterNamespace() {
        currentNamespace = new Namespace(namespaceCnt++, currentNamespace.getId());

        assertFalse(table.containsKey(currentNamespace));
        assertFalse(offsetMap.containsKey(currentNamespace));
        table.put(currentNamespace, new LinkedHashMap<>(16));
        offsetMap.put(currentNamespace, new LinkedHashMap<>(16));

        assertFalse(namespaceMap.containsKey(currentNamespace.getId()));
        namespaceMap.put(currentNamespace.getId(), currentNamespace);
    }

    /**
     * 退出当前命名空间
     */
    void exitNamespace() {
        int pid = currentNamespace.getPid();

        assertTrue(pid != Namespace.NO_PARENT_NAMESPACE);
        assertTrue(namespaceMap.containsKey(pid));

        currentNamespace = namespaceMap.get(pid);
    }

    /**
     * 沿着命名空间的路径往上查找指定标志符
     *
     * @param name 标志符名字
     * @return 是否存在
     */
    private boolean exists(String name) {
        Namespace namespace = currentNamespace;

        while (namespace != null) {
            if (table.get(namespace).containsKey(name)) {
                return true;
            }
            namespace = namespaceMap.get(namespace.getPid());
        }

        return false;
    }

    /**
     * 创建新符号
     *
     * @param offset 偏移量
     * @param name   标志符名称
     * @param type   标志符类型
     * @return 新创建的符号
     */
    VariableSymbol createVariableSymbol(int offset, String name, Type type) {
        if (exists(name)) {
            return null;
        }

        VariableSymbol newVariableSymbol = new VariableSymbol(offset, currentNamespace, name, type);
        table.get(currentNamespace).put(name, newVariableSymbol);
        offsetMap.get(currentNamespace).put(offset, newVariableSymbol);

        return newVariableSymbol;
    }

    /**
     * 根据标志符名称获取符号
     *
     * @param identifierName 标志符名称
     * @return 符号
     */
    VariableSymbol getVariableSymbolByName(String identifierName) {
        Namespace namespace = currentNamespace;

        while (namespace != null) {
            if (table.get(namespace).containsKey(identifierName)) {
                return table.get(namespace).get(identifierName);
            }
            namespace = namespaceMap.get(namespace.getPid());
        }

        return null;
    }

    /**
     * 根据符号偏移量获取符号
     *
     * @param offset 符号偏移量
     * @return 符号
     */
    VariableSymbol getVariableSymbolByOffset(int offset) {
        Namespace namespace = currentNamespace;

        while (namespace != null) {
            if (offsetMap.get(namespace).containsKey(offset)) {
                return offsetMap.get(namespace).get(offset);
            }
            namespace = namespaceMap.get(namespace.getPid());
        }

        return null;
    }

    @Override
    public String toString() {
        Map<String, Map<String, VariableSymbol>> tableJSONMap = new LinkedHashMap<>(16);
        table.forEach((key, value) -> tableJSONMap.put("[" + key.getId() + ", " + key.getPid() + "]", value));
        return JSON.toJSONString(tableJSONMap);
    }
}
