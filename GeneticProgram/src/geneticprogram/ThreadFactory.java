/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticprogram;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rudo
 */
public class ThreadFactory {
          private static ThreadFactory factory = null;
          private ThreadFactory() {
          }

          public static ThreadFactory instance(){
                    if(factory == null){
                              factory = new ThreadFactory();
                    }
                    return factory;
          }
          
          public  GeneticOperatorThread getGeneticOperatorThread(CountDownLatch latch, Program[] parents, char operation, int max_depth, long seed,int [][] training_instances) throws Exception {
                    return new GeneticOperatorThread(latch, parents, operation, max_depth, seed,training_instances);
          }
          
          public RunnerThread getRunnerThread(CountDownLatch latch, Program prog, long seed, int domain, int instance) throws Exception{
                    return new RunnerThread(latch, prog, seed, domain, instance);
          }
          
           
}

 

class RunnerThread extends Thread{
          private final CountDownLatch latch;
          private final Runner runner; 
 
          @Override
          public void run() {
                    runner.execute();
                     latch.countDown();
          }  
          
          public Runner getRunner() {
                    return runner;
          }

          
          public   RunnerThread(CountDownLatch latch, Program prog, long seed, int domain, int instance) throws Exception {
                    this.runner = new Runner(prog, domain, instance, seed);
                    this.latch = latch;
                   
          } 

          @Override
          public String toString() {
                    return "SPHH";
          }
}


class GeneticOperatorThread extends Thread {

                    private final Program[] parents;
                    private final char operation;
                    private final int max_depth;
                    private final long seed;
                    private final CountDownLatch latch;
                    private final int [][] training_instances;

                    public GeneticOperatorThread(CountDownLatch latch, Program[] parents, char operation, int max_depth, long seed,int [][] training_instances) throws Exception {
                              this.parents = new Program[parents.length];
                              setParents(parents);
                              this.operation = operation;
                              this.max_depth = max_depth;
                              this.seed = seed;
                              this.latch = latch;
                              this.training_instances = training_instances;
                    }

                    @Override
                    public void run() { 
                              try {
                                        switch (operation) {
                                                  case Meta.MUTATE:
                                                            GeneticOperators.mutate(parents[0], seed);
                                                            parents[0].setFitness(Fitness.getInstance().evaluate(parents[0], training_instances, seed));
                                                            latch.countDown();
                                                            break;
                                                  case Meta.CROSSOVER:
                                                            GeneticOperators.crossover(parents[0], parents[1], seed);
                                                            parents[0].setFitness(Fitness.getInstance().evaluate(parents[0], training_instances, seed));
                                                            parents[1].setFitness(Fitness.getInstance().evaluate(parents[1], training_instances, seed));
                                                            latch.countDown();
                                                            break;
                                                  case Meta.HOIST:
                                                            GeneticOperators.hoist(parents[0], seed);
                                                            parents[0].setFitness(Fitness.getInstance().evaluate(parents[0], training_instances, seed));
                                                            latch.countDown();
                                                            break;
                                                  case Meta.GROW:
                                                            GeneticOperators.grow(parents[0], max_depth, seed);
                                                            parents[0].setFitness(Fitness.getInstance().evaluate(parents[0], training_instances, seed));
                                                            latch.countDown();
                                                            break;
                                                  case Meta.FULL:
                                                            GeneticOperators.full(parents[0], max_depth, seed);
                                                            parents[0].setFitness(Fitness.getInstance().evaluate(parents[0], training_instances, seed));
                                                            latch.countDown();
                                                            break;
                                        }
                              } catch (Exception e) {
                                        e.printStackTrace();
                              }
                    }

                    public Program[] getParents() {
                              return parents;
                    }

                    public char getOperation() {
                              return operation;
                    }

                    private void setParents(Program[] parents) throws Exception {
                              for (int i = 0; i < parents.length; i++) {
                                        Program prog = FlyWeight.getInstance().getProgram();
                                        prog.copy(parents[i]);
                                        this.parents[i] = prog;
                              }
                    }

          }