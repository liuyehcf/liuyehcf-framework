package org.liuyehcf.compile.engine.expression.test.optimizer;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.liuyehcf.compile.engine.expression.compile.optimize.impl.ConstantExpressionOptimizer;
import org.liuyehcf.compile.engine.expression.core.ExpressionCode;
import org.liuyehcf.compile.engine.expression.core.bytecode.ByteCode;
import org.liuyehcf.compile.engine.expression.core.bytecode.cf._ifeq;
import org.liuyehcf.compile.engine.expression.core.bytecode.cp.*;
import org.liuyehcf.compile.engine.expression.core.bytecode.ir._invokestatic;
import org.liuyehcf.compile.engine.expression.core.bytecode.ir._return;
import org.liuyehcf.compile.engine.expression.core.bytecode.sl.*;
import org.liuyehcf.compile.engine.expression.test.TestBase;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author hechenfeng
 * @date 2018/10/1
 */
public class TestConstantExpressionOptimizer extends TestBase {
    @Test
    public void case1() {
        List<ByteCode> byteCodes = Lists.newArrayList();
        ByteCode code0 = new _lconst(1L);
        ByteCode code1 = new _lconst(2L);
        ByteCode code2 = new _bconst(true);
        ByteCode code3 = new _dconst(1D);
        ByteCode code4 = new _return();

        byteCodes.add(code0);
        byteCodes.add(code1);
        byteCodes.add(code2);
        byteCodes.add(code3);
        byteCodes.add(code4);

        ExpressionCode code = new ExpressionCode(byteCodes);
        new ConstantExpressionOptimizer().optimize(code);

        assertEquals(5, code.getByteCodes().size());
        assertEquals(code0, code.getByteCodes().get(0));
        assertEquals(code1, code.getByteCodes().get(1));
        assertEquals(code2, code.getByteCodes().get(2));
        assertEquals(code3, code.getByteCodes().get(3));
        assertEquals(code4, code.getByteCodes().get(4));
    }

    @Test
    public void case2() {
        List<ByteCode> byteCodes = Lists.newArrayList();
        ByteCode code0 = new _lconst(1L);
        ByteCode code1 = new _lconst(2L);
        ByteCode code2 = new _bconst(true);
        ByteCode code3 = new _dconst(1D);
        ByteCode code4 = new _ifeq(4);
        ByteCode code5 = new _return();

        byteCodes.add(code0);
        byteCodes.add(code1);
        byteCodes.add(code2);
        byteCodes.add(code3);
        byteCodes.add(code4);
        byteCodes.add(code5);

        ExpressionCode code = new ExpressionCode(byteCodes);
        new ConstantExpressionOptimizer().optimize(code);

        assertEquals(6, code.getByteCodes().size());
        assertEquals(code0, code.getByteCodes().get(0));
        assertEquals(code1, code.getByteCodes().get(1));
        assertEquals(code2, code.getByteCodes().get(2));
        assertEquals(code3, code.getByteCodes().get(3));
        assertEquals(code4, code.getByteCodes().get(4));
        assertEquals(code5, code.getByteCodes().get(5));
    }

    @Test
    public void case3() {
        List<ByteCode> byteCodes = Lists.newArrayList();
        ByteCode code0 = new _lconst(1L);
        ByteCode code1 = new _lconst(2L);
        ByteCode code2 = new _and();
        ByteCode code3 = new _dconst(1D);
        ByteCode code4 = new _ifeq(5);
        ByteCode code5 = new _return();

        byteCodes.add(code0);
        byteCodes.add(code1);
        byteCodes.add(code2);
        byteCodes.add(code3);
        byteCodes.add(code4);
        byteCodes.add(code5);

        ExpressionCode code = new ExpressionCode(byteCodes);
        new ConstantExpressionOptimizer().optimize(code);

        assertEquals(4, code.getByteCodes().size());
        assertEquals(0L, ((_lconst) code.getByteCodes().get(0)).getValue());
        assertEquals(code3, code.getByteCodes().get(1));
        assertEquals(code4, code.getByteCodes().get(2));
        assertEquals(code5, code.getByteCodes().get(3));
    }

    @Test
    public void case4() {
        List<ByteCode> byteCodes = Lists.newArrayList();
        ByteCode code0 = new _lconst(1L);
        ByteCode code1 = new _lconst(2L);
        ByteCode code2 = new _add();
        ByteCode code3 = new _bconst(false);
        ByteCode code4 = new _dconst(5.6d);
        ByteCode code5 = new _dconst(3.3d);
        ByteCode code6 = new _sub();
        ByteCode code7 = new _dconst(1D);
        ByteCode code8 = new _ifeq(9);
        ByteCode code9 = new _return();

        byteCodes.add(code0);
        byteCodes.add(code1);
        byteCodes.add(code2);
        byteCodes.add(code3);
        byteCodes.add(code4);
        byteCodes.add(code5);
        byteCodes.add(code6);
        byteCodes.add(code7);
        byteCodes.add(code8);
        byteCodes.add(code9);

        ExpressionCode code = new ExpressionCode(byteCodes);
        new ConstantExpressionOptimizer().optimize(code);

        assertEquals(6, code.getByteCodes().size());
        assertEquals(3L, ((_lconst) code.getByteCodes().get(0)).getValue());
        assertEquals(code3, code.getByteCodes().get(1));
        assertEquals(2.3D, ((_dconst) code.getByteCodes().get(2)).getValue(), 1e-10);
        assertEquals(code7, code.getByteCodes().get(3));
        assertEquals(code8, code.getByteCodes().get(4));
        assertEquals(code9, code.getByteCodes().get(5));
    }

