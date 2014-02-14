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
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



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
    
    Compressor airCompressor;
    Solenoid s1;
    Solenoid s2;
    Solenoid s3;
    Solenoid s4;
    RobotDrive myDrive;
    Joystick moveStick;
    AirRunnable airRun;
    Thread airThread;
    SolenoidClick launcherRun1;
    Thread launcherThread1;
    SolenoidClick launcherRun2;
    Thread launcherThread2;
    Relay launcherRelay;
    DigitalInput launcherSwitch;
    Victor motorOne;
    Victor motorTwo;
    //Ultrasonic sonic1;    
    
    


//This initializes the motors and controls.
    public void robotInit() {
        myDrive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
        moveStick = new Joystick(1);
        airCompressor = new Compressor(1,1);
        s1 = new Solenoid(1);
        s2 = new Solenoid(2);
        s3 = new Solenoid(5);
        s4 = new Solenoid(4);     
        airRun = new AirRunnable(airCompressor);
        airThread = new Thread(airRun);
        launcherRun1 = new SolenoidClick(1,moveStick,s1,s2);
        launcherThread1 = new Thread(launcherRun1);
        launcherRun2 = new SolenoidClick(1,moveStick,s3,s4);
        launcherThread2 = new Thread(launcherRun2);
        launcherRelay = new Relay(4);
        launcherSwitch = new DigitalInput(5);
        //sonic1 = new Ultrasonic(1,1);
        motorOne = new Victor(5);
        motorTwo = new Victor(6);
        myDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        myDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
    }
    
     //This function is called once each time the robot enters autonomous mode.
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
        
    
    }

    
    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        
        //s1.set(false);
        //s2.set(true);
        s3.set(true);
        s4.set(false);
        airThread.start(); // starts automatic compressor switching in parallel
        //launcherThread2.start();
        launcherThread2.start();
        while (isOperatorControl() && isEnabled()) {
           myDrive.setSafetyEnabled(true);
           double xMovement = buffer(1,moveStick,true,0.18,-0.18);
           double yMovement = buffer(2,moveStick,true,0.18,-0.18);
           double twist = buffer(4,moveStick,true,0.18,-0.18);
           //myDrive.mecanumDrive_Cartesian(xMovement, yMovement, twist, 0.0);
           relayControl(launcherRelay, launcherSwitch);
           motorOne.set(buffer(3,moveStick,false,1.0,-0.18));
           motorTwo.set(buffer(3,moveStick,true,1.0,-0.18));
           //solenoidToggle(1,2,moveStick,s1,s2);
           //solenoidToggle(3,4,moveStick,s3,s4);
           
          
           Timer.delay(0.01);
        }
        airRun.stop(); // stops automatic switching.
        //launcherRun1.stop();
        launcherRun2.stop();
        
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
            if(inverted){
                moveOut = -moveIn;
            }
            if(!inverted){ 
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
    /**
     * This function controls operation of a relay with a switch.
     * @param relayName The Relay object.
     * @param switchName The switch for input.
     */
    
    public void relayControl(Relay relayName, DigitalInput switchName ){
        
        if(!switchName.get()) {
            relayName.set(Relay.Value.kForward);
        }
        if(switchName.get()) {
            relayName.set(Relay.Value.kOff);
        }
    }
    
    /**
     * This runs the winch with an Ultrasonic senor.
     * @param relayName The relay spike.
     * @param sonicPing The ultrasonic sensor.
     * @param pullBack The distance to pull back.
     */
    public void relayControl(Relay relayName, Ultrasonic sonicPing, double pullBack) {
        
        sonicPing.setAutomaticMode(true);
        double pulledBack = sonicPing.getRangeMM();
        
        if(pulledBack != pullBack){
            relayName.set(Relay.Value.kForward);
        }
        if(pulledBack == pullBack){
            relayName.set(Relay.Value.kOff);
        }
    }
    
    /**
     * Tells you if you're ready to fire.
     * @param sensor The Ultrasonic sensor.
     * @param wantedDistance The distance to fire from.
     */
    public void ultraShooting(Ultrasonic sensor, double wantedDistance) {
        
        sensor.setAutomaticMode(true);
        
        
        double distanceAway = sensor.getRangeMM();
        
        if(wantedDistance == distanceAway) {
            SmartDashboard.putString("Ready to fire?", "Yeah, fire that ball!");
        }
        else {
            SmartDashboard.putString("Ready to fire?","Nope.");
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
      




