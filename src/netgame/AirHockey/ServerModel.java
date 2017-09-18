package netgame.AirHockey;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerModel {

	private ConcurrentHashMap<Integer, AirHockeyModel> currentGames 
	= new ConcurrentHashMap<Integer, AirHockeyModel>();
	private ConcurrentHashMap<Integer, PlayerProfile> playerProfiles 
	= new ConcurrentHashMap<Integer, PlayerProfile>();
	private LinkedBlockingQueue<Integer> waitingPlayers = new LinkedBlockingQueue<Integer>(50);
	public void addPlayerToQueue(int playerID) throws InterruptedException {
		if (!playerProfiles.containsKey(playerID))
			playerProfiles.put(playerID, new PlayerProfile(playerID));
		currentGames.remove(playerID);
		try {
			waitingPlayers.put(playerID);
		}
		catch (InterruptedException e) {

		}
	}
	public int getNextPlayer() throws InterruptedException {
		return waitingPlayers.take();
	}
	public AirHockeyModel getGame(int playerID) {
		return currentGames.get(playerID);
	}
	public int getMostRecentOpponent(int secondPlayer) {
		// TODO Auto-generated method stub
		return 0;
	}
	public AirHockeyModel startGame(int firstPlayer, int secondPlayer) {
		if (!playerProfiles.containsKey(firstPlayer))
			playerProfiles.put(firstPlayer, new PlayerProfile(firstPlayer));
		if (!playerProfiles.containsKey(secondPlayer))
			playerProfiles.put(secondPlayer, new PlayerProfile(secondPlayer));
		AirHockeyModel game = new AirHockeyModel(1200, 600, firstPlayer, secondPlayer);
		currentGames.put(firstPlayer, game);
		currentGames.put(secondPlayer, game);
		playerProfiles.get(firstPlayer).colorID = 0;
		playerProfiles.get(secondPlayer).colorID = 1;
		game.startGame();
		return game;
	}
	public String getPlayerName(int playerID) {
		return playerProfiles.get(playerID).getName();
	}
	
	public int getOpponent(int playerID) {
		AirHockeyModel game = currentGames.get(playerID);
		if (game != null)
			return game.getOtherPlayerID(playerID);
		return -1;
	}
	
	public void remove(int playerID) {
		// TODO Auto-generated method stub
			AirHockeyModel game = currentGames.get(playerID);
			if (game != null) {
				int opponentID = game.getOtherPlayerID(playerID);
				currentGames.remove(playerID);
				currentGames.remove(opponentID);
				waitingPlayers.add(opponentID);
			}
			playerProfiles.remove(playerID);
			waitingPlayers.remove(playerID);
	}
	public void setPlayerName(int playerID, String name) {
		// TODO Auto-generated method stub
		if (!playerProfiles.containsKey(playerID))
			playerProfiles.put(playerID, new PlayerProfile(playerID));
		playerProfiles.get(playerID).setName(name);
	}
	public PlayerProfile getPlayerProfile(int playerID) { 
		return playerProfiles.get(playerID);
	}
	public void setGreen(int i, int j, int playerID) {
		// TODO Auto-generated method stub
		currentGames.get(playerID).setGreen(i, j);
	}
	public void setRed(int i, int j, int playerID) {
		// TODO Auto-generated method stub
		currentGames.get(playerID).setRed(i, j);

	}
}
