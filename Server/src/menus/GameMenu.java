package menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import games.Uno;
import games.War;
import main.Button;
import main.GameType;
import main.Main;

public class GameMenu {
	
	Main m;
	private Button shutdownServer;
	private Button kickPlayer;
	private Button endGame;
	private War warGame;
	private Uno unoGame;
	
	boolean gameRunning = false;
	
	public GameMenu(Main m) {
		this.m = m;
		//Create Buttons
		kickPlayer = new Button(new Point((Main.WIDTH - 5 -  Main.BUTTON_WIDTH), (Main.HEIGHT / 6 * 4) - 5), "/hostingMenuButtons/kickPlayer");
		endGame = new Button(new Point((Main.WIDTH - 5 - Main.BUTTON_WIDTH), (Main.HEIGHT / 6 * 5) - 5), "/hostingMenuButtons/endGame");
		shutdownServer = new Button(new Point((5), Main.HEIGHT / 6 * 5), "/hostingMenuButtons/shutDownServer");
	}
	
	public void tick(Main m) {
		if(gameRunning) {
			if(Main.theServer.runningGame == GameType.WAR)
				warGame.tick(m);
			else if(Main.theServer.runningGame == GameType.UNO) {
				unoGame.tick(m);
			}
		}
	}
	
	public void render(Main m, Graphics g) {
		if(gameRunning == false) return;
		Font n = new Font("Arial", 20, 20);
		g.setFont(n);
		g.setColor(Color.BLACK);
		g.drawString("Currently Hosting: " + Main.theServer.runningGame, 5, 20);
		g.drawString("Hosting On Port: " + Main.port, 5, 40);
		g.drawString("Players Connected: " + Main.GetNumConnections() + "/" + Main.maxPlayers, 5, 60);
		for(int i = 0; i < Main.getClients().size(); i++) {
			String s = "- " + Main.getClients().get(i).getPlayer().getUserName() + "    Ready Status: " + Main.getClients().get(i).getPlayer().getReadyStatus();
			if(Main.getClients().get(i).isAI()) s = s + "    [ AI ]";
			g.drawString(s, 5, 90 + (i * 20) );
		}
		kickPlayer.render(g);
		shutdownServer.render(g);
		endGame.render(g);
	}
	
	public Button getShutdownServerButton() {
		return shutdownServer;
	}
	
	public Button getKickPlayerButton() {
		return kickPlayer;
	}
	
	public War getWarGame() {
		return warGame;
	}
	
	public Button getEndGameButton() {
		return endGame;
	}
	
	public void startWarGame() {
		warGame = new War(m);
		gameRunning = true;
	}
	
	public void startUnoGame() {
		unoGame = new Uno(m, m.clients.size(), 0, 0, true, true, true);
		gameRunning = true;
	}
}
