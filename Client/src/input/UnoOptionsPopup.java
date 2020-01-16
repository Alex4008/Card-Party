package input;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import graphics.TextLabel;
import main.Main;

public class UnoOptionsPopup {

	Rectangle box;
	DropDownMenu numberAI;
	DropDownMenu difficulty;
	DropDownMenu dealer;
	CheckBox allowDrawFourAnytime;
	CheckBox allowStackingDrawTwo;
	CheckBox drawUntilPlay;
	ActionButton submit;
	ActionButton cancel;
	
	//Text labels
	TextLabel title;
	TextLabel numberAILabel;
	TextLabel difficultyLabel;
	TextLabel dealerLabel;
	TextLabel drawFourLabel;
	TextLabel stackTwoLabel;
	TextLabel drawUntilPlayLabel;
	
	public UnoOptionsPopup(Point loc) {
		box = new Rectangle(loc.x, loc.y, 500, 500);
		numberAI = new DropDownMenu(new Rectangle(box.x + (int) (box.getWidth() / 3 * 2), box.y + 50, 150, 40), new String[]{"1", "2", "3"}, 2);
		difficulty = new DropDownMenu(new Rectangle(box.x + (int) (box.getWidth() / 3 * 2), box.y + 125, 150, 40), new String[]{"Easy", "Medium", "Hard"}, 1);
		dealer = new DropDownMenu(new Rectangle(box.x + (int) (box.getWidth() / 3 * 2), box.y + 200, 150, 40), new String[]{"Random", "0", "1", "2", "3"}, 0);
		allowDrawFourAnytime = new CheckBox(new Point(box.x + (int) (box.getWidth() / 3 * 2) + 60, box.y + 275), 30);
		allowStackingDrawTwo = new CheckBox(new Point(box.x + (int) (box.getWidth() / 3 * 2) + 60, box.y + 325), 30);
		drawUntilPlay = new CheckBox(new Point(box.x + (int) (box.getWidth() / 3 * 2) + 60, box.y + 375), 30);
		submit = new ActionButton("Play", new Rectangle(box.x  + (int) (box.getWidth() / 2) - 150, box.y + 440, 100, 50), false);
		cancel = new ActionButton("Back", new Rectangle(box.x + (int) (box.getWidth() / 2) + 50, box.y + 440, 100, 50), false);
		
		title = new TextLabel(new Point(loc.x + 200, loc.y), "Uno Options");
		numberAILabel = new TextLabel(new Point(loc.x + 20, loc.y + 50), "Number of AI: ");
		difficultyLabel = new TextLabel(new Point(loc.x + 20, loc.y + 125), "Difficulty: ");
		dealerLabel = new TextLabel(new Point(loc.x + 20, loc.y + 200), "Select First Player: ");
		drawFourLabel = new TextLabel(new Point(loc.x + 20, loc.y + 265), "Allow Draw Four at Anytime: ");
		stackTwoLabel = new TextLabel(new Point(loc.x + 20, loc.y + 315), "Allow Stacking Draw Two's: ");
		drawUntilPlayLabel = new TextLabel(new Point(loc.x + 20, loc.y + 365), "Enable Draw Until Play: ");
	}
	
	public void tick(Main m) {
		if(!dealer.isExtended()) {
			allowDrawFourAnytime.tick(m);
			allowStackingDrawTwo.tick(m);
			drawUntilPlay.tick(m);	
		}
		
		if(!difficulty.isExtended()) dealer.tick(m);
		if(!numberAI.isExtended()) difficulty.tick(m);
		numberAI.tick(m);
		
		submit.tick(m);
		cancel.tick(m);
		
		int selected;
		
		if(numberAI.getSelectedOption().equals("Random")) selected = -1;
		else selected = Integer.parseInt(numberAI.getSelectedOption());
		
		if(dealer.numOptions() != selected + 2) {
			switch(selected) {
			case 1:
				dealer = new DropDownMenu(new Rectangle(box.x + (int) (box.getWidth() / 3 * 2), box.y + 200, 150, 40), new String[]{"Random", "0", "1"}, 0);
				break;
			case 2:
				dealer = new DropDownMenu(new Rectangle(box.x + (int) (box.getWidth() / 3 * 2), box.y + 200, 150, 40), new String[]{"Random", "0", "1", "2"}, 0);
				break;
			case 3:
				dealer = new DropDownMenu(new Rectangle(box.x + (int) (box.getWidth() / 3 * 2), box.y + 200, 150, 40), new String[]{"Random", "0", "1", "2", "3"}, 0);
				break;	
			default:
				break;
			}
			
		}
	}
	
	public void render(Main m, Graphics g) {
		
		Color currentColor = g.getColor();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(box.x, box.y, box.width, box.height);
		
		g.setColor(Color.BLACK);
		g.drawRect(box.x, box.y, box.width, box.height);
		
		g.setColor(currentColor);
		
		allowDrawFourAnytime.render(m, g);
		allowStackingDrawTwo.render(m, g);
		drawUntilPlay.render(m, g);
		
		dealer.render(m, g);
		difficulty.render(m, g);
		numberAI.render(m, g);
		
		submit.render(m, g);
		cancel.render(m, g);
		
		title.render(g);
		numberAILabel.render(g);
		difficultyLabel.render(g);
		dealerLabel.render(g);
		drawFourLabel.render(g);
		stackTwoLabel.render(g);
		drawUntilPlayLabel.render(g);
	}
	
	public boolean isComplete() {
		return submit.getClicked();
	}
	
	public boolean isCanceled() {
		return cancel.getClicked();
	}
	
	public int getNumAI() {
		return Integer.parseInt(numberAI.getSelectedOption());
	}
	
	public int getDifficulty() {
		if(difficulty.getSelectedOption().equals("Easy")) return 0;
		else if(difficulty.getSelectedOption().equals("Normal")) return 1;
		else if(difficulty.getSelectedOption().equals("Hard")) return 2;
		else return -1;
	}
	
	public int getDealer() {
		if(dealer.getSelectedOption().equals("Random")) return -1;
		else return Integer.parseInt(dealer.getSelectedOption());
	}
	
	public boolean getDrawFourRule() {
		return allowDrawFourAnytime.getChecked();
	}
	
	public boolean getStackingRule() {
		return allowStackingDrawTwo.getChecked();
	}
	
	public boolean getDrawRule() {
		return drawUntilPlay.getChecked();
	}
	
}
