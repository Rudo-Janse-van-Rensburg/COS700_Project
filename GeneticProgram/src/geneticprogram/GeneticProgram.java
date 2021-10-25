/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticprogram;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import BinPacking.BinPacking;
import FlowShop.FlowShop;
import PersonnelScheduling.PersonnelScheduling;
import SAT.SAT;
import java.util.Random;

/**
 *
 * @author rudo
 */
public class GeneticProgram {

          /**
           * This method creates the relevant HyperHeuristic object from the
           * index given as a parameter. after the HyperHeuristic object is
           * created, its time limit is set.
           */
          private static HyperHeuristic loadHyperHeuristic(int index, long timeLimit, Random rng) throws Exception {
                    HyperHeuristic h = null;
                    switch (index) {
                              case 0:
                                        Program prog = FlyWeight.getInstance().getProgram();
                                        GeneticOperators.full(prog, 3, rng.nextLong());
                                        h = new ExampleHyperHeuristic1(rng.nextLong(), prog);
                                        h.setTimeLimit(timeLimit);
                                        break;
                              case 1:
                                        h = new ExampleHyperHeuristic2(rng.nextLong());
                                        h.setTimeLimit(timeLimit);
                                        break;
                              default:
                                        System.err.println("there is no hyper heuristic with this index");
                                        System.exit(1);
                    }
                    return h;
          }

          /**
           * this method creates the relevant ProblemDomain object from the
           * index given as a parameter. for each instance, the ProblemDomain is
           * initialised with an identical seed for each hyper-heuristic. this
           * is so that each hyper-heuristic starts its search from the same
           * initial solution.
           */
          private static ProblemDomain loadProblemDomain(int index, long instanceseed) {
                    ProblemDomain p = null;
                    switch (index) {
                              case 0:
                                        p = new SAT(instanceseed);
                                        break;
                              case 1:
                                        p = new BinPacking(instanceseed);
                                        break;
                              case 2:
                                        p = new PersonnelScheduling(instanceseed);
                                        break;
                              case 3:
                                        p = new FlowShop(instanceseed);
                                        break;
                              default:
                    }//end switch
                    return p;
          }

          /**
           * @param args the command line arguments
           * @throws java.lang.Exception
           */
          public static void main(String[] args) throws Exception {
                    // TODO code application logic here
                    int number_training_instances = 5;
                    double acceptance_threshold = 0.5;
                    int window_size = 4;
                    long run_time = 100;

                    int max_generation = 1;
                    int k_folds = 10;
                    int main_max_depth = 5;
                    int condition_max_depth = 5;
                    int population_size = 2;
                    int tournament_size = 2;
                    double crossover_chance = 0.5;
                    double mutation_chance = 0.4;
                    double hoist_chance = 0.1;

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
                    
                    Evolution evolution =Evolution.getInstance();
                    do{
                             evolution.print();
                    }while( evolution.evolveGeneration());

          }

}
