package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;

import cards.UnoColor;
import main.Main;

public class UnoColorIndicator {

	Rectangle upper;
	Rectangle lower;
	
	UnoColor setColor;
	
	private Color color;
	
	public UnoColorIndicator(Point upperLoc, Point lowerLoc) {
		upper = new Rectangle(upperLoc.x, upperLoc.y, 200, 10);
		lower = new Rectangle(lowerLoc.x, lowerLoc.y, 200, 10);
		setColor = UnoColor.NULL;
	}
	
	public void tick(Main m) {
		if(setColor == UnoColor.RED) color = Colors.unoRed;
		else if(setColor == UnoColor.YELLOW) color = Colors.unoYellow;
		else if(setColor == UnoColor.GREEN) color = Colors.unoGreen;
		else if(setColor == UnoColor.BLUE) color = Colors.unoBlue;
	}
	
	public void render(Main m, Graphics g) {
		if(setColor != UnoColor.NULL) {
			Graphics2D g2 = (Graphics2D) g;
			
			g2.setStroke(new BasicStroke(2.0f));
			g2.setPaint(color);
			
	        g2.fill(new RoundRectangle2D.Double(upper.x, upper.y, (int) upper.getWidth(), (int) upper.getHeight(), 10, 10));
	        g2.fill(new RoundRectangle2D.Double(lower.x, lower.y, (int) lower.getWidth(), (int) lower.getHeight(), 10, 10));
			
	        g2.setPaint(Color.BLACK);
	        g2.draw(new RoundRectangle2D.Double(upper.x, upper.y, (int) upper.getWidth(), (int) upper.getHeight(), 10, 10));
	        g2.draw(new RoundRectangle2D.Double(lower.x, lower.y, (int) lower.getWidth(), (int) lower.getHeight(), 10, 10));
		}
	}
	
	public void setColor(UnoColor c) {
		setColor = c;
	}
}
