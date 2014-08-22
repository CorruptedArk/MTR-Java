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
        pistonOne = new SolenoidClick(1,joystickOne,pullOne,pushOne,"button");
        pistonTwo = new SolenoidClick(2,joystickOne,pullTwo,pushTwo,"button");
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
            double motorOneValue = buffer(2,joystickOne,true,0.18,-0.18);
            double motorTwoValue = buffer(5,joystickOne,true,0.18,-0.18);
            motorOne.set(motorOneValue);
            motorTwo.set(motorTwoValue);
            encoder.setDistancePerPulse(1);
            SmartDashboard.putNumber("Pulses Per Second", encoder.getRate());
            Timer.delay(motorOneValue);
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
    * Controller Mapping
    1: A
    2: B
    3: X
    4: Y
    5: Left Bumper
    6: Right Bumper
    7: Back
    8: Start
    9: Left Joystick
    10: Right Joystick

    The axis on the controller follow this mapping
    (all output is between -1 and 1)
    1: Left Stick X Axis
    -Left:Negative ; Right: Positive
    2: Left Stick Y Axis
    -Up: Negative ; Down: Positive
    3: Triggers
    -Left: Positive ; Right: Negative
    4: Right Stick X Axis
    -Left: Negative ; Right: Positive
    5: Right Stick Y Axis
    -Up: Negative ; Down: Positive
    6: Directional Pad (Not recommended, buggy)
    */
    
}
