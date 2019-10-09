import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import PathFinding.Path;
import jason.asSyntax.Literal;
import Automation.PathGenerator;
import Containers.ObjectContainer;

public class ServerHandler extends Thread {
	public static boolean Server_Running = true;
	public static boolean Close_Connections = false;

	private PrintWriter out;
	private BufferedReader in;
	private BufferedReader reader;

	private ObjectContainer objContainer;

	public static final int PORT = 5984;

	private ParamedicEnvironment environment;
	private CommandHandler cmdHandler;
	private PathGenerator pathGenerator;

	public ServerHandler(ParamedicEnvironment environment) {
		this.environment = environment;
		this.objContainer = new ObjectContainer();
		this.cmdHandler = new CommandHandler(objContainer);
		this.pathGenerator = new PathGenerator(objContainer);
	}

	public PrintWriter CreateWriter(Socket client) throws Exception {
		return new PrintWriter(client.getOutputStream(), true);
	}

	public BufferedReader CreateReader(Socket client) throws Exception {
		return new BufferedReader(new InputStreamReader(client.getInputStream()));
	}

	public void CreateWriterReader(Socket client) {
		try {
			out = CreateWriter(client);
			in = CreateReader(client);
			reader = new BufferedReader(new InputStreamReader(System.in));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ObjectContainer GetObjectContainer() {
		return this.objContainer;
	}

	public PathGenerator GetPathGenerator() {
		return this.pathGenerator;
	}

	public void AddObstacle(int x, int y) {
		pathGenerator.AddObstruction(x, y);
		System.out.println("[SERVER] Added Obstruction: " + x + "," + y);
	}

	public void SendMessage(String msg) {
		if (this.out != null) {
			this.out.println(msg);
		}
	}

	@Override
	public void run() {
		try {
			System.out.println("[INFO] Awaiting Robot Connection...");
			System.out.println("[INFO] WAITING ON PORT: " + PORT);

			ServerSocket server = new ServerSocket(PORT);
			Socket client = server.accept();

			if (client.isBound())
				System.out.println("[INFO] Connected to Robot.");

			CreateWriterReader(client);

			environment.addPercept(Literal.parseLiteral("connected"));

			while (Server_Running) {

				if (in.ready()) {
					cmdHandler.Handle(in.readLine());
				}

				if (reader.ready()) {
					String entered_text = reader.readLine();
					switch (entered_text) {
					case "/end":
						Close_Connections = true;
						Server_Running = false;
						out.println("/end");
						break;
					case "/goto":
						entered_text = reader.readLine();
						String[] xy = entered_text.split(",");
						System.out.println(xy[0]);
						System.out.println(xy[1]);

						int x = Integer.parseInt(xy[0].trim());
						int y = Integer.parseInt(xy[1].trim());

						Path p = pathGenerator.GetPath(new int[] { objContainer.Robot_X(), objContainer.Robot_Y() },
								new int[] { x, y });
						String instruction = pathGenerator.GenerateInstruction(p);
						out.println(instruction);
					default:
						if (entered_text.startsWith("/obstacles")) {

							String obs_regex = "\\(([0-5])\\,([0-5])\\)+";

							final Pattern pattern = Pattern.compile(obs_regex, Pattern.MULTILINE);
							final Matcher matcher = pattern.matcher(entered_text);
							while (matcher.find()) {
								pathGenerator.AddObstruction(Integer.parseInt(matcher.group(1)),
										Integer.parseInt(matcher.group(2)));
							}

							out.println("Added obstructions: " + pathGenerator.ObstructionCount());
							break;
						} else {
							out.println(entered_text);
							break;
						}
					}
				}

				if (Close_Connections) {
					reader.close();
					server.close();
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
