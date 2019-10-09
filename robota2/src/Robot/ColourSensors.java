package Robot;

import Handlers.ColourHandler.Colours;
import Handlers.ColourHandler;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ColourSensors {

	private EV3ColorSensor leftSensor;
	private EV3ColorSensor rightSensor;

	private SampleProvider leftSample;
	private SampleProvider rightSample;

	private float[] leftValues, rightValues;

	private ColourHandler colourHandler;

	public ColourSensors() {
		this.leftSensor = new EV3ColorSensor(LocalEV3.get().getPort("S1"));
		this.rightSensor = new EV3ColorSensor(LocalEV3.get().getPort("S4"));

		this.leftSample = this.leftSensor.getRGBMode();
		this.rightSample = this.rightSensor.getRGBMode();

		this.leftValues = new float[this.leftSample.sampleSize()];
		this.rightValues = new float[this.rightSample.sampleSize()];

		this.rightSample.fetchSample(this.rightValues, 0);
		this.leftSample.fetchSample(this.leftValues, 0);

		this.colourHandler = new ColourHandler(this);
	}

	/**
	 * Gets the colour handler.
	 * @return
	 */
	public ColourHandler GetColourHandler() {
		return this.colourHandler;
	}

	/**
	 * Retrieves the samples from the colour sensors.
	 */
	private void FetchSamples() {
		this.leftSample.fetchSample(leftValues, 0);
		this.rightSample.fetchSample(rightValues, 0);
	}

	/**
	 * Used to retrieve the red value of both sensors
	 * 
	 * @return [0] = left, [1] = right
	 */
	public float[] Red() {
		FetchSamples();
		return new float[] { this.leftValues[0], this.rightValues[0] };
	}

	/**
	 * Gets the average red from both sensors.
	 * 
	 * @return Average red.
	 */
	public float AverageRed() {
		FetchSamples();
		return ((this.leftValues[0] + this.rightValues[0]) / 2);
	}

	/**
	 * Gets the average green from both sensors.
	 * 
	 * @return Average green.
	 */
	public float AverageGreen() {
		FetchSamples();
		return ((this.leftValues[1] + this.rightValues[1]) / 2);
	}

	/**
	 * Gets the average blue from both sensors.
	 * 
	 * @return Average blue.
	 */
	public float AverageBlue() {
		FetchSamples();
		return ((this.leftValues[2] + this.rightValues[2]) / 2);
	}

	/**
	 * Used to retrieve the green value of both sensors
	 * 
	 * @return [0] = left, [1] = right
	 */
	public float[] Green() {
		FetchSamples();
		return new float[] { this.leftValues[1], this.rightValues[1] };
	}

	/**
	 * Used to retrieve the blue value of both sensors
	 * 
	 * @return [0] = left, [1] = right
	 */
	public float[] Blue() {
		FetchSamples();
		return new float[] { this.leftValues[2], this.rightValues[2] };
	}

	/**
	 * Determines if left sensor is on black
	 * 
	 * @return
	 */
	public boolean IsLeftBlack() {
		FetchSamples();
		return GetColourHandler().GetClosestColour(Red()[0], Green()[0], Blue()[0]) == Colours.BLACK;
	}

	/**
	 * Determines if both colour sensors are on black;
	 * 
	 * @return
	 */
	public boolean IsBothBlack() {
		return this.IsLeftBlack() && this.IsRightBlack();
	}

	/**
	 * Determines if either of the colour sensors are on black;
	 * 
	 * @return
	 */
	public boolean IsEitherBlack() {
		return this.IsLeftBlack() || this.IsRightBlack();
	}

	/**
	 * Determines if right sensor is on black;
	 * 
	 * @return
	 */
	public boolean IsRightBlack() {
		FetchSamples();
		return GetColourHandler().GetClosestColour(Red()[1], Green()[1], Blue()[1]) == Colours.BLACK;
	}
}
