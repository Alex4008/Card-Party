package main;

public enum Msg {
	SOMEONE_UNO(" has called Uno!"),
	YOU_UNO("You have called Uno!"),
	SOMEONE_SKIPPED(" has been skipped!"),
	YOU_SKIPPED("You have been skipped!"),
	REVERED("The game has been reversed!"),
	DRAW_TWO("You must draw 2 cards!"),
	DRAW_FOUR("You must draw 4 cards!"),
	SOMEONE_WIN(" has won the game!"),
	YOU_WIN("You have won the game!"),
	CHALLANGE_DRAW("Your opponent played a draw 4, click to challange!");
	
	public final String msg;
	
	Msg(String msg) {
		this.msg = msg;
	}
	
}
