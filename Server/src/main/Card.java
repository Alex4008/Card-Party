package main;

import java.awt.image.BufferedImage;

import cards.Suit;
import cards.Value;

public class Card {

	private Suit suit;
	private Value value;
	
	Card(Main m, Suit suit, Value value) {
		this.suit = suit;
		this.value = value;
	}
	
	
	public Suit getSuit() {
		return suit;
	}
	
	public Value getValue() {
		return value;
	}
}
