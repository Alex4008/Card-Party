package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import cards.Suit;
import cards.Value;
import connection.ConnectionInformation;
import connection.WarInformation;
import games.War;

public class ClientHandler extends Thread {

	ObjectInputStream objIn = null;
	ObjectOutputStream objOut = null;

	Main m;
	final Socket s;
	
	boolean isFake;
	String errorCode;
	
	Player thePlayer;
	String ogName;
	ConnectionInformation connectionInfo;
	
	int clientID;
	boolean startGame = false;
	boolean endGame = false;
	String loser = null;
	String winner = null;
	
	boolean running = true;
	
	public ClientHandler(Main m, Socket s, ObjectInputStream in, ObjectOutputStream out) throws IOException {
		this.m = m;
		this.s = s;
        // obtaining input and out streams 
        objIn = in;
        objOut = out;
		thePlayer = new Player(m);
		ogName = "";
		errorCode = "";
		isFake = false;
	}
	
	public ClientHandler(Main m, String username) {
		s = null;
		this.m = m;
		thePlayer = new Player(m);
		thePlayer.setUserName(username);
		isFake = true;
		errorCode = "";
	}
	
	
	@Override
	public void run() {
		if(!isFake) {
			try {
				if(connectionSetup()) {
					System.out.println("uh ok");
					while(running) {
						if(processInput() == false) break;
						processOutput();
						
						if(!errorCode.isEmpty()) break;
					}
					disconnect();
				}	
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		dummyLoop();
		
		System.out.println("this threads life has come to a close");
	}
	
    public void dummyLoop() {
    	while(Main.theServer.gameRunning || Main.theServer.inGameStartUp) {
    		isFake = true;
    		thePlayer.isAI = true;
    		thePlayer.setReady(true);
    	}
    }
    
	public Player getPlayer() {
		return thePlayer;
	}
	
	private boolean processInput() throws ClassNotFoundException, IOException {
		Object obj = objIn.readObject();
		if(obj instanceof ConnectionInformation) {
			connectionInfo = (ConnectionInformation) obj;
			if(connectionInfo.getStatus().contains("exit") ) {
				errorCode = "ERROR:104";
				return false;
			}
			else if(connectionInfo.getStatus().contains("WAR-READY")) {
				thePlayer.setReady(true);
			}
		}
		else if(obj instanceof WarInformation) {
			WarInformation wi = (WarInformation) obj;
			if(wi.isReady) thePlayer.setReady(true);
		}
		
		return true;
	}
	
	private void processOutput() throws IOException {
		if(Main.theServer.gameRunning == false) {
			connectionInfo.setSetGame(Main.theServer.runningGame);
			connectionInfo.setStatus("NONE");
			connectionInfo.setMaxPlayers(Main.maxPlayers);
			ArrayList<String> usernames = new ArrayList<String>();
			for(ClientHandler ch : Main.clients) {
				usernames.add(ch.getPlayer().getUserName());
			}
			connectionInfo.setUsernames(usernames);
			objOut.writeObject(connectionInfo);
			try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
		}
		else if(Main.theServer.gameRunning == true && Main.theServer.runningGame == GameType.WAR) {
			if(startGame == true) { //Setup war
				startGame = false;
				WarInformation wi = new WarInformation();
				wi.status = "WAR-SETUP";
				wi.numConnections = Main.getClients().size();
				wi.clientID = clientID;
				wi.username = thePlayer.getUserName();
				wi.numCards = thePlayer.getNumCards();
				ArrayList<String> users = new ArrayList<String>();
				ArrayList<Integer> numCards = new ArrayList<Integer>();
				
				for(ClientHandler ch : Main.getClients()) {
					if(!ch.equals(this)) {
						users.add(ch.getPlayer().getUserName());
						numCards.add(ch.getPlayer().getNumCards());
					}
				}
				wi.otherUsernames = users;
				wi.otherNumCards = numCards;
				objOut.writeObject(wi);
				try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
			}
			else if(m.getGameMenu().getWarGame().getCanSendUpdate()) { //Up keep war
				if(loser != null) {
					WarInformation wi = new WarInformation();
					wi.status = "WAR-UPDATE-GAMEOVER";
					wi.loser = loser;
					objOut.writeObject(wi);
					try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
					loser = null;
				}
				
    			if(winner != null) {
    				WarInformation wi = new WarInformation();
    				wi.status = "WAR-UPDATE-WINNER";
    				wi.winner = winner;
    				objOut.writeObject(wi);
    				try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
    				winner = null;
    				//Upon getting this message the clients will end the game
    				getPlayer().resetPlayer();
    				return;
    			}
    			
    			if(m.getGameMenu().getWarGame().getForceEndGame()) { //This will force end the game.
    				WarInformation wi = new WarInformation();
    				wi.status = "WAR-UPDATE-ENDED";
    				wi.winner = winner;
    				objOut.writeObject(wi);
    				try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
    				//Upon getting this message the clients will end the game
    				getPlayer().resetPlayer();
    				return;
    			}
    			// This is terrible
    			
    			WarInformation wi = new WarInformation();
    			War w = m.getGameMenu().getWarGame();
    			wi.status = "WAR-UPDATE";
    			wi.numConnections = Main.getClients().size();
    			
				ArrayList<String> otherUsers = new ArrayList<String>();
    			ArrayList<Suit> cardSuits = new ArrayList<Suit>();
    			ArrayList<Value> cardValues = new ArrayList<Value>();
    			ArrayList<Integer> cardNums = new ArrayList<Integer>();
    			ArrayList<Boolean> isAI = new ArrayList<Boolean>();
    			ArrayList<Integer> warCardsSize = new ArrayList<Integer>();
    			ArrayList<Suit> warCardSuits = new ArrayList<Suit>();
    			ArrayList<Value> warCardValues = new ArrayList<Value>();
    			
    			for(ClientHandler ch : Main.getClients()) {
    				Player p = ch.getPlayer();
    				otherUsers.add(p.getUserName());
    				cardSuits.add(p.getCardPlayedSuit());
    				cardValues.add(p.getCardPlayedValue());
    				cardNums.add(p.getNumCards());
    				isAI.add(p.isAI);
    				if(w.inWar || w.timer > 35) {
    					warCardsSize.add(p.getWarCardsSize());
    					for(int i = 0; i < p.getWarCards().size(); i++) {
    						warCardSuits.add(p.getWarCards().get(i).getSuit());
    						warCardValues.add(p.getWarCards().get(i).getValue());
    					}
    				}
    			}
				wi.cardSuits = cardSuits;
				wi.cardValues = cardValues;
				wi.otherNumCards = cardNums;
				wi.isAI = isAI;
				wi.warCardsSize = warCardsSize;
				wi.warSuits = warCardSuits;
				wi.warValues = warCardValues;
				wi.otherUsernames = otherUsers;
				objOut.writeObject(wi);
				try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
			}
		}
		else if(Main.theServer.gameRunning == true && Main.theServer.runningGame == GameType.UNO) {
			System.out.println("Here we be");
			if(startGame == true) {
				System.out.println("We still bein in here and shit");
				
			}
			
		}
	}
	
    public boolean isAI() {
    	return isFake;
    }
    
    public void startGame() {
    	startGame = true;
    }
    
    public void sendLoser(String value) {
    	loser = value;
    }
    
    public void sendWinner(String value) {
    	winner = value;
    }
    
    public void endGame() {
    	endGame = true;
    }
	
	private boolean connectionSetup() throws IOException, ClassNotFoundException {
		ConnectionInformation ci = null;
		while(ci == null) ci = (ConnectionInformation) objIn.readObject();
		if(ci.getVersion() != Main.version) {
			errorCode = "ERROR:" + (Main.version * 10);
			return false;
		}
		
		if(Main.clients.size() > Main.maxPlayers) {
			errorCode = "ERROR:100";
			return false;
		}
		
		if(Main.theServer.gameRunning) {
			errorCode = "ERROR:103";
			return false;
		}
		
		int num = 0;
		int rng = (int) (Math.random() * 1000 + 1);
		for(ClientHandler ch : Main.clients) {
			if(ch.getPlayer().getUserName().equalsIgnoreCase(ci.getUsername()) || ch.ogName.equalsIgnoreCase(ci.getUsername())) {
				num++;
			}
		}
		
		ogName = ci.getUsername();
		if(num > 0) ci.setUsername(ogName + " " + rng);
		thePlayer.setUserName(ci.getUsername());
		ci.setStatus("PASS:0:" + ci.getUsername()); 
		ci.setMaxPlayers(Main.maxPlayers);
		objOut.writeObject(ci);
		try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
		connectionInfo = new ConnectionInformation(m, thePlayer.getUserName());
		return true;
		
	}
	
	public void disconnect() {
		disconnect(true);
	}

	public void disconnect(boolean sendMsg) {
		running = false;
		System.out.println("Disconnecting");
    	if(isFake) return;
    	
        System.out.println("Closing this connection with status code: " + errorCode); 
        try {
        	ConnectionInformation ci = new ConnectionInformation(m, thePlayer.getUserName());
        	ci.setStatus(errorCode);
        	if(!errorCode.equals("ERROR:104") && sendMsg) {
        		objOut.writeObject(ci);
    			try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
        	}
        	this.objIn.close();
        	this.objOut.close();
			this.s.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
    public void closeServer() {
    	errorCode = "ERROR:101";
    }
    
    public void kickPlayer() {
    	if(isFake) JOptionPane.showMessageDialog(Main.frame, "You cannot kick an AI!", Main.TITLE, JOptionPane.ERROR_MESSAGE);
    	errorCode = "ERROR:102";
    }
}
