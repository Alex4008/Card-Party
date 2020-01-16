package main;

public class Tick {
	public static void ticking(Main main) {
		
		switch (Main.gs) {
		
		case MAIN_MENU:
			//Render main menu code here
			TickMainMenu(main);
			return;
		
		case MAIN_SINGLEPLAYER:
			TickSinglePlayerMenu(main);
			return;
			
		case MAIN_MULTIPLAYER:
			TickMultiplayerMenu(main);
			return;
		
		case CONNECTION_SCREEN:
			TickConnectionMenu(main);
			return;
			
		case SINGLEPLAYER_GAME:
			TickSinglePlayerGameMenu(main);
			return;
		
		case MULTIPLAYER_GAME:
			TickMutliPlayerGameMenu(main);
			return;
			
		case LOGGED_IN_SCREEN:
			//RenderLevelEdit(main, g);
			break;
			
		default:
			break;
		}
	}
	
	private static void TickMainMenu(Main m) {
		//Only process mouse activity if the mouse is on screen
		if(m.getMousePosition() == null) return;
		
		try {
			//Detect hovers
			if(m.getMainMenu().getSinglePlayerButton().getRectangle().contains(m.getMousePosition()) == true) {
				m.getMainMenu().getSinglePlayerButton().SetHover(true);
			}
			else {
				m.getMainMenu().getSinglePlayerButton().SetHover(false);
			}
			
			if(m.getMainMenu().getMultiplayerButton().getRectangle().contains(m.getMousePosition()) == true) {
				m.getMainMenu().getMultiplayerButton().SetHover(true);
			}
			else {
				m.getMainMenu().getMultiplayerButton().SetHover(false);
			}
			
			if(m.getMainMenu().getExitButton().getRectangle().contains(m.getMousePosition()) == true) {
				m.getMainMenu().getExitButton().SetHover(true);
			}
			else {
				m.getMainMenu().getExitButton().SetHover(false);
			}
		}
		catch(NullPointerException e){
			System.out.println("Null caught");
		}
		
		
		if(m.getClicked() == true) {
			if(m.getMainMenu().getSinglePlayerButton().GetHoverStatus() == true) {
				LoadState.Load(m, GameState.MAIN_SINGLEPLAYER);
			}
			else if(m.getMainMenu().getMultiplayerButton().GetHoverStatus() == true) {
				LoadState.Load(m, GameState.CONNECTION_SCREEN);
			}
			else if(m.getMainMenu().getExitButton().GetHoverStatus() == true) {
				Main.closeGame();
			}
			
			m.setClicked(false);
		}
	}
	
	private static void TickSinglePlayerMenu(Main m) {
		//Only process mouse activity if the mouse is on screen
		if(m.getMousePosition() == null) return;
		
		try {
			//Detect hovers
			if(m.getSinglePlayerMenu().getWarButton().getRectangle().contains(m.getMousePosition()) == true) {
				m.getSinglePlayerMenu().getWarButton().SetHover(true);
			}
			else {
				m.getSinglePlayerMenu().getWarButton().SetHover(false);
			}
			
			if(m.getSinglePlayerMenu().getUnoButton().getRectangle().contains(m.getMousePosition()) == true) {
				m.getSinglePlayerMenu().getUnoButton().SetHover(true);
			}
			else {
				m.getSinglePlayerMenu().getUnoButton().SetHover(false);
			}
			
			if(m.getSinglePlayerMenu().getEuchreButton().getRectangle().contains(m.getMousePosition()) == true) {
				//m.getSinglePlayerMenu().getEuchreButton().SetHover(true);
			}
			else {
				m.getSinglePlayerMenu().getEuchreButton().SetHover(false);
			}
			
			if(m.getSinglePlayerMenu().getBackButton().getRectangle().contains(m.getMousePosition()) == true) {
				m.getSinglePlayerMenu().getBackButton().SetHover(true);
			}
			else {
				m.getSinglePlayerMenu().getBackButton().SetHover(false);
			}
		}
		catch(NullPointerException e){
			System.out.println("Null caught");
		}
		
		
		if(m.getClicked() == true) {
			if(m.getSinglePlayerMenu().getWarButton().GetHoverStatus() == true) {
				Main.currentGame = GameType.WAR;
				LoadState.Load(m, GameState.SINGLEPLAYER_GAME);
			}
			else if(m.getSinglePlayerMenu().getUnoButton().GetHoverStatus() == true) {
				Main.currentGame = GameType.UNO;
				LoadState.Load(m, GameState.SINGLEPLAYER_GAME);
			}
			else if(m.getSinglePlayerMenu().getEuchreButton().GetHoverStatus() == true) {
				System.out.println("Euchre is not yet supported");
			}
			else if(m.getSinglePlayerMenu().getBackButton().GetHoverStatus() == true) {
				LoadState.Load(m, GameState.MAIN_MENU);
			}
			
			m.setClicked(false);
		}
	}
	
