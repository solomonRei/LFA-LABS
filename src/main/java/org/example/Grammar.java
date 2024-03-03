package org.example;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.example.fa.impl.DFiniteAutomaton;

/** Represents a context-free grammar. */
public class Grammar {

  private final List<Character> Vn;
  private final List<Character> Vt;
  private final Map<Character, List<String>> productions;
  private final char S;

  public Grammar(
      List<Character> Vn, List<Character> Vt, Map<Character, List<String>> productions, char S) {
    this.Vn = Vn;
    this.Vt = Vt;
    this.productions = productions;
    this.S = S;
  }

  /**
   * Classifies the grammar based on the Chomsky hierarchy.
   *
   * @return the type of the grammar according to the Chomsky hierarchy.
   */
  public String classifyGrammar() {
    boolean isType3 = true;
    boolean isType2 = true;
    boolean isRightLinear = true;
    boolean isLeftLinear = true;
    boolean hasContextSensitiveProduction = false;

    for (Map.Entry<Character, List<String>> entry : productions.entrySet()) {
      char lhs = entry.getKey();
      for (String production : entry.getValue()) {
        if (!production.matches("[a-zA-Z]*[A-Z]?")) {
          isType3 = false;
        }
        if (!production.matches("[a-zA-Zε]*")) {
          isType2 = false;
        }
        if (!production.matches("[a-z]*[A-Z]?")) {
          isRightLinear = false;
        }
        if (!production.matches("[A-Z]?[a-z]*")) {
          isLeftLinear = false;
        }

        if (production.length() < String.valueOf(lhs).length()) {
          hasContextSensitiveProduction = true;
        }
      }
    }

    if (!isType2 && !isType3) {
      return "Type 0 or Type 1 (Cannot be precisely determined without further analysis)";
    } else if (isType3) {
      if (isRightLinear && !isLeftLinear) {
        return "Type 3 (Regular, Right-Linear)";
      } else if (!isRightLinear && isLeftLinear) {
        return "Type 3 (Regular, Left-Linear)";
      } else if (isRightLinear) {
        return "Type 3 (Regular, Mixed Linear)";
      } else {
        return "Type 3 (Regular)";
      }
    } else if (isType2) {
      return "Type 2 (Context-Free)";
    } else {
      return "Type 1 (Context-Sensitive)";
    }
  }

  /**
   * Classifies the grammar based on the Chomsky hierarchy.
   *
   * @return the type of the grammar according to the Chomsky hierarchy.
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
    final int MIN_STEPS = 10;
    final int MAX_STEPS = 15;
    Random random = new Random();

    int step = 0;
    while (step < MAX_STEPS && word.toString().matches(".*[SABC].*")) {
      boolean replaced = false;
      for (int i = 0; i < word.length(); i++) {
        char currentChar = word.charAt(i);
        if (Vn.contains(currentChar)) {
          List<String> possibleProductions = productions.getOrDefault(currentChar, List.of());

          String chosenProduction = null;
          if (step <= MIN_STEPS) {
            for (String production : possibleProductions) {
              if (production.matches(".*[SABC].*")) {
                chosenProduction = production;
                break;
              }
            }
          }
          if (chosenProduction == null) {
            chosenProduction = possibleProductions.get(random.nextInt(possibleProductions.size()));
          }

          if (!chosenProduction.isEmpty()) {
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
  public DFiniteAutomaton toFiniteAutomaton() {
    DFiniteAutomaton fa = new DFiniteAutomaton(new HashSet<>(Vt));

    for (char vn : Vn) {
      fa.addState(String.valueOf(vn), false);
    }
    fa.addState("F", true);

    productions.forEach(
        (key, value) ->
            value.forEach(
                production -> {
                  if (production.length() == 1 && Vt.contains(production.charAt(0))) {
                    fa.addTransition(String.valueOf(key), production.charAt(0), "F");
                  } else {
                    char symbol = production.charAt(0);
                    String nextState =
                        production.length() > 1 ? String.valueOf(production.charAt(1)) : "F";
                    fa.addTransition(String.valueOf(key), symbol, nextState);
                  }
                }));

    fa.setStartState(String.valueOf(S));
    return fa;
  }

  @Override
  public String toString() {
    return "Grammar{"
        + "Vn="
        + Vn
        + ", Vt="
        + Vt
        + ", productions="
        + productions
        + ", S="
        + S
        + '}';
  }
}
