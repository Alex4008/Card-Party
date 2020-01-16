package singlePlayerWar;

import java.awt.Graphics;
import java.util.ArrayList;

import main.GameType;
import main.Main;

public abstract class SinglePlayerGame {

	int playerCount;
	GameType gameType;
	int numberAI;
	int maxAI;
	ArrayList<SingleplayerPlayer> players;
	
	SinglePlayerGame(int playerCount) {
		this.playerCount = playerCount;
		this.numberAI = playerCount - 1;
		if(numberAI > maxAI) numberAI = maxAI;
		players = new ArrayList<SingleplayerPlayer>();
	}
	
	public abstract void tick(Main m);
	public abstract void render(Main m, Graphics g);
	
	public boolean addPlayer(SingleplayerPlayer p) {
		if(players.size() < playerCount) {
			players.add(p);
			return true;
		}
		else return false;
	}
	
	public boolean removePlayer(SingleplayerPlayer p) {
		if(players.contains(p)) {
			players.remove(p);
			return true;
		}
		else return false;
	}
	
	//Getters / Setters
	public int getPlayerCount() {
		return playerCount;
	}
	
	public GameType getGameType() {
		return gameType;
	}
	
	
}
