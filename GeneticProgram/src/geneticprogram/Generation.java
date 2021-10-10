package geneticprogram;

import java.util.ArrayList;

public class Generation {

    private final Program[] population;
    private final Program best_program,
              worst_program;
    private final double[] fitnesses;
    private double total_fitness,
              best_fitness,
              worst_fitness;
    private int capacity;

    public Generation() throws Exception {
        capacity = 0;
        total_fitness = 0;
        best_fitness = -1;
        worst_fitness = 1;
        best_program = FlyWeight.getInstance().getProgram();
        worst_program = FlyWeight.getInstance().getProgram();
        population = new Program[Parameters.getInstance().getPopulation_size()];
        fitnesses = new double[Parameters.getInstance().getPopulation_size()];
    }

    /**
     * @param individual to add to the population.
     * @return
     * @throws Exception
     */
    public boolean add(Program individual) throws Exception {
        synchronized (this) {
            if (capacity < Parameters.getInstance().getPopulation_size()) {
                double fitness = Fitness.getInstance(Fitness.f1).evaluate(individual);
                return add(individual, fitness);

            } else {
                return false;
            }
        }
    }

    /**
     * @return
     */
    public double[] getFitnesses() {
        return fitnesses;
    }

    /**
     * @return
     */
    public double getBest_fitness() {
        return best_fitness;
    }

    public double getWorst_fitness() {
        return worst_fitness;
    }

    /**
     * @return
     */
    public Program getBest_program() {
        return best_program;
    }

    public Program getWorst_program() {
        return worst_program;
    }

    /**
     *
     * @param individual
     * @param fitness
     * @return
     * @throws Exception
     */
    public boolean add(Program individual, double fitness) throws Exception {
        if (capacity < population.length) {
            if (fitness > best_fitness) {
                best_fitness = fitness;
                best_program.copy(individual);
            }
            if (fitness < worst_fitness) {
                worst_fitness = fitness;
                worst_program.copy(individual);
            }
            population[capacity] = individual;
            fitnesses[capacity] = fitness;
            total_fitness += fitnesses[capacity];
            ++capacity;
            return true;
        } else {
            FlyWeight.getInstance().addProgram(individual);
            return false;
        }
    }

    /**
     *
     * @return
     */
    public double getAverage_fitness() {
        return total_fitness > 0 ? total_fitness / (1.0 * capacity) : 0;
    }

    /**
     * @return total fitness.
     */
    public double getTotal_fitness() {
        return total_fitness;
    }

    /**
     * @return whether the generation is empty.
     */
    public boolean isEmpty() {
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
    public Program getIndividual(int position) throws Exception {
        if (position >= 0 && position < capacity) {
            return population[position];
        } else {
            throw new Exception("Individual position out of bounds.");
        }
    }

    public double getFitness(int position) throws Exception {
        if (position < capacity) {
            return fitnesses[position];
        } else {
            throw new Exception("Fitness position out of bounds.");
        }
    }

    /**
     * @throws java.lang.Exception
     */
    public void recycle() throws Exception {

        for (int i = 0; i < capacity; i++) {
            Program prog = population[i];
            FlyWeight.getInstance().addProgram(prog);
        }
        clear();
    }

    /**
     * @throws Exception
     */
    public void clear() throws Exception {
        total_fitness = 0;
        capacity = 0;
        best_fitness = -1;
        //population  = new Program[Parameters.getInstance().getPopulation_size()];
        //fitnesses   = new double[Parameters.getInstance().getPopulation_size()];
    }

}
