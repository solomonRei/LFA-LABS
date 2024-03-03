package org.example;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.example.fa.impl.DFiniteAutomaton;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class GrammarTest {

  static Stream<String> stringProvider() {
    return Stream.of("db", "dada", "ddcccccccccccca", "bbddcccccccccca");
  }

  @ParameterizedTest
  @MethodSource("stringProvider")
  void testFiniteAutomatonWithStrings(String input) {
    Grammar grammar =
        new Grammar(
            List.of('S', 'A', 'B'),
            List.of('a', 'b', 'c', 'd'),
            Map.of(
                'S', List.of("bS", "dA"), 'A', List.of("aA", "dB", "b"), 'B', List.of("cB", "a")),
            'S');
    DFiniteAutomaton fa = grammar.toFiniteAutomaton();
    assertTrue(
        fa.isStringAccepted(input), () -> "The string \"" + input + "\" should be accepted.");
  }

  @Test
  void testClassifyRegularGrammar() {
    Grammar grammar =
        new Grammar(List.of('S'), List.of('a', 'b'), Map.of('S', List.of("aS", "b")), 'S');
    assertEquals("Type 3 (Regular, Right-Linear)", grammar.classifyGrammar());
  }

  @Test
  void testClassifyContextFreeGrammar() {
    Grammar grammar =
        new Grammar(
            List.of('S', 'A'),
            List.of('a', 'b'),
            Map.of(
                'S', List.of("aA", "b"),
                'A', List.of("aS", "bA", "ε")),
            'S');
    assertEquals("Type 2 (Context-Free)", grammar.classifyGrammar());
  }
}
