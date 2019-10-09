package Handlers;

import java.util.LinkedList;
import Interfaces.ITask;

public class RepositoryHandler {
	private LinkedList<ITask> Queue;

	public RepositoryHandler() {
		this.Queue = new LinkedList<ITask>();
	}

	/**
	 * Method used to retrieve the task queue.
	 * 
	 * @return LinkedList<ITask> - Task Queue
	 */
	public LinkedList<ITask> GetQueue() {
		return this.Queue;
	}
}