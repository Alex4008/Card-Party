package singlePlayerWar;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import cards.CardDeck;
import main.GameState;
import main.GameType;
import main.LoadState;
import main.Main;

public class SinglePlayerWar extends SinglePlayerGame{

	boolean gameStarted;
	boolean cardsPlayed = false;
	boolean inWar = false;
	boolean multiplayer;
	int numWar = 0;
	int timer = 0;
	CardDeck main;
	SingleplayerPlayer winner;
	ArrayList<SingleplayerPlayer> remove;
	
	public SinglePlayerWar(Main m, int playerCount) {
		super(playerCount);
		super.gameType = GameType.WAR;
		super.maxAI = 3;
		super.players.add(new SingleplayerPlayer(m, 0, m.getName(), false));
		for(int i = 1; i < playerCount; i++) { //Add AI
			super.players.add(new SingleplayerPlayer(m, i, "AI " + i, true));
		}
		//Give everyone cards;
		main = new CardDeck(m, new Point(0,0), false);
		main.Shuffle(5);
		
		while(main.getSize() > 0) {
			for(SingleplayerPlayer p : players) {
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
		for(SingleplayerPlayer p : players) {
			int value = p.getCardPlayedValue().num;
			if(value == 1) value = 14;
			if(value > highest) {
				winner = p;
				highest = value;
			}
			else if(value == highest) {
				warValue = value;
			}
		}
		
		if(warValue == highest)  {
			inWar = true;
			for(SingleplayerPlayer p : players) {
				int value = p.getCardPlayedValue().num;
				if(value == 1) value = 14;
				if(value == warValue) p.setInWar(true);
			}
		}
		
		if(inWar == false) {
			for(SingleplayerPlayer p : players) {
				if(p.cardPlayed != null) winner.addCard(p.cardPlayed);
			}
		}
	}
	
	public void decideWinnerOfWar() {
		winner = null;
		int highest = 0;
		int warValue = 0;
		for(SingleplayerPlayer p : players) {
			if(p.inWar()) {
				int value = p.getWarCardValue();
				if(value == 1) value = 14;
				if(value > highest) {
					winner = p;
					highest = value;
				}
				else if(value == highest) {
					warValue = value;
				}	
			}
		}
		
		if(warValue == highest) {
			inWar = true;
			numWar++;
		}
		else {
			inWar = false;
			for(SingleplayerPlayer p : players) p.setInWar(false);
		}
	}

	@Override
	public void tick(Main m) {
		remove = new ArrayList<SingleplayerPlayer>();
		for(SingleplayerPlayer p : players) { //Tick player and hands.
			p.tick(m);
			if(p.getHandSize() <= 0 && cardsPlayed == false) remove.add(p);
		}
		
		for(SingleplayerPlayer p : remove) players.remove(p);
		
		boolean flag = true;
		for(SingleplayerPlayer p : players) {
			if(!p.isAI) flag = false;
		}
		
		if(flag) { //This is for if the player has lost the game.
			JOptionPane.showMessageDialog(Main.frame, "You have lost the game!", "Game Over", JOptionPane.DEFAULT_OPTION);
			LoadState.Load(m, GameState.MAIN_MENU);
		}
		
		if(players.size() == 1) { //Win check
			JOptionPane.showMessageDialog(Main.frame, "You have won the game!", "Congratulations!", JOptionPane.DEFAULT_OPTION);
			LoadState.Load(m, GameState.MAIN_MENU);
		}
		
		// The main player clicked their card.
		if(players.get(0).clicked() && cardsPlayed == false) {
			for(SingleplayerPlayer p : players) p.playCard();
			cardsPlayed = true;
		}
		else {
			players.get(0).setClicked(false);
		}
		
		if(cardsPlayed) {
			timer++;
			if(inWar) {
				if(timer == 1) {
					for(SingleplayerPlayer p : players) {
						if(p.inWar()) {
							for(int i = 0; i < Main.CARDS_FLIPPED_WAR; i++) p.addWarCard();	
						}
					}	
				}
				else if(timer > 35) {
					decideWinnerOfWar();
					if(inWar) { //If we have triggered another war
						timer = 0;
					}
				}
			}
			else {
				if(timer == 35) {
					timer = 0;
					decideWinner();
					if(inWar == false) { //Only do this if there is no war
						cardsPlayed = false;
						for(SingleplayerPlayer p : players) p.setCardPlayedNull();
					}
				}
				else if(timer > 70) { //This is for when the war is over and we can give out cards
					timer = 0;
					cardsPlayed = false;
					for(SingleplayerPlayer p : players) {
						winner.addCard(p.cardPlayed);
						p.setCardPlayedNull();
						while(p.getWarCardsSize() > 0) winner.addCard(p.removeNextWarCard());
					}
				}
			}
		}
	}

	@Override
	public void render(Main m, Graphics g) {
		for(SingleplayerPlayer p : players) p.render(g);
		
	}

}
