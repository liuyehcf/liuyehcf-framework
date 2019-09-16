package com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message;

/**
 * @author hechenfeng
 * @date 2019/9/9
 */
public enum ElectionState {

    /**
     * first stage of election
     * in this stage, candidates will broadcast its transaction id and its unique member id
     */
    first,

    /**
     * if a majority of member accept first-state-election
     * then it will enter to the second-stage-election
     */
    second;

    public boolean isFirstState() {
        return first.equals(this);
    }
}
