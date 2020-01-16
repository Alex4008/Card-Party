package graphics;

import java.awt.Color;
import java.awt.Graphics;

import main.GameType;
import main.Main;
import singlePlayerUno.LocalPlayerUno;
import singlePlayerUno.LocalUno;

public class PlayerToken {

	GameType gt;
	int location;
	LocalUno game;
	
	public PlayerToken(GameType gt, LocalUno game, int location) {
		this.gt = gt;
		this.game = game;
		this.location = location;
	}
	
	public PlayerToken(GameType gt) {
		this(gt, null, 0);
	}
	
	public void tick(Main m) {
		
	}
	
	public void render(Main m, Graphics g) {
		if(location < 0 || location > 3) return;
		
		if(gt.equals(GameType.WAR)) {
			//TODO: Add player token to war?
		}
		else if(gt.equals(GameType.UNO)) {
			Color currentColor = g.getColor();
			g.setColor(Colors.tintColor(Colors.unoYellow, 0.25));
			LocalPlayerUno p = game.getPlayerByID(location);
			switch(location) {
			case 0:
				g.drawRoundRect(p.getHandLocation().x - 205, p.getHandLocation().y - 5, p.getCurrentHandWidth() + 10, Main.unoCardBack.getHeight() + 10, 20, 20);
				break;
			case 1:
				g.drawRoundRect(p.getHandLocation().x + 15, p.getHandLocation().y - 185, Main.unoCardBack.getHeight() + 10, p.getCurrentHandWidth() + 10, 20, 20);
				break;
			case 2:
				g.drawRoundRect(p.getHandLocation().x - 205, p.getHandLocation().y + 40, p.getCurrentHandWidth() + 10, Main.unoCardBack.getHeight() + 10, 20, 20);
				break;
			case 3:
				g.drawRoundRect(p.getHandLocation().x + 15, p.getHandLocation().y - 185, Main.unoCardBack.getHeight() + 10, p.getCurrentHandWidth() + 10, 20, 20);
				break;
			default:
				return;
			}
			g.setColor(currentColor);
		}
		else if(gt.equals(GameType.EUCHRE)) {
			
		}
	}
	
	public void setLocation(int location) {
		this.location = location;
	}
	
	public int getLocation() {
		return location;
	}
	
	public void nextPos(boolean clockwise, int numPlayers) {
		if(clockwise) {
			if(location == numPlayers - 1) location = 0;
			else location++;
		}
		else {
			if(location == 0) location = numPlayers - 1;
			else location--;
		}
	}
}
