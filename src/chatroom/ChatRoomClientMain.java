package chatroom;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ChatRoomClientMain {

    public static void main(String[] args) {
        String host = JOptionPane.showInputDialog(
                       "Enter the host name of the\ncomputer that hosts the chat room:");
        if (host == null || host.trim().length() == 0)
            return;
        ChatRoomClientView view = new ChatRoomClientView();
        view.setLocation(200,100);
        view.setVisible(true);
        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
        	new ChatRoomClientController(host, view);
        }
        catch (IOException e) {
        	view.addToTranscript("Error Connecting To Server");
        }
    }    
}
