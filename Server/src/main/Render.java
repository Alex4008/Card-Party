package main;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import menus.GameMenu;
import menus.HostingMenu;
import menus.SetupMenu;

public class Render {

	public static void rendering(Main main, Graphics g) {
		
		switch (main.gs) {
		
		case SERVER_SETUP:
			//Render main menu code here
			RenderServerSetup(main, g);
			return;
		
		case SERVER_MAIN:
			RenderServerMain(main, g);
			return;
			
		case WAR_GAME:
			RenderWarGame(main, g);
			return;
			
		default:
			break;

		}
		
	}
	
	private static void RenderServerSetup(Main m, Graphics g) {
		SetupMenu menu = m.getSetupMenu();
		
		menu.render(g);
	}
	
	private static void RenderServerMain(Main m, Graphics g) {
		HostingMenu menu = m.getHostingMenu();
		
		menu.render(m, g);
	}
	
	private static void RenderWarGame(Main m, Graphics g) {
		GameMenu menu = m.getGameMenu();
		
		menu.render(m, g);
	}
}