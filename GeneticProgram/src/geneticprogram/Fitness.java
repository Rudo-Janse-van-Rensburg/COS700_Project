package geneticprogram;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class Fitness {

          private static Fitness singleton = null;

          private Fitness() {
          }

          /**
           * @return @throws Exception
           */
          public static Fitness getInstance() throws Exception {
                    if (singleton == null) {
                              singleton = new Fitness();
                    }
                    return singleton;
          }

          /**
           * @param prog
           * @param training_instances
           * @param seed
           * @return
           * @throws Exception
           */
          public double evaluate(Program prog, int[][] training_instances, long seed) throws Exception {
                    if (prog != null) {
                              int number_domains = 4; 
                              CountDownLatch latch = new CountDownLatch(number_domains * Parameters.getInstance().getTraining_instances());
                              ArrayList<RunnerThread> runner_threads = FlyWeight.getInstance().getRunnerThreads();
                              for (int d = 0; d < number_domains; d++) {
                                        for (int i = 0; i < Parameters.getInstance().getTraining_instances(); i++) {
                                                  RunnerThread thread = FlyWeight.getInstance().getRunnerThread();
                                                  thread.reset(latch, prog, seed, d, training_instances[d][i]);
                                                  runner_threads.add(thread);
                                                  thread.run();
                                        }
                              }
                              
                              latch.await();
                              
                              double fitness = 0;
                              for (int d = 0; d < number_domains; d++) {
                                        for (int i = 0; i < Parameters.getInstance().getTraining_instances(); i++) {
                                                  RunnerThread thread = runner_threads.remove(0);
                                                  fitness += thread.getRunner().getBestSolutionValue();
                                                  FlyWeight.getInstance().addRunnerThread(thread);
                                        }
                              }
                              FlyWeight.getInstance().addRunnerThreads(runner_threads);
                              
                              return fitness / (1.0 * Math.max(1, number_domains * Parameters.getInstance().getTraining_instances()));
                    } else {
                              throw new Exception("Cannot evaluate null program.");
                    }
          }

}
