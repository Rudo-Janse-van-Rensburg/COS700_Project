/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package COS700_Project;

import java.util.Arrays;
import java.util.List;

public class Main {

 
          /**
           * @param args the command line arguments
           * @throws java.lang.Exception
           */
          public static void main(String[] args) throws Exception {
                    int mg = 100,
                              kf = 10,
                              mmd = 8,
                              cmd = 8,
                              ps = 10,
                              ts = 2;
                    double cc = 0.5,
                              mc = 0.4,
                              hc = 0.1;
                    Parameters.setParameters(mg, kf, mmd, cmd, ps, ts, cc, mc, hc);
                  
                    Evolution evolution = Evolution.getInstance();
                    do {
                              evolution.print();
                              if(evolution.getAverageFitness() == 0.0){
                                        System.out.println(Arrays.toString(evolution.getCurrentGeneration().getFitnesses()));
                              }
                    } while (evolution.evolveGeneration());  
                   evolution.writeToCSV();
                   
                    Program best = evolution.getBest_program();
                    /*CompetitionRunner competitionRunner = new CompetitionRunner(best);
                    
                    competitionRunner.run();
                    try{
                              competitionRunner.join();
                    }catch(Exception e){
                              e.printStackTrace();
                    }*/
          }

}
