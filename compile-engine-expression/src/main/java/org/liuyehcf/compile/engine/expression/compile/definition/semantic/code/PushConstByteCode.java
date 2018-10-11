package org.liuyehcf.compile.engine.expression.compile.definition.semantic.code;

import org.liuyehcf.compile.engine.expression.compile.CompilerContext;
import org.liuyehcf.compile.engine.expression.compile.definition.model.AttrName;
import org.liuyehcf.compile.engine.expression.compile.definition.model.LiteralType;
import org.liuyehcf.compile.engine.expression.compile.definition.semantic.AbstractSemanticAction;
import org.liuyehcf.compile.engine.expression.core.ExpressionException;
import org.liuyehcf.compile.engine.expression.core.bytecode.sl.*;

import java.math.BigInteger;

import static org.liuyehcf.compile.engine.core.utils.AssertUtils.assertTrue;
import static org.liuyehcf.compile.engine.expression.compile.definition.Constant.NORMAL_BOOLEAN_FALSE;
import static org.liuyehcf.compile.engine.expression.compile.definition.Constant.NORMAL_BOOLEAN_TRUE;

/**
 * 加载字面值
 *
 * @author hechenfeng
 * @date 2018/9/25
 */
public class PushConstByteCode extends AbstractSemanticAction {

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
    private final LiteralType literalType;

    public PushConstByteCode(int literalStackOffset, LiteralType literalType) {
        this.literalStackOffset = literalStackOffset;
        this.literalType = literalType;
    }

    @Override
    public void onAction(CompilerContext context) {
        String literal = context.getAttr(literalStackOffset, AttrName.LITERAL_VALUE);

        switch (literalType) {
            case BOOLEAN:
                /*
                 * 布尔字面值也作为int处理
                 */
                assertTrue(NORMAL_BOOLEAN_TRUE.equals(literal) || NORMAL_BOOLEAN_FALSE.equals(literal), "[SYSTEM_ERROR] - Boolean literal parse error");
                context.addByteCode(new _bconst(NORMAL_BOOLEAN_TRUE.equals(literal)));
                break;
            case INTEGER:
                char firstChar = literal.charAt(0);
                BigInteger bigInteger;
                switch (firstChar) {
                    case '1':
                        if (literal.endsWith("l") || literal.endsWith("L")) {
                            bigInteger = new BigInteger(literal.substring(1, literal.length() - 1), 10);
                            if (bigInteger.bitLength() > 63) {
                                throw new ExpressionException("illegal integer literal='" + literal + "'");
                            }
                            context.addByteCode(new _lconst(bigInteger.longValue()));
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
                        throw new ExpressionException("lexer parsing integer literal error");
                }
                if (bigInteger.bitLength() < 64) {
                    context.addByteCode(new _lconst(bigInteger.longValue()));
                } else {
                    throw new ExpressionException("illegal integer literal='" + literal + "'");
                }
                break;
            case FLOAT:
                double d;
                if (literal.endsWith("f") || literal.endsWith("F") || literal.endsWith("d") || literal.endsWith("D")) {
                    d = Double.parseDouble(literal.substring(0, literal.length() - 1));
                } else {
                    d = Double.parseDouble(literal);
                }
                if (Double.isInfinite(d)) {
                    throw new ExpressionException("illegal float literal='" + literal + "'");
                }
                context.addByteCode(new _dconst(d));
                break;
            case STRING:
                /*
                 * string，也就是char数组
                 */
                assertTrue(literal.length() >= 2 &&
                                (literal.charAt(0) == '\"' && literal.charAt(literal.length() - 1) == '\"'
                                        || literal.charAt(0) == '\'' && literal.charAt(literal.length() - 1) == '\''),
                        "[SYSTEM_ERROR] - String literal parse error");
                String content = literal.substring(1, literal.length() - 1);
                context.addByteCode(new _sconst(content));
                break;
            case NULL:
                context.addByteCode(new _nconst());
                break;
            default:
                throw new ExpressionException("unexpected literalType='" + literalType + "'");
        }
    }
}
