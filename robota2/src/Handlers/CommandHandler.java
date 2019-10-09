package Handlers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import Global.Extractor;
import Global.RucksackContainer;
import Interfaces.IRucksack;
import Interfaces.ITask;
import Interfaces.Pair;

/**
 * Using the IRucksack Interface to link all robotic elements and sub-elements
 * together. Interface: {@link Interfaces.IRuckack}.
 */
public class CommandHandler extends IRucksack {

	/**
	 * Constructor;
	 * 
	 * @param repos - {@link Handlers.RepositoryHandler} to store/retrieve tasks in
	 *              a queue-like format.
	 */
	public CommandHandler(RucksackContainer rcontainer) {
		super(rcontainer);
	}

	/**
	 * When a command is passed to the command handler, it extracts the command and
	 * the command arguments using {@link Global.Extractor#ExtractCommand(String)}
	 * and {@link Global.Extractor#ExtractArguments(String)}.
	 * 
	 * @param str - command to extract.
	 */
	public void HandleCommand(String str) {
		System.out.println("[cmd#server]: " + str);
		Extractor extractor = new Extractor();
		ITask task = CreateTask(extractor.ExtractCommand(str), extractor.ExtractArguments(str));
		if (task != null)
			GetRepository().GetQueue().push(task);
	}

	/**
	 * Method used to create the ITask with the given parameters.
	 * 
	 * @param cmd  - Command to create.
	 * @param args - Arguments to create task with.
	 * @return ITask with specified input.
	 */
	public ITask CreateTask(String cmd, ArrayList<Pair> args) {
		try {
			String class_name = Global.Properties.Tasks.PACKAGE + "." + cmd;
			Class<?> clazz = Class.forName(class_name);
			Constructor<?> ctor = clazz.getConstructor(ArrayList.class);
			return (ITask) ctor.newInstance(new Object[] { args });
		} catch (Exception ex) {
			return null;
		}
	}
}
