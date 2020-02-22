package com.github.liuyehcf.framework.expression.engine.compile.optimize.impl;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.expression.engine.compile.optimize.Optimizer;
import com.github.liuyehcf.framework.expression.engine.core.ExpressionCode;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.ByteCode;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.cf.*;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.sl._bconst;
import com.github.liuyehcf.framework.expression.engine.core.bytecode.sl._cconst;
import com.github.liuyehcf.framework.expression.engine.core.model.ComparableValue;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;

/**
 * @author hechenfeng
 * @date 2018/9/30
 */
public class ControlTransferOptimizer implements Optimizer {
    @Override
    public void optimize(ExpressionCode expressionCode) {
        List<ByteCode> byteCodes = expressionCode.getByteCodes();

        boolean canBreak = false;
        while (!canBreak) {
            final int originSize = byteCodes.size();

            /*
             * 对于常量跳转语句，直接改为goto，或者删除跳转
             */
            removeConstantControlTransferCode(byteCodes);

            /*
             * 将多级跳转指令改成一个跳转指令
             */
            simplifyControlTransferCode(byteCodes);

            /*
             * 删除多余的跳转指令（不可达）
             */
            removeRedundantControlTransferCode(byteCodes);

            /*
             * 移除单链路上的所有goto
             */
            removeSinglePathGotoControlTransferCode(byteCodes);

            /*
             * 上面3个方法会将一些不可达的字节码填充成null，这里清除，同时维护跳转指令的跳转偏移量
             */
            removeNullByteCode(byteCodes);

            Assert.assertFalse(byteCodes.size() > originSize);
            canBreak = byteCodes.size() == originSize;
        }
    }

    private void removeConstantControlTransferCode(final List<ByteCode> byteCodes) {
        boolean canBreak = false;

        while (!canBreak) {
            canBreak = true;

            Set<Integer> toCodeOffset = Sets.newHashSet();
            for (ByteCode byteCode : byteCodes) {
                if (byteCode instanceof ControlTransfer) {
                    toCodeOffset.add(((ControlTransfer) byteCode).getCodeOffset());
                }
            }

            for (int offset = 1; offset < byteCodes.size(); offset++) {
                ByteCode preByteCode = byteCodes.get(offset - 1);
                ByteCode byteCode = byteCodes.get(offset);

                /*
                 * 若果这两个指令作为其他跳转指令的目标，那么不允许优化
                 */
                if (toCodeOffset.contains(offset - 1) || toCodeOffset.contains(offset)) {
                    continue;
                }

                if (preByteCode instanceof _bconst || preByteCode instanceof _cconst) {
                    ComparableValue comparableValue;
                    if (preByteCode instanceof _bconst) {
                        comparableValue = ((_bconst) preByteCode).getComparableValue();
                    } else {
                        comparableValue = ((_cconst) preByteCode).getComparableValue();
                    }

                    if (byteCode instanceof _ifeq) {
                        if (comparableValue.getValue() == 0) {
                            byteCodes.set(offset - 1, null);
                            byteCodes.set(offset, new _goto(((_ifeq) byteCode).getCodeOffset()));
                        } else {
                            byteCodes.set(offset - 1, null);
                            byteCodes.set(offset, null);
                        }
                        canBreak = false;
                    } else if (byteCode instanceof _ifge) {
                        if (comparableValue.getValue() >= 0) {
                            byteCodes.set(offset - 1, null);
                            byteCodes.set(offset, new _goto(((_ifge) byteCode).getCodeOffset()));
                        } else {
                            byteCodes.set(offset - 1, null);
                            byteCodes.set(offset, null);
                        }
                        canBreak = false;
                    } else if (byteCode instanceof _ifgt) {
                        if (comparableValue.getValue() > 0) {
                            byteCodes.set(offset - 1, null);
                            byteCodes.set(offset, new _goto(((_ifgt) byteCode).getCodeOffset()));
                        } else {
                            byteCodes.set(offset - 1, null);
                            byteCodes.set(offset, null);
                        }
                        canBreak = false;
                    } else if (byteCode instanceof _ifle) {
                        if (comparableValue.getValue() <= 0) {
                            byteCodes.set(offset - 1, null);
                            byteCodes.set(offset, new _goto(((_ifle) byteCode).getCodeOffset()));
                        } else {
                            byteCodes.set(offset - 1, null);
                            byteCodes.set(offset, null);
                        }
                        canBreak = false;
                    } else if (byteCode instanceof _iflt) {
                        if (comparableValue.getValue() < 0) {
                            byteCodes.set(offset - 1, null);
                            byteCodes.set(offset, new _goto(((_iflt) byteCode).getCodeOffset()));
                        } else {
                            byteCodes.set(offset - 1, null);
                            byteCodes.set(offset, null);
                        }
                        canBreak = false;
                    } else if (byteCode instanceof _ifne) {
                        if (comparableValue.getValue() != 0) {
                            byteCodes.set(offset - 1, null);
                            byteCodes.set(offset, new _goto(((_ifne) byteCode).getCodeOffset()));
                        } else {
                            byteCodes.set(offset - 1, null);
                            byteCodes.set(offset, null);
                        }
                        canBreak = false;
                    }
                }
            }
        }
    }

