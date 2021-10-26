package geneticprogram;

import geneticprogram.ThreadFactory;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;

public class Evolution {
 

          private static Evolution instance = null;
          private static Generation curr = null,
                    next = null;
          private final Program best_program;
          private int generation; 
          private Evolution() throws Exception { 
                    Data.initialiseData();
                    Randomness.getInstance();
                    curr = FlyWeight.getInstance().getGeneration();
                    next = FlyWeight.getInstance().getGeneration();

                    best_program = new Program();
                   
                    createInitialPopulation();
          }

          /**
           * @return Evolution singleton
           * @throws Exception
           */
          public static Evolution getInstance() throws Exception {
                    if (instance == null) {
                              instance = new Evolution();
                    }
                    return instance;
          }

          /**
           * @throws Exception initial population
           */
          private void createInitialPopulation() throws Exception {
                     ExecutorService go_service = Executors.newFixedThreadPool(Parameters.getInstance().getPopulation_size());
                    Randomness.getInstance().reseed();
                    curr.recycle();
                    next.recycle();
                    generation = 0;
                    int depths = Parameters.getInstance().getMain_max_depth();
                    int ipg = (int) Math.ceil(Parameters.getInstance().getPopulation_size() / ((depths - 2) * 1.0));   //individuals per generation 
                    boolean has_capacity = true;
                    int position = 0;
                    List<GeneticOperatorThread> go_tasks = new ArrayList<>();
                    CountDownLatch latch = new CountDownLatch(Parameters.getInstance().getPopulation_size());
                    for (int depth = depths - 1; has_capacity && depth >= 2; depth--) {
                              int individual = 0;
                              for (; position < Parameters.getInstance().getPopulation_size() && has_capacity && individual < ipg;) {
                                        go_tasks.add(ThreadFactory.instance().getGeneticOperatorThread(latch, new Program[]{FlyWeight.getInstance().getProgram()}, Meta.FULL, depth, Randomness.getInstance().getRandomLong(), curr.getTraining_instaces()));

                                        go_tasks.add(ThreadFactory.instance().getGeneticOperatorThread(latch, new Program[]{FlyWeight.getInstance().getProgram()}, Meta.GROW, depth, Randomness.getInstance().getRandomLong(), curr.getTraining_instaces()));

                                        has_capacity = go_tasks.size() < Parameters.getInstance().getPopulation_size();
                              }
                    }
                    while (has_capacity) {
                              go_tasks.add(ThreadFactory.instance().getGeneticOperatorThread(latch, new Program[]{FlyWeight.getInstance().getProgram()}, Meta.FULL, Parameters.getInstance().getMain_max_depth(), Randomness.getInstance().getRandomLong(), curr.getTraining_instaces()));
                              has_capacity = go_tasks.size() < Parameters.getInstance().getPopulation_size();
                    }

                    for (int i = 0; i < Parameters.getInstance().getPopulation_size(); i++) {
                              go_service.execute(go_tasks.get(i));
                    }
                    latch.await();
                    go_service.shutdown();

                    for (int i = 0; i < Parameters.getInstance().getPopulation_size(); i++) {
                              curr.add(go_tasks.get(i).getParents()[0]);
                    }

          }

