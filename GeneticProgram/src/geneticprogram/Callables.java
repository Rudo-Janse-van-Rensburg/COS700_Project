/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticprogram;

import java.util.concurrent.Callable;

/**
 *
 * @author rudo
 */
public class Callables {

          public static Callable<Program[]> getGeneticOperatorTask(Program[] parents, char operation, long seed) {
                    return new GeneticOperatorTask(parents, operation, seed);

          }

          public static Callable<Program[]> getGeneticOperatorTask(Program[] parents, char operation, int max_depth, long seed) {
                    return new GeneticOperatorTask(parents, operation, max_depth, seed);

          }

}

class GeneticOperatorTask implements Callable<Program[]> {

          Program[] parents;
          private char operation;
          private int max_depth;
          private long seed;

          public GeneticOperatorTask(Program[] parents, char operation, long seed) {
                    this.parents = parents;
                    this.operation = operation;
                    this.seed = seed;
          }

          public GeneticOperatorTask(Program[] parents, char operation, int max_depth, long seed) {
                    this.parents = parents;
                    this.operation = operation;
                    this.max_depth = max_depth;
                    this.seed = seed;
          }

          @Override
          public Program[] call() throws Exception {

                    try {
                              switch (operation) {
                                        case Meta.MUTATE:
                                                  GeneticOperators.mutate(parents[0], seed);
                                                  break;
                                        case Meta.CROSSOVER:
                                                  GeneticOperators.crossover(parents[0], parents[1], seed);
                                                  break;
                                        case Meta.HOIST:
                                                  GeneticOperators.hoist(parents[0], seed);
                                                  break;
                                        case Meta.GROW:
                                                  GeneticOperators.grow(parents[0], max_depth, seed);
                                                  break;
                                        case Meta.FULL:
                                                  GeneticOperators.full(parents[0], max_depth, seed);
                                                  break;
                              }
                              return parents;
                    } catch (Exception e) {

                    }
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
          }
}