    private void simplifyControlTransferCode(final List<ByteCode> byteCodes) {
        for (ByteCode byteCode : byteCodes) {
            if (!(byteCode instanceof ControlTransfer)) {
                continue;
            }

            ControlTransfer controlTransferCode = (ControlTransfer) byteCode;

            int codeOffset = controlTransferCode.getCodeOffset();

            codeOffset = getFinalCodeOffset(byteCodes, codeOffset);

            controlTransferCode.setCodeOffset(codeOffset);
        }
    }

    private void removeRedundantControlTransferCode(final List<ByteCode> byteCodes) {
        Set<Integer> visitedCodes = new HashSet<>();

        visitCode(0, visitedCodes, byteCodes);

        List<Integer> unvisitedCodeOffsets = new ArrayList<>();
        for (int offset = 0; offset < byteCodes.size(); offset++) {
            unvisitedCodeOffsets.add(offset);
        }
        unvisitedCodeOffsets.removeAll(visitedCodes);

        Collections.sort(unvisitedCodeOffsets);

        for (int unvisitedCodeOffset : unvisitedCodeOffsets) {
            byteCodes.set(unvisitedCodeOffset, null);
        }
    }

    private void removeSinglePathGotoControlTransferCode(final List<ByteCode> byteCodes) {
        for (int offset = 0; offset < byteCodes.size(); offset++) {
            ByteCode byteCode = byteCodes.get(offset);

            /*
             * 若为goto指令
             */
            if (byteCode instanceof _goto) {
                int toOffset = ((_goto) byteCode).getCodeOffset();

                for (int i = offset + 1; i < toOffset; i++) {
                    /*
                     * 中间必然是不可达指令序列
                     */
                    Assert.assertNull(byteCodes.get(i));
                }

                /*
                 * 将goto指令置空
                 * 只有往后跳的goto可以清除
                 */
                if (toOffset > offset) {
                    byteCodes.set(offset, null);
                }
            }
            /*
             * 非goto的其他指令
             */
            else if (byteCode instanceof ConditionalControlTransfer) {
                return;
            }
        }
    }

    private void removeNullByteCode(final List<ByteCode> byteCodes) {
        /*
         * 断言：跳转指令的跳转目标一定非空
         */
        for (ByteCode byteCode : byteCodes) {
            if (byteCode instanceof ControlTransfer) {
                int codeOffset = ((ControlTransfer) byteCode).getCodeOffset();
                Assert.assertNotNull(byteCodes.get(codeOffset));
            }
        }

        /*
         * 移除为null的byteCode
         */
        Map<ControlTransfer, Integer> controlTransferToCodeOffsetMap = Maps.newHashMap();
        for (ByteCode byteCode : byteCodes) {
            if (byteCode instanceof ControlTransfer) {
                controlTransferToCodeOffsetMap.put((ControlTransfer) byteCode, ((ControlTransfer) byteCode).getCodeOffset());
            }
        }

        List<ByteCode> copy = Lists.newArrayList(byteCodes);
        byteCodes.clear();

        for (int offset = 0; offset < copy.size(); offset++) {
            ByteCode byteCode = copy.get(offset);
            if (byteCode == null) {
                for (Map.Entry<ControlTransfer, Integer> entry : controlTransferToCodeOffsetMap.entrySet()) {
                    ControlTransfer controlTransfer = entry.getKey();
                    int toOffset = entry.getValue();
                    if (toOffset > offset) {
                        controlTransfer.setCodeOffset(controlTransfer.getCodeOffset() - 1);
                    }
                }
            } else {
                byteCodes.add(byteCode);
            }
        }
    }

    private int getFinalCodeOffset(final List<ByteCode> byteCodes, final int codeOffset) {
        int finalCodeOffset = codeOffset;

        Set<ByteCode> visitedCodes = new HashSet<>();

        ByteCode byteCode;
        while ((byteCode = byteCodes.get(finalCodeOffset)) instanceof _goto) {
            /*
             * 对于for(;;){}这样的循环，只有一个goto语句，且回到原点
             * 跳转链路的终点是一个goto self
             */
            if (!visitedCodes.add(byteCode)) {
                return finalCodeOffset;
            }
            finalCodeOffset = ((ControlTransfer) byteCode).getCodeOffset();
        }

        return finalCodeOffset;
    }

    private void visitCode(final int codeOffset, Set<Integer> visitedCodes, final List<ByteCode> byteCodes) {
        if (codeOffset >= byteCodes.size() || visitedCodes.contains(codeOffset)) {
            return;
        }

        visitedCodes.add(codeOffset);
        ByteCode byteCode = byteCodes.get(codeOffset);

        if (byteCode instanceof ControlTransfer) {
            visitCode(((ControlTransfer) byteCode).getCodeOffset(), visitedCodes, byteCodes);
            if (byteCode instanceof ConditionalControlTransfer) {
                visitCode(codeOffset + 1, visitedCodes, byteCodes);
            }
        } else {
            visitCode(codeOffset + 1, visitedCodes, byteCodes);
        }
    }
}
