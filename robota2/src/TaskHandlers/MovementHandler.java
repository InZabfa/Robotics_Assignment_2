package TaskHandlers;

import Global.RucksackContainer;
import Handlers.ColourHandler.Colours;
import Interfaces.ITaskHandler;

public class MovementHandler extends ITaskHandler {

	public MovementHandler(RucksackContainer rcontainer) {
		super(rcontainer, "move");
	}

	public void left(String arg) {
		try {
			int times = Integer.parseInt(arg);

			for (int i = 0; i < times; i++) {
				GetMotorbility().RotateLeft(90);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void facenorth(String arg) {
		try {
			switch (GetCompass().GetFacingDirection()) {
			case N:
				break;
			case E:
				GetMotorbility().RotateLeft(90);
				break;
			case S:
				GetMotorbility().RotateLeft(90);
				GetMotorbility().RotateLeft(90);
				break;
			case W:
				GetMotorbility().RotateRight(90);
				break;
			}
		} catch (Exception ex) {
		}
	}

	public void right(String arg) {
		try {
			int times = Integer.parseInt(arg);

			for (int i = 0; i < times; i++) {
				GetMotorbility().RotateRight(90);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void forward(String arg) {
		try {
			GetMotorbility().GetPilot().travel(0);
			int cells = Integer.parseInt(arg);

			for (int i = 0; i < cells; i++) {
				GetMotorbility().MoveForwardByCell(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void back(String arg) {
		try {
			int times = Integer.parseInt(arg);

			for (int i = 0; i < times; i++) {
				GetMotorbility().RotateLeft(90);
				GetMotorbility().RotateLeft(90);
				GetMotorbility().MoveForwardByCell(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void center(String arg) {
		GetMotorbility().GetPilot().travel(-7);
	}

	public void align(String colour) {
		try {
			GetMotorbility().GetPilot().travel(0);
			
			double speed = GetMotorbility().GetPilot().getLinearSpeed();
			double a_speed = GetMotorbility().GetPilot().getAngularSpeed();
			
			GetMotorbility().GetPilot().setLinearSpeed(3);
			GetMotorbility().GetPilot().setAngularSpeed(10);
			

			while (!GetSensors().GetColourSensors().IsEitherBlack()) {
				if (!GetMotorbility().GetPilot().isMoving())
					GetMotorbility().GetPilot().forward();
			}

			GetMotorbility().GetPilot().stop();
			GetMotorbility().GetPilot().travel(0);

			while (!GetSensors().GetColourSensors().IsBothBlack()) {
				while (GetSensors().GetColourSensors().IsLeftBlack()
						&& !GetSensors().GetColourSensors().IsRightBlack()) {
					if (!GetMotorbility().GetRightWheel().getMotor().isMoving())
						GetMotorbility().GetRightWheel().getMotor().forward();
				}
				GetMotorbility().GetRightWheel().getMotor().stop();

				while (GetSensors().GetColourSensors().IsRightBlack()
						&& !GetSensors().GetColourSensors().IsLeftBlack()) {
					if (!GetMotorbility().GetLeftWheel().getMotor().isMoving())
						GetMotorbility().GetLeftWheel().getMotor().forward();
				}
				GetMotorbility().GetLeftWheel().getMotor().stop();
			}
			
			GetMotorbility().GetPilot().setLinearSpeed(speed);
			GetMotorbility().GetPilot().setAngularSpeed(a_speed);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		GetSensors().GetGyroscope().SetCurrentPosition();
	}

	public void whereareyou(String args) {
		System.out.println("X: " + GetCompass().GetXY()[0] + ", Y: " + GetCompass().GetXY()[1]);
		GetRucksackContainer().GetServer().GetOutput().println("update X " + GetCompass().GetXY()[0]);
		GetRucksackContainer().GetServer().GetOutput().println("update Y " + GetCompass().GetXY()[1]);
		GetRucksackContainer().GetServer().GetOutput()
				.println("update direction " + GetCompass().GetFacingDirection().toString());
	}

	public void takesample(String args) {
		try {
			float red = GetRucksackContainer().GetSensors().GetColourSensors().AverageRed();
			float blue = GetRucksackContainer().GetSensors().GetColourSensors().AverageBlue();
			float green = GetRucksackContainer().GetSensors().GetColourSensors().AverageGreen();

			Colours color = GetRucksackContainer().GetSensors().GetColourSensors().GetColourHandler()
					.GetClosestColour(red, green, blue);
			GetRucksackContainer().GetServer().GetOutput().println("update colour " + color.toString().toLowerCase());
			System.out.println("Sample of colour is " + color.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
