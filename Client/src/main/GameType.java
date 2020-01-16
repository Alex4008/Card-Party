package main;

public enum GameType {
	WAR(1),
	UNO(2),
	EUCHRE(3),
	NULL(0);
	
	public final int num;
	
	GameType(int value) {
		this.num = value;
	}
	
	
	public static GameType stringToGameType(String value) {
		if(value == null) return GameType.NULL;
		else if(value.equalsIgnoreCase("WAR")) return GameType.WAR;
		else if(value.equalsIgnoreCase("UNO")) return GameType.UNO;
		else if(value.equalsIgnoreCase("EUCHRE")) return GameType.EUCHRE;
		else return GameType.NULL;
	}
	
	public static GameType integerToGameType(int value) {
		if(value == 0) return GameType.NULL;
		else if(value == 1) return GameType.WAR;
		else if(value == 2) return GameType.UNO;
		else if(value == 3) return GameType.EUCHRE;
		else return GameType.NULL;
	}
}
