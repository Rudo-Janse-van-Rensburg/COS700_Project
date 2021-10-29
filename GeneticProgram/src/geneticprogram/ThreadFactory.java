/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticprogram;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import BinPacking.BinPacking;
import FlowShop.FlowShop;
import PersonnelScheduling.PersonnelScheduling;
import SAT.SAT;
import VRP.VRP;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import travelingSalesmanProblem.TSP;

/**
 *
 * @author rudo
 */
public class ThreadFactory {

          private static ThreadFactory factory = null;

          private ThreadFactory() {
          }

          public static ThreadFactory instance() {
                    if (factory == null) {
                              factory = new ThreadFactory();
                    }
                    return factory;
          }

          public GeneticOperatorThread getGeneticOperatorThread(CountDownLatch latch, Program[] parents, char operation, int max_depth, long seed, int[][] training_instances) throws Exception {
                    return new GeneticOperatorThread(latch, parents, operation, max_depth, seed, training_instances);
          }

          public GeneticOperatorThread getGeneticOperatorThread(CountDownLatch latch, Program[] parents, char operation, long seed, int[][] training_instances) throws Exception {
                    return new GeneticOperatorThread(latch, parents, operation, seed, training_instances);
          }

          public RunnerThread getRunnerThread(CountDownLatch latch, Program prog, long seed, int domain, int instance) throws Exception {
                    return new RunnerThread(latch, prog, domain, instance, Parameters.getInstance().getRun_time(), seed);
          }

          public CompetitionRunnerThread getCompetitionRunnerThread(CountDownLatch latch,
                    Program prog,
                    int domain,
                    int instance,
                    long time_limit,
                    long[] seeds,
                    int runs) throws Exception {
                    return new CompetitionRunnerThread(latch, prog, domain, instance, time_limit, seeds,runs);
          }

}

class CompetitionRunnerThread extends Thread {

          private static HyperHeuristic loadHyperHeuristic(Program prog, long timeLimit, long seed) {
                    HyperHeuristic h;
                    h = new SelectivePeturbativeHyperHeuristic(prog, seed);
                    h.setTimeLimit(timeLimit);
                    return h;
          }

          private static ProblemDomain loadProblemDomain(int problem, long instanceseed ) {
                    ProblemDomain p;
                    switch (problem) {
                              case 0:
                                        p = new SAT(instanceseed);
                                        break;
                              case 1:
                                        p = new BinPacking(instanceseed);
                                        break;
                              case 2:
                                        p = new PersonnelScheduling(instanceseed);
                                        break;
                              case 3:
                                        p = new FlowShop(instanceseed);
                                        break;
                              case 4:
                                        p = new TSP(instanceseed);
                                        break;
                              case 5:
                                        p = new VRP(instanceseed);
                                        break;
                              default:
                                        System.err.println("there is no problem domain with this index"); 
                                        System.exit(0);
                                        p = null;
                    }
                    return p;
          }

          private final CountDownLatch latch;
          private final List<String> printer_content;
          private final StringBuilder buffer_printer_content;
          private final String resultsfolder;
          private final int problem, instance;
          private final int runs;
          private final long[] seeds;
          private final Program prog;
          private final long time;
          private final Random rng;

          @Override
          public void run() {
                    printer_content.add("PROBLEM DOMAIN " + resultsfolder + "\n");
                    printer_content.add("  instance " + instance + " " + "\n");

                    for (int run = 0; run < this.runs; run++) {
                              long instanceseed = seeds[run];
                              printer_content.add("    RUN " + run);
                              ProblemDomain p = loadProblemDomain(problem, instanceseed);
                              HyperHeuristic h = loadHyperHeuristic(this.prog, time,  rng.nextLong());
                              p.loadInstance(instance);
                              h.loadProblemDomain(p);
                              long initialTime2 = System.currentTimeMillis();
                              h.run();
                              int[] i = p.getHeuristicCallRecord();
                              int counter = 0;
                              for (int y : i) {
                                        counter += y;
                              }
                              printer_content.add("\t" + h.getBestSolutionValue() + "\t" + (h.getElapsedTime() / 1000.0) + "\t" + (System.currentTimeMillis() - initialTime2) / 1000.0 + "\t" + counter + "\n");

                              buffer_printer_content.append(h.getBestSolutionValue()).append(" ");
                              StringBuilder buffer_printer_content_1 = new StringBuilder();
                              double[] u = h.getFitnessTrace();
                              for (double y : u) {
                                        buffer_printer_content_1.append(y).append(" ");
                              }
                              buffer_printer_content_1.append("\n");
                              try {
                                        Path pathtofile = Paths.get("./"+ Parameters.getInstance().toString() + "/results/" + resultsfolder + "/time" + instance + ".txt");
                                        if (!Files.exists(pathtofile)) {
                                                  Files.createDirectories(pathtofile.getParent());
                                                  Files.createFile(pathtofile);
                                        }
                                        Files.write(pathtofile, printer_content.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                              } catch (Exception e) {
                                        e.printStackTrace();
                              }
                              buffer_printer_content.append("\n");
                    }
                    try {
                              Path pathtofile = Paths.get("./"+ Parameters.getInstance().toString() + "/results/" + problem + "-" + instance + ".txt");
                              if (!Files.exists(pathtofile)) {
                                        Files.createDirectories(pathtofile.getParent());
                                        Files.createFile(pathtofile);
                              }
                              Files.write(pathtofile, printer_content.toString().getBytes(), StandardOpenOption.CREATE);
                              pathtofile = Paths.get("./"+ Parameters.getInstance().toString() + "/results/" + resultsfolder + "/instance" + instance + ".txt");
                              if (!Files.exists(pathtofile)) {
                                        Files.createDirectories(pathtofile.getParent());
                                        Files.createFile(pathtofile);
                              }
                              Files.write(pathtofile, buffer_printer_content.toString().getBytes(), StandardOpenOption.CREATE);

                    } catch (Exception e) {
                              e.printStackTrace();
                    }
                    latch.countDown();
          }

          public CompetitionRunnerThread(
                    CountDownLatch latch,
                    Program prog,
                    int domain,
                    int instance,
                    long time_limit,
                    long[] seeds,
                    int runs
          ) throws Exception {
                    this.rng = new Random();
                    this.time = time_limit;
                    this.runs = runs;
                    this.seeds = seeds;
                    this.prog = prog;
                    this.latch = latch;
                    this.instance = instance;
                    this.problem = domain;
                    this.printer_content = new ArrayList<>();
                    this.buffer_printer_content = new StringBuilder();
                    switch (domain) {
                              case 0:
                                        resultsfolder = "SAT";
                                        break;
                              case 1:
                                        resultsfolder = "BinPacking";
                                        break;
                              case 2:
                                        resultsfolder = "PersonnelScheduling";
                                        break;
                              case 3:
                                        resultsfolder = "FlowShop";
                                        break;
                              case 4:
                                        resultsfolder = "TSP";
                                        break;
                              case 5:
                                        resultsfolder = "VRP";
                                        break;
                              default:
                                        resultsfolder = "not worked";
                                        System.err.println("wrong input for the problem domain");
                                        System.exit(-1);
                    }
          }

          @Override
          public String toString() {
                    return "SPHH";
          }
}

class RunnerThread extends Thread {

