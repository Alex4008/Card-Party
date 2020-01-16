package main;

import javax.swing.JOptionPane;
import javax.swing.text.Style;

public class Tick {
public static void ticking(Main main) {
		
		switch (main.gs) {
		
		case SERVER_SETUP:
			//Render main menu code here
			TickServerSetup(main);
			return;
		
		case SERVER_MAIN:
			TickServerMain(main);
			return;
			
		case WAR_GAME:
			TickWarGame(main);
			return;
			
		default:
			break;
		}
	}
	
	private static void TickWarGame(Main m) {
		m.getGameMenu().tick(m);
		//Only process mouse activity if the mouse is on screen
		if(m.getMousePosition() == null) return;
		
		try {
			//Detect hovers
			if(m.getGameMenu().getShutdownServerButton().getRectangle().contains(m.getMousePosition()) == true) {
				m.getGameMenu().getShutdownServerButton().SetHover(true);
			}
			else {
				m.getGameMenu().getShutdownServerButton().SetHover(false);
			}
			
			if(m.getGameMenu().getKickPlayerButton().getRectangle().contains(m.getMousePosition()) == true) {
				m.getGameMenu().getKickPlayerButton().SetHover(true);
			}
			else {
				m.getGameMenu().getKickPlayerButton().SetHover(false);
			}
			
			if(m.getGameMenu().getEndGameButton().getRectangle().contains(m.getMousePosition()) == true) {
				m.getGameMenu().getEndGameButton().SetHover(true);
			}
			else {
				m.getGameMenu().getEndGameButton().SetHover(false);
			}
		}
		catch(NullPointerException e){
			System.out.println("Null caught");
		}
		
		
		if(m.getClicked() == true) {
			if(m.getGameMenu().getShutdownServerButton().GetHoverStatus() == true) {
				LoadState.Load(m, GameState.SERVER_SETUP);
				m.closeServer();
				System.out.println("Shutting down server");
			}
			else if(m.getGameMenu().getKickPlayerButton().GetHoverStatus() == true) {
				String[] options = {"<Empty Slot>", "<Empty Slot>", "<Empty Slot>", "<Empty Slot>"};
				int i = 0;
				for(ClientHandler ch : Main.clients) {
					options[i] = ch.getPlayer().getUserName();
					i++;
				}
				
				String choice = String.valueOf(JOptionPane.showInputDialog(
				                    Main.frame,
				                    "Select A Player:",
				                    "Kick Player",
				                    JOptionPane.PLAIN_MESSAGE,
				                    null,
				                    options,
				                    null));
				m.kickPlayer(choice);
				
			}
			else if(m.getGameMenu().getEndGameButton().GetHoverStatus() == true) {
				m.getGameMenu().getWarGame().setForceEndGame(true);
			}
			
			m.setClicked(false);
		}
	}

	private static void TickServerSetup(Main m) {
		//Only process mouse activity if the mouse is on screen
		if(m.getMousePosition() == null) return;
		
		try {
			//Detect hovers
			if(m.getSetupMenu().getHostServerButton().getRectangle().contains(m.getMousePosition()) == true) {
				m.getSetupMenu().getHostServerButton().SetHover(true);
			}
			else {
				m.getSetupMenu().getHostServerButton().SetHover(false);
			}
		}
		catch(NullPointerException e){
			System.out.println("Null caught");
		}
		
		
		if(m.getClicked() == true) {
			if(m.getSetupMenu().getHostServerButton().GetHoverStatus() == true) {
				LoadState.Load(m, GameState.SERVER_MAIN);
			}
			
			m.setClicked(false);
		}
	}
	
