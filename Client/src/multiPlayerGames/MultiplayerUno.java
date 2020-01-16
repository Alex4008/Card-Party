package multiPlayerGames;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import cards.Card;
import cards.CardDeck;
import cards.UnoColor;
import cards.UnoValue;
import graphics.PlayerToken;
import graphics.Sprite;
import graphics.UnoColorIndicator;
import input.UnoColorSelector;
import main.GameState;
import main.GameType;
import main.LoadState;
import main.Main;
import singlePlayerUno.LocalPlayerUno;

public class MultiplayerUno {

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
	
	UnoColorIndicator colorIndicator;
	PlayerToken token;
	
	boolean clockwise;
	UnoColor currentColor;
	UnoValue currentValue;

	ArrayList<LocalPlayerUno> players;
	
	LocalPlayerUno winner; //Used to determine when the game is over.
	int timer; //Used to determine when to close after a winner is found.
	
	boolean drawTwo; //Used to determine if the next player must draw two.
	boolean drawFour; //Used to determine if the next player must draw four.
	boolean skip; //Used to determine if the next player should be skipped.
	
	ArrayList<Integer> playersWithUno; //Used to determine who has uno.
	boolean unoChallanged; //Used to determine if uno has been challanged or not.
	
	public MultiplayerUno(Main m, int numPlayers, int difficulty, int startingPlayer, boolean allowDrawFourAnyTime, boolean allowStackingDrawTwos, boolean drawUntilPlay) {
		this.m = m;
		
		this.difficulty = difficulty;
		this.allowDrawFourAnytime = allowDrawFourAnyTime;
		this.allowStackingDrawTwos = allowStackingDrawTwos;
		this.drawUntilPlay = drawUntilPlay;
		
		winner = null;
		timer = 0;
		drawDeck = new CardDeck(m, new Point(Main.WIDTH / 2 + 5, Main.HEIGHT / 2 - (Main.unoCardBack.getHeight() / 2)), GameType.UNO, false);
		drawDeck.Shuffle(2);
		discard = new CardDeck(m, new Point(Main.WIDTH / 2 - Main.unoCardBack.getWidth() - 5, Main.HEIGHT / 2 - (Main.unoCardBack.getHeight() / 2)), GameType.UNO, true);
		
		playersWithUno = new ArrayList<Integer>();
		unoChallanged = false;
		
		clockwise = true;
		colorIndicator = new UnoColorIndicator(new Point(Main.WIDTH / 2 - Main.unoCardBack.getWidth() - 15, Main.HEIGHT / 2 - (Main.unoCardBack.getHeight() / 2) - 20), new Point(Main.WIDTH / 2 - Main.unoCardBack.getWidth() - 10, Main.HEIGHT / 2 + Main.unoCardBack.getHeight() - (Main.unoCardBack.getHeight() / 2) + 10));
		
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
		
		//token = new PlayerToken(GameType.UNO, this, startingPlayer);
		
		players = new ArrayList<LocalPlayerUno>();
		
		// Create players
		for(int i = 0; i < numPlayers; i++) {
			//players.add(new LocalPlayerUno(m, this, i));
		}
		gameSetup();
	}
	
	public void tick(Main m) {
		
		if(winner != null) {
			if(timer == 0) {
				for(LocalPlayerUno p : players) p.sendWinMsg();
			}
			timer++;
			
			if(timer == TIME_BEFORE_CLOSE) LoadState.Load(m, GameState.MAIN_MENU);
			
			return; //Game is over, no need to continue.
		}
		
		drawDeck.tick(m);
		discard.tick(m);
		colorIndicator.tick(m);
		token.tick(m);
		
		if(drawDeck.getSize() == 0) {
			resetDiscard();
			sendMsgToAllPlayers("The discard has been reshuffled in!");
		}
		
		if(discard.getNextCard().getUnoColor() != UnoColor.BLACK) currentColor = discard.getNextCard().getUnoColor(); //Do not override the color selector
		currentValue = discard.getNextCard().getUnoValue();
		colorIndicator.setColor(currentColor);
		
		for(LocalPlayerUno p : players) {
			p.tick(m);
		}
		
		for(LocalPlayerUno p : players) {
			if(p.hasUno() && p.hasCalledUno() == false && unoChallanged) {
				drawPlayerCards(p, 2);
				sendMsgToAllPlayers("Player " + p.getPlayerID() + " failed to call Uno!");
				p.sendSplashMsg("You didn't call Uno! Draw 2 cards!");
				p.canCallUno = false; //They can no longer call uno after being challenged successfully
			}
			
			if(p.hasCalledUno() && p.canCallUno == true) {
				if(p.hasUno()) {
					sendMsgToAllPlayers("Player " + p.getPlayerID() + " has Uno!");
					p.canCallUno = false;
				}
			}
			
			if(p.isTurn() && p.isTurnComplete()) nextTurn();
			if(p.hasWon()) winner = p;
		}
		
		unoChallanged = false;
		drawDeck.setClicked(false);
	}
	
