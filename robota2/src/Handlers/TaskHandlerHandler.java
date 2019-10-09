package Handlers;

import java.util.ArrayList;

import Global.RucksackContainer;
import Interfaces.ITaskHandler;
import TaskHandlers.*;

public class TaskHandlerHandler {
	private ArrayList<ITaskHandler> taskHandlers;
	private RucksackContainer ruckcontainer;
	
	public TaskHandlerHandler(RucksackContainer rcontainer) {
		this.ruckcontainer = rcontainer; 
		this.taskHandlers = new ArrayList<ITaskHandler>();
		this.taskHandlers.add(new MovementHandler(this.ruckcontainer));		
		this.taskHandlers.add(new RotateHandler(this.ruckcontainer));	
		this.taskHandlers.add(new TaskHandlers.CommandHandler(this.ruckcontainer));
	}
	
	public ArrayList<ITaskHandler> ObtainHandlers() { return taskHandlers; }
	
}
