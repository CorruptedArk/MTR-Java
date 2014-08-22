/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Timer;

/**
 *A Runnable class to toggle the compressor on and off in parallel.
 * @author Noah
 */
public class AirRunnable implements Runnable {
    private final Compressor airCompressor;
   
    private volatile boolean running;
    
/**
 * This constructor passes the needed Compressor object.
 * @param airCompressor The Compressor object.
 */   
    public AirRunnable(Compressor airCompressor) {
        this.running = true;
        this.airCompressor = airCompressor;
    }
    
    public void run() {
       running = true;
        while(running){
           
           if (airCompressor.getPressureSwitchValue()) {
                airCompressor.stop();
            }
           else {
                airCompressor.start();
            }
           Timer.delay(0.005);
       }
    }
    
    public void stop() {
        running = false;
        airCompressor.stop();
    }
}
