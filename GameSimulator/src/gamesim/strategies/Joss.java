package gamesim.strategies;

import java.util.concurrent.ThreadLocalRandom;

import gamesim.GameStrategy;

/**A strategy by Johann Joss, as seen in Axelrod's first tournament.
 *<br>Essentially TFT with 10% fault chance (plays D instead of C).
 * @author DaJay42
 *
 */
public class Joss extends GameStrategy {

	@Override
	public Strategy first() {
		return Strategy.C;
	}

	@Override
	public Strategy next(Strategy Answer) {
		return (Answer == Strategy.D || ThreadLocalRandom.current().nextDouble() < 0.1) ? Strategy.D : Strategy.C;
	}

}
