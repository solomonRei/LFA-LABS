package org.example;

import java.util.List;
import java.util.Scanner;
import org.example.lexer.Lexer;
import org.example.lexer.Token;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter an expression to tokenize:");
        String input = scanner.nextLine();
        //12 + 24.5 / (3 - 4) * varNam

        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();

        tokens.forEach(System.out::println);

        scanner.close();
    }
}