	public void render(Main m, Graphics g) {
		drawDeck.render(g);
		if(discard.getNextCard() != null) {
			Card c = discard.getNextCard();
			g.drawImage(c.getImage(), Main.WIDTH / 2 - Main.unoCardBack.getWidth() - 5, Main.HEIGHT / 2 - (Main.unoCardBack.getHeight() / 2), null);	
		}
		colorIndicator.render(m, g);
		token.render(m, g);
		for(LocalPlayerUno p : players) p.render(m, g);
		// Render arrows
		if(clockwise) {
			g.drawImage(Main.unoArrow, Main.WIDTH / 2 - Main.unoCardBack.getWidth() * 2 - 20, Main.HEIGHT / 2 + 30, null);
			g.drawImage(Sprite.rotate(Main.unoArrow, 180), Main.WIDTH / 2 + Main.unoCardBack.getWidth() + 30, Main.HEIGHT / 2 - (Main.unoCardBack.getHeight() / 2) - 30, null);
		}
		else {
			g.drawImage(Sprite.rotate(Sprite.flipVertical(Main.unoArrow), 270), Main.WIDTH / 2 - Main.unoCardBack.getWidth() * 2 - 20, Main.HEIGHT / 2 + 30, null);
			g.drawImage(Sprite.rotate(Sprite.flipVertical(Main.unoArrow), 90), Main.WIDTH / 2 + Main.unoCardBack.getWidth() + 30, Main.HEIGHT / 2 - (Main.unoCardBack.getHeight() / 2) - 30, null);
		}
		token.render(m, g);
	}
	
	private void gameSetup() {
		//Give each player in the game 7 cards
		for(int i = 0; i < 7; i++) {
			for(LocalPlayerUno p : players) {
				Card c = drawDeck.getNextCard();
				p.givePlayerCard(c);
				drawDeck.remove(c);
				if(p.getPlayerID() == token.getLocation()) p.startTurn();
			}
		}
		
		sendMsgToAllPlayers("Player " + token.getLocation() + " goes first!");
		
		Card c = drawDeck.getNextCard();
		
		while(c.getUnoValue() == UnoValue.DRAWFOUR) {
			drawDeck.remove(c);
			drawDeck.add(c);
			c = drawDeck.getNextCard();
		}
		discard.add(c);
		drawDeck.remove(c);
		
		if(c.getUnoValue() == UnoValue.WILD) {
			getPlayerByID(token.getLocation()).selectingColor = true;
			getPlayerByID(token.getLocation()).firstPriv = true;
		}
		else if(c.getUnoValue() == UnoValue.DRAWTWO) {
			drawTwoNextPlayer();
			nextTurn();
		}
		else if(c.getUnoValue() == UnoValue.SKIP) {
			skipNextPlayer();
			nextTurn();
		}
		else if(c.getUnoValue() == UnoValue.REVERSE) {
			reverseGame();
			nextTurn();
		}
	}
	
