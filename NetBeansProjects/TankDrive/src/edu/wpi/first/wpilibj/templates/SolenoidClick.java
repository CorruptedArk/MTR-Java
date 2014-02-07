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
    private final int toggleButton;
    private final Joystick joystickName;
    private final Solenoid solenoid1;
    private final Solenoid solenoid2;
    
    /**
     * This constructor passes the needed objects to control the solenoid.
     * @param toggleButton The ID of the button to toggle with
     * @param joystickName The joystick object used for input
     * @param solenoid1 The first solenoid 
     * @param solenoid2 The second solenoid
     */
    public SolenoidClick(int toggleButton, Joystick joystickName, Solenoid solenoid1, Solenoid solenoid2) {
        this.toggleButton = toggleButton;
        this.joystickName = joystickName;
        this.solenoid1 = solenoid1;
        this.solenoid2 = solenoid2;
    }
    
    public void run() {
        boolean pressed = joystickName.getRawButton(toggleButton);
        
        if (pressed) {
            solenoid1.set(!solenoid1.get());
            solenoid2.set(!solenoid2.get());
            while (pressed) {
                solenoid1.set(solenoid1.get());
                solenoid2.set(solenoid2.get());
            }
        }
        
    }
    
}