    @Test
    public void case5() {
        List<ByteCode> byteCodes = Lists.newArrayList();
        ByteCode code0 = new _lconst(1L);
        ByteCode code1 = new _lconst(2L);
        ByteCode code2 = new _add();
        ByteCode code3 = new _lconst(3L);
        ByteCode code4 = new _mul();
        ByteCode code5 = new _lconst(4L);
        ByteCode code6 = new _sub();
        ByteCode code7 = new _return();

        byteCodes.add(code0);
        byteCodes.add(code1);
        byteCodes.add(code2);
        byteCodes.add(code3);
        byteCodes.add(code4);
        byteCodes.add(code5);
        byteCodes.add(code6);
        byteCodes.add(code7);

        ExpressionCode code = new ExpressionCode(byteCodes);
        new ConstantExpressionOptimizer().optimize(code);

        assertEquals(2, code.getByteCodes().size());
        assertEquals(5L, ((_lconst) code.getByteCodes().get(0)).getValue());
        assertEquals(code7, code.getByteCodes().get(1));
    }

    @Test
    public void case6() {
        List<ByteCode> byteCodes = Lists.newArrayList();
        ByteCode code0 = new _lconst(1L);
        ByteCode code1 = new _add();
        ByteCode code2 = new _return();

        byteCodes.add(code0);
        byteCodes.add(code1);
        byteCodes.add(code2);

        ExpressionCode code = new ExpressionCode(byteCodes);
        new ConstantExpressionOptimizer().optimize(code);

        assertEquals(3, code.getByteCodes().size());
        assertEquals(code0, code.getByteCodes().get(0));
        assertEquals(code1, code.getByteCodes().get(1));
        assertEquals(code2, code.getByteCodes().get(2));
    }

    @Test
    public void case7() {
        List<ByteCode> byteCodes = Lists.newArrayList();
        ByteCode code0 = new _lconst(1L);
        ByteCode code1 = new _lconst(1L);
        ByteCode code2 = new _neg();
        ByteCode code3 = new _return();

        byteCodes.add(code0);
        byteCodes.add(code1);
        byteCodes.add(code2);
        byteCodes.add(code3);

        ExpressionCode code = new ExpressionCode(byteCodes);
        new ConstantExpressionOptimizer().optimize(code);

        assertEquals(3, code.getByteCodes().size());
        assertEquals(code0, code.getByteCodes().get(0));
        assertEquals(-1L, ((_lconst) code.getByteCodes().get(1)).getValue());
        assertEquals(code3, code.getByteCodes().get(2));
    }

    @Test
    public void case8() {
        List<ByteCode> byteCodes = Lists.newArrayList();
        ByteCode code0 = new _sconst("a");
        ByteCode code1 = new _sconst("b");
        ByteCode code2 = new _sconst("c");
        ByteCode code3 = new _add();
        ByteCode code4 = new _return();

        byteCodes.add(code0);
        byteCodes.add(code1);
        byteCodes.add(code2);
        byteCodes.add(code3);
        byteCodes.add(code4);

        ExpressionCode code = new ExpressionCode(byteCodes);
        new ConstantExpressionOptimizer().optimize(code);

        assertEquals(3, code.getByteCodes().size());
        assertEquals(code0, code.getByteCodes().get(0));
        assertEquals("bc", ((_sconst) code.getByteCodes().get(1)).getValue());
        assertEquals(code4, code.getByteCodes().get(2));
    }

    @Test
    public void case9() {
        List<ByteCode> byteCodes = Lists.newArrayList();
        ByteCode code0 = new _sconst("a");
        ByteCode code1 = new _sconst("b");
        ByteCode code2 = new _sconst("c");
        ByteCode code3 = new _add();
        ByteCode code4 = new _pload("a.b.c");
        ByteCode code5 = new _sconst("a");
        ByteCode code6 = new _add();
        ByteCode code7 = new _lconst(1L);
        ByteCode code8 = new _dconst(2.3d);
        ByteCode code9 = new _dconst(1.d);
        ByteCode code10 = new _sub();
        ByteCode code11 = new _invokestatic("collection.include", 2);
        ByteCode code12 = new _dconst(2.3d);
        ByteCode code13 = new _dconst(1.d);
        ByteCode code14 = new _mul();
        ByteCode code15 = new _return();

        byteCodes.add(code0);
        byteCodes.add(code1);
        byteCodes.add(code2);
        byteCodes.add(code3);
        byteCodes.add(code4);
        byteCodes.add(code5);
        byteCodes.add(code6);
        byteCodes.add(code7);
        byteCodes.add(code8);
        byteCodes.add(code9);
        byteCodes.add(code10);
        byteCodes.add(code11);
        byteCodes.add(code12);
        byteCodes.add(code13);
        byteCodes.add(code14);
        byteCodes.add(code15);

        ExpressionCode code = new ExpressionCode(byteCodes);
        new ConstantExpressionOptimizer().optimize(code);

        assertEquals(10, code.getByteCodes().size());
        assertEquals(code0, code.getByteCodes().get(0));
        assertEquals("bc", ((_sconst) code.getByteCodes().get(1)).getValue());
        assertEquals(code4, code.getByteCodes().get(2));
        assertEquals(code5, code.getByteCodes().get(3));
        assertEquals(code6, code.getByteCodes().get(4));
        assertEquals(code7, code.getByteCodes().get(5));
        assertEquals(1.3d, ((_dconst) code.getByteCodes().get(6)).getValue(), 1e-10);
        assertEquals(code11, code.getByteCodes().get(7));
        assertEquals(2.3d, ((_dconst) code.getByteCodes().get(8)).getValue(), 1e-10);
        assertEquals(code15, code.getByteCodes().get(9));
    }
}
