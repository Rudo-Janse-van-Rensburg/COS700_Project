package geneticprogram;

import java.util.ArrayList;
import java.util.Random;

public class Generation {

          private long generation_seed;
          private final Program[] population;
          private final Program best_program,
                    worst_program;
          private final double[] fitnesses;
          private double total_fitness,
                    best_fitness,
                    worst_fitness;
          private int capacity;
          private final int[][] training_instaces;

          public Generation() throws Exception {
                    generation_seed = Randomness.getInstance().getRandomLong();
                    capacity = 0;
                    total_fitness = 0;
                    best_fitness = -1;
                    worst_fitness = 1;
                    best_program = FlyWeight.getInstance().getProgram();
                    worst_program = FlyWeight.getInstance().getProgram();
                    population = new Program[Parameters.getInstance().getPopulation_size()];
                    fitnesses = new double[Parameters.getInstance().getPopulation_size()];
                    training_instaces = new int[4][Parameters.getInstance().getTraining_instances()];

                    chooseInstances();
          }

          private void chooseInstances() throws Exception {
                    Random rand = FlyWeight.getInstance().getRandom();
                    rand.setSeed(generation_seed);
                    for (int d = 0; d < 4; d++) { 
                              ArrayList<Integer> instances = FlyWeight.getInstance().getArrayListInt();
                              for (int i = 0; i < Parameters.getInstance().getTraining_instances(); i++) {
                                        instances.add(i);
                              }

                              for (int i = 0; i < Parameters.getInstance().getTraining_instances(); i++) {
                                        training_instaces[d][i] = instances.remove(rand.nextInt(instances.size()));
                              }
                              FlyWeight.getInstance().addArrayListInt(instances);
                    }
                    FlyWeight.getInstance().addRandom(rand);
          }

          /**
           * @param individual to add to the population.
           * @return
           * @throws Exception
           */
          public boolean add(Program individual) throws Exception {
                    synchronized (this) {
                              if (capacity < Parameters.getInstance().getPopulation_size()) {
                                        double fitness = Fitness.getInstance().evaluate(individual, training_instaces, generation_seed);
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
                    generation_seed = Randomness.getInstance().getRandomLong();
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
                    generation_seed = Randomness.getInstance().getRandomLong();
                    total_fitness = 0;
                    capacity = 0;
                    best_fitness = -1;
                    //population  = new Program[Parameters.getInstance().getPopulation_size()];
                    //fitnesses   = new double[Parameters.getInstance().getPopulation_size()];
                    chooseInstances();
          }

}
