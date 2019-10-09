package Interfaces;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public abstract class ITask {
	/** Used to store task values */
	private Dictionary<String,String> Arguments = new Hashtable<String,String>();
	private ArrayList<Pair> Pair_Arguments;
	
	/**
	 * Used to initialise the task
	 * @param pairs
	 */
	public ITask(ArrayList<Pair> pairs){	
		this.Pair_Arguments = pairs;
		
		for (Pair pair : pairs) {
			this.Arguments.put(pair.Key(),pair.Value());
		}
	}

	/**
	 * Method used to return the task arguments
	 * @return Dictionary<String,String>
	 */
	public Dictionary<String,String> GetArguments() { return this.Arguments; }
	
	public ArrayList<Pair> GetPairArguments() { return this.Pair_Arguments; }
	
	/**
	 * Used to return an argument, if non-existent, returns null;
	 * @param id
	 * @return argument string
	 */
	public String GetArgument(String id) { 
		if (Arguments.get(id) != null) return Arguments.get(id);
		else return null;
	}

	/**
	 * Used to update an argument.
	 * @param key
	 * @param newValue
	 */
	public void UpdateArgument(String key, String newValue){
		GetArguments().remove(key);
		GetArguments().put(key,newValue);
	}
}
