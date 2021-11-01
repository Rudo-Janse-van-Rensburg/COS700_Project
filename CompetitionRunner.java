package COS700_Project;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompetitionRunner extends Thread {

          private final Program prog;
          private double score;

          public CompetitionRunner(Program prog) {
                    this.prog = prog;
          }

          @Override
          public void run() {
                    score = 0;
                    double[][] arr_scores = new double[CompetitionParameters.instance().domains][CompetitionParameters.instance().instances];
                    List<Double>[][] list_run_scores = new ArrayList[CompetitionParameters.instance().domains][CompetitionParameters.instance().instances];

                    CountDownLatch latch = new CountDownLatch(CompetitionParameters.instance().domains * CompetitionParameters.instance().instances * CompetitionParameters.instance().numberofruns);
                    ExecutorService comp_service = Executors.newFixedThreadPool(CompetitionParameters.instance().domains * CompetitionParameters.instance().instances * CompetitionParameters.instance().numberofruns);
                    try {
                              List<RunnerThread> threads = new ArrayList<>();
                              for (int i = 0; i < CompetitionParameters.instance().instances; i++) {
                                        for (int problem = 0; problem < CompetitionParameters.instance().domains; problem++) {
                                                  for (int run = 0; run < CompetitionParameters.instance().numberofruns; run++) {
                                                            list_run_scores[problem][i] = new ArrayList<>();
                                                            RunnerThread thread = ThreadFactory.instance().getRunnerThread(
                                                                      latch,
                                                                      prog,
                                                                      problem,
                                                                      i,
                                                                      CompetitionParameters.instance().time,
                                                                      CompetitionParameters.instance().instanceseeds[problem][i][run]
                                                            );
                                                            comp_service.execute(thread);
                                                            threads.add(thread);
                                                  }

                                        }
                              }
                              latch.await();
                              comp_service.shutdown();
                              StringBuilder content = new StringBuilder();
                              content.append("problem, instance, best_solution_value, elapsed_time, run_time, total_heuristic_call \n");
                              for (RunnerThread thread : threads) {
                                        String problem = "";
                                        switch (thread.domain) {
                                                  case 0:
                                                            problem = "SAT";
                                                            break;
                                                  case 1:
                                                            problem = "BinPacking";
                                                            break;
                                                  case 2:
                                                            problem = "PersonnelScheduling";
                                                            break;
                                                  case 3:
                                                            problem = "FlowShop";
                                                            break;
                                                  case 4:
                                                            problem = "TSP";
                                                            break;
                                                  case 5:
                                                            problem = "VRP";
                                                            break;
                                                  default: 
                                                            System.exit(-1);
                                        }
                                        content
                                                  .append(problem).append(" ,")
                                                  .append(thread.instance).append(" ,")
                                                  .append(thread.getRunner().getBestSolutionValue()).append(" ,")
                                                  .append(thread.getRunner().getElapsedTime() / 1000.0).append(" ,")
                                                  .append(thread.getRunner().getRunTime() / 1000.0).append(" ,")
                                                  .append(thread.getRunner().getTotalHeuristicCalls()).append(" \n");
                              }
                              threads.clear();
                              Path pathtofile = Paths.get("./result - " + Parameters.getInstance().toString()+".csv");
                              Files.write(pathtofile, content.toString().getBytes(), StandardOpenOption.CREATE);
                    } catch (Exception ex) {
                              ex.printStackTrace();
                    }

          }

          private void oldrun() {

                    String resultsfolder;
                    CountDownLatch latch = new CountDownLatch(CompetitionParameters.instance().domains * CompetitionParameters.instance().instances);
                    ExecutorService comp_service = Executors.newFixedThreadPool(CompetitionParameters.instance().domains * CompetitionParameters.instance().instances);

                    List<CompetitionRunnerThread> threads = new ArrayList<>();
                    for (int problem = 0; problem < CompetitionParameters.instance().domains; problem++) {
                              System.out.println(problem + " : ");
                              for (int i = 0; i < CompetitionParameters.instance().instances; i++) {
                                        try {

                                                  System.out.print(CompetitionParameters.instance().instances_to_use[problem][i] + " ");
                                                  CompetitionRunnerThread thread = ThreadFactory.instance().getCompetitionRunnerThread(
                                                            latch,
                                                            prog,
                                                            problem,
                                                            CompetitionParameters.instance().instances_to_use[problem][i],
                                                            CompetitionParameters.instance().time,
                                                            CompetitionParameters.instance().instanceseeds[problem][i],
                                                            CompetitionParameters.instance().numberofruns
                                                  );
                                                  comp_service.execute(thread);
                                                  threads.add(thread);

                                        } catch (Exception ex) {
                                                  ex.printStackTrace();
                                        }

                              }
                              System.out.println("");

                    }
                    try {
                              latch.await();
                    } catch (InterruptedException ex) {
                              ex.printStackTrace();
                    }
                    comp_service.shutdown();
          }

          public static class Score implements Comparable<Score> {

                    int num;
                    double score;

                    public Score(int n, double s) {
                              num = n;
                              score = s;
                    }

                    public int compareTo(Score o) {
                              Score obj = (Score) o;
                              if (this.score < obj.score) {
                                        return -1;
                              } else if (this.score == obj.score) {
                                        return 0;
                              } else {
                                        return 1;
                              }
                    }
          }

}

class CompetitionParameters {

          /* These are parameters which can be changed.
	 * time - set to ten minutes, but this may need to change depending on your machine spec. Refer to http://www.asap.cs.nott.ac.uk/chesc2011/benchmarking.html
	 * numberofhyperheuristics - the number you wish to test in the same run
	 * problem - the selected domain
	 * instance - the selected instance of the problem domain. This should be between 0-4 inclusive
	 * rng - select a random seed
           */
          public final long time = 600000;//600000;//600000;  
          public final Random rng;

          /* These are parameters were used for the competition, so if they are changed then the results may not be comparable to those of the competition
           */
          public final long[][][] instanceseeds;
          public final int numberofruns = 31;
          public final int domains = 6;
          public final int instances = 5;
          // int[][] instances_to_use = new int[domains][];
          /*
		 * These instances are generated by CompetitionInstanceSelector.java
		 * Ten instances are included for each problem domain, but these are the instances selected for use in the competition.
		 * The last two instances of the first four domains were hidden instances.
           */
          public final int[][] instances_to_use = {
                    {3, 5, 4, 10, 11},
                    {7, 1, 9, 10, 11},
                    {5, 9, 8, 10, 11},
                    {1, 8, 3, 10, 11},
                    {0, 8, 2, 7, 6},
                    {6, 2, 5, 1, 9}
          };

          private static CompetitionParameters singleton = null;

          private CompetitionParameters() {
                    rng = new Random(123456789);
                    instanceseeds = new long[domains][instances][numberofruns];

                    for (int x = 0; x < domains; x++) {
                              for (int y = 0; y < instances; y++) {
                                        for (int r = 0; r < numberofruns; r++) {
                                                  instanceseeds[x][y][r] = rng.nextLong();
                                        }
                              }
                    }
          }

          public static CompetitionParameters instance() {
                    if (singleton == null) {
                              singleton = new CompetitionParameters();
                    }
                    return singleton;
          }
}
