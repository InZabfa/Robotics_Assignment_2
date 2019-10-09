package Handlers;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import Global.Properties;
import Global.RucksackContainer;
import Interfaces.IRucksack;
import Robot.CellItem;
import Robot.Compass;
import lejos.hardware.ev3.LocalEV3;

public class GridHandler extends IRucksack {
	
	private CellItem[][] Grid;
	
	private ArrayList<int[]> VisitedCells = new ArrayList<int[]>();
	private ArrayList<int[]> InaccessibleCells = new ArrayList<int[]>();
	private ArrayList<int[]> ObjectiveCells = new ArrayList<int[]>();
	
	private int CurrentObjectiveIndex = 0;
	private boolean CanRun = true;
	
	
	public GridHandler(RucksackContainer rcontainer) {
		super(rcontainer);
		
		this.Grid = new CellItem[GetColumns()][GetRows()];
	}
	
	public int GetColumns() { return Properties.Map.COLUMNS; }
	public int GetRows() { return Properties.Map.ROWS; }
	public int GetColumnsMaxIndex() { return GetColumns() - 1; }
	public int GetRowsMaxIndex() { return GetRows() - 1; }
	
	/**
	 * Determines if the current position is at a goal
	 */
	public boolean IsAtGoal() {
		return Arrays.equals(GetCompass().GetXY(), GetObjective());
	}
	
	
	public int NumberOfVisits(int[] xy) {
		int count = 0;
		
		for (int[] x : this.VisitedCells) {
			if (!Arrays.equals(x, xy)) continue;
			else count = count + 1;
		}
		
		return count;
	}
	

	
	/**
	 * Adds objectives to HashSet<int[]>
	 * @param objectives
	 */
	public void AddObjectives(int[][] objectives) {
		for (int[] obj : objectives) 
			this.ObjectiveCells.add(obj);
	}
	
	
	/**
	 * Get the objective cells.
	 * @return
	 */
	public ArrayList<int[]> GetObjectives(){
		return this.ObjectiveCells;
	}
	
	/**
	 * Gets the objective int[x][y]
	 * @return
	 */
	public int[] GetObjective() {
		return GetObjectives().get(CurrentObjectiveIndex);
	}
	
	/**
	 * Method for simulating tests
	 */
	public void CreateTests() {
		AddInaccessibleCell(new int[] {0, 6});
		AddInaccessibleCell(new int[] {4, 2});
		AddInaccessibleCell(new int[] {3, 4});
	}
	
	/**
	 * Add a cell which is believed to be inaccessible.
	 */
	public void AddInaccessibleCell(int[] xy) {
		this.InaccessibleCells.add(xy);
	}
	
	/**
	 * Add a cell which is believed to be inaccessible.
	 */
	public void AddInaccessibleCell(int x, int y) {
		int[] xy = new int[] {x,y};
		this.InaccessibleCells.add(xy);
		
		if (Contains(GetInaccessibleCells(), xy)) {
			this.ObjectiveCells.remove(xy);
		}
	}
	
	
	/**
	 * Iterates through the objective list.
	 */
	public void NextObjective() {
		if (CurrentObjectiveIndex >= (GetObjectives().size() - 1)) {
			CurrentObjectiveIndex = 0;
			FinishedObjectives();
		} else {
			CurrentObjectiveIndex = CurrentObjectiveIndex + 1;
			if (Contains(GetInaccessibleCells(), GetObjective())) {
				NextObjective();
			} else {
				Hashtable<String, Object> obj = new Hashtable<String, Object>();
				obj.put("objective", GetObjective());

				/***
				 * [TO DO]
				 * HERE THE GRID SHOULD BE UPDATED ON THE DISPLAY??
				 * 
				 * 
				 * GetHelper().GetServer().Send(obj);	
				 */
			}
		}
	}
	
	
	/**
	 * Runs when all objectives have been reached.
	 */
	public void FinishedObjectives() {
		this.CanRun = false;
		LocalEV3.get().getAudio().systemSound(3);
	}
	
	public void RemoveInaccessibleCell(int xy[]) {
		try {
			for (int[] x : GetInaccessibleCells()) {
				if (Arrays.equals(xy, x)) GetInaccessibleCells().remove(xy);
			}
		} catch (Exception ex) {
			
		}
	}
	
	
	/**
	 * Determines if can run
	 * @return
	 */
	public boolean CanRun() {
		return this.CanRun;
	}
	
