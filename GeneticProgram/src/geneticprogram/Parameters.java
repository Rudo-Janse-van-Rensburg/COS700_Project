package geneticprogram; 

public class Parameters {
    
    private static int max_generation           = 50,
                       main_max_depth           = 10,           // the maximum dpeth of the main program tree
                       condition_max_depth      = 5,            //
                       population_size          = 200,
                       tournament_size          = 2,
                       k_folds                  = 10;
    private static Parameters singleton         = null;         //
    private static double   crossover_chance    = 50,
                            mutation_chance     = 30,
                            hoist_chance        = 10,
                            edit_chance         = 10;
    private Parameters(int mg,int kf,int mmd, int cmd, int ps, int ts, double cc, double mc, double hc, double ec) {
        Parameters.max_generation         = mg;
        Parameters.k_folds                = kf;
        Parameters.main_max_depth         = mmd;
        Parameters.condition_max_depth    = cmd;
        Parameters.population_size        = ps;
        Parameters.tournament_size        = ts;
        Parameters.crossover_chance       = cc;
        Parameters.mutation_chance        = mc;
        Parameters.hoist_chance           = hc;
        Parameters.edit_chance            = ec;
    } 
    
    /**
     * @param kf
     * @param mmd   - main max depth
     * @param cmd   - condition max depth
     * @param ps    - population size
     * @param ts    - tournament size
     * @param cc    - crossover chance
     * @param mc    - mutation chance
     * @param hc    - hoist chance
     * @param ec    - edit chance
     * @return Parameters singleton.
     * @throws Exception
     */
    public static Parameters setParameters(int mg,int kf,int mmd, int cmd, int ps, int ts, double cc, double mc, double hc, double ec) throws Exception{
        if(mmd >  3 && cmd > 3 && ps > 0 && ts > 0 && ps >  ts){
            if(singleton != null){
                Parameters.max_generation        = mg;
                Parameters.k_folds               = kf;
                Parameters.main_max_depth        = mmd;
                Parameters.condition_max_depth   = cmd;
                Parameters.population_size       = ps;
                Parameters.tournament_size       = ts;
                Parameters.crossover_chance      = cc;
                Parameters.mutation_chance       = mc;
                Parameters.hoist_chance          = hc;
                Parameters.edit_chance           = ec; 
            }else
                singleton = new Parameters(mg,kf,mmd,cmd,ps,ts,cc,mc,hc,ec);
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
     * @return max generation
     */
    public int getMax_generation() {
        return max_generation;
    }
    
    /**
     * @return number of folds
     */
    public int getK_folds() {
        return k_folds;
    }
    
    /**
     * @return tournament size
     */
    public int getTournament_size() {
        return tournament_size;
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
