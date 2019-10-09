package PathFinding;

import Automation.PathGenerator.TURN;
import PathFinding.PathFindingContext;
import PathFinding.TileBasedMap;

public class Map implements TileBasedMap {

	/**
	 * Fields
	 */
	private int columns, rows;
	private int[][] Map;

	private int previous_x = 0;
	private int previous_y = 0;
	private String direction = "n";

	/**
	 * Map constructor using columns and rows.
	 * 
	 * @param columns - number of columns
	 * @param rows    - number of rows.
	 */
	public Map(int columns, int rows) {
		this.rows = rows;
		this.columns = columns;
		this.Map = new int[columns][rows];
	}

	/**
	 * Returns number of columns.
	 * 
	 * @return Map columns.
	 */
	public int GetColumns() {
		return this.columns;
	}

	/**
	 * Returns number of rows.
	 * 
	 * @return Map rows.
	 */
	public int GetRows() {
		return this.rows;
	}

	/**
	 * Adds an obstacle at (x,y).
	 * 
	 * @param x - X on Map
	 * @param y - Y on Map
	 */
	public void setObstacle(int x, int y) {
		this.Map[x][y] = 1;
	}

	/**
	 * Gets the count of obstacles.
	 * 
	 * @return count of obstacles
	 */
	public int getObstacles() {
		int count = 0;

		for (int x = 0; x < this.columns; x++) {
			for (int y = 0; y < this.rows; y++) {
				if (this.Map[x][y] == 1)
					count++;
			}
		}

		return count;
	}

	public void setDirection(String dir) {
		this.direction = dir;
	}

	public void setPrevX(int x) {
		this.previous_x = x;
	}

	public void setPrevY(int y) {
		this.previous_y = y;
	}

	public int previousX() {
		return this.previous_x;
	}

	public int previousY() {
		return this.previous_y;
	}

	public String direction() {
		return this.direction;
	}

	/**
	 * Method used to return the cost of a position on the map.
	 */
	@Override
	public float getCost(PathFindingContext arg0, int arg1, int arg2) {
		return 1.0f;
	}

	public enum TURN {
		LEFT, RIGHT, BACK
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
		this.direction = UpdateFacing(this.direction, turn);
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

	/**
	 * Method used to return the Height of map (Rows).
	 */
	@Override
	public int getHeightInTiles() {
		return this.GetRows();
	}

	/**
	 * Method used to return Width of map (Columns).
	 */
	@Override
	public int getWidthInTiles() {
		return this.GetColumns();
	}

	/**
	 * Method is invoked if a path has been visited.
	 */
	@Override
	public void pathFinderVisited(int arg0, int arg1) {
	}

	/**
	 * Returns if a Map cell is blocked.
	 */
	@Override
	public boolean blocked(PathFindingContext context, int tx, int ty) {
		return this.Map[tx][ty] != 0;
	}
}