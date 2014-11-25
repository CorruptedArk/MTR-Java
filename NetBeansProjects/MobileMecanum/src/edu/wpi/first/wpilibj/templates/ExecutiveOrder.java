/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
/**
 * A class that helps establish a hierarchy between two robot operators.
 * @author Noah
 */
public class ExecutiveOrder {
    
    public final Joystick president;
    public final Joystick congress;
    public final int release;
    private boolean releaseState;

    /**
     * A constructor.
     * @param president The lead driver's controller.
     * @param congress The secondary driver's controller.
     * @param release The button deciding whether or not congress will be trapped.
     */
    public ExecutiveOrder(Joystick president, Joystick congress, int release){
        this.releaseState = true;
        this.president = president;
        this.congress = congress;
        this.release = release;
    }

    /**
     *Sets releaseState to false.
     */
    public synchronized void trap(){
       releaseState = false; 
    }
    
    /**
     *Sets releaseState to true.
     */
    public synchronized void release(){
       releaseState = true;  
    }
    
    /**
     *Returns the current releaseState value 
     * @return releaseState
     */
    public synchronized boolean getReleaseState() {
       return releaseState; 
    }
    
}
