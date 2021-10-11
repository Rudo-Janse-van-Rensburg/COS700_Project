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
                      2,
                      2,
                      100,
                      2,
                      0,//0.6, 
                      1,//0.3, 
                      0,//0.05, 
                      0//0.05
            ); 
            Evolution evolution = Evolution.getInstance();
            do{
                evolution.print();
            }while(evolution.evolveGeneration()); 
             
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
