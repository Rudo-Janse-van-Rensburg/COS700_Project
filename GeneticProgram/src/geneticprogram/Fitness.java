package geneticprogram;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Fitness {

          private static Fitness singleton = null;

          private Fitness() {
          }

          /**
           * *
           * @return
           * @throws Exception
           */
          public static Fitness getInstance() throws Exception {
                    if (singleton == null) {
                              singleton = new Fitness();
                    }
                    return singleton;
          }

          /**
           * @param prog
           * @param seed
           * @return
           * @throws Exception
           */
          public double evaluate(Program prog,Random rand) throws Exception {
                    if (prog != null) {
                              return _hyflex(prog,rand,rand.nextLong());
                    } else {
                              throw new Exception("Cannot evaluate null program.");
                    }
          }

          private  double _hyflex(Program prog,Random rand, long seed) throws Exception {
                    int instances = 10;
                    int number_domains = 4;
                    double[][] your_results = FlyWeight.getInstance().getProblem_score_arrays();
                    //int[][] your_instances = new int[number_domains][instances];
                    int [] training_instances = new int[Parameters.getInstance().getTraining_instances()];
                    ArrayList<Integer> ti = FlyWeight.getInstance().getArrayListInt();
                    for (int i = 0; i < 10; i++) {
                              ti.add(i);
                    }
                    FlyWeight.getInstance().addArrayListInt(ti);
                    for (int i = 0; i < Parameters.getInstance().getTraining_instances(); i++) {
                              training_instances[i] = ti.remove(rand.nextInt(ti.size()));
                    }
                    
                    CountDownLatch latch = new CountDownLatch(number_domains*instances);
                    ArrayList<RunnerThread> runner_threads = FlyWeight.getInstance().getRunnerThreads();
                    for (int d = 0; d < number_domains; d++) {
                              for (int i = 0; i < instances; i++) {
                                        RunnerThread thread = FlyWeight.getInstance().getRunnerThread();
                                        thread.reset(latch, prog, seed, d, training_instances[i]);
                                        thread.start();
                                        runner_threads.add(thread);
                              }
                    }
                    latch.await();
                    for (int d = 0; d < number_domains; d++) {
                              for (int i = 0; i < instances; i++) {
                                        RunnerThread thread = runner_threads.remove(0);
                                        your_results[d][i] = thread.getBestSolutionValue();
                                        FlyWeight.getInstance().addRunnerThread(thread);
                              }
                    }
                    FlyWeight.getInstance().addRunnerThreads(runner_threads);
                    int hyperheuristics = 9;
                    double[] scores = new double[hyperheuristics];
                    double[] base_scores = {10, 8, 6, 5, 4, 3, 2, 1, 0, 0, 0, 0, 0, 0, 0};
                    double total_domain_scores = 0;
                    for (double g : base_scores) {
                              total_domain_scores += g;
                    }
                    total_domain_scores *= instances;
                    
                    
                    for (int domain = 0; domain < number_domains; domain++) {
                              double[] domain_scores = FlyWeight.getInstance().getScoreArray(); 
                              for (int instance = 0; instance < instances; instance++) { 
                                        double[] res = FlyWeight.getInstance().getScoreArray();
                                        System.arraycopy(Score.results[domain][instance], 0, res, 0, 8);
                                        res[8] = your_results[domain][instance];
                                        //double[] res = Score.results[domain][instance];
                                        ArrayList<Score> al = FlyWeight.getInstance().getScoreArrayList();
                                        for (int s = 0; s < res.length; s++) {
                                                  Score obj = new Score(s, res[s]);
                                                  al.add(obj);
                                        }
                                        Collections.sort(al);
                                        double previous_score = Double.POSITIVE_INFINITY;
                                        int score_index = 0;
                                        double tie_average = 0;
                                        int tie_number = 0;
                                        ArrayList<Integer> list = FlyWeight.getInstance().getArrayListInt();
                                        while (true) {
                                                  Score test1 = null;
                                                  if (score_index < al.size()) {
                                                            test1 = al.get(score_index);
                                                  }
                                                  if (test1 == null || test1.score != previous_score) {
                                                            double average = tie_average / list.size();
                                                            for (int f = 0; f < list.size(); f++) {
                                                                      scores[list.get(f)] += average;
                                                                      domain_scores[list.get(f)] += average;
                                                            }
                                                            if (test1 == null) {
                                                                      break;//all HH have been given a score
                                                            }
                                                            tie_number = 0;
                                                            tie_average = 0;
                                                            FlyWeight.getInstance().addArrayListInt(list);
                                                            list = FlyWeight.getInstance().getArrayListInt();
                                                            list.add(test1.num);
                                                            tie_number++;
                                                            tie_average += base_scores[score_index];
                                                  } else {
                                                            list.add(test1.num);
                                                            tie_number++;
                                                            tie_average += base_scores[score_index];
                                                  }
                                                  previous_score = test1.score;
                                                  score_index++;
                                        }
                                        double domain_total = 0;
                                        for (double domain_score : domain_scores) {
                                                  domain_total += domain_score;
                                        }
                                        if (Math.round(domain_total) != total_domain_scores) {
                                                  System.exit(-1);
                                        }
                              }

                    }
                    FlyWeight.getInstance().addProblem_score_arrays(your_results);
                    return scores[8];
          }

}
