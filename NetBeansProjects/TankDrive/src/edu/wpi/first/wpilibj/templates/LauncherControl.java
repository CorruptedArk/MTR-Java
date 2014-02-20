/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

/**
 * This Runnable subclass runs a polling loop waiting for controller input and
 * runs semi-autonomous code to launch the ball.
 * @author Noah
 */
public class LauncherControl implements Runnable {
    
    private final AnalogChannel analSensor1;
    private final AnalogChannel analSensor2;
    private final Victor pivot;
    private final Joystick joystick;
    private final int button;
    
    private static boolean running = true;
    
    /**
     * Constructor
     * @param analSensor1 The sensor closest to the pivot.
     * @param analSensor2 The sensor farthest from the pivot.
     * @param pivot The motor controlling the launcher.
     * @param joystick The joystick to used to initiate the firing sequence.
     * @param button The button to fire with.
     */
    public LauncherControl(AnalogChannel analSensor1, AnalogChannel analSensor2, Victor pivot, Joystick joystick, int button) {
        this.analSensor1 = analSensor1;
        this.analSensor2 = analSensor2;
        this.pivot = pivot;
        this.joystick = joystick;
        this.button = button;
    }
    
    public void run(){
        while(running) {
            boolean pressed = joystick.getRawButton(button);
            if(pressed) {
                boolean sensor1State = analSensor1.getVoltage()/0.0048828 >= 12.0;
                while(sensor1State){
                    sensor1State = analSensor1.getVoltage()/0.0048828 >= 12.0;
                    pivot.set(1.0);
                }
                pivot.set(-1.0);
                boolean sensor2State = analSensor2.getVoltage()/0.0048828 >= 12.0;
                while(sensor2State) {
                    pivot.set(-0.5);
                }
                pivot.set(0.5);
                Timer.delay(0.01);
                pivot.set(0.0);
                
            }
            
        }
        
    }
    
    public void stop() {
        running = false;
        pivot.set(0.0);
    }
    
}
