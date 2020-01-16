package menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import graphics.BufferedImageLoader;
import input.Button;
import main.GameType;
import main.Main;

public class MultiMenu {
	
	Button disconnect;
	GameType game = GameType.NULL;
	
	public MultiMenu() {
		BufferedImageLoader loader = new BufferedImageLoader();
		
		try {
			BufferedImage disconn = loader.loadImage("/multiMenuButtons/disconnect.png");
			BufferedImage disconnectHover = loader.loadImage("/multiMenuButtons/disconnect-hover.png");
			
			disconnect = new Button(new Point((Main.WIDTH) - (disconn.getWidth() + 5), Main.HEIGHT - (disconn.getHeight() + 5)), disconn, disconnectHover);
		}
		catch(IOException e) { e.printStackTrace(); }
	}
	
	public void render(Graphics g) {
		g.setFont(new Font("Arial", Main.FONT_SIZE, Main.FONT_SIZE));
		g.setColor(Color.BLACK);
		g.drawString("Connected As: " + Main.userName, 5, Main.FONT_SIZE);
		g.drawString("Selected Game: " + game, 5, Main.FONT_SIZE * 2);
		g.drawString("Users Online: " + Main.theClient.usernames.size() + "/" + Main.theClient.serverCap, 5, Main.FONT_SIZE * 3);
		for(int i = 0; i < Main.theClient.usernames.size(); i++) {
			g.drawString("- " + Main.theClient.usernames.get(i), 5, Main.FONT_SIZE * 4 + (i * Main.FONT_SIZE) + (Main.FONT_SIZE / 2));
		}
		disconnect.render(g);
	}
	
	public Button getDisconnectButton() {
		return disconnect;
	}
	
	public void setGameType(GameType value) {
		game = value;
	}
	
}
