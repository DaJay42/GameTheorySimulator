package gamesim.strategies;

import gamesim.GameStrategy;

/**A RANDOM strategy. Has a 50%-50% chance to play either C or D each round.
 * @author DaJay42
 *
 */
public class FiftyFifty implements GameStrategy {

	@Override
	public Strategy first() {
		return Math.random() < 0.5 ? Strategy.C : Strategy.D;
	}

	@Override
	public Strategy next(Strategy Answer) {
		return Math.random() < 0.5 ? Strategy.C : Strategy.D;
	}

}
