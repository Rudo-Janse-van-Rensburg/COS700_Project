package geneticprogram;

public class Interface {
          
          public Interface() {

          }

          public void run() throws Exception{
                    for (int run = 0; run < 10; run++) {
                              runExperiment(run);
                    }
          }
          
          private void runExperiment(int run) throws Exception {
                    Evolution evolution = Evolution.getInstance();
                    do {
                              evolution.print();
                    } while (evolution.evolveGeneration());
                    
                    Program prog = evolution.getBest_program();
                    
                    CompetitionRunner competitionRunner = CompetitionRunner.getInstance(run);
                    
                    
          }

}
