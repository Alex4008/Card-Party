package multiPlayerGames;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import main.GameType;
import main.Main;

public abstract class MultiPlayerGame {

	int playerCount;
	GameType gameType;
	MultiplayerPlayer player;
	HashMap<String, Opponent> opponents;
	
	MultiPlayerGame(int playerCount) {
		this.playerCount = playerCount;
		opponents = new HashMap<String, Opponent>();
	}
	
	public abstract void tick(Main m);
	public abstract void render(Main m, Graphics g);
	
	//Getters / Setters
	public int getPlayerCount() {
		return playerCount;
	}
	
	public GameType getGameType() {
		return gameType;
	}
	
	
}
