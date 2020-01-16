package singlePlayerUno;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import cards.Card;
import cards.Hand;
import cards.UnoColor;
import cards.UnoValue;
import graphics.Colors;
import graphics.SplashMsg;
import input.ActionButton;
import input.UnoColorSelector;
import main.Main;

public class LocalPlayerUno {

	private final int AI_WAIT_TIME = 60; //The amount of time an AI waits before playing in ticks. 20 ticks = 1 second
	private final int REMIND_TIME = 300; //Remind the player that it is their turn, after 15 seconds of inactivity.
	
	Main m; //Reference to Main class
	LocalUno game; //Reference to the Uno game
	
	Hand hand;
	SplashMsg msgBox;
	ActionButton callUno;
	ActionButton challangeUno;
	ActionButton sortByColor;
	ActionButton sortByValue;
	ActionButton pass;
	UnoColorSelector colorPick;
	
	int playerID; //An integer from 0 to 3. 0 Is the main player
	
	int timer; //Used to have AI wait before playing AND for reminding a human player that it is their turn
	
	int aiDifficulty; // 0 = easy, 1 = medium, 2 = hard.
	
	boolean isTurn; //Used to determine if it is this players turn.
	boolean complete; //Used to determine if a players turn has been completed.
	public boolean selectingColor; //Used to determine if the color picker should be displayed
	private boolean hasUno; //Used to determine if the player has uno.
	public boolean calledUno; //Flag that is set to true when the player calls uno.
	public boolean canCallUno; // Flag that is used to determine if the player can still call uno
	boolean won; //Used to determine if the player is out of cards, and therefore has won the game.
	public boolean firstPriv; //Used to determine if this player starts the game, and if enabled, they are to pick the color.
	boolean deckClicked; //Used to determine if the player has clicked the deck, and if they have it prevents them from doing so again.
	boolean forceStackTwo; //Used to determine if the player MUST play a draw two card, or pass their turn.
	
	public LocalPlayerUno(Main m, LocalUno game, int playerID) {
		this.m = m;
		this.game = game;
		this.playerID = playerID;
		timer = 0;
		isTurn = false;
		selectingColor = false;
		canCallUno = true;
		deckClicked = false;
		forceStackTwo = false;
		
		this.aiDifficulty = game.getDifficulty();
		
		if(playerID == 0) {
			callUno = new ActionButton("Uno!", new Rectangle(Main.WIDTH / 2 - 200, 480, 100, 75), Color.LIGHT_GRAY);
			challangeUno = new ActionButton("Challange Uno!", new Rectangle(Main.WIDTH / 2 - 95, 480, 150, 75), Color.LIGHT_GRAY);
			sortByColor = new ActionButton("Sort By Color", new Rectangle(Main.WIDTH / 2 + 60, 480, 150, 32), Color.LIGHT_GRAY);
			sortByValue = new ActionButton("Sort By Value", new Rectangle(Main.WIDTH / 2 + 60, 480 + 41, 150, 32), Color.LIGHT_GRAY);
			pass = new ActionButton("Pass Turn", new Rectangle(Main.WIDTH / 2 + 60, 480, 150, 75), Color.LIGHT_GRAY);
			colorPick = new UnoColorSelector(new Rectangle(Main.WIDTH / 2 - 200, 480, 400, 75));
		}
		else {
			callUno = null;
			challangeUno = null;
			sortByColor = null;
			sortByValue = null;
			colorPick = null;
			pass = null;
		}
		
		//Create hand
		switch(playerID) {
		case 0:
			hand = new Hand(m, new Point(Main.WIDTH / 2 - (Main.unoCardBack.getWidth() / 4), Main.HEIGHT / 5 * 4), 0, true);
			break;
		case 1:
			hand = new Hand(m, new Point(Main.WIDTH / 10 - Main.unoCardBack.getHeight(), Main.HEIGHT / 2 - (Main.unoCardBack.getWidth() / 2)), 2, false);
			break;
		case 2:
			hand = new Hand(m, new Point(Main.WIDTH / 2 - (Main.unoCardBack.getWidth() / 4), Main.HEIGHT / 5 - (Main.unoCardBack.getHeight() / 3 * 4)), 1, false);
			break;
		case 3:
			hand = new Hand(m, new Point(Main.WIDTH / 10 * 8 + (Main.unoCardBack.getHeight() / 3 * 2), Main.HEIGHT / 2 - (Main.unoCardBack.getWidth() / 2)), 3, false);
			break;
		default:
			break;
		}
		
	}
	
