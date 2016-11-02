/**
 * 
 */
package gamesim.players;

import java.util.concurrent.ThreadLocalRandom;

import gamesim.GameMain;
import gamesim.GamePlayer;
import gamesim.GameStrategy;
import gamesim.GameStrategy.Strategy;

/**A player that has a given chance to randomly (forgive and) forget
 *<br>everything it learned and restart its strategy from the first() move.
 *
 *<p>Takes one floating point number as first String parameter.
 *<br>All subsequent arguments are ignored.
 * 
 * @param lapseChance : double. Determines the likelihood of "accidents".
 * @author DaJay42
 *
 */
public class PlayerAmnesiac extends GamePlayer {

	double lapseChance = 0.005;
	
	public PlayerAmnesiac(Class<? extends GameStrategy> s, String... args)
			throws InstantiationException, IllegalAccessException {
		super(s, args);

		if(args != null && args.length > 0)
			lapseChance = Double.parseDouble(args[0]);
		
		lapseChance = Double.max(0, Double.min(1, lapseChance));
	}
	/**
	 * @see gamesim.GamePlayer#first()
	 */
	@Override
	public Strategy first() {
		return myStrategy.first();
	}

	/**
	 * @see gamesim.GamePlayer#next(gamesim.GameStrategy.Strategy)
	 */
	@Override
	public Strategy next(Strategy answer) {
		if (ThreadLocalRandom.current().nextDouble() < lapseChance){
			int tempscore = score;
			try {
				reset();
			} catch (InstantiationException e) {
				GameMain.printException(e);
			}
			score = tempscore;
			return myStrategy.first();
		}else{
			return myStrategy.next(answer);
		}
	}

	/**
	 * @see gamesim.GamePlayer#getName()
	 */
	@Override
	public String getName() {
		return getClass().getSimpleName() + " ("+100*lapseChance+"%% lapse chance)";
	}

}
