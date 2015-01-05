package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public class DoubleSolenoidPlus extends DoubleSolenoid{

	private final int forwardChannel, reverseChannel;
	private Solenoid forwardSolenoid, reverseSolenoid;
	
	/**
	 * Constructor. 
	 * @param forwardChannel The module number of the solenoid module to use.
	 * @param reverseChannel The forward channel on the module to control.
	 */
	public DoubleSolenoidPlus(int forwardChannel, int reverseChannel) {
		super(forwardChannel, reverseChannel);
		this.forwardChannel = forwardChannel;
		this.reverseChannel = reverseChannel;
	}
	
	/**
	 * Constructor.
	 * @param moduleNumber The module number of the solenoid module to use.
	 * @param forwardChannel The forward channel on the module to control.
	 * @param reverseChannel The forward channel on the module to control.
	 */
	public DoubleSolenoidPlus(int moduleNumber, int forwardChannel,
			int reverseChannel) {
		super(moduleNumber, forwardChannel, reverseChannel);
		this.forwardChannel = forwardChannel;
		this.reverseChannel = reverseChannel;
	}

	/**
	 * @return The value of the forward channel.
	 */
	public int getForwardChannel(){
		return forwardChannel;
	}
	
	/**
	 * @return The value of the reverse channel.
	 */
	public int getReverseChannel(){
		return reverseChannel;
	}
	
	/**
	 * Creates an individual instance of the forward solenoid if null and returns it.
	 * @return An instance of the forward solenoid.
	 */
	public Solenoid getForwardSolenoid(){
		if(forwardSolenoid == null){
			forwardSolenoid = new Solenoid(forwardChannel);
		}
		return forwardSolenoid;
	}
	
	/**
	 * Creates an individual instance of the reverse solenoid if null and returns it.
	 * @return An instance of the reverse solenoid.
	 */
	public Solenoid getReverseSolenoid(){
		if(reverseSolenoid == null){
			reverseSolenoid = new Solenoid(reverseChannel);
		}
		return reverseSolenoid;
	}
	
}
