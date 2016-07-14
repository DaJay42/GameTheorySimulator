package gamesim;

import gamesim.GameStrategy.Strategy;


/**Model of a player operating with a given strategy.
 *<br>Takes one Class(? extends GameStrategy) to instantiate its strategy from.
 *<br>Subclasses may also take further parameters, passed as String.
 * @author DaJay42
 *
 */
public abstract class GamePlayer extends GameEntity{
	public int score;
	public String name;
	public GameStrategy myStrategy;
	private Class<? extends GameStrategy> strategyType;
	
	
	public GamePlayer(Class<? extends GameStrategy> s, String...args) throws InstantiationException{
		super(args);
		strategyType = s;
		name = strategyType.getSimpleName();
		myStrategy = (GameStrategy) GameEntityReflector.create(strategyType);		
		score = 0;
	}
	
	public final void reset() throws InstantiationException{
		score = 0;
		myStrategy = (GameStrategy) GameEntityReflector.create(strategyType);	
	}
	
	public final GamePlayer duplicate() throws InstantiationException{
		return GameEntityReflector.createPlayer(this.getClass(), strategyType, arg0);
	}
	
	public abstract Strategy first();
	
	public abstract Strategy next(Strategy answer);
	
	public abstract String getName();
}
