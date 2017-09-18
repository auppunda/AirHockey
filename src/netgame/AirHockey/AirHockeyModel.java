package netgame.AirHockey;

import java.awt.Point;
import java.io.IOException;
import java.io.Serializable;


public class AirHockeyModel implements Serializable{
	int height;
	int width;
	Point greenPaddle = new Point(400, 300);
	Point redPaddle = new Point(800, 300);
	int[] greenPaddleV  = {0,0};
	int[] redPaddleV  = {0,0};
	int puckX = 0;
	int puckY = 0;
	public int idPlayerX;
	public int idPlayerO; 
	boolean sent = false;
	public AirHockeyModel(int playerX, int player0, int height, int width) { 
		this.height = height;
		this.width = width;
		this.idPlayerO = player0;
		this.idPlayerX = playerX;
	}
	public static boolean isGoal(int x, int y, int height, int width, int padd) { 
		if(padd == 0) { 
			if(x <= width && x >= (int)(19 * width/20)) { 
				if(y >= height/2 -50 && y <= height/2  + 50) { 
					return true;
				}
			}
		}
		else if(padd == 1) { 
			
			if(x >= 0 && x <= (int)(.05 * width)) { 
				if(y >= height/2 -50 && y <= height/2  + 50) { 
					return true;
				}
			}
		}
		return false;
	}
	public double angleOfHit(int paddlex, int paddley) { 
		return Math.tan(paddley/paddlex);
	}
	public static boolean hitPuck(int paddleX, int paddleY, int puckX, int puckY) {
		float xd = paddleX - puckX;
		float yd = paddleY - puckY;

		float sumRadius = 70;
		float sqrRadius = sumRadius*sumRadius;

		float distSqr = (yd*yd) + (xd*xd);

		if(distSqr <= sqrRadius) { 
			return true;
		}
		return false;
	}
	public boolean hitWall(int x, int y) {
		// TODO Auto-generated method stub
		if(y-30 > height/20 && y + 30 < 19*height/20){
			if(x - 30 > width/10 && x + 30 < 9*width/10)
				return false;			
		}
		return true;
	}

	public static boolean hitHorizontalWall(int x, int y, int width, int height) { 
		if(x - 30 > width/20 && x+ 30 < 9*width/20) { 
			return false;
		}
		else if(y < height/2 + 50 && y> height/2 -50)
			return false;
		else return true;
	}

	public static boolean hitVerticalWall(int x, int y, int width, int height) { 
		if(y-30 > height/20 && y + 30 < 19*height/20){
			return false;
		}
		return true;
	}
	public int getOtherPlayerID(int playerID) {
		if (playerID == idPlayerX)
			return idPlayerO;
		else if (playerID == idPlayerO)
			return idPlayerX;
		else
			return -1;
	}
	public void startGame() { 
		System.out.println("hello");
		//greenPaddle = new Point(1*height/2, width/4);
		//redPaddle = new Point(1*height/2, 3*width/4);

	}

	public void setGreen(int x, int y) { 
		if(x == 0 && y == 0) { 
			
		}
		greenPaddle.setLocation(x,y);
	}

	public void setRed(int x, int y) { 
		if(x == 0 && y == 0) { 

		}
		redPaddle.setLocation(x, y);
	}

	public int getRedX() { 
		return (int)redPaddle.getX();
	}

	public int getRedY() { 
		return (int)redPaddle.getY();
	}
	public int getGreenX() { 
		return (int)greenPaddle.getX();
	}
	public int getGreenY() { 
		return (int)greenPaddle.getY();
	}

}