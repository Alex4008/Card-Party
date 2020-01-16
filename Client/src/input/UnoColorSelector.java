package input;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;

import cards.UnoColor;
import graphics.Colors;
import main.Main;

public class UnoColorSelector {

	Rectangle mainBox;

	ActionButton red;
	ActionButton yellow;
	ActionButton green;
	ActionButton blue;
	
	UnoColor selectedColor;
	
	public UnoColorSelector(Rectangle mainBox) {
		this.mainBox = mainBox;

		red = new ActionButton("", new Rectangle(mainBox.x + 30, mainBox.y + (mainBox.height / 2), 30, 30), Colors.unoRed);
		yellow = new ActionButton("", new Rectangle(mainBox.x + (mainBox.width / 4 * 1) + 30, mainBox.y + (mainBox.height / 2), 30, 30), Colors.unoYellow);
		green = new ActionButton("", new Rectangle(mainBox.x + (mainBox.width / 4 * 2) + 30, mainBox.y + (mainBox.height / 2), 30, 30), Colors.unoGreen);
		blue = new ActionButton("", new Rectangle(mainBox.x + (mainBox.width / 4 * 3) + 30, mainBox.y + (mainBox.height / 2), 30, 30), Colors.unoBlue);
		
		selectedColor = UnoColor.NULL;
	}
	
	public void tick(Main m) {
		red.tick(m);
		yellow.tick(m);
		green.tick(m);
		blue.tick(m);
		
		if(red.getClicked()) selectedColor = UnoColor.RED;
		else if(yellow.getClicked()) selectedColor = UnoColor.YELLOW;
		else if(green.getClicked()) selectedColor = UnoColor.GREEN;
		else if(blue.getClicked()) selectedColor = UnoColor.BLUE;
	}
	
	public void render(Main m, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2.0f));
		
		//Draw rectangle
		g2.setPaint(Color.WHITE);
		g2.fill(new RoundRectangle2D.Double(mainBox.x, mainBox.y, (int) mainBox.getWidth(), (int) mainBox.getHeight(), 10, 10));

		//Draw border
        g2.setPaint(Color.black);
        g2.draw(new RoundRectangle2D.Double(mainBox.x, mainBox.y, (int) mainBox.getWidth(), (int) mainBox.getHeight(), 10, 10));
		
        //Draw text
		Font f = g.getFont();
		g.setFont(new Font("Arial", Font.BOLD, 20));
		int width = g.getFontMetrics().stringWidth("Select a color!");
		int emptySpace = (int) mainBox.getWidth() - width;
		g.drawString("Select a color!", mainBox.x + (emptySpace / 2), mainBox.y + (int) mainBox.getHeight() / 5 + (20 / 2));
		
		//Reset to previous value
		g.setFont(f);
		
		//Render action buttons
		red.render(m, g);
		yellow.render(m, g);
		green.render(m, g);
		blue.render(m, g);
	}
	
	public UnoColor getSelectedColor() {
		return selectedColor;
	}
	
	public void reset() {
		selectedColor = UnoColor.NULL;
	}
	
}
