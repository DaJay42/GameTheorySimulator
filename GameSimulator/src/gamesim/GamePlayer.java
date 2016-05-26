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
	
	public GamePlayer(Class<? extends GameStrategy> s) throws InstantiationException, IllegalAccessException{
		strategyType = s;
		name = strategyType.getSimpleName();
		myStrategy = strategyType.newInstance();		
		score = 0;
	}
	
	public void reset() throws InstantiationException, IllegalAccessException{
		score = 0;
		myStrategy = strategyType.newInstance();
	}
	
	public GamePlayer duplicate() throws InstantiationException, IllegalAccessException{
		GamePlayer p = new GamePlayer(strategyType);
		return p;
	}
	
}
