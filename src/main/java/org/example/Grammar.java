package org.example;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Grammar {

    private final List<Character> Vn;

    private final List<Character> Vt;

    private final List<String> P;

    private final char S;

    public Grammar() {
        this.Vn = List.of('S', 'A', 'B');
        this.Vt = List.of('a', 'b', 'c', 'd');
        this.P = List.of("bS", "dA", "aA", "dB", "cB", "b", "a");
        this.S = 'S';
    }

    public String generateString() {
        StringBuilder word = new StringBuilder();
        Random random = new Random();
        word.append('S');

        while (word.indexOf("S") != -1 || word.indexOf("A") != -1 || word.indexOf("B") != -1) {
            if (word.indexOf("S") != -1) {
                int index = word.indexOf("S");
                if (random.nextBoolean()) {
                    word.replace(index, index + 1, "bS");
                } else {
                    word.replace(index, index + 1, "dA");
                }
            }
            if (word.indexOf("A") != -1) {
                int index = word.indexOf("A");
                int choice = random.nextInt(3);
                if (choice == 0) {
                    word.replace(index, index + 1, "aA");
                } else if (choice == 1) {
                    word.replace(index, index + 1, "dB");
                } else {
                    word.replace(index, index + 1, "b");
                }
            }
            if (word.indexOf("B") != -1) {
                int index = word.indexOf("B");
                if (random.nextBoolean()) {
                    word.replace(index, index + 1, "cB");
                } else {
                    word.replace(index, index + 1, "a");
                }
            }
        }
        return word.toString();
    }

    public String generateStringWithProgression() {
        StringBuilder word = new StringBuilder("S");
        StringBuilder progression = new StringBuilder("S");
        final int MAX_STEPS = 20;

        int step = 0;
        while (step < MAX_STEPS) {
            boolean foundNonTerminal = false;

            for (int i = 0; i < word.length(); i++) {
                char currentChar = word.charAt(i);
                if (Vn.contains(currentChar)) {
                    foundNonTerminal = true;

                    for (String production : P) {
                        if (production.charAt(0) == currentChar) {
                            String replaceWith = production.substring(1);
                            word.replace(i, i + 1, replaceWith);
                            progression.append(" -> ").append(word);
                            break;
                        }
                    }
                    break;
                }
            }

            if (!foundNonTerminal) {
                break;
            }

            step++;
        }

        return progression.toString();
    }


    public FiniteAutomaton toFiniteAutomaton() {
        FiniteAutomaton fa = new FiniteAutomaton(new HashSet<>(Vt));

        for (char vn : Vn) {
            fa.addState(String.valueOf(vn), false);
        }

        fa.addState("F", true);

        for (String production : P) {
            String fromState = String.valueOf(production.charAt(0));
            if (production.length() == 2) {
                char symbol = production.charAt(0);
                fa.addTransition(fromState, symbol, "F");
            } else if (production.length() > 2) {
                char symbol = production.charAt(1);
                String toState = String.valueOf(production.charAt(2));
                fa.addTransition(fromState, symbol, toState);
            }
        }

        fa.setStartState(String.valueOf(S));

        for (String production : P) {
            if (production.length() == 2) {
                fa.addState(String.valueOf(production.charAt(0)), true);
            }
        }

        return fa;
    }


}

