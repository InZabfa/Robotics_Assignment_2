package Robot;

import java.util.Arrays;

public class Compass {
	public enum FACING_DIRECTION {
		N, E, S, W
	}

	public enum TURN_DIRECTION {
		LEFT, RIGHT
	}

	public enum SIDE {
		LEFT, RIGHT, FORWARD, BEHIND
	}

	private FACING_DIRECTION Direction = FACING_DIRECTION.N;
	private FACING_DIRECTION Previous_Direction = FACING_DIRECTION.N;

	private int[] XYPosition = new int[] { 0, 0 };
	private int[] Previous_XYPosition = new int[] { 0, 0 };

	/**
	 * Returns if XY has changed since updating compass position.
	 * 
	 * @return if xy has changed
	 */
	public boolean HasXYChanged() {
		return !Arrays.equals(this.Previous_XYPosition, this.XYPosition);
	}

	/**
	 * Returns if facing direction has changed since updating facing direction.
	 * 
	 * @return if direction has changed
	 */
	public boolean HasDirectionChanged() {
		return (this.Direction != this.Previous_Direction);
	}

	/**
	 * Used to update the previous XY property to current XY value.
	 */
	public void UpdatePreviousXY() {
		this.Previous_XYPosition = new int[] { this.XYPosition[0], this.XYPosition[1] };
	}

	/**
	 * Used to update the current direction of the robot.
	 */
	public void UpdatePreviousDirection() {
		this.Previous_Direction = this.Direction;
	}

	/**
	 * Using current facing direction, update direction based on turn.
	 * 
	 * @param turn
	 */
	public void UpdateDirection(TURN_DIRECTION turn) {
		this.UpdatePreviousDirection();

		switch (GetFacingDirection()) {
		case N:
			if (turn == TURN_DIRECTION.LEFT)
				this.Direction = FACING_DIRECTION.W;
			else
				this.Direction = FACING_DIRECTION.E;
			break;
		case E:
			if (turn == TURN_DIRECTION.LEFT)
				this.Direction = FACING_DIRECTION.N;
			else
				this.Direction = FACING_DIRECTION.S;
			break;
		case S:
			if (turn == TURN_DIRECTION.LEFT)
				this.Direction = FACING_DIRECTION.E;
			else
				this.Direction = FACING_DIRECTION.W;
			break;
		case W:
			if (turn == TURN_DIRECTION.LEFT)
				this.Direction = FACING_DIRECTION.S;
			else
				this.Direction = FACING_DIRECTION.N;
			break;
		}
	}

	/**
	 * Used to set XY to values of x,y
	 * 
	 * @param x - new X
	 * @param y - new Y
	 */
	public void SetXY(int x, int y) {
		this.Previous_XYPosition[0] = this.XYPosition[0];
		this.Previous_XYPosition[1] = this.XYPosition[1];

		this.XYPosition[0] = x;
		this.XYPosition[1] = y;
	}

	/**
	 * The Facing Direction of the robot.
	 * 
	 * @return The robot's facing direction
	 */
	public FACING_DIRECTION GetFacingDirection() {
		return this.Direction;
	}

	/**
	 * Get the XY position of the robot.
	 * 
	 * @return x,y
	 */
	public int[] GetXY() {
		return this.XYPosition;
	}

	/**
	 * Updates the current position with movement and position. X cols - Y rows
	 */
	public void UpdatePosition() {
		int y = this.GetXY()[1], x = this.GetXY()[0];
		switch (this.GetFacingDirection()) {
		case N:
			this.SetXY(x, y + 1);
			break;
		case E:
			this.SetXY(x + 1, y);
			break;
		case S:
			this.SetXY(x, y - 1);
			break;
		case W:
			this.SetXY(x - 1, y);
			break;
		}
		System.out.println("XY: (" + GetXY()[0] + "," + GetXY()[1] + ")");
	}
}