	/**
	 * Picks the cell with the smallest value - taking into account the turns etc.
	 * @return
	 */
	public CellItem PickSurroundingCell(){
		ArrayList<int[]> Cells = new ArrayList<int[]>();
			
		int[] aboveCell = GetCellFromDirection(Compass.SIDE.FORWARD, GetCompass().GetFacingDirection());
		int[] rightCell = GetCellFromDirection(Compass.SIDE.RIGHT, GetCompass().GetFacingDirection());
		int[] leftCell = GetCellFromDirection(Compass.SIDE.LEFT, GetCompass().GetFacingDirection());
		int[] bottomCell = GetCellFromDirection(Compass.SIDE.BEHIND, GetCompass().GetFacingDirection());
				
		int[] f_cell = new int[] {-1,-1};
		
		if (Arrays.equals(aboveCell, f_cell) == false) Cells.add(aboveCell);
		if (Arrays.equals(rightCell, f_cell) == false) Cells.add(rightCell);
		if (Arrays.equals(leftCell, f_cell) == false) Cells.add(leftCell);
		if (Arrays.equals(bottomCell, f_cell) == false) Cells.add(bottomCell);
		
		CellItem minCell = null;
		double maxValue = 100.0;
		
		if (Cells.size() > 0) {
			
			for (int[] x : Cells) {		
				CellItem c = GetCell(x[0],x[1]);
				if ((c.GetValue() < maxValue) && (IsInaccessibleCell(x) == false)) {
					maxValue = c.GetValue();
					minCell = c;
				}
			}
		}
		
		return minCell;
	}	
	
	/**
	 * Resets the grid and all items within the grid
	 * Uses the GetDestination()[x,y] to calculate values spanning.
	 */
	public void ResetGrid() {
		Grid = new CellItem[GetColumns()][GetRows()];
		CreateCells();
		GenerateGrid();
	}
	
	/**
	 * Gets the cells visited as an ArrayList of CellItem
	 * @return
	 */
	public ArrayList<int[]> GetVisitedCells(){ return this.VisitedCells; }
	
	/**
	 * Adds a cell to the list of visited cells.
	 * @param c
	 */
	public void AddVisitedCell(int[] pos) { this.VisitedCells.add(pos); }
	
	
	/**
	 * Returns the cell ahead of the bot based on orientation.3
	 * @return int[]
	 */
	public int[] GetCellInFront() {
		int[] pos = GetCompass().GetXY();
		
		Compass.FACING_DIRECTION facing = GetCompass().GetFacingDirection();
		if (facing == Compass.FACING_DIRECTION.N) return new int[] {pos[0],pos[1]+1};
		if (facing == Compass.FACING_DIRECTION.E) return new int[] {pos[0]+1,pos[1]};
		if (facing == Compass.FACING_DIRECTION.S) return new int[] {pos[0],pos[1]-1};
		if (facing == Compass.FACING_DIRECTION.W) return new int[] {pos[0]-1,pos[1]};
		return pos;
	}
	
	/**
	 * Creates new cells for the grid
	 */
	public void CreateCells(){
		for (int row = 0; row < GetRows(); row++) {
			for (int col = 0; col < GetColumns(); col++) {
				Grid[col][row] = new CellItem(this, col,row, 0);
			}
		}
	}
	
	/**
	 * Calculates if a cell is within the grid
	 * @param x_col
	 * @param y_row
	 * @return
	 */
	public boolean IsInGrid(int x_col, int y_row) {
		return (x_col >= 0 && 
				x_col <= GetColumns() - 1) && 
				(y_row >= 0 && y_row <= GetRows() - 1);
	}
	
	
	/**
	 * Generate the grid by assigning a value for each cell from dest_x and dest_y spanning outwards.
	 * @param dest_x
	 * @param dest_y
	 */
	public void GenerateGrid() {
		int dest_x = GetObjective()[0];
		int dest_y = GetObjective()[1];
		for (int x = 0; x < GetColumns(); x++) {
			
			for (int y = 0; y < GetRows(); y++) {
				
				if (!IsInaccessibleCell(new int[] {x,y})) 
					GetGrid()[x][y].UpdateValue(CalculateCellDifference(x, y));							
				else 
					GetGrid()[x][y].UpdateValue(Properties.Map.INACCESSIBLE_CELL_VALUE);
			}
			
		}
		
		this.VisitedCells.clear();
		
		GetGrid()[dest_x][dest_y].UpdateValue(-100);
	}

	
	/**
	 * Returns inaccessible cells as int[x,y]
	 * @return
	 */
	public ArrayList<int[]> GetInaccessibleCells(){
		return this.InaccessibleCells;
	}
	
	/**
	 * Determines if a cell is inaccessible - e.g. obstruction at location.
	 * @param cell_x_col
	 * @param cell_y_row
	 * @return
	 */
	public boolean IsInaccessibleCell(int[] xy) {
		return Contains(GetInaccessibleCells(), xy);
	}
	
	
	/**
	 * Determines if a collection of int[2] contains the value (b) int[2].
	 * @param a - collection to look
	 * @param b - value to check for.
	 * @return flag of true or false
	 */
	public boolean Contains(AbstractCollection<int[]> a, int[] b) {
		for (int[] x : a)
			if (Arrays.equals(x, b)) return true;
		return false;
	}

