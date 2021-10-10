/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticprogram;

import java.util.concurrent.CountDownLatch;

/**
 * @author rudo
 */
public class GeneticOperatorThread extends Thread {

    private CountDownLatch latch;
    private Thread t;
    private Program[] parents;
    private char operation;
    private boolean full;
    private int max_depth;
    private long seed;

    public GeneticOperatorThread() {
    }

    public void setOperation(char op) {
        operation = op;
    }

    public void setParents(Program[] parents) {
        this.parents = parents;
    }

    public Program[] getParents() {
        return parents;
    }

    
    @Override
    public void run() {
        System.out.println("starting thread " + operation);
        try {
            switch (operation) {
                case Meta.MUTATE:
                    GeneticOperators.mutate(parents[0], seed);
                    System.out.println("mutate thread exitting");
                    // latch.countDown(); 
                    break;
                case Meta.CROSSOVER:
                    GeneticOperators.crossover(parents[0], parents[1], seed);
                    System.out.println("crossover thread exitting");
                     latch.countDown(); 
                    break;
                case Meta.HOIST:
                    GeneticOperators.hoist(parents[0], seed);
                    System.out.println("hoist thread exitting"); 
                    latch.countDown();
                    break;
                case Meta.EDIT:
                    GeneticOperators.edit(parents[0], seed);
                    System.out.println("edit thread exitting"); 
                    latch.countDown();
                    break;
                case Meta.GROW:
                    GeneticOperators.grow(parents[0], max_depth, seed);
                    //System.out.println("grow thread exitting");
                    latch.countDown();
                    break;
                case Meta.FULL:
                    GeneticOperators.full(parents[0], max_depth, seed); 
                    latch.countDown();
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } 
        System.out.println("exiting thread " + operation);
    }

    public void start(){ 
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
    public void reset(CountDownLatch latch, long seed, char op, Program prog,int depth) {
        this.latch = latch; 
        this.operation = op;
        this.max_depth = depth;
        this.seed = seed; 
        this.parents = new Program[]{prog};
    }

    public void reset(CountDownLatch latch, long seed, Program[] parents, char op) {
        this.latch = latch;
        this.parents = parents;
        this.operation = op;
        this.seed = seed; 
    }
}
