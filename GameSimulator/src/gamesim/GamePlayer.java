package gamesim;


/**
 * @author DaJay42
 *
 */
public class GamePlayer {
	public int score;
	public String name;
	public GameStrategy myStrategy;
	private Class<? extends GameStrategy> strategyType;
	
	public GamePlayer(GameStrategy aStrategy) throws InstantiationException, IllegalAccessException{
		strategyType = aStrategy.getClass();
		name = strategyType.getSimpleName();
		myStrategy = strategyType.newInstance();		
		score = 0;
	}
	
	public void reset() throws InstantiationException, IllegalAccessException{
		score = 0;
		myStrategy = strategyType.newInstance();
	}
	
}
