package org.liuyehcf.compile.engine.hua.compiler;

import org.liuyehcf.compile.engine.hua.bytecode.cf.ControlTransfer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlu
 * @date 2018/6/13
 */
public class StatusInfo {

    private final List<ControlTransfer> uncertainCodes = new ArrayList<>();
    private int ifNestedLevel = 0;

    public List<ControlTransfer> getUncertainCodes() {
        return uncertainCodes;
    }

    public void enterConditionStatement() {
        ifNestedLevel++;
    }

    public void exitConditionStatement() {
        ifNestedLevel--;
    }

    public int getIfNestedLevel() {
        return ifNestedLevel;
    }
}
