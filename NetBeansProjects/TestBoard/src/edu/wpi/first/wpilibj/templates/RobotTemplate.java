/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends SimpleRobot {
    
    //Constants for Buttons
    static final int A_BUTTON = 1;
    static final int B_BUTTON = 2;
    static final int X_BUTTON = 3;
    static final int Y_BUTTON = 4;
    static final int LEFT_BUMPER = 5;
    static final int RIGHT_BUMPER = 6;
    static final int BACK_BUTTON = 7;
    static final int START_BUTTON = 8;
    static final int LEFT_JOYSTICK_CLICK = 9;
    static final int RIGHT_JOYSTICK_CLICK = 10;
    
    //Constants for Axes
    static final int LEFT_X_AXIS = 1;
    static final int LEFT_Y_AXIS = 2;
    static final int TRIGGERS_AXIS = 3;
    static final int RIGHT_X_AXIS = 4;
    static final int RIGHT_Y_AXIS = 5;
    static final int D_PAD = 6; // Buggy, not recommended
    
    
    Joystick joystickOne;
    Victor motorOne;
    Talon motorTwo;
    Compressor airCompressor;
    AirRunnable airRun;
    Solenoid pullOne, pushOne, pullTwo, pushTwo;
    SolenoidClick pistonOne, pistonTwo;
    Thread airThread, pistonOneThread, pistonTwoThread;
    Encoder encoder;
    DigitalInput limit1;
    DigitalInput limit2;
   
    
    
    
    
    
    
    //This initializes the motors and controls.
    public void robotInit() {
        joystickOne = new Joystick(1);
        motorOne = new Victor(1);
        motorTwo = new Talon(2);
        airCompressor = new Compressor(1,1);
        airRun = new AirRunnable(airCompressor);
        pullOne = new Solenoid(1);
        pushOne = new Solenoid(2);
        pullTwo = new Solenoid(3);
        pushTwo = new Solenoid(4);
        encoder = new Encoder(4,5,false);
        limit1 = new DigitalInput(2);
        limit2 = new DigitalInput(3);
        pistonOne = new SolenoidClick(A_BUTTON,joystickOne,pullOne,pushOne,"button");
        pistonTwo = new SolenoidClick(B_BUTTON,joystickOne,pullTwo,pushTwo,"button");
        airThread = new Thread(airRun);
        pistonOneThread = new Thread(pistonOne);
        pistonTwoThread = new Thread(pistonTwo);
    }
    
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        
        pullOne.set(true);
        pushOne.set(false);
        pullTwo.set(true);
        pushTwo.set(false);
        
        airThread = new Thread(airRun);
        pistonOneThread = new Thread(pistonOne);
        pistonTwoThread = new Thread(pistonTwo);
        
        airThread.start();
        pistonOneThread.start();
        pistonTwoThread.start();
        
        while(isOperatorControl() && isEnabled()){
            double motorOneValue = buffer(LEFT_Y_AXIS,joystickOne,true,0.18,-0.18);
            double motorTwoValue = buffer(RIGHT_Y_AXIS,joystickOne,true,0.18,-0.18);
            motorOne.set(motorOneValue);
            motorTwo.set(motorTwoValue);
            encoder.setDistancePerPulse(1);
            SmartDashboard.putNumber("Pulses Per Second", encoder.getRate());
            Timer.delay(0.01);
        }

        airRun.stop();
        pistonOne.stop();
        pistonTwo.stop();
    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
    
    }
    
    /**
     * This function buffers Joystick.getRawAxis() input.
     * @param axisNum The ID for the axis of a Joystick.
     * @param joystickName The Joystick that input is coming from. 
     * @param inverted Is it flipped?
     * @param highMargin The high margin of the buffer.
     * @param lowMargin The low margin of the buffer.
     * @return moveOut - The buffered axis data from Joystick.getRawAxis().
     **/
    public double buffer(int axisNum,Joystick joystickName, boolean inverted, double highMargin, double lowMargin) {
        double moveIn = joystickName.getRawAxis(axisNum);
        double moveOut;
        moveOut = 0.0;
        
        if(moveIn >= lowMargin && moveIn <= highMargin ) {
            moveOut = 0.0;
        }
        else{
            if(inverted){
                moveOut = -moveIn;
            }
            else if(!inverted){ 
                moveOut = moveIn;
            }    
        }
	
	return moveOut;
   }
    
    /**
     * This function buffers Joystick.getRawAxis() input.
     * @param axisNum The ID for the axis of a Joystick.
     * @param joystickName The Joystick that input is coming from. 
     * @param inverted Is it flipped?
     * @param highMargin The high margin of the buffer.
     * @param lowMargin The low margin of the buffer.
     * @param scale The amount you want to divide the output by.
     * @return moveOut - The buffered axis data from joystickName.getRawAxis().
     **/
    public double buffer(int axisNum, Joystick joystickName, boolean inverted, double highMargin, double lowMargin, double scale) {
        double moveIn = joystickName.getRawAxis(axisNum);
        double moveOut;
        moveOut = 0.0;
        
        if(moveIn >= lowMargin && moveIn <= highMargin ) {
            moveOut = 0.0;
        }
        else{
            if(inverted){
                moveOut = -moveIn;
            }
            else if(!inverted){ 
                moveOut = moveIn;
            }    
        }
	
        if(scale <= 1){
            scale = 1;
        }
        
        moveOut = moveOut/scale;
        
	return moveOut;
   }
    
}
