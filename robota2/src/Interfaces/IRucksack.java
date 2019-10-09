package Interfaces;

import Global.RucksackContainer;
import Handlers.GridHandler;
import Handlers.RepositoryHandler;
import Handlers.SensorHandler;
import Robot.Compass;
import Robot.Motorbility;

public abstract class IRucksack {

	private RepositoryHandler repository;
	private Compass compass;
	private SensorHandler sensorHandler;
	private GridHandler gridHandler;
	private Motorbility motorbility;

	public IRucksack() {
	}

	public IRucksack(RucksackContainer container) {
		this.repository = container.GetRepository();
		this.compass = container.GetCompass();
		this.sensorHandler = container.GetSensors();
		this.gridHandler = container.GetGridHandler();
		this.motorbility = container.GetMotorbility();
	}

	protected RepositoryHandler GetRepository() {
		return this.repository;
	}

	protected Compass GetCompass() {
		return this.compass;
	}

	protected SensorHandler GetSensors() {
		return this.sensorHandler;
	}

	protected GridHandler GetGridHandler() {
		return this.gridHandler;
	}

	protected Motorbility GetMotorbility() {
		return this.motorbility;
	}
}