          /**
           * @return @throws Exception
           */
          public boolean evolveGeneration() throws Exception {
                    if (!curr.isEmpty()) {
                              if ((generation + 1) < Parameters.getInstance().getMax_generation()) {
                                        next.recycle();
                                        int num_crossover = (int) Math.ceil(Parameters.getInstance().getCrossover_chance() * Parameters.getInstance().getPopulation_size());
                                        int num_mutation = (int) Math.ceil(Parameters.getInstance().getMutation_chance() * Parameters.getInstance().getPopulation_size());
                                        int num_hoist = (int) Math.ceil(Parameters.getInstance().getHoist_chance() * Parameters.getInstance().getPopulation_size());
                                        // int num_edit = (int) Math.ceil(Parameters.getInstance().getEdit_chance() * Parameters.getInstance().getPopulation_size());
                                        
                                        int num_threads = num_crossover + num_mutation + num_hoist/* + num_edit*/;
                                        ExecutorService go_service = Executors.newFixedThreadPool(num_threads);
                                        if (Meta.debug) {
                                                  System.out.format("number threads   : %d\n ", num_threads);
                                        }
                                        CountDownLatch latch = new CountDownLatch(num_threads);
                                        List<GeneticOperatorThread> go_tasks = new ArrayList<>();
                                        //ArrayList<GeneticOperatorThread> threads = FlyWeight.getInstance().getGeneticOperatorThreads();
                                        long[] seeds = new long[num_threads];
                                        for (int i = 0; i < num_threads; i++) {
                                                  seeds[i] = Randomness.getInstance().getRandomLong();
                                        }
                                        int individual = 0;
                                        do {
                                                  if (num_threads > 0 && num_crossover > 0) {
                                                            Program a = Selection.getInstance(Selection.tournament).select(curr);
                                                            Program b = Selection.getInstance(Selection.tournament).select(curr);
                                                            GeneticOperatorThread crossover = ThreadFactory.instance().getGeneticOperatorThread(latch, new Program[]{a, b}, Meta.CROSSOVER, seeds[individual], curr.getTraining_instaces());
                                                            go_tasks.add(crossover);
                                                            /*crossover.reset(latch, seeds[individual], new Program[]{a, b}, Meta.CROSSOVER);
                                                            threads.add(crossover);*/
                                                            --num_crossover;
                                                            --num_threads;
                                                  }
                                                  if (num_threads > 0 && num_mutation > 0) {
                                                            Program mutant = Selection.getInstance(Selection.tournament).select(curr);
                                                            GeneticOperatorThread mutation = ThreadFactory.instance().getGeneticOperatorThread(latch, new Program[]{mutant}, Meta.MUTATE, seeds[individual], curr.getTraining_instaces());
                                                            go_tasks.add(mutation);
                                                            /*mutation.reset(latch, seeds[individual], new Program[]{mutant}, Meta.MUTATE);
                                                            threads.add(mutation);*/
                                                            --num_threads;
                                                            --num_mutation;
                                                  }
                                                  if (num_threads > 0 && num_hoist > 0) {
                                                            Program hoist = Selection.getInstance(Selection.tournament).select(curr);
                                                            GeneticOperatorThread hoisted = ThreadFactory.instance().getGeneticOperatorThread(latch, new Program[]{hoist}, Meta.HOIST, seeds[individual], curr.getTraining_instaces());
                                                            go_tasks.add(hoisted);
                                                            /*hoisted.reset(latch, seeds[individual], new Program[]{hoist}, Meta.HOIST);
                                                            threads.add(hoisted);*/
                                                            --num_threads;
                                                            --num_hoist;
                                                  }

                                        } while (num_threads > 0);

                                        for (int i = 0; i < go_tasks.size(); i++) {
                                                  go_service.execute(go_tasks.get(i));
                                        }
                                        latch.await();
                                        go_service.shutdown();
                                        
                                        for (int i = 0; i < go_tasks.size(); i++) { 
                                                   if (go_tasks.get(i).getOperation() == Meta.CROSSOVER) {
                                                            next.add(go_tasks.get(i).getParents()[0]);
                                                            next.add(go_tasks.get(i).getParents()[1]);
                                                  } else {
                                                            next.add(go_tasks.get(i).getParents()[0]);
                                                  }
                                        } 
                                        curr.recycle();
                                       
                                        for (int  i = 0; i < Parameters.getInstance().getPopulation_size(); i++) {
                                                  curr.add(next.getIndividual(i));
                                        }
                                        next.clear();
                                        ++generation;
                                        return true;
                              } else {
                                        return false;
                              }
                    } else {
                              throw new Exception("Cannot evolve empty generation.");
                    }
          }

          public Program getBest_program() {
                    return best_program;
          }

          public void print() throws Exception {
                    System.out.println("=======================================");
                    System.out.format("GENERATION   #%d%n", generation);
                    System.out.println("---------------------------------------");
                    System.out.format("    occupancy     :   %d\n", curr.getCapacity());
                    System.out.format("    avergage fitness     :   %f\n", curr.getAverage_fitness());
                    //System.out.println("    fitnesses            :   " + Arrays.toString(curr.getFitnesses()));
                    System.out.format("    best fitness         :   %f\n", curr.getBest_fitness());
                    System.out.format("    worst  fitness      :   %f\n", curr.getWorst_fitness());
                    //System.out.format("    best program         :   \n%s\n",Helper.toString(best_program));
                    System.out.println("=======================================");
          }

}
