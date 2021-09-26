package geneticprogram; 

public class Parameters {
    private static int main_max_depth           = 10;           // the maximum dpeth of the main program tree
    private static int condition_max_depth      = 5;            //
    private static Parameters singleton         = null;         //
    private static int population_size          = 200;          //
    private static double   crossover_chance    = 50,
                            mutation_chance     = 30,
                            hoist_chance        = 10,
                            edit_chance         = 10;
    private Parameters(int mmd, int cmd, int ps, int cc, int mc, int hc, int ec) {
        Parameters.main_max_depth         = mmd;
        Parameters.condition_max_depth    = cmd;
        Parameters.population_size        = ps;
        Parameters.crossover_chance       = cc;
        Parameters.mutation_chance        = mc;
        Parameters.hoist_chance           = hc;
        Parameters.edit_chance            = ec;
    } 
    
    /**
     * @param mmd   - main max depth
     * @param cmd   - condition max depth
     * @param ps    - population size
     * @param cc    - crossover chance
     * @param mc    - mutation chance
     * @param hc    - hoist chance
     * @param ec    - edit chance
     * @return Parameters singleton.
     */
    public static Parameters setParameters(int mmd, int cmd, int ps, int cc, int mc, int hc, int ec) throws Exception{
        if(mmd > 2 && cmd > 0 && ps > 0){
            if(singleton != null){
                Parameters.main_max_depth        = mmd;
                Parameters.condition_max_depth   = cmd;
                Parameters.population_size       = ps;
                Parameters.crossover_chance      = cc;
                Parameters.mutation_chance       = mc;
                Parameters.hoist_chance          = hc;
                Parameters.edit_chance           = ec;
            }else
                singleton = new Parameters(mmd,cmd,ps,cc,mc,hc,ec);
            return singleton;
        }else
            throw new Exception("Invalid parameter values.");
        
    } 
    
    /**
     * @return Parameters singleton. 
     * @throws Exception  
     */
    public static Parameters getInstance() throws Exception{
        if(singleton != null){
            return singleton;
        }else
            throw new Exception("Parameters have not been set.");
    }
    
    /**
     * @return crossover chance.
     */
    public double getCrossover_chance() {
        return crossover_chance;
    }

    /**
     * @return mutation chance.
     */
    public double getMutation_chance() {
        return mutation_chance;
    }

    /**
     * @return edit chance.
     */
    public double getEdit_chance() {
        return edit_chance;
    }

    /**
     * @return hoist chance.
     */
    public double getHoist_chance() {
        return hoist_chance;
    }
    
    /**
     * @return Population size
     */
    public int getPopulation_size() {
        return population_size;
    }

    /**
     * @return Condition Max Depth
     */
    public int getCondition_max_depth() {
        return condition_max_depth;
    }

    /**
     * @return Main Max Depth
     */
    public int getMain_max_depth() {
        return main_max_depth;
    }  
}
