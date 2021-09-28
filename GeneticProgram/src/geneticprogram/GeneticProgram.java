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
           Parameters.setParameters(5, 5, 5, 20, 2, 0.6, 0.3, 0.05, 0.05);
           
           Evolution evolution = Evolution.getInstance();
           evolution.createInitialPopulation();
           
       }catch(Exception e){
           System.err.println("Error    :   "+e.getMessage());
       }
       
    }
    
}
