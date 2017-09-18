package netgame.AirHockey;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import common.Client;
import common.Hub;

public class AirHockeyClientController implements MouseListener, MouseMotionListener, WindowListener {
	private AirHockeyView view;
	private AirHockeyModel model;
	int previousX = 0;
	int previousY = 0;
	int paddlevelocityX = 0;
	int paddlevelocityY = 0;
	boolean endGame = false;
	static final int MAX_VELOCITY = 6000;
	private AirHockeyConnection connection;
	private int myID;

	public AirHockeyClientController(AirHockeyView viw, String host, int port) throws IOException 
	{
		connection = new AirHockeyConnection(host, port);
		this.view = viw;
		previousX = view.game.greenPaddle.getX();
		previousY = view.game.greenPaddle.getX();


		Timer collisionChecker = new Timer(1, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(view.game.puck.getX() > view.getWidth() || view.game.puck.getX() < 0) { 
					view.game.puck.reset(view.getHeight(), view.getWidth());
					connection.send("reset");
				}
				if(view.game.puck.getY() > view.getHeight() || view.game.puck.getY() < 0) { 
					view.game.puck.reset(view.getHeight(), view.getWidth());
					connection.send("reset");
				}
				if(AirHockeyModel.isGoal(view.game.puck.getX(), view.game.puck.getY(), view.getHeight(), view.getWidth(), connection.getPaddleNumber())) {
					connection.send("goal");
					view.game.puck.reset(view.getHeight(), view.getWidth());
					if(connection.getPaddleNumber() == 0) { 
						view.game.greenScore = view.game.greenScore+1;
					}
					else { 
						view.game.redScore = view.game.redScore+1;
					}
				}
				if(AirHockeyModel.hitHorizontalWall(view.game.puck.getX(), view.game.puck.getY(), view.getWidth(), view.getHeight())) { 
					connection.send(new int[] {-(int)view.game.puck.getVelocityX(), (int)view.game.puck.getVelocityY(), 2 /*, view.game.puck.getX(), view.game.puck.getY() */});
					view.game.puck.setVelocity(-(int)view.game.puck.getVelocityX(), (int)view.game.puck.getVelocityY());
				}
				if((AirHockeyModel.hitVerticalWall(view.game.puck.getX(), view.game.puck.getY(), view.getWidth(), view.getHeight()))) { 
					connection.send(new int[] {(int)view.game.puck.getVelocityX(), -(int)view.game.puck.getVelocityY(), 2 /*, view.game.puck.getX(), view.game.puck.getY()*/});
					view.game.puck.setVelocity((int)view.game.puck.getVelocityX(), -(int)view.game.puck.getVelocityY());
				}
				if(connection.getPaddleNumber() == 0) { 
					int currentX = view.game.greenPaddle.getX();
					int currentY = view.game.greenPaddle.getY();
					paddlevelocityX = (currentX - previousX)*1000;
					paddlevelocityY = (currentY - previousY)*1000; 
					previousX = currentX;
					previousY = currentY;
					if(AirHockeyModel.hitPuck(currentX, currentY, view.game.puck.getX(), view.game.puck.getY())) { 
						connection.send(new int[] {(int) view.game.puck.getVelocityX(), (int) view.game.puck.getVelocityY(), 3, 1, paddlevelocityX, paddlevelocityY} );
					}
				}
				else if(connection.getPaddleNumber() == 1) { 
					int currentX = view.game.redPaddle.getX();
					int currentY = view.game.redPaddle.getY();
					paddlevelocityX = (currentX - previousX)*100;
					paddlevelocityY = (currentY - previousY)*100; 
					previousX = currentX;
					previousY = currentY;
					if(AirHockeyModel.hitPuck(currentX, currentY, view.game.puck.getX(), view.game.puck.getY())) { 
						connection.send(new int[] {(int) view.game.puck.getVelocityX(), (int) view.game.puck.getVelocityY(), 3, 1, paddlevelocityX, paddlevelocityY} );
					}
				}

				if(model != null) { 
					if(connection.getPaddleNumber() == 0) { 
						if(model.getRedX() > 5 &&  model.getRedY() > 5) { 
							view.game.redPaddle.setPosition(model.getRedX(), model.getRedY());
						} else { 

						}
					} else if(connection.getPaddleNumber() == 1) { 
						if(model.getGreenX() > 5 &&  model.getGreenY() > 5) { 
							view.game.greenPaddle.setPosition(model.getGreenX(), model.getGreenY());
						}
						else { 

						}
					}
				}
				view.game.puck.setPosition((int)(view.game.puck.getX() + .001*view.game.puck.getVelocityX()) , (int)(view.game.puck.getY() + .001*view.game.puck.getVelocityY()));
				view.repaint();
				endGame = view.game.endGame;
			}


		});
		collisionChecker.start();
		view.addMouseListener(this);
		view.addMouseMotionListener(this);
		view.addWindowListener(this);

		this.myID = connection.getID();
	}

	public class AirHockeyConnection extends Client {

		private int paddleColorNumber = 242522352;
		public AirHockeyConnection(String hubHostName, int hubPort)
				throws IOException {
			super(hubHostName, hubPort);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void messageReceived(final Object message) {
			// TODO Auto-generated method stub
			if(message instanceof AirHockeyModel) {
				System.out.println("message elements");
				System.out.println("RED : [" + ((AirHockeyModel) message).getRedX() + " , " + ((AirHockeyModel) message).getRedY() + "]");
				System.out.println("GREEN: [" + ((AirHockeyModel) message).getGreenX() + " , " + ((AirHockeyModel) message).getGreenY() + "]");
				SwingUtilities.invokeLater(new Runnable() {
					public void run() { 
						updateModel((AirHockeyModel) message);
						view.repaint();
					}
				});
			}
			if(message instanceof int[]) { 
				int[] position = (int[]) message;
				if(position[2] == 0) { 
					view.game.greenPaddle.setPosition(position[0], position[1]);
					if(model != null)
						model.greenPaddle.setLocation(position[0], position[1]);
				}
				else if(position[2] == 1){ 
					view.game.redPaddle.setPosition(position[0], position[1]);
					if(model != null)
						model.redPaddle.setLocation(position[0], position[1]);
				}
				else if(position[2] == 3) { 
					view.game.puck.setVelocity(position[3], position[4]);
				}
				else if(position[2] == 2) { 
					view.game.puck.setVelocity(position[0], position[1]);
					//view.game.puck.setPosition(position[3], position[4]);
				}
				//System.out.println("eh bby");
			}
			if (message instanceof String) {
				String command = (String) message;
				if (command.equals("waiting")) {
					view.setMessageText("Waiting for an opponent");
					view.repaint();	

				}
				else if(command.equals("goal")) { 
					view.game.puck.reset(view.getHeight(), view.getWidth());
					if(connection.getPaddleNumber() == 0) { 
						view.game.redScore = view.game.redScore+1;
					}
					else { 
						view.game.greenScore = view.game.greenScore+1;
					}
				}
				else if(command.equals("reset")) { 
					view.game.puck.reset(view.getHeight(), view.getWidth());
				}
			}
			if (message instanceof Integer) { 
				connection.send(new int[] {view.game.greenPaddle.getX(), view.game.greenPaddle.getX(),0, 0 ,0 });
				connection.send(new int[] {view.game.redPaddle.getX(), view.game.redPaddle.getX(),1, 0, 0});
				paddleColorNumber = (Integer)message;
				if(paddleColorNumber == 0) { 
					view.setMessageText("You are the green paddle");
					System.out.println("GREEN");
				}
				else { 
					view.setMessageText("You are the red paddle");
					System.out.println("RED");

				}
			}
		}
		protected void extraHandshake(ObjectInputStream in,
				ObjectOutputStream out) throws IOException {
			String userName = JOptionPane
					.showInputDialog("Enter that name that you want\nto use in the game.");
			try {
				out.writeObject(userName);
				Boolean userNameAvailable = (Boolean) in.readObject();
				while (!userNameAvailable) {
					String name = JOptionPane
							.showInputDialog("Username already in use. Choose another.");
					out.writeObject(name);
					userNameAvailable = (Boolean) in.readObject();
				}
				//view.repaint();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e ) {
				e.printStackTrace();
			}
		}


		protected void serverShutdown(String message) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(view,
							"The server has disconnected.\n"
									+ "The game is ended.");
					System.exit(0);
				}
			});
		}
		public int getPaddleNumber() {
			return paddleColorNumber;
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if(!endGame) {
			if(connection.getPaddleNumber() == 0) { 
				Point point = e.getPoint();
				if(e.getX() <view.game.getWidth()/2 && e.getX() > view.game.getWidth()/20) {
					if(e.getY() < 19*view.game.getHeight()/20 && e.getY() > view.game.getHeight()/20){
						view.game.greenPaddle.setPosition((int)point.getX(), (int)point.getY());
						int[] green = {(int)point.getX(), 
								(int)point.getY(), 0, paddlevelocityX, paddlevelocityY};
						connection.send(green);
					}
				}

			}
			else if(connection.getPaddleNumber() == 1){ 
				Point point = e.getPoint();
				if(e.getX() >view.game.getWidth()/2 && e.getX() < 19*view.game.getWidth()/20) { 
					if(e.getY() < 19*view.game.getHeight()/20 && e.getY() > view.game.getHeight()/20) {
						int[] red = {(int)point.getX(), 
								(int)point.getY(), 1,  paddlevelocityX, paddlevelocityY};
						connection.send(red);
						view.game.redPaddle.setPosition((int)point.getX(), (int)point.getY());
					}
				}
			}
			view.repaint();
		}
	}

	public void updateModel(AirHockeyModel mod)  { 
		this.model = mod;

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		view.dispose();
		connection.disconnect(); // Send a disconnect message to the hub.
		try {
			Thread.sleep(500); // Wait one-half second to allow the message to
			// be sent.
		} catch (InterruptedException ec) {

		}
		System.exit(0);
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}
}
