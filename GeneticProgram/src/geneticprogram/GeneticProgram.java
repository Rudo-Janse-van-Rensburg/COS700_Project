/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticprogram;

public class GeneticProgram {

          public static void runExperiment() throws Exception {
                    Randomness.getInstance().setSeed(42069);
                    Evolution evolution = Evolution.getInstance();
                    do {
                              evolution.print();
                    } while (evolution.evolveGeneration());
                    evolution.writeToCSV();
                    Program best = evolution.getBest_program();
                    CompetitionRunner r = new CompetitionRunner(best);
                    r.start();
                    try {
                              r.join();
                    } catch (InterruptedException e) {
                              System.out.println();
                              System.out.println();
                              System.exit(0);
                    }
          }

          /**
           * @param args the command line arguments
           * @throws java.lang.Exception
           */
          public static void main(String[] args) throws Exception {
                    // TODO code application logic here
                    /*int number_training_instance  = 5;
                    double acceptance_threshold = 0.5;
                    int window_size = 4;
                    long run_time = 100;

                    int max_generation = 50;
                    int k_folds = 10;
                    int main_max_depth = 2;
                    int condition_max_depth = 2;
                    int population_size = 2;
                    int tournament_size = 2;
                    double crossover_chance = 0.5;
                    double mutation_chance = 0.4;
                    double hoist_chance = 0.1;
                     */

                    for (int i = 0; i < 10; i++) {

                    }
                    double[] acceptance_thresholds = {0.5, 0.25, 0.75};
                    int[] window_sizes = {4, 8, 2};
                    long run_time = 600000;
                    int[] max_generations = {50, 100, 150};
                    int[] main_max_depths = {5, 10, 15};
                    int[] condition_max_depths = {5, 10, 15};
                    int population_size = 500;
                    int[] tournament_sizes = {2, 4};
                    for (double acceptance_threshold : acceptance_thresholds) {
                              for (int window_size : window_sizes) {
                                        for (int max_generation : max_generations) {
                                                  for (int main_max_depth : main_max_depths) {
                                                            for (int condition_max_depth : condition_max_depths) {
                                                                      for (int tournament_size : tournament_sizes) {
                                                                                Parameters.setParameters(
                                                                                          5,
                                                                                          window_size,
                                                                                          run_time,
                                                                                          acceptance_threshold,
                                                                                          max_generation,
                                                                                          10,
                                                                                          main_max_depth,
                                                                                          condition_max_depth,
                                                                                          population_size,
                                                                                          tournament_size,
                                                                                          0.5,
                                                                                          0.4,
                                                                                          0.1
                                                                                );
                                                                                runExperiment();
                                                                      }
                                                            }
                                                  }
                                        }
                              }
                    }

          }

}
