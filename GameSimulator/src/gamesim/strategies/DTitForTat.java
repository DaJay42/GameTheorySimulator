package gamesim.strategies;

import gamesim.GameStrategy;

/**An unfriendly variant of TFT. Plays D first round, then behaves like TFT.
 * @see TitForTat
 * @author DaJay42
 *
 */
public class DTitForTat implements GameStrategy {

	@Override
	public Strategy first() {
		return Strategy.D;
	}

	@Override
	public Strategy next(Strategy Answer) {
		return Answer;
	}

}
