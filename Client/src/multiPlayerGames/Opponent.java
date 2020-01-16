package multiPlayerGames;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import cards.Card;
import cards.CardDeck;
import cards.Suit;
import cards.Value;
import main.Main;

public class Opponent {
	Main m;
	String username;
	int cards;
	ArrayList<Card> warCards;
	int id;
	Point loc;
	Point cardLoc;
	boolean ready;
	Card cardPlayed;
	int renderNum;
	int cardsInHand;
	boolean gameLost = false;
	boolean isAI;
	
	public Opponent(Main m, String username, int cards, int playerID, boolean isAI) {
		this.m = m;
		this.username = username;
		this.cards = cards;
		this.id = playerID;
		this.isAI = isAI;
		ready = false;
		cardPlayed = null;
		loc = new Point(150,150); //Temp point until render loc is set
		cardLoc = new Point(150,150); //Temp point until render loc is set
		warCards = new ArrayList<Card>();
		cardsInHand = 0;
	}

	public void updateInformation(int cards) {
		this.cards = cards;
	}
	
	public void updateInformation(ArrayList<Card> warCards) {
		this.warCards = warCards;
	}
	
	public boolean readyStatus() {
		return ready;
	}
	
	public void setCardPlayed(int suit, int value) {
		if(suit == 0 || value == 0) cardPlayed = null;
		else cardPlayed = new Card(m, Suit.intToSuit(suit), Value.intToValue(value));
	}
	
	public int getCardsInHand() {
		return cardsInHand;
	}
	
	public void setCardsInHand(int value) {
		cardsInHand = value;
	}
	
	public void setWarCards(ArrayList<Card> cards) {
		warCards = cards;
	}
	
	public void setIsAI(boolean value) {
		isAI = value;
	}
	
	public void setRenderNum(int value) {
		renderNum = value;
		
		switch(renderNum) {
		case 0:
			loc = new Point(Main.WIDTH / 2 - (Main.cardBack.getWidth() / 2), Main.HEIGHT / 5 * 4);
			cardLoc = new Point(loc.x, loc.y - 100);
			break;
			
		case 1:
			loc = new Point(Main.WIDTH / 2 - (Main.cardBack.getWidth() / 2), (Main.HEIGHT / 5) - Main.cardBack.getHeight());
			cardLoc = new Point(loc.x, loc.y + 100);
			break;
			
		case 2:
			loc = new Point(Main.WIDTH / 5 - Main.cardBack.getWidth(), Main.HEIGHT / 2 - (Main.cardBack.getHeight() / 2));
			cardLoc = new Point(loc.x + 100, loc.y);
			break;
			
		case 3:
			loc = new Point(Main.WIDTH / 5 * 4 + (Main.cardBack.getWidth() / 2), Main.HEIGHT / 2 - (Main.cardBack.getHeight() / 2));
			cardLoc = new Point(loc.x - 100, loc.y);
			break;
			
		default:
			loc = new Point((renderNum + 2) * 50, (renderNum + 2) * 20);
			cardLoc = new Point(loc.x - 85, loc.y - 85);
			break;
		}
	}
	
	public void setGameOver() {
		gameLost = true;
	}
	
	public void render(Graphics g) {
		if(cardsInHand != 0) g.drawImage(Main.cardBack, loc.x, loc.y, null);
		
		if(renderNum == 0 || renderNum == 1) {
			if(!gameLost) {
				if(!gameLost) {
					g.drawString("Cards: " + cardsInHand, loc.x + Main.CARD_WIDTH + 15, loc.y + 40);
					if(isAI && !username.toLowerCase().contains("ai")) g.drawString("AI", loc.x + Main.CARD_WIDTH + 15, loc.y + 60);
				}
			}
			else g.drawString("has run out of cards!", loc.x + Main.CARD_WIDTH + 15, loc.y + 40);
			
			g.drawString(username, loc.x + Main.CARD_WIDTH + 15, loc.y + 20);	
		}
		else {
			if(!gameLost) {
				if(!gameLost) {
					g.drawString("Cards: " + cardsInHand, loc.x, loc.y - 40);
					if(isAI && !username.toLowerCase().contains("ai")) g.drawString("AI", loc.x, loc.y - 20);
				}
			}
			else g.drawString("has run out of cards!", loc.x, loc.y - 40);
			
			g.drawString(username, loc.x, loc.y - 60);
		}
		
		
		if(cardPlayed != null) {
			g.drawImage(cardPlayed.getImage(),cardLoc.x, cardLoc.y, null);
		}
		
		if(warCards.isEmpty() == false) {
			for(int i = 0; i < warCards.size(); i++) {
				if(renderNum != 3) { //Formating for the right most player
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
		}
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getCardNum() {
		return cards;
	}
}
