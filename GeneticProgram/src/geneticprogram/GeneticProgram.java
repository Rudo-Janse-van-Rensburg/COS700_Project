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
           
            for (int depth = 0; depth < 10; depth++) {
                System.out.format("DEPTH    %d\n",depth);
                System.out.println("---------------------------------------");
                try{
                    Program mutant = FlyWeight.getInstance().getProgram();
                    GeneticOperators.grow(mutant, depth);
                    System.out.format("Grow      :   \n%s\n",Util.getInstance().toString(mutant));
                    GeneticOperators.mutate(mutant);
                    //System.out.format("Mutant    :   \n%s\n",Util.getInstance().toString(mutant));
                }catch(Exception e){
                    System.err.println("Error : "+e.getMessage());
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
