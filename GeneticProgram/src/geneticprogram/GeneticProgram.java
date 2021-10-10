/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticprogram;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import jdk.jshell.execution.Util;

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
                      2,
                      1,
                      5,
                      4,
                      5,
                      2,
                      0,//0.6, 
                      1,//0.3, 
                      0,//0.05, 
                      0//0.05
            );

            Data.initialiseData();

            Program full = FlyWeight.getInstance().getProgram();
            final CountDownLatch latch = new CountDownLatch(1);
            GeneticOperatorThread f_thread = FlyWeight.getInstance().getGeneticOperatorThread();
            f_thread.reset(latch, 0, Meta.FULL, full, Parameters.getInstance().getMain_max_depth());
            f_thread.start();
            try {
                f_thread.join();
                latch.await();
               // full = f_thread.getParents()[0];
                for (int ml = 0; ml < Parameters.getInstance().getMain_max_depth(); ml++) {
                    for (int mp = 0; mp < 1 << ml; mp++) {
                        System.out.format("main (%d|%d) :  %d\n",ml,mp,full.getMain()[ml][mp]);
                        System.out.format("main (%d|%d) :   \n",ml,mp);
                        for (int cl = 0; cl < Parameters.getInstance().getCondition_max_depth(); cl++) {
                            System.out.println(Arrays.toString(full.getConditions()[ml][mp][cl]));
                        }
                    }
                }
                //System.out.format("full    :\n %s\n", Helper.toString(full));
                for (int i = 0; i < 0; i++) {

                    final CountDownLatch latch_i = new CountDownLatch(1);
                    GeneticOperatorThread m_thread = FlyWeight.getInstance().getGeneticOperatorThread();
                    m_thread.reset(latch_i, i, new Program[]{full}, Meta.MUTATE);
                    m_thread.start();
                    try {
                        m_thread.join();
                        Random rand = FlyWeight.getInstance().getRandom();
                        rand.setSeed(i);
                        System.out.format("Main function    : %d %s", i, Arrays.toString(Helper.getMainFunction(full.getMain(), true, rand)));
                        System.out.format("Main trminal     : %d %s", i, Arrays.toString(Helper.getMainTerminal(full.getMain(), true, rand)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*
            Evolution evolution = Evolution.getInstance();
            do{
                evolution.print();
            }while(evolution.evolveGeneration()); 
             */
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
