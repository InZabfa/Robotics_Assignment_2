package Handlers;

import Robot.Gyroscope;
import Robot.Infrared;
import Robot.ColourSensors;

public class SensorHandler {

	private Gyroscope Gyroscope;
	private Infrared Infrared;
	private ColourSensors ColourSensors;

	public SensorHandler() {
		this.Infrared = new Infrared();
		this.Gyroscope = new Gyroscope();
		this.ColourSensors = new ColourSensors();
	}

	/**
	 * Gets the gyro sensor object
	 * 
	 * @return gyro sensor object
	 */
	public Gyroscope GetGyroscope() {
		return this.Gyroscope;
	}

	/**
	 * Gets the infrared sensor object
	 * 
	 * @return infrared sensor object
	 */
	public Infrared GetInfrared() {
		return this.Infrared;
	}

	/**
	 * Gets the colour sensor object
	 * 
	 * @return colour sensor object
	 */
	public ColourSensors GetColourSensors() {
		return this.ColourSensors;
	}
}
