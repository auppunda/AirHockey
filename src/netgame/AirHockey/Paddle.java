package netgame.AirHockey;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Paddle {
	private String color;
	private Image image;
	private int x, y;
	
	public Paddle(String color, int x, int y) { 
		this.color = color;
		String fileName = "";
		if(color.equals("green")) { 
			fileName = "green paddle.jpg";
		}
		else { 
			fileName = "red paddle.jpg";
		}
		System.out.println(fileName);
		try {
			image = ImageIO.read(getClass().getClassLoader().getResource(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics g) { 
		//g.drawImage(image, x, y, 40, 40, null);
		if(color.equals("green")) { 
			g.setColor(Color.green);
		} else
			 g.setColor(Color.RED);
		g.fillOval(x - 40, y - 40, 80, 80);
		g.setColor(Color.white);
		g.fillOval(x - 35, y - 35, 70, 70);
		if(color.equals("green")) { 
			g.setColor(Color.green);
		}
		else g.setColor(Color.RED);
		g.fillOval(x - 20,y - 20, 40, 40);
		g.setColor(Color.BLACK);
		g.fillOval(x - 10,y - 10, 20, 20);
		
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
}
