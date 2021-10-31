package COS700_Project;

import java.util.ArrayList;
import java.util.Arrays;
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
           * @return
           * @throws Exception
           */
          public double evaluate(Program prog) throws Exception {
                    if (prog != null) {
                              double f1 = 0;
                              CountDownLatch latch = new CountDownLatch(Parameters.getInstance().getK_folds());
                              ExecutorService  service = Executors.newFixedThreadPool(Parameters.getInstance().getK_folds());
                              ArrayList<F1Thread> threads = new ArrayList<>();
                              for (int k = 0; k < Parameters.getInstance().getK_folds(); k++) {
                                        F1Thread thread = ThreadFactory.instance().getF1Thread(latch, prog, k);
                                        threads.add(thread);
                                        service.execute(thread);
                              } 
                              latch.await();
                              for (F1Thread thread: threads) {
                                        f1 += thread.getF1();
                              }
                              service.shutdown();
                              f1 /= Parameters.getInstance().getK_folds() * 1.0;
                               
                              return f1 >= 0 ? f1: 0;
                    } else {
                              throw new Exception("Cannot evaluate null program.");
                    }
          }
 

}
