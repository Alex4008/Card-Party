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

public class SingleMenu {
	
	Button war;
	Button uno;
	Button euchre;
	Button back;
	Label text;
	
	public SingleMenu() {
		BufferedImageLoader loader = new BufferedImageLoader();
		
		try {
			BufferedImage warImg = loader.loadImage("/singleMenuButtons/war.png");
			BufferedImage warHover = loader.loadImage("/singleMenuButtons/war-hover.png");
			BufferedImage unoImg = loader.loadImage("/singleMenuButtons/uno.png");
			BufferedImage unoHover = loader.loadImage("/singleMenuButtons/uno-hover.png");
			BufferedImage euchreImg = loader.loadImage("/singleMenuButtons/euchre.png");
			BufferedImage euchreHover = loader.loadImage("/singleMenuButtons/euchre-hover.png");
			BufferedImage backImg = loader.loadImage("/genericButtons/back.png");
			BufferedImage backHover = loader.loadImage("/genericButtons/back-hover.png");
			BufferedImage textImg = loader.loadImage("/genericButtons/gamesText.png");
			
			war = new Button(new Point((Main.WIDTH / 2) - (warImg.getWidth() / 2), (Main.HEIGHT / 6 * 2) - warImg.getHeight()), warImg, warHover);
			uno = new Button(new Point((Main.WIDTH / 2) - (unoImg.getWidth() / 2), (Main.HEIGHT / 6 * 3) - unoImg.getHeight()), unoImg, unoHover);
			euchre = new Button(new Point((Main.WIDTH / 2) - (euchreImg.getWidth() / 2), (Main.HEIGHT / 6 * 4) - euchreImg.getHeight()), euchreImg, euchreHover);
			back = new Button(new Point((Main.WIDTH / 2) - (backImg.getWidth() / 2), (Main.HEIGHT / 10 * 9) - backImg.getHeight()), backImg, backHover);
			text = new Label(new Point((Main.WIDTH / 2) - (textImg.getWidth() / 2), Main.HEIGHT / 5 - (textImg.getHeight())), textImg);
		}
		catch(IOException e) { e.printStackTrace(); }
	}
	
	public void render(Graphics g) {
		text.render(g);
		war.render(g);
		uno.render(g);
		euchre.render(g);
		back.render(g);
	}
	
	public Button getWarButton() {
		return war;
	}
	
	public Button getUnoButton() {
		return uno;
	}
	
	public Button getEuchreButton() {
		return euchre;
	}
	
	public Button getBackButton() {
		return back;
	}
	
}
