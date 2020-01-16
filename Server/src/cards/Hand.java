package cards;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import main.Main;

public class Hand {

	//private final int SIZE_OF_HAND = 450 - (Main.unoCardBack.getWidth() / 2);
	
	Main m;
	Point loc;
	ArrayList<Card> cards;
	int posNum;
	boolean showCards;
	int priorityCard; //Used to determine which card in the arraylist has priority
	int prevHandSize; //Used to determine when the locations of the cards need updated.
	int totalLengthOfHand; //This is the length in pixels from the start of the first card, to the end of the last one
	
	public Hand(Main m, Point loc, int posNum, boolean showCards) {
		this.m = m;
		this.loc = loc;
		cards = new ArrayList<Card>();
		this.posNum = posNum;
		this.showCards = showCards;
		priorityCard = -1;
		prevHandSize = 0;
		totalLengthOfHand = 0;
	} 
	
	public void tick(Main m) {
		/*
		// Update locations of cards in hand
		updateLocations();
		for(Card c : cards) c.tick(m);
		 
		priorityCard = -1;
		
		for(int i = 0; i < cards.size(); i++) if(cards.get(i).getPriority()) priorityCard = i;
		
		for(int i = cards.size(); i > 0; i--) {
			if(cards.get(i - 1).isHover() && priorityCard == -1) {
				cards.get(i - 1).setPriority(true);
			}
		}
		
		if(priorityCard != -1 && cards.size() > 5) {
			int spaceBetween = SIZE_OF_HAND / cards.size() + 1;
			int diff = Math.abs(spaceBetween - Main.unoCardBack.getWidth());
			int x = loc.x - 200 + diff;
			
			for(int i = 0; i < priorityCard + 1; i++) x += spaceBetween;
			
			for(int i = priorityCard + 1; i < cards.size(); i++) {
				cards.get(i).setLocation(new Point(x, cards.get(i).getLocation().y));
				cards.get(i).updateBoundingBox(new Point(x, cards.get(i).getLocation().y));
				x += spaceBetween;
			}	
		}
		
		updateHandLength();
		*/
	}
	
	public void render(Main m, Graphics g) {
		/*
		if(showCards) {
			for(Card c : cards) {
				c.render(m, g);
			}
			
			for(int i = priorityCard + 1; i < cards.size(); i++) {
				cards.get(i).render(m, g);
			}	
			
			for(Card c : cards) {
				if(c.getPriority()) { 
					c.render(m, g);
					g.drawRoundRect(c.getLocation().x, c.getLocation().y, Main.unoCardBack.getWidth(), Main.unoCardBack.getHeight(), 20, 20);
				}
			}
		}
		else {
			BufferedImage card = Main.unoCardBack;
			
			if(posNum == 1) card = Sprite.rotate(card, 180);
			else if(posNum == 2) card = Sprite.rotate(card, 90);
			else if(posNum == 3) card = Sprite.rotate(card, 270);
			
			for(Card c : cards) {
				g.drawImage(card, c.getLocation().x, c.getLocation().y, null);
			}
		}
		*/
	}
	
	
	public void addCard(Card c) {
		cards.add(c);
		updateLocations();
	}
	
	public void addCard(UnoColor c, UnoValue v) {
		cards.add(new Card(m, null, c, v));
		updateLocations();
	}
	
	public void removeCard(Card c) {
		cards.remove(c);
		updateLocations();
	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public int getHandLength() {
		return totalLengthOfHand;
	}
	
	public Point getHandLocation() {
		return loc;
	}
	
	public void updateHandLength() {
		/*
		if(posNum == 0 || posNum == 1) {
			if(cards.size() == 0) totalLengthOfHand = 0;
			else totalLengthOfHand = Math.abs(cards.get(0).getLocation().x - cards.get(cards.size() - 1).getLocation().x) + Main.unoCardBack.getWidth();	
		}
		else {
			if(cards.size() == 0) totalLengthOfHand = 0;
			else totalLengthOfHand = Math.abs(cards.get(0).getLocation().y - cards.get(cards.size() - 1).getLocation().y) + Main.unoCardBack.getWidth();
		}
		*/
	}
	
	public void sortByColor() {
		int index = 0;
		for(int i = 1; i < 6; i++) {
			for(int j = 0; j < cards.size(); j++) {
				if(cards.get(j).getUnoColor().equals(UnoColor.intToUnoColor(i))) {
					switchCards(j, index);
					index++;
				}
			}
		}
	}
	
	public void sortByValue() {
		int index = 0;
		for(int i = 0; i < 12; i++) {
			for(int j = 0; j < cards.size(); j++) {
				if(cards.get(j).getUnoValue().equals(UnoValue.intToValue(i))) {
					switchCards(j, index);
					index++;
				}
			}
		}
	}
	
	private void switchCards(int one, int two) {
		Card c1 = cards.get(one);
		Card c2 = cards.get(two);
		cards.set(two, c1);
		cards.set(one,  c2);
	}
	
	private void updateLocations() {
		/*
		if(priorityCard != -1) {
			if(cards.size() == prevHandSize) return;	
		}

		prevHandSize = cards.size();
		if(cards.size() != 0) {
			int spaceBetween = SIZE_OF_HAND / cards.size() + 1;
			if(posNum == 0 || posNum == 1) {
				int x = loc.x - 200;
				for(Card c : cards) {
					c.setLocation(new Point(x, loc.y));
					c.updateBoundingBox(new Point(x, loc.y));
					x += spaceBetween;
				}	
			}
			else {
				int y = loc.y - 200;
				for(Card c : cards) {
					c.setLocation(new Point(loc.x, y));
					c.updateBoundingBox(new Point(loc.x, y));
					y += spaceBetween;
				}	
			}
		}
		*/
	}
}
