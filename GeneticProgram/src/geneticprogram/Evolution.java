package geneticprogram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class Evolution {

    private static Evolution instance = null;
    private static Generation curr = null,
              next = null;
    private final Program best_program;
    private int generation;

    private Evolution() throws Exception {
        Data.initialiseData();
        Randomness.getInstance();
        curr = FlyWeight.getInstance().getGeneration();
        next = FlyWeight.getInstance().getGeneration();
        best_program = new Program();
        createInitialPopulation();
    }

    /**
     * @return Evolution singleton
     * @throws Exception
     */
    public static Evolution getInstance() throws Exception {
        if (instance == null) {
            instance = new Evolution();
        }
        return instance;
    }

    /**
     * @throws Exception initial population
     */
    private void createInitialPopulation() throws Exception {
        curr.recycle();
        next.recycle();
        generation = 0;
        ArrayList<GeneticOperatorThread> threads = FlyWeight.getInstance().getGeneticOperatorThreads();
        int depths = Parameters.getInstance().getMain_max_depth();
        int ipg = (int) Math.ceil(Parameters.getInstance().getPopulation_size() / ((depths - 2) * 1.0));   //individuals per generation 

        if (Meta.debug) {
            System.out.println("CREATING INITIAL POPULATION...");
            System.out.format("Population size  :   %d\n", Parameters.getInstance().getPopulation_size());
            System.out.format("Depths           :   [2,%d]\n", depths);
            System.out.format("IPG              :   %d\n", ipg);
        }

        boolean has_capcity = true;
        Program prog;
        GeneticOperatorThread full, grow;
        CountDownLatch latch = new CountDownLatch(Parameters.getInstance().getPopulation_size());
        long[] seeds = new long[Parameters.getInstance().getPopulation_size()];
        for (int i = 0; i < Parameters.getInstance().getPopulation_size(); i++) {
            seeds[i] = Randomness.getInstance().getRandomLong();
        }
        for (int depth = depths - 1; has_capcity && depth >= 2; depth--) {
            if (Meta.debug && curr.getCapacity() < Parameters.getInstance().getPopulation_size()) {
                System.out.format("depth            :   %d\n", depth);
            }
            int individual = 0;
            for (; has_capcity && individual < ipg;) {

                prog = FlyWeight.getInstance().getProgram();
                full = FlyWeight.getInstance().getGeneticOperatorThread();
                full.reset(latch, Randomness.getInstance().getRandomLong(), Meta.FULL, prog, depth);
                prog = FlyWeight.getInstance().getProgram();
                grow = FlyWeight.getInstance().getGeneticOperatorThread();
                grow.reset(latch, Randomness.getInstance().getRandomLong(), Meta.GROW, prog, depth);
                has_capcity = threads.size() < Parameters.getInstance().getPopulation_size();
                ++individual;
                //GeneticOperators.full(prog, depth,0);  
                threads.add(full);
                if (has_capcity) {
                    threads.add(grow);
                    ++individual;
                    has_capcity = threads.size() < Parameters.getInstance().getPopulation_size();
                }

                if (false && Meta.debug && has_capcity) {
                    System.out.format("full (%d,%d):   \n%s\n", curr.getCapacity(), Parameters.getInstance().getPopulation_size(), Helper.toString(prog));
                }
                if (false && Meta.debug && has_capcity) {
                    System.out.format("grow (%d,%d):   \n%s\n", curr.getCapacity(), Parameters.getInstance().getPopulation_size(), Helper.toString(prog));
                }
            }
        }
        while (has_capcity) {
            full = FlyWeight.getInstance().getGeneticOperatorThread();
            prog = FlyWeight.getInstance().getProgram();
            full.reset(latch, Randomness.getInstance().getRandomLong(), Meta.FULL, prog, Parameters.getInstance().getMain_max_depth());
            threads.add(full);
            has_capcity = threads.size() < Parameters.getInstance().getPopulation_size();
        }
        for (GeneticOperatorThread thread : threads) {
            thread.start();
        }
        latch.await();
        curr.recycle();
        for (GeneticOperatorThread thread : threads) {
            curr.add(thread.getParents()[0]);
            //FlyWeight.getInstance().addGeneticOperatorThread(thread);
        }
        FlyWeight.getInstance().addGeneticOperatorThreads(threads);
    }

    /**
     * @return @throws Exception
     */
    public boolean evolveGeneration() throws Exception {
        if (!curr.isEmpty()) {
            if ((generation + 1) < Parameters.getInstance().getMax_generation()) {
                next.recycle();
                int num_crossover = (int) Math.ceil(Parameters.getInstance().getCrossover_chance() * Parameters.getInstance().getPopulation_size());
                int num_mutation = (int) Math.ceil(Parameters.getInstance().getMutation_chance() * Parameters.getInstance().getPopulation_size());
                int num_hoist = (int) Math.ceil(Parameters.getInstance().getHoist_chance() * Parameters.getInstance().getPopulation_size());
                int num_edit = (int) Math.ceil(Parameters.getInstance().getEdit_chance() * Parameters.getInstance().getPopulation_size());
                
                if (Meta.debug) {
                    System.out.format("number crossover  : %d\n", num_crossover);
                    System.out.format("number mutation   : %d\n", num_mutation);
                    System.out.format("number hoist      : %d\n", num_hoist);
                    System.out.format("number edit       : %d\n", num_edit);
                } 
                int num_threads = num_crossover + num_mutation + num_hoist + num_edit;
                if(Meta.debug){
                    System.out.format("number threads   : %d\n ",num_threads);
                }
                final CountDownLatch latch = new CountDownLatch(num_crossover);
                ArrayList<GeneticOperatorThread> threads = FlyWeight.getInstance().getGeneticOperatorThreads();
                long[] seeds = new long[Parameters.getInstance().getPopulation_size()];
                for (int i = 0; i < num_threads; i++) {
                    seeds[i] = Randomness.getInstance().getRandomLong();
                }
                if (false && Meta.debug) {
                    System.out.println("seeds   : " + Arrays.toString(seeds));
                }
                int individual = 0;
                do {
                    if (num_threads > 0 && num_crossover > 0) {
                        if (false && Meta.debug) {
                            System.out.println("Crossover");
                        }
                        Program a = FlyWeight.getInstance().getProgram();
                        a.copy(Selection.getInstance(Selection.tournament).select(curr));
                        Program b = FlyWeight.getInstance().getProgram();
                        b.copy(Selection.getInstance(Selection.tournament).select(curr));
                        GeneticOperatorThread crossover = FlyWeight.getInstance().getGeneticOperatorThread();
                        crossover.reset(latch, seeds[individual], new Program[]{a, b}, Meta.CROSSOVER);
                        threads.add(crossover);
                        --num_crossover; 
                        --num_threads;
                    }
                    if (num_threads > 0 && num_mutation > 0) {
                        if (false && Meta.debug) {
                            System.out.println("Mutating");
                        }
                        Program mutant = FlyWeight.getInstance().getProgram();
                        mutant.copy(Selection.getInstance(Selection.tournament).select(curr));
                        GeneticOperatorThread mutation = FlyWeight.getInstance().getGeneticOperatorThread();
                        mutation.reset(latch, seeds[individual], new Program[]{mutant}, Meta.MUTATE);
                        threads.add(mutation);
                        --num_threads;
                        --num_mutation;
                    }
                    if (num_threads > 0 && num_hoist > 0) {
                        Program hoist = FlyWeight.getInstance().getProgram();
                        hoist.copy(Selection.getInstance(Selection.tournament).select(curr));
                        GeneticOperatorThread hoisted = FlyWeight.getInstance().getGeneticOperatorThread();
                        hoisted.reset(latch, seeds[individual], new Program[]{hoist}, Meta.HOIST);
                        threads.add(hoisted);
                        --num_threads;
                        --num_hoist;
                    }
                    if (num_threads > 0 && num_edit > 0) {
                        Program edit = FlyWeight.getInstance().getProgram();
                        edit.copy(Selection.getInstance(Selection.tournament).select(curr));
                        GeneticOperatorThread edited = FlyWeight.getInstance().getGeneticOperatorThread();
                        edited.reset(latch, seeds[individual], new Program[]{edit}, Meta.EDIT);
                        threads.add(edited);
                        --num_threads;
                        --num_edit;
                    }
                } while (num_threads > 0);
                for (int i = 0; i < threads.size(); i++) {
                    threads.get(i).start();
                } 
                latch.await();
                System.out.println(" threads done");
                for (int i = 0; i < threads.size(); i++) {
                    if(threads.get(i).getOperation() == Meta.CROSSOVER){
                        next.add(threads.get(i).getParents()[0]) ;
                        next.add(threads.get(i).getParents()[1]) ;
                    }else{
                        next.add(threads.get(i).getParents()[0]) ;
                    }
                }  
                threads.clear();
                FlyWeight.getInstance().addGeneticOperatorThreads(threads);
                curr.recycle();
                for (int i = 0; i < Parameters.getInstance().getPopulation_size(); i++) {
                    curr.add(next.getIndividual(i), next.getFitness(i));
                }
                next.clear();
                ++generation;
                return true;
            } else {
                return false;
            }
        } else {
            throw new Exception("Cannot evolve empty generation.");
        }
    }

    public Program getBest_program() {
        return best_program;
    }

    public void print() throws Exception {
        System.out.println("=======================================");
        System.out.format("GENERATION   #%d%n", generation);
        System.out.println("---------------------------------------");
        System.out.format("    avergage fitness     :   %f\n", curr.getAverage_fitness());
        //System.out.println("    fitnesses            :   " + Arrays.toString(curr.getFitnesses()));
        System.out.format("    best fitness         :   %f\n", curr.getBest_fitness());
        System.out.format("    worst  fitness      :   %f\n", curr.getWorst_fitness());
        //System.out.format("    best program         :   \n%s\n",Helper.toString(best_program));
        System.out.println("=======================================");
    }

}
