package org.example.utils;

import static guru.nidi.graphviz.model.Factory.*;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.example.fa.impl.DFiniteAutomaton;
import org.example.fa.impl.NDFiniteAutomaton;

public class GraphUtils {

  /**
   * Generates a graph for a deterministic finite automaton.
   *
   * @param dfa the deterministic finite automaton
   * @param outputPath the path to save the graph
   */
  public static void generateGraph(DFiniteAutomaton dfa, String outputPath) {
    MutableGraph g =
        mutGraph("finite_automaton").setDirected(true).add(mutNode("start").add(Shape.POINT));
    MutableNode startNode = mutNode(dfa.getStartState()).add(Color.BLACK.fill(), Shape.CIRCLE);
    g.add(startNode);
    g.add(mutNode("start").addLink(to(startNode)));

    // Add all states
    dfa.getStates()
        .forEach(
            state -> {
              MutableNode node = mutNode(state).add(Shape.CIRCLE);
              if (dfa.getAcceptStates().contains(state)) {
                node.add(Shape.DOUBLE_CIRCLE);
              }
              g.add(node);
            });

    // Determine if the empty set state is needed
    Set<String> definedTransitions = new HashSet<>();
    dfa.getTransitions().values().forEach(map -> definedTransitions.addAll(map.values()));
      boolean emptySetNeeded = dfa.getStates().stream()
              .anyMatch(state -> dfa.getSigma().stream()
                      .anyMatch(symbol -> !dfa.getTransitions().getOrDefault(state, Collections.emptyMap()).containsKey(symbol)));


      // Add transitions
    dfa.getTransitions().entrySet().stream()
        .flatMap(
            entry ->
                entry.getValue().entrySet().stream()
                    .map(
                        subEntry ->
                            mutNode(entry.getKey())
                                .addLink(
                                    to(mutNode(subEntry.getValue()))
                                        .with(Label.of(subEntry.getKey().toString())))))
        .forEach(g::add);

    // Handle missing transitions to the empty set state
    if (emptySetNeeded) {
      g.add(mutNode("Ø").add(Shape.CIRCLE, Color.GRAY.fill()));
      dfa.getStates().stream()
          .flatMap(
              state ->
                  dfa.getSigma().stream()
                      .filter(
                          symbol ->
                              !dfa.getTransitions()
                                  .getOrDefault(state, new HashMap<>())
                                  .containsKey(symbol))
                      .map(
                          symbol ->
                              mutNode(state)
                                  .addLink(to(mutNode("Ø")).with(Label.of(symbol.toString())))))
          .forEach(g::add);
    }

    try {
      Graphviz.fromGraph(g).render(Format.PNG).toFile(new File(outputPath));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Generates a graph for a non-deterministic finite automaton.
   *
   * @param nfa the non-deterministic finite automaton
   * @param outputPath the path to save the graph
   */
  public static void generateNfaGraph(NDFiniteAutomaton nfa, String outputPath) {
    MutableGraph g =
        mutGraph("non_deterministic_finite_automaton")
            .setDirected(true)
            .add(mutNode("start").add(Shape.POINT));
    MutableNode startNode = mutNode(nfa.getStartState()).add(Color.BLACK.fill(), Shape.CIRCLE);
    g.add(startNode);
    g.add(mutNode("start").addLink(to(startNode)));

    // Add all states
    nfa.getStates()
        .forEach(
            state -> {
              MutableNode node = mutNode(state).add(Shape.CIRCLE);
              if (nfa.getAcceptStates().contains(state)) {
                node.add(Shape.DOUBLE_CIRCLE);
              }
              g.add(node);
            });

    // Add transitions
    nfa.getNfaTransitions()
        .forEach(
            (fromState, charSetMap) ->
                charSetMap.forEach(
                    (input, toStates) ->
                        toStates.forEach(
                            toState ->
                                g.add(
                                    mutNode(fromState)
                                        .addLink(
                                            to(mutNode(toState))
                                                .with(Label.of(String.valueOf(input))))))));

    try {
      Graphviz.fromGraph(g).render(Format.PNG).toFile(new File(outputPath));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
