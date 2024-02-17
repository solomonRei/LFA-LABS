package org.example;

public class Main {

    public static void main(String[] args) {
        Grammar grammar = new Grammar();

        for (int i = 0; i < 5; i++) {
            System.out.println(grammar.generateString());
        }

        String progression = grammar.generateStringWithProgression();
        System.out.println(progression);

        FiniteAutomaton fa = grammar.toFiniteAutomaton();

        System.out.println(fa.accepts("db"));
    }
}
//Variant 25:
//        VN={S, A, B},
//        VT={a, b, c, d},
//        P={
//        S → bS
//        S → dA
//        A → aA
//        A → dB
//        B → cB
//        A → b
//        B → a
//        }