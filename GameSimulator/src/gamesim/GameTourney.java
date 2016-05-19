package gamesim;

import java.util.ArrayList;

/**
 * @author DaJay42
 *
 */
public abstract class GameTourney {
	public ArrayList<GamePlayer> players;
	public GameType ruleset;
	protected GameThread[] workers;
	

	
	void resetPlayers() throws InstantiationException, IllegalAccessException{
		for(GamePlayer player : players){
			if(player != null)
				player.reset();
		}
	}
	
	public abstract boolean isFinished();
	
	public abstract int getRoundsPerGame();
	
	public abstract String getRoundsDescriptor();
	
	public abstract int getNumPlayers();
	
	public abstract GameThread[] getNextMatchUp();

	public abstract void evaluate(GameThread[] workers);

	public abstract void setup();
	
	public abstract String[] printResults();
}
