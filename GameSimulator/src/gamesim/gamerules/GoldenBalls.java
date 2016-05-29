package gamesim.gamerules;

import gamesim.GameRuleSet;

/**A GameType modeled after Golden Balls.
 * 
 *<p>T > R > P = S
 * @author DaJay42
 *
 */
public class GoldenBalls extends GameRuleSet {

	@Override
	public int R() {
		return 1;
	}

	@Override
	public int S() {
		return 0;
	}

	@Override
	public int T() {
		return 2;
	}

	@Override
	public int P() {
		return 0;
	}

}
