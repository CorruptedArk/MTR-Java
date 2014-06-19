/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Noah
 */
public class DriveState implements Runnable {
    public volatile boolean orientation;
    
    private volatile Joystick controller;
    private volatile int buttonID;
    private volatile boolean running;
    
    
    public DriveState(boolean defaultState,Joystick controller,int buttonID){
        this.orientation = defaultState;
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
    }
}
