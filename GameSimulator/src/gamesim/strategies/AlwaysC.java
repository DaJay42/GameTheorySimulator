package gamesim.strategies;

import gamesim.GameStrategy;

/**A trivial strategy that always plays C. Did not play in Axelrod's tournaments.
 * @author DaJay42
 *
 */
public class AlwaysC implements GameStrategy {

	@Override
	public Strategy first() {
		return Strategy.C;
	}

	@Override
	public Strategy next(Strategy Answer) {
		return Strategy.C;
	}

}
