/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;

/**
 * A Runnable class using a button to toggle what is forward and backward on a robot.
 * It does this by switching the state of a boolean variable. 
 * That value is then passed to the buffer() function in RobotTemplate.
 * @author Noah
 */
public class DriveState implements Runnable {
    public volatile boolean orientation;
    private final boolean defaultState;
    private final Joystick controller;
    private final int buttonID;
    private volatile boolean running;
    
    
    /**
     * A constructor passing all necessary information to the object.
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
    
    public void run(){
        running = true;
        while(running){
            boolean pressed = controller.getRawButton(buttonID);
            
            if(pressed){
                orientation = !orientation;
                while(pressed){
                    pressed = controller.getRawButton(buttonID);
                    
                }
            }
            
        }
        
    }
    
    public void stop(){
        running = false;
        orientation = defaultState;
    }
}
