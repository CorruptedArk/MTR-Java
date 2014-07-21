/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
/**
 *
 * @author Noah
 */
public class ExecutiveOrder {
    
    public final Joystick president;
    public final Joystick congress;
    public final int release;
    public volatile boolean releaseState; 
    
    public ExecutiveOrder(Joystick president, Joystick congress, int release){
        this.releaseState = true;
        this.president = president;
        this.congress = congress;
        this.release = release;
    } 
    
    public void trap(){
       releaseState = false; 
    }
    
    public void release(){
       releaseState = true;  
    }
    
}
