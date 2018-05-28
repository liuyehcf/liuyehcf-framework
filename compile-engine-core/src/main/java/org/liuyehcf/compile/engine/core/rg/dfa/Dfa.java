package org.liuyehcf.compile.engine.core.rg.dfa;

import org.liuyehcf.compile.engine.core.GrammarHolder;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.rg.Matcher;
import org.liuyehcf.compile.engine.core.rg.Pattern;
import org.liuyehcf.compile.engine.core.rg.nfa.Nfa;
import org.liuyehcf.compile.engine.core.rg.nfa.NfaClosure;
import org.liuyehcf.compile.engine.core.rg.nfa.NfaState;
import org.liuyehcf.compile.engine.core.utils.AssertUtils;

import java.util.*;

/**
 * Dfa自动机
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class Dfa implements Pattern, GrammarHolder {

    /**
     * Nfa自动机
     */
    private final Nfa nfa;

    /**
     * 起始Dfa节点
     */
    private DfaState startDfaState;

    public Dfa(Nfa nfa) {
        this.nfa = nfa;
        init();
    }

    private void init() {
        startDfaState = Transfer.getStartDfaStateFromNfaClosure(nfa.getNfaClosure());
    }

    DfaState getStartDfaState() {
        return startDfaState;
    }

    @Override
    public Grammar getGrammar() {
        return nfa.getGrammar();
    }

    @Override
    public Matcher matcher(String input) {
        return new DfaMatcher(this, input);
    }

    public void print() {
        AssertUtils.assertNotNull(startDfaState);
        startDfaState.print();
    }

    private static class Transfer {
        private final NfaClosure nfaClosure;
        private DfaState startDfaState;

        private Map<DfaStateDescription, DfaState> dfaStatesMap = new HashMap<>();
        private Set<DfaState> markedDfaStates = new HashSet<>();
        private Set<DfaState> unMarkedDfaStates = new HashSet<>();

        private Transfer(NfaClosure nfaClosure) {
            this.nfaClosure = nfaClosure;
            init();
        }

        private static DfaState getStartDfaStateFromNfaClosure(NfaClosure nfaClosure) {
            return new Transfer(nfaClosure).getStartDfaState();
        }

        private DfaState getStartDfaState() {
            return startDfaState;
        }

        private void init() {
            addFirstUnMarkedDfaState(
                    DfaState.createDfaStateWithNfaStates(
                            Collections.singletonList(nfaClosure.getStartNfaState())));

            DfaState curDfaState;
            while ((curDfaState = getUnMarkedDfaState()) != null) {

                markDfaState(curDfaState);

                for (Symbol inputSymbol : curDfaState.getAllInputSymbols()) {
                    List<NfaState> nextNfaStates = curDfaState.getNextNfaStatesWithInputSymbol(inputSymbol);

                    DfaState nextDfaState = DfaState.createDfaStateWithNfaStates(nextNfaStates);

                    DfaStateDescription nextDfaStateDescription = nextDfaState.getDescription();
                    if (!dfaStatesMap.containsKey(nextDfaStateDescription)) {
                        addUnMarkedDfaState(nextDfaState);
                    } else {
                        nextDfaState = dfaStatesMap.get(nextDfaStateDescription);
                    }

                    curDfaState.addInputSymbolAndNextDfaState(inputSymbol, nextDfaState);
                }
            }
        }

        private void addFirstUnMarkedDfaState(DfaState dfaState) {
            startDfaState = dfaState;
            addUnMarkedDfaState(dfaState);
        }

        private void addUnMarkedDfaState(DfaState dfaState) {
            AssertUtils.assertFalse(dfaState.isMarked());
            AssertUtils.assertFalse(dfaStatesMap.containsKey(dfaState.getDescription()));
            dfaStatesMap.put(dfaState.getDescription(), dfaState);
            AssertUtils.assertFalse(unMarkedDfaStates.contains(dfaState));
            unMarkedDfaStates.add(dfaState);
        }

        private DfaState getUnMarkedDfaState() {
            Iterator<DfaState> it = unMarkedDfaStates.iterator();

            DfaState dfaState = null;
            if (it.hasNext()) {
                dfaState = it.next();
                AssertUtils.assertFalse(dfaState.isMarked());
            }
            return dfaState;
        }

        private void markDfaState(DfaState dfaState) {
            AssertUtils.assertFalse(dfaState.isMarked());
            dfaState.setMarked();
            AssertUtils.assertTrue(unMarkedDfaStates.contains(dfaState));
            unMarkedDfaStates.remove(dfaState);
            AssertUtils.assertFalse(markedDfaStates.contains(dfaState));
            markedDfaStates.add(dfaState);
        }
    }
}
