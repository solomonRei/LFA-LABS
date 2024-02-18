package org.example;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

/** Represents a context-free grammar. */
public class Grammar {

  private final List<Character> Vn;

  private final List<Character> Vt;

  private final Map<Character, List<String>> productions;

  private final Map<Character, List<String>> productionsTerminals;

  private final char S;

  public Grammar() {
    this.Vn = List.of('S', 'A', 'B');
    this.Vt = List.of('a', 'b', 'c', 'd');
    this.productions =
        Map.of(
            'S', List.of("bS", "dA"),
            'A', List.of("aA", "dB"),
            'B', List.of("cB"));
    this.productionsTerminals =
        Map.of(
            'A', List.of("b"),
            'B', List.of("a"));

    this.S = 'S';
  }

  /**
   * Generates a string based on the grammar.
   *
   * @return the generated string
   */
  public String generateString() {
    StringBuilder word = new StringBuilder();
    Random random = new Random();
    word.append(S);

    while (word.toString().matches(".*[SAB].*")) {
      for (int i = 0; i < word.length(); i++) {
        char currentChar = word.charAt(i);
        if (Vn.contains(currentChar)) {
          List<String> possibleProductions = productions.getOrDefault(currentChar, List.of());
          List<String> terminalProductions =
              productionsTerminals.getOrDefault(currentChar, List.of());

          possibleProductions = new java.util.ArrayList<>(possibleProductions);
          possibleProductions.addAll(terminalProductions);

          if (!possibleProductions.isEmpty()) {
            String chosenProduction =
                possibleProductions.get(random.nextInt(possibleProductions.size()));
            word.replace(i, i + 1, chosenProduction);
            break;
          }
        }
      }
    }

    return word.toString();
  }

  /**
   * Generates a string based on the grammar and shows the progression of the string.
   *
   * @return the generated string
   */
  public String generateStringWithProgression() {
    StringBuilder word = new StringBuilder(String.valueOf(S));
    StringBuilder progression = new StringBuilder(String.valueOf(S));
    final int MIN_STEPS = 15;
    final int MAX_STEPS = 20;
    Random random = new Random();

    int step = 0;
    while (step < MAX_STEPS) {
      boolean replaced = false;
      for (int i = 0; i < word.length(); i++) {
        char currentChar = word.charAt(i);
        if (Vn.contains(currentChar)) {
          List<String> possibleProductions =
              new java.util.ArrayList<>(productions.getOrDefault(currentChar, List.of()));

          if (step >= MIN_STEPS - 1 && step < MAX_STEPS) {
            possibleProductions.addAll(productionsTerminals.getOrDefault(currentChar, List.of()));
          }

          if (!possibleProductions.isEmpty()) {
            String chosenProduction =
                possibleProductions.get(random.nextInt(possibleProductions.size()));
            word.replace(i, i + 1, chosenProduction);
            progression.append(" -> ").append(word);
            replaced = true;
            break;
          }
        }
      }

      if (!replaced) {
        if (step >= MIN_STEPS) break;
      } else {
        step++;
      }
    }

    System.out.println("Steps: " + step);
    System.out.println("Progression: " + progression);
    return word.toString();
  }

  /**
   * Converts the grammar to a finite automaton.
   *
   * @return the finite automaton
   */
  public FiniteAutomaton toFiniteAutomaton() {
    FiniteAutomaton fa = new FiniteAutomaton(new HashSet<>(Vt));

    for (char vn : Vn) {
      fa.addState(String.valueOf(vn), false);
    }
    fa.addState("F", true);

    productions.forEach(
        (key, value) ->
            value.forEach(
                production -> {
                  char symbol = production.charAt(0);
                  String nextState =
                      production.length() > 1 ? String.valueOf(production.charAt(1)) : "F";
                  fa.addTransition(String.valueOf(key), symbol, nextState);
                }));

    productionsTerminals.forEach(
        (key, value) ->
            value.forEach(
                production -> {
                  fa.addTransition(String.valueOf(key), production.charAt(0), "F");
                }));

    fa.setStartState(String.valueOf(S));
    return fa;
  }
}
