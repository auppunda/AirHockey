package netgame.AirHockey;

public class PlayerProfile {
	private String username;
	private int id;
	int colorID = 2222;
	
	public PlayerProfile (int id) { 
		this.id = id;
	}
	
	public int getId() { 
		return id;
	}
	
	public String getName() { 
		return username;
	}
	
	public void setName(String name) { 
		username = name;
	}
}
