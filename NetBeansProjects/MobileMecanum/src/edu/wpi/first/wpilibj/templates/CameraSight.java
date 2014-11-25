/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 * @author Noah
 */
public class CameraSight implements Runnable{
    
    private volatile boolean running;
    private final AxisCamera camera;
    private ColorImage image;
    //Camera constants used for distance calculation
    final int Y_IMAGE_RES = 480;		//X Image resolution in pixels, should be 120, 240 or 480
    final double VIEW_ANGLE = 49;		//Axis M1013
    //final double VIEW_ANGLE = 41.7;		//Axis 206 camera
    //final double VIEW_ANGLE = 37.4;           //Axis M1011 camera
    final double PI = 3.141592653;
    
    public CameraSight(){
        camera = AxisCamera.getInstance();
        running = true;
    }
    
    public void run(){
        
        while(running){
            try {
                image = camera.getImage();
                SmartDashboard.putData("Camera", (Sendable) image);
                image.free();
            } catch (AxisCameraException ex) {
                ex.printStackTrace();
            } catch (NIVisionException ex) {
                ex.printStackTrace();
            }
            Timer.delay(0.01);
        }
        
    }
    
    public void stop(){
        running = false;
    }
    
}
