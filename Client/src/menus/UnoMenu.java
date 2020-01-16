package menus;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import input.Button;
import input.UnoOptionsPopup;
import main.GameState;
import main.LoadState;
import main.Main;
import singlePlayerUno.LocalUno;

public class UnoMenu {

	Main m;
	
	Button exit;
	
	LocalUno game;
	
	UnoOptionsPopup options;
	
	public UnoMenu(Main m, int numPlayers) {
		this.m = m;
		BufferedImage exitImg = m.loadImage("/gameButtons/exit.png");
		exit = new Button(new Point((Main.WIDTH - (exitImg.getWidth() / 2)) - (exitImg.getWidth() / 2), Main.HEIGHT / 10 * 9), exitImg, m.loadImage("/gameButtons/exit-hover.png"));
		options = new UnoOptionsPopup(new Point(Main.WIDTH / 2 - 250, Main.HEIGHT / 2 - 250));
	}
	
	public void render(Main m, Graphics g) {
		exit.render(g);
		while(options != null) {
			options.render(m, g);
			return;
		}
		game.render(m, g);
	}
	
	public void tick(Main m) {
		while(options != null) {
			options.tick(m);
			if(options.isComplete()) startGame();
			else if(options.isCanceled()) LoadState.Load(m, GameState.MAIN_MENU);
			return;
		}
		game.tick(m);
	}
	
	public Button getExitButton() {
		return exit;
	}
	
	private void startGame() {
		game = new LocalUno(m, options.getNumAI() + 1, options.getDifficulty(), options.getDealer(), options.getDrawFourRule(), options.getStackingRule(), options.getDrawRule(), null);
		options = null;
	}
}
