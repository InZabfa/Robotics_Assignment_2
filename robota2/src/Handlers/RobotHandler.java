package Handlers;

import java.util.ArrayList;

import Global.Properties;
import Global.RucksackContainer;
import Interfaces.ITask;
import Interfaces.ITaskHandler;
import Server.ServerThread;
import lejos.hardware.Button;

public class RobotHandler {

	/**
	 * Method to handle the clockwork of the robot.
	 * 
	 * @throws Exception
	 */
	public void Start() {
		try {
			RucksackContainer rContainer = new RucksackContainer();

			rContainer.GetSensors().GetColourSensors().GetColourHandler().PollColours();
			System.out.println("Place me on start then press <ENTER>...");
			Button.ENTER.waitForPress();
			rContainer.GetSensors().GetGyroscope().SetCurrentPosition();

			ServerThread serverHandlingThread = new ServerThread(rContainer);
			serverHandlingThread.start();
			ArrayList<ITaskHandler> TaskHandlers = new TaskHandlerHandler(rContainer).ObtainHandlers();

			while (Properties.Generic.ROBOT_RUNNING && Button.ESCAPE.isUp()) {
				if (!rContainer.GetRepository().GetQueue().isEmpty()) {
					ITask task = rContainer.GetRepository().GetQueue().pop();
					for (ITaskHandler thandler : TaskHandlers) {
						if (thandler.AcceptsTask(task)) {
							thandler.HandleTask(task);
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}