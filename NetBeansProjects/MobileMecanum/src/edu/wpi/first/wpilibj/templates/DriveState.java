/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

/**
 * A Runnable class using a button to toggle what is forward and backward on a robot.
 * It does this by switching the state of a boolean variable. 
 * That value is meant to be passed to my buffer() function in RobotTemplate.
 * @author Noah
 */
public class DriveState implements Runnable {
    private volatile boolean orientation;
    private final boolean defaultState;
    private final Joystick controller;
    private final int buttonID;
    private volatile boolean running;
    private ExecutiveOrder control = null;
    
    
    /**
     * A constructor using a single Joystick.
     * @param defaultState What state will the robot start out with?
     * @param controller the Joystick that the button is on
     * @param buttonID the ID of the button
     */
    public DriveState(boolean defaultState,Joystick controller,int buttonID){
        this.orientation = defaultState;
        this.defaultState = defaultState;
        this.controller = controller;
        this.buttonID = buttonID;
        
    }
  
    /**
     * A constructor using an ExecutiveOrder.
     * @param defaultState What state will the robot start out with?
     * @param control the ExecutiveOrder controlling the switching
     * @param buttonID The ID of the button
     */
    public DriveState(boolean defaultState,ExecutiveOrder control,int buttonID){
        this.orientation = defaultState;
        this.defaultState = defaultState;
        this.control = control;
        this.buttonID = buttonID;
        this.controller = null;
    }
    
    
    
    /**
     * Returns the orientation value.
     * @return orientation
     */

    public synchronized boolean getOrientation(){
        return orientation;
    }
    
    public void run(){
        running = true;
        Joystick currentDriver;
        while(running){
            if(controller == null){
                if(control.getReleaseState()){
                    currentDriver = control.congress;
                }
                else {
                    currentDriver = control.president;
                }
            }else{
                currentDriver = controller;
            }
            setOrientation(currentDriver);
            Timer.delay(0.005);
        }
        
    }
    
    private void setOrientation(Joystick currentController){
        boolean pressed = currentController.getRawButton(buttonID);
            
            if(pressed){
                orientation = !orientation;
                while(pressed){
                    pressed = currentController.getRawButton(buttonID);
                    Timer.delay(0.005);
                }
            }
    }
    
    public void stop(){
        running = false;
        orientation = defaultState;
    }
}
