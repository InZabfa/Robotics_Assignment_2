package TaskHandlers;

import Global.RucksackContainer;
import Interfaces.ITaskHandler;

public class RotateHandler extends ITaskHandler {

	public RotateHandler(RucksackContainer ruckcontainer) {
		super(ruckcontainer, "rotate");
	}

	public void left(String args) {
		try {
			int count = Integer.parseInt(args);
			for(int x = 0; x < count; x++) GetMotorbility().RotateLeft(90);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void angle(String angle) {
		try {
			double _angle = Double.parseDouble(angle);
			GetMotorbility().Rotate(_angle);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void fix(String args) {
		try {
			GetMotorbility().AdjustAngle();
		} catch (Exception ex) { }
	}
	
	public void right(String args) {
		try {
			int count = Integer.parseInt(args);
			for(int x = 0; x < count; x++) GetMotorbility().RotateRight(90);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
