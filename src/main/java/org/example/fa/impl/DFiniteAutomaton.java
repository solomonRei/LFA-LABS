package org.example.fa.impl;

import org.example.Grammar;
import org.example.fa.FiniteAutomation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Represents a Deterministic finite automaton. */
public class DFiniteAutomaton implements FiniteAutomation {

  private final Set<String> states;
  private final Set<Character> sigma;

  private final Map<String, Map<Character, String>> transitions;

  private String startState;

  private final Set<String> acceptStates;

  public DFiniteAutomaton(final Set<Character> sigma) {
    this.states = new HashSet<>();
    this.sigma = sigma;
    this.transitions = new HashMap<>();
    this.acceptStates = new HashSet<>();
  }

  /**
   * Sets the start state of the finite automaton.
   *
   * @param startState the start state
   */
  @Override
  public void setStartState(final String startState) {
    this.startState = startState;
    states.add(startState);
  }

  /**
   * Adds a state to the finite automaton.
   *
   * @param state the state
   * @param isAcceptState whether the state is an accept state
   */
  @Override
  public void addState(final String state, final boolean isAcceptState) {
    states.add(state);
    if (isAcceptState) {
      acceptStates.add(state);
    }
    transitions.putIfAbsent(state, new HashMap<>());
  }

  /**
   * Adds a transition to the finite automaton.
   *
   * @param fromState the state from which the transition starts
   * @param input the input symbol
   * @param toState the state to which the transition goes
   */
  @Override
  public void addTransition(final String fromState, final char input, final String toState) {
    transitions.putIfAbsent(fromState, new HashMap<>());
    transitions.get(fromState).put(input, toState);
  }

  /**
   * Checks if a string is accepted by the finite automaton.
   *
   * @param input the input string
   * @return true if the string is accepted, false otherwise
   */
  @Override
  public boolean isStringAccepted(final String input) {
    String currentState = startState;
    for (char symbol : input.toCharArray()) {
      Map<Character, String> currentTransitions = transitions.get(currentState);
      if (currentTransitions == null || !currentTransitions.containsKey(symbol)) {
        return false;
      }
      currentState = currentTransitions.get(symbol);
    }
    return acceptStates.contains(currentState);
  }

  /**
   * Converts this finite automaton to an equivalent regular grammar.
   *
   * @return the equivalent regular grammar.
   */
  @Override
  public Grammar toRegularGrammar() {
    // Initialize the components of the Grammar
    List<Character> Vn = new ArrayList<>();
    List<Character> Vt = new ArrayList<>(sigma);
    Map<Character, List<String>> productions = new HashMap<>();
    char S = startState.charAt(0);

    for (String state : states) {
      Vn.add(state.charAt(0));
      productions.putIfAbsent(state.charAt(0), new ArrayList<>());
    }

    for (Map.Entry<String, Map<Character, String>> transitionEntry : transitions.entrySet()) {
      char fromState = transitionEntry.getKey().charAt(0);
      for (Map.Entry<Character, String> symbolEntry : transitionEntry.getValue().entrySet()) {
        char symbol = symbolEntry.getKey();
        String toStateStr = symbolEntry.getValue();
        char toState = toStateStr.charAt(0);

        productions.get(fromState).add(symbol + "" + toState);

        if (acceptStates.contains(toStateStr)) {
          productions.get(fromState).add(String.valueOf(symbol));
        }
      }
    }

    return new Grammar(Vn, Vt, productions, S);
  }
}
