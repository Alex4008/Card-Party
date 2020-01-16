package main;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import cards.Suit;
import cards.Value;

public class CardDeck {
	
	private ArrayList<Card> cards = new ArrayList<Card>();
	
	//Create fresh deck of cards. Include all cards
	public CardDeck(Main m, boolean empty) {
		if(empty == false) {
			Suit s = Suit.CLUBS;
			while(s.num <= 4) {
				Value v = Value.ACE;
				while(v.num <= 13) {
					cards.add(new Card(m, s, v));
					v = v.nextValue();
					if(v.num == 1) break;
				}
				s = s.nextValue();
				if(s.num == 1) break;
			}
		}
	}
	
	public void Shuffle(int times) {
		int i = 0;
		while(i < times) {
			Shuffle();
			i++;
		}
	}
	
	public void Shuffle() {
		Random rgen = new Random();  // Random number generator			
		 
		for (int i = 0; i < cards.size(); i++) {
		    int randomPosition = rgen.nextInt(cards.size());
		    Card temp = cards.get(i);
		    cards.set(i, cards.get(randomPosition));
		    cards.set(randomPosition, temp);
		}
	}
	
	public void add(Card c) {
		cards.add(c);
	}
	
	public void remove(Card c) {
		cards.remove(c);
	}
	
	public boolean contains(Card c) {
		return cards.contains(c);
	}
	
	public int getSize() {
		return cards.size();
	}
	
	public void tick(Main m) {
	}
	
	public Card getNextCard() {
		return cards.get(0);
	}
	
	public void render(Graphics g) {
	}
	
}
