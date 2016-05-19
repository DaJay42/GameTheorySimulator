package gamesim.strategies;

import gamesim.GameStrategy;

/**A strategy by Johann Joss, as seen in Axelrod's first tournament.
 * Essentially TFT with 10% fault chance (plays D instead of C).
 * @author DaJay42
 *
 */
public class Joss implements GameStrategy {

	@Override
	public Strategy first() {
		return Strategy.C;
	}

	@Override
	public Strategy next(Strategy Answer) {
		return (Answer == Strategy.D || Math.random() < 0.1) ? Strategy.D : Strategy.C;
	}

}
