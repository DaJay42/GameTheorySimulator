package gamesim;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * @author DaJay42
 *
 */
public abstract class GameTourney {
	public ArrayList<GamePlayer> players;
	public GameRuleSet ruleset;
	protected GameThread[] workers;
	
	@SuppressWarnings("unused")
	private GameTourney() {
		/*skip*/
	}
	protected GameTourney(String[] args) {
		/*skip*/
	}
	
	public void resetPlayers() throws InstantiationException, IllegalAccessException{
		for(GamePlayer player : players){
			if(player != null)
				player.reset();
		}
	}
	
	public abstract boolean isFinished();
	
	public abstract int getRoundsPerGame();
	
	public abstract String getRoundsDescriptor();
	
	public abstract int getNumPlayers();
	
	public abstract GameThread[] getNextMatchUp() throws IllegalArgumentException, InvocationTargetException, InstantiationException, IllegalAccessException;

	public abstract void evaluate(GameThread[] workers);

	public abstract void setup();
	
	public abstract String[][] printResults();
}
