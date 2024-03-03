package org.example.fa.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.example.Grammar;
import org.example.fa.FiniteAutomation;

/** Represents a non-deterministic finite automaton. */
public class NDFiniteAutomaton implements FiniteAutomation {

  // Override the transitions map to accommodate NFAs
  private final Map<String, Map<Character, Set<String>>> nfaTransitions;
  private final Set<String> states;
  private final Set<Character> sigma;

  private String startState;

  private final Set<String> acceptStates;

  public NDFiniteAutomaton(Set<Character> sigma) {
    this.nfaTransitions = new HashMap<>();
    this.states = new HashSet<>();
    this.sigma = sigma;
    this.acceptStates = new HashSet<>();
  }

  /**
   * Sets the start state of the finite automaton.
   *
   * @param startState the start state
   */
  @Override
  public void setStartState(String startState) {
    this.startState = startState;
    states.add(startState);
  }

  // Override methods to work with the NFA structure
  @Override
  public void addState(final String state, final boolean isAcceptState) {
    states.add(state);
    if (isAcceptState) {
      acceptStates.add(state);
    }
    nfaTransitions.putIfAbsent(state, new HashMap<>());
  }

  /**
   * Adds a transition to the NFA.
   *
   * @param fromState the state from which the transition starts
   * @param input the input symbol
   * @param toState the state to which the transition can go
   */
  public void addTransition(final String fromState, final char input, final String toState) {
    nfaTransitions.putIfAbsent(fromState, new HashMap<>());
    nfaTransitions.get(fromState).putIfAbsent(input, new HashSet<>());
    nfaTransitions.get(fromState).get(input).add(toState);
  }

  @Override
  public boolean isStringAccepted(String input) {
    return false;
  }

  @Override
  public Grammar toRegularGrammar() {
    List<Character> Vn = new ArrayList<>();
    List<Character> Vt = new ArrayList<>(sigma);
    Map<Character, List<String>> productions = new HashMap<>();
    char S = startState.charAt(0);

    for (String state : states) {
      Vn.add(state.charAt(0));
      productions.putIfAbsent(state.charAt(0), new ArrayList<>());
    }

    for (Map.Entry<String, Map<Character, Set<String>>> transitionEntry :
        nfaTransitions.entrySet()) {
      char fromState = transitionEntry.getKey().charAt(0);
      for (Map.Entry<Character, Set<String>> symbolEntry : transitionEntry.getValue().entrySet()) {
        char symbol = symbolEntry.getKey();
        for (String toStateStr : symbolEntry.getValue()) {
          char toState = toStateStr.charAt(0);

          productions.get(fromState).add(symbol + "" + toState);

          if (acceptStates.contains(toStateStr)) {
            productions.get(fromState).add(String.valueOf(symbol));
          }
        }
      }
    }

    return new Grammar(Vn, Vt, productions, S);
  }

  /**
   * Checks if the finite automaton is deterministic.
   *
   * @return true if the finite automaton is deterministic, false otherwise
   */
  public boolean isDeterministic() {
    for (Map.Entry<String, Map<Character, Set<String>>> stateTransitions :
        nfaTransitions.entrySet()) {
      Map<Character, Set<String>> transitions = stateTransitions.getValue();
      for (Set<String> destinations : transitions.values()) {
        if (destinations.size() > 1) {
          return false;
        }
      }
    }
    return true;
  }
}
