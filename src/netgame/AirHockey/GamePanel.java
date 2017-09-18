package netgame.AirHockey;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class GamePanel extends JPanel {

	Paddle greenPaddle;
	Paddle redPaddle;
	Puck puck;
	int redScore = 0;
	int greenScore = 0;
	boolean endGame = false;
	boolean startingGame = false;
	GamePanel() { 
		greenPaddle = new Paddle("green", (int) (.25*this.getWidth()), (int) (.5*this.getHeight()));
		redPaddle = new Paddle("red", (int)(.75*this.getWidth()), (int)(.5*this.getHeight()));
		puck = new Puck(0,0);
	}

	public void paintComponent(Graphics g) { 
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.ORANGE);
		g.fillRect(0,0, this.getWidth(), (int) (1.0/20.0 * this.getHeight()));
		g.fillRect(0, (int)(19.0*this.getHeight()/20.0), this.getWidth(), (int)(this.getHeight()/20.0));
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, this.getWidth()/20, (int)(this.getHeight()*.5) - 75);
		g.setColor(Color.red);
		g.fillRect(0, this.getHeight()/2 + 75, this.getWidth()/20, this.getHeight()/2 - 75);
		g.setColor(Color.green);
		g.fillRect(this.getWidth() * 19/20, 0, this.getWidth()/20, (int)(this.getHeight()*.5) - 75);
		g.setColor(Color.yellow);
		g.fillRect(this.getWidth() * 19/20, this.getHeight()/2 + 75, this.getWidth()/20, this.getHeight()/2 - 75);
		g.setColor(Color.WHITE);
		g.fillRect(this.getWidth()/2 - 5, 0, 10, this.getHeight());
		if(!startingGame) { 
			greenPaddle.setPosition((int) (.25*this.getWidth()), (int) (.5*this.getHeight()));
			redPaddle.setPosition((int)(.75*this.getWidth()), (int)(.5*this.getHeight()));
			puck.setPosition((int)(.5*this.getWidth()), (int)(.5*this.getHeight()));
		}
		g.setColor(Color.YELLOW);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
		g.drawString("Red: " + redScore + ": Green: " + greenScore, 70, this.getHeight()/10);
		puck.draw(g);
		greenPaddle.draw(g);
		redPaddle.draw(g);
		startingGame = true;
		if(greenScore == 4) { 
			g.setColor(Color.RED);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			//JOptionPane.showMessageDialog(this, "Green Wins. Restart Client Jar to play again");
			g.drawString("GREEN WINS",getWidth()/2 , getHeight()/2);
			greenScore = 0;
			endGame = true;
			
		}
		else if(redScore == 4) { 
			g.setColor(Color.RED);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			//JOptionPane.showMessageDialog(this, "Red Wins. Restart Client Jar to play again");
			g.drawString("RED WINS",getWidth()/2 , getHeight()/2);
			redScore = 0;
			endGame = true;
		}
	}
}
