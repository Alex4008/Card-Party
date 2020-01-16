package games;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import main.CardDeck;
import main.ClientHandler;
import main.GameState;
import main.GameType;
import main.LoadState;
import main.Main;
import main.Player;

public class War extends Game{
	Main m;
	boolean gameStarted;
	boolean cardsPlayed = false;
	public boolean inWar = false;
	boolean multiplayer;
	public int numWar = 0;
	public int timer = 0;
	CardDeck main;
	Player winner;
	ArrayList<Player> remove;
	boolean allPlayersReady = false;
	String currentCommand = "WAIT";
	boolean sendUpdate = true;
	boolean runTurn = false;
	
	boolean forceEndGame = false;
	
	public War(Main m) {
		this.m = m;
		super.gameType = GameType.WAR;
		
		//Give everyone cards;
		main = new CardDeck(m, false);
		main.Shuffle(2);
		
		while(main.getSize() > 0) {
			for(ClientHandler ch : Main.getClients()) {
				Player p = ch.getPlayer();
				if(main.getSize() != 0) {
					p.addCard(main.getNextCard());
					main.remove(main.getNextCard());
				}
			}
		}
	}
	
	public void decideWinner() {
		winner = null;
		int highest = 0;
		int warValue = 0;
		for(ClientHandler ch : Main.getClients()) {
			if(ch.getPlayer().getLost()) continue;
			int value = ch.getPlayer().getCardPlayedValue().num;
			if(value == 1) value = 14;
			if(value > highest) {
				winner = ch.getPlayer();
				highest = value;
			}
			else if(value == highest) {
				warValue = value;
			}
		}
		
		if(warValue == highest)  {
			inWar = true;
			for(ClientHandler ch : Main.getClients()) {
				if(ch.getPlayer().getLost()) continue;
				int value = ch.getPlayer().getCardPlayedValue().num;
				if(value == 1) value = 14;
				if(value == warValue) ch.getPlayer().setInWar(true);
			}
		}
		
		if(inWar == false) {
			for(ClientHandler ch : Main.getClients()) {
				if(ch.getPlayer().getLost()) continue;
				if(ch.getPlayer().cardPlayed != null) winner.addCard(ch.getPlayer().cardPlayed);
			}
		}
	}
	
	public void decideWinnerOfWar() {
		winner = null;
		int highest = -1;
		int warValue = 0;
		for(ClientHandler ch : Main.getClients()) {
			if(ch.getPlayer().getLost()) continue;
			if(ch.getPlayer().inWar()) {
				int value = ch.getPlayer().getWarCardValue();
				if(value == 1) value = 14;
				//System.out.println("Value: " + value);
				if(value > highest) {
					winner = ch.getPlayer();
					highest = value;
				}
				else if(value == highest) {
					warValue = value;
				}	
			}
		}
		System.out.println(winner.getWarCardValue());
		if(warValue == highest) {
			inWar = true;
			numWar++;
		}
		else {
			inWar = false;
			for(ClientHandler ch : Main.getClients()) {
				if(ch.getPlayer().getLost()) continue;
				ch.getPlayer().setInWar(false);
			}
		}
	}

	public boolean getCanSendUpdate() {
		return sendUpdate;
	}
	
	@Override
	public void tick(Main m) {
		for(ClientHandler ch : Main.getClients()) { //Tick player and hands.
			Player p = ch.getPlayer();
			p.tick(m);
		}
		
		//Check to make sure that the game is not all AIs
		boolean allAI = true;
		for(ClientHandler ch : Main.getClients()) {
			if(!ch.isAI()) allAI = false;
		}
		
		
		if(allAI == true) { //End game due to all AI
			for(ClientHandler ch : Main.getClients()) {
				ch.sendWinner("Oops all AI");
			}
			LoadState.Load(m, GameState.SERVER_MAIN);
		}
		
		if(m.getGameMenu().getWarGame().getForceEndGame() == true) { //End game due to force close.
			LoadState.Load(m, GameState.SERVER_MAIN);
		}
		
		// Check to see if everyone is ready
		allPlayersReady = true;
		for(ClientHandler ch : Main.getClients()) {
			if(ch.getPlayer().getLost()) continue;
			if(ch.getPlayer().getReadyStatus() == false) allPlayersReady = false;
		}
		
		if(allPlayersReady || runTurn) {
			runTurn = true;
			for(ClientHandler ch : Main.getClients()) {
				ch.getPlayer().setReady(false);
			}
		}
		
		int playersLeft = 0;
		for(ClientHandler ch : Main.getClients()) {
			if(ch.getPlayer().getLost() == false) playersLeft++;
		}
		
		//End game
		if(playersLeft == 1) {
			for(ClientHandler ch : Main.getClients()) {
				Player p = ch.getPlayer();
				if(p.getLost() == false) winner = p;
			}
			for(ClientHandler ch : Main.getClients()) {
				ch.sendWinner(winner.getUserName());
			}
			LoadState.Load(m, GameState.SERVER_MAIN);
		}
		
		if(runTurn) {
			sendUpdate = false;
			
			//Play card for every player
			for(ClientHandler ch : Main.getClients()) {
				if(ch.getPlayer().getLost()) continue;
				if(ch.getPlayer().getCurrentCard() == null) ch.getPlayer().playCard();
			}
			
			
			timer++;
			if(inWar) {
				if(timer == 1) {
					//System.out.println("Timer is 1: War has started");
					for(ClientHandler ch : Main.getClients()) {
						if(ch.getPlayer().getLost()) continue;
						if(ch.getPlayer().inWar()) {
							for(int i = 0; i < 3; i++) ch.getPlayer().addWarCard();	
						}
					}	
				}
				else if(timer > 35) {
					//System.out.println("Timer is > 35: deciding winner of war");
					decideWinnerOfWar();
					if(inWar) { //If we have triggered another war
						//System.out.println("We have triggered another war");
						timer = 0;
					}
				}
			}
			else {
				if(timer == 35) {
					//System.out.println("Timer is 35: Deciding first round winner");
					timer = 0;
					decideWinner();
					if(inWar == false) { //Only do this if there is no war
						//System.out.println("There was no war.");
						cardsPlayed = false;
						runTurn = false;
						for(ClientHandler ch : Main.getClients()) {
							if(ch.getPlayer().getLost()) continue;
							ch.getPlayer().setCardPlayedNull();
						}
					}
					else {
						//System.out.println("There is a war!");
					}
				}
				else if(timer > 70) { //This is for when the war is over and we can give out cards
					//System.out.println("Timer is 70: War is over, giving out cards");
					timer = 0;
					cardsPlayed = false;
					runTurn = false;
					for(ClientHandler ch : Main.getClients()) {
						if(ch.getPlayer().getLost()) continue;
						winner.addCard(ch.getPlayer().cardPlayed);
						ch.getPlayer().setCardPlayedNull();
						while(ch.getPlayer().getWarCardsSize() > 0) winner.addCard(ch.getPlayer().removeNextWarCard());
					}
				}
			}
			
			//For determining when a player has lost.
			for(ClientHandler ch : Main.getClients()) {
				Player p = ch.getPlayer();
				if(p.cardPlayed == null && cardsPlayed == false && p.getHandSize() == 0 && p.getLost() == false) {
					p.setLost(true);
					for(ClientHandler c : Main.getClients()) c.sendLoser(p.getUserName());
				}
			}
			
			sendUpdate = true;
		}
	}

	public boolean getForceEndGame() {
		return forceEndGame;
	}
	
	public void setForceEndGame(boolean value) {
		forceEndGame = value;
	}
	
	@Override
	public void render(Main m, Graphics g) {
		
	}
}
