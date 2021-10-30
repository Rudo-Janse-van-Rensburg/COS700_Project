package geneticprogram;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;

public class Data {

          private static Data singleton = null;
          private final double[] data_instance;                                 //
          //private final ArrayList<double[]> data_instances;          // going to hold the instances of the  
          private final int number_classes = 4, // mutation, crossover, ruin-create, local-search 
                    number_attributes;

          private Data() throws Exception {
                    //data_instances = new ArrayList<>();
                    number_attributes = 2 * Parameters.getInstance().getWindow_size();
                    data_instance = new double[number_attributes];
                    Arrays.fill(data_instance, 0);
          }

          /**
           * @return @throws Exception
           */
          public static Data initialiseData() throws Exception {
                    if (singleton == null) {
                              singleton = new Data();
                    }
                    return singleton;
          }

          /**
           * @return
           */
          public double[] getData_instance() {
                    return data_instance;
          }

          /**
           * @param last_low_level_heuristic
           * @param delta
           * @throws java.lang.Exception
           */
          public void add(int last_low_level_heuristic, double delta) throws Exception {
                    double[] copy = new double[number_attributes];
                    System.arraycopy(data_instance, 0, copy, 0, number_attributes);
                    //data_instances.add(copy);
                    for (int i = Parameters.getInstance().getWindow_size() - 1; i > 0; i--) {
                              data_instance[i] = data_instance[i - 1];
                              data_instance[i + Parameters.getInstance().getWindow_size()] = data_instance[i + Parameters.getInstance().getWindow_size() - 1];
                    }
                    data_instance[Parameters.getInstance().getWindow_size() - 1] = last_low_level_heuristic;
                    data_instance[2 * Parameters.getInstance().getWindow_size() - 1] = delta;
          }

          /**
           * @return number of classes
           */
          public int getNumberClasses() {
                    return number_classes;
          }

          /**
           * @return number of attributes
           */
          public int getNumberAttributes() {
                    return number_attributes;
          }

          /*private  void saveFile() throws IOException {
                    File file = new File("data.csv");
                    try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {
                              for (int i = 0; i < number_attributes; i++) {
                                        bw.write("hh_(" + (0 - i) + "),");
                              }
                              
                              for (int i = 0; i < number_attributes; i++) {
                                        bw.write("d_(" + (0 - i) + ")");
                                        if (i < number_attributes - 1) {
                                                  bw.write(",");
                                        }
                              }
                              bw.newLine();
                              
                              for (double[] ins : data_instances) {
                                        for (int i = 0; i < 2 * number_attributes; i++) {
                                                  bw.write(""+ins[i]);
                                                  if (i < (2 * number_attributes) - 1) {
                                                            bw.write(",");
                                                  }
                                        }
                                        bw.newLine();
                              }
                    }
          }*/

}
