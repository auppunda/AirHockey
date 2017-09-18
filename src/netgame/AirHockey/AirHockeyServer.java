package netgame.AirHockey;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import common.Hub;

public class AirHockeyServer extends Hub {

	private ServerModel model; // Records the state of the game.
	private HashSet<String> userNames = new HashSet<String>();
	private boolean running;

	public AirHockeyServer(int port) throws IOException { 
		super(port);
		model = new ServerModel();
		new Thread() {
			public void start()  { 
				running = true;
				while(running) { 
					try {
						int firstPlayer = model.getNextPlayer();
						int secondPlayer = model.getNextPlayer();
						AirHockeyModel game = model.startGame(firstPlayer,
								secondPlayer);
						System.out.println("Red : " + game.redPaddle.toString());
						System.out.println("Green : " + game.greenPaddle.toString());

						AirHockeyServer.this.sendToOne(firstPlayer,
								game);
						AirHockeyServer.this.sendToOne(secondPlayer,
								game);
						if(!game.sent) { 
							AirHockeyServer.this.sendToOne(firstPlayer,
									0);
							AirHockeyServer.this.sendToOne(secondPlayer,
									1);
							System.out.println("Sending number");
						}
					} catch (InterruptedException e) {

					}
				}
			}
		}.start();
	}

	protected void messageReceived(int playerID, Object message) {
		if(message instanceof int[]) { 
			int[] position = (int[]) message;
			AirHockeyModel mod = model.getGame(playerID);
			if(position[2]==0) { 
				model.setGreen(position[0], position[1], playerID); 

			}
			else if(position[2] == 1){ 
				model.setRed(position[0], position[1], playerID);


			}
			else if(position[2] == 3) {
				if(position[0] == 0 && position[1] == 0) { 
					position[3] = -position[4];
					position[4] = -position[5];
				}
				else { 
					position[3] = position[4] + position[0];
					position[4] = position[5] + position[1];
				}
				if(position[3] > AirHockeyClientController.MAX_VELOCITY) { 
					position[3] = AirHockeyClientController.MAX_VELOCITY;
				} else if(position[3] < -AirHockeyClientController.MAX_VELOCITY)
					position[3] = -AirHockeyClientController.MAX_VELOCITY;
				if(position[4] > AirHockeyClientController.MAX_VELOCITY) { 
					position[4] = AirHockeyClientController.MAX_VELOCITY;
				} else if(position[4] < -AirHockeyClientController.MAX_VELOCITY)
					position[4] = -AirHockeyClientController.MAX_VELOCITY;
				AirHockeyServer.this.sendToOne(playerID, position);
				
			}
			if(playerID % 2 == 0)
				AirHockeyServer.this.sendToOne(playerID - 1, position);
			else if (playerID % 2 == 1) { 
				AirHockeyServer.this.sendToOne(playerID + 1, position);
			}
			//			AirHockeyModel game = model.getGame(playerID);
			//			System.out.println("RED : [" + game.getRedX() + " , " + game.getRedY() + "]");
			//			System.out.println("GREEN: [" + game.getGreenX() + " , " + game.getGreenY() + "]");
			//			AirHockeyServer.this.sendToOne(playerID, game);
			//			AirHockeyServer.this.sendToOne(model.getOpponent(playerID), game);

		}
		else {
			String command = (String) message;
			if (command.equals("newgame")) {
				try {
					this.sendToOne(playerID, "waiting");
					model.addPlayerToQueue(playerID);
				}
				catch (InterruptedException e) {

				}
			}
			if(playerID % 2 == 0)
				AirHockeyServer.this.sendToOne(playerID - 1, command);
			else if (playerID % 2 == 1) { 
				AirHockeyServer.this.sendToOne(playerID + 1, command);
			}
		}

	}
	protected void playerConnected(int playerID) {

	}

	protected void playerDisconnected(int playerID) {
		userNames.remove(model.getPlayerName(playerID));
		int opponentID = model.getOpponent(playerID);
		model.remove(playerID);
		this.sendToOne(opponentID, "waiting");

	}

	protected void extraHandshake(int playerID, ObjectInputStream in,
			ObjectOutputStream out) throws IOException {
		String name;
		try {
			name = (String) in.readObject();
			while (userNames.contains(name)) {
				out.writeObject(false);
				name = (String) in.readObject();
			}
			userNames.add(name);
			out.writeObject(true); // username was available
			model.addPlayerToQueue(playerID);
			model.setPlayerName(playerID, name);
		} catch (ClassNotFoundException e) {
			return;
		} catch (InterruptedException e) {
			return;
		}

	}
}
