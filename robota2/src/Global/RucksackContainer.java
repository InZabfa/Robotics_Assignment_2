package Global;

import Handlers.ColourHandler;
import Handlers.GridHandler;
import Handlers.RepositoryHandler;
import Handlers.SensorHandler;
import Robot.Compass;
import Robot.Motorbility;
import Server.Server;

public class RucksackContainer {
	
	private RepositoryHandler repository;
	private SensorHandler sensors;
	private Compass compass;
	private GridHandler grid;
	private Motorbility motorbility;
	private Server server;
	
	public RucksackContainer() {
		this.repository = new RepositoryHandler();
		this.sensors = new SensorHandler();
		this.compass = new Compass();
		this.grid = new GridHandler(this);
		this.motorbility = new Motorbility(this);
	}
	
	public void SetServer(Server server) { this.server = server; }
	
	public Server GetServer() { return this.server; }
	public SensorHandler GetSensors() { return this.sensors; }
	public ColourHandler GetColourHandler() { return this.sensors.GetColourSensors().GetColourHandler(); }
	public RepositoryHandler GetRepository() { return this.repository; }
	public Compass GetCompass() { return this.compass; }
	public GridHandler GetGridHandler() { return this.grid; }
	public Motorbility GetMotorbility() { return this.motorbility; }
	
}
