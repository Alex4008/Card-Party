package singlePlayerWar;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import cards.Card;
import cards.CardDeck;
import cards.Value;
import main.Main;

public class SingleplayerPlayer {
	Main m;
	int playerID;
	String userName;
	boolean isAI;
	CardDeck hand;
	Card cardPlayed;
	Point loc;
	Point cardLoc;
	boolean inWar = false;
	private ArrayList<Card> warCards = new ArrayList<Card>();
	
	SingleplayerPlayer(Main m, int playerID, String userName, boolean isAI) {
		this.m = m;
		this.playerID = playerID;
		this.userName = userName;
		this.isAI = isAI;
		
		switch(playerID) {
		
		case 0:
			loc = new Point(Main.WIDTH / 2 - (Main.cardBack.getWidth() / 2), Main.HEIGHT / 5 * 4);
			cardLoc = new Point(loc.x, loc.y - 100);
			hand = new CardDeck(m, loc, true);
			break;
			
		case 1:
			loc = new Point(Main.WIDTH / 2 - (Main.cardBack.getWidth() / 2), (Main.HEIGHT / 5) - Main.cardBack.getHeight());
			cardLoc = new Point(loc.x, loc.y + 100);
			hand = new CardDeck(m, loc, true);
			break;
			
		case 2:
			loc = new Point(Main.WIDTH / 5 - Main.cardBack.getWidth(), Main.HEIGHT / 2 - (Main.cardBack.getHeight() / 2));
			cardLoc = new Point(loc.x + 100, loc.y);
			hand = new CardDeck(m, loc, true);
			break;
			
		case 3:
			loc = new Point(Main.WIDTH / 5 * 4 + (Main.cardBack.getWidth() / 2), Main.HEIGHT / 2 - (Main.cardBack.getHeight() / 2));
			cardLoc = new Point(loc.x - 100, loc.y);
			hand = new CardDeck(m, loc, true);
			break;
			
		default:
			loc = new Point((playerID + 2) * 50, (playerID + 2) * 20);
			cardLoc = new Point(loc.x - 85, loc.y - 85);
			hand = new CardDeck(m, loc, true);
			break;
		}
	}
	
	public void playCard() {
		cardPlayed = hand.getNextCard();
		hand.remove(cardPlayed);
		hand.setClicked(false);
	}
	
	public boolean addWarCard() {
		if(hand.getSize() == 0) return false;
		warCards.add(hand.getNextCard());
		hand.remove(hand.getNextCard());
		return true;
	}
	
	public void addCard(Card c) {
		hand.add(c);
	}
	
	public void removeCard(Card c) {
		if(hand.contains(c)) hand.remove(c);
	}
	
	public int getHandSize() {
		return hand.getSize();
	}
	
	public void setCardPlayedNull() {
		cardPlayed = null;
	}
	
	public void tick(Main m) {
		hand.tick(m);
		//if(hand.isClicked()) playCard();
	}
	
	public void render(Graphics g) {
		hand.render(g);
		if(playerID == 0 || playerID == 1) g.drawString("Cards:" + hand.getSize(), loc.x + Main.CARD_WIDTH + 15, loc.y + 20);
		else g.drawString("Cards:" + hand.getSize(), loc.x, loc.y - 20);
			
		if(cardPlayed != null) {
			g.drawImage(cardPlayed.getImage(),cardLoc.x, cardLoc.y, null);
		}
		
		if(warCards.isEmpty() == false) {
			for(int i = 0; i < warCards.size(); i++) {
				if(playerID != 3) { //Formating for the right most player
					g.drawImage(Main.cardBack, cardLoc.x + ((i + 1) * Main.CARD_WIDTH / 4), cardLoc.y, null);	
					if(i % 3 == 2 || i == warCards.size() - 1) {
						g.drawImage(warCards.get(i).getImage(), cardLoc.x + ((i + 1) * Main.CARD_WIDTH / 4), cardLoc.y, null);
					}	
				}
				else {
					g.drawImage(Main.cardBack, cardLoc.x - ((i + 1) * Main.CARD_WIDTH / 4), cardLoc.y, null);	
					if(i % 3 == 2 || i == warCards.size() - 1) {
						g.drawImage(warCards.get(i).getImage(), cardLoc.x - ((i + 1) * Main.CARD_WIDTH / 4), cardLoc.y, null);
					}
				}
			}
			//g.drawImage(warCards.get(0).getImage(), cardLoc.x + (warCards.size() * Main.CARD_WIDTH / 4), cardLoc.y, null);
		}
	}
	
	public Value getCardPlayedValue() {
		return cardPlayed.getValue();
	}
	
	public int getWarCardValue() {
		if(warCards.size() == 0) return -1;
		return warCards.get(warCards.size() - 1).getValue().num;
	}
	
	public Card removeNextWarCard() {
		Card c = warCards.get(0);
		warCards.remove(0);
		return c;
	}
	
	//Getters / Setters
	public int getPlayerID() {
		return playerID;
	}
	
	public boolean clicked() {
		return hand.isClicked();
	}
	
	public void setClicked(boolean value) {
		hand.setClicked(value);
	}
	
	public int getWarCardsSize() {
		return warCards.size();
	}
	
	public boolean inWar() {
		return inWar;
	}
	
	public void setInWar(boolean value) {
		inWar = value;
	}
	
}
