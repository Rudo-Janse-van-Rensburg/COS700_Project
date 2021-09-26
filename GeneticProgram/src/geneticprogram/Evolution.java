package geneticprogram;
 
public class Evolution {
    
    private static Evolution instance   = null;
    private static Generation curr      = null,
                              next      = null;
    private Evolution() throws Exception{
        curr    = new Generation();
        next    = new Generation();
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
    public void createInitialPopulation() throws Exception{
        
        curr.clear();
        next.clear();
        
        int num_individuals = (int) Math.floor(Parameters.getInstance().getPopulation_size() / (2 * Parameters.getInstance().getMain_max_depth() - 2 ));     
        
        boolean has_capcity = true;
        
        while(has_capcity){
            int curr_depth      = 2;
            
            while(has_capcity && curr_depth <= Parameters.getInstance().getMain_max_depth()){
                int individual = 0;
                while(has_capcity && individual < num_individuals){
                    Program prog    = new Program(curr_depth);
                    curr.add(prog);
                    ++num_individuals;
                    prog            = new Program(curr_depth);
                    curr.add(prog);
                    ++num_individuals;
                }
                if(curr_depth < Parameters.getInstance().getMain_max_depth()){
                    ++curr_depth;
                }
            }
            
            
        }
        
    }
    
    /**
     * @throws Exception 
     */
    public void evolveGeneration() throws Exception{
        if(!curr.isEmpty()){
            next.clear();
            int num_crossover = (int) Math.floor(Parameters.getInstance().getCrossover_chance()  * Parameters.getInstance().getPopulation_size());
            int num_mutation  = (int) Math.floor(Parameters.getInstance().getMutation_chance()   * Parameters.getInstance().getPopulation_size());
            int num_hoist     = (int) Math.floor(Parameters.getInstance().getHoist_chance()      * Parameters.getInstance().getPopulation_size());
            int num_edit      = (int) Math.floor(Parameters.getInstance().getEdit_chance()       * Parameters.getInstance().getPopulation_size());

            boolean has_capcity =  true;
            do{
                if(num_crossover > 0){
                    Program a = new Program(Selection.getInstance(Selection.tournament).select(curr));
                    Program b = new Program(Selection.getInstance(Selection.tournament).select(curr));

                    GeneticOperators.crossover(a, b);

                    has_capcity = has_capcity && next.add(a);
                    has_capcity = has_capcity && next.add(b);
                }

                if(has_capcity && num_mutation > 0){
                    Program mutant = new Program(Selection.getInstance(Selection.tournament).select(curr));

                    GeneticOperators.mutate(mutant);

                    has_capcity = has_capcity && next.add(mutant);
                }

                if(has_capcity && num_hoist > 0){
                    Program hoisted = new Program(Selection.getInstance(Selection.tournament).select(curr));

                    GeneticOperators.hoist(hoisted);

                    has_capcity = has_capcity && next.add(hoisted);
                }

                if(has_capcity && num_edit > 0){
                    Program edit = new Program(Selection.getInstance(Selection.tournament).select(curr));

                    GeneticOperators.hoist(edit);

                    has_capcity = has_capcity && next.add(edit);
                }

            }while(has_capcity);
           
            curr.clear();
            for (int i = 0; i < Parameters.getInstance().getPopulation_size(); i++) {
                curr.add(next.get(i));
            }
            next.clear();
        }else 
            throw new Exception("Cannot evolve empty generation.");
    }
    
}
