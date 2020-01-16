package connection;

import java.io.Serializable;
import java.util.ArrayList;

import cards.Suit;
import cards.Value;

public class WarInformation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8547254768277016431L;
	public String status;
	public int numConnections;
	public int clientID;
	public String username;
	public int numCards;
	
	public String winner;
	public String loser;
	
	public ArrayList<String> otherUsernames;
	public ArrayList<Integer> otherNumCards;
	
	public ArrayList<Suit> cardSuits;
	public ArrayList<Value> cardValues;
	public ArrayList<Boolean> isAI;
	
	public ArrayList<Integer> warCardsSize;
	public ArrayList<Suit> warSuits;
	public ArrayList<Value> warValues;
	
	public boolean isReady;
	
	public WarInformation() {
		
	}
}