	public void tick(Main m) {
		if(msgBox != null) msgBox.tick(m);
		if(hand.getCards().isEmpty()) won = true;
		if(won) return;
		if(playerID == 0) {
			hand.tick(m);
			
			if(selectingColor == false) {
				callUno.tick(m);
				challangeUno.tick(m);	
				if(forceStackTwo) {
					pass.tick(m);
				}
				else {
					sortByColor.tick(m);
					sortByValue.tick(m);	
				}
				
				if(callUno.getClicked()) {
					if(canCallUno) calledUno = true;
				}
				else if(challangeUno.getClicked()) {
					game.challangeUno();
				}
				else if(sortByColor.getClicked()) {
					hand.sortByColor();
				}
				else if(sortByValue.getClicked()) {
					hand.sortByValue();
				}
				
			}
			else colorPick.tick(m);
			
			if(!isTurn) return;
			
			runHumanTurn(); //Run the turn for the human player
			
		}
		else {
			hand.updateHandLength();

			if(!isTurn) return;
			if(timer > AI_WAIT_TIME) {
				if(aiDifficulty == 0) runEasyAITurn(); //Run AI turn
				else if(aiDifficulty == 1) runNormalAITurn();
				else runHardAITurn();
			}
			timer++;
		}
	}
	
	public void render(Main m, Graphics g) {
		hand.render(m, g);
		if(playerID == 0) { // Render main player stuff
			if(msgBox != null) msgBox.render(m, g);	
			
			if(selectingColor == true) colorPick.render(m, g);
			else {
				callUno.render(m, g);
				challangeUno.render(m, g);
				if(forceStackTwo) {
					pass.render(m, g);
				}
				else {
					sortByColor.render(m, g);
					sortByValue.render(m, g);	
				}
			}
		}
	}
	
	public void givePlayerCard(Card c) {
		hand.addCard(c);
	}
	
	public boolean isTurnComplete() {
		return complete;
	}
	
	public boolean isTurn() {
		return isTurn;
	}
	
	public void startTurn() {
		deckClicked = false;
		isTurn = true;
	}
	
	public void endTurn() {
		if(hand.getCards().size() == 1) setHasUno(true);
		else setHasUno(false);
		isTurn = false;
		complete = false;
		timer = 0;
		forceStackTwo = false;
	}
	
	public int getPlayerID() {
		return playerID;
	}
	
	public boolean hasWon() {
		return won;
	}
	
	public boolean hasUno() {
		return hasUno;
	}
	
	public boolean hasCalledUno() {
		return calledUno;
	}
	
	public boolean canCallUno() {
		return canCallUno;
	}
	
	public void sendWinMsg() {
		if(playerID != 0) return;
		if(won) msgBox = new SplashMsg("You have won Uno!", "Returning in 30 seconds", new Rectangle(2, Main.HEIGHT / 10 * 9, 354, 70), 300, true);
		else msgBox = new SplashMsg("You have lost!", "Returning in 30 seconds", new Rectangle(2, Main.HEIGHT / 10 * 9, 354, 70), 300, true);
	}
	
	public void sendSplashMsg(String msg) {
		sendSplashMsg(msg, 100, true);
	}
	
	public void sendSplashMsg(String msg, boolean dismissable) {
		sendSplashMsg(msg, -1, dismissable);
	}
	
