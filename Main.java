/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package COS700_Project;

public class Main {

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
 
                    double[] acceptance_thresholds = {0.5};
                    int[] window_sizes = {5};
                    long run_time = 6000;//600000;
                    int[] max_generations = {100};
                    int[] main_max_depths = {8};
                    int[] condition_max_depths = {8};
                    int population_size = 50;
                    int[] tournament_sizes = {2};
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
