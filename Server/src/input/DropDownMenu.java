package input;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import main.Main;

public class DropDownMenu {

	ActionButton mainClick;
	ArrayList<ActionButton> buttons;
	boolean active; //Used to determine if the menu is extended
	ActionButton selected;
	
	public DropDownMenu(Rectangle box, String[] msgs, int defaultOption) {
		mainClick = new ActionButton(msgs[defaultOption], box, false);
		
		buttons = new ArrayList<ActionButton>();
		
		for(int i = 0; i < msgs.length; i++) buttons.add(new ActionButton(msgs[i], new Rectangle(box.x, box.y + (box.height * i), box.width, box.height), false));
		
		selected = buttons.get(defaultOption);
		
		active = false;
	}
	
	public void tick(Main m) {
		mainClick.updateText(selected.getText());
		mainClick.tick(m);
		
		if(mainClick.getClicked() && active == false) {
			active = true;
			return;
		}
		else if(active){
			boolean hoveringMenu = false;
			for(ActionButton ab : buttons) if(ab.hovering) hoveringMenu = true;
			if(mainClick.hovering) hoveringMenu = true;
			active = hoveringMenu;
		}
		
		if(active) for(ActionButton ab : buttons) ab.tick(m);
		
		for(ActionButton ab : buttons) {
			if(ab.getClicked()) {
				selected = ab;
				active = false;
			}
		}
		
	}
	
	public void render(Main m, Graphics g) {
		mainClick.render(m, g);
		
		if(active) for(ActionButton ab : buttons) ab.render(m, g);
	}
	
	public String getSelectedOption() {
		return selected.getText();
	}
	
	public boolean isExtended() {
		return active;
	}
	
	public int numOptions() {
		return buttons.size();
	}
}
