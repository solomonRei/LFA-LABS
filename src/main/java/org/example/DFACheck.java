package org.example;

import java.util.HashSet;
import java.util.Set;

import org.example.fa.impl.DFiniteAutomaton;
import org.example.fa.impl.NDFiniteAutomaton;
import org.example.utils.GraphUtils;

public class DFACheck {
  public static void main(String[] args) {
    Set<Character> sigma = new HashSet<>();
    sigma.add('a');
    sigma.add('b');
    NDFiniteAutomaton nfa = new NDFiniteAutomaton(sigma);

    nfa.addState("S", true);
    nfa.addState("A", false);

    nfa.setStartState("S");

    nfa.addTransition("S", 'a', "A");
    nfa.addTransition("S", 'b', "S");
    nfa.addTransition("A", 'a', "S");
    nfa.addTransition("A", 'b', "A");

    var gr = nfa.toRegularGrammar();
    System.out.println(gr);
    boolean isDFA = nfa.isDeterministic();
    System.out.println("The FA is " + (isDFA ? "deterministic." : "non-deterministic."));

    DFiniteAutomaton dfa = nfa.convertToDFA();
    System.out.println(dfa);
    GraphUtils.generateGraph(dfa, "C:/Users/tlz27/IdeaProjects/LFA/src/main/resources/fa2.png");
  }
}