	public void sendSplashMsg(String msg, int fadeoutTime, boolean dismissable) {
		if(playerID != 0) return;
		if(dismissable) msgBox = new SplashMsg(msg, new Rectangle(2, Main.HEIGHT / 10 * 9, 354, 70), fadeoutTime, true);
		else msgBox = new SplashMsg(msg, "", new Rectangle(2, Main.HEIGHT / 10 * 9, 354, 70), fadeoutTime, true);
	}
	
	public void sendTwoLinedSplashMsg(String msg1, String msg2) {
		if(playerID != 0) return;
		msgBox = new SplashMsg(msg1, msg2, new Rectangle(2, Main.HEIGHT / 10 * 9, 354, 70), 100, false);
	}
	
	public Point getHandLocation() {
		return hand.getHandLocation();
	}
	
	public int getCurrentHandWidth() {
		return hand.getHandLength();
	}
	
	public int handSize() {
		return hand.getCards().size();
	}
	
	public void setHasUno(boolean value) {
		hasUno = value;
		canCallUno = true;
	}
	
	public boolean canStackTwo() {
		for(Card c : hand.getCards()) if(c.getUnoValue() == UnoValue.DRAWTWO) return true;
		return false;
	}
	
	public void forceStackTwo() {
		forceStackTwo = true;
	}
	
	private void runHumanTurn() {
		if(timer > REMIND_TIME) { 
			sendSplashMsg("Hey! It's your turn!");
			timer = 0;
		}
		timer++;
		
		//If this player goes first, and a wild card was turned up, have them select a color.
		if(firstPriv && selectingColor) {
			sendSplashMsg("You get to pick the color!", false);
			if(colorPick.getSelectedColor() != UnoColor.NULL) {
				game.changeCurrentColor(colorPick.getSelectedColor());
				colorPick.reset();
				firstPriv = false;
				selectingColor = false;
			}
			//They must select a color before they can play.
			return;
		}
		
		if(selectingColor) {
			//If the selected color is not null, then the player has selected a color.
			if(colorPick.getSelectedColor() != UnoColor.NULL) {
				game.changeCurrentColor(colorPick.getSelectedColor());
				colorPick.reset();
				selectingColor = false;
				complete = true;
			}
			//If they are selecting color, they have already played, so return.
			return;
		}
		
		Card cardBeingPlayed = null;
		
		//In this case, the game has told the player that they can and must stack a draw two
		if(forceStackTwo) {
			
			if(pass.getClicked()) {
				game.drawPlayerCards(this, game.getCurrentStackAmount());
				game.resetCurrentStackAmount();
				complete = true;
			}
			
			for(Card c : hand.getCards()) {
				if(c.isHover() && m.getClicked()) cardBeingPlayed = c;
			}
			
			if(cardBeingPlayed != null) {
				//Check to see if card is a draw two
				if(cardBeingPlayed.getUnoValue() == UnoValue.DRAWTWO) {
					hand.removeCard(cardBeingPlayed);
					game.addToDiscard(cardBeingPlayed);
					
					checkForCardEffects(cardBeingPlayed); //This checks the card that was played and modifies game if needed
					complete = true;
				}
			}
			
			return;
		}
		
		for(Card c : hand.getCards()) {
			if(c.isHover() && m.getClicked()) { 
				if(!game.allowDrawFourAnytime() && c.getUnoValue() == UnoValue.DRAWFOUR) {
					boolean allowed = true;
					for(Card cs : hand.getCards()) {
						if(cs.getUnoValue() != UnoValue.DRAWFOUR) {
							if(game.cardMatch(cs)) allowed = false;
						}
					}
					if(!allowed) {
						sendSplashMsg("You cannot play that card!");
						return;
					}
				}
				cardBeingPlayed = c;
			}
		}
		
		if(cardBeingPlayed != null) {
			//Check to see if card matches discard pile
			if(game.cardMatch(cardBeingPlayed)) {
				hand.removeCard(cardBeingPlayed);
				game.addToDiscard(cardBeingPlayed);
				
				checkForCardEffects(cardBeingPlayed); //This checks the card that was played and modifies game if needed
				
				//If the card is a wild card, enable color picked.
				if(cardBeingPlayed.getUnoColor() == UnoColor.BLACK) selectingColor = true;
				else complete = true;
				return;
			}
		}
		
		//If no card has been played, check to see if they clicked the deck
		if(cardBeingPlayed == null && deckClicked == false) {
			if(game.isDeckClicked()) {
				hand.addCard(game.drawCard());
				deckClicked = true;
			}
			return;
		}
		
		boolean playableCard = false;
		
		for(Card c : hand.getCards()) {
			if(game.cardMatch(c)) playableCard = true;
		}
		
		//Little hack to make this work, if draw to play is enabled, set the deck clicked to false.
		if(game.isDrawToPlayEnabled()) deckClicked = false;
		
		if(playableCard == false && deckClicked == true) {
			complete = true;
		}
	}
	
