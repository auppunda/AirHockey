package netgame.AirHockey;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.JobName;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartGameView extends JPanel implements MouseListener{

	Image image = null;
	public StartGameView() {
		try {
			image = ImageIO.read(getClass().getClassLoader().getResource("StartScreen.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.addMouseListener(this);
		 
	}
	
	public static void main(String[] args) { 
		JFrame frame = new JFrame();
		frame.setSize(800, 1400);
		frame.setContentPane(new StartGameView());
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	@Override
	public void paintComponent(Graphics g) { 
		g.drawImage(image, 0 , 0, this.getWidth(), this.getHeight(), null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame();
		frame.setSize(800, 1400);
		frame.setContentPane(new HostPanel());
		frame.setContentPane(new StartGameView());
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setVisible(true);
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
	
	public class HostPanel extends JPanel implements ActionListener{ 
		JTextField field;
		JButton button;
		boolean hasMadeView = false;
		public HostPanel() {
			field = new JTextField();
			button = new JButton("enter");
			button.addActionListener(this);
			this.add(button);
			this.add(field);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getActionCommand().equals("enter") && !hasMadeView) { 
				String host = field.getText();
				AirHockeyView viw = new AirHockeyView();
				try {
					new AirHockeyClientController(viw, host, AirHockeyView.DEFAULT_PORT);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				hasMadeView = true;
			}
			
		}
	}
}
