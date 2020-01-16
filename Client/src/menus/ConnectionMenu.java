package menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import graphics.BufferedImageLoader;
import graphics.Label;
import input.Button;
import main.Main;

public class ConnectionMenu {
	
	Label connectToServerText;
	Label ipText;
	Label portText;
	Label userText;
	Button connect;
	Button back;
	
	public ConnectionMenu() {
		BufferedImageLoader loader = new BufferedImageLoader();
		
		try {
			BufferedImage connectImg = loader.loadImage("/connectionMenuButtons/connectToServerText.png");
			BufferedImage ipImg = loader.loadImage("/connectionMenuButtons/ipText.png");
			BufferedImage portImg = loader.loadImage("/connectionMenuButtons/portText.png");
			BufferedImage userImg = loader.loadImage("/connectionMenuButtons/usernameText.png");
			BufferedImage connImg = loader.loadImage("/connectionMenuButtons/connect.png");
			BufferedImage connectHover = loader.loadImage("/connectionMenuButtons/connect-hover.png");
			BufferedImage backImg = loader.loadImage("/genericButtons/back.png");
			BufferedImage backHover = loader.loadImage("/genericButtons/back-hover.png");
			
			connectToServerText = new Label(new Point((Main.WIDTH / 2) - (connectImg.getWidth() / 2), (Main.HEIGHT / 6 * 1) - (connectImg.getHeight())), connectImg);
			ipText = new Label(new Point((Main.WIDTH / 3) - (ipImg.getWidth() / 3), (Main.HEIGHT / 6 * 2) - (ipImg.getHeight())), ipImg);
			portText = new Label(new Point((Main.WIDTH / 3) - (portImg.getWidth() / 3), (Main.HEIGHT / 6 * 3) - (portImg.getHeight())), portImg);
			userText = new Label(new Point((Main.WIDTH / 3) - (userImg.getWidth() / 3), (Main.HEIGHT / 6 * 4) - (userImg.getHeight())), userImg);
			connect = new Button(new Point((0) + (connImg.getWidth() / 2), Main.HEIGHT / 6 * 5), connImg, connectHover);
			back = new Button(new Point((int) ((Main.WIDTH) - (backImg.getWidth() * 1.5)), Main.HEIGHT / 6 * 5), backImg, backHover);
		}
		catch(IOException e) { e.printStackTrace(); }
	}
	
	public void render(Graphics g) {
		connectToServerText.render(g);
		ipText.render(g);
		portText.render(g);
		userText.render(g);
		connect.render(g);
		back.render(g);
	}
	
	public Button getConnectButton() {
		return connect;
	}
	
	public Button getBackButton() {
		return back;
	}
	
}
