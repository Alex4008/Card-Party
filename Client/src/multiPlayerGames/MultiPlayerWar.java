package multiPlayerGames;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import cards.CardDeck;
import main.GameState;
import main.GameType;
import main.LoadState;
import main.Main;

public class MultiPlayerWar extends MultiPlayerGame{
	int clientId;
	int[] render = {0,0,0,0};
	
	public MultiPlayerWar(Main m, int playerCount, MultiplayerPlayer player, HashMap<String, Opponent> opponents) {
		super(playerCount);
		super.gameType = GameType.WAR;
		super.player = player;
		super.opponents = opponents;
		clientId = player.getPlayerId();
		
		switch(clientId) {
		case 0:
			render[0] = 0;
			render[1] = 1;
			render[2] = 2;
			render[3] = 3;
			break;
		case 1:
			render[0] = 1;
			render[1] = 0;
			render[2] = 3;
			render[3] = 2;
			break;
			
		case 2:
			render[0] = 2;
			render[1] = 3;
			render[2] = 1;
			render[3] = 0;
			break;
			
		case 3:
			render[0] = 3;
			render[1] = 2;
			render[2] = 0;
			render[3] = 1;
			break;
			
			default:
				System.out.println("Something broke " + clientId);
				break;
		}
		
		//This code is ugly and WILL crash <3 - Past Alex Who Doesn't Care Yet
		for(int i = 0; i < opponents.size(); i++) {
			((Opponent) opponents.values().toArray()[i]).setRenderNum(render[i + 1]);
		}
		
		
	}


	@Override
	public void tick(Main m) {
		player.tick(m);

	}

	@Override
	public void render(Main m, Graphics g) {
		player.render(g);
		for(Opponent o : opponents.values()) o.render(g);
		
		g.setFont(new Font("Arial", Main.FONT_SIZE, Main.FONT_SIZE));
		g.setColor(Color.BLACK);
		g.drawString("Connected As: " + Main.userName, 5, Main.FONT_SIZE);
		Font n = new Font("Arial", 20, 20);
		g.setFont(n);
		g.setColor(Color.BLACK);
	}

}