	private void runEasyAITurn() {
		//Decide if they wanna call uno or challange
		
		int rng = (int) (Math.random() * 100 + 1);

		if(rng <= 50) {
			if(canCallUno) calledUno = true;
		}
		
		rng = (int) (Math.random() * 100 + 1);
		
		if(rng <= 20) {
			game.challangeUno();
		}
		
		Card cardBeingPlayed = null;
		//In this case, the game has told the player that they can and must stack a draw two
		if(forceStackTwo) {
			
			rng = (int) (Math.random() * 100 + 1);

			if(rng <= 50) { //50% chance they will pass it
				game.drawPlayerCards(this, game.getCurrentStackAmount());
				game.resetCurrentStackAmount();
				complete = true;
			}
			else {
				for(Card c : hand.getCards()) {
					if(c.getUnoValue() == UnoValue.DRAWTWO) cardBeingPlayed = c;
				}
				hand.removeCard(cardBeingPlayed);
				game.addToDiscard(cardBeingPlayed);
				
				checkForCardEffects(cardBeingPlayed); //This checks the card that was played and modifies game if needed
				complete = true;
			}
			
			return;
		}
		
		//If this AI goes first, and a wild card was turned up, have them select a color.
		if(firstPriv && selectingColor) {
			firstPriv = false;
			selectingColor = false;
			//Random selection on easy
			
			rng = (int) (Math.random() * 100 + 1);
			
			if(rng <= 25) game.changeCurrentColor(UnoColor.RED);
			else if(rng <= 50) game.changeCurrentColor(UnoColor.YELLOW);
			else if(rng <= 75) game.changeCurrentColor(UnoColor.GREEN);
			else game.changeCurrentColor(UnoColor.BLUE);
			
		}
		
		for(Card c : hand.getCards()) {
			if(game.cardMatch(c)) { 
				if(!game.allowDrawFourAnytime() && c.getUnoValue() == UnoValue.DRAWFOUR) {
					boolean allowed = true;
					for(Card cs : hand.getCards()) {
						if(cs.getUnoValue() != UnoValue.DRAWFOUR) {
							if(game.cardMatch(cs)) allowed = false;
						}
					}
					if(allowed) cardBeingPlayed = c;
				}
				else {
					cardBeingPlayed = c;
				}
				if(cardBeingPlayed != null) break;
			}
		}
		
		// If there was a playable card, play it
		if(cardBeingPlayed != null) {
			//Check to see if card matches discard pile
			if(game.cardMatch(cardBeingPlayed)) {
				hand.removeCard(cardBeingPlayed);
				game.addToDiscard(cardBeingPlayed);
				
				//If card was a wild, have AI pick a new color
				if(cardBeingPlayed.getUnoColor() == UnoColor.BLACK) {
					for(Card c : hand.getCards()) {
						if(c.getUnoColor() != UnoColor.BLACK) {
							game.changeCurrentColor(c.getUnoColor());
							break;
						}
					}
				}
				
				checkForCardEffects(cardBeingPlayed); //This checks the card that was played and modifies game if needed
				
			}
		}
		
		// If there was no playable card, draw
		if(cardBeingPlayed == null)  {
			hand.addCard(game.drawCard());
			
			for(Card c : hand.getCards()) {
				if(game.cardMatch(c)) cardBeingPlayed = c;
			}
			
			// If the card drew was playable, then play it.
			if(cardBeingPlayed != null) {
				//Check to see if card matches discard pile
				if(game.cardMatch(cardBeingPlayed)) {
					hand.removeCard(cardBeingPlayed);
					game.addToDiscard(cardBeingPlayed);
					
					//If card was a wild, have AI pick a new color
					if(cardBeingPlayed.getUnoColor() == UnoColor.BLACK) {
						for(Card c : hand.getCards()) {
							if(c.getUnoColor() != UnoColor.BLACK) {
								game.changeCurrentColor(c.getUnoColor());
								break;
							}
						}
					}
					
					checkForCardEffects(cardBeingPlayed); //This checks the card that was played and modifies game if needed
					
				}
			}
		}
		
		//Another odd hack, if draw to play is enabled, don't allow a complete turn until the AI has played a card.
		if(game.isDrawToPlayEnabled() && cardBeingPlayed == null) complete = false;
		else complete = true;
	}

