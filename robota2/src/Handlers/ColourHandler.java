package Handlers;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;

import java.util.HashMap;
import Robot.ColourSensors;

public class ColourHandler {
	public static int NUMBER_OF_READINGS = 500;

	private ColourSensors colourSensors;

	private HashMap<String, float[]> ColourCalibration;

	public HashMap<String, float[]> GetColourCalibrations() {
		return this.ColourCalibration;
	}

	public ColourHandler(ColourSensors colourSensor) {
		this.colourSensors = colourSensor;
		this.ColourCalibration = new HashMap<String, float[]>();
	}

	public ColourSensors GetColourSensors() {
		return this.colourSensors;
	}

	public enum Colours {
		WHITE, CYAN, BLACK, BURGANDY, YELLOW, NONE
	}

	public void PollColours() {
		for (Colours value : Colours.values()) {
			if (value == Colours.NONE)
				continue;
			System.out.println("Place me on <" + value.toString().toUpperCase() + "> and press <ENTER>...");
			Button.ENTER.waitForPress();

			float r = 0, g = 0, b = 0;
			for (int x = 0; x < NUMBER_OF_READINGS; x++) {

				r += GetColourSensors().AverageRed();
				g += GetColourSensors().AverageGreen();
				b += GetColourSensors().AverageBlue();
			}

			float avg_r = (r / NUMBER_OF_READINGS);
			float avg_g = (g / NUMBER_OF_READINGS);
			float avg_b = (b / NUMBER_OF_READINGS);

			GetColourCalibrations().put(value.toString().toLowerCase(), new float[] { avg_r, avg_g, avg_b });
		
			LocalEV3.get().getTextLCD().clear();
		}

		System.out.println("Calibration complete...");
	}

	public Colours GetClosestColour(float r, float g, float b) {

		HashMap<Colours, Double> ClosestColours = new HashMap<Colours, Double>();

		for (Colours val : Colours.values()) {
			if (val == Colours.NONE)
				continue;
			float[] colour = GetColourCalibrations().get(val.toString().toLowerCase());
			double avg = Math
					.sqrt(Math.pow(r - colour[0], 2) + Math.pow(g - colour[1], 2) + Math.pow(b - colour[2], 2));

			ClosestColours.put(val, avg);
		}

		Colours closest = Colours.NONE;
		double avg = Double.MAX_VALUE;
		for (Colours val : Colours.values()) {
			if (val == Colours.NONE)
				continue;
			if (ClosestColours.get(val) < avg) {
				avg = ClosestColours.get(val);
				closest = val;
			}
		}

		return closest;
	}

}
