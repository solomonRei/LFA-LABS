package org.example.lexer;

import org.example.lexer.enums.Type;

/** Represents a token. */
public class Token {
  private final Type type;
  private final String text;

  public Token(Type type, String text) {
    this.type = type;
    this.text = text;
  }

  @Override
  public String toString() {
    return type + ": '" + text + "'";
  }
}
