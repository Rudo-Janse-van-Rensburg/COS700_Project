/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticprogram;

/**
 *
 * @author rudo
 */
public class GeneticProgram {

          /**
           * @param args the command line arguments
           */
          public static void main(String[] args) {
                    // TODO code application logic here
                    int number_training_instances = 5;
                    double acceptance_threshold = 0.5;
                    int window_size = 4;
                    long run_time = 600000;

                    int max_generation = 50;
                    int k_folds = 10;
                    int main_max_depth = 5;
                    int condition_max_depth = 5;
                    int population_size = 500;
                    int tournament_size = 2;
                    double crossover_chance = 0.5;
                    double mutation_chance = 0.4;
                    double hoist_chance = 0.1;

                    try {
                              Parameters.setParameters(
                                        number_training_instances,
                                        window_size,
                                        run_time,
                                        acceptance_threshold,
                                        max_generation,
                                        k_folds,
                                        main_max_depth,
                                        condition_max_depth,
                                        population_size,
                                        tournament_size,
                                        crossover_chance,
                                        mutation_chance,
                                        hoist_chance
                              );
                              
                              
                              Evolution evolution = Evolution.getInstance();
                              do {
                                        evolution.print();
                              } while (evolution.evolveGeneration());

                    } catch (Exception e) {
                              e.printStackTrace();
                    }

          }

}
