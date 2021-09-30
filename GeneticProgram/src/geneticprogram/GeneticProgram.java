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
       try{
           /**
            * @param mg     - max generation
            * @param kf     - number of folds in k-cross-validation
            * @param mmd    - main max depth
            * @param cmd    - condition max depth
            * @param ps     - population size
            * @param ts     - tournament size
            * @param cc     - crossover chance
            * @param mc     - mutation chance
            * @param hc     - hoist chance
            * @param ec     - edit chance
            * @return Parameters singleton.
            * @throws Exception
            */
           Parameters.setParameters(
                   1,
                   1, 
                   10, 
                   3, 
                   100, 
                   2, 
                   0.6, 
                   0.3, 
                   0.05, 
                   0.05
           ); 
           
            for (int depth = 2; depth < 9; depth++) {
            //for (int depth = 2; depth < 4; depth++) {
                System.out.format("DEPTH    %d\n",depth);
                System.out.println("---------------------------------------");
                try{
                    Program edit = FlyWeight.getInstance().getProgram();
                    GeneticOperators.grow(edit, depth);
                    System.out.println("GROW    :");
                    System.out.format("    fitness : %f\n",Fitness.getInstance(Fitness.f1).evaluate(edit));
                    System.out.format("Full      :   \n%s\n",Util.getInstance().toString(edit));
                    GeneticOperators.edit(edit); 
                    System.out.println("Edit    :");
                    System.out.format("    fitness : %f\n",Fitness.getInstance(Fitness.f1).evaluate(edit));
                    System.out.format("Edit      :   \n%s\n",Util.getInstance().toString(edit));
                }catch(Exception e){
                    e.printStackTrace();
                }
                System.out.println("---------------------------------------");
               
            }
           
           
           /*
           Evolution evolution = Evolution.getInstance();
           do{
               evolution.print();
           }while(evolution.evolveGeneration()); 
           */
       }catch(Exception e){
           e.printStackTrace(); 
       }
       
    }
    
}
