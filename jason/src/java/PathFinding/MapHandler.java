package PathFinding;

import PathFinding.AStarPathFinder;
import PathFinding.Path;

public class MapHandler {
	/**
	 * Fields.
	 */
	private AStarPathFinder pathFinder;
	private Path path;
	private Map map;

	/**
	 * Adds an obstacle to the map at x,y
	 * 
	 * @param x
	 * @param y
	 */
	public void AddObstacle(int x, int y) {
		map.setObstacle(x, y);
	}

	public int getObstacles() {
		return this.map.getObstacles();
	}

	/**
	 * Initialises the constructor of the MapHandler.
	 * 
	 * @param columns - number of columns on the map.
	 * @param rows    - number of rows on the map.
	 */
	public MapHandler(int columns, int rows) {
		this.map = new Map(columns, rows);
		this.pathFinder = new AStarPathFinder(map, 100, false);
	}

	/**
	 * Returns the calculated path using the start (x,y) to goal (gx,gy)
	 * 
	 * @param sx - Start X
	 * @param sy - Start Y
	 * @param gx - Goal X
	 * @param gy - Goal Y
	 */
	public void SetPath(int sx, int sy, int gx, int gy) {
		this.path = pathFinder.findPath(null, sx, sy, gx, gy);
	}

	public int GetCost(int x, int y, int tx, int ty) {
		return (int) (distance(x,y,tx,ty) + this.pathFinder.getHeuristicCost(null, x, y, tx, ty));
	}

	public double distance(int x, int y, int tx, int ty) {
		double deltaX = y - ty;
		double deltaY = x - tx;
		double result = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		return result;
	}

	public Map GetMap() {
		return this.map;
	}

	/**
	 * Returns the Map generated using {@link #SetPath(int, int, int, int)}.
	 * 
	 * @return {@link Path} object.
	 */
	public Path GetPath() {
		return this.path;
	}

	/**
	 * Returns the length of the path - number of points in the Map's path.
	 * 
	 * @return integer - path length.
	 */
	public int GetPathLength() {
		return this.path.getLength();
	}
}
