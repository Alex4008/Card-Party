package menus;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import graphics.BufferedImageLoader;
import graphics.Label;
import input.Button;
import main.Main;

public class MainMenu {

	private Label title;
	private Button singlePlayer;
	private Button multiPlayer;
	private Button exitGame;
	private Label credit;
	
	public MainMenu() {
		BufferedImageLoader loader = new BufferedImageLoader();
		
		try {
			BufferedImage titleImg = loader.loadImage("/mainMenuButtons/title.png");
			BufferedImage single = loader.loadImage("/mainMenuButtons/singlePlayer.png");
			BufferedImage singleHover = loader.loadImage("/mainMenuButtons/singlePlayer-hover.png");
			BufferedImage multi = loader.loadImage("/mainMenuButtons/multiPlayer.png");
			BufferedImage multiHover = loader.loadImage("/mainMenuButtons/multiPlayer-hover.png");
			BufferedImage exit = loader.loadImage("/gameButtons/exit.png");
			BufferedImage exitHover = loader.loadImage("/gameButtons/exit-hover.png");
			BufferedImage creditImg = loader.loadImage("/mainMenuButtons/credit.png");
			title = new Label(new Point((Main.WIDTH / 2) - (titleImg.getWidth() / 2), (Main.HEIGHT / 5) - titleImg.getHeight()), titleImg);
			singlePlayer = new Button(new Point((Main.WIDTH / 2) - (single.getWidth() / 2), (Main.HEIGHT / 6 * 2) - single.getHeight()), single, singleHover);
			multiPlayer = new Button(new Point((Main.WIDTH / 2) - (multi.getWidth() / 2), (Main.HEIGHT / 6 * 3) - multi.getHeight()), multi, multiHover);
			exitGame = new Button(new Point((Main.WIDTH / 2) - (exit.getWidth() / 2), (Main.HEIGHT / 6 * 4) - exit.getHeight()), exit, exitHover);
			credit = new Label(new Point((Main.WIDTH / 2) - (creditImg.getWidth() / 2), Main.HEIGHT / 10 * 9 - (creditImg.getHeight())), creditImg);
		}
		catch(IOException e) { e.printStackTrace(); }
	}
	
	public void render(Graphics g) {
		title.render(g);
		singlePlayer.render(g);
		multiPlayer.render(g);
		exitGame.render(g);
		credit.render(g);
	}
	
	public Button getSinglePlayerButton() {
		return singlePlayer;
	}
	
	public Button getMultiplayerButton() {
		return multiPlayer;
	}
	
	public Button getExitButton() {
		return exitGame;
	}
}
