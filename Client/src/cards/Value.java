package cards;

public enum Value {
	NULL(0),
	ACE(1),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7),
	EIGHT(8),
	NINE(9),
	TEN(10),
	JACK(11),
	QUEEN(12),
	KING(13);
	
	public final int num;
	
	private Value(int num) {
		this.num = num;
	}
	
	public Value nextValue() {
		if(num == 1) return TWO;
		else if(num == 2) return THREE;
		else if(num == 3) return FOUR;
		else if(num == 4) return FIVE;
		else if(num == 5) return SIX;
		else if(num == 6) return SEVEN;
		else if(num == 7) return EIGHT;
		else if(num == 8) return NINE;
		else if(num == 9) return TEN;
		else if(num == 10) return JACK;
		else if(num == 11) return QUEEN;
		else if(num == 12) return KING;
		else return ACE;
	}
	
	public Value prevValue() {
		if(num == 1) return KING;
		else if(num == 2) return ACE;
		else if(num == 3) return TWO;
		else if(num == 4) return THREE;
		else if(num == 5) return FOUR;
		else if(num == 6) return FIVE;
		else if(num == 7) return SIX;
		else if(num == 8) return SEVEN;
		else if(num == 9) return EIGHT;
		else if(num == 10) return NINE;
		else if(num == 11) return TEN;
		else if(num == 12) return JACK;
		else return QUEEN;
	}
	
	public static Value intToValue(int num) {
		if(num == 1) return ACE;
		else if(num == 2) return TWO;
		else if(num == 3) return THREE;
		else if(num == 4) return FOUR;
		else if(num == 5) return FIVE;
		else if(num == 6) return SIX;
		else if(num == 7) return SEVEN;
		else if(num == 8) return EIGHT;
		else if(num == 9) return NINE;
		else if(num == 10) return TEN;
		else if(num == 11) return JACK;
		else if(num == 12) return QUEEN;
		else if(num == 13) return KING;
		else return NULL;
	}
}
