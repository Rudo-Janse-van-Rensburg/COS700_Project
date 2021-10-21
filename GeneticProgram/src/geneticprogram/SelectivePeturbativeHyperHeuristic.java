package geneticprogram;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import java.util.Random;

public class SelectivePeturbativeHyperHeuristic extends HyperHeuristic {

          private Program prog;

          public SelectivePeturbativeHyperHeuristic(Program prog, long seed) {
                    super(seed);
                    this.prog = prog;
          }

          @Override
          protected void solve(ProblemDomain problem) {
                    //initialise the variable which keeps track of the current objective function value
                    double current_obj_function_value = Double.POSITIVE_INFINITY;

                    //initialise the solution at index 0 in the solution memory array
                    problem.initialiseSolution(0);
                    
                    //the main loop of any hyper-heuristic, which checks if the time limit has been reached
                    while (false && !hasTimeExpired()) {
                              
                    }
          }

          @Override
          public String toString() {
                    try {
                              return Helper.toString(prog);
                    } catch (Exception e) {
                              e.printStackTrace();
                              System.exit(-1);
                    }
                    return "error";

          }

}
