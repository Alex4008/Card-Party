package connection;

import java.io.Serializable;
import java.util.ArrayList;

import cards.Card;
import cards.UnoColor;
import cards.UnoValue;

public class UnoInformation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3213622985510236458L;
	
	public int numPlayers;
	public boolean clockwise;
	public boolean allowAnyTimeFour;
	public boolean stackTwos;
	public boolean drawUntilPlay;
	public ArrayList<Integer> playersWithUno;
	public boolean challangeUno;
	public int currentPlayer;
	public UnoColor color;
	public UnoValue value;
	public ArrayList<Card> hand;
	public ArrayList<Card> deck;
	public int cardsInHand;
	public int cardsInDeck;

	public UnoInformation() {
		
	}
	
}
