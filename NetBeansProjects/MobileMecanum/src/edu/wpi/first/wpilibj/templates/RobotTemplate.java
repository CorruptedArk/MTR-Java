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
    
    
    Victor frontLeft;
    Victor rearLeft;
    Victor frontRight;
    Victor rearRight;
    Compressor airCompressor;
    Solenoid pull1;
    Solenoid push1;
    RobotDrive myDrive;
    Joystick moveStick;
    
    AirRunnable airRun;
    Thread airThread;
    SolenoidClick solenoidControl1;
    Thread solenoidThread1;
    
     
    SendableChooser autoChooser; 
    Integer autonomousID;
    SendableChooser teleChooser;
    Integer teleID;
    
    DriveState orientationSwitcher;
    Thread orientationThread;
    
    
    


//This initializes the motors and controls.
    public void robotInit() {
        frontLeft = new Victor(3);
        rearLeft = new Victor(4);
        frontRight = new Victor(2);
        rearRight = new Victor(1);
        myDrive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
        moveStick = new Joystick(1);
        
        airCompressor = new Compressor(1,1);
        pull1 = new Solenoid(8); 
        push1 = new Solenoid(7);
        airRun = new AirRunnable(airCompressor);
        orientationSwitcher = new DriveState(true,moveStick,A_BUTTON);
        
        myDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        myDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        
    }
    
     //This function is called once each time the robot enters autonomous mode.
    public void autonomous() {
        
        
    }

    
    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        airThread = new Thread(airRun);
        airThread.start(); // starts automatic compressor switching in parallel
        pull1.set(true);
        push1.set(false);
        
       
        solenoidControl1 = new SolenoidClick(B_BUTTON,moveStick,pull1,push1,"button"); 
        solenoidThread1 = new Thread(solenoidControl1);
        solenoidThread1.start();
        
        orientationThread = new Thread(orientationSwitcher);
        orientationThread.start();
        
        while (isOperatorControl() && isEnabled()) {
           myDrive.setSafetyEnabled(true);
           boolean inverted = orientationSwitcher.getOrientation();
           double xMovement = buffer(LEFT_X_AXIS,moveStick,inverted,0.18,-0.18);
           double yMovement = buffer(LEFT_Y_AXIS,moveStick,inverted,0.18,-0.18);
           double twist = buffer(RIGHT_X_AXIS,moveStick,true,0.18,-0.18);
           myDrive.mecanumDrive_Cartesian(xMovement, yMovement, twist, 0.0);
           
           
           Timer.delay(0.01);
        }
        airRun.stop(); // stops automatic switching.
        solenoidControl1.stop();
        orientationSwitcher.stop();
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
     * @return moveOut - The buffered axis data from joystickName.getRawAxis().
     **/
    public double buffer(int axisNum, Joystick joystickName, boolean inverted, double highMargin, double lowMargin) {
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
       else if (pressedOff) {
        solenoid1.set(false);
        solenoid2.set(true);
       }
       
    }
    
    /**
     * This function controls operation of a relay with a switch.
     * @param relayName The Relay object.
     * @param switchName1 The switch for forward motion.
     * @param switchName2 The switch for backward motion.
     */
    
    public void relayControl(Relay relayName, DigitalInput switchName1, DigitalInput switchName2){
        
        if(switchName1.get() && !switchName2.get()) {
            relayName.set(Relay.Value.kForward);
        }
        else if(!switchName1.get() && switchName2.get()) {
            relayName.set(Relay.Value.kReverse);
        }
        else{
            relayName.set(Relay.Value.kOff);
        }
    }
    
    /**
     * This runs the winch with an AnalogChannel senor.
     * @param relayName The relay spike.
     * @param sonicPing The ultrasonic sensor.
     * @param pullBack The distance to pull back.
     */
    public void relayControl(Relay relayName, AnalogChannel sonicPing, double pullBack) {
        
        
        double pulledBack = (sonicPing.getVoltage()/0.0048828125);
        
        if(pulledBack != pullBack){
            relayName.set(Relay.Value.kForward);
        }
        else if(pulledBack == pullBack){
            relayName.set(Relay.Value.kOff);
        }
    }
    
    /**
     * This controls a relay with either axis input or two buttons.
     * When using an axis, forward and back should be the same value.
     * @param relayName The Relay that is being controlled.
     * @param joystickName The joystick for input.
     * @param forward The id for the forward button or one half of an axis.
     * @param back The id for the back button or one half of an axis.
     * @param type Is the input from a button or axis?
     * @exception IllegalArgumentException() If type is invalid. 
     */
    public void relayControl(Relay relayName, Joystick joystickName, int forward, int back, String type) {
        boolean pressedForward = false;
        boolean pressedBack = false;
        
        if(type.equalsIgnoreCase("button")) {
           pressedForward = joystickName.getRawButton(forward);
           pressedBack = joystickName.getRawButton(back);
        }
        else if(type.equalsIgnoreCase("axis")) {
           pressedForward = joystickName.getRawAxis(forward) <= -0.40;
           pressedBack = joystickName.getRawAxis(back) >= 0.40;
        }
        else {
            throw new IllegalArgumentException(type + " is not a valid type of input.");
        }
        
        if(pressedForward && !pressedBack) {
            relayName.set(Relay.Value.kForward);
        }
        else if(!pressedForward && pressedBack) {
            relayName.set(Relay.Value.kReverse);
        }
        else {
            relayName.set(Relay.Value.kOff);
        }
    }
    
    
     /**
     * Relay control with buttons and limit switches.
     * @param relayName The relay under control.
     * @param joystickName The joystick controlling it.
     * @param forward The id for the forward button or one half of an axis.
     * @param back The id for the back button or one half of an axis.
     * @param type Is the input from a button or axis?
     * @param inside The switch at the inside limit.
     * @param outside The switch on the outside limit.
     * @exception IllegalArgumentException If type is invalid.
     */
    public void relayControl(Relay relayName, Joystick joystickName, int forward, 
             int back, String type , DigitalInput inside, DigitalInput outside) {
        boolean pressedForward = false;
        boolean pressedBack = false;
        
        if(type.equalsIgnoreCase("button")) {
           pressedForward = joystickName.getRawButton(forward);
           pressedBack = joystickName.getRawButton(back);
        }
        else if(type.equalsIgnoreCase("axis")) {
           pressedForward = joystickName.getRawAxis(forward) <= -0.40;
           pressedBack = joystickName.getRawAxis(back) >= 0.40;
        }
        else {
            throw new IllegalArgumentException(type + " is not a valid type of input.");
        }
        
        if(pressedForward && !pressedBack && !outside.get()) {
            relayName.set(Relay.Value.kForward);
        }
        else if(pressedForward && !pressedBack && outside.get()) {
            relayName.set(Relay.Value.kOff);
        }
        else if(!pressedForward && pressedBack && !inside.get()) {
            relayName.set(Relay.Value.kReverse);
        }
        else if(!pressedForward && pressedBack && inside.get()) {
            relayName.set(Relay.Value.kOff);
        }
        else {
            relayName.set(Relay.Value.kOff);
        }
    }
   
} 
      




