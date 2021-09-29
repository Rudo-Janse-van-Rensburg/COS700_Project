package geneticprogram;
 
public class Generation {
    private final Program[] population;
    private final double[]  fitnesses;
    private double total_fitness;
    private int capacity;
    public Generation() throws Exception{
        capacity        = 0;
        total_fitness   = 0; 
        population      = new Program[Parameters.getInstance().getPopulation_size()];   
        fitnesses       = new double[Parameters.getInstance().getPopulation_size()];   
    }
    
    /**
     * @param individual to add to the population.
     * @return 
     * @throws Exception 
     */
    public boolean add(Program individual) throws Exception{
        if(capacity < population.length){
            population[capacity]    = individual;
            fitnesses[capacity]     = Fitness.getInstance(Fitness.f1).evaluate(individual);
            if(Meta.debug){
                System.out.println(""+fitnesses[capacity]);
            }
            total_fitness           += fitnesses[capacity];
            ++capacity;
            return true;
        }else 
            return false;
    }

    /**
     * @return 
     */
    public double[] getFitnesses() {
        return fitnesses;
    }
    
    /**
     * 
     * @param individual
     * @param fitness
     * @return
     * @throws Exception 
     */
    public boolean add(Program individual, double fitness) throws Exception{
        if(capacity < population.length){
            population[capacity]    = individual;
            fitnesses[capacity]     = fitness;
            total_fitness           += fitnesses[capacity];
            ++capacity;
            return true;
        }else 
            return false;
        
    }
    
    /**
     * @return total fitness. 
     */
    public double getTotal_fitness(){
        return total_fitness;
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
        if(position >= 0 && position < capacity){
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
     * @throws java.lang.Exception
     */
    public void recycle() throws Exception{
        
        for (int i = 0; i < capacity; i++) {
            Program prog = population[i];
            FlyWeight.getInstance().addCharArray2dMain(prog.getMain());
            FlyWeight.getInstance().addCharArray4D(prog.getConditions());
        }
        total_fitness   = 0;
        capacity        = 0;
    }
    
    /**
     * @throws Exception 
     */
    public void clear() throws Exception{
        total_fitness   = 0;
        capacity        = 0;
        //population  = new Program[Parameters.getInstance().getPopulation_size()];
        //fitnesses   = new double[Parameters.getInstance().getPopulation_size()];
    }
    
}
