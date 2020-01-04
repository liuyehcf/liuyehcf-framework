package com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.attr;

import com.github.liuyehcf.framework.flow.engine.dsl.CompilerContext;
import com.github.liuyehcf.framework.flow.engine.dsl.DslException;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.model.AttrName;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.model.LiteralType;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.AbstractSemanticAction;

import java.math.BigInteger;

import static com.github.liuyehcf.framework.compile.engine.utils.Assert.assertTrue;

/**
 * @author hechenfeng
 * @date 2019/4/25
 */
public class SetLiteral extends AbstractSemanticAction {

    private static final String NORMAL_BOOLEAN_TRUE = "true";
    private static final String NORMAL_BOOLEAN_FALSE = "false";

    /**
     * 偏移量，相对于语法树栈
     * '0'  表示栈顶
     * '-1' 表示栈次顶，以此类推
     * '1' 表示未来入栈的元素，以此类推
     */
    private static final int LITERAL_STACK_OFFSET = 0;

    /**
     * 字面值类型
     */
    private final LiteralType literalType;

    public SetLiteral(LiteralType literalType) {
        this.literalType = literalType;
    }

    @Override
    public void onAction(CompilerContext context) {
        String literal = context.getAttr(LITERAL_STACK_OFFSET, AttrName.LITERAL_VALUE);

        switch (literalType) {
            case BOOLEAN:
                assertTrue(NORMAL_BOOLEAN_TRUE.equals(literal) || NORMAL_BOOLEAN_FALSE.equals(literal), "[SYSTEM_ERROR] - Boolean literal parse error");
                context.setAttrToLeftNode(AttrName.LITERAL_VALUE, NORMAL_BOOLEAN_TRUE.equals(literal));
                break;
            case INTEGER:
                char firstChar = literal.charAt(0);
                BigInteger bigInteger;

                if ('1' == firstChar) {
                    if (literal.endsWith("l") || literal.endsWith("L")) {
                        bigInteger = new BigInteger(literal.substring(1, literal.length() - 1), 10);
                        if (bigInteger.bitLength() > 63) {
                            throw new DslException("illegal long literal='" + literal + "'");
                        }
                        context.setAttrToLeftNode(AttrName.LITERAL_VALUE, bigInteger.longValue());
                    } else {
                        bigInteger = new BigInteger(literal.substring(1), 10);
                        if (bigInteger.bitLength() > 31) {
                            throw new DslException("illegal int literal='" + literal + "'");
                        }
                        context.setAttrToLeftNode(AttrName.LITERAL_VALUE, bigInteger.intValue());
                    }
                } else {
                    if ('2' == firstChar) {
                        bigInteger = new BigInteger(literal.substring(3), 16);
                    } else if ('3' == firstChar) {
                        bigInteger = new BigInteger(literal.substring(2), 8);
                    } else {
                        throw new DslException("lexer parsing integer literal error");
                    }

                    if (bigInteger.bitLength() < 32) {
                        context.setAttrToLeftNode(AttrName.LITERAL_VALUE, bigInteger.intValue());
                    } else if (bigInteger.bitLength() < 64) {
                        context.setAttrToLeftNode(AttrName.LITERAL_VALUE, bigInteger.longValue());
                    } else {
                        throw new DslException("illegal integer literal='" + literal + "'");
                    }
                }
                break;
            case FLOAT:
                if (literal.endsWith("f") || literal.endsWith("F")) {
                    context.setAttrToLeftNode(AttrName.LITERAL_VALUE, Float.parseFloat(literal.substring(0, literal.length() - 1)));
                } else if (literal.endsWith("d") || literal.endsWith("D")) {
                    context.setAttrToLeftNode(AttrName.LITERAL_VALUE, Double.parseDouble(literal.substring(0, literal.length() - 1)));
                } else {
                    context.setAttrToLeftNode(AttrName.LITERAL_VALUE, Double.parseDouble(literal));
                }
                break;
            case STRING:
                assertTrue(literal.length() >= 2
                                && literal.charAt(0) == '\"'
                                && literal.charAt(literal.length() - 1) == '\"',
                        "[SYSTEM_ERROR] - String literal parse error");
                String content = literal.substring(1, literal.length() - 1);
                context.setAttrToLeftNode(AttrName.LITERAL_VALUE, content);
                break;
            default:
                throw new DslException("unexpected literalType='" + literalType + "'");
        }
    }
}
