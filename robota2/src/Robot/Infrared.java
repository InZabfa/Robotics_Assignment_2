package Robot;

import Global.Properties;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.RegulatedMotor;

public class Infrared  {

	private EV3IRSensor infrared;
	private RegulatedMotor head;
	private float[] Values;
	private int HeadAngle = 0;

	public Infrared() {
		this.infrared = new EV3IRSensor(LocalEV3.get().getPort("S3"));
		this.head = Motor.C;
		this.head.setSpeed(300);
		this.Values = new float[infrared.getDistanceMode().sampleSize()];
	}

	/**
	 * Used to rotate the infrared motor left.
	 */
	public void RotateLeft() {
		this.RotateForward();
		this.HeadAngle = 90;
		this.head.rotate(HeadAngle);
	}

	/**
	 * Used to rotate the infrared motor right.
	 */
	public void RotateRight() {
		this.RotateForward();
		this.HeadAngle = -90;
		this.head.rotate(-90);
	}

	/**
	 * Used to rotate the infrared motor forward.
	 */
	public void RotateForward() {
		if (this.HeadAngle == 90) {
			this.head.rotate(-90);
			this.HeadAngle = 0;
		} else if (this.HeadAngle == -90) {
			this.head.rotate(90);
			this.HeadAngle = 0;
		}
	}

	/**
	 * Get the values from the Infrared sensor.
	 * 
	 * @return
	 */
	public float[] GetValues() {
		this.infrared.fetchSample(Values, 0);
		return Values;
	}

	/**
	 * Gets if an obstruction is ahead.
	 * 
	 * @return
	 */
	public boolean ObstructionAhead() {
		return (this.GetObstructionDistance() <= Properties.Map.OBSTRUCTION_DISTANCE);
	}

	/**
	 * Gets the distance of an obstruction ahead
	 * 
	 * @return
	 */
	public float GetObstructionDistance() {
		return this.GetValues()[0];
	}

}
