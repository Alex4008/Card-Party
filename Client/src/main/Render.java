package main;
import java.awt.Graphics;

import menus.ConnectionMenu;
import menus.MainMenu;
import menus.MultiMenu;
import menus.SingleMenu;
import menus.WarMenu;

public class Render {

	public static void rendering(Main main, Graphics g) {
		
		switch (Main.gs) {
		
		case MAIN_MENU:
			RenderMainMenu(main, g);
			return;
		
		case MAIN_SINGLEPLAYER:
			RenderSinglePlayerMenu(main, g);
			return;
			
		case MAIN_MULTIPLAYER:
			RenderMultiplayerMenu(main, g);
			return;
		
		case CONNECTION_SCREEN:
			RenderConnectionMenu(main, g);
			return;
			
		case SINGLEPLAYER_GAME:
			RenderSinglePlayerGame(main, g);
			return;
			
		case MULTIPLAYER_GAME:
			RenderSinglePlayerGame(main, g);
			return;
			
		case LOGGED_IN_SCREEN:
			//RenderLevelEdit(main, g);
			break;
			
		default:
			break;

		}
		
	}
	
	private static void RenderMainMenu(Main m, Graphics g) {
		MainMenu menu = m.getMainMenu();
		menu.render(g);
	}
	
	private static void RenderSinglePlayerMenu(Main m, Graphics g) {
		SingleMenu menu = m.getSinglePlayerMenu();
		menu.render(g);
	}
	
	private static void RenderConnectionMenu(Main m, Graphics g) {
		ConnectionMenu menu = m.getConnectionMenu();
		menu.render(g);
	}
	
	private static void RenderMultiplayerMenu(Main m, Graphics g) {
		MultiMenu menu = m.getMultiplayerMenu();
		menu.render(g);
	}
	
	private static void RenderSinglePlayerGame(Main m, Graphics g) {
		if(Main.currentGame == GameType.WAR) {
			m.getWarMenu().render(m, g);	
		}
		else if(Main.currentGame == GameType.UNO) {
			m.getUnoMenu().render(m, g);
		}
	}
	
}