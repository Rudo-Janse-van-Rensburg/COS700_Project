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
           
            for (int depth = 2; depth < 5; depth++) {
            //for (int depth = 2; depth < 4; depth++) {
                System.out.format("DEPTH    %d\n",depth);
                System.out.println("=======================================");
                try{
                    Program a = FlyWeight.getInstance().getProgram();
                    Program b = FlyWeight.getInstance().getProgram();
                    System.out.println("FULL   A :");
                    GeneticOperators.full(a, depth);
                    System.out.format("\n%s\n",Util.getInstance().toString(a));
                    System.out.format("\n  fitness : %f\n\n",Fitness.getInstance(Fitness.f1).evaluate(a));
                    
                    System.out.println("FULL   B :");
                    GeneticOperators.full(b, depth);
                    System.out.format("\n%s\n",Util.getInstance().toString(b));
                    System.out.format("\n  fitness : %f\n\n",Fitness.getInstance(Fitness.f1).evaluate(a));
                    
                    System.out.println("---------------------------------------");

                    GeneticOperators.crossover(a, b);
                    System.out.println("CROSSOVER A :"); 
                    System.out.format("\n%s\n",Util.getInstance().toString(a));
                    System.out.format("\n  fitness : %f\n\n",Fitness.getInstance(Fitness.f1).evaluate(a)); 
                    
                    System.out.println("CROSSOVER B :");  
                    System.out.format("\n%s\n",Util.getInstance().toString(b));
                    System.out.format("\n  fitness : %f\n\n",Fitness.getInstance(Fitness.f1).evaluate(b));
                }catch(Exception e){
                    e.printStackTrace();
                }
                System.out.println("=======================================");
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
