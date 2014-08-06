/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 *A Runnable class that toggles solenoids in parallel with a single input.
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
    private final DigitalInput switch1;
    private ExecutiveOrder control = null;
    
    private volatile boolean running;
    
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
        this.running = true;
        this.toggler = toggler;
        this.joystickName = joystickName;
        this.solenoid1 = solenoid1;
        this.solenoid2 = solenoid2;
        this.inputType = inputType;
        this.highMargin = 0.4;
        this.lowMargin = -0.4;
        this.switch1 = null;
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
        this.running = true;
        this.toggler = toggler;
        this.joystickName = joystickName;
        this.solenoid1 = solenoid1;
        this.solenoid2 = solenoid2;
        this.inputType = inputType;
        this.highMargin = highMargin;
        this.lowMargin = lowMargin;
        this.switch1 = null;
    }
    
    /**
     * Constructor.
     * Uses a switch to toggle the solenoid.
     * @param switch1 The DigitalInput switch.
     * @param solenoid1 The first solenoid.
     * @param solenoid2 The second solenoid.
     */
    public SolenoidClick(DigitalInput switch1, Solenoid solenoid1, Solenoid solenoid2) {
        this.running = true;
        this.switch1 = switch1;
        this.solenoid1 = solenoid1;
        this.solenoid2 = solenoid2;
        this.inputType = "switch";
        this.toggler = 1;
        this.joystickName = new Joystick(10);
        this.highMargin = 0.4;
        this.lowMargin = -0.4;
    }
    
    /**
     * Constructor. Creates an instance of SolenoidClick that uses an ExecutiveOrder object for input. 
     * @param toggler The ID of the button that will toggle the solenoids.
     * @param control The ExecutiveOrder object
     * @param solenoid1 The first solenoid
     * @param solenoid2 The second solenoid
     * @param inputType Axis or Button?
     */
    public SolenoidClick(int toggler, ExecutiveOrder control, Solenoid solenoid1, Solenoid solenoid2, String inputType){
        this.running = true;
        this.toggler = toggler; 
        this.control = control;
        this.joystickName = null;
        this.solenoid1 = solenoid1;
        this.solenoid2 = solenoid2;
        this.inputType = inputType;
        this.highMargin = 0.4;
        this.lowMargin = -0.4;
        this.switch1 = null;
    }
  
    /**
     * Is called once when a SolenoidClick object is started by a Thread object.
     * @exception IllegalArgumentException If inputType is invalid.
     */
    public void run() {
        running = true;
        if(control != null && inputType.equalsIgnoreCase("button")){
            executiveButtonToggle();  
        }
        else if(control != null && inputType.equalsIgnoreCase("axis")){
            executiveAxisToggle(); 
        }
        else if(inputType.equalsIgnoreCase("button")) {
            buttonToggle();
        }
        else if(inputType.equalsIgnoreCase("axis")) {
            axisToggle();
        }
        else if(inputType.equalsIgnoreCase("switch") && switch1 != null) {
            switchToggle();
        }
        else {
            throw new IllegalArgumentException(inputType + " is not a valid type of input.");
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
    
    /**
     * Toggles solenoids with a switch.
     */
    public void switchToggle() {
        while(running) {
            boolean pressed = switch1.get();
            
            if(pressed) {
                solenoid1.set(!solenoid1.get());
                solenoid2.set(!solenoid2.get());
                while(pressed) {
                    pressed = switch1.get();
                    solenoid1.set(solenoid1.get());
                    solenoid2.set(solenoid2.get());
                }
            }
        }
        
    }

    /**
     *Uses an ExecutiveOrder object and a button to toggle solenoids.
     */
    public void executiveButtonToggle() {
      while(running) {       
            boolean pressed = getButtonPressed();  
       
        
            if(pressed) {
                solenoid1.set(!solenoid1.get());
                solenoid2.set(!solenoid2.get());
                while(pressed) {
                    pressed = getButtonPressed(); 
                    solenoid1.set(solenoid1.get());
                    solenoid2.set(solenoid2.get());
                }
            }   
        
        }
    }
    
    /**
     *Uses an ExecutiveOrder object and an axis to toggle solenoids.
     */
    public void executiveAxisToggle() {
      while(running) {       
        boolean pressed = getAxisPressed();  
       
        
            if(pressed) {
                solenoid1.set(!solenoid1.get());
                solenoid2.set(!solenoid2.get());
                while(pressed) {
                    pressed = getAxisPressed(); 
                    solenoid1.set(solenoid1.get());
                    solenoid2.set(solenoid2.get());
                }
            }   
        
        } 
    }
    
    /**
     * Uses an ExecutiveOrder object to check if the button is pressed.
     * @return pressed 
     */
    public boolean getButtonPressed() {
        boolean pressed = false;
        
        if(control.president.getRawButton(toggler)){
            control.trap();
            pressed = true;
        }
        else if(control.congress.getRawButton(toggler) && control.releaseState){
            pressed = true;
        }
        
        
        return pressed;
    }

    /**
     *Uses an ExecutiveOrder object to check if the axis is in the defined margin.
     * @return pressed
     */
    public boolean getAxisPressed() {
        boolean pressed = false;
        double presidentAxis = control.president.getRawAxis(toggler);
        double congressAxis = control.congress.getRawAxis(toggler);
            
        if(presidentAxis >= highMargin || presidentAxis  <= lowMargin){
            control.trap();
            pressed = true;
        }
        else if((congressAxis >= highMargin || congressAxis  <= lowMargin) && control.releaseState){
            pressed = true;
        }
        
        
        return pressed; 
        
    }
    
     public void stop() {
        running = false;
    }
    
}


