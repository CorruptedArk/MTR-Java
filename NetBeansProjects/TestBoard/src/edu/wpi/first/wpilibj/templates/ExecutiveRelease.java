/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Timer;

/**
 *A Runnable class that manages the releaseState of an ExecutiveOrder class.
 * @author Noah
 */
public class ExecutiveRelease implements Runnable {
    private final ExecutiveOrder control;
    
    private volatile boolean running;

    /**
     *A constructor. This passes the ExecutiveOrder object that will be managed.
     * @param control The ExecutiveOrder object.
     */
    public ExecutiveRelease(ExecutiveOrder control){
        this.control = control;
    }

    public void run() {
       running = true; 
       while(running) {
          boolean pressed = control.president.getRawButton(control.release);
          
          if(pressed){
              control.release();
              while(pressed){
                  pressed = control.president.getRawButton(control.release);
                  Timer.delay(0.005);
              }
          }
         Timer.delay(0.005); 
       }
    }
    
    public void stop() {
        running = false;
        control.release();
    }
    
    
}
