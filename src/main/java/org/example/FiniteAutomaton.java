package org.example;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Represents a finite automaton. */
public class FiniteAutomaton {

  private final Set<String> states;

  private final Set<Character> sigma;

  private final Map<String, Map<Character, String>> transitions;

  private String startState;

  private final Set<String> acceptStates;

  public FiniteAutomaton(final Set<Character> sigma) {
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
  public void setStartState(final String startState) {
    this.startState = startState;
    this.states.add(startState);
  }

  /**
   * Adds a state to the finite automaton.
   *
   * @param state the state
   * @param isAcceptState whether the state is an accept state
   */
  public void addState(final String state, final boolean isAcceptState) {
    this.states.add(state);
    if (isAcceptState) {
      this.acceptStates.add(state);
    }
    this.transitions.putIfAbsent(state, new HashMap<>());
  }

  /**
   * Adds a transition to the finite automaton.
   *
   * @param fromState the state from which the transition starts
   * @param input the input symbol
   * @param toState the state to which the transition goes
   */
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
}
