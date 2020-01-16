package games;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import cards.Card;
import cards.CardDeck;
import cards.UnoColor;
import cards.UnoValue;
import connection.UnoInformation;
import main.GameType;
import main.Main;

public class Uno extends Game {

	private final int STARTING_PLAYER = 0;
	private final int TIME_BEFORE_CLOSE = 300; //Measured in ticks, 20 ticks = 1 second
	
	
	public int difficulty;
	boolean allowDrawFourAnytime;
	boolean allowStackingDrawTwos;
	int currentStackNum; //Used to determine how many draw twos have been stacked.
	boolean drawUntilPlay;
	
	Main m; //Reference to Main class
	
	CardDeck drawDeck;
	CardDeck discard;
	
	boolean waitingForColor;
	
	boolean clockwise;
	UnoColor currentColor;
	UnoValue currentValue;

	int timer; //Used to determine when to close after a winner is found.
	
	boolean drawTwo; //Used to determine if the next player must draw two.
	boolean drawFour; //Used to determine if the next player must draw four.
	boolean skip; //Used to determine if the next player should be skipped.
	
	ArrayList<Integer> playersWithUno; //Used to determine who has uno.
	boolean unoChallanged; //Used to determine if uno has been challanged or not.
	
	int currentPlayer;
	
	UnoInformation info;
	
	public Uno(Main m, int numPlayers, int difficulty, int startingPlayer, boolean allowDrawFourAnyTime, boolean allowStackingDrawTwos, boolean drawUntilPlay) {
		this.m = m;
		
		info = new UnoInformation();
		
		this.difficulty = difficulty;
		this.allowDrawFourAnytime = allowDrawFourAnyTime;
		this.allowStackingDrawTwos = allowStackingDrawTwos;
		this.drawUntilPlay = drawUntilPlay;
		
		timer = 0;
		drawDeck = new CardDeck(m, GameType.UNO, false);
		drawDeck.Shuffle(2);
		discard = new CardDeck(m, GameType.UNO, true);
		
		playersWithUno = new ArrayList<Integer>();
		unoChallanged = false;
		
		clockwise = true;
		//colorIndicator = new UnoColorIndicator(new Point(Main.WIDTH / 2 - Main.unoCardBack.getWidth() - 15, Main.HEIGHT / 2 - (Main.unoCardBack.getHeight() / 2) - 20), new Point(Main.WIDTH / 2 - Main.unoCardBack.getWidth() - 10, Main.HEIGHT / 2 + Main.unoCardBack.getHeight() - (Main.unoCardBack.getHeight() / 2) + 10));
		
		// If the starting player is -1, then random selection was selected.
		if(startingPlayer == -1) {
			while(startingPlayer == -1) {
				int rng = (int) (Math.random() * 100 + 1);
				if(rng <= 25) startingPlayer = 0;	
				else if(rng <= 50) startingPlayer = 1;
				else if(rng <= 75) startingPlayer = 2;
				else startingPlayer = 3;
				
				if(startingPlayer > (numPlayers - 1)) startingPlayer = -1;
			}
		}
		
		currentPlayer = startingPlayer;
		//gameSetup();
	}
	
	@Override
	public void tick(Main m) {
		info.currentPlayer = currentPlayer;
		info.clockwise = clockwise;
		info.playersWithUno = playersWithUno;
		info.challangeUno = unoChallanged;
		info.color = currentColor;
		info.value = currentValue;
		info.cardsInDeck = drawDeck.getSize();
		
		if(drawDeck.getSize() == 0) {
			resetDiscard();
			sendMsgToAllPlayers("The discard has been reshuffled in!");
		}
	}

	@Override
	public void render(Main m, Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
	private void sendMsgToAllPlayers(String msg) {
		//for(info. p : players) p.sendSplashMsg(msg);
	}
	
	private void resetDiscard() {
		// Move all discard cards to draw deck
		for(Card c : discard.getCards()) {
			drawDeck.add(new Card(m, null, c.getUnoColor(), c.getUnoValue()));
		}
		// Reset discard
		//discard = new CardDeck(m, new Point(Main.WIDTH / 2 - Main.unoCardBack.getWidth() - 5, Main.HEIGHT / 2 - (Main.unoCardBack.getHeight() / 2)), GameType.UNO, true);
		
		// Shuffle draw deck
		drawDeck.Shuffle(2);
	}
}
