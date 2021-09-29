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
        int depths      = Parameters.getInstance().getMain_max_depth();
        int ipg         = (int) Math.ceil(Parameters.getInstance().getPopulation_size() / ((depths - 2)* 1.0));   //individuals per generation 
            
        if(Meta.debug){
            System.out.println("CREATING INITIAL POPULATION...");
            System.out.format("Population size  :   %d\n",Parameters.getInstance().getPopulation_size());
            System.out.format("Depths           :   [2,%d]\n",depths);
            System.out.format("IPG              :   %d\n",ipg);
        }
        boolean has_capcity = true; 
        Program prog;
        for (int depth = depths-1; has_capcity && depth >= 2; depth--) {
            if(Meta.debug && curr.getCapacity() < Parameters.getInstance().getPopulation_size()){
                System.out.format("depth    :    %d\n",depth);
            }
            for (int individual = 0;has_capcity && individual < ipg; individual+=2) {
                prog        = FlyWeight.getInstance().getProgram();
                GeneticOperators.full(prog, depth);  
                has_capcity = has_capcity && curr.add(prog);
                if(Meta.debug && has_capcity && false){
                    System.out.format("full (%d,%d):   \n%s\n",curr.getCapacity(),Parameters.getInstance().getPopulation_size(),Util.getInstance().toString(prog));
                }
                prog        = FlyWeight.getInstance().getProgram();
                GeneticOperators.grow(prog, depth);  
                has_capcity = has_capcity && curr.add(prog);  
                if(Meta.debug && has_capcity && false){
                    System.out.format("grow (%d,%d):   \n%s\n",curr.getCapacity(),Parameters.getInstance().getPopulation_size(),Util.getInstance().toString(prog));
                }
            } 
        }
        if(curr.getBest_fitness() > best_fitness){
            best_fitness    = curr.getBest_fitness();
            best_program.copy(curr.getBest_program());
        }
    }
    
    /**
     * @return 
     * @throws Exception 
     */
    public boolean evolveGeneration() throws Exception{
        if(!curr.isEmpty()){
            if(generation + 1 < Parameters.getInstance().getMax_generation()){
                next.recycle();
                int num_crossover = (int) Math.ceil(Parameters.getInstance().getCrossover_chance()  * Parameters.getInstance().getPopulation_size());
                int num_mutation  = (int) Math.ceil(Parameters.getInstance().getMutation_chance()   * Parameters.getInstance().getPopulation_size());
                int num_hoist     = (int) Math.ceil(Parameters.getInstance().getHoist_chance()      * Parameters.getInstance().getPopulation_size());
                int num_edit      = (int) Math.ceil(Parameters.getInstance().getEdit_chance()       * Parameters.getInstance().getPopulation_size());
                if(Meta.debug){
                    System.out.format("number crossover  : %d\n",num_crossover);
                    System.out.format("number mutation   : %d\n",num_mutation);
                    System.out.format("number hoist      : %d\n",num_hoist);
                    System.out.format("number edit       : %d\n",num_edit);
                }
                boolean has_capcity =  true;
                do{
                    if(has_capcity && num_crossover > 0){
                        Program a = FlyWeight.getInstance().getProgram();
                        a.copy(Selection.getInstance(Selection.tournament).select(curr));
                        Program b = FlyWeight.getInstance().getProgram();
                        b.copy(Selection.getInstance(Selection.tournament).select(curr));
                        if(Meta.debug){
                            System.out.println("CROSSOVER   :");
                            System.out.format("Parent A    : \n\n%s\n",Util.getInstance().toString(a));
                            System.out.format("Parent B    : \n\n%s\n",Util.getInstance().toString(b));
                            
                        }
                        GeneticOperators.crossover(a, b);
                        if(Meta.debug){
                            System.out.format("Child A    : \n\n%s\n",Util.getInstance().toString(a));
                            System.out.format("Child B    : \n\n%s\n",Util.getInstance().toString(b));
                            
                        }
                        
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
                curr.recycle();
                for (int i = 0; i < Parameters.getInstance().getPopulation_size(); i++) { 
                    curr.add(next.getIndividual(i),next.getFitness(i));
                }
                next.clear();
                if(curr.getBest_fitness() > best_fitness){
                    best_fitness    = curr.getBest_fitness();
                    best_program.copy(curr.getBest_program());
                }
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
    
    public void print() throws Exception{
        System.out.println("=======================================");
        System.out.format("GENERATION   #%d%n",generation);
        System.out.println("---------------------------------------");
        System.out.format("    avergage fitness     :   %f\n",curr.getAverage_fitness());
        System.out.println("    fitnesses            :   " + Arrays.toString(curr.getFitnesses()));
        System.out.format("    best fitness         :   %f\n",best_fitness);
        System.out.format("    best program         :   \n%s\n",Util.getInstance().toString(best_program));
        System.out.println("=======================================");
    }
    
}
