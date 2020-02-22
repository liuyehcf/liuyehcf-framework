package com.github.liuyehcf.framework.expression.engine.compile.optimize.impl;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.compile.engine.utils.Pair;
import com.github.liuyehcf.framework.expression.engine.compile.optimize.Optimizer;
import com.github.liuyehcf.framework.expression.engine.core.ExpressionCode;
import com.github.liuyehcf.framework.expression.engine.core.ExpressionException;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.ByteCode;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.cf.ControlTransfer;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.cp.Compute;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.sl.*;
import com.github.liuyehcf.framework.expression.engine.core.model.ComparableValue;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hechenfeng
 * @date 2018/9/30
 */
public class ConstantExpressionOptimizer implements Optimizer {
    @Override
    public void optimize(final ExpressionCode expressionCode) {
        /*
         * 将指令序列分解成基本块（Basic Block）
         * 基本块是满足下列条件的最大的连续三地址指令序列
         * 1. 控制流只能从基本块的第一个指令进入该块，换言之，没有跳转到基本块中间或末尾指令的转移指令
         * 2. 除了基本块的最后一个指令，控制流在离开基本块之前不会跳转
         *
         * 首指令
         * 1. 指令序列的第一个三地址指令是一个首指令
         * 2. 任意一个条件或无条件转移指令的目标是一个首指令
         * 3. 紧跟在一个条件或无条件转移指令之后的指令是一个首指令
         */
        List<BasicBlock> basicBlocks = splitCodesToBasicBlocks(expressionCode);

        /*
         * 对于每个基本块的常量部分，直接计算其值，优化性能
         */
        for (BasicBlock basicBlock : basicBlocks) {
            constantOptimization(basicBlock);
        }

        /*
         * 将基本块还原为指令序列
         */
        List<ByteCode> optimizedCodes = joinBasicBlocksToByteCodes(basicBlocks);

        expressionCode.setByteCodes(optimizedCodes);
    }

    private List<BasicBlock> splitCodesToBasicBlocks(final ExpressionCode expressionCode) {
        List<Integer> startCodeOffsets = Lists.newArrayList();

        /*
         * 1. 指令序列的第一个三地址指令是一个首指令
         */
        startCodeOffsets.add(0);

        for (int i = 0; i < expressionCode.getByteCodes().size(); i++) {
            ByteCode byteCode = expressionCode.getByteCodes().get(i);
            if (byteCode instanceof ControlTransfer) {
                /*
                 * 2. 任意一个条件或无条件转移指令的目标是一个首指令
                 */
                int codeOffset = ((ControlTransfer) byteCode).getCodeOffset();
                startCodeOffsets.add(codeOffset);

                /*
                 * 3. 紧跟在一个条件或无条件转移指令之后的指令是一个首指令
                 */
                if (i + 1 < expressionCode.getByteCodes().size()) {
                    startCodeOffsets.add(i + 1);
                }
            }
        }

        startCodeOffsets = startCodeOffsets.stream().distinct().collect(Collectors.toList());
        Collections.sort(startCodeOffsets);

        /*
         * 将转移指令的偏移量暂时改为blockId
         */
        for (int i = 0; i < expressionCode.getByteCodes().size(); i++) {
            ByteCode byteCode = expressionCode.getByteCodes().get(i);
            if (byteCode instanceof ControlTransfer) {
                int codeOffset = ((ControlTransfer) byteCode).getCodeOffset();
                Assert.assertFalse(codeOffset == -1);
                int blockId = startCodeOffsets.indexOf(codeOffset);
                ((ControlTransfer) byteCode).setCodeOffset(blockId);
            }
        }

        /*
         * 依据首指令集合，将指令序列拆分成BasicBlock
         */
        int blockIdCnt = 0;
        if (startCodeOffsets.size() == 1) {
            return Lists.newArrayList(new BasicBlock(blockIdCnt, expressionCode.getByteCodes()));
        }

        List<BasicBlock> basicBlocks = Lists.newArrayList();
        int startCodeOffsetOfNextBlock = startCodeOffsets.get(blockIdCnt + 1);
        for (int i = 0; i < expressionCode.getByteCodes().size(); ) {
            List<ByteCode> byteCodesOfCurrentBlock = Lists.newArrayList();
            while (i < startCodeOffsetOfNextBlock) {
                byteCodesOfCurrentBlock.add(expressionCode.getByteCodes().get(i));
                i++;
            }
            basicBlocks.add(new BasicBlock(blockIdCnt, byteCodesOfCurrentBlock));
            blockIdCnt++;

            if (blockIdCnt + 1 < startCodeOffsets.size()) {
                startCodeOffsetOfNextBlock = startCodeOffsets.get(blockIdCnt + 1);
            } else {
                startCodeOffsetOfNextBlock = expressionCode.getByteCodes().size();
            }
        }
        return basicBlocks;
    }

