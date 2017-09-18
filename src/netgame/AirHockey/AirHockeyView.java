package netgame.AirHockey;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class AirHockeyView extends JFrame{
	static final int DEFAULT_PORT = 45017;
	GamePanel game = new GamePanel();
	ScoringPanel scorePanel = new ScoringPanel();
	public AirHockeyView() { 
		JOptionPane.showMessageDialog(this, "Drag mouse to move paddle");
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BorderLayout());
		mainpanel.add(game, BorderLayout.CENTER);
		System.out.println("Ehh bby");
		this.setContentPane(mainpanel);
		setSize(1200, 600);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) throws IOException { 
		String host = JOptionPane
				.showInputDialog("Enter the name of the\nhost computer");
		if (host == null || host.trim().length() == 0)
			return;
		AirHockeyView viw = new AirHockeyView();
		viw.game.requestFocus();
		AirHockeyClientController con = new AirHockeyClientController(viw, host, DEFAULT_PORT);
	}
	public void pause() { 
		
	}
	public class ScoringPanel extends JPanel { 
		JTextField text = new JTextField();
		public ScoringPanel() { 
			this.add(text);
			text.setText("Score: 0 to 0");
		}
	}
	public void setMessageText(String string) {
		// TODO Auto-generated method stub
		this.setTitle(string);
		this.repaint();
	}
}
