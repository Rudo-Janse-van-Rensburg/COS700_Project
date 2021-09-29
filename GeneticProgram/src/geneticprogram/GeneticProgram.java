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
            * @param kf
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
           Parameters.setParameters(20,5, 5, 3, 1, 2, 0.6, 0.3, 0.05, 0.05);
           
           Evolution evolution  = Evolution.getInstance(); 
           evolution.print();
           //String str_prg       = Util.getInstance().toString(prog);
           //System.out.println(str_prg);
           while(false && evolution.evolveGeneration()){
               //evolution.print();
           }
           Program prog         = evolution.getBest_program();
       }catch(Exception e){
           e.printStackTrace(); 
       }
       
    }
    
}
