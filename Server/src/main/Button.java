package main;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Button {
	private Point location;
	private Rectangle box;
	private boolean hoverStatus;
	private BufferedImage image;
	private BufferedImage imageHover;
	
	public Button(Point location, String imagePath) {
		
		BufferedImageLoader loader = new BufferedImageLoader();
		
		try {
			image = loader.loadImage(imagePath + ".png");
			imageHover = loader.loadImage(imagePath + "-hover.png");
		}
		catch(IOException e) { e.printStackTrace(); }
		
		this.location = location;
		box = new Rectangle(location.x, location.y, image.getWidth(), image.getHeight());
	}
	
	//Maybe use this?
	public void render(Graphics g) {
		/*
		g.setFont(new Font("Arial", FONT_SIZE, FONT_SIZE));
		g.setColor(Color.BLACK);
		g.drawString(text, location.x, location.y);
		if(Main.devMode == true) g.drawRect(box.x, box.y, box.width, box.height);
		*/
		if(hoverStatus) {
			g.drawImage(imageHover, location.x, location.y, null);
		}
		else {
			g.drawImage(image, location.x, location.y, null);
		}
	}
	
	//Getters and Setters
	public Point getLocation() {
		return location;
	}
	
	public Rectangle getRectangle() {
		return box;
	}
	
	public void SetHover(boolean status) {
		hoverStatus = status;
	}
	
	public boolean GetHoverStatus() {
		return hoverStatus;
	}
	
}
