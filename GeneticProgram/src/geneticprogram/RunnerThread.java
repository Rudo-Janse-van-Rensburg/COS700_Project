package geneticprogram;

import java.util.concurrent.CountDownLatch;

public class RunnerThread extends Thread {
          private CountDownLatch latch;
          private Runner runner;
          private volatile Thread t;


          public RunnerThread() {
                    runner = null;
          }
 
          @Override
          public void run() {
                    runner.execute();
                     latch.countDown();
          } 

          @Override
          public synchronized void start() {
                    t = null;
                    t = new Thread(this);
                    t.start();
          }

          public Runner getRunner() {
                    return runner;
          }

          
          public void reset(CountDownLatch latch, Program prog, long seed, int domain, int instance) throws Exception {
                    this.runner = new Runner(prog, domain, instance, seed);
                    this.latch = latch;
                   
          } 

          @Override
          public String toString() {
                    return "SPHH";
          }

}
