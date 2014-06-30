/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author Noah
 */
public class ExecutiveRelease implements Runnable {
    private final ExecutiveOrder control;
    
    private volatile boolean running;
    
    public ExecutiveRelease(ExecutiveOrder control){
        this.control = control;
        this.running = true;
    }

    public void run() {
       while(running) {
          boolean pressed = control.president.getRawButton(control.release);
          
          if(pressed){
              control.release();
              while(pressed){
                  
              }
          }
       }
    }
    
    public void stop() {
        running = false;
    }
    
    
}
