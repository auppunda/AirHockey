package chatroom;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import common.*;

public class ChatRoomClientView extends JFrame {
	
	public static final String SEND_COMMAND = "Send";
	public static final String QUIT_COMMAND = "Quit";
    
    private JTextField messageInput;   
    private JButton sendButton;   
    private JButton quitButton;    

    private JTextArea transcript;      
    
    public ChatRoomClientView() {
        super("Chat Room");
        setBackground(Color.BLACK);
        setLayout(new BorderLayout(2,2));
        transcript = new JTextArea(30,60);
        transcript.setLineWrap(true);
        transcript.setWrapStyleWord(true);
        transcript.setMargin(new Insets(5,5,5,5));
        transcript.setEditable(false);
        add(new JScrollPane(transcript), BorderLayout.CENTER);
        sendButton = new JButton(SEND_COMMAND);
        quitButton = new JButton(QUIT_COMMAND);
        messageInput = new JTextField(40);
        messageInput.setMargin(new Insets(3,3,3,3));
        sendButton.setEnabled(false);
        messageInput.setEditable(false);
        messageInput.setEnabled(false);
        messageInput.setActionCommand(SEND_COMMAND);
        JPanel bottom = new JPanel();
        bottom.setBackground(Color.LIGHT_GRAY);
        bottom.add(new JLabel("You say:"));
        bottom.add(messageInput);
        bottom.add(sendButton);
        bottom.add(Box.createHorizontalStrut(30));
        bottom.add(quitButton);
        add(bottom,BorderLayout.SOUTH);
        pack();
    }
    
    public void registerActionListener(ActionListener listener) {
    	sendButton.addActionListener(listener);
        quitButton.addActionListener(listener);
        messageInput.addActionListener(listener);
    }
    
    public void registerWindowListener(WindowListener listener) {
    	addWindowListener(listener);
    }
    
    public void addToTranscript(String message) {
        transcript.append(message);
        transcript.append("\n\n");
        transcript.setCaretPosition(transcript.getDocument().getLength());
    }
    
    public void setEnableForSendButtonAndInputTextField(boolean enabled) {
        sendButton.setEnabled(enabled);
        messageInput.setEnabled(enabled);
        messageInput.setEditable(enabled);
        messageInput.setText("");
    }

    public String getMessageText() {
    	return messageInput.getText();
    }
    
    public void clearMessageText() {
    	messageInput.setText("");
    }
    
    public void setFocusToMessageText() {
    	messageInput.requestFocus();
    }
    

}
