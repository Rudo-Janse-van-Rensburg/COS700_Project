package geneticprogram;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import BinPacking.BinPacking;
import FlowShop.FlowShop;
import PersonnelScheduling.PersonnelScheduling;
import SAT.SAT;
import VRP.VRP;
import java.util.Random;
import travelingSalesmanProblem.TSP;

public class HyFlexHelper {

          /**
           * This method creates the HyperHeuristic object from the program
           * given as a parameter. after the HyperHeuristic object is created,
           * its time limit is set.
           */
          public static HyperHeuristic loadHyperHeuristic(Program prog, long timeLimit, Random rng) throws Exception {
                    HyperHeuristic h = new ExampleHyperHeuristic1(rng.nextLong(), prog);
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
          public static ProblemDomain loadProblemDomain(int index, long instanceseed) {
                    ProblemDomain problemDomain = null; 
                    switch (index) {
                              case 0:
                                        problemDomain = new SAT(instanceseed);
                                        break;
                              case 1:
                                        problemDomain = new BinPacking(instanceseed);
                                        break;
                              case 2:
                                        problemDomain = new PersonnelScheduling(instanceseed);
                                        break;
                              case 3:
                                        problemDomain = new FlowShop(instanceseed);
                                        break;
                              case 4:
                                        problemDomain = new TSP(instanceseed);
                                        break;
                              case 5:
                                        problemDomain = new VRP(instanceseed);
                                        break;
                              default:
                                        System.err.println("there is no problem with this index");
                                        System.exit(1);
                    }
                     return problemDomain;
          }
          
          /**
           * @param program
           * @param problem_index
           * @param instance_to_use
           * @param instance_seed
           * @param time_limit
           * @param rng
           * @return
           * @throws Exception 
           */
          public static HyperHeuristic prepareHyperHeuristic(Program program, int problem_index, int instance_to_use, long instance_seed,long time_limit, Random rng) throws Exception{
                    ProblemDomain problem_domain_object = HyFlexHelper.loadProblemDomain(problem_index, instance_seed);
                    HyperHeuristic hyper_heuristic_object = HyFlexHelper.loadHyperHeuristic(program,  time_limit, rng);
                    problem_domain_object.loadInstance(instance_to_use);
                   hyper_heuristic_object.loadProblemDomain(problem_domain_object);
                    return hyper_heuristic_object;
          }
          
}