	private void runNormalAITurn() {
		//Decide if they wanna call uno or challange

		calledUno = true;
		
		int rng = (int) (Math.random() * 100 + 1);

		if(rng <= 50) {
			game.challangeUno();
		}
		
		Card cardBeingPlayed = null;
		//In this case, the game has told the player that they can and must stack a draw two
		if(forceStackTwo) {
			
			rng = (int) (Math.random() * 100 + 1);

			if(rng <= 20) { //20% chance they will pass it
				game.drawPlayerCards(this, game.getCurrentStackAmount());
				game.resetCurrentStackAmount();
				complete = true;
			}
			else {
				for(Card c : hand.getCards()) {
					if(c.getUnoValue() == UnoValue.DRAWTWO) cardBeingPlayed = c;
				}
				hand.removeCard(cardBeingPlayed);
				game.addToDiscard(cardBeingPlayed);
				
				checkForCardEffects(cardBeingPlayed); //This checks the card that was played and modifies game if needed
				complete = true;
			}
			
			return;
		}
		
		//If this AI goes first, and a wild card was turned up, have them select a color.
		if(firstPriv && selectingColor) {
			firstPriv = false;
			selectingColor = false;
			for(Card c : hand.getCards()) {
				if(c.getUnoColor() != UnoColor.BLACK) {
					game.changeCurrentColor(c.getUnoColor());
					break;
				}
			}
		}
		
		//Match color first
		for(Card c : hand.getCards()) {
			
			if(c.getUnoColor() == game.currentColor) {
				if(game.cardMatch(c)) { 
					if(!game.allowDrawFourAnytime() && c.getUnoValue() == UnoValue.DRAWFOUR) {
						boolean allowed = true;
						for(Card cs : hand.getCards()) {
							if(cs.getUnoValue() != UnoValue.DRAWFOUR) {
								if(game.cardMatch(cs)) allowed = false;
							}
						}
						if(allowed) cardBeingPlayed = c;
					}
					else {
						cardBeingPlayed = c;
					}
					if(cardBeingPlayed != null) break;
				}	
			}
		}
		
		//Match number second
		for(Card c : hand.getCards()) {
			if(game.cardMatch(c)) { 
				if(!game.allowDrawFourAnytime() && c.getUnoValue() == UnoValue.DRAWFOUR) {
					boolean allowed = true;
					for(Card cs : hand.getCards()) {
						if(cs.getUnoValue() != UnoValue.DRAWFOUR) {
							if(game.cardMatch(cs)) allowed = false;
						}
					}
					if(allowed) cardBeingPlayed = c;
				}
				else {
					cardBeingPlayed = c;
				}
				if(cardBeingPlayed != null) break;
			}	
		}
		
		// If there was a playable card, play it
		if(cardBeingPlayed != null) {
			//Check to see if card matches discard pile
			if(game.cardMatch(cardBeingPlayed)) {
				hand.removeCard(cardBeingPlayed);
				game.addToDiscard(cardBeingPlayed);
				
				//If card was a wild, have AI pick a new color
				if(cardBeingPlayed.getUnoColor() == UnoColor.BLACK) {
					for(Card c : hand.getCards()) {
						if(c.getUnoColor() != UnoColor.BLACK) {
							game.changeCurrentColor(c.getUnoColor());
							break;
						}
					}
				}
				
				checkForCardEffects(cardBeingPlayed); //This checks the card that was played and modifies game if needed
				
			}
		}
		
		// If there was no playable card, draw
		if(cardBeingPlayed == null)  {
			hand.addCard(game.drawCard());
			
			for(Card c : hand.getCards()) {
				if(game.cardMatch(c)) cardBeingPlayed = c;
			}
			
			// If the card drew was playable, then play it.
			if(cardBeingPlayed != null) {
				//Check to see if card matches discard pile
				if(game.cardMatch(cardBeingPlayed)) {
					hand.removeCard(cardBeingPlayed);
					game.addToDiscard(cardBeingPlayed);
					
					//If card was a wild, have AI pick a new color
					if(cardBeingPlayed.getUnoColor() == UnoColor.BLACK) {
						for(Card c : hand.getCards()) {
							if(c.getUnoColor() != UnoColor.BLACK) {
								game.changeCurrentColor(c.getUnoColor());
								break;
							}
						}
					}
					
					checkForCardEffects(cardBeingPlayed); //This checks the card that was played and modifies game if needed
					
				}
			}
		}
		
		//Another odd hack, if draw to play is enabled, don't allow a complete turn until the AI has played a card.
		if(game.isDrawToPlayEnabled() && cardBeingPlayed == null) complete = false;
		else complete = true;
	}