          private final CountDownLatch latch;
          private final Runner runner;

          @Override
          public void run() {
                    runner.execute();
                    latch.countDown();
          }

          public Runner getRunner() {
                    return runner;
          }

          public RunnerThread(CountDownLatch latch, Program prog, int domain, int instance, long time_limit, long seed) throws Exception {
                    this.runner = new Runner(prog, domain, instance, time_limit, seed);
                    this.latch = latch;

          }

          @Override
          public String toString() {
                    return "SPHH";
          }
}

class GeneticOperatorThread extends Thread {

          private final Program[] parents;
          private final char operation;
          private final int max_depth;
          private final long seed;
          private final CountDownLatch latch;
          private final int[][] training_instances;

          public GeneticOperatorThread(CountDownLatch latch, Program[] parents, char operation, int max_depth, long seed, int[][] training_instances) throws Exception {
                    this.parents = new Program[parents.length];
                    setParents(parents);
                    this.operation = operation;
                    this.max_depth = max_depth;
                    this.seed = seed;
                    this.latch = latch;
                    this.training_instances = training_instances;
          }

          public GeneticOperatorThread(CountDownLatch latch, Program[] parents, char operation, long seed, int[][] training_instances) throws Exception {
                    this.parents = new Program[parents.length];
                    setParents(parents);
                    this.operation = operation;
                    this.max_depth = Parameters.getInstance().getMain_max_depth();
                    this.seed = seed;
                    this.latch = latch;
                    this.training_instances = training_instances;
          }

          @Override
          public void run() {
                    try {
                              switch (operation) {
                                        case Meta.MUTATE:
                                                  GeneticOperators.mutate(parents[0], seed);
                                                  parents[0].setFitness(Fitness.getInstance().evaluate(parents[0], training_instances, seed));
                                                  latch.countDown();
                                                  break;
                                        case Meta.CROSSOVER:
                                                  GeneticOperators.crossover(parents[0], parents[1], seed);
                                                  parents[0].setFitness(Fitness.getInstance().evaluate(parents[0], training_instances, seed));
                                                  parents[1].setFitness(Fitness.getInstance().evaluate(parents[1], training_instances, seed));
                                                  latch.countDown();
                                                  break;
                                        case Meta.HOIST:
                                                  GeneticOperators.hoist(parents[0], seed);
                                                  parents[0].setFitness(Fitness.getInstance().evaluate(parents[0], training_instances, seed));
                                                  latch.countDown();
                                                  break;
                                        case Meta.GROW:
                                                  GeneticOperators.grow(parents[0], max_depth, seed);
                                                  parents[0].setFitness(Fitness.getInstance().evaluate(parents[0], training_instances, seed));
                                                  latch.countDown();
                                                  break;
                                        case Meta.FULL:
                                                  GeneticOperators.full(parents[0], max_depth, seed);
                                                  parents[0].setFitness(Fitness.getInstance().evaluate(parents[0], training_instances, seed));
                                                  latch.countDown();
                                                  break;
                              }
                    } catch (Exception e) {
                              e.printStackTrace();
                    }
          }

          public Program[] getParents() {
                    return parents;
          }

          public char getOperation() {
                    return operation;
          }

          private void setParents(Program[] parents) throws Exception {
                    for (int i = 0; i < parents.length; i++) {
                              Program prog = FlyWeight.getInstance().getProgram();
                              prog.copy(parents[i]);
                              this.parents[i] = prog;
                    }
          }

}
