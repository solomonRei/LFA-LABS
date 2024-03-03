package org.example;

import org.example.fa.impl.DFiniteAutomaton;
import org.example.fa.impl.NDFiniteAutomaton;
import org.example.utils.GraphUtils;

import java.util.Set;

public class ENFACheck {
    public static void main(String[] args) {
        NDFiniteAutomaton epsilonNFA = new NDFiniteAutomaton(Set.of('a', 'b'));
        epsilonNFA.setStartState("q0");
        epsilonNFA.addState("q0", false);
        epsilonNFA.addState("q1", false);
        epsilonNFA.addState("q2", true);

        epsilonNFA.addTransition("q0", 'ε', "q1");
        epsilonNFA.addTransition("q1", 'a', "q1");
        epsilonNFA.addTransition("q1", 'b', "q2");
        epsilonNFA.addTransition("q2", 'ε', "q0");

        DFiniteAutomaton dfa = epsilonNFA.convertEpsilonNfaToDfa();
        System.out.println(dfa);
        GraphUtils.generateGraph(dfa, "C:/Users/tlz27/IdeaProjects/LFA/src/main/resources/enfa.png");
    }
}
