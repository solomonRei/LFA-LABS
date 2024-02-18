package org.example;

import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Grammar grammar = new Grammar();
    FiniteAutomaton fa = grammar.toFiniteAutomaton();
    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.println("\nMenu:");
      System.out.println("1. Generate strings based on the grammar");
      System.out.println("2. Check if a string is accepted by the finite automaton");
      System.out.println("3. Exit");
      System.out.print("Enter your choice (1/2/3): ");

      int choice = scanner.nextInt();
      scanner.nextLine();

      switch (choice) {
        case 1:
          System.out.println("Generated strings:");
          for (int i = 0; i < 5; i++) {
            System.out.println(grammar.generateString());
          }
          System.out.println("\nGenerated strings with progression:");
          for (int i = 0; i < 5; i++) {
            System.out.println("Result: " + grammar.generateStringWithProgression());
          }
          break;
        case 2:
          System.out.print("Enter a string to check: ");
          String inputString = scanner.nextLine();
          System.out.println(
              "The input string \""
                  + inputString
                  + "\" is "
                  + (fa.isStringAccepted(inputString) ? "accepted" : "not accepted")
                  + " by the finite automaton.");
          break;
        case 3:
          System.out.println("Exiting...");
          return;
        default:
          System.out.println("Invalid choice. Please enter 1, 2, or 3.");
          break;
      }
    }
  }
}

// Variant 25:
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
