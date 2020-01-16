package input;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import graphics.Colors;
import main.Main;

public class CheckBox {

	Point loc;
	int buttonSize;
	ActionButton button;
	boolean checked; //Used to determine if the box is checked or not
	
	
	public CheckBox(Point loc, int buttonSize) {
		this.loc = loc;
		this.buttonSize = buttonSize;
		button = new ActionButton("", new Rectangle(loc.x, loc.y, buttonSize, buttonSize), false);
		checked = false;
	}
	
	public void tick(Main m) {
		button.tick(m);
		if(button.getClicked()) checked = !checked;
	}
	
	public void render(Main m, Graphics g) {
		button.render(m, g);
		Color currentColor = g.getColor();
		g.setColor(Colors.unoGreen);
		if(checked) g.fillRect(loc.x + 5, loc.y + 5, buttonSize - 10, buttonSize - 10);
		g.setColor(currentColor);
	}
	
	
	public boolean getChecked() {
		return checked;
	}
}
