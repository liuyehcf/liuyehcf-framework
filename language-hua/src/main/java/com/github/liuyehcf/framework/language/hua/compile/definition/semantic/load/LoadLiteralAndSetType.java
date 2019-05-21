package com.github.liuyehcf.framework.language.hua.compile.definition.semantic.load;

import com.github.liuyehcf.framework.language.hua.compile.CompilerContext;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.AttrName;
import com.github.liuyehcf.framework.language.hua.compile.definition.model.Type;
import com.github.liuyehcf.framework.language.hua.compile.definition.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.language.hua.core.bytecode.sl.*;

import java.math.BigInteger;

import static com.github.liuyehcf.framework.compile.engine.utils.Assert.assertTrue;
import static com.github.liuyehcf.framework.language.hua.compile.definition.Constant.NORMAL_BOOLEAN_FALSE;
import static com.github.liuyehcf.framework.language.hua.compile.definition.Constant.NORMAL_BOOLEAN_TRUE;

/**
 * 加载字面值
 *
 * @author hechenfeng
 * @date 2018/6/11
 */
public class LoadLiteralAndSetType extends AbstractSemanticAction {

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
    public void onAction(CompilerContext context) {
        String literal = context.getAttr(literalStackOffset, AttrName.LITERAL_VALUE);

        if (Type.TYPE_INT.equals(type)) {
            char firstChar = literal.charAt(0);
            BigInteger bigInteger;
            switch (firstChar) {
                case '1':
                    if (literal.endsWith("l") || literal.endsWith("L")) {
                        bigInteger = new BigInteger(literal.substring(1, literal.length() - 1), 10);
                        if (bigInteger.bitLength() > 63) {
                            throw new RuntimeException("Illegal integer literal");
                        }
                        context.setAttrToLeftNode(AttrName.TYPE, Type.TYPE_LONG);
                        context.addByteCodeToCurrentMethod(new _lconst(bigInteger.longValue()));
                        return;
                    }
                    bigInteger = new BigInteger(literal.substring(1), 10);
                    break;
                case '2':
                    bigInteger = new BigInteger(literal.substring(3), 16);
                    break;
                case '3':
                    bigInteger = new BigInteger(literal.substring(2), 8);
                    break;
                default:
                    throw new RuntimeException("Lexer parsing integer literal error");
            }
            if (bigInteger.bitLength() < 32) {
                context.setAttrToLeftNode(AttrName.TYPE, Type.TYPE_INT);
                context.addByteCodeToCurrentMethod(new _iconst(bigInteger.intValue()));
            } else if (bigInteger.bitLength() < 64) {
                context.setAttrToLeftNode(AttrName.TYPE, Type.TYPE_LONG);
                context.addByteCodeToCurrentMethod(new _lconst(bigInteger.longValue()));
            } else {
                throw new RuntimeException("Illegal integer literal");
            }
        } else if (Type.TYPE_FLOAT.equals(type)) {
            if (literal.endsWith("f") || literal.endsWith("F")) {
                context.setAttrToLeftNode(AttrName.TYPE, Type.TYPE_FLOAT);
                float f = Float.parseFloat(literal.substring(0, literal.length() - 1));
                if (Float.isInfinite(f)) {
                    throw new RuntimeException("Illegal float literal");
                }
                context.addByteCodeToCurrentMethod(new _fconst(f));
            } else {
                context.setAttrToLeftNode(AttrName.TYPE, Type.TYPE_DOUBLE);
                double d;
                if (literal.endsWith("f") || literal.endsWith("F") || literal.endsWith("d") || literal.endsWith("D")) {
                    d = Double.parseDouble(literal.substring(0, literal.length() - 1));
                } else {
                    d = Double.parseDouble(literal);
                }
                if (Double.isInfinite(d)) {
                    throw new RuntimeException("Illegal double literal");
                }
                context.addByteCodeToCurrentMethod(new _dconst(d));
            }
        } else if (Type.TYPE_BOOLEAN.equals(type)) {
            /*
             * 布尔字面值也作为int处理
             */
            assertTrue(NORMAL_BOOLEAN_TRUE.equals(literal) || NORMAL_BOOLEAN_FALSE.equals(literal), "[SYSTEM_ERROR] - Boolean literal parse error");
            context.setAttrToLeftNode(AttrName.TYPE, Type.TYPE_BOOLEAN);
            context.addByteCodeToCurrentMethod(new _iconst(NORMAL_BOOLEAN_TRUE.equals(literal) ? 1 : 0));
        } else if (Type.TYPE_CHAR.equals(type)) {
            /*
             * char也作为int处理
             */
            assertTrue(literal.length() == 3 && literal.charAt(0) == '\'' && literal.charAt(2) == '\'', "[SYSTEM_ERROR] - Char literal parse error");
            context.setAttrToLeftNode(AttrName.TYPE, Type.TYPE_CHAR);
            context.addByteCodeToCurrentMethod(new _iconst(literal.charAt(1)));
        } else if (Type.TYPE_CHAR_ARRAY.equals(type)) {
            /*
             * string，也就是char数组
             */
            assertTrue(literal.length() >= 2 && literal.charAt(0) == '\"' && literal.charAt(literal.length() - 1) == '\"', "[SYSTEM_ERROR] - String literal parse error");
            String content = literal.substring(1, literal.length() - 1);
            int constantOffset = context.addConstant(content);
            context.setAttrToLeftNode(AttrName.TYPE, Type.TYPE_CHAR_ARRAY);
            context.addByteCodeToCurrentMethod(new _ldc(constantOffset));
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
