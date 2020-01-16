package main;

import javax.swing.JOptionPane;

import games.War;
import menus.GameMenu;
import menus.HostingMenu;
import menus.SetupMenu;

public class LoadState {

	public static void Load(Main m, GameState state) {
		
		m.SetAllTextFieldsFalse(); //Reset all textfields
		switch (state) {
		
		case SERVER_SETUP:
			LoadServerSetup(m);
			return;
			
		case SERVER_MAIN:
			if(Main.serverOn) {
				LoadServerFromGame(m);
			}
			else {
				LoadServerMainStartUp(m);	
			}
			return;
			
		case WAR_GAME:
			LoadWarGame(m);
			return;
			
		case UNO_GAME:
			LoadUnoGame(m);
			return;
			
		default:
			break;
			
		}
		
	}
	
	private static void LoadServerFromGame(Main m) {
		m.hostingMenu = new HostingMenu();
		Main.theServer.gameRunning = false;
		Main.gs = GameState.SERVER_MAIN;
	}
	
	private static void LoadUnoGame(Main m) {
		Main.theServer.inGameStartUp = true;
		m.gameMenu = new GameMenu(m);
		
		//Add AI
		for(int i = 0; i < Main.numberAI; i++) {
            // create a new thread object 
            ClientHandler ch = new ClientHandler(m, "AI " + (i + 1));
            Thread t = ch;
            Main.addClient(ch);
            // Invoking the start() method 
            t.start(); 
		}
		
		for(ClientHandler c : Main.getClients()) {
			c.startGame();
		}
		Main.gs = GameState.UNO_GAME;
		Main.theServer.startRunningGame();
	}
	
	private static void LoadWarGame(Main m) {
		Main.theServer.inGameStartUp = true;
		m.gameMenu = new GameMenu(m);
		
		//Add AI
		for(int i = 0; i < Main.numberAI; i++) {
            // create a new thread object 
            ClientHandler ch = new ClientHandler(m, "AI " + (i + 1));
            Thread t = ch;
            Main.addClient(ch);
            // Invoking the start() method 
            t.start(); 
		}
		
		for(ClientHandler c : Main.getClients()) {
			c.startGame();
		}
		Main.gs = GameState.WAR_GAME;
		Main.theServer.startRunningGame();
		
	}
	
	private static void LoadServerSetup(Main m)  {
		m.setupMenu = new SetupMenu();
		Main.gs = GameState.SERVER_SETUP;
		m.SetAllTextFieldsTrue();
	}
	
	private static void LoadServerMainStartUp(Main m) {
		
		if(!Main.portField.getText().isEmpty()) {
			try {
				Main.port = Integer.parseInt(Main.portField.getText());
				if(Main.port < 1024 || Main.port > 65535) {
					throw new NumberFormatException("Invalid Number");
				}
			}
			catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(Main.frame, "Server Port Must Be Between 1024 and 65535!", "Error", JOptionPane.ERROR_MESSAGE);
				LoadState.Load(m, GameState.SERVER_SETUP);
				return;
			}
		}
		else {
			Main.port = 5056;
		}
		
		if(!Main.maxPlayersField.getText().isEmpty()) {
			try {
				Main.maxPlayers = Integer.parseInt(Main.maxPlayersField.getText());
				if(Main.maxPlayers < 2 || Main.maxPlayers > 4) {
					throw new NumberFormatException("Invalid Number");
				}
			}
			catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(Main.frame, "Max Players Must Be Between 2 and 4!", "Error", JOptionPane.ERROR_MESSAGE);
				LoadState.Load(m, GameState.SERVER_SETUP);
				return;
			}
		}
		else {
			Main.maxPlayers = 4;
		}
		
		m.hostingMenu = new HostingMenu();
		Main.gs = GameState.SERVER_MAIN;
		m.startServer();
	}
	
}
