package chatroom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import common.Client;
import common.ForwardedMessage;

public class ChatRoomClientController implements ActionListener, WindowListener {

	private static final int PORT = 37829;

	private volatile boolean connected;
	private ChatRoomClientView view;
	private ChatRoomClientConnection connection;
	
	ChatRoomClientController(final String host, final ChatRoomClientView view)
			throws IOException {
		this.view = view;
		view.registerActionListener(this);
		view.registerWindowListener(this);
		new Thread() {
			// This is a thread that opens the connection to the server. Since
			// that operation can block, it's not done directly in the
			// constructor.
			// Once the connection is established, the user interface elements
			// are
			// enabled so the user can send messages. The Thread dies after
			// the connection is established or after an error occurs.
			public void run() {
				try {
					view.addToTranscript("Connecting to " + host + " ...");
					connection = new ChatRoomClientConnection(host);
					connected = true;
					view.setEnableForSendButtonAndInputTextField(true);
					view.setFocusToMessageText();
				} catch (IOException e) {
					view.addToTranscript("Connection attempt failed.");
					view.addToTranscript("Error: " + e);
				}
			}
		}.start();
	}

	private class ChatRoomClientConnection extends Client {

		public ChatRoomClientConnection(String host) throws IOException {
			super(host, PORT);
		}

		/**
		 * Responds when a message is received from the server. It should be a
		 * ForwardedMessage representing something that one of the participants
		 * in the chat room is saying. The message is simply added to the
		 * transcript, along with the ID number of the sender.
		 */
		protected void messageReceived(Object message) {
			if (message instanceof ForwardedMessage) { // (no other message
														// types are expected)
				ForwardedMessage bm = (ForwardedMessage) message;
				view.addToTranscript("#" + bm.senderID + " SAYS:  "
						+ bm.message);
			}
		}

		/**
		 * Called when the connection to the client is shut down because of some
		 * error message. (This will happen if the server program is
		 * terminated.)
		 */
		protected void connectionClosedByError(String message) {
			view.addToTranscript("Sorry, communication has shut down due to an error:\n     "
					+ message);
			view.setEnableForSendButtonAndInputTextField(false);
			connected = false;
			connection = null;
		}

		/**
		 * Posts a message to the transcript when someone joins the chat room.
		 */
		protected void playerConnected(int newPlayerID) {
			view.addToTranscript("Someone new has joined the chat room, with ID number "
					+ newPlayerID);
		}

		/**
		 * Posts a message to the transcript when someone leaves the chat room.
		 */
		protected void playerDisconnected(int departingPlayerID) {
			view.addToTranscript("The person with ID number "
					+ departingPlayerID + " has left the chat room");
		}
	}



	public void actionPerformed(ActionEvent evt) {
		String command = evt.getActionCommand();
		if (command.equals(ChatRoomClientView.QUIT_COMMAND)) { // Disconnect
																// from the
																// server and
																// end the
																// program.
			doQuit();
		} else if (command.equals(ChatRoomClientView.SEND_COMMAND)) {
			String message = view.getMessageText();
			if (message.trim().length() == 0)
				return;
			connection.send(message);
			view.clearMessageText();
			view.setFocusToMessageText();
		}
	}

	/**
	 * Called when the user clicks the Quit button or closes the window by
	 * clicking its close box.
	 */
	private void doQuit() {
		if (connected)
			connection.disconnect(); // Sends a DisconnectMessage to the server.
		view.dispose(); // Destroys the window and cleans up
		try {
			Thread.sleep(1000); // Time for DisconnectMessage to actually be
								// sent.
		} catch (InterruptedException e) {
		}
		System.exit(0);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		doQuit();

	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}