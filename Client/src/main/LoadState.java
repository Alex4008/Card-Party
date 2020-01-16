package main;

import javax.swing.JOptionPane;

import menus.ConnectionMenu;
import menus.MainMenu;
import menus.MultiMenu;
import menus.SingleMenu;
import menus.UnoMenu;
import menus.WarMenu;

//This class is used to load the start of a game state. Load whatever that state needs to have loaded before moving on to rendering and ticking
public class LoadState {

	public static void Load(Main m, GameState state) {
		
		m.SetAllTextFieldsFalse(); //Reset all textfields

		switch (state) {
		
		case MAIN_MENU:
			LoadMainMenu(m);
			return;
			
		case MAIN_SINGLEPLAYER:
			LoadSinglePlayerMenu(m);
			return;
		
		case CONNECTION_SCREEN:
			LoadConnectionScreen(m);
			return;
					
		case MAIN_MULTIPLAYER:
			LoadMultiplayerMenu(m);
			return;
			
		case SINGLEPLAYER_GAME:
			LoadSinglePlayerGame(m);
			break;
			
		case MULTIPLAYER_GAME:
			LoadMultiPlayerGame(m);
			break;
			
		default:
			break;
			
		}
		
	}
	
	private static void LoadMainMenu(Main m)  {
		m.currentGame = null;
		m.mainMenu = new MainMenu();
		Main.gs = GameState.MAIN_MENU;
	}
	
	private static void LoadSinglePlayerMenu(Main m) {
		m.singleMenu = new SingleMenu();
		Main.gs = GameState.MAIN_SINGLEPLAYER;
	}
	
	private static void LoadConnectionScreen(Main m) {
		m.connectionMenu = new ConnectionMenu();
		Main.gs = GameState.CONNECTION_SCREEN;
		Main.theThread = null;
		Main.theClient = null;
		m.SetAllTextFieldsTrue();
	}
	
	private static void LoadMultiplayerMenu(Main m) {
		if(Main.gs == GameState.MULTIPLAYER_GAME) { //If we are in a game we are just returning to the menu, no need for seting up client server connection
			m.multiMenu = new MultiMenu();
			Main.gs = GameState.MAIN_MULTIPLAYER;
		}
		else {
			
			if(!Main.userNameField.getText().isEmpty()) {
				Main.userName = Main.userNameField.getText();
				if(Main.userName.length() > 16) Main.userName = Main.userName.substring(0, 15);
				
				if(Main.userName.trim().length() == 0) {
					Main.userNameField.setText("Player");
					Main.userName = Main.userNameField.getText();
				}
				
				if(Main.userName.contains(":") || Main.userName.contains(",")) {
					JOptionPane.showMessageDialog(Main.frame, "Player username cannot contain any illegal characters! (',' ':')", "Error", JOptionPane.ERROR_MESSAGE);
					LoadState.Load(m, GameState.CONNECTION_SCREEN);
					return;
				}
			}
			else {
				Main.userNameField.setText("Player");
				Main.userName = Main.userNameField.getText();
			}
			
			if(!Main.portField.getText().isEmpty()) {
				try {
					Main.port = Integer.parseInt(Main.portField.getText());
					if(Main.port < 1024 || Main.port > 65535) {
						throw new NumberFormatException("Invalid Number");
					}
				}
				catch(NumberFormatException e) {
					JOptionPane.showMessageDialog(Main.frame, "Server Port Must Be Between 1024 and 65535!", "Error", JOptionPane.ERROR_MESSAGE);
					LoadState.Load(m, GameState.CONNECTION_SCREEN);
					return;
				}
			}
			else {
				Main.portField.setText("5056");
				Main.port = Integer.parseInt(Main.portField.getText());
			}
			
			
			if(!Main.ipField.getText().isEmpty()) {
				Main.ip = Main.ipField.getText();
			}
			else {
				JOptionPane.showMessageDialog(Main.frame, "You did not enter an IP address, trying to connect to localhost.", Main.TITLE, JOptionPane.INFORMATION_MESSAGE);
				Main.ipField.setText("localhost");
				Main.ip = "localhost";
			}
			
			if(m.StartConnection()) { //New connection
				m.multiMenu = new MultiMenu();
				System.out.println("Changing the menu!");
				//No need to change gamestate here. The Client class will do that if the connection is successful.	
			}	
			else {
				System.out.println("Something is really broken here");
			}
		}
	}
	
	private static void LoadSinglePlayerGame(Main m) {
		Main.gs = GameState.SINGLEPLAYER_GAME;
		if(Main.currentGame == GameType.WAR) {
			Object[] possibilities = {"1", "2", "3"};
			String s = (String) JOptionPane.showInputDialog(
			                    Main.frame,
			                    "How Many AI?",
			                    "War Options",
			                    JOptionPane.PLAIN_MESSAGE,
			                    null,
			                    possibilities,
			                    "1");
			try {
				int value = Integer.parseInt(s) + 1;
				m.warMenu = new WarMenu(m, value);
			}
			catch(NumberFormatException e) { //If they clicked cancel return.
				LoadState.Load(m, GameState.MAIN_SINGLEPLAYER);
			}
		}
		else if(Main.currentGame == GameType.UNO) {
			m.unoMenu = new UnoMenu(m, 4);
		}
	}
	
	private static void LoadMultiPlayerGame(Main m) {
		m.warMenu = new WarMenu(m, m.getClient());
		Main.gs = GameState.MULTIPLAYER_GAME;
	}
	
}