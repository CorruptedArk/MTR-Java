/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;

/**
 * This Runnable subclass compares the currently measured distance of an 
 * Ultrasonic sensor to a wanted distance and outputs whether you are ready to fire
 * to SmartDashboard.
 * @author Noah
 */
public class UltrasonicApproval implements Runnable {
    
    private final AnalogChannel sensor;
    private final double wantedDistance;
    
    private static boolean running = true;
    
    /**
     * Constructor
     * @param sensor the sensor to be used
     * @param wantedDistance the distance to fire from
     */
    public UltrasonicApproval(AnalogChannel sensor, double wantedDistance) {
        this.sensor = sensor;
        this.wantedDistance = wantedDistance;
    }
    
    public void run(){
        running = true;
        double[] distances = new double[5];
        while(running){
            double sum = 0.0;
            for(int i = 0; i < distances.length; i++) {
               distances[i] = (sensor.getVoltage()/0.0048828125);
               sum = sum + distances[i];
               Timer.delay(0.5);
            }
            
            double average = sum / distances.length;
            
            double lowTolerance = wantedDistance - 5.0;
            double highTolerance = wantedDistance + 5.0;
            
            if(lowTolerance < average && average < highTolerance){
                SmartDashboard.putString("Ready to fire?", "Yeah, fire that ball!");
            }
            else{
                SmartDashboard.putString("Ready to fire?", "Nope.");
            }
        }
    }
    
    public void stop(){
        running = false;
        
    }
    
    
    
    
}