    private void constantOptimization(final BasicBlock basicBlock) {
        final int size = basicBlock.getByteCodes().size();

        final LinkedList<ByteCode> optimizedByteCodes = new LinkedList<>();

        int i = 0;
        while (i < size) {

            /*
             * 非常量指令
             */
            ByteCode byteCode;
            if (!isConstantByteCode(byteCode = basicBlock.getByteCodes().get(i))) {
                optimizedByteCodes.add(byteCode);
                i++;
                continue;
            }

            /*
             * 取出连续常量指令序列
             */
            LinkedList<ByteCode> constantByteCodes = Lists.newLinkedList();
            while (i < size && isConstantByteCode(byteCode = basicBlock.getByteCodes().get(i))) {
                constantByteCodes.add(byteCode);
                i++;
            }

            /*
             * 下面开始计算常量部分，从后往前以，借助堆栈进行计算
             * stack是计算栈，pair.first=Compute, pair.second=List<Const>
             */
            LinkedList<Pair<Compute, LinkedList<Const>>> stack = Lists.newLinkedList();
            ListIterator<ByteCode> iterator = constantByteCodes.listIterator(constantByteCodes.size());
            while (iterator.hasPrevious()) {
                ByteCode constantByteCode = iterator.previous();
                Pair<Compute, LinkedList<Const>> peek = stack.peek();
                if (constantByteCode instanceof Const) {
                    if (peek == null) {
                        Pair<Compute, LinkedList<Const>> pair = new Pair<>(null, Lists.newLinkedList());
                        pair.getSecond().addFirst((Const) constantByteCode);
                        stack.push(pair);
                        continue;
                    }

                    Compute computeByteCode = peek.getFirst();
                    LinkedList<Const> constByteCodes = peek.getSecond();
                    constByteCodes.addFirst((Const) constantByteCode);

                    boolean hasEnoughOperators;
                    if (computeByteCode == null) {
                        hasEnoughOperators = false;
                    } else {
                        final int stackOperandNum = computeByteCode.getStackOperandNum();
                        Assert.assertFalse(constByteCodes.size() > stackOperandNum);
                        hasEnoughOperators = constByteCodes.size() == stackOperandNum;
                    }

                    if (!hasEnoughOperators) {
                        continue;
                    }

                    /*
                     * 计算当前运算式
                     */
                    LinkedList<ByteCode> byteCodesToBeCalculated = Lists.newLinkedList();
                    byteCodesToBeCalculated.addAll(constByteCodes);
                    byteCodesToBeCalculated.add(computeByteCode);
                    stack.pop();

                    Object result = new ExpressionCode(byteCodesToBeCalculated).execute();
                    Const resultConstByteCode;
                    if (result == null) {
                        resultConstByteCode = new _nconst();
                    } else if (result instanceof Boolean) {
                        resultConstByteCode = new _bconst((boolean) result);
                    } else if (result instanceof ComparableValue) {
                        resultConstByteCode = new _cconst(((ComparableValue) result).getValue());
                    } else if (result instanceof Long) {
                        resultConstByteCode = new _lconst((long) result);
                    } else if (result instanceof Double) {
                        resultConstByteCode = new _dconst((double) result);
                    } else if (result instanceof String) {
                        resultConstByteCode = new _sconst((String) result);
                    } else {
                        throw new ExpressionException("unexpected constantExpressionResult='" + result.getClass().getName() + "'");
                    }

                    /*
                     * 注意，这里会改变constantByteCodes
                     */
                    iterator.add(resultConstByteCode);
                } else if (constantByteCode instanceof Compute) {
                    stack.push(new Pair<>((Compute) constantByteCode, Lists.newLinkedList()));
                } else {
                    throw new ExpressionException("unexpected constantByteCode='" + (constantByteCode == null ? null : constantByteCode.getClass().getName()) + "'");
                }
            }

            /*
             * 将栈中无法计算的剩余指令返还
             */
            while (!stack.isEmpty()) {
                Pair<Compute, LinkedList<Const>> pair = stack.pop();
                optimizedByteCodes.addAll(pair.getSecond());
                if (pair.getFirst() != null) {
                    optimizedByteCodes.add(pair.getFirst());
                }
            }
        }

        basicBlock.setByteCodes(optimizedByteCodes);
    }

    private boolean isConstantByteCode(final ByteCode byteCode) {
        return byteCode instanceof Compute
                || byteCode instanceof Const;
    }

    private List<ByteCode> joinBasicBlocksToByteCodes(final List<BasicBlock> basicBlocks) {

        List<ByteCode> joinedByteCode = Lists.newArrayList();

        int codeOffset = 0;
        /*
         * from blockId -> codeOffset
         */
        Map<Integer, Integer> offsetMap = Maps.newHashMap();
        for (BasicBlock basicBlock : basicBlocks) {
            offsetMap.put(basicBlock.getId(), codeOffset);
            codeOffset += basicBlock.getByteCodes().size();
            joinedByteCode.addAll(basicBlock.getByteCodes());
        }

        for (ByteCode byteCode : joinedByteCode) {
            if (byteCode instanceof ControlTransfer) {
                int blockId = ((ControlTransfer) byteCode).getCodeOffset();
                Assert.assertTrue(offsetMap.containsKey(blockId));
                ((ControlTransfer) byteCode).setCodeOffset(offsetMap.get(blockId));
            }
        }

        return joinedByteCode;
    }

    private static final class BasicBlock {

        /**
         * 基本块id
         */
        private final int id;

        /**
         * 连续指令序列
         */
        private List<ByteCode> byteCodes;

        private BasicBlock(int id, List<ByteCode> byteCodes) {
            this.id = id;
            this.byteCodes = byteCodes;
        }

        private int getId() {
            return id;
        }

        private List<ByteCode> getByteCodes() {
            return byteCodes;
        }

        private void setByteCodes(List<ByteCode> byteCodes) {
            this.byteCodes = byteCodes;
        }
    }
}
