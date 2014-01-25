/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;

/**
 *
 * @author Noah
 */
public class AirRunnable implements Runnable {
    Compressor airCompressor = new Compressor(1,1);
   
    private static boolean running = true;
    
    public void run() {
       while(running){
            if (airCompressor.getPressureSwitchValue()) {
                airCompressor.stop();
            }
            else {
                airCompressor.start();
            }
       }
    }
    
    public void stop() {
        running = false;
    }
}
