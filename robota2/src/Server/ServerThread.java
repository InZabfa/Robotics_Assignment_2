package Server;

import Global.RucksackContainer;
import Handlers.CommandHandler;
import Handlers.RepositoryHandler;
import lejos.hardware.Button;

public class ServerThread extends Thread {

	private Server server;
	private RepositoryHandler repository;
	private CommandHandler commandHandler;
	private RucksackContainer rucksackcontainer;

	private Server GetServer() {
		return this.server;
	}

	public RepositoryHandler GetRepository() {
		return this.repository;
	}

	public CommandHandler GetCommandHandler() {
		return this.commandHandler;
	}

	public RucksackContainer GetRuckSackContainer() {
		return this.rucksackcontainer;
	}

	public ServerThread(RucksackContainer rcontainer) {
		this.server = new Server();
		this.rucksackcontainer = rcontainer;
		this.rucksackcontainer.SetServer(this.server);

		this.repository = rcontainer.GetRepository();
		this.commandHandler = new CommandHandler(rcontainer);
	}

	public void run() {
		GetServer().Connect();
		GetRuckSackContainer().SetServer(GetServer());

		try {
			while (Global.Properties.Generic.ROBOT_RUNNING && Button.ESCAPE.isUp()) {
				if (GetServer().GetSocket() != null && GetServer().GetSocket().isConnected()) {
					if (GetServer().IsReady()) {
						String output = GetServer().GetString();
						if (output != null && output != "") {
							GetCommandHandler().HandleCommand(output);
							output = null;
						}
					} else {
						GetServer().GetOutput().println("update X " + GetRuckSackContainer().GetCompass().GetXY()[0]);
						GetServer().GetOutput().println("update Y " + GetRuckSackContainer().GetCompass().GetXY()[1]);
						GetServer().GetOutput().println("update direction "
								+ GetRuckSackContainer().GetCompass().GetFacingDirection().toString().toLowerCase());
					}
				} else {
					Global.Properties.Generic.ROBOT_RUNNING = false;
					return;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}