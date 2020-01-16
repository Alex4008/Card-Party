package graphics;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Main;

public class TextLabel {
	private Point loc;
	private String text;
	
	public TextLabel(Point location, String text) {
		this.loc = location;
		this.text = text;
	}
	
	
	public void render(Graphics g) {
		g.drawString(text, loc.x, loc.y + 30);
	}
	
	//Getters and Setters
	public Point getLocation() {
		return loc;
	}
	
}
