package org.example.fa;

import org.example.Grammar;

public interface FiniteAutomation {
    void setStartState(final String startState);
    void addState(final String state, final boolean isAcceptState);
    void addTransition(final String fromState, final char input, final String toState);

    boolean isStringAccepted(final String input);

    Grammar toRegularGrammar();
}
