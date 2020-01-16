package games;

import java.awt.Graphics;
import java.util.ArrayList;

import main.GameType;
import main.Main;
import main.Player;

public abstract class Game {
	GameType gameType;
	//ArrayList<Player> players;
	
	Game() {
		//players = new ArrayList<Player>();
	}
	
	public abstract void tick(Main m);
	public abstract void render(Main m, Graphics g);
	
	/*
	public boolean addPlayer(Player p) {
		if(players.size() < playerCount) {
			players.add(p);
			return true;
		}
		else return false;
	}
	
	public boolean removePlayer(Player p) {
		if(players.contains(p)) {
			players.remove(p);
			return true;
		}
		else return false;
	}
	*/
	//Getters / Setters
	public GameType getGameType() {
		return gameType;
	}
	
}
