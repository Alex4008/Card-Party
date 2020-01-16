package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;

import main.Main;

public class SplashMsg {

	Rectangle box;
	Font font;
	Color color;
	Color borderColor;
	int duration;
	String msg;
	String msg2; // The secondary message.
	int tick;
	boolean fade;
	boolean expire;
	boolean hovering;
	boolean canDismiss; // Used to determine if the msg can be dismissed or not.
	
	public SplashMsg(String msg, String msg2, Rectangle box, Font font, Color color, int duration, boolean fade, boolean canDismiss) {
		this.msg = msg;
		if(msg2 == null) this.msg2 = "(Click to dismiss)";
		else this.msg2 = msg2;
		this.box = box;
		this.font = font;
		this.color = color;
		this.borderColor = Color.BLACK;
		this.duration = duration;
		tick = 0;
		expire = false;
		this.fade = fade;
		this.canDismiss = canDismiss;
		hovering = false;
	}
	
	public SplashMsg(String msg, Rectangle box, int duration, boolean fade) {
		this(msg, null, box, new Font("Arial", Font.BOLD, 20), Color.WHITE, duration, fade, true);
	}
	
	public SplashMsg(String msg, Rectangle box, int duration, boolean fade, boolean canDismiss) {
		this(msg, null, box, new Font("Arial", Font.BOLD, 20), Color.WHITE, duration, fade, canDismiss);
	}
	
	public SplashMsg(String msg, String msg2, Rectangle box, int duration, boolean fade) {
		this(msg, msg2, box, new Font("Arial", Font.BOLD, 20), Color.WHITE, duration, fade, true);
	}
	
	public void tick(Main m) {
		hovering = m.isHovering(box);
		int timeRemaining = duration - tick;
		
		if(!hovering) { //If the splash isn't being hovered over, tick and fade the image
			tick++;
			
			if(tick > duration) expire = true;
			
			if(fade) {
				if(timeRemaining < 63 && timeRemaining > 0) {
					color = new Color(color.getRed(), color.getGreen(), color.getBlue(), timeRemaining * 4);
					borderColor = new Color(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(), timeRemaining * 2);
				}
			}	
		}
		else { //If it is, tick it down to where fade would start, but stop the fade. Also prevent expiration
			if(fade) {
				if(timeRemaining > 63) tick++;
				color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
				borderColor = new Color(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(), 255);
			}
			
			if(m.getClicked() && canDismiss) {
				expire = true; //If you click the splash message, we expire it.
			}
		}
	}
	
	public void render(Main m, Graphics g) {
		if(!expire) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(2.0f));
			
			g2.setPaint(color);
	        g2.fill(new RoundRectangle2D.Double(box.x, box.y, (int) box.getWidth(), (int) box.getHeight(), 10, 10));
			
	        g2.setPaint(borderColor);
	        g2.draw(new RoundRectangle2D.Double(box.x, box.y, (int) box.getWidth(), (int) box.getHeight(), 10, 10));

			Font f = g.getFont();
			g.setFont(font);
			int width = g.getFontMetrics().stringWidth(msg);
			int emptySpace = (int) box.getWidth() - width;
			g.drawString(msg, box.x + (emptySpace / 2), box.y + (int) box.getHeight() / 2 + (font.getSize() / 2));
			
			g.setFont(new Font("Arial", Font.PLAIN, 12));
			width = g.getFontMetrics().stringWidth(msg2);
			emptySpace = (int) box.getWidth() - width;
			if(canDismiss) g.drawString(msg2, box.x + (emptySpace / 2), box.y + (int) box.getHeight() / 4 * 3 + (font.getSize() / 2));
			
			//Reset to previous values
			g.setFont(f);
		}
	}
	
	public boolean isExpired() {
		return expire;
	}
}
