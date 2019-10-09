package TaskHandlers;

import Global.RucksackContainer;
import Interfaces.ITaskHandler;

public class CommandHandler extends ITaskHandler {

	public CommandHandler(RucksackContainer ruckcontainer) {
		super(ruckcontainer, "reset");
	}

	public void gyro(String args) {
		//GetSensors().GetGyroscope().SetCurrentPosition();
	}
}
