package input;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Main;

public class Clickable {
	private Point location;
	private Rectangle box;
	private boolean hoverStatus;
	private BufferedImage image;
	public boolean isClicked;
	
	public Clickable(Point location, BufferedImage image) {
		this.location = location;
		this.image = image;
		box = new Rectangle(location.x, location.y, image.getWidth(), image.getHeight());
		isClicked = false;
	}
	
	public void render(Graphics g) {
		g.drawImage(image, location.x, location.y, null);
	}
	
	public void tick(Main m) {
		if(m.getMousePosition() == null) return;
		
		try {
			//Detect hovers
			if(box.contains(m.getMousePosition()) == true) {
				SetHover(true);
			}
			else {
				SetHover(false);
			}
		}
		catch(NullPointerException e){
			System.out.println("Null caught");
		}

		if(m.getClicked() == true) {
			if(GetHoverStatus() == true) {
				isClicked = true;
			} else {
				isClicked = false;
			}
		}
	}
	
	public void setClicked(boolean value) {
		isClicked = value;
	}
	
	public boolean getClicked() {
		return isClicked;
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
