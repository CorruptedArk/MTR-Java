/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Compressor;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends SimpleRobot {
    final int frontLeft = 1;
    final int rearLeft = 2;
    final int frontRight = 3;
    final int rearRight = 4;
    
    Solenoid s1;
    Solenoid s2;
    Compressor airCompressor;
    RobotDrive myDrive;
    Joystick moveStick;
    
    /**
	*This initializes the motors and controls.
	**/
    public void robotInit() {
        myDrive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
        moveStick = new Joystick(1);
        airCompressor = new Compressor(1,1);
        s1 = new Solenoid(3);
        s2 = new Solenoid(4);
        
    }
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        myDrive.setSafetyEnabled(false);
        s1.set(false);
        s2.set(true);
        while(!airCompressor.getPressureSwitchValue()){
            airCompressor.start();
        }
        airCompressor.stop();
        myDrive.mecanumDrive_Cartesian(0.0, 1.0, 0.0, 0.0);
        Timer.delay(3.0);
        myDrive.mecanumDrive_Cartesian(0.0, 0.0, 0.0, 0.0);
        s1.set(true);
        s2.set(false);
        Timer.delay(2.0);
        s1.set(false);
        s2.set(true);
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        airCompressor.start(); // Uncomment if compressAuto() is active.
        while (isOperatorControl() && isEnabled()) {
            //compressManual(2); // Uncomment to manually switch.
            compressAuto(); // Uncomment to let it switch automatically.
            myDrive.setSafetyEnabled(true);
            myDrive.mecanumDrive_Cartesian(bufferMove(1), bufferMove(2), bufferMove(4), 0.0);
            s1.set(moveStick.getRawButton(1));
            s2.set(!moveStick.getRawButton(1));
           
            Timer.delay(0.01);
        }
        airCompressor.stop(); // Uncomment if compressAuto() is active.
}    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
    
    }
    
    /**
     * This function lets you toggle the compressor.
     * @param buttonId ID of button on controller
     */
    public void compressManual(int buttonId) {
        boolean pressed = moveStick.getRawButton(buttonId);
        
        if (airCompressor.enabled() && pressed) {
            airCompressor.stop();
        }
        if (!airCompressor.enabled() && pressed) {
            airCompressor.start();
        }
        
        
    }
    
	/**
	* This function buffers the moveStick.getRawAxis() input.
        * @param axisNum The ID for the axis in moveStick.
        * @return moveOut - The buffered axis data from moveStick.getRawAxis().
	**/
    public double bufferMove(int axisNum) {
        double moveIn = moveStick.getRawAxis(axisNum);
        double moveOut;
       
        if(moveIn >= -0.10 && moveIn <= 0.10 ) {
         moveOut = 0.0;
        }
        else{
         moveOut = moveStick.getRawAxis(axisNum);
        }
	
	return moveOut;
   }
   
        /**
         * This toggles the compressor by pressure.
         */
    public void compressAuto() {
       if (airCompressor.getPressureSwitchValue()) {
           airCompressor.stop();
       }
       else {
           airCompressor.start();
       }
   }
    
    
} 
      




