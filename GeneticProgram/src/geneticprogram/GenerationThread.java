/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticprogram;

/**
 *
 * @author rudo
 */
public class GenerationThread extends Thread {

    private long seed;
    
    private Thread t;
    public GenerationThread() {
    }

    @Override
    public void run() {
        try {
            System.out.println("starting thread");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
