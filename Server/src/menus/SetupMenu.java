package menus;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import main.BufferedImageLoader;
import main.Button;
import main.Label;
import main.Main;

public class SetupMenu {
	private Label titleText;
	private Label serverPortText;
	private Label maxPlayersText;
	private Button hostServer;
	private Label creditText;
	
	public SetupMenu() {
		BufferedImageLoader loader = new BufferedImageLoader();
		
		try {
			BufferedImage titleImg = loader.loadImage("/setupMenuButtons/title.png");
			BufferedImage serverPortImg = loader.loadImage("/setupMenuButtons/portText.png");
			BufferedImage maxPlayersImg = loader.loadImage("/setupMenuButtons/maxPlayersText.png");
			BufferedImage creditImg = loader.loadImage("/setupMenuButtons/credit.png");
			
			titleText = new Label(new Point((Main.WIDTH / 2) - (titleImg.getWidth() / 2), (Main.HEIGHT / 6 * 2) - (int) (titleImg.getHeight() * 1.5)), titleImg);
			serverPortText = new Label(new Point((Main.WIDTH / 3) - (serverPortImg.getWidth() / 2), (Main.HEIGHT / 6 * 2) - (serverPortImg.getHeight())), serverPortImg);
			maxPlayersText = new Label(new Point((Main.WIDTH / 3) - (maxPlayersImg.getWidth() / 2), (Main.HEIGHT / 6 * 3) - (maxPlayersImg.getHeight())), maxPlayersImg);
			hostServer = new Button(new Point((int) ((Main.WIDTH / 2) - (Main.BUTTON_WIDTH / 2)), Main.HEIGHT / 6 * 4), "/setupMenuButtons/hostServer");
			creditText = new Label(new Point((Main.WIDTH / 2) - (creditImg.getWidth() / 2), (Main.HEIGHT / 6 * 6) - (creditImg.getHeight())), creditImg);
		}
		catch(IOException e) { e.printStackTrace(); }
		
	}
	
	public void render(Graphics g) {
		titleText.render(g);
		serverPortText.render(g);
		maxPlayersText.render(g);
		hostServer.render(g);
		creditText.render(g);
	}
	
	public Button getHostServerButton() {
		return hostServer;
	}
}
