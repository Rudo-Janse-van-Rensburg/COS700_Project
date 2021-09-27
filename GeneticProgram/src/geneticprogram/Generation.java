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
     * @param individual to add to the population.
     * @return 
     */
    public boolean add(Program individual) throws Exception{
        if(capacity < population.length){
            population[capacity]    = individual;
            fitnesses[capacity]     = Fitness.getInstance(Fitness.f1).evaluate(individual);
            ++capacity;
            return true;
        }else 
            return false;
        
    }
    
    public boolean add(Program individual, double fitness) throws Exception{
        if(capacity < population.length){
            population[capacity]    = individual;
            fitnesses[capacity]     = fitness;
            ++capacity;
            return true;
        }else 
            return false;
        
    }
    
    
    /**
     * @return whether the generation is empty. 
     */
    public boolean isEmpty(){
        return capacity == 0;
    }

    /**
     * @return get the number of individuals in the generation.
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * @param position individual position 
     * @return Program.
     * @throws Exception 
     */
    public Program getIndividual(int position) throws Exception{
        if(position < capacity){
            return population[position];
        }else 
            throw new Exception("Individual position out of bounds.");
    }
    
    public double getFitness(int position) throws Exception{
        if(position < capacity){
            return fitnesses[position];
        }else 
            throw new Exception("Fitness position out of bounds.");
    }
    
    /**
     * @throws Exception 
     */
    public void clear() throws Exception{
        capacity    = 0;
        population  = new Program[Parameters.getInstance().getPopulation_size()];
        fitnesses   = new double[Parameters.getInstance().getPopulation_size()];
    }
    
}
