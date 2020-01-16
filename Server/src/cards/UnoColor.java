package cards;

public enum UnoColor {
	NULL(0),
	RED(1),
	YELLOW(2),
	GREEN(3),
	BLUE(4),
	BLACK(5);
	
	public final int num;
	
	private UnoColor(int num) {
		this.num = num;
	}
	
	public UnoColor nextValue() {
		if(num == 1) return YELLOW;
		else if(num == 2) return GREEN;
		else if(num == 3) return BLUE;
		else if(num == 4) return BLACK;
		else return RED;
	}
	
	public UnoColor prevValue() {
		if(num == 1) return BLACK;
		else if(num == 2) return RED;
		else if(num == 3) return YELLOW;
		else if(num == 4) return GREEN;
		else return BLUE;
	}
	
	public static UnoColor intToUnoColor(int value) {
		switch (value) {
		case 1:
			return UnoColor.RED;
			
		case 2:
			return UnoColor.YELLOW;
			
		case 3:
			return UnoColor.GREEN;
			
		case 4: 
			return UnoColor.BLUE;
			
		case 5:
			return UnoColor.BLACK;
			
		default:
			return UnoColor.NULL;
		}
	}
}
