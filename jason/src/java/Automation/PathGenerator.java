package Automation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import PathFinding.Path;

import Containers.ObjectContainer;
import PathFinding.*;

public class PathGenerator {
	private MapHandler maphandler;
	private ObjectContainer objContainer;

	private String facing_dir = "n";

	public PathGenerator(ObjectContainer objContainer) {
		this.maphandler = new MapHandler(6, 6);
		this.objContainer = objContainer;
	}

	public ObjectContainer GetObjectContainer() {
		return this.objContainer;
	}

	public Path GetPath(int[] start_xy, int[] goal_xy) {
		maphandler.SetPath(start_xy[0], start_xy[1], goal_xy[0], goal_xy[1]);
		return maphandler.GetPath();
	}

	public int GetCost(int[] start_xy, int[] goal_xy) {
		int cost = maphandler.GetCost(start_xy[0], start_xy[1], goal_xy[0], goal_xy[1]);
		return cost;
	}

	public MapHandler GetMap() {
		return this.maphandler;
	}

	/**
	 * Returns the number of obstructions.
	 * 
	 * @return
	 */
	public int ObstructionCount() {
		return maphandler.getObstacles();
	}

	/**
	 * Adds an obstruction at XY
	 * 
	 * @param x
	 * @param y
	 */
	public void AddObstruction(int x, int y) {
		maphandler.AddObstacle(x, y);
	}

	/**
	 * Stores the turn type.
	 */
	public enum TURN {
		LEFT, RIGHT, BACK
	}

	/**
	 * Generates the instruction string from the given path.
	 * 
	 * @param p - path to generate instruction using
	 * @return string of instructions.
	 */
	public String GenerateInstruction(Path p) {
		StringBuilder builder = new StringBuilder();

		facing_dir = GetObjectContainer().GetStoredValues().get("direction").toLowerCase();

		if (GetObjectContainer().Robot_X() == 0 && GetObjectContainer().Robot_Y() == 0
				&& GetObjectContainer().GetStoredValues().get("direction") == "n")
			builder.append("Move align=1 center=1 ");
		else
			builder.append("Move ");

		for (int i = 1; i < p.getLength(); i++) {
			int x = p.getX(i);
			int y = p.getY(i);

			int p_x = p.getX(i - 1);
			int p_y = p.getY(i - 1);

			if (IsBehind(facing_dir, p_x, p_y, x, y)) {
				facing_dir = UpdateFacing(facing_dir, TURN.BACK);
				builder.append("right=2 align=1 center=1 forward=1 ");
				continue;
			} else if (IsForward(facing_dir, p_x, p_y, x, y)) {
				builder.append("forward=1 ");
				continue;
			} else if (IsLeft(facing_dir, p_x, p_y, x, y)) {
				facing_dir = UpdateFacing(facing_dir, TURN.LEFT);
				builder.append("left=1 align=1 center=1 forward=1 ");
				continue;
			} else if (IsRight(facing_dir, p_x, p_y, x, y)) {
				facing_dir = UpdateFacing(facing_dir, TURN.RIGHT);
				builder.append("right=1 align=1 center=1 forward=1 ");
				continue;
			}
		}

		builder.append("takesample=1 whereareyou=1 ");
		return builder.toString();
	}

	/**
	 * Gets the value of the final command
	 * 
	 * @param str - command string
	 * @return int of command value
	 */
	public int GetLastValueInt(String str) {
		String reg = ".+ ((\\w+)\\=(\\d+))";
		final Pattern pattern = Pattern.compile(reg, Pattern.MULTILINE);
		final Matcher matcher = pattern.matcher(str);
		int x = 0;

		while (matcher.find()) {
			x = Integer.parseInt(matcher.group(3));
		}

		return x;
	}

	/**
	 * Method to replace a group without group occurance limit
	 * 
	 * @param source
	 * @param groupToReplace
	 * @param replacement
	 * @return
	 */
	public String replaceGroup(String source, int groupToReplace, String replacement) {
		return replaceGroup(source, groupToReplace, 1, replacement);
	}

