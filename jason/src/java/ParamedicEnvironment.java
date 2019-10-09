
import jason.asSyntax.*;
import jason.environment.*;
import jason.stdlib.abolish;

import java.util.logging.*;

import PathFinding.Path;

public class ParamedicEnvironment extends Environment {

	private Logger logger = Logger.getLogger("jason" + ParamedicEnvironment.class.getName());

	private ServerHandler server;

	private DisplayMap dm;

	private int obstacles = 0;
	private int victims = 0;
	private int nom = 0;

	public boolean cango = false;

	// Called before the MAS execution with the args informed in .mas2j
	@Override
	public void init(String[] args) {
		super.init(args);
		server = new ServerHandler(this);
		server.start();
		addPercept(Literal.parseLiteral("atHospital"));
		dm = new DisplayMap();
		dm.initDisplay();
		dm.mapGUI();
	}
	// store victims and obstacles in the array to make sure that they can be used:
	// as goals where the victims are
	// as obstacles for the map and also the path finding

	@Override
	public boolean executeAction(String agName, Structure action) {
		try {

			int robot_x = server.GetObjectContainer().Robot_X();
			int robot_y = server.GetObjectContainer().Robot_Y();
			int obj_x = GetInt("ObjectiveX");
			int obj_y = GetInt("ObjectiveY");

			String colour = server.GetObjectContainer().GetStoredValues().get("colour");
			String dir = server.GetObjectContainer().GetStoredValues().get("direction");

			try {
				dm.setLblCurrentCol(colour.toUpperCase());
				dm.setLblTarget(obj_x, obj_y);
				dm.setLblCurrentPos("(" + robot_x + "," + robot_y + ")");
				dm.setLblOrientation(dir.toUpperCase());
				dm.updateGrid();
				dm.getCell(robot_x, robot_y).setRobotAtCell(true);
				dm.getCell(obj_x, obj_y).setObjective(true);
				dm.getCell(robot_x, robot_y).setDirection(dir.toLowerCase());
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			String act = action.getFunctor();

			if (act.equalsIgnoreCase("addvictim")) {
				int x = (int) ((NumberTerm) action.getTerm(0)).solve();
				int y = (int) ((NumberTerm) action.getTerm(1)).solve();
				dm.setType(x, y, "victim");
			} else if (act.equalsIgnoreCase("addobstacle")) {
				int x = (int) ((NumberTerm) action.getTerm(0)).solve();
				int y = (int) ((NumberTerm) action.getTerm(1)).solve();
				server.AddObstacle(x, y);

				obstacles += 1;
				dm.setLblObstacle(String.valueOf(obstacles));
				dm.setType(x, y, "obstacle");
				dm.updateGrid();
			} else if (act.equalsIgnoreCase("savednoncritical")) {
				int x = (int) ((NumberTerm) action.getTerm(0)).solve();
				int y = (int) ((NumberTerm) action.getTerm(1)).solve();
				dm.setType(x, y, "savednoncritical");
				victims++;
				dm.setLblVictim("N/A");
			} else if (act.equalsIgnoreCase("savedcritical")) {
				int x = (int) ((NumberTerm) action.getTerm(0)).solve();
				int y = (int) ((NumberTerm) action.getTerm(1)).solve();
				dm.setType(x, y, "savedcritical");
				victims++;
				dm.setLblVictim("N/A");
			} else if (act.equalsIgnoreCase("msg")) {
				try {
					StringBuilder b = new StringBuilder();
					for (int i = 1; i < action.getTerms().size(); i++) {
						b.append(action.getTerm(i).toString());
					}
					dm.appendStats(action.getTerm(0).toString(), b.toString(), false);
				} catch (Exception ex) {
				}
			} else if (act.equalsIgnoreCase("gotcolour")) {
				try {
					int x = (int) ((NumberTerm) action.getTerm(0)).solve();
					int y = (int) ((NumberTerm) action.getTerm(1)).solve();
					String clr = action.getTerm(2).toString();
					if (clr.equalsIgnoreCase("burgandy")) {
						dm.setLblVictim("Critical");
						dm.setType(x, y, "critical");
					} else if (clr.equalsIgnoreCase("cyan")) {
						dm.setLblVictim("Non-Critical");
						dm.setType(x, y, "nonCritical");
					} else {
						dm.setLblVictim("None");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (act.equalsIgnoreCase("requestcolour")) {
				if (colour.equalsIgnoreCase("burgandy") || colour.equalsIgnoreCase("cyan")) {
					addPercept(Literal.parseLiteral("hasColour(" + robot_x + "," + robot_y + "," + colour + ")"));
					PrintMessage(action, "Colour is " + colour);
				} else {
					addPercept(Literal.parseLiteral("locationWithNoColour(" + robot_x + "," + robot_y + ")"));
					addPercept(Literal.parseLiteral("skipColourCheck"));
				}
			} else if (act.equalsIgnoreCase("remove")) {
				int x = (int) ((NumberTerm) action.getTerm(0)).solve();
				int y = (int) ((NumberTerm) action.getTerm(1)).solve();
				dm.setType(x, y, "empty");
			} else if (act.equalsIgnoreCase("update")) {
				if (robot_x == 0 && robot_y == 0)
					if (containsPercept(Literal.parseLiteral("nonCriticalCount"))) {
						removePercept(Literal.parseLiteral("atGoal"));
						removePercept(Literal.parseLiteral("atHospital"));
						removePercept(Literal.parseLiteral("inTransit"));
						PrintMessage(action, "Removing all percepts because nonCriticalCount(0).");
					} else
						addPercept(Literal.parseLiteral("atHospital"));
				else {
					removePercept(Literal.parseLiteral("atHospital"));
				}
				if (obj_y == robot_y && obj_x == robot_x) {
					addPercept(Literal.parseLiteral("atGoal"));
					removePercept(Literal.parseLiteral("inTransit"));
				}
			} else if (act.equalsIgnoreCase("addhospital")) {
				int hx = (int) ((NumberTerm) action.getTerm(0)).solve();
				int hy = (int) ((NumberTerm) action.getTerm(1)).solve();
				PutInt("Hospital_X", (int) ((NumberTerm) action.getTerm(0)).solve());
				PutInt("Hospital_Y", (int) ((NumberTerm) action.getTerm(1)).solve());
				dm.setType(hx, hy, "hospital");
				dm.updateGrid();
			} else if (act.equalsIgnoreCase("calculatecostofpath")) {
				int c_x = (int) ((NumberTerm) action.getTerm(0)).solve();
				int c_y = (int) ((NumberTerm) action.getTerm(1)).solve();
				String direction = server.GetObjectContainer().GetStoredValues().get("direction");
				server.GetPathGenerator().GetMap().GetMap().setDirection(direction);

				int p = server.GetPathGenerator().GetCost(new int[] { robot_x, robot_y }, new int[] { c_x, c_y });
				PrintMessage(action,
						"Cost of path from (" + robot_x + "," + robot_y + ") to (" + c_x + "," + c_y + ") : " + p);

				addPercept(Literal.parseLiteral("costOfPathToVictim(" + p + "," + c_x + "," + c_y + ")"));

			} else if (act.equalsIgnoreCase("clearpercepts")) {
				clearPercepts();
			} else if (act.equalsIgnoreCase("finish")) {
				dm.showCompletionMessage(victims, nom);

			} else if (act.equalsIgnoreCase(("movetolocation"))) {
				int g_x = (int) ((NumberTerm) action.getTerm(0)).solve();
				int g_y = (int) ((NumberTerm) action.getTerm(1)).solve();

				PutInt("ObjectiveX", g_x);
				PutInt("ObjectiveY", g_y);

				Path path = server.GetPathGenerator().GetPath(
						new int[] { server.GetObjectContainer().Robot_X(), server.GetObjectContainer().Robot_Y() },
						new int[] { g_x, g_y });

				String instruction = server.GetPathGenerator().GenerateInstruction(path);

				PrintMessage(action, "Requesting robot to move to given location (" + g_x + "," + g_y + ")...");
				
				server.SendMessage(instruction);

				nom++;
				addPercept(Literal.parseLiteral("inTransit"));
			}
		} catch (

		Exception ex) {
			ex.printStackTrace();
		}

		informAgsEnvironmentChanged();
		return true;

	}

	public Literal parsePercept(String percept, String... values) {
		StringBuilder builder = new StringBuilder();
		builder.append(percept);
		builder.append("(");
		for (int x = 0; x < values.length; x++) {
			builder.append(values[x]);

			if (x < values.length - 1)
				builder.append(",");
		}

		builder.append(")");
		return Literal.parseLiteral(builder.toString());
	}

	public int GetInt(String property) {
		return Integer.valueOf(server.GetObjectContainer().GetStoredValues().get(property));
	}

	public void PutInt(String property, int val) {
		server.GetObjectContainer().GetStoredValues().put(property, String.valueOf(val));
	}

	public void PrintMessage(Structure s, String msg) {
		dm.appendStats("Server~" + s.getFunctor(), msg, true);
	}

	// Called before the end of MAS execution
	@Override
	public void stop() {
		super.stop();
	}
}
