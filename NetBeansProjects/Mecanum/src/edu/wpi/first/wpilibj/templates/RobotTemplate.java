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
        myDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        myDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
    }
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        myDrive.setSafetyEnabled(false);  
        s1.set(false); // sets initial s1 value
        s2.set(true);  // sets initial s2 value
        airThread.start(); // starts compressor switching loop in parallel.
        myDrive.mecanumDrive_Cartesian(1.0, 0.0, 0.0, 0.0); // starts movement
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
            myDrive.mecanumDrive_Cartesian(buffer(1, moveStick, true), buffer(2, moveStick, true), buffer(4, moveStick, true), 0.0);
            motorOne.set(buffer(3, moveStick, false));
            solenoidToggle(1,2,moveStick, s1, s2);
            
           
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
	* This function buffers the moveStick.getRawAxis() input.
        * @param axisNum The ID for the axis in moveStick.
        * @param joystickName The Joystick that input is coming from. 
        * @param inverted Is it flipped?
        * @return moveOut - The buffered axis data from joystickName.getRawAxis().
	**/
    public double buffer(int axisNum,Joystick joystickName, boolean inverted) {
        double moveIn = joystickName.getRawAxis(axisNum);
        double moveOut;
        moveOut = 0.0;
        
       
        if(moveIn >= -0.2 && moveIn <= 0.20 ) {
         moveOut = 0.0;
        }
        else{
            if(inverted){
                moveOut = -moveIn;
            }
            if(!inverted)
                moveOut = moveIn;
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
     * This function toggles the solenoids with two buttons.
     * @param offButton ID of button to deactivate 
     * @param onButton ID of button to activate
     * @param joystickName Name of Joystick input is coming from
     * @param solenoid1 The first solenoid
     * @param solenoid2 The second solenoid
     */
   
    public void solenoidToggle(int offButton, int onButton, Joystick joystickName, Solenoid solenoid1, Solenoid solenoid2 ) {
       boolean pressedOn = joystickName.getRawButton(onButton);
       boolean pressedOff = joystickName.getRawButton(offButton);
       
       if (pressedOn) {
        solenoid1.set(true);
        solenoid2.set(false);
       }
       if (pressedOff) {
        solenoid1.set(false);
        solenoid2.set(true);
       }
       
     } 
    
    /**
     * This function toggles the solenoid with one button. 
     * @param toggleButton ID of button to toggle with.
     * @param joystickName Name of Joystick
     * @param solenoid1 First Solenoid
     * @param solenoid2 Second Solenoid
     */
    public void solenoidClick(int toggleButton, Joystick joystickName, Solenoid solenoid1, Solenoid solenoid2) {
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
      