	/**
	 * Determines if a cell is on the same row or column.
	 */
	public boolean IsOnSameXOrY(int cell_x_col, int cell_y_row) {
		return (cell_x_col == GetObjective()[0] 
				|| cell_y_row == GetObjective()[1]);
	}
	
	/**
	 * Sets the current objective index to 0.
	 */
	public void SetObjective() {
		this.CurrentObjectiveIndex = 0;
	}
	
	
	/**
	 * Determines if a cell is in the corner of the grid.
	 * @param cell_x_col
	 * @param cell_y_row
	 * @return
	 */
	public boolean IsCellInCorner(int cell_x_col, int cell_y_row) {
		return (cell_x_col == GetColumnsMaxIndex() &&  
				cell_y_row == GetRowsMaxIndex());
	}
	
	/**
	 * Determines if a cell is against a wall.
	 * @param cell_x_col
	 * @param cell_y_row
	 * @return
	 */
	public boolean IsCellAgainstWall(int cell_x_col, int cell_y_row) {
		return ((cell_x_col == GetColumnsMaxIndex()) || 
				(cell_x_col == 0) ||
				cell_y_row == GetRowsMaxIndex() ||
				(cell_y_row == 0));
	}
	
	/**
	 * Using different environmental factors, calculate this cell's value.
	 * @param cell_x_col - X
	 * @param cell_y_row - Y
	 * @param start_value - value for calculations to begin at
	 * @return
	 */
	public int CalculateCellValue(int cell_x_col, int cell_y_row, int start_value) {
		if (!IsInaccessibleCell(new int[] {cell_x_col, cell_y_row})) {
			int starting_value = start_value;
			
			starting_value += NumberOfVisits(new int[] {cell_x_col,cell_y_row});
			
			return starting_value;
		} else {
			return Properties.Map.INACCESSIBLE_CELL_VALUE;
		}
	}
	
	public int CalculateCellDifference(int cell_x_col, int cell_y_row) {
		int[] obj = GetObjective();
		int[] look = new int[] {cell_x_col, cell_y_row};	
		
		int x1 = Math.max(obj[0], look[0]) - Math.min(obj[0], look[0]);
		int x2 = Math.max(obj[1], look[1]) - Math.min(obj[1], look[1]);
		
		int difference = x1+x2;
		return difference;
	}
	
	/**
	 * Evaluates the close cell updating it's value based on movement to it.
	 * @param cell_x_col
	 * @param cell_y_row
	 */
	public void EvaluateCloseCell(int cell_x_col, int cell_y_row) {
		if (IsInGrid(cell_x_col,cell_y_row)){
			if (!Arrays.equals(GetObjective(), new int[] {cell_x_col, cell_y_row})) {
				if (GetGrid()[cell_x_col][cell_y_row] != null) {
					if (IsInaccessibleCell(new int[] {cell_x_col, cell_y_row})) 
						GetCell(cell_x_col, cell_y_row).UpdateValue(Properties.Map.INACCESSIBLE_CELL_VALUE);
				 else {
						int[] look = new int[] {cell_x_col, cell_y_row};	
						int val = CalculateCellDifference(look[0],look[1]) + NumberOfTurnsToCell(cell_x_col, cell_y_row);
						if (IsVisitedCell(look)) val += 1;
						GetCell(cell_x_col, cell_y_row).UpdateValue(val);
					}
				}
			}
		}
	}
	
	/**
	 * Returns if a cell has been visited
	 * @param cellxy
	 * @return
	 */
	public boolean IsVisitedCell(int[] cellxy) { 
		return Contains(GetVisitedCells(), cellxy);
	}
	
	/**
	 * Sets current position as a visited cell
	 */
	public void SetCellVisited() {
		if (IsInGrid(GetCompass().GetXY()[0], GetCompass().GetXY()[1])) {
			AddVisitedCell(GetCompass().GetXY());
		}
	}
	
