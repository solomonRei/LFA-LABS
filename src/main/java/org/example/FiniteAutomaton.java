package org.example;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class FiniteAutomaton {

    private Set<String> states;

    private Set<Character> sigma;

    private Map<String, Map<Character, String>> transitions;

    private String startState;

    private Set<String> acceptStates;

    public FiniteAutomaton(Set<Character> sigma) {
        this.states = new HashSet<>();
        this.sigma = sigma;
        this.transitions = new HashMap<>();
        this.acceptStates = new HashSet<>();
    }

    public void setStartState(String startState) {
        this.startState = startState;
        this.states.add(startState);
    }

    public void addState(String state, boolean isAcceptState) {
        this.states.add(state);
        if (isAcceptState) {
            this.acceptStates.add(state);
        }
        this.transitions.putIfAbsent(state, new HashMap<>());
    }

    public void addTransition(String fromState, char input, String toState) {
        transitions.putIfAbsent(fromState, new HashMap<>());
        transitions.get(fromState).put(input, toState);
    }

    public boolean accepts(String input) {
        String currentState = startState;
        for (char symbol : input.toCharArray()) {
            if (!sigma.contains(symbol) || !transitions.get(currentState).containsKey(symbol)) {
                return false;
            }
            currentState = transitions.get(currentState).get(symbol);
        }
        return acceptStates.contains(currentState);
    }
}
