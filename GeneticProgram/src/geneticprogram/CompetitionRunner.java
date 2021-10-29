package geneticprogram;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompetitionRunner extends Thread {

          private final Program prog;

          public CompetitionRunner(Program prog) {
                    this.prog = prog;
          }

          @Override
          public void run() {
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
                    /*
                    System.out.println("PROBLEM DOMAIN " + resultsfolder);
                    printer_content.add("PROBLEM DOMAIN " + resultsfolder);
                    int instancetouse = instances_to_use[problem][instance];
                    System.out.println("  instance " + instancetouse + " ");
                    printer_content.add("  instance " + instancetouse + " ");
                    //FileWriter f = new FileWriter("results/" + resultsfolder + "/instance" + instance + ".txt");
                    //PrintWriter buffprint = new PrintWriter(f);
                    for (int run = 0; run < numberofruns; run++) {
                              instanceseed = instanceseeds[problem][instance][run];
                              System.out.println("    RUN " + run + " " + instanceseed);
                              printer_content.add("    RUN " + run);
                              for (int hyperheuristic = 0; hyperheuristic < 1; hyperheuristic++) {

                                        ProblemDomain p = loadProblemDomain(problem);
                                        HyperHeuristic h = loadHyperHeuristic(this.prog, time, rng);
                                        System.out.print("      HYPER HEURISTIC " + h.toString());
                                        p.loadInstance(instancetouse);
                                        h.loadProblemDomain(p);
                                        long initialTime2 = System.currentTimeMillis();
                                        h.run();
                                        int[] i = p.getHeuristicCallRecord();
                                        int counter = 0;
                                        for (int y : i) {
                                                  counter += y;
                                        }
                                        System.out.println("\t" + h.getBestSolutionValue() + "\t" + (h.getElapsedTime() / 1000.0) + "\t" + (System.currentTimeMillis() - initialTime2) / 1000.0 + "\t" + counter);
                                        printer_content.add("\t" + h.getBestSolutionValue() + "\t" + (h.getElapsedTime() / 1000.0) + "\t" + (System.currentTimeMillis() - initialTime2) / 1000.0 + "\t" + counter);

                                        buffer_printer_content.append(h.getBestSolutionValue()).append(" ");

                                        StringBuilder buffer_printer_content_1 = new StringBuilder();
                                        double[] u = h.getFitnessTrace();
                                        for (double y : u) {
                                                  buffer_printer_content_1.append(y).append(" ");
                                        }
                                        buffer_printer_content_1.append("\n");
                                        try {
                                                  Path pathtofile = Paths.get("./results/" + resultsfolder + "/time" + instance + ".txt");
                                                  if (!Files.exists(pathtofile)) {
                                                            Files.createDirectories(pathtofile.getParent());
                                                            Files.createFile(pathtofile);
                                                  }
                                                  Files.write(pathtofile, printer_content.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                                        } catch (Exception e) {
                                                  e.printStackTrace();
                                        }

                              }
                              buffer_printer_content.append("\n");
                    }
                    try {
                              Path pathtofile = Paths.get("./results/" + problem + "-" + instance + ".txt");
                              if (!Files.exists(pathtofile)) {
                                        Files.createDirectories(pathtofile.getParent());
                                        Files.createFile(pathtofile);
                              }
                              Files.write(pathtofile, printer_content.toString().getBytes(), StandardOpenOption.CREATE);
                              pathtofile = Paths.get("./results/" + resultsfolder + "/instance" + instance + ".txt");
                              if (!Files.exists(pathtofile)) {
                                        Files.createDirectories(pathtofile.getParent());
                                        Files.createFile(pathtofile);
                              }
                              Files.write(pathtofile, buffer_printer_content.toString().getBytes(), StandardOpenOption.CREATE);

                    } catch (Exception e) {
                              e.printStackTrace();
                    }
                     */
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
          public final long time = 100;//600000;  
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
