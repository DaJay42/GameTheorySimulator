package gamesim.players;

import gamesim.GamePlayer;
import gamesim.GameStrategy;
import gamesim.GameStrategy.Strategy;

/**A player that will always play the correct choice, according to its strategy.
 *
 *<p>Takes no String parameters; all arguments are ignored
 * @author DaJay42
 *
 */
public class PlayerReliable extends GamePlayer {

	public PlayerReliable(Class<? extends GameStrategy> s, String... args)
			throws InstantiationException, IllegalAccessException {
		super(s, args);
	}

	@Override
	public String getName() {
		return PlayerReliable.class.getSimpleName();
	}
	@Override
	public Strategy first(){
		return myStrategy.first();
	}

	@Override
	public Strategy next(Strategy answer){
		return myStrategy.next(answer);
	}
}
