package main;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Label {
	private Point location;
	private Rectangle box;
	private BufferedImage image;
	
	public Label(Point location, BufferedImage image) {
		this.location = location;
		this.image = image;
		box = new Rectangle(location.x, location.y, image.getWidth(), image.getHeight());
	}
	
	public void render(Graphics g) {
		g.drawImage(image, location.x, location.y, null);
	}
	
	//Getters and Setters
	public Point getLocation() {
		return location;
	}
	
	public Rectangle getRectangle() {
		return box;
	}
	
}
