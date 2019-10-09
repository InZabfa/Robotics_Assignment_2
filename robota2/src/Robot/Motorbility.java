package Robot;

import Global.RucksackContainer;
import Interfaces.IRucksack;
import Robot.Compass.TURN_DIRECTION;
import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class Motorbility extends IRucksack {
	private MovePilot pilot;
	private Wheel leftWheel;
	private Wheel rightWheel;

	public Motorbility(RucksackContainer rucksackcontainer) {
		super(rucksackcontainer);
		leftWheel = WheeledChassis.modelWheel(Motor.B, 4.24).offset(-5.55);
		rightWheel = WheeledChassis.modelWheel(Motor.D, 4.24).offset(5.55);
		Chassis chassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
		this.pilot = new MovePilot(chassis);
		this.pilot.setLinearSpeed(20);
		this.pilot.setAngularSpeed(25);
	}

	/**
	 * Gets the left wheel
	 * 
	 * @return
	 */
	public Wheel GetLeftWheel() {
		return this.leftWheel;
	}

	/**
	 * Gets the right wheel
	 * 
	 * @return
	 */
	public Wheel GetRightWheel() {
		return this.rightWheel;
	}

	/**
	 * Method used to adjust the angle of the bot using the Gyro sensor.
	 */
	public void AdjustAngle() {
		float angle = GetSensors().GetGyroscope().GetValue();
		Rotate((Math.round(angle / -90.0) * -90.0) - angle);
	}

	/**
	 * Method to retrieve the MovePilot
	 * 
	 * @return {@link #pilot} property.
	 */
	public MovePilot GetPilot() {
		return this.pilot;
	}

	/**
	 * Move backwards using centimetres - (does not update the grid).
	 * 
	 * @param centimetres
	 * @return
	 */
	public void MoveBack(double centimetres) {
		GetPilot().travel(-centimetres);
		GetCompass().UpdatePosition();
	}

	/**
	 * Moves the bot forward using centimetres
	 * 
	 * @param centimetres - distance to travel in CM
	 */
	public void MoveForward(double centimetres) {
		GetPilot().travel(centimetres);
		AdjustAngle();
		GetCompass().UpdatePosition();
	}

	/**
	 * Move the bot forward using number of cells
	 * 
	 * @param numberOfCells - cells to travel forward
	 */
	public void MoveForwardByCell(int numberOfCells) {
		this.MoveForward(Global.Properties.Map.CELL_SIZE * numberOfCells);
	}

	/**
	 * Rotate the bot using specified angle.
	 * 
	 * @param angle - angle of rotation (positive = right, negative = left)
	 */
	public void Rotate(double angle) {
		GetPilot().rotate(angle);
	}

	/**
	 * Rotate the bot left and automatically update the robot's internal compass.
	 * 
	 * @param angle - angle to rotate left.
	 */
	public void RotateLeft(double angle) {
		Rotate(-angle);
		AdjustAngle();
		GetCompass().UpdateDirection(TURN_DIRECTION.LEFT);
	}

	/**
	 * Rotate the bot right and automatically update the robot's internal compass.
	 * 
	 * @param angle - angle to rotate right.
	 */
	public void RotateRight(double angle) {
		Rotate(angle);
		AdjustAngle();
		GetCompass().UpdateDirection(TURN_DIRECTION.RIGHT);
	}

	/**
	 * Stops the robot's movement.
	 */
	public void StopMovement() {
		GetPilot().stop();
	}
}
