package gamesim.strategies;

import java.util.concurrent.ThreadLocalRandom;

import gamesim.GameStrategy;

/**A RANDOM strategy. Has a 50%-50% chance to play either C or D each round.
 * @author DaJay42
 *
 */
public class FiftyFifty extends GameStrategy {

	@Override
	public Strategy first() {
		return ThreadLocalRandom.current().nextDouble() < 0.5 ? Strategy.C : Strategy.D;
	}

	@Override
	public Strategy next(Strategy Answer) {
		return ThreadLocalRandom.current().nextDouble() < 0.5 ? Strategy.C : Strategy.D;
	}

}
