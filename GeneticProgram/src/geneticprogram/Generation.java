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
public class Generation {
    private Program[]   population;
    private double[]    fitnesses;
    private int capacity;
    public Generation() throws Exception{
        capacity    = 0;
        population  = new Program[Parameters.getInstance().getPopulation_size()];   
        fitnesses   = new double[Parameters.getInstance().getPopulation_size()];   
    }
    
    /**
     * @param individual to add to le population.
     * @return 
     */
    public boolean add(Program individual){
        if(capacity < population.length){
            population[capacity++]  = individual;
            return true;
        }else 
            return false;
        
    }
    
    public boolean isEmpty(){
        return capacity == 0;
    }
    
    /**
     * @param position individual position 
     * @return Program 
     * @throws Exception 
     */
    public Program get(int position) throws Exception{
        if(position < capacity){
            return population[position];
        }else 
            throw new Exception("Individual position out of bounds.");
    }
    
    /**
     * @throws Exception 
     */
    public void clear() throws Exception{
        capacity    = 0;
        population  = new Program[Parameters.getInstance().getPopulation_size()];
    }
    
}
