package COS700_Project;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
                              ArrayList<RunnerThread> tasks = FlyWeight.getInstance().getRunnerThreads();
                              ExecutorService run_service = Executors.newFixedThreadPool(number_domains * Parameters.getInstance().getTraining_instances());
                              for (int d = 0; d < number_domains; d++) {
                                        for (int i = 0; i < Parameters.getInstance().getTraining_instances(); i++) {
                                                  RunnerThread thread = ThreadFactory.instance().getRunnerThread(latch, prog, seed, d, training_instances[d][i]);
                                                  tasks.add(thread); 
                                                  run_service.execute(thread);
                                        }
                              }
                              
                              latch.await();
                              run_service.shutdown();
                              
                              double fitness = 0;
                              for (int d = 0; d < number_domains *  Parameters.getInstance().getTraining_instances(); d++) {
                                        fitness +=  tasks.get(d).getRunner().getBestSolutionValue();
                              } 
                              
                              return fitness / (1.0 * Math.max(1, number_domains * Parameters.getInstance().getTraining_instances()));
                    } else {
                              throw new Exception("Cannot evaluate null program.");
                    }
          }

}
