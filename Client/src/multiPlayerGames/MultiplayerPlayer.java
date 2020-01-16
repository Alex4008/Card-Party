package multiPlayerGames;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import cards.Card;
import cards.Suit;
import cards.Value;
import input.Clickable;
import main.Main;

public class MultiplayerPlayer {

	int cards;
	String username;
	boolean ready;
	Card cardPlayed;
	Point loc;
	Point cardLoc;
	Clickable hand;
	int cardsInHand;
	int playerId;
	private ArrayList<Card> warCards;
	Main m;
	boolean gameLost = false;
	
	public MultiplayerPlayer(Main m, String username, int cards, int playerID) {
		this.m = m;
		this.username = username;
		this.cards = cards;
		this.playerId = playerID;
		ready = false;
		cardPlayed = null;
		loc = new Point(Main.WIDTH / 2 - (Main.cardBack.getWidth() / 2), Main.HEIGHT / 5 * 4);
		cardLoc = new Point(loc.x, loc.y - 100);
		hand = new Clickable(loc, Main.cardBack);
		cardsInHand = 0;
		warCards = new ArrayList<Card>();
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
	
	public int getPlayerId() {
		return playerId;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String value) {
		username = value;
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
	
	public void setGameOver() {
		gameLost = true;
		JOptionPane.showMessageDialog(Main.frame, "You have run out of cards!", "Game Over!", JOptionPane.ERROR_MESSAGE);
	}
	
	public void setReadyStatus(boolean value) {
		ready = value;
	}
	
	public void tick(Main m) {
		if(!gameLost) {
			hand.tick(m);
			
			if(hand.getClicked()) {
				ready = true;
				hand.setClicked(false);
			}	
		}
	}
	
	public void render(Graphics g) {
		if(cardsInHand != 0) hand.render(g);
		
		if(!gameLost) {
			g.drawString("Cards: " + cardsInHand, loc.x + Main.CARD_WIDTH + 15, loc.y + 40);
		}
		else g.drawString("has run out of cards!", loc.x + Main.CARD_WIDTH + 15, loc.y + 40);
		
		g.drawString(username, loc.x + Main.CARD_WIDTH + 15, loc.y + 20);
		
		if(cardPlayed != null) {
			g.drawImage(cardPlayed.getImage(),cardLoc.x, cardLoc.y, null);
		}
		
		if(warCards.isEmpty() == false) {
			for(int i = 0; i < warCards.size(); i++) {
				if(playerId != 3) { //Formating for the right most player
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
	
}