	private static void TickConnectionMenu(Main m) {
		//Only process mouse activity if the mouse is on screen
		if(m.getMousePosition() == null) return;
		
		try {
			//Detect hovers
			if(m.getConnectionMenu().getConnectButton().getRectangle().contains(m.getMousePosition()) == true) {
				m.getConnectionMenu().getConnectButton().SetHover(true);
			}
			else {
				m.getConnectionMenu().getConnectButton().SetHover(false);
			}
			
			if(m.getConnectionMenu().getBackButton().getRectangle().contains(m.getMousePosition()) == true) {
				m.getConnectionMenu().getBackButton().SetHover(true);
			}
			else {
				m.getConnectionMenu().getBackButton().SetHover(false);
			}
		}
		catch(NullPointerException e){
			System.out.println("Null caught");
		}
		
		
		if(m.getClicked() == true) {
			if(m.getConnectionMenu().getConnectButton().GetHoverStatus() == true) {
				LoadState.Load(m, GameState.MAIN_MULTIPLAYER);
			}
			else if(m.getConnectionMenu().getBackButton().GetHoverStatus() == true) {
				LoadState.Load(m, GameState.MAIN_MENU);
			}
			
			m.setClicked(false);
		}
	}
	
	private static void TickMultiplayerMenu(Main m) {
		m.getMultiplayerMenu().setGameType(m.getClient().currentGameType);
		//Only process mouse activity if the mouse is on screen
		if(m.getMousePosition() == null) return;
		
		try {
			//Detect hovers
			if(m.getMultiplayerMenu().getDisconnectButton().getRectangle().contains(m.getMousePosition()) == true) {
				m.getMultiplayerMenu().getDisconnectButton().SetHover(true);
			}
			else {
				m.getMultiplayerMenu().getDisconnectButton().SetHover(false);
			}
		}
		catch(NullPointerException e){
			System.out.println("Null caught");
		}
		
		
		if(m.getClicked() == true) {
			if(m.getMultiplayerMenu().getDisconnectButton().GetHoverStatus() == true) {
				//Disconnecting from the server will switch the gamestate.
				m.endConnection();
				System.out.println("Disconnecting from server!");
			}
			
			m.setClicked(false);
		}
	}
	
	private static void TickSinglePlayerGameMenu(Main m) {
		if(Main.currentGame == GameType.WAR) {
			m.getWarMenu().tick(m);
			if(m.getMousePosition() == null) return;
			
			try {
				//Detect hovers
				if(m.getWarMenu().getExitButton().getRectangle().contains(m.getMousePosition()) == true) {
					m.getWarMenu().getExitButton().SetHover(true);
				}
				else {
					m.getWarMenu().getExitButton().SetHover(false);
				}
			}
			catch(NullPointerException e){
				System.out.println("Null caught");
			}

			if(m.getClicked() == true) {
				if(m.getWarMenu().getExitButton().GetHoverStatus() == true) {
					LoadState.Load(m, GameState.MAIN_MENU);
				}
				
				m.setClicked(false);
			}	
		}
		else if(Main.currentGame == GameType.UNO) {
			m.getUnoMenu().tick(m);
			if(m.isHovering(m.getUnoMenu().getExitButton().getRectangle())) {
				m.getUnoMenu().getExitButton().SetHover(true);
			}
			else {
				m.getUnoMenu().getExitButton().SetHover(false);
			}

			if(m.getClicked() == true) {
				if(m.getUnoMenu().getExitButton().GetHoverStatus() == true) {
					LoadState.Load(m, GameState.MAIN_MENU);
				}
			}	
		}
	}
	
	private static void TickMutliPlayerGameMenu(Main m) {
		m.getWarMenu().tick(m);
		if(m.getMousePosition() == null) return;
		
		try {
			//Detect hovers
			if(m.getWarMenu().getExitButton().getRectangle().contains(m.getMousePosition()) == true) {
				m.getWarMenu().getExitButton().SetHover(true);
			}
			else {
				m.getWarMenu().getExitButton().SetHover(false);
			}
		}
		catch(NullPointerException e){
			System.out.println("Null caught");
		}

		if(m.getClicked() == true) {
			if(m.getWarMenu().getExitButton().GetHoverStatus() == true) {
				LoadState.Load(m, GameState.MAIN_MENU);
				Main.connect = false;
				System.out.println("Disconnecting from server!");
			}
			
			m.setClicked(false);
		}
	}
}
