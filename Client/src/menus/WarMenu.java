package menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import graphics.BufferedImageLoader;
import input.Button;
import main.Client;
import main.Main;
import multiPlayerGames.MultiPlayerWar;
import multiPlayerGames.MultiplayerPlayer;
import multiPlayerGames.Opponent;
import singlePlayerWar.SinglePlayerWar;

public class WarMenu {
	
	Button exit;
	
	SinglePlayerWar singleWar;
	MultiPlayerWar multiWar;
	boolean multiplayer;
	Client client;
	
	public WarMenu(Main m, int numPlayers) {
		multiplayer = false;
		singleWar = new SinglePlayerWar(m, numPlayers);
		
		BufferedImageLoader loader = new BufferedImageLoader();
		
		try {
			BufferedImage exitImg = loader.loadImage("/gameButtons/exit.png");
			BufferedImage exitHover = loader.loadImage("/gameButtons/exit-hover.png");
			
			//pause = new Button(new Point(0, Main.HEIGHT / 10 * 9), pauseImg, pauseHover);
			exit = new Button(new Point((Main.WIDTH - (exitImg.getWidth() / 2)) - (exitImg.getWidth() / 2), Main.HEIGHT / 10 * 9), exitImg, exitHover);
		}
		catch(IOException e) { e.printStackTrace(); }
	}
	
	public WarMenu(Main m, Client c) {
		multiplayer = true;
		client = c;
		multiWar = new MultiPlayerWar(m, c.getPlayerCount(), c.getPlayer(), c.getOpponents());
		BufferedImageLoader loader = new BufferedImageLoader();
		
		try {
			BufferedImage exitImg = loader.loadImage("/gameButtons/exit.png");
			BufferedImage exitHover = loader.loadImage("/gameButtons/exit-hover.png");
			
			//pause = new Button(new Point(0, Main.HEIGHT / 10 * 9), pauseImg, pauseHover);
			exit = new Button(new Point((Main.WIDTH - (exitImg.getWidth() / 2)) - (exitImg.getWidth() / 2), Main.HEIGHT / 10 * 9), exitImg, exitHover);
		}
		catch(IOException e) { e.printStackTrace(); }
	}
	
	public void render(Main m, Graphics g) {
		exit.render(g);
		if(multiplayer) multiWar.render(m, g);
		else singleWar.render(m, g);
	}
	
	public Button getExitButton() {
		return exit;
	}
	
	public void tick(Main m) {
		if(multiplayer) multiWar.tick(m);
		else singleWar.tick(m);
	}
	
	public MultiPlayerWar getMultiWarGame() {
		return multiWar;
	}
	
}
