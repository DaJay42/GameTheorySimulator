package gamesim.strategies;

import gamesim.GameStrategy;

/**A trivial strategy that always plays D. Did not play in Axelrod's tournaments.
 * @author DaJay42
 *
 */
public class AlwaysD extends GameStrategy {

	@Override
	public Strategy first() {
		return Strategy.D;
	}

	@Override
	public Strategy next(Strategy Answer) {
		return Strategy.D;
	}

}
