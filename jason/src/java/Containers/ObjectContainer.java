package Containers;

import java.util.HashMap;

public class ObjectContainer {
	public ObjectContainer() {
		StoredValues = new HashMap<String, String>();
		
		GetStoredValues().put("X", "0");
		GetStoredValues().put("Y", "0");
		
		GetStoredValues().put("direction", "n");
		
		GetStoredValues().put("R", "0");
		GetStoredValues().put("G", "0");
		GetStoredValues().put("B", "0");
		
		GetStoredValues().put("ObjectiveX", "0");
		GetStoredValues().put("ObjectiveY", "0");
		
		GetStoredValues().put("colour", "unknown");
	}

	/**
	 * Stores the bot properties.
	 */
	private HashMap<String, String> StoredValues;

	/**
	 * Returns the stored values
	 * 
	 * @return stored properties.
	 */
	public HashMap<String, String> GetStoredValues() {
		return this.StoredValues;
	}

	/**
	 * Stores the known Y position of the bot.
	 */
	public int Robot_X() {
		return Integer.parseInt(GetStoredValues().get("X"));
	}

	/**
	 * Stores the known X position of the bot.
	 */
	public int Robot_Y() {
		return Integer.parseInt(GetStoredValues().get("Y"));
	}

	/**
	 * Stores the known facing direction of the bot.
	 */
	public FACING Direction() {
		switch (GetStoredValues().get("direction").toLowerCase()) {
		case "n":
			return FACING.N;
		case "e":
			return FACING.E;
		case "s":
			return FACING.S;
		case "w":
			return FACING.W;
		default:
			return FACING.N;
		}
	}

	/**
	 * Enum to store compass directions.
	 */
	public enum FACING {
		N, E, S, W
	}
}
