package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import lejos.hardware.ev3.LocalEV3;

public class Server{
	private PrintWriter out;
	private BufferedReader in;

	private Socket socket;

	public Server() {}

	public PrintWriter GetOutput() { return this.out; }
	public BufferedReader GetInput() { return this.in; }
	public Socket GetSocket() { return this.socket; }

	public void Connect() {
		try {
			this.socket = new Socket(Global.Properties.Server.IP, Global.Properties.Server.PORT);
			this.out = new PrintWriter(GetSocket().getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(GetSocket().getInputStream()));	

			AlertConnection();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void AlertConnection() {
		if (GetSocket() != null) {
			if (GetSocket().isBound()) {
				LocalEV3.get().getAudio().systemSound(1);
			}
		}
	}

	public boolean IsReady() throws IOException { return GetInput().ready(); }

	public String GetString(){
		try {
			return GetInput().readLine();
		} catch (Exception e) {
			return null;
		}
	}
}