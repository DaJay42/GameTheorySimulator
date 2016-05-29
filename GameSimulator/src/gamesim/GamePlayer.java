package gamesim;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import gamesim.GameStrategy.Strategy;


/**Model of a player operating with a given strategy.
 *<br>Takes one Class(? extends GameStrategy) to instantiate its strategy from.
 *<br>Subclasses may also take further parameters, passed as String.
 * @author DaJay42
 *
 */
public abstract class GamePlayer {
	public int score;
	public String name;
	public GameStrategy myStrategy;
	private Class<? extends GameStrategy> strategyType;
	private String[] arg0 = {};
	
	@SuppressWarnings("unused")
	private GamePlayer(){}
	
	public GamePlayer(Class<? extends GameStrategy> s, String...args) throws InstantiationException, IllegalAccessException{
		strategyType = s;
		name = strategyType.getSimpleName();
		myStrategy = strategyType.newInstance();		
		score = 0;
		arg0 = args;
	}
	
	public final void reset() throws InstantiationException, IllegalAccessException{
		score = 0;
		myStrategy = strategyType.newInstance();
	}
	
	public final GamePlayer duplicate() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		GamePlayer p = null;
		Object[] o = new Object[]{strategyType, arg0};
		for(Constructor<?> c : this.getClass().getConstructors()){
			if(c.getParameterTypes().length == 2 && c.getParameterTypes()[1] == String[].class){
				p = (GamePlayer) c.newInstance(o);
				break;
			}
		}
		if(p == null){
			throw new InstantiationException(String.format("No suitable constructor found for class %s with args {%s, %s}", getClass().getName(), o[0].toString(), o[1].toString()));
		}
		return p;
	}
	
	public abstract Strategy first();
	
	public abstract Strategy next(Strategy answer);
	
	public abstract String getName();
}
