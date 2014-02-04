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
import edu.wpi.first.wpilibj.Victor;

//import edu.wpi.first.wpilibj.Compressor;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends SimpleRobot {
    final int frontLeft = 2;
    final int rearLeft = 3;
    final int frontRight = 1;
    final int rearRight = 4;
    
    Solenoid s1;
    Solenoid s2;
   //Compressor airCompressor;
    RobotDrive myDrive;
    Joystick moveStick;
    AirRunnable airRun;
    Thread airThread; 
    Victor motorOne;
    
    /**
	*This initializes the motors and controls.
	**/
    public void robotInit() {
        myDrive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
        moveStick = new Joystick(1);
        //airCompressor = new Compressor(1,1);
        s1 = new Solenoid(3);
        s2 = new Solenoid(4);
        airRun = new AirRunnable();
        airThread = new Thread(airRun);
        motorOne = new Victor(5);
        
    }
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        myDrive.setSafetyEnabled(false);  
        s1.set(false); // sets initial s1 value
        s2.set(true);  // sets initial s2 value
        airThread.start(); // starts compressor switching loop in parallel.
        myDrive.mecanumDrive_Cartesian(0.0, 1.0, 0.0, 0.0); // starts movement
        Timer.delay(3.0); // delays input for 3 seconds
        myDrive.mecanumDrive_Cartesian(0.0, 0.0, 0.0, 0.0); // stops movement
        s1.set(true); // switches s1 value
        s2.set(false); // switches s2 value
        Timer.delay(2.0); // delays input for 2 seconds
        s1.set(false); // switches s1 value
        s2.set(true); // switches s2 value
        airRun.stop(); // ends compressor switching loop 
        //airCompressor.stop(); // turns off the compressor
    
    }

    
    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        airThread.start(); // starts automatic compressor switching in parallel
        while (isOperatorControl() && isEnabled()) {
            //compressManual(2); // Use to manually switch compressor.
            myDrive.setSafetyEnabled(true);
            myDrive.mecanumDrive_Cartesian(bufferMove(1), bufferMove(2), bufferMove(4), 0.0);
            motorOne.set(bufferMove(3));
            solenoidToggle(1,2);
            
           
            Timer.delay(0.01);
        }
        airRun.stop(); // stops automatic switching.
        //airCompressor.stop(); // disables the compressor
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
    /*public void compressManual(int buttonId) {
        boolean pressed = moveStick.getRawButton(buttonId);
        
        if (airCompressor.enabled() && pressed) {
            airCompressor.stop();
        }
        if (!airCompressor.enabled() && pressed) {
            airCompressor.start();
        }
        
        
    }*/
    
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
         * This toggles the compressor by pressure in a single thread.
         */
  /*  public void compressAuto() {
       if (airCompressor.getPressureSwitchValue()) {
           airCompressor.stop();
       }
       else {
           airCompressor.start();
       }
   }*/
    
     /**
     * This function toggles the solenoids.
     * @param offButton ID of button to deactivate 
     * @param onButton ID of button to activate
     */
   
    public void solenoidToggle(int offButton, int onButton) {
       boolean pressedOn = moveStick.getRawButton(offButton);
       boolean pressedOff = moveStick.getRawButton(onButton);
       
       if (pressedOn) {
        s1.set(true);
        s2.set(false);
       }
       if (pressedOff) {
        s1.set(false);
        s2.set(true);
       }
       
       
       
   } 
    
    public void solenoidClick() {
        
    }
    
    
} 
      