	private void runHardAITurn() {
		//Always call / challenge on hard
		calledUno = true;
		game.challangeUno();
		
		Card cardBeingPlayed = null;
		//In this case, the game has told the player that they can and must stack a draw two
		if(forceStackTwo) {
			for(Card c : hand.getCards()) {
				if(c.getUnoValue() == UnoValue.DRAWTWO) cardBeingPlayed = c;
			}
			hand.removeCard(cardBeingPlayed);
			game.addToDiscard(cardBeingPlayed);
			
			checkForCardEffects(cardBeingPlayed); //This checks the card that was played and modifies game if needed
			complete = true;
			
			return;
		}
		
		//If this AI goes first, and a wild card was turned up, have them select a color.
		if(firstPriv && selectingColor) {
			firstPriv = false;
			selectingColor = false;
			
			int red = 0;
			int yellow = 0;
			int green = 0;
			int blue = 0;
			
			for(Card c : hand.getCards()) {
				if(c.getUnoColor() == UnoColor.RED) red++;
				else if(c.getUnoColor() == UnoColor.YELLOW) yellow++;
				else if(c.getUnoColor() == UnoColor.GREEN) green++;
				else if(c.getUnoColor() == UnoColor.BLUE) blue++;
			}
			
			if(red >= yellow && red >= green && red >= blue) game.changeCurrentColor(UnoColor.RED);
			else if(yellow >= red && yellow >= green && yellow >= blue) game.changeCurrentColor(UnoColor.YELLOW);
			else if(green >= red && green >= yellow && green >= blue) game.changeCurrentColor(UnoColor.GREEN);
			else if(blue >= red && blue >= yellow && blue >= green) game.changeCurrentColor(UnoColor.BLUE);
			else {
				System.out.println("Could not determine color, going with RED");
				game.changeCurrentColor(UnoColor.RED);
			}
		}
		
		//Match color first
		for(Card c : hand.getCards()) {
			
			if(c.getUnoColor() == game.currentColor) {
				if(game.cardMatch(c)) { 
					if(!game.allowDrawFourAnytime() && c.getUnoValue() == UnoValue.DRAWFOUR) {
						boolean allowed = true;
						for(Card cs : hand.getCards()) {
							if(cs.getUnoValue() != UnoValue.DRAWFOUR) {
								if(game.cardMatch(cs)) allowed = false;
							}
						}
						if(allowed) cardBeingPlayed = c;
					}
					else {
						cardBeingPlayed = c;
					}
					if(cardBeingPlayed != null) break;
				}	
			}
		}
		
		//Match number second
		for(Card c : hand.getCards()) {
			if(game.cardMatch(c)) { 
				if(!game.allowDrawFourAnytime() && c.getUnoValue() == UnoValue.DRAWFOUR) {
					boolean allowed = true;
					for(Card cs : hand.getCards()) {
						if(cs.getUnoValue() != UnoValue.DRAWFOUR) {
							if(game.cardMatch(cs)) allowed = false;
						}
					}
					if(allowed) cardBeingPlayed = c;
				}
				else {
					cardBeingPlayed = c;
				}
				if(cardBeingPlayed != null) break;
			}	
		}
		
		// If there was a playable card, play it
		if(cardBeingPlayed != null) {
			//Check to see if card matches discard pile
			if(game.cardMatch(cardBeingPlayed)) {
				hand.removeCard(cardBeingPlayed);
				game.addToDiscard(cardBeingPlayed);
				
				//If card was a wild, have AI pick a new color
				if(cardBeingPlayed.getUnoColor() == UnoColor.BLACK) {
					for(Card c : hand.getCards()) {
						if(c.getUnoColor() != UnoColor.BLACK) {
							game.changeCurrentColor(c.getUnoColor());
							break;
						}
					}
				}
				
				checkForCardEffects(cardBeingPlayed); //This checks the card that was played and modifies game if needed
				
			}
		}
		
		// If there was no playable card, draw
		if(cardBeingPlayed == null)  {
			hand.addCard(game.drawCard());
			
			for(Card c : hand.getCards()) {
				if(game.cardMatch(c)) cardBeingPlayed = c;
			}
			
			// If the card drew was playable, then play it.
			if(cardBeingPlayed != null) {
				//Check to see if card matches discard pile
				if(game.cardMatch(cardBeingPlayed)) {
					hand.removeCard(cardBeingPlayed);
					game.addToDiscard(cardBeingPlayed);
					
					//If card was a wild, have AI pick a new color
					if(cardBeingPlayed.getUnoColor() == UnoColor.BLACK) {
						for(Card c : hand.getCards()) {
							if(c.getUnoColor() != UnoColor.BLACK) {
								game.changeCurrentColor(c.getUnoColor());
								break;
							}
						}
					}
					
					checkForCardEffects(cardBeingPlayed); //This checks the card that was played and modifies game if needed
					
				}
			}
		}
		
		//Another odd hack, if draw to play is enabled, don't allow a complete turn until the AI has played a card.
		if(game.isDrawToPlayEnabled() && cardBeingPlayed == null) complete = false;
		else complete = true;
	}
	
	private void checkForCardEffects(Card c) {
		if(c.getUnoValue() == UnoValue.DRAWTWO) game.drawTwoNextPlayer();
		else if(c.getUnoValue() == UnoValue.DRAWFOUR) game.drawFourNextPlayer();
		else if(c.getUnoValue() == UnoValue.SKIP) game.skipNextPlayer();
		else if(c.getUnoValue() == UnoValue.REVERSE) game.reverseGame();
	}
	
}
