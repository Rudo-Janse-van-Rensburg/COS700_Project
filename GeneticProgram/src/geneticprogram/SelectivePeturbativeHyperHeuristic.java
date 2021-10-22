package geneticprogram;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

public class SelectivePeturbativeHyperHeuristic extends HyperHeuristic {

          private Program prog;

          public SelectivePeturbativeHyperHeuristic(Program prog, long seed) {
                    super(seed);
                    this.prog = prog;
          }

          @Override
          protected void solve(ProblemDomain problem) {
                    try {
                              //initialise the variable which keeps track of the current objective function value
                              double current_obj_function_value = Double.POSITIVE_INFINITY;

                              //initialise the solution at index 0 in the solution memory array
                              problem.initialiseSolution(0);

                              double[] attributes = Data.initialiseData().getData_instance();

                              //the main loop of any hyper-heuristic, which checks if the time limit has been reached
                              while (!hasTimeExpired()) {
                                        int heuristic_to_apply = (int) Interpreter.getInstance().Interpret(prog, attributes);

                                        double new_obj_function_value = problem.applyHeuristic(heuristic_to_apply, 0, 1);

                                        double delta = current_obj_function_value - new_obj_function_value;
                                        Data.initialiseData().add(heuristic_to_apply, delta);
                                        //all of the problem domains are implemented as minimisation problems. A lower fitness means a better solution.
                                        if (delta > 0) {
                                                  //if there is an improvement then we 'accept' the solution by copying the new solution into memory index 0
                                                  problem.copySolution(1, 0);
                                                  //we also set the current objective function value to the new function value, as the new solution is now the current solution
                                                  current_obj_function_value = new_obj_function_value;
                                        } else {
                                                  //if there is not an improvement in solution quality then we accept the solution probabilistically
                                                  //if (rng.nextBoolean()) {
                                                  if (rng.nextDouble() > Parameters.getInstance().getAcceptance_threshold()) {
                                                            //the process for 'accepting' a solution is the same as above
                                                            problem.copySolution(1, 0);
                                                            current_obj_function_value = new_obj_function_value;
                                                  }
                                        }
                                        //one iteration has been completed, so we return to the start of the main loop and check if the time has expired
                              }
                    } catch (Exception ex) {
                              ex.printStackTrace();
                    }

          }

          @Override
          public String toString() {
                    try {
                              return Helper.toString(prog);
                    } catch (Exception e) {
                              System.exit(-1);
                    }
                    return "error";

          }

}
