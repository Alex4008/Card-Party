package main;

import java.util.ArrayList;

import cards.Suit;
import cards.Value;

public class Player {

	Main m;
	String username;
	CardDeck hand;
	public Card cardPlayed;
	ArrayList<Card> warCards;
	boolean isReady = false;
	public boolean isAI = false;
	boolean inWar = false;
	public boolean lost = false;
	
	Player(Main m) {
		this.m = m;
		username = "Undefined";
		cardPlayed = null;
		hand = new CardDeck(m, true);
		warCards = new ArrayList<Card>();
	}
	
	Player(String aiName) {
		this.username = aiName;
		isAI = true;
	}
	
	public void resetPlayer() {
		cardPlayed = null;
		hand = new CardDeck(m, true);
		warCards = new ArrayList<Card>();
		lost = false;
		isAI = false;
		inWar = false;
		isReady = false;
	}
	
	public void tick(Main m) {
		if(!lost) {
			hand.tick(m);	
		}
	}
	
	public boolean getLost() {
		return lost;
	}
	
	public void setLost(boolean value) {
		lost = value;
	}
	
	public void playCard() {
		if(!lost) {
			cardPlayed = hand.getNextCard();
			hand.remove(cardPlayed);	
		}
	}
	
	public String getUserName() {
		return username;
	}
	
	public void setUserName(String us) {
		username = us;
	}
	
	public CardDeck getHand() {
		return hand;
	}
	
	public Card getCurrentCard() {
		return cardPlayed;
	}
	
	public ArrayList<Card> getWarCards() {
		return warCards;
	}
	
	public int getNumCards() {
		return hand.getSize();
	}
	
	public boolean getReadyStatus() {
		return isReady;
	}
	
	public void setReady(boolean value) {
		isReady = value;
	}
	
	public Value getCardPlayedValue() {
		if(cardPlayed == null) return Value.NULL;
		return cardPlayed.getValue();
	}
	
	public Suit getCardPlayedSuit() {
		if(cardPlayed == null) return Suit.NULL;
		return cardPlayed.getSuit();
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
	
	public boolean inWar() {
		return inWar;
	}
	
	public void setInWar(boolean value) {
		inWar = value;
	}
	
	public int getWarCardsSize() {
		return warCards.size();
	}
}
