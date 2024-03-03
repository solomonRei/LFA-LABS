package org.example.fa.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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

  /**
   * Checks if a string is accepted by the finite automaton.
   *
   * @param input the input string
   * @return true if the string is accepted, false otherwise
   */
  @Override
  public boolean isStringAccepted(String input) {
    return false;
  }

  /**
   * Converts the NFA to a regular grammar.
   *
   * @return the regular grammar
   */
  @Override
  public Grammar toRegularGrammar() {
    List<Character> Vn = new ArrayList<>();
    List<Character> Vt = new ArrayList<>(sigma);
    Map<Character, List<String>> productions = new HashMap<>();
    char S = 'S'; // Starting symbol for the grammar

    // Initialize non-terminals and their productions
    states.forEach(
        state -> {
          char stateSymbol = state.equals(startState) ? S : state.charAt(0);
          Vn.add(stateSymbol);
          productions.putIfAbsent(stateSymbol, new ArrayList<>());

          // If it's an accept state, add an ε-production
          if (acceptStates.contains(state)) {
            productions.get(stateSymbol).add("ε");
          }
        });

    // Add transitions to productions
    nfaTransitions.forEach(
        (state, charSetMap) -> {
          char fromStateSymbol = state.equals(startState) ? S : state.charAt(0);

          charSetMap.forEach(
              (symbol, toStates) -> {
                toStates.forEach(
                    toState -> {
                      char toStateSymbol = toState.equals(startState) ? S : toState.charAt(0);

                      // Correctly concatenate as Strings
                      String production =
                          ""
                              + symbol
                              + toStateSymbol; // Precede with "" to ensure string concatenation
                      productions.get(fromStateSymbol).add(production);
                    });
              });
        });

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

  public Map<String, Map<Character, Set<String>>> getNfaTransitions() {
    return nfaTransitions;
  }

  /**
   * Converts the NFA to an equivalent DFA.
   *
   * @return the equivalent DFA
   */
  public DFiniteAutomaton convertToDFA() {
    DFiniteAutomaton dfa = new DFiniteAutomaton(sigma);
    Map<Set<String>, String> dfaStatesMap = new HashMap<>();
    Set<Set<String>> markedStates = new HashSet<>();
    Set<Set<String>> unmarkedStates = new HashSet<>();

    char currentStateChar = 'S';
    Function<Integer, String> getStateName =
        (counter) -> {
          if (counter == 0) {
            return String.valueOf(currentStateChar);
          } else {
            return String.valueOf((char) ('A' + counter - 1));
          }
        };

    Set<String> startSet = new HashSet<>();
    startSet.add(startState);
    unmarkedStates.add(startSet);
    String startStateName = getStateName.apply(0);
    dfaStatesMap.put(startSet, startStateName);
    dfa.addState(startStateName, acceptStates.contains(startState));
    dfa.setStartState(startStateName);

    int stateCounter = 1;

    while (!unmarkedStates.isEmpty()) {
      Set<String> currentSet = unmarkedStates.iterator().next();
      unmarkedStates.remove(currentSet);
      markedStates.add(currentSet);
      String currentStateName = dfaStatesMap.get(currentSet);

      for (char symbol : sigma) {
        Set<String> newSet = new HashSet<>();
        currentSet.forEach(
            state ->
                newSet.addAll(
                    nfaTransitions
                        .getOrDefault(state, Collections.emptyMap())
                        .getOrDefault(symbol, Collections.emptySet())));

        if (!newSet.isEmpty()) {
          String newStateName;
          if (!dfaStatesMap.containsKey(newSet)) {
            newStateName = getStateName.apply(stateCounter++);
            dfaStatesMap.put(newSet, newStateName);
            dfa.addState(newStateName, !Collections.disjoint(newSet, acceptStates));
            unmarkedStates.add(newSet);
          } else {
            newStateName = dfaStatesMap.get(newSet);
          }
          dfa.addTransition(currentStateName, symbol, newStateName);
        }
      }
    }

    return dfa;
  }

  /**
   * Converts the NFA to an equivalent DFA using the ε-closure method.
   *
   * @return the equivalent DFA
   */
  public DFiniteAutomaton convertEpsilonNfaToDfa() {
    DFiniteAutomaton dfa = new DFiniteAutomaton(sigma);

    Map<String, Set<String>> epsilonClosures = new HashMap<>();

    states.forEach(state -> epsilonClosures.put(state, calculateEpsilonClosure(state)));

    Set<String> startClosure = epsilonClosures.get(startState);
    Set<Set<String>> dfaStates = new HashSet<>();
    dfaStates.add(startClosure);

    List<Set<String>> queue = new ArrayList<>();
    queue.add(startClosure);

    dfa.setStartState(encodeState(startClosure));
    dfa.addState(encodeState(startClosure), startClosure.stream().anyMatch(acceptStates::contains));

    while (!queue.isEmpty()) {
      Set<String> currentDfaState = queue.remove(0);
      sigma.forEach(
          symbol -> {
            Set<String> nextState = new HashSet<>();
            currentDfaState.forEach(
                state -> {
                  Set<String> transitions =
                      nfaTransitions
                          .getOrDefault(state, Collections.emptyMap())
                          .getOrDefault(symbol, Collections.emptySet());
                  transitions.forEach(
                      transition -> nextState.addAll(epsilonClosures.get(transition)));
                });

            if (!nextState.isEmpty() && !dfaStates.contains(nextState)) {
              dfaStates.add(nextState);
              queue.add(nextState);
              dfa.addState(
                  encodeState(nextState), nextState.stream().anyMatch(acceptStates::contains));
            }

            if (!nextState.isEmpty()) {
              dfa.addTransition(encodeState(currentDfaState), symbol, encodeState(nextState));
            }
          });
    }

    return dfa;
  }

  /**
   * Calculates the ε-closure of a state.
   *
   * @param startState the start state
   * @return the ε-closure of the state
   */
  private Set<String> calculateEpsilonClosure(String startState) {
    Set<String> closure = new HashSet<>();
    dfsEpsilonClosure(startState, closure);
    return closure;
  }

  /**
   * Depth-first search to calculate the ε-closure of a state.
   *
   * @param currentState the current state
   * @param closure the set of states in the ε-closure
   */
  private void dfsEpsilonClosure(String currentState, Set<String> closure) {
    if (!closure.contains(currentState)) {
      closure.add(currentState);
      Set<String> epsilonTransitions =
          nfaTransitions
              .getOrDefault(currentState, new HashMap<>())
              .getOrDefault('ε', Collections.emptySet());
      epsilonTransitions.forEach(nextState -> dfsEpsilonClosure(nextState, closure));
    }
  }

  /**
   * Encodes a set of states into a single string.
   *
   * @param stateSet the set of states
   * @return the encoded string
   */
  private String encodeState(Set<String> stateSet) {
    List<String> sortedStates = new ArrayList<>(stateSet);
    Collections.sort(sortedStates);
    return String.join("_", sortedStates);
  }

  public Set<String> getStates() {
    return states;
  }

  public Set<Character> getSigma() {
    return sigma;
  }

  public String getStartState() {
    return startState;
  }

  public Set<String> getAcceptStates() {
    return acceptStates;
  }
}
