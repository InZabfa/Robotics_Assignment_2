package Robot;

import java.util.ArrayList;

import Handlers.GridHandler;

public class CellItem {
	private double occupancy_value = 0.5;
	private double value = 0;
	
	private boolean is_obstruction = false;
	
	private int x = 0;
	private int y = 0;
	
	private GridHandler gHandler;
		
	public CellItem(GridHandler handler, int x_col, int y_row) { 
		this.x = x_col;
		this.y = y_row;		
		this.gHandler = handler;
	}		
		
	public CellItem(GridHandler gHandler, int x_col, int y_row, int val) {
		 this(gHandler, x_col, y_row);
		 this.value = val;
	}
	
	public int[] GetXY() { return new int[] {x,y}; }
	public int GetX() { return this.x; }
	public int GetY() { return this.y; }
	
	public GridHandler GetGridHandler() { return this.gHandler; }
	public void UpdateValue(double val) { this.value = val; }
	public void AddToValue(double value) { this.value += value; }
	public double GetValue() { return this.value; }
	public void UpdateObstruction(boolean val) { this.is_obstruction = val; }
	public void UpdateOccupancyValue(double val) { this.occupancy_value = val; }
	public double GetOccupancyValue() { return this.occupancy_value; }
	public boolean GetObstruction() { return this.is_obstruction; }

	public ArrayList<CellItem> getNeighbors() {
		int current_pos_x = this.x, current_pos_y = this.y;
		
		CellItem aboveCell = GetGridHandler().GetCell(current_pos_x, current_pos_y + 1);
		CellItem rightCell = GetGridHandler().GetCell(current_pos_x + 1, current_pos_y);
		CellItem leftCell = GetGridHandler().GetCell(current_pos_x - 1, current_pos_y);
		CellItem bottomCell = GetGridHandler().GetCell(current_pos_x, current_pos_y - 1);
		
		ArrayList<CellItem> neighbors = new ArrayList<CellItem>();
		
		if (aboveCell != null) neighbors.add(aboveCell);
		if (rightCell != null) neighbors.add(rightCell);
		if (leftCell != null) neighbors.add(leftCell);
		if (bottomCell != null) neighbors.add(bottomCell);
		
		return neighbors;
	}
}