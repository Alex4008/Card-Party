package menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import main.BufferedImageLoader;
import main.Button;
import main.Label;
import main.Main;

public class HostingMenu {
	private Button kickPlayer;
	private Button changeGame;
	private Button startGame;
	private Button shutdownServer;
	
	public HostingMenu() {
		//Create Buttons
		kickPlayer = new Button(new Point((Main.WIDTH - 5 -  Main.BUTTON_WIDTH), (Main.HEIGHT / 6 * 3) - 5), "/hostingMenuButtons/kickPlayer");
		changeGame = new Button(new Point((Main.WIDTH - 5 - Main.BUTTON_WIDTH), (Main.HEIGHT / 6 * 4) - 5), "/hostingMenuButtons/changeGame");
		startGame = new Button(new Point((Main.WIDTH - 5 - Main.BUTTON_WIDTH), Main.HEIGHT / 6 * 5), "/hostingMenuButtons/startGame");
		shutdownServer = new Button(new Point((5), Main.HEIGHT / 6 * 5), "/hostingMenuButtons/shutDownServer");
	}
	
	public void render(Main m, Graphics g) {
		Font n = new Font("Arial", 20, 20);
		g.setFont(n);
		g.setColor(Color.BLACK);
		g.drawString("Currently Hosting: " + Main.theServer.runningGame, 5, 20);
		g.drawString("Hosting On Port: " + Main.port, 5, 40);
		g.drawString("Players Connected: " + Main.GetNumConnections() + "/" + Main.maxPlayers, 5, 60);
		for(int i = 0; i < Main.getClients().size(); i++) {
			g.drawString("- " + Main.getClients().get(i).getPlayer().getUserName(), 5, 90 + (i * 20));
		}
		kickPlayer.render(g);
		changeGame.render(g);
		startGame.render(g);
		shutdownServer.render(g);
	}
	
	public Button getShutdownServerButton() {
		return shutdownServer;
	}
	
	public Button getStartGameButton() {
		return startGame;
	}
	
	public Button getChangeGameButton() {
		return changeGame;
	}
	
	public Button getKickPlayerButton() {
		return kickPlayer;
	}
}
