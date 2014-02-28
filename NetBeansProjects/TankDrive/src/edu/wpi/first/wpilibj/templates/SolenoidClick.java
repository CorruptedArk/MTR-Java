/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

/**
 *A Runnable class that toggles solenoids in parallel with a single button.
 * @author Noah
 */
public class SolenoidClick implements Runnable{
    private final int toggler;
    private final Joystick joystickName;
    private final Solenoid solenoid1;
    private final Solenoid solenoid2;
    private final String inputType;
    private final double highMargin;
    private final double lowMargin;
    
    private static boolean running = true;
    
    /**
     * This constructor passes the needed objects to control the solenoid.
     * It uses a default margin if an axis is used.
     * @param toggler The ID of the button to toggle with
     * @param joystickName The joystick object used for input
     * @param solenoid1 The first solenoid 
     * @param solenoid2 The second solenoid
     * @param inputType Axis or Button?
     */
    public SolenoidClick(int toggler, Joystick joystickName, Solenoid solenoid1, Solenoid solenoid2, String inputType) {
        this.toggler = toggler;
        this.joystickName = joystickName;
        this.solenoid1 = solenoid1;
        this.solenoid2 = solenoid2;
        this.inputType = inputType;
        this.highMargin = 0.4;
        this.lowMargin = -0.4;
    }
    
    /**
     * This constructor passes the needed objects to control the solenoid.
     * This constructor uses a custom margin.
     * @param toggler The ID of the button to toggle with
     * @param joystickName The joystick object used for input
     * @param solenoid1 The first solenoid
     * @param solenoid2 The second solenoid
     * @param inputType Axis or Button?
     * @param highMargin The high margin for the axis
     * @param lowMargin The low margin for the axis
     */ 
    public SolenoidClick(int toggler, Joystick joystickName, Solenoid solenoid1, Solenoid solenoid2, String inputType, double highMargin, double lowMargin) {
        this.toggler = toggler;
        this.joystickName = joystickName;
        this.solenoid1 = solenoid1;
        this.solenoid2 = solenoid2;
        this.inputType = inputType;
        this.highMargin = highMargin;
        this.lowMargin = lowMargin;
    }
    
  
    
    public void run() {
      
        if(inputType.equalsIgnoreCase("button")) {
            buttonToggle();
        }
        else if(inputType.equalsIgnoreCase("axis")) {
            axisToggle();
        }

   }     
    
    /**
     * Toggles solenoids with a button.
     */
    public void buttonToggle() {
        while(running) {       
            boolean pressed = joystickName.getRawButton(toggler);
        
            if(pressed) {
                solenoid1.set(!solenoid1.get());
                solenoid2.set(!solenoid2.get());
                while(pressed) {
                    pressed = joystickName.getRawButton(toggler);
                    solenoid1.set(solenoid1.get());
                    solenoid2.set(solenoid2.get());
                }
            }   
        
        }
    }
    
    /**
     * Toggles solenoids with an axis.
     */
    public void axisToggle() {
        while(running) {
            boolean pressed;
            double axisVal = joystickName.getRawAxis(toggler);
            pressed = axisVal >= highMargin || axisVal <= lowMargin;
            
            
            if(pressed) {
                solenoid1.set(!solenoid1.get());
                solenoid2.set(!solenoid2.get());  
                while(pressed) {
                    axisVal = joystickName.getRawAxis(toggler);
                    pressed = axisVal >= highMargin || axisVal <= lowMargin;
                    solenoid1.set(solenoid1.get());
                    solenoid2.set(solenoid2.get());
                }
            }
        }
    }
    
     public void stop() {
        running = false;
    }
    
}


