package input;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Button {
	private Point location;
	private Rectangle box;
	private boolean hoverStatus;
	private BufferedImage image;
	private BufferedImage imageHover;
	
	public Button(Point location, BufferedImage image, BufferedImage imageHover) {
		this.location = location;
		this.image = image;
		this.imageHover = imageHover;
		box = new Rectangle(location.x, location.y, image.getWidth(), image.getHeight());
	}
	
	public void render(Graphics g) {
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
