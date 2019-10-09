package Robot;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;

public class Gyroscope {
	private EV3GyroSensor Gyro;
	private SampleProvider GyroValues;

	private float[] Values;

	public Gyroscope() {
		this.Gyro = new EV3GyroSensor(LocalEV3.get().getPort("S2"));
		this.GyroValues = this.Gyro.getAngleMode();
		this.Values = new float[this.Gyro.getAngleMode().sampleSize()];

		//SetCurrentPosition();
	}

	public void SetCurrentPosition() {
		this.Gyro.reset();
	}

	public float GetValue() {
		this.GyroValues.fetchSample(Values, 0);
		return Values[0];
	}
}