	/**
	 * Used to replace a regex group
	 * 
	 * @param source
	 * @param groupToReplace
	 * @param groupOccurrence
	 * @param replacement
	 * @return
	 */
	public String replaceGroup(String source, int groupToReplace, int groupOccurrence, String replacement) {
		String regex = ".+ ((\\w+)\\=\\d+)";
		Matcher m = Pattern.compile(regex).matcher(source);
		for (int i = 0; i < groupOccurrence; i++)
			if (!m.find())
				return source; // pattern not met, may also throw an exception here
		return new StringBuilder(source).replace(m.start(groupToReplace), m.end(groupToReplace), replacement)
				.toString();
	}

	/**
	 * Checks the previous command string to see if they match the current command.
	 * 
	 * @param val   - current command string
	 * @param s_val - command to check
	 * @return if they match
	 */
	public boolean PreviousCommandMatches(String val, String s_val) {
		String reg = ".+ ((\\w+)\\=\\d+)";

		final Pattern pattern = Pattern.compile(reg, Pattern.MULTILINE);
		final Matcher matcher = pattern.matcher(val);

		boolean found = false;
		while (matcher.find()) {
			if (matcher.group(2).equalsIgnoreCase(s_val)) {
				found = true;
			}
		}

		return found;
	}

	/**
	 * Method to update the facing direction based on a turn value and the current
	 * facing direction.
	 * 
	 * @param dir  - Current facing direction (N,E,S,W)
	 * @param turn - Turn direction (Left,Right,Back)
	 * @return dir - Updated based on turn direction.
	 */
	public String UpdateFacing(String dir, TURN turn) {
		switch (dir) {
		case "n":
			switch (turn) {
			case LEFT:
				return "w";
			case RIGHT:
				return "e";
			case BACK:
				return "s";
			}
		case "e":
			switch (turn) {
			case LEFT:
				return "n";
			case RIGHT:
				return "s";
			case BACK:
				return "w";
			}
		case "w":
			switch (turn) {
			case LEFT:
				return "s";
			case RIGHT:
				return "n";
			case BACK:
				return "e";
			}
		case "s":
			switch (turn) {
			case LEFT:
				return "e";
			case RIGHT:
				return "w";
			case BACK:
				return "n";
			}
		}
		return "n";
	}

	/**
	 * Determines if the cell is to the right of the bot.
	 * 
	 * @param dir - facing direction
	 * @param c_x - current x
	 * @param c_y - current y
	 * @param x   - cell x
	 * @param y   - cell y
	 * @return
	 */
	private boolean IsRight(String dir, int c_x, int c_y, int x, int y) {
		switch (dir) {
		case "n":
			return x > c_x;
		case "e":
			return y < c_y;
		case "s":
			return x < c_x;
		case "w":
			return y > c_y;
		}
		return false;
	}

	/**
	 * Determines if an XY is forward from the Current XY
	 * 
	 * @param dir - facing direction
	 * @param c_x - current x
	 * @param c_y - current y
	 * @param x   - cell x
	 * @param y   - cell y
	 * @return
	 */
	private boolean IsForward(String dir, int c_x, int c_y, int x, int y) {
		switch (dir) {
		case "n":
			return y > c_y;
		case "e":
			return x > c_x;
		case "s":
			return y < c_y;
		case "w":
			return x < c_x;
		}
		return false;
	}

	/**
	 * Determines if an XY is left from the Current XY
	 * 
	 * @param dir - facing direction
	 * @param c_x - current x
	 * @param c_y - current y
	 * @param x   - cell x
	 * @param y   - cell y
	 * @return
	 */
	private boolean IsLeft(String dir, int c_x, int c_y, int x, int y) {
		switch (dir) {
		case "n":
			return x < c_x;
		case "e":
			return y > c_y;
		case "s":
			return x > c_x;
		case "w":
			return y < c_y;
		}
		return false;
	}

	/**
	 * Determines if an XY is behind from the Current XY
	 * 
	 * @param dir - facing direction
	 * @param c_x - current x
	 * @param c_y - current y
	 * @param x   - cell x
	 * @param y   - cell y
	 * @return
	 */
	private boolean IsBehind(String dir, int c_x, int c_y, int x, int y) {
		switch (dir) {
		case "n":
			return y < c_y;
		case "e":
			return x < c_x;
		case "s":
			return y > c_y;
		case "w":
			return x > c_x;
		}
		return false;
	}
}
