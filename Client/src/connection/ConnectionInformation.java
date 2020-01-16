package connection;

import java.io.Serializable;
import java.util.ArrayList;

import main.GameType;
import main.Main;

public class ConnectionInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -918403668745916724L;
	double version;
	String username;
	String status;
	ArrayList<String> usernames;
	int maxPlayers;
	GameType setGame;
	
	public ConnectionInformation(Main m, String username) {
		this.version = m.getVersion();
		this.username = username;
		status = "UNDEFINED";
		usernames = new ArrayList<String>();
	}
	
	public double getVersion() {
		return version;
	}
	
	public void setVersion(double version) {
		this.version = version;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setUsernames(ArrayList<String> usernames) {
		this.usernames = usernames;
	}
	
	public ArrayList<String> getUsernames() {
		return usernames;
	}
	
	public void setSetGame(GameType setGame) {
		this.setGame = setGame;
	}
	
	public GameType getSetGame() {
		return setGame;
	}
	
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
}
