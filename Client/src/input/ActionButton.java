package input;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;

import graphics.Colors;
import main.Main;

public class ActionButton {
	Rectangle box;
	Font font;
	Color color;
	Color borderColor;
	String msg;
	boolean hovering;
	boolean clicked;
	boolean rounded;
	
	public ActionButton(String msg, Rectangle box, Font font, Color color, boolean rounded) {
		this.msg = msg;
		this.box = box;
		this.font = font;
		this.color = color;
		this.borderColor = Color.BLACK;
		this.rounded = rounded;
		
		hovering = false;
		clicked = false;
	}
	
	public ActionButton(String msg, Rectangle box) {
		this(msg, box, new Font("Arial", Font.BOLD, 20), Color.WHITE, true);
	}
	
	public ActionButton(String msg, Rectangle box, boolean rounded) {
		this(msg, box, new Font("Arial", Font.BOLD, 20), Color.WHITE, rounded);
	}
	
	public ActionButton(String msg, Rectangle box, Color color) {
		this(msg, box, new Font("Arial", Font.BOLD, 20), color, true);
	}
	
	public void tick(Main m) {
		hovering = m.isHovering(box);
		if(hovering)
			clicked = m.getClicked();
		else
			clicked = false;
	}
	
	public void render(Main m, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2.0f));
		
		if(hovering) { //Add tint to the color
			if(color != Color.WHITE) g2.setPaint(Colors.tintColor(color, 0.25));
			else g2.setPaint(Colors.shadeColor(color, 0.25));
		}
		else g2.setPaint(color);
		
		if(rounded) {
	        g2.fill(new RoundRectangle2D.Double(box.x, box.y, (int) box.getWidth(), (int) box.getHeight(), 10, 10));
			
	        g2.setPaint(borderColor);
	        g2.draw(new RoundRectangle2D.Double(box.x, box.y, (int) box.getWidth(), (int) box.getHeight(), 10, 10));	
		}
		else {
	        g2.fill(new Rectangle(box.x, box.y, (int) box.getWidth(), (int) box.getHeight()));
			
	        g2.setPaint(borderColor);
	        g2.draw(new Rectangle(box.x, box.y, (int) box.getWidth(), (int) box.getHeight()));
		}

		Font f = g.getFont();
		g.setFont(font);
		int width = g.getFontMetrics().stringWidth(msg);
		int emptySpace = (int) box.getWidth() - width;
		g.drawString(msg, box.x + (emptySpace / 2), box.y + (int) box.getHeight() / 2 + (font.getSize() / 2));
		
		//Reset to previous values
		g.setFont(f);
	}
	
	public boolean getClicked() {
		return clicked;
	}
	
	public void updateText(String value) {
		msg = value;
	}
	
	public String getText() {
		return msg;
	}
}
