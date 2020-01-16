package main;
  
import java.awt.Point;
import java.io.*; 
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JOptionPane;

import cards.Card;
import cards.Suit;
import cards.Value;
import connection.ConnectionInformation;
import connection.UnoInformation;
import connection.WarInformation;
import multiPlayerGames.MultiplayerPlayer;
import multiPlayerGames.Opponent; 
  
// Client class 
public class Client extends Thread
{ 
	private boolean connected = false;
	private Main m;
	
	//Sockets
	InetAddress ip = null;
	Socket s = null;
	InputStream dis = null;
	OutputStream dos = null;
	
	ObjectInputStream objIn = null;
	ObjectOutputStream objOut = null;
	
	
	private String[] serverCode;
	private boolean properDisconnect = false;
	
	//Sent From Server
	public ArrayList<String> usernames = new ArrayList<String>();
	public int serverCap;
	public GameType currentGameType = GameType.NULL;
	
	//For to war
	int playerCount;
	int clientPlayerId = 0;
	//ArrayList<Opponent> opps;
	HashMap<String, Opponent> opponents;
	MultiplayerPlayer player;
	
	//ClientInput in = null;
	//ClientOutput out = null;
	
	Thread t1 = null;
	Thread t2 = null;
	
	// For to Uno
	
	
	volatile ConnectionInformation connectionInfo;
	
    public Client(Main m) {
    	this.m = m;
    }
    
    @Override
    public void run()  
    { 
        try
        { 
        	System.out.println("Trying to Connect to " + Main.ip + ":" + Main.port);
             
            // getting localhost ip 
            ip = InetAddress.getByName(Main.ip); 
      
            // establish the connection with server port 5056 
            try {
            	s = new Socket(ip, Main.port); 	
            }
            catch(ConnectException e) {
            	System.out.println("Could not connect to server");
        		LoadState.Load(m, GameState.CONNECTION_SCREEN);
        		JOptionPane.showMessageDialog(Main.frame, "Could not connect to the server!", Main.TITLE, JOptionPane.INFORMATION_MESSAGE);
        		connected = false;
            	return;
            }
            catch(Exception e) {
            	System.out.println("Something happened");
            	e.printStackTrace();
            }
      
            // obtaining input and out streams 
            dis = s.getInputStream();
            dos = s.getOutputStream();
            
            objIn = new ObjectInputStream(dis);
            objOut = new ObjectOutputStream(dos);
            
            /*
            in = new ClientInput(s, this);
            out = new ClientOutput(s, this);

            t1 = in;
            t2 = out;
            
            t1.start();
            t2.start();
            */
            // the following loop performs the exchange of 
            // information between client and client handler 
            while (Main.connect)  
            { 
            	if(connected == false) { //First time setup
            		connected = true;
            		connectionInfo = new ConnectionInformation(m, Main.userName);
            		connectionInfo.setVersion(Main.version);
            		objOut.writeObject(connectionInfo);
            		connectionInfo = null;
            		connectionInfo = (ConnectionInformation) objIn.readObject();	
            		serverCode = connectionInfo.getStatus().split(":");
            		System.out.println(connectionInfo.getStatus() + " REEEEE you reeeee");
            		if(codeHandler(serverCode) == false) return; 
            		objOut.writeObject(connectionInfo);
            	}
            	
            	// Here is where we do server things.
        		serverLoop();
            } 
              
            // closing resources 
            closeConnection(); //Close connection directly. (User disconnected)
        }catch(Exception e){ 
        	//Failed to connect
        	if(properDisconnect) return; //This should be taken care of if properly disconnected. Will probably rewrite this later. 
        	if(connected == false) {
        		System.out.println("Could not connect to server");
        		LoadState.Load(m, GameState.CONNECTION_SCREEN);
        		JOptionPane.showMessageDialog(Main.frame, "Could not connect to the server!", Main.TITLE, JOptionPane.INFORMATION_MESSAGE);
        		connected = false;
        	}
        	else {
        		e.printStackTrace();
        		codeHandler(serverCode);
        		
        	}
            
        }
        System.out.println("This thread, and all that inhabites it is dead");
    } 
    
    public void processInput() {
    	
    }
    
    public void processOutput() {
    	
    }
    
