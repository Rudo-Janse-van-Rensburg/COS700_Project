/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticprogram;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author rudo
 */
public class GeneticProgram {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {

            /**
             * @param mg - max generation
             * @param kf - number of folds in k-cross-validation
             * @param mmd - main max depth
             * @param cmd - condition max depth
             * @param ps - population size
             * @param ts - tournament size
             * @param cc - crossover chance
             * @param mc - mutation chance
             * @param hc - hoist chance
             * @param ec - edit chance
             * @return Parameters singleton.
             * @throws Exception
             */
            System.out.println("hi");
            Parameters.setParameters(
                      1,
                      1,
                      5,
                      4,
                      500,
                      2,
                      1,//0.6, 
                      0,//0.3, 
                      0,//0.05, 
                      0//0.05
            );
            Program full = FlyWeight.getInstance().getProgram();
            Program grow = FlyWeight.getInstance().getProgram();
            GeneticOperatorThread g_thread = FlyWeight.getInstance().getGeneticOperatorThread();
            GeneticOperatorThread f_thread = FlyWeight.getInstance().getGeneticOperatorThread();
            Data.initialiseData();
            /*
            final CountDownLatch latch = new CountDownLatch(2);
            f_thread.reset(latch, 0, Meta.FULL,full, Parameters.getInstance().getMain_max_depth());
            g_thread.reset(latch, 0, Meta.GROW,grow, Parameters.getInstance().getMain_max_depth());
            f_thread.start();
            g_thread.start();
            latch.await();
            FlyWeight.getInstance().addGeneticOperatorThread(f_thread);
            FlyWeight.getInstance().addGeneticOperatorThread(g_thread);
            */
            Evolution evolution = Evolution.getInstance();
            do{
                evolution.print();
            }while(evolution.evolveGeneration());
           /* for (int i = 0; i < Parameters.getInstance().getMain_max_depth(); i++) {
                for (int j = 0; j < 1 << i; j++) {
                    System.out.println("main " + i + " " + j);
                    System.out.println("" + grow.getMain()[i][j]);
                    System.out.println("condition " + i + " " + j);
                    for (int k = 0; k < Parameters.getInstance().getCondition_max_depth(); k++) {
                        System.out.println("" + Arrays.toString(grow.getConditions()[i][j][k]));
                    }
                }
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
