package org.liuyehcf.compile.engine.hua.compile.definition.semantic.load;

import org.liuyehcf.compile.engine.hua.compile.HuaContext;
import org.liuyehcf.compile.engine.hua.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.hua.compile.definition.model.Type;
import org.liuyehcf.compile.engine.hua.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.hua.core.bytecode.sl._iconst;
import org.liuyehcf.compile.engine.hua.core.bytecode.sl._lconst;
import org.liuyehcf.compile.engine.hua.core.bytecode.sl._ldc;

import java.io.Serializable;
import java.math.BigInteger;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertTrue;
import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.NORMAL_BOOLEAN_FALSE;
import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.NORMAL_BOOLEAN_TRUE;

/**
 * 加载字面值
 *
 * @author hechenfeng
 * @date 2018/6/11
 */
public class LoadLiteralAndSetType extends AbstractSemanticAction implements Serializable {

    /**
     * 字面值-偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private final int literalStackOffset;

    /**
     * 字面值类型
     */
    private final Type type;

    public LoadLiteralAndSetType(int literalStackOffset, Type type) {
        this.literalStackOffset = literalStackOffset;
        this.type = type;
    }

    @Override
    public void onAction(HuaContext context) {
        String literal = context.getAttr(literalStackOffset, AttrName.LITERAL_VALUE);

        if (Type.TYPE_INT.equals(type)) {
            /*
             * integer字面值可能是int类型或者long类型
             * 详见 <literal> → <integer literal> 产生式
             */
            if (literal.endsWith("l") || literal.endsWith("L")) {
                context.setAttrToLeftNode(AttrName.TYPE, Type.TYPE_LONG);
                context.addByteCodeToCurrentMethod(new _lconst(parseLong(literal)));
            } else {
                context.setAttrToLeftNode(AttrName.TYPE, Type.TYPE_INT);
                context.addByteCodeToCurrentMethod(new _iconst(parseInt(literal)));
            }
        } else if (Type.TYPE_BOOLEAN.equals(type)) {
            /*
             * 布尔字面值也作为int处理
             */
            assertTrue(NORMAL_BOOLEAN_TRUE.equals(literal) || NORMAL_BOOLEAN_FALSE.equals(literal));
            context.setAttrToLeftNode(AttrName.TYPE, Type.TYPE_BOOLEAN);
            context.addByteCodeToCurrentMethod(new _iconst(NORMAL_BOOLEAN_TRUE.equals(literal) ? 1 : 0));
        } else if (Type.TYPE_CHAR.equals(type)) {
            /*
             * char也作为int处理
             */
            assertTrue(literal.length() == 3 && literal.charAt(0) == '\'' && literal.charAt(2) == '\'');
            context.setAttrToLeftNode(AttrName.TYPE, Type.TYPE_CHAR);
            context.addByteCodeToCurrentMethod(new _iconst(literal.charAt(1)));
        } else if (Type.TYPE_CHAR_ARRAY.equals(type)) {
            /*
             * string，也就是char数组
             */
            assertTrue(literal.length() >= 2 && literal.charAt(0) == '\"' && literal.charAt(literal.length() - 1) == '\"');
            String content = literal.substring(1, literal.length() - 1);
            int constantOffset = context.addConstant(content);
            context.setAttrToLeftNode(AttrName.TYPE, Type.TYPE_CHAR_ARRAY);
            context.addByteCodeToCurrentMethod(new _ldc(constantOffset));
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private long parseLong(String literal) {
        if ("0".equals(literal)) {
            return 0L;
        }
        if (literal.startsWith("0x") || literal.startsWith("0X")) {
            return new BigInteger(literal.substring(2), 16).longValue();
        } else if (literal.startsWith("0")) {
            return new BigInteger(literal.substring(1), 8).longValue();
        } else {
            return Long.parseLong(literal.substring(0, literal.length() - 1));
        }
    }

    private int parseInt(String literal) {
        if ("0".equals(literal)) {
            return 0;
        }
        if (literal.startsWith("0x") || literal.startsWith("0X")) {
            return new BigInteger(literal.substring(2), 16).intValue();
        } else if (literal.startsWith("0")) {
            return new BigInteger(literal.substring(1), 8).intValue();
        } else {
            return Integer.parseInt(literal);
        }
    }
}
