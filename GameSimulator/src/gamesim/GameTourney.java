package gamesim;

import gamesim.util.ScoreTuple;

import java.util.ArrayList;
import java.util.List;

/**Model of the Tournament players will play in.
 *<br>Takes String... arguments, the semantics of which are to be determined by subclasses.
 * @author DaJay42
 *
 */
public abstract class GameTourney extends GameEntity{
	public ArrayList<GamePlayer> players;
	public GameRuleSet ruleset;
	protected GameThread[] workers;
	
	
	protected GameTourney(String[] args) {
		super(args);
	}

	public void resetPlayers() throws InstantiationException{
		for(GamePlayer player : players){
			if(player != null)
				player.reset();
		}
	}
	
	public abstract boolean isFinished();
	
	public abstract int getRoundsPerGame();
	
	public abstract String getRoundsDescriptor();
	
	public abstract int getNumPlayers();
	
	public abstract GameThread[] getNextMatchUp() throws InstantiationException;

	public abstract void evaluate(GameThread[] workers);

	public abstract void setup() throws InstantiationException;
	
	public abstract String[][] printResults();
	
	public abstract List<ScoreTuple<GamePlayer>> getRanking();
}
