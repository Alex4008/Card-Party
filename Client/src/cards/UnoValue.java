package cards;

public enum UnoValue {
	NULL(-1),
	ZERO(0),
	ONE(1),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7),
	EIGHT(8),
	NINE(9),
	SKIP(10),
	REVERSE(11),
	DRAWTWO(12),
	WILD(13),
	DRAWFOUR(14);
	
	public final int num;
	
	private UnoValue(int num) {
		this.num = num;
	}

	public UnoValue nextValue() {
		if (num == 0) return ONE;
		else if(num == 1) return TWO;
		else if(num == 2) return THREE;
		else if(num == 3) return FOUR;
		else if(num == 4) return FIVE;
		else if(num == 5) return SIX;
		else if(num == 6) return SEVEN;
		else if(num == 7) return EIGHT;
		else if(num == 8) return NINE;
		else if(num == 9) return SKIP;
		else if(num == 10) return REVERSE;
		else if(num == 11) return DRAWTWO;
		else return ZERO;
	}
	
	public UnoValue prevValue() {
		if (num == 0) return DRAWTWO;
		else if(num == 1) return ZERO;
		else if(num == 2) return ONE;
		else if(num == 3) return TWO;
		else if(num == 4) return THREE;
		else if(num == 5) return FOUR;
		else if(num == 6) return FIVE;
		else if(num == 7) return SIX;
		else if(num == 8) return SEVEN;
		else if(num == 9) return EIGHT;
		else if(num == 10) return NINE;
		else if(num == 11) return SKIP;
		else return REVERSE;
	}
	
	public static UnoValue intToValue(int num) {
		if(num == 0) return ZERO;
		else if(num == 1) return ONE;
		else if(num == 2) return TWO;
		else if(num == 3) return THREE;
		else if(num == 4) return FOUR;
		else if(num == 5) return FIVE;
		else if(num == 6) return SIX;
		else if(num == 7) return SEVEN;
		else if(num == 8) return EIGHT;
		else if(num == 9) return NINE;
		else if(num == 10) return SKIP;
		else if(num == 11) return REVERSE;
		else if(num == 12) return DRAWTWO;
		else if(num == 13) return WILD;
		else if(num == 14) return DRAWFOUR;
		else return NULL;
	}
}
