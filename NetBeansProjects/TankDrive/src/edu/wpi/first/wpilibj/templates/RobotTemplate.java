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
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends SimpleRobot {
    Victor frontLeft;
    Victor rearLeft;
    Victor frontRight;
    Victor rearRight;
    //Compressor airCompressor;
    Solenoid latchExtend;
    Solenoid latchRetract;
    //Solenoid s3;
    //Solenoid s4;
    Solenoid tensionPull1;
    Solenoid tensionPush1;
    Solenoid tensionPull2;
    Solenoid tensionPush2;
    RobotDrive myDrive;
    Joystick moveStick;
    Joystick shootStick;
    //AirRunnable airRun;
    //Thread airThread;
    UltrasonicApproval approvalRun;
    Thread approvalThread;
    SolenoidClick solenoidControl1;
    Thread solenoidThread1;
    //SolenoidClick solenoidControl2;
    //Thread solenoidThread2;
    SolenoidClick solenoidControl3;
    Thread solenoidThread3;
    SolenoidClick solenoidControl4;
    Thread solenoidThread4;
    Relay pickupRelay1;
    Relay pickupRelay2;
    //DigitalInput raiseSwitch1;
    DigitalInput dummy;
    DigitalInput inside;
    DigitalInput outside;
    //Victor motorOne;
    //Victor motorTwo;
    AnalogChannel sonic1;
    
    
   
    
    
    //This initializes controls and motors
    public void robotInit(){
        frontLeft = new Victor(8);
        rearLeft = new Victor(4);
        frontRight = new Victor(7);
        rearRight = new Victor(9);
        myDrive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
        moveStick = new Joystick(1);
        shootStick = new Joystick(2);
        //airCompressor = new Compressor(1,3);
        latchExtend = new Solenoid(5); //1 little
        latchRetract = new Solenoid(6);
        //s3 = new Solenoid(3); //2 pickup
        //s4 = new Solenoid(4);
        tensionPull1 = new Solenoid(2); //3 pull
        tensionPush1 = new Solenoid(1);
        tensionPull2 = new Solenoid(4); //4 pull
        tensionPush2 = new Solenoid(3);
        //airRun = new AirRunnable(airCompressor);
        //airThread = new Thread(airRun);
        pickupRelay1 = new Relay(4, Relay.Direction.kBoth);
        pickupRelay2 = new Relay(2, Relay.Direction.kBoth);
        dummy = new DigitalInput(10);
        inside = new DigitalInput(2);
        outside = new DigitalInput(3);
        //raiseSwitch1 = new DigitalInput(5);
        sonic1 = new AnalogChannel(1,2);
        approvalRun = new UltrasonicApproval(sonic1, 5000.0);
        approvalThread = new Thread(approvalRun);
        //motorOne = new Victor(5);
        //motorTwo = new Victor(6);
        solenoidControl1 = new SolenoidClick(3,moveStick,latchExtend,latchRetract,"button",dummy);//little
        solenoidControl3 = new SolenoidClick(2,moveStick,tensionPull1,tensionPush1,"button",dummy);//pull
        solenoidControl4 = new SolenoidClick(2,moveStick,tensionPull2,tensionPush2,"button",dummy);//pull
        
        
        
    }
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        myDrive.setSafetyEnabled(false);
        myDrive.tankDrive(1.0, 1.0); 
        Timer.delay(1.0); 
        myDrive.tankDrive(0.0, 0.0);
          
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        latchExtend.set(false);
        latchRetract.set(true);
        //s3.set(false);
        //s4.set(true);
        tensionPull1.set(false);
        tensionPush1.set(true);
        tensionPull2.set(false);
        tensionPush2.set(true);
        //airThread = new Thread(airRun);
        //airThread.start(); // starts automatic compressor switching in parallel
        solenoidThread1 = new Thread(solenoidControl1);
        solenoidThread1.start();
        //solenoidThread2 = new Thread(solenoidControl2);
        //solenoidThread.start();
        solenoidThread3 = new Thread(solenoidControl3);
        solenoidThread3.start();
        solenoidThread4 = new Thread(solenoidControl4);
        solenoidThread4.start();
        //approvalThread = new Thread(approvalRun);
        //approvalThread.start();
        while(isOperatorControl() && isEnabled()) {
            myDrive.setSafetyEnabled(true);
            double leftMovement = buffer(2,moveStick,true,0.10,-0.10);
            double rightMovement = buffer(5,moveStick,true,0.10,-0.10);
            myDrive.tankDrive(leftMovement, rightMovement);
            
            //motorOne.set(buffer(3,moveStick,true,0.10,-0.10));
            //motorTwo.set(buffer(3,moveStick,false,0.10,-0.10));
            relayControl(pickupRelay1,moveStick,3,3,"axis");
            relayControl(pickupRelay2,moveStick,3,3,"axis");
            SmartDashboard.putString("Distance", (sonic1.getVoltage()/0.0048828125)+"cm");
            //SmartDashboard.putBoolean("Switch 1", launcherSwitch1.get());
            //SmartDashboard.putBoolean("Switch 2", launcherSwitch2.get());
            SmartDashboard.putNumber("Trigger data", buffer(3,moveStick,true,0,0));
            //SmartDashboard.putNumber("Motor 1", motorOne.get());
            //SmartDashboard.putNumber("Motor 2", motorTwo.get());
            
            Timer.delay(0.01);
        }
        //airRun.stop(); // stops automatic switching
        solenoidControl1.stop();
        //solenoidControl2.stop();
        solenoidControl3.stop();
        solenoidControl4.stop();
        //approvalRun.stop();
        
    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
    
    }
    
 
    
    /**
     * This function buffers the joystickName.getRawAxis() input.
     * @param axisNum The ID for the axis in moveStick.
     * @param joystickName The Joystick that input is coming from. 
     * @param inverted Is it flipped?
     * @param highMargin The high margin of the buffer.
     * @param lowMargin The low margin of the buffer.
     * @return moveOut - The buffered axis data from joystickName.getRawAxis().
     **/
    public double buffer(int axisNum,Joystick joystickName, boolean inverted, double highMargin, double lowMargin) {
        double moveIn = joystickName.getRawAxis(axisNum);
        double moveOut;
        moveOut = 0.0; 
       
        if(moveIn >= lowMargin && moveIn <= highMargin ) {
         moveOut = 0.0;
        }
        else{
            if(inverted) {
                moveOut = -moveIn;
            }
            else if(!inverted) {
                moveOut = moveIn;
            }
        }
	
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
     * This runs the winch with an AnalogChannel sensor.
     * @param relayName The relay spike.
     * @param sonicPing The ultrasonic sensor.
     * @param pullBack  The distance to pull back.
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