	/**
	 * Gets a cell from the specified facing direction, left or right;
	 * @param side
	 * @param dir
	 * @return
	 */
	public int[] GetCellFromDirection(Compass.SIDE side, Compass.FACING_DIRECTION dir) {
		int x = GetCompass().GetXY()[0];
		int y = GetCompass().GetXY()[1];
		
		if (dir == Compass.FACING_DIRECTION.N) {
			if (side == Compass.SIDE.LEFT) {
				x = x - 1;
			} else if (side == Compass.SIDE.FORWARD) {
				y = y + 1;
			} else if (side == Compass.SIDE.RIGHT) {
				x = x + 1;
			} else {
				y = y - 1;
			}
		} 
		
		if (dir == Compass.FACING_DIRECTION.E) {
			if (side == Compass.SIDE.LEFT) {
				y = y + 1;
			} else if (side == Compass.SIDE.FORWARD) {
				x = x + 1;
			} else if (side == Compass.SIDE.RIGHT) {
				y = y - 1;
			} else {
				x = x - 1;
			}
		} 
		
		if (dir == Compass.FACING_DIRECTION.S) {
			if (side == Compass.SIDE.LEFT) {
				x = x + 1;
			} else if (side == Compass.SIDE.FORWARD) {
				y = y - 1;
			} else if (side == Compass.SIDE.RIGHT) {
				x = x - 1;
			} else {
				y = y + 1;
			}
		}
		
		if (dir == Compass.FACING_DIRECTION.W) {
			if (side == Compass.SIDE.LEFT) {
				y = y - 1;
			} else if (side == Compass.SIDE.FORWARD) {
				x = x - 1;
			} else if (side == Compass.SIDE.RIGHT) {
				y = y + 1;
			} else {
				x = x + 1;
			}
		}
		
		int[] xy = new int[] {x,y};
		
		if (IsInGrid(x,y)) {
			return xy;
		} else {
			return new int[]{-1,-1};
		}
	}

	/**
	 * Get number of turns to cell from current position
	 * @param x
	 * @param y
	 * @return
	 */
	public int NumberOfTurnsToCell(int x, int y) {
		int score = 0;
		int currentPos_Y = GetCompass().GetXY()[1];
		int currentPos_X = GetCompass().GetXY()[0];
		
		Compass.FACING_DIRECTION dir = GetCompass().GetFacingDirection();
		
		if (dir == Compass.FACING_DIRECTION.N) {
			 if (y < currentPos_Y) score += 2;		 
			 if ((x > currentPos_X) || (x < currentPos_X)) score += 1;
		 }	 
		 if (dir == Compass.FACING_DIRECTION.E) {
			 if ((y > currentPos_Y) || (y < currentPos_Y)) score += 1;		 
			 if (x < currentPos_X) score += 2;
		 }
		 if (dir == Compass.FACING_DIRECTION.S) {
			 if ((x > currentPos_X) || (x < currentPos_X)) score += 1;		 
			 if (y < currentPos_Y) score += 2;
		 }
		 if (dir == Compass.FACING_DIRECTION.W) {
			 if ((y > currentPos_Y) || (y < currentPos_Y)) score += 1;
			 if (x > currentPos_X) score += 2; 
		 }
		return score;
	}
	
	/**
	 * Returns the surrounding cells with the given radius spanning outwards as an HashSet of int[].
	 * @param currentCol_x
	 * @param currentRow_y
	 * @param radius
	 * @return
	 */
	public Set<int[]> GetCellsFromPoint(int currentCol_x, int currentRow_y, int radius){
		return GetCellsFromPoint(currentCol_x, currentRow_y, radius, false);
	}
	
	/**
	 * Returns the surrounding cells with the given radius spanning outwards as an HashSet of int[].
	 * @param currentCol_x
	 * @param currentRow_y
	 * @param radius
	 * @param remove_corners - Ignore corners
	 * @return
	 */
	public Set<int[]> GetCellsFromPoint(int currentCol_x, int currentRow_y, int radius, boolean remove_corners){
		Set<int[]> crossSections = new HashSet<int[]>();

		int[] topLeft = new int[] {currentCol_x - radius, currentRow_y + radius};
		int[] bottomLeft = new int[] {currentCol_x - radius, currentRow_y - radius};
		int[] bottomRight = new int[] {currentCol_x + radius, currentRow_y - radius};
			
		//Left to right
		for (int fromLeft = currentCol_x - radius; fromLeft <= (currentCol_x + radius); fromLeft++) {
			if (IsInGrid(fromLeft, topLeft[1])) crossSections.add(new int[] {fromLeft,topLeft[1]});
			if (IsInGrid(fromLeft, bottomLeft[1])) crossSections.add(new int[] {fromLeft,bottomLeft[1]});
		}

		for (int bottomToTop = currentRow_y - radius; bottomToTop <= (currentRow_y + radius); bottomToTop++) {
			if (IsInGrid(bottomLeft[0], bottomToTop)) crossSections.add(new int[] {bottomLeft[0], bottomToTop});
			if (IsInGrid(bottomLeft[0], bottomToTop)) crossSections.add(new int[] {bottomRight[0],bottomToTop});
		}
		
		return crossSections;
	}
	
	/**
	 * Get the grid 2d array 
	 * @return CellItem[X][Y]
	 */
	public CellItem[][] GetGrid() { return this.Grid; }
	
	/**
	 * Returns a cell from the grid.
	 * @param col_x
	 * @param row_y
	 * @return
	 */
	public CellItem GetCell(int col_x, int row_y) {
		try {
			return Grid[col_x][row_y];
		} catch (Exception ex) {
			return null;
		}
	}
}
