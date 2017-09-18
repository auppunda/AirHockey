package netgame.AirHockey;

import java.io.IOException;

public class AirHockeyServerMain {
	private static final int DEFAULT_PORT = 45017;
	
	public static void main(String[] args) {
		try {
			new AirHockeyServer(DEFAULT_PORT);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
