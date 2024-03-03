package org.example;

import org.example.fa.impl.NDFiniteAutomaton;

import java.util.HashSet;
import java.util.Set;

public class Main2 {
  public static void main(String[] args) {
    Set<Character> sigma = new HashSet<>();
    sigma.add('a');
    sigma.add('b');
    NDFiniteAutomaton nfa = new NDFiniteAutomaton(sigma);

    //q0 = S, q1 = A, q2 = B, q3 = C
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

    System.out.println(nfa.toRegularGrammar());

    boolean isDFA = nfa.isDeterministic();
    System.out.println("The FA is " + (isDFA ? "deterministic." : "non-deterministic."));

  }
}
