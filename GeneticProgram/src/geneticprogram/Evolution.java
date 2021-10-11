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
            if (generation + 1 < Parameters.getInstance().getMax_generation()) {
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
                boolean has_capcity = true;
                final CountDownLatch latch = new CountDownLatch(Parameters.getInstance().getPopulation_size());
                ArrayList<GeneticOperatorThread> threads = FlyWeight.getInstance().getGeneticOperatorThreads();
                long[] seeds = new long[Parameters.getInstance().getPopulation_size()];
                for (int i = 0; i < Parameters.getInstance().getPopulation_size(); i++) {
                    seeds[i] = Randomness.getInstance().getRandomLong();
                }
                if (Meta.debug) {
                    System.out.println("seeds   : " + Arrays.toString(seeds));
                }
                int individual = 0;
                do {
                    if (individual < Parameters.getInstance().getPopulation_size() && num_crossover > 0) {
                        Program a = FlyWeight.getInstance().getProgram();
                        a.copy(Selection.getInstance(Selection.tournament).select(curr));
                        Program b = FlyWeight.getInstance().getProgram();
                        b.copy(Selection.getInstance(Selection.tournament).select(curr));
                        GeneticOperatorThread crossover = FlyWeight.getInstance().getGeneticOperatorThread();
                        crossover.reset(latch, seeds[individual], new Program[]{a, b}, Meta.CROSSOVER);
                        individual += 2;
                        threads.add(crossover);
                    }
                    if (individual < Parameters.getInstance().getPopulation_size() && num_mutation > 0) {
                        if (Meta.debug) {
                            System.out.println("Mutating");
                        }
                        Program mutant = FlyWeight.getInstance().getProgram();
                        mutant.copy(Selection.getInstance(Selection.tournament).select(curr));
                        GeneticOperatorThread mutation = FlyWeight.getInstance().getGeneticOperatorThread();
                        mutation.reset(latch, seeds[individual], new Program[]{mutant}, Meta.MUTATE);
                        ++individual;
                        threads.add(mutation);
                    }
                    if (individual < Parameters.getInstance().getPopulation_size() && num_hoist > 0) {
                        Program hoist = FlyWeight.getInstance().getProgram();
                        hoist.copy(Selection.getInstance(Selection.tournament).select(curr));
                        GeneticOperatorThread hoisted = FlyWeight.getInstance().getGeneticOperatorThread();
                        hoisted.reset(latch, seeds[individual], new Program[]{hoist}, Meta.HOIST);
                        ++individual;
                        threads.add(hoisted);
                    }
                    if (individual < Parameters.getInstance().getPopulation_size() && num_edit > 0) {
                        Program edit = FlyWeight.getInstance().getProgram();
                        edit.copy(Selection.getInstance(Selection.tournament).select(curr));
                        GeneticOperatorThread edited = FlyWeight.getInstance().getGeneticOperatorThread();
                        edited.reset(latch, seeds[individual], new Program[]{edit}, Meta.EDIT);
                        ++individual;
                        threads.add(edited);
                    }
                } while (individual < Parameters.getInstance().getPopulation_size());

                threads.forEach(thread -> {
                    thread.start();
                });
                latch.await();
                for (GeneticOperatorThread thread : threads) {
                    for (Program program : thread.getParents()) {
                        //System.out.println(Helper.toString(program));
                        try{
                            next.add(program);
                        }catch(Exception e){
                            //e.printStackTrace();
                             next.add(program);
                            /*
                            for (int md = 0; md < Parameters.getInstance().getMain_max_depth(); md++) {
                                for (int mp = 0; mp < 1 << md; mp++) {
                                    System.out.format("main (%d %d) : %d \n",md,mp,program.getMain()[md][mp]);
                                    System.out.format("condition (%d %d) : \n",md,mp);
                                    for (int cd = 0; cd < Parameters.getInstance().getCondition_max_depth(); cd++) {
                                        System.out.println(Arrays.toString(program.getConditions()[md][mp][cd]));
                                    }
                                }
                            }
                            */
                        }
                        
                    }
                }
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
        System.out.println("    fitnesses            :   " + Arrays.toString(curr.getFitnesses()));
        System.out.format("    best fitness         :   %f\n", curr.getBest_fitness());
        System.out.format("    worst  fitness      :   %f\n", curr.getWorst_fitness());
        //System.out.format("    best program         :   \n%s\n",Helper.toString(best_program));
        System.out.println("=======================================");
    }

}
