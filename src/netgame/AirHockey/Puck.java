package netgame.AirHockey;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Puck {

	private int x, y;
	private int constx, consty;
	double velocityX = 0;
	private double velocityY = 0;
	private Image image;
	
	public Puck(int x, int y)  { 
		this.x = x;
		this.y = y;
		String fileName = "puck.jpg";
		try {
			 image = ImageIO.read(getClass().getClassLoader().getResource(fileName));
			} catch (IOException e) {
				e.printStackTrace();
		}
	}
	public void draw(Graphics g) { 
		//g.drawImage(image, x, y, 35, 35, null);
		g.setColor(Color.BLUE);
		g.fillOval(x - 30, y - 30, 60, 60);
		g.setColor(Color.WHITE);
		g.fillOval(x - 25, y - 25, 50, 50);
	}
	
	public void setPosition(int x, int y) { 
		this.x = x;
		this.y = y;
	}
	public int getY() {
		 return y;
	}
	public int getX() { 
		return x;
	}
	
	public double getVelocityX() { 
		return velocityX;
	}

	public double getVelocityY() { 
		return velocityY;
	}
	public void setVelocity(double d, double e) { 
		this.velocityX = d;
		this.velocityY = e;
	}
	public void reset(int height, int width) {
		y = height/2;
		x = width/2;
		setVelocity(0, 0);
		// TODO Auto-generated method stub
		
	}
	
}