	private void nextTurn() {
		getPlayerByID(token.getLocation()).endTurn(); //End last players turn.
		
		if(getPlayerByID(token.getLocation()).hasCalledUno()) {
			if(!getPlayerByID(token.getLocation()).hasUno()) {
				getPlayerByID(token.getLocation()).calledUno = false;
			}
			else {
				sendMsgToAllPlayers("Player " + token.getLocation() + " has Uno!");
				getPlayerByID(token.getLocation()).canCallUno = false;
			}
		}
		
		token.nextPos(clockwise, getPlayerCount());
		
		//If a skip was played, skip the next pos.
		if(skip) {
			skip = false;
			getPlayerByID(token.getLocation()).sendSplashMsg("You were skipped!");
			token.nextPos(clockwise, getPlayerCount());
		}
		
		//Make the next player draw two cards, then skip them
		if(drawTwo) {
			// If the player can stack a draw two, let them play. AND stacking draw twos is enabled via the settings
			if(getPlayerByID(token.getLocation()).canStackTwo() && allowStackingDrawTwos) {
				currentStackNum = 2;
				getPlayerByID(token.getLocation()).sendTwoLinedSplashMsg("You can stack another +2 on that one!", "If you don't you will draw " + currentStackNum);
			}
			// Make them draw two cards and skip them as usual.
			else {
				drawTwo = false;
				if(currentStackNum != 0) {
					drawPlayerCards(getPlayerByID(token.getLocation()), currentStackNum);
					getPlayerByID(token.getLocation()).sendTwoLinedSplashMsg("You had to draw " + currentStackNum + " cards!", (currentStackNum / 2) + "draw twos were played!");
					currentStackNum = 0;
				}
				else {
					drawPlayerCards(getPlayerByID(token.getLocation()), 2);
					getPlayerByID(token.getLocation()).sendSplashMsg("You had to draw two cards!");	
				}
				token.nextPos(clockwise, getPlayerCount()); //Skip the next player due to drawing cards	
			}
		}
		
		//Make the next player draw four cards, then skip them
		if(drawFour) {
			drawFour = false;
			drawPlayerCards(getPlayerByID(token.getLocation()), 4);
			getPlayerByID(token.getLocation()).sendSplashMsg("You had to draw four cards!");
			token.nextPos(clockwise, getPlayerCount()); //Skip the next player due to drawing cards
		}
		
		getPlayerByID(token.getLocation()).startTurn(); //Start next players turn
	}
	
	public boolean cardMatch(Card c) {
		if(c.getUnoColor() == currentColor) return true;
		
		if(c.getUnoValue() == currentValue) return true;
		
		if(c.getUnoValue() == UnoValue.DRAWFOUR || c.getUnoValue() == UnoValue.WILD) return true;
		
		return false;
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public void addToDiscard(Card c) {
		discard.addToTop(c);
	}
	
	public boolean isDeckClicked() {
		return drawDeck.isClicked();
	}
	
	public Card drawCard() {
		Card draw = drawDeck.getNextCard();
		drawDeck.remove(draw);
		return draw;
	}
	
	public void reverseGame() {
		clockwise = !clockwise;
		sendMsgToAllPlayers("The game has been reversed!");
	}
	
	public void skipNextPlayer() {
		skip = true;
	}
	
	public void drawFourNextPlayer() {
		drawFour = true;
	}
	
	public void drawTwoNextPlayer() {
		if(drawTwo == true) currentStackNum += 2;
		else drawTwo = true;
	}
	
	public void challangeUno() {
		unoChallanged = true;
	}
	
	public void changeCurrentColor(UnoColor uc) {
		currentColor = uc;
		currentValue = UnoValue.WILD;
		colorIndicator.setColor(currentColor);
		if(uc == UnoColor.RED) sendMsgToAllPlayers("Red!");
		else if(uc == UnoColor.GREEN) sendMsgToAllPlayers("Green!");
		else if(uc == UnoColor.YELLOW) sendMsgToAllPlayers("Yellow!");
		else if(uc == UnoColor.BLUE) sendMsgToAllPlayers("Blue!");
	}
	
	public LocalPlayerUno getPlayerByID(int id) {
		for(LocalPlayerUno p : players) {
			if(p.getPlayerID() == id) return p;
		}
		
		return null; //Player does not exist
	}

	public int getCurrentStackAmount() {
		return currentStackNum;
	}
	
	//This is used by a human player class when the opt to pass stacking a draw 2
	public void resetCurrentStackAmount() {
		drawTwo = false;
		currentStackNum = 0;
	}
	
	public boolean isDrawToPlayEnabled() {
		return drawUntilPlay;
	}
	
	public boolean allowDrawFourAnytime() {
		return allowDrawFourAnytime;
	}
	
	public void drawPlayerCards(LocalPlayerUno player, int amount) {
		for(int i = 0; i < amount; i++) player.givePlayerCard(drawCard());
		if(player.handSize() > 1) player.setHasUno(false);
	}
	
	public int getPlayerCount() {
		return players.size();
	}
	
	private void sendMsgToAllPlayers(String msg) {
		for(LocalPlayerUno p : players) p.sendSplashMsg(msg);
	}
	
	private void resetDiscard() {
		// Move all discard cards to draw deck
		for(Card c : discard.getCards()) {
			drawDeck.add(new Card(m, null, c.getUnoColor(), c.getUnoValue()));
		}
		// Reset discard
		discard = new CardDeck(m, new Point(Main.WIDTH / 2 - Main.unoCardBack.getWidth() - 5, Main.HEIGHT / 2 - (Main.unoCardBack.getHeight() / 2)), GameType.UNO, true);
		
		// Shuffle draw deck
		drawDeck.Shuffle(2);
	}
}
