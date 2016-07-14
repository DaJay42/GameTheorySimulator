package gamesim.strategies;

import gamesim.GameStrategy;

/**A trivial strategy that alternates between C and D.
 * Did not play in Axelrod's tournaments.
 *<br>While by itself not overly successful,
 * its presence can have tremendous impact on the success of other strategies.
 * @author DaJay42
 *
 */
public class Alternate extends GameStrategy {

	boolean flip = true;
	
	@Override
	public Strategy first() {
		return Strategy.C;
	}

	@Override
	public Strategy next(Strategy Answer) {
		flip = !flip;
		return flip ? Strategy.C : Strategy.D;
	}

}
