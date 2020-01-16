package cards;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Main;

public class Card {

	private Suit suit;
	private Value value;
	private BufferedImage image;
	
	private UnoColor unoColor;
	private UnoValue unoValue;
	private Point loc;
	private Rectangle box;
	private boolean hover;
	private boolean hasPriority; //Used for uno hand
	
	public Card(Main m, Suit suit, Value value) {
		this.suit = suit;
		this.value = value;
		image = m.getSprites().getSprite(suit, value);
		loc = null;
		box = null;
		hover = false;
		hasPriority = false;
	}

	public Card(Main m, Point loc, UnoColor unoColor, UnoValue unoValue) {
		this.loc = loc;
		if(loc != null) this.box = new Rectangle(loc.x, loc.y, image.getWidth(), image.getHeight());
		else box = null;
		this.unoColor = unoColor;
		this.unoValue = unoValue;
		image = m.getSprites().getUnoSprite(unoColor, unoValue);
		hover = false;
		hasPriority = false;
	}

	public void tick(Main m) {
		if(box == null) return;
		
		if(m.isHovering(box)) hover = true;
		else {
			hasPriority = false;
			hover = false;
		}
	}
	
	public void render(Main m, Graphics g) {
		if(loc != null) g.drawImage(image, loc.x, loc.y, null);
	}
	
	public Suit getSuit() {
		return suit;
	}
	
	public Value getValue() {
		return value;
	}
	
	public UnoColor getUnoColor() {
		return unoColor;
	}
	
	public UnoValue getUnoValue() {
		return unoValue;
	}
	
	public BufferedImage getImage() {
		return image;
	}	
	
	public Point getLocation() {
		if(loc != null) return loc;
		else return null;
	}
	
	public Point getCenter() {
		if(loc == null) return null;
		else return new Point(image.getWidth() / 2, image.getHeight() / 2);
	}
	
	public void setLocation(Point value) {
		loc = value;
	}
	
	public Rectangle getBoundingBox() {
		return box;
	}
	
	public void updateBoundingBox(Point p) {
		if(box == null) box = new Rectangle(p.x, p.y, image.getWidth(), image.getHeight());
		else box.setBounds(p.x, p.y, image.getWidth(), image.getHeight());
	}
	
	public boolean isHover() {
		return hover;
	}
	
	public void setPriority(boolean value) {
		hasPriority = value;
	}
	
	public boolean getPriority() {
		return hasPriority;
	}
}
