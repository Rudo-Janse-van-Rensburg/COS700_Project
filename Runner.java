package COS700_Project;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import BinPacking.BinPacking;
import FlowShop.FlowShop;
import PersonnelScheduling.PersonnelScheduling;
import SAT.SAT;
import VRP.VRP;
import java.util.Random;
import travelingSalesmanProblem.TSP;

public class Runner {

          private long startTime, endTime;
          private final ProblemDomain problem_domain;
          private final int problem;
          private final int instance;
          private final HyperHeuristic hyperheuristic;

          public Runner(Program program, int problem_index,int instance_to_use, long time_limit, long seed) throws Exception {
                    this.problem = problem_index;
                    this.instance = instance_to_use;
                    this.problem_domain = loadProblemDomain(problem_index, seed);
                    this.hyperheuristic = loadHyperHeuristic(program, time_limit, seed);
                    this.problem_domain.loadInstance(instance_to_use);
                    this.hyperheuristic.loadProblemDomain(problem_domain);
          }

          public void execute() {
                    startTime = System.currentTimeMillis();
                    hyperheuristic.run();
                    endTime = System.currentTimeMillis();
          }

          public int[] getHeuristicCallRecord() {
                    return this.problem_domain.getHeuristicCallRecord();
          }

          public double getBestSolutionValue() {
                    return hyperheuristic.getBestSolutionValue();
          }

          public long getElapsedTime() {
                    return hyperheuristic.getElapsedTime();
          }

          public long getRunTime() {
                    return endTime - startTime;
          }

          public double[] getFitnessTrace() {
                    return hyperheuristic.getFitnessTrace();
          }

          public int[] getHeuristicCallTimeRecord() {
                    return problem_domain.getHeuristicCallRecord();
          }

          public int getTotalHeuristicCalls() {
                    int counter = 0;
                    for (int y : problem_domain.getHeuristicCallRecord()) {
                              counter += y;
                    }
                    return counter;
          }
          @Override
          public String toString() {
                    return  problem+","+instance+","+this.getTotalHeuristicCalls()+","+this.getBestSolutionValue()+","+this.getElapsedTime()+","+this.getRunTime()+"";
          }
          /**
           * This method creates the relevant HyperHeuristic object from the
           * index given as a parameter. after the HyperHeuristic object is
           * created, its time limit is set.
           */
          private static HyperHeuristic loadHyperHeuristic(Program prog, long timeLimit, long seed) {
                    HyperHeuristic h = new SelectivePeturbativeHyperHeuristic(prog, seed);
                    h.setTimeLimit(timeLimit);
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
                              case 4:
                                        p = new TSP(instanceseed);
                                        break;
                              case 5:
                                        p = new VRP(instanceseed);
                                        break;
                              default:
                                        System.err.println("there is no problem domain with this index");
                                        System.exit(1);
                    }//end switch
                    return p;
          }

          public static void main(String[] args) throws Exception {

                    // TODO code application logic here
                  /*  int number_training_instances = 5;
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

                    //we first initialise the random number generator for this class
                    //it is useful to generate all of the random numbers from one seed, so that the experiments can be easily replicated
                    Random random_number_generator = new Random(12345);
                    long time_limit = 10000;

                    //this array is initialised with the indices of the instances that we wish to test the hyper-heuristics on
                    //for this example we arbitrarily choose the indices of five instances from each problem domain
                    int[][] instances_to_use = new int[4][];
                    int[] sat = {0, 1, 2, 3, 4};
                    int[] bp = {2, 3, 4, 5, 6};
                    int[] ps = {4, 5, 6, 7, 8};
                    int[] fs = {9, 0, 4, 2, 3};
                    instances_to_use[0] = sat;
                    instances_to_use[1] = bp;
                    instances_to_use[2] = ps;
                    instances_to_use[3] = fs;

                    for (int problem_domain_index = 0; problem_domain_index < 4; problem_domain_index++) {

                              //to ensure that all hyperheuristics begin from the same initial solution, we set a seed for each problem domain
                              long problem_domain_seed = random_number_generator.nextInt();

                              //loop through the five instances in the current problem domain
                              for (int instance = 0; instance < 5; instance++) {
                                        int instancetouse = instances_to_use[problem_domain_index][instance];

                                        System.out.println("Problem Domain " + problem_domain_index);
                                        System.out.println("	Instance " + instancetouse);

                                        //to ensure that all hyperheuristics begin from the same initial solution, we set a seed for each instance
                                        long instance_seed = problem_domain_seed * (instance + 1);

                                        Program prog = FlyWeight.getInstance().getProgram();
                                        GeneticOperators.full(prog, 5, instance_seed);

                                        Runner runner = new Runner(prog, problem_domain_index, instancetouse, instance_seed);
                                        runner.execute();
                                        //for this example, we use the record within each problem domain of the number of times each low level heuristic was called.
                                        //we sum the results to obtain the total number of times that a low level heuristic was called
                                        int[] i = runner.getHeuristicCallRecord();
                                        int counter = 0;
                                        for (int y : i) {
                                                  counter += y;
                                        }

                                        //we print the results of this hyper-heuristic on this problem instance
                                        //print the name of this hyper-heuristic
                                        System.out.print("\tHYPER HEURISTIC " + runner.toString());
                                        //print the best solution found within the time limit
                                        System.out.print("\t" + runner.getBestSolutionValue());
                                        //print the elapsed time in seconds
                                        System.out.print("\t" + (runner.getElapsedTime() / 1000.0));
                                        //print the number of calls to any low level heuristic
                                        System.out.println("\t" + counter);

                                        double[] fitnesstrace = runner.getFitnessTrace();
                                        for (double f : fitnesstrace) {
                                                  System.out.print(f + " ");
                                        }
                                        System.out.println();
                              }
                    }*/
          }
}
