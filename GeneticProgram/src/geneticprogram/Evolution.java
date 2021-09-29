package geneticprogram;
 
import java.util.Arrays;

public class Evolution {
    
    private static Evolution instance   = null;
    private static Generation curr      = null,
                              next      = null;
    private double  best_fitness;
    private final Program best_program;
    private int     generation;
    
    
    private Evolution() throws Exception{
        curr    = FlyWeight.getInstance().getGeneration();
        next    = FlyWeight.getInstance().getGeneration();
        best_fitness    = 0;
        best_program    = new Program();
        createInitialPopulation();
    }
    
    /**
     * @return Evolution singleton
     * @throws Exception 
     */
    public static Evolution getInstance() throws Exception{
        if(instance == null){
            instance = new Evolution();
        }
        return instance;
    }
    
    /**
     * @throws Exception initial population
     */
    private void createInitialPopulation() throws Exception{ 
        curr.recycle();
        next.recycle();
        generation  = 0;
        int num_individuals = (int) Math.floor(Parameters.getInstance().getPopulation_size() / ((Parameters.getInstance().getMain_max_depth() -2 ) << 1 ));     
        
        boolean has_capcity = true;
        
        while(has_capcity){ 
            int curr_depth      = 2;
            
            while(has_capcity && curr_depth <= Parameters.getInstance().getMain_max_depth()){
                int individual = 0;
                while(has_capcity && individual < num_individuals){
                    Program prog    = FlyWeight.getInstance().getProgram();
                    GeneticOperators.full(prog, curr_depth);
                    has_capcity = curr.add(prog);
                    if(has_capcity){
                        ++individual;
                        prog            = FlyWeight.getInstance().getProgram();
                        GeneticOperators.grow(prog, curr_depth);
                        has_capcity = curr.add(prog);
                        if(has_capcity){
                            ++individual;
                        }else
                            FlyWeight.getInstance().addProgram(prog); 
                    }else
                        FlyWeight.getInstance().addProgram(prog);  
                }
                ++curr_depth; 
            }
        }
    }
    
    /**
     * @return 
     * @throws Exception 
     */
    public boolean evolveGeneration() throws Exception{
        if(!curr.isEmpty()){
            if(generation < Parameters.getInstance().getMax_generation()){
                next.recycle();
                int num_crossover = (int) Math.floor(Parameters.getInstance().getCrossover_chance()  * Parameters.getInstance().getPopulation_size());
                int num_mutation  = (int) Math.floor(Parameters.getInstance().getMutation_chance()   * Parameters.getInstance().getPopulation_size());
                int num_hoist     = (int) Math.floor(Parameters.getInstance().getHoist_chance()      * Parameters.getInstance().getPopulation_size());
                int num_edit      = (int) Math.floor(Parameters.getInstance().getEdit_chance()       * Parameters.getInstance().getPopulation_size());

                boolean has_capcity =  true;
                do{
                    if(num_crossover > 0){
                        Program a = FlyWeight.getInstance().getProgram();
                        a.copy(Selection.getInstance(Selection.tournament).select(curr));
                        Program b = FlyWeight.getInstance().getProgram();
                        b.copy(Selection.getInstance(Selection.tournament).select(curr));

                        GeneticOperators.crossover(a, b);

                        has_capcity = has_capcity && next.add(a);
                        has_capcity = has_capcity && next.add(b);
                    }

                    if(has_capcity && num_mutation > 0){
                        Program mutant = FlyWeight.getInstance().getProgram();
                        mutant.copy(Selection.getInstance(Selection.tournament).select(curr));

                        GeneticOperators.mutate(mutant);

                        has_capcity = has_capcity && next.add(mutant);
                    }

                    if(has_capcity && num_hoist > 0){
                        Program hoisted = FlyWeight.getInstance().getProgram();
                        hoisted.copy(Selection.getInstance(Selection.tournament).select(curr));

                        GeneticOperators.hoist(hoisted);

                        has_capcity = has_capcity && next.add(hoisted);
                    }

                    if(has_capcity && num_edit > 0){
                        Program edit = FlyWeight.getInstance().getProgram();
                        edit.copy(Selection.getInstance(Selection.tournament).select(curr));

                        GeneticOperators.hoist(edit);

                        has_capcity = has_capcity && next.add(edit);
                    } 
                }while(has_capcity); 
                curr.clear();
                for (int i = 0; i < Parameters.getInstance().getPopulation_size(); i++) {
                    
                    Program prog    = next.getIndividual(i);
                    double fitness  = next.getFitness(i);
                    
                    if(fitness > best_fitness){
                        best_fitness =  fitness;
                        best_program.copy(prog);
                    }
                    
                    curr.add(next.getIndividual(i),next.getFitness(i));
                }
                next.clear();
                ++generation;
                return true;
            }else
                return false;
        }else 
            throw new Exception("Cannot evolve empty generation."); 
    } 

    public double getBest_fitness() {
        return best_fitness;
    }

    public Program getBest_program() {
        return best_program;
    }
    
    public void print(){
        System.out.println("=======================================");
        System.out.format("GENERATION   #%d%n",generation);
        System.out.println("---------------------------------------");
        System.out.println("    fitnesses   :   " + Arrays.toString(curr.getFitnesses()));
        System.out.println("=======================================");
    }
    
}
