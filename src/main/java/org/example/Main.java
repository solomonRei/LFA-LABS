package org.example;

import java.util.HashSet;
import java.util.Set;

import org.example.fa.impl.DFiniteAutomaton;
import org.example.fa.impl.NDFiniteAutomaton;
import org.example.utils.GraphUtils;

public class Main {
  public static void main(String[] args) {
    Set<Character> sigma = new HashSet<>();
    sigma.add('a');
    sigma.add('b');
    NDFiniteAutomaton nfa = new NDFiniteAutomaton(sigma);

    // q0 = S, q1 = A, q2 = B, q3 = C
    nfa.addState("S", false);
    nfa.addState("A", false);
    nfa.addState("B", true);
    nfa.addState("C", false);

    nfa.setStartState("S");

    nfa.addTransition("S", 'a', "S");
    nfa.addTransition("S", 'a', "A");
    nfa.addTransition("A", 'a', "B");
    nfa.addTransition("A", 'b', "A");
    nfa.addTransition("B", 'a', "C");
    nfa.addTransition("C", 'a', "A");

    var gr = nfa.toRegularGrammar();
    System.out.println(gr);
    for (int i = 0; i < 5; i++) {
      gr.generateStringWithProgression();
    }

    boolean isDFA = nfa.isDeterministic();
    System.out.println("The FA is " + (isDFA ? "deterministic." : "non-deterministic."));

    DFiniteAutomaton dfa = nfa.convertToDFA();
    System.out.println(dfa);

    GraphUtils.generateGraph(dfa, "C:/Users/tlz27/IdeaProjects/LFA/src/main/resources/fa.png");
    GraphUtils.generateNfaGraph(nfa, "C:/Users/tlz27/IdeaProjects/LFA/src/main/resources/nfa.png");
  }
}

// Variant 25
//        Q = {q0,q1,q2,q3},
//        ∑ = {a,b},
//        F = {q2},
//        δ(q0,a) = q0,
//        δ(q0,a) = q1,
//        δ(q1,a) = q2,
//        δ(q1,b) = q1,
//        δ(q2,a) = q3,
//        δ(q3,a) = q1.

