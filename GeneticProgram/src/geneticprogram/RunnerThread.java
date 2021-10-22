package geneticprogram;

import AbstractClasses.ProblemDomain;
import BinPacking.BinPacking;
import FlowShop.FlowShop;
import PersonnelScheduling.PersonnelScheduling;
import SAT.SAT;
import VRP.VRP;
import java.util.concurrent.CountDownLatch;
import travelingSalesmanProblem.TSP;

public class RunnerThread extends Thread {

          public final static int SAT = 0,
                    BinPacking = 1,
                    PersonnelScheduling = 2,
                    FlowShop = 3,
                    TSP = 4,
                    VRP = 5;
          private long startTime,endTime;
          private CountDownLatch latch; 
          private volatile Thread t;  
          private ProblemDomain p; 
          private SelectivePeturbativeHyperHeuristic h; 
          public RunnerThread() {

          }
 

          @Override
          public void run() {
                    startTime = System.currentTimeMillis();
                     h.loadProblemDomain(p);
                     h.run();
                     endTime = System.currentTimeMillis();
                     latch.countDown();
                     
          }

          @Override
          public synchronized void start() { 
                    t = null;
                    t = new Thread(this);
                    t.start();
          }
          
          public double getBestSolutionValue(){
                    return h.getBestSolutionValue();
          }
          
          public long getElapsedTime(){
                    return h.getElapsedTime();
          }
          
          public long getRunTime(){
                    return endTime - startTime;
          }
          public double [] getFitnessTrace(){
                    return h.getFitnessTrace();
          }
          
          public int[] getHeuristicCallTimeRecord(){
                    return p.getHeuristicCallRecord();
          }
          
          public int getTotalHeuristicCalls(){
                    int counter = 0;
                    for (int y: p.getHeuristicCallRecord()) {
                              counter += y;
                    }
                    return counter;
          }
          
          public void reset(CountDownLatch latch,Program prog,long seed,  int domain, int instance) throws Exception {
                    this.latch = latch; 
                    h = new SelectivePeturbativeHyperHeuristic(prog, seed);
                    switch (domain) {
                              case 0:
                                        p = new SAT(seed);
                                        break;
                              case 1:
                                        p = new BinPacking(seed);
                                        break;
                              case 2:
                                        p = new PersonnelScheduling(seed);
                                        break;
                              case 3:
                                        p = new FlowShop(seed);
                                        break;
                              case 4:
                                        p = new TSP(seed);
                                        break;
                              case 5:
                                        p = new VRP(seed);
                                        break;
                              default:
                                        throw new Exception("There is no problem domain with this index");
                    }
          }
          
          @Override
          public String toString(){
                    return h.toString();
          }

}
