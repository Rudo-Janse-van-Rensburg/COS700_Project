/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticprogram;

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
                      50,
                      1,
                      5,
                      5,
                      500,
                      4,
                      0.5,//0.6, 
                      0.4,//0.3, 
                      0.1 //0.05, 
                      //1//5//0.05
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
