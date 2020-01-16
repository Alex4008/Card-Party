package cards;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import main.GameType;
import main.Main;

public class CardDeck {
	
	private Point loc;
	private ArrayList<Card> cards = new ArrayList<Card>();
	//Clickable click;
	
	//Create fresh deck of cards. Include all cards
	public CardDeck(Main m, Point loc, boolean empty) {
		this.loc = loc;
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
		//click = new Clickable(loc, Main.cardBack);
	}
	
	public CardDeck(Main m, GameType gt, boolean empty) {
		if(empty == false) {
			if(gt == GameType.WAR || gt == GameType.EUCHRE) {
				//click = new Clickable(loc, Main.cardBack);
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
			else if(gt == GameType.UNO) {
				//click = new Clickable(loc, Main.unoCardBack);
				cards.add(new Card(m, null, UnoColor.RED, UnoValue.ZERO));
				cards.add(new Card(m, null, UnoColor.YELLOW, UnoValue.ZERO));
				cards.add(new Card(m, null, UnoColor.GREEN, UnoValue.ZERO));
				cards.add(new Card(m, null, UnoColor.BLUE, UnoValue.ZERO));

				for(int i = 0; i < 4; i++) {
					cards.add(new Card(m, null, UnoColor.BLACK, UnoValue.WILD));
					cards.add(new Card(m, null, UnoColor.BLACK, UnoValue.DRAWFOUR));
				}
				
				for(int i = 0; i < 2; i++) {
					UnoColor c = UnoColor.RED;
					while(c.num <= 4) { //Generate cards
						UnoValue v = UnoValue.ONE;
						while(v.num <= 12) {
							cards.add(new Card(m, null, c, v));
							v = v.nextValue();
							if(v.num == 0) break;
						}
						c = c.nextValue();
						if(c.num == 1) break;
					}	
				}
			}
		}
		else {
			//click = new Clickable(loc, Main.cardBack);
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
	
	public void addToTop(Card c) {
		cards.add(0, c);
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
		//click.tick(m);
	}
	
	public Card getNextCard() {
		if(cards.size() == 0) return null;
		return cards.get(0);
	}
	
	public boolean isClicked() {
		//return click.isClicked;
		return false;
	}
	
	public void setClicked(boolean value) {
		//click.setClicked(value);
	}
	
	public void render(Graphics g) {
		//click.render(g);
	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}
}
