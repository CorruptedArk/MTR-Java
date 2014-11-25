/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * This Runnable subclass runs a polling loop waiting for controller input and
 * runs semi-autonomous code to launch the ball. DO NOT RUN THIS IF THERE ARE 
 * OTHER SOLENOID CONTROLS IN USE. THIS IS UNTESTED.
 * @author Noah
 */
public class LauncherControl implements Runnable {
    
    private final Solenoid latchRetract;
    private final Solenoid latchExtend;
    private final Solenoid tensionPull1;
    private final Solenoid tensionPush1;
    private final Solenoid tensionPull2;
    private final Solenoid tensionPush2;
    private final Joystick joystick;
    private final int button;
    
    private volatile boolean running;
    
    /**
     * Constructor
     * @param joystick The joystick to used to initiate the firing sequence.
     * @param button The button to fire with.
     * @param latchRetract The Solenoid that pulls the latch.
     * @param latchExtend The Solenoid that pushes the latch.
     * @param tensionPull1 The Solenoid that pulls one of the large pistons.
     * @param tensionPush1 The Solenoid that pushes one of the large pistons.
     * @param tensionPull2 The Solenoid that pulls the other large piston.
     * @param tensionPush2 The SOlenoid that pushes the other large piston.
     */
    public LauncherControl(Joystick joystick, int button, Solenoid latchRetract,
            Solenoid latchExtend, Solenoid tensionPull1, Solenoid tensionPush1, 
            Solenoid tensionPull2, Solenoid tensionPush2) {
        this.running = true;
        this.joystick = joystick;
        this.button = button;
        this.latchRetract = latchRetract;
        this.latchExtend = latchExtend;
        this.tensionPull1 = tensionPull1;
        this.tensionPush1 = tensionPush1;
        this.tensionPull2 = tensionPull2;
        this.tensionPush2 = tensionPush2;
    }
    
    public void run(){
        running = true;
        while(running) {
            boolean pressed = joystick.getRawButton(button);
            if(pressed) {
                latchRetract.set(false);
                latchExtend.set(true);
                tensionPull1.set(true);
                tensionPush1.set(false);
                tensionPull2.set(true);
                tensionPush2.set(false);
                Timer.delay(8.0);
                latchRetract.set(true);
                latchExtend.set(false);
                Timer.delay(2);
                tensionPull1.set(false);
                tensionPush1.set(true);
                tensionPull2.set(false);
                tensionPush2.set(true);
            }
            Timer.delay(0.005); 
        }
        
    }
    
    public void stop() {
        running = false;
        
    }
    
}
