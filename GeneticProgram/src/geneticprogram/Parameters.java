package geneticprogram;

public class Parameters {

          /*
          =================================
          Genetic Program parameters
          =================================
           */
          private  int max_generation = 50,
                    main_max_depth = 10,
                    condition_max_depth = 5,
                    population_size = 200,
                    tournament_size = 2,
                    k_folds = 10;

          private  double crossover_chance = 50,
                    mutation_chance = 30,
                    hoist_chance = 10;

          /*
          =================================
          HyFlex parameters
          =================================
           */
          private  long run_time = 600000;
          private  double acceptance_threshold = 0.5;
          private  int window_size = 4;
          private  int training_instances = 5;

          private static Parameters singleton = null;         //

          /**
           * @param mg
           * @param kf
           * @param mmd
           * @param cmd
           * @param ps
           * @param ts
           * @param cc
           * @param mc
           * @param hc
           */
          private Parameters(int ti,int window_size, long run_time, double acceptance_threshold, int mg, int kf, int mmd, int cmd, int ps, int ts, double cc, double mc, double hc) {
                    this.training_instances = ti;
                    this.window_size = window_size;
                    this.run_time = run_time;
                    this.acceptance_threshold = acceptance_threshold;
                    this.max_generation = mg;
                    this.k_folds = kf;
                    this.main_max_depth = mmd;
                    this.condition_max_depth = cmd;
                    this.population_size = ps;
                    this.tournament_size = ts;
                    this.crossover_chance = cc;
                    this.mutation_chance = mc;
                    this.hoist_chance = hc;
          }

          /**
           * @param ti
           * @param window_size
           * @param run_time
           * @param acceptance_threshold
           * @param mg
           * @param kf
           * @param mmd - main max depth
           * @param cmd - condition max depth
           * @param ps - population size
           * @param ts - tournament size
           * @param cc - crossover chance
           * @param mc - mutation chance
           * @param hc - hoist chance
           * @return Parameters singleton.
           * @throws Exception
           */
          public static Parameters setParameters(int ti, int window_size, long run_time, double acceptance_threshold, int mg, int kf, int mmd, int cmd, int ps, int ts, double cc, double mc, double hc) throws Exception {
                    if (mmd > 1 && cmd > 1 && ps > 0 && ts > 0 && ps >= ts) {
                              if (singleton != null) {
                                        singleton.training_instances = ti;
                                        singleton.window_size = window_size;
                                        singleton.run_time = run_time;
                                        singleton.acceptance_threshold = acceptance_threshold;
                                        singleton.max_generation = mg;
                                        singleton.k_folds = kf;
                                        singleton.main_max_depth = mmd;
                                        singleton.condition_max_depth = cmd;
                                        singleton.population_size = ps;
                                        singleton.tournament_size = ts;
                                        singleton.crossover_chance = cc;
                                        singleton.mutation_chance = mc;
                                        singleton.hoist_chance = hc;
                                        //singleton.edit_chance           = ec; 
                              } else {
                                        singleton = new Parameters(ti,window_size, run_time, acceptance_threshold, mg, kf, mmd, cmd, ps, ts, cc, mc, hc/*,ec*/);
                              }
                              return singleton;
                    } else {
                              throw new Exception("Invalid parameter values.");
                    }
          }

          public int getTraining_instances() {
                    return training_instances;
          }

          
          
          /**
           * @return Parameters singleton.
           * @throws Exception
           */
          public static Parameters getInstance() throws Exception {
                    if (singleton != null) {
                              return singleton;
                    } else {
                              throw new Exception("Parameters have not been set.");
                    }
          }

          public   double getAcceptance_threshold() {
                    return acceptance_threshold;
          }

          public long getRun_time() {
                    return run_time;
          }

          public int getWindow_size() {
                    return window_size;
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
