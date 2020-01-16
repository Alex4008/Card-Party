package cards;

public enum Suit {
	NULL(0),
	CLUBS(1),
	HEARTS(2),
	SPADES(3),
	DIAMONDS(4);
	
	public final int num;
	
	private Suit(int num) {
		this.num = num;
	}
	
	public Suit nextValue() {
		if(num == 1) return HEARTS;
		else if(num == 2) return SPADES;
		else if(num == 3) return DIAMONDS;
		else return CLUBS;
	}
	
	public Suit prevValue() {
		if(num == 1) return DIAMONDS;
		else if(num == 2) return CLUBS;
		else if(num == 3) return HEARTS;
		else return SPADES;
	}
	
	public static Suit intToSuit(int value) {
		switch (value) {
		case 1:
			return Suit.CLUBS;
			
		case 2:
			return Suit.HEARTS;
			
		case 3:
			return Suit.SPADES;
			
		case 4: 
			return Suit.DIAMONDS;
			
		default:
			return Suit.NULL;
		}
	}
}