    public void serverLoop() {
		try {
			if(Main.connect) {
				boolean hasReply = false;
		    	WarInformation wi = null;
		    	UnoInformation ui = null;
			    Object obj = objIn.readObject();
			    
			    if(obj == null) return;
			    
			    if(obj instanceof ConnectionInformation) {
			    	
			    	connectionInfo = (ConnectionInformation) obj;
			    	if(connectionInfo.getStatus().contains("NONE")) {
			    		int playerCount = connectionInfo.getUsernames().size() + 1;
			    		serverCap = connectionInfo.getMaxPlayers();
			    		currentGameType = connectionInfo.getSetGame();
			    		ArrayList<String> users = connectionInfo.getUsernames();
			    		usernames = users;
			    	}
			    	else if(connectionInfo.getStatus().contains("ERROR")) {
			    		codeHandler(connectionInfo.getStatus());
			    	}
			    }
			    else if(obj instanceof WarInformation) {
			    	wi = (WarInformation) obj;
			    	if(wi.status.contains("SETUP")) {
			    		System.out.println("We have reached war-setup");
			    		playerCount = wi.numConnections;
			    		clientPlayerId = wi.clientID;
			    		player = new MultiplayerPlayer(m, wi.username, wi.numCards, clientPlayerId);
			    		opponents = new HashMap<String,Opponent>();
			    		System.out.println(wi.numCards + "");
			    		for(int i = 0; i < playerCount - 1; i++) {
			    			opponents.put(wi.otherUsernames.get(i), new Opponent(m, wi.otherUsernames.get(i),
			    					wi.otherNumCards.get(i),
			    					i + 1, true
			    					));
			    			System.out.println("Loop " + i);
			    		}
			    		System.out.println("We are about to load");
			    		Main.currentGame = GameType.WAR;
			    		LoadState.Load(m, GameState.MULTIPLAYER_GAME);
			    		System.out.println("We loaded.");
			    	}
			    	else if(wi.status.contains("UPDATE")) {
			    		if(wi.status.contains("GAMEOVER")) {
			    			if(player.getUsername().equals(wi.loser)) {
			    				player.setGameOver();
			    			}
			    			else {
			    				opponents.get(wi.loser).setGameOver();
			    			}
			    		}
			    		else if(wi.status.contains("WINNER")) {
			    			if(player.getUsername().equals(wi.winner)) {
			    				JOptionPane.showMessageDialog(Main.frame, "You have won the game!", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
			    			}
			    			else {
			    				JOptionPane.showMessageDialog(Main.frame, wi.winner + " has won the game!", "Game Over!", JOptionPane.INFORMATION_MESSAGE);
			    			}
			    			
			    			System.out.println("We are ending the game now.");
			    			player = null;
			    			opponents = null;
			    			Main.currentGame = null;
			    			LoadState.Load(m, GameState.MAIN_MULTIPLAYER);
			    			System.out.println("We have ended the game.");
			    			
						    ConnectionInformation ci = new ConnectionInformation(m, "nothing");
						    ci.setStatus("NOTHING");
						    objOut.writeObject(ci);
			    			
			    			return;
			    		}
			    		else if(wi.status.contains("ENDED")) {
			    			JOptionPane.showMessageDialog(Main.frame, "The game has been ended by the host.", Main.TITLE, JOptionPane.INFORMATION_MESSAGE);
			    			
			    			System.out.println("We are ending the game now.");
			    			player = null;
			    			opponents = null;
			    			Main.currentGame = null;
			    			LoadState.Load(m, GameState.MAIN_MULTIPLAYER);
			    			System.out.println("We have ended the game.");
			    			
						    ConnectionInformation ci = new ConnectionInformation(m, "nothing");
						    ci.setStatus("NOTHING");
						    objOut.writeObject(ci);
			    			
			    			return;
			    		}
			    		
		    			//while(objIn.available() == 0) {} //I am sure that there is an easier way to do this. But lmao. Here we are >:D
		    			//wi = (WarInformation) objIn.readObject();
		    			
			    		int warCardPlace = 0;
			    		
		    			for(int i = 0; i < wi.numConnections; i++) {
			    			if(player.getUsername().equals(wi.otherUsernames.get(i))) {
			    				player.setCardPlayed(wi.cardSuits.get(i).num, wi.cardValues.get(i).num);
			    				player.setCardsInHand(wi.otherNumCards.get(i));
		    					ArrayList<Card> warCards = new ArrayList<Card>();

			    				if(wi.warCardsSize != null) {
			    					if(wi.warCardsSize.size() > i) {
			    						int numWarCards = wi.warCardsSize.get(i);
				    					for(int j = 0; j < numWarCards; j++) {
				    						if(numWarCards == 0) break;
				    						warCards.add(new Card(m, wi.warSuits.get(j + warCardPlace), wi.warValues.get(j + warCardPlace)));
				    					}
				    					warCardPlace += numWarCards;
			    					}
			    				}
			    				player.setWarCards(warCards);
			    			}
			    			else {
			    				opponents.get(wi.otherUsernames.get(i)).setCardPlayed(wi.cardSuits.get(i).num, wi.cardValues.get(i).num);
			    				opponents.get(wi.otherUsernames.get(i)).setCardsInHand(wi.otherNumCards.get(i));
			    				opponents.get(wi.otherUsernames.get(i)).setIsAI(wi.isAI.get(i));
			    				ArrayList<Card> warCards = new ArrayList<Card>();
			    				
			    				if(wi.warCardsSize != null) {
			    					if(wi.warCardsSize.size() > i) {
			    						int numWarCards = wi.warCardsSize.get(i);
				    					for(int j = 0; j < numWarCards; j++) {
				    						if(numWarCards == 0) break;
				    						warCards.add(new Card(m, wi.warSuits.get(j + warCardPlace), wi.warValues.get(j + warCardPlace)));
				    					}
				    					warCardPlace += numWarCards;
			    					}
			    				}
		    					opponents.get(wi.otherUsernames.get(i)).setWarCards(warCards);
			    			}
		    			}
		    			wi.isReady = player.readyStatus();
		    			hasReply = true;
		    			player.setReadyStatus(false);
			    	}
			    }
			    else if(obj instanceof UnoInformation) {
			    	ui = (UnoInformation) obj;
			    	if(wi.status.contains("SETUP")) {
			    		System.out.println("We have reached uno-setup");
			    		playerCount = ui.numPlayers;
			    		clientPlayerId = ui.currentPlayer;
			    		player = new MultiplayerPlayer(m, connectionInfo.getUsername(), wi.numCards, clientPlayerId);
			    		opponents = new HashMap<String,Opponent>();
			    		System.out.println(wi.numCards + "");
			    		for(int i = 0; i < playerCount - 1; i++) {
			    			opponents.put(wi.otherUsernames.get(i), new Opponent(m, wi.otherUsernames.get(i),
			    					wi.otherNumCards.get(i),
			    					i + 1, true
			    					));
			    			System.out.println("Loop " + i);
			    		}
			    		System.out.println("We are about to load");
			    		Main.currentGame = GameType.WAR;
			    		LoadState.Load(m, GameState.MULTIPLAYER_GAME);
			    		System.out.println("We loaded.");
			    	}
			    }
			    ConnectionInformation ci = new ConnectionInformation(m, "nothing");
			    ci.setStatus("NOTHING");
			    
			    if(hasReply) objOut.writeObject(wi);
			    else objOut.writeObject(ci);
			    
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    //Helper method for codeHandler. Converts string into string[]
    public boolean codeHandler(String unformatedCode) {
    	return codeHandler(unformatedCode.split(":"));
    }
    
    public boolean codeHandler(String[] code) {
    	double num = Double.parseDouble(code[1]);
    	System.out.println(num);
    	if(code[0].equals("ERROR")) {
    		System.out.println("Error Code: " + num + " has been sent from the server. Disconnecting.");
    		properDisconnect = true;
    		closeConnection();
    		//Show error code to user.
    		if(num < 100) { //Server mismatch error
    			JOptionPane.showMessageDialog(Main.frame, "Client-Server Mismatch Error. Client: v" + Main.version + " Server: v" + num / 10, Main.TITLE, JOptionPane.INFORMATION_MESSAGE);
    		}
    		else if(num == 100) { //Server is full error
    			JOptionPane.showMessageDialog(Main.frame, "The server is full!", Main.TITLE, JOptionPane.INFORMATION_MESSAGE);
    		}
    		else if(num == 101) { //Server is closing
    			JOptionPane.showMessageDialog(Main.frame, "The server was closed!", Main.TITLE, JOptionPane.INFORMATION_MESSAGE);
    		}
    		else if(num == 102) { //Player was kicked from server
    			JOptionPane.showMessageDialog(Main.frame, "You were kicked from the server!", Main.TITLE, JOptionPane.INFORMATION_MESSAGE);
    		}
    		else if(num == 103) { //A game is currently running
    			JOptionPane.showMessageDialog(Main.frame, "There is a game running on this server, you cannot connect mid game!", Main.TITLE, JOptionPane.INFORMATION_MESSAGE);
    		}
    		else { //Unknown error
    			JOptionPane.showMessageDialog(Main.frame, "An unknown error has occured. This may be due to a mismatched version between server and client.", Main.TITLE, JOptionPane.INFORMATION_MESSAGE);
    		}
    		return false;
    	}
    	else if(code[0].equals("PASS")) { //Server connected successfully.
    		Main.gs = GameState.MAIN_MULTIPLAYER; //Update Main Game State
    		if(!Main.userName.equals(code[2])) {
    			JOptionPane.showMessageDialog(Main.frame, "You tried to join with the same name as someone else, your username has been updated!", Main.TITLE, JOptionPane.INFORMATION_MESSAGE);
    			Main.userName = code[2];
    		}
    	}
    	return true;
    }
    
    
    public int getPlayerCount() {
    	return playerCount;
    }
    
    public HashMap<String,Opponent> getOpponents() {
    	return opponents;
    }
    
    public MultiplayerPlayer getPlayer() {
    	return player;
    }
    
    
    public void closeConnection() {
    	try {
    		ConnectionInformation ci = new ConnectionInformation(m, "This doesnt matter, I am leaving");
    		ci.setStatus("exit");
    		objOut.writeObject(ci); 
    		objIn.close();
    		objOut.close();
            dos.close(); 
            dis.close(); 
            s.close();
    	}
    	catch(IOException e) {
    		if(Main.devMode) e.printStackTrace();
    	}
    	LoadState.Load(m, GameState.CONNECTION_SCREEN);
    	Main.connect = false;
    	connected = false;
    	objIn = null;
    	objOut = null;
    	dos = null;
    	dis = null;
    	s = null;
    	t1 = null;
    	t2 = null;
    }
} 