	private static void TickServerMain(Main m) {
		//Only process mouse activity if the mouse is on screen
		if(m.getMousePosition() == null) return;
		
		try {
			//Detect hovers
			if(m.getHostingMenu().getShutdownServerButton().getRectangle().contains(m.getMousePosition()) == true) {
				m.getHostingMenu().getShutdownServerButton().SetHover(true);
			}
			else {
				m.getHostingMenu().getShutdownServerButton().SetHover(false);
			}
			
			if(m.getHostingMenu().getKickPlayerButton().getRectangle().contains(m.getMousePosition()) == true) {
				m.getHostingMenu().getKickPlayerButton().SetHover(true);
			}
			else {
				m.getHostingMenu().getKickPlayerButton().SetHover(false);
			}
			
			if(m.getHostingMenu().getChangeGameButton().getRectangle().contains(m.getMousePosition()) == true) {
				m.getHostingMenu().getChangeGameButton().SetHover(true);
			}
			else {
				m.getHostingMenu().getChangeGameButton().SetHover(false);
			}
			
			if(m.getHostingMenu().getStartGameButton().getRectangle().contains(m.getMousePosition()) == true) {
				m.getHostingMenu().getStartGameButton().SetHover(true);
			}
			else {
				m.getHostingMenu().getStartGameButton().SetHover(false);
			}
		}
		catch(NullPointerException e){
			System.out.println("Null caught");
		}
		
		
		if(m.getClicked() == true) {
			if(m.getHostingMenu().getShutdownServerButton().GetHoverStatus() == true) {
				LoadState.Load(m, GameState.SERVER_SETUP);
				m.closeServer();
				System.out.println("Shutting down server");
			}
			else if(m.getHostingMenu().getKickPlayerButton().GetHoverStatus() == true) {
				String[] options = {"<Empty Slot>", "<Empty Slot>", "<Empty Slot>", "<Empty Slot>"};
				int i = 0;
				for(ClientHandler ch : Main.clients) {
					options[i] = ch.getPlayer().getUserName();
					i++;
				}
				
				String choice = String.valueOf(JOptionPane.showInputDialog(
				                    Main.frame,
				                    "Select A Player:",
				                    "Kick Player",
				                    JOptionPane.PLAIN_MESSAGE,
				                    null,
				                    options,
				                    null));
				m.kickPlayer(choice);
				
			}
			else if(m.getHostingMenu().getChangeGameButton().GetHoverStatus() == true) {
				//GameType[] options = {GameType.WAR, GameType.UNO, GameType.EUCHRE};
				GameType[] options = {GameType.WAR, GameType.UNO};
				GameType s = (GameType) JOptionPane.showInputDialog(
				                    Main.frame,
				                    "Select A Game:",
				                    "Server Settings",
				                    JOptionPane.PLAIN_MESSAGE,
				                    null,
				                    options,
				                    "War");
				if(s == null) { //Operation was canceled
					m.setClicked(false);
					return;
				}
				Main.theServer.runningGame = s;
			}
			else if(m.getHostingMenu().getStartGameButton().GetHoverStatus() == true) {
				String[] optionsCaseOne = {"1", "2", "3"};
				String[] optionsCaseTwo = {"0", "1", "2"};
				String[] optionsCaseThree = {"0", "1"};
				int numberAI = 0;
				
				try {
					switch(Main.GetNumConnections()) {
					
					case 0:
						JOptionPane.showMessageDialog(Main.frame, "You cannot start a game with 0 players!", Main.TITLE, JOptionPane.ERROR_MESSAGE);
						m.setClicked(false);
						return;
						
					case 1:
						numberAI = Integer.parseInt((String) JOptionPane.showInputDialog(
			                    Main.frame,
			                    "How Many AI?",
			                    "Server Settings",
			                    JOptionPane.PLAIN_MESSAGE,
			                    null,
			                    optionsCaseOne,
			                    "1"));
						break;
						
					case 2:
						numberAI = Integer.parseInt((String) JOptionPane.showInputDialog(
			                    Main.frame,
			                    "How Many AI?",
			                    "Server Settings",
			                    JOptionPane.PLAIN_MESSAGE,
			                    null,
			                    optionsCaseTwo,
			                    "0"));
						break;
						
					case 3:
						numberAI = Integer.parseInt((String) JOptionPane.showInputDialog(
			                    Main.frame,
			                    "How Many AI?",
			                    "Server Settings",
			                    JOptionPane.PLAIN_MESSAGE,
			                    null,
			                    optionsCaseThree,
			                    "0"));
						break;
						
					default:
						numberAI = 0;
						break;
					
					}	
				}
				catch(Exception e) { //An error occurred, just cancel.
					m.setClicked(false);
					return;
				}
				
				Main.numberAI = numberAI;
				if(Main.theServer.runningGame == GameType.WAR) {
					LoadState.Load(m, GameState.WAR_GAME);
				}
				else if(Main.theServer.runningGame == GameType.UNO) {
					LoadState.Load(m, GameState.UNO_GAME);
				}
			}
			
			m.setClicked(false);
		}
	}
	
}
