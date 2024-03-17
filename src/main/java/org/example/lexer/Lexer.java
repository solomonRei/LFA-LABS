package org.example.lexer;

import java.util.ArrayList;
import java.util.List;
import org.example.lexer.enums.Type;

/** A simple lexer for a simple expression language. */
public class Lexer {
  private final String input;
  private final List<Token> tokens = new ArrayList<>();
  private int pos = 0;

  public Lexer(String input) {
    this.input = input.trim();
  }

  /**
   * Tokenizes the input string.
   *
   * @return the list of tokens
   */
  public List<Token> tokenize() {
    while (pos < input.length()) {
      char current = input.charAt(pos);
      if (Character.isDigit(current) || current == '.') {
        tokenizeNumberOrFloat();
      } else if (Character.isLetter(current)) {
        tokenizeIdentifier();
      } else if (isOperator(current)) {
        tokens.add(new Token(Type.OPERATOR, String.valueOf(input.charAt(pos++))));
      } else if (isParenthesis(current)) {
        tokens.add(new Token(Type.PARENTHESIS, String.valueOf(input.charAt(pos++))));
      } else if (Character.isWhitespace(current)) {
        pos++;
      } else {
        throw new IllegalArgumentException("Unexpected character: " + current);
      }
    }
    return tokens;
  }

  /** Tokenizes a number or a float. */
  private void tokenizeNumberOrFloat() {
    StringBuilder number = new StringBuilder();
    while (pos < input.length()
        && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.')) {
      number.append(input.charAt(pos++));
    }
    tokens.add(new Token(Type.NUMBER, number.toString()));
  }

  /** Tokenizes an identifier. */
  private void tokenizeIdentifier() {
    StringBuilder identifier = new StringBuilder();
    while (pos < input.length() && Character.isLetterOrDigit(input.charAt(pos))) {
      identifier.append(input.charAt(pos++));
    }
    tokens.add(new Token(Type.IDENTIFIER, identifier.toString()));
  }

  /**
   * Checks if the character is an operator.
   *
   * @param c the character to check
   * @return true if the character is an operator, false otherwise
   */
  private boolean isOperator(char c) {
    return "+-*/".indexOf(c) != -1;
  }

  /**
   * Checks if the character is a parenthesis.
   *
   * @param c the character to check
   * @return true if the character is a parenthesis, false otherwise
   */
  private boolean isParenthesis(char c) {
    return "()".indexOf(c) != -1;
  }
}
