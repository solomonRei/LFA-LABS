package org.example.fa.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.example.Grammar;
import org.example.fa.FiniteAutomation;

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

  public Set<String> getStates() {
    return states;
  }

  public Set<Character> getSigma() {
    return sigma;
  }

  public Map<String, Map<Character, String>> getTransitions() {
    return transitions;
  }

  public String getStartState() {
    return startState;
  }

  public Set<String> getAcceptStates() {
    return acceptStates;
  }

  /**
   * Converts this finite automaton to an equivalent regular grammar.
   *
   * @return the equivalent regular grammar.
   */
  @Override
  public Grammar toRegularGrammar() {
    List<Character> Vn = new ArrayList<>();
    List<Character> Vt = new ArrayList<>(sigma);
    Map<Character, List<String>> productions = new HashMap<>();
    char S = startState.charAt(0);

    states.forEach(
        state -> {
          Vn.add(state.charAt(0));
          productions.putIfAbsent(state.charAt(0), new ArrayList<>());
        });

    transitions.forEach(
        (fromState, toState) ->
            toState.forEach(
                (symbol, state) -> {
                  productions.get(fromState.charAt(0)).add(symbol + "" + state.charAt(0));
                  if (acceptStates.contains(state)) {
                    productions.get(fromState.charAt(0)).add(String.valueOf(symbol));
                  }
                }));

    return new Grammar(Vn, Vt, productions, S);
  }

  @Override
  public String toString() {
    String statesStr = String.join(", ", states);
    String sigmaStr = sigma.stream().map(Object::toString).collect(Collectors.joining(", "));
    String startStateStr = startState;
    String acceptStatesStr = String.join(", ", acceptStates);

    String transitionsStr =
        transitions.entrySet().stream()
            .flatMap(
                entry ->
                    entry.getValue().entrySet().stream()
                        .map(
                            subEntry ->
                                "    "
                                    + entry.getKey()
                                    + " --"
                                    + subEntry.getKey()
                                    + "--> "
                                    + subEntry.getValue()))
            .collect(Collectors.joining("\n"));

    return String.format(
        "DFiniteAutomaton{\n"
            + "  States: [%s]\n"
            + "  Sigma: [%s]\n"
            + "  Start State: '%s'\n"
            + "  Accept States: [%s]\n"
            + "  Transitions:\n%s\n"
            + "}",
        statesStr, sigmaStr, startStateStr, acceptStatesStr, transitionsStr);
  }
}
