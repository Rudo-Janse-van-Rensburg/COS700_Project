package geneticprogram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Fitness {

          public static final byte 
                    hyflex = 0,
                    f1 = 1,
                    recall = 2,
                    precision = 3,
                    accuracy = 4;

          private byte measure = f1;
          private static Fitness singleton = null;

          private Fitness(byte measure) {
                    this.measure = measure;
          }

          /**
           * *
           * @param measure
           * @return
           * @throws Exception
           */
          public static Fitness getInstance(byte measure) throws Exception {
                    if (measure >= 0 && measure <= 43) {
                              if (singleton == null) {
                                        singleton = new Fitness(measure);
                              }
                              singleton.measure = measure;
                              return singleton;
                    } else {
                              throw new Exception("Invalid fitness measure.");
                    }

          }

          /**
           * @param prog
           * @return
           * @throws Exception
           */
          public double evaluate(Program prog) throws Exception {
                    if (prog != null) {
                              switch (this.measure) {
                                        case hyflex:
                                                  return _hyflex(prog);
                                        case f1:
                                                  return _f1(prog); 
                                        default:
                                                  return _hyflex(prog);
                              }
                    } else {
                              throw new Exception("Cannot evaluate null program.");
                    }
          }

          
          
          private synchronized static double _hyflex(Program prog) throws Exception {
                    
                    
                    
                    int instances                  = 10;
                    int number_domains   = 4;
                    double[][] your_results = FlyWeight.getInstance().getProblem_score_arrays();
                    int[][] your_instances    = new int[number_domains][instances]; 
                    int hyperheuristics                     = 9;
                    double[] scores                          = new double[hyperheuristics];
                    double[] base_scores              = {10, 8, 6, 5, 4, 3, 2, 1, 0, 0, 0, 0, 0, 0, 0};
                    double total_domain_scores = 0;
                    for (double g : base_scores) {
                              total_domain_scores += g;
                    }
                    total_domain_scores *= instances;
                    for (int domain = 0; domain < number_domains; domain++) {
                              double[] domain_scores = FlyWeight.getInstance().getScoreArray();
                             
                              for (int instance = 0; instance < instances; instance++) {
                                        int domain_instance = your_instances[domain][instance];
                                        double[] res = FlyWeight.getInstance().getScoreArray(); 
                                        System.arraycopy(Score.results[domain][instance], 0, res, 0, 8);
                                        res[8] = your_instances[domain][instance];
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

          private static double _f1(Program prog) throws Exception {
                    int number_classes = Data.initialiseData().getNumberClasses(),
                              target,
                              output,
                              fold_instances = (int) Math.ceil(Data.initialiseData().getNumber_instances() / Parameters.getInstance().getK_folds() * 1.0);
                    double avg_f1 = 0, prec = 0, rec = 0;
                    int[] tp, fp, fn;
                    double[] instance;
                    for (int k = 0; k < Parameters.getInstance().getK_folds(); k++) {

                              List<double[]> data = Data.initialiseData().getFold(k);
                              tp = new int[number_classes];
                              fp = new int[number_classes];
                              fn = new int[number_classes];
                              /*for (int i = 0; i < number_classes; i++) {
                tp[i] = 0;
                fp[i] = 0;
                fn[i] = 0;
            }*/
                              for (int i = 0; i < fold_instances; i++) {
                                        instance = data.get(i);
                                        target = (int) instance[Data.initialiseData().getNumberAttributes()];
                                        output = (int) Interpreter.getInstance().Interpret(prog, instance);

                                        if (target == output) {
                                                  tp[output] += 1;
                                        } else {
                                                  fn[target] += 1;
                                                  fp[output] += 1;
                                        }
                              }
                              if (false && Meta.debug) {
                                        System.out.println(" tp :   " + Arrays.toString(tp));
                                        System.out.println(" fp :   " + Arrays.toString(fp));
                                        System.out.println(" fn :   " + Arrays.toString(fn));
                              }
                              prec = 0;
                              rec = 0;
                              for (int c = 0; c < number_classes; c++) {
                                        if (tp[c] > 0 || fp[c] > 0) {
                                                  prec += (1.0 * tp[c]) / (1.0 * (tp[c] + fp[c]));
                                        }
                                        if (tp[c] > 0 || fn[c] > 0) {
                                                  rec += (1.0 * tp[c]) / (1.0 * (tp[c] + fn[c]));
                                        }
                              }
                              prec /= (1.0 * number_classes);
                              rec /= (1.0 * number_classes);
                              avg_f1 += (2.0 * (prec * rec) / (1.0 * (prec + rec)));
                    }
                    avg_f1 = avg_f1 > 0 ? avg_f1 / (1.0 * Parameters.getInstance().getK_folds()) : 0;
                    return avg_f1;
          }

}
