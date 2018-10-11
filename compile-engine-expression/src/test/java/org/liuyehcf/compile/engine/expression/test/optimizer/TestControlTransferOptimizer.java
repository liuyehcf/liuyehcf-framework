package org.liuyehcf.compile.engine.expression.test.optimizer;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.liuyehcf.compile.engine.expression.compile.optimize.impl.ControlTransferOptimizer;
import org.liuyehcf.compile.engine.expression.core.ExpressionCode;
import org.liuyehcf.compile.engine.expression.core.bytecode.ByteCode;
import org.liuyehcf.compile.engine.expression.core.bytecode.cf._goto;
import org.liuyehcf.compile.engine.expression.core.bytecode.cf._ifeq;
import org.liuyehcf.compile.engine.expression.core.bytecode.cp._add;
import org.liuyehcf.compile.engine.expression.core.bytecode.ir._return;
import org.liuyehcf.compile.engine.expression.core.bytecode.sl._bconst;
import org.liuyehcf.compile.engine.expression.core.bytecode.sl._cconst;
import org.liuyehcf.compile.engine.expression.core.bytecode.sl._lconst;
import org.liuyehcf.compile.engine.expression.core.io.ExpressionInputStream;
import org.liuyehcf.compile.engine.expression.core.io.ExpressionOutputStream;
import org.liuyehcf.compile.engine.expression.test.TestBase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/10/1
 */
public class TestControlTransferOptimizer extends TestBase {
    @Test
    public void case1() {
        List<ByteCode> byteCodes = Lists.newArrayList();
        ByteCode code0 = new _lconst(1L);
        ByteCode code1 = new _ifeq(4);
        ByteCode code2 = new _bconst(true);
        ByteCode code3 = new _goto(5);
        ByteCode code4 = new _bconst(false);
        ByteCode code5 = new _return();

        byteCodes.add(code0);
        byteCodes.add(code1);
        byteCodes.add(code2);
        byteCodes.add(code3);
        byteCodes.add(code4);
        byteCodes.add(code5);

        ExpressionCode code = new ExpressionCode(byteCodes);
        new ControlTransferOptimizer().optimize(code);

        assertEquals(6, code.getByteCodes().size());
        assertEquals(code0, code.getByteCodes().get(0));
        assertEquals(code1, code.getByteCodes().get(1));
        assertEquals(code2, code.getByteCodes().get(2));
        assertEquals(code3, code.getByteCodes().get(3));
        assertEquals(code4, code.getByteCodes().get(4));
        assertEquals(code5, code.getByteCodes().get(5));
    }

    @Test
    public void case2() {
        List<ByteCode> byteCodes = Lists.newArrayList();
        ByteCode code0 = new _cconst(1);
        ByteCode code1 = new _ifeq(4);
        ByteCode code2 = new _bconst(true);
        ByteCode code3 = new _goto(5);
        ByteCode code4 = new _bconst(false);
        ByteCode code5 = new _return();

        byteCodes.add(code0);
        byteCodes.add(code1);
        byteCodes.add(code2);
        byteCodes.add(code3);
        byteCodes.add(code4);
        byteCodes.add(code5);

        ExpressionCode code = new ExpressionCode(byteCodes);
        new ControlTransferOptimizer().optimize(code);

        assertEquals(2, code.getByteCodes().size());
        assertEquals(code2, code.getByteCodes().get(0));
        assertEquals(code5, code.getByteCodes().get(1));
    }

    @Test
    public void case3() {
        List<ByteCode> byteCodes = Lists.newArrayList();
        ByteCode code0 = new _lconst(1L);
        ByteCode code1 = new _lconst(2L);
        ByteCode code2 = new _add();
        ByteCode code3 = new _goto(0);

        byteCodes.add(code0);
        byteCodes.add(code1);
        byteCodes.add(code2);
        byteCodes.add(code3);

        ExpressionCode code = new ExpressionCode(byteCodes);
        new ControlTransferOptimizer().optimize(code);

        assertEquals(4, code.getByteCodes().size());
        assertEquals(code0, code.getByteCodes().get(0));
        assertEquals(code1, code.getByteCodes().get(1));
        assertEquals(code2, code.getByteCodes().get(2));
        assertEquals(code3, code.getByteCodes().get(3));
    }

    @Test
    public void case4() {
        List<ByteCode> byteCodes = Lists.newArrayList();
        ByteCode code0 = new _lconst(1L);
        ByteCode code1 = new _lconst(2L);
        ByteCode code2 = new _add();
        ByteCode code3 = new _goto(3);

        byteCodes.add(code0);
        byteCodes.add(code1);
        byteCodes.add(code2);
        byteCodes.add(code3);

        ExpressionCode code = new ExpressionCode(byteCodes);
        new ControlTransferOptimizer().optimize(code);

        assertEquals(4, code.getByteCodes().size());
        assertEquals(code0, code.getByteCodes().get(0));
        assertEquals(code1, code.getByteCodes().get(1));
        assertEquals(code2, code.getByteCodes().get(2));
        assertEquals(code3, code.getByteCodes().get(3));
    }

    @Test
    public void case5() throws IOException {
        List<ByteCode> byteCodes = Lists.newArrayList();
        ByteCode code0 = new _cconst(1);
        ByteCode code1 = new _lconst(2L);
        ByteCode code2 = new _bconst(true);
        ByteCode code3 = new _return();

        byteCodes.add(code0);
        byteCodes.add(code1);
        byteCodes.add(code2);
        byteCodes.add(code3);

        ExpressionCode code = new ExpressionCode(byteCodes);
        new ControlTransferOptimizer().optimize(code);

        assertEquals(4, code.getByteCodes().size());
        assertEquals(code0, code.getByteCodes().get(0));
        assertEquals(code1, code.getByteCodes().get(1));
        assertEquals(code2, code.getByteCodes().get(2));
        assertEquals(code3, code.getByteCodes().get(3));

        /*
         * 测试cconst的序列化
         */
        assertEquals(code0, byteCodes.get(0));
        assertEquals(code1, byteCodes.get(1));
        assertEquals(code2, byteCodes.get(2));
        assertEquals(code3, byteCodes.get(3));
        ExpressionCode expressionCode = new ExpressionCode(byteCodes);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ExpressionOutputStream outputStream = new ExpressionOutputStream(byteArrayOutputStream);
        outputStream.writeExpressionCode(expressionCode);

        ExpressionInputStream inputStream = new ExpressionInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        ExpressionCode loadedExpressionCode = inputStream.readExpressionCode();

        assertEquals(expressionCode.getByteCodes().size(), loadedExpressionCode.getByteCodes().size());
        for (int i = 0; i < expressionCode.getByteCodes().size(); i++) {
            assertEquals(expressionCode.getByteCodes().get(i).toString(), loadedExpressionCode.getByteCodes().get(i).toString());
        }
    }
}
