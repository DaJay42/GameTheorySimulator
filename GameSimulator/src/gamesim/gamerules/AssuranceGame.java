package gamesim.gamerules;

import gamesim.GameRuleSet;

/**A.k.a "deer hunt".
 *
 *<p>R > T > P > S
 * @author DaJay42
 *
 */
public class AssuranceGame extends GameRuleSet {

	@Override
	public int R() {
		return 4;
	}

	@Override
	public int S() {
		return 1;
	}

	@Override
	public int T() {
		return 3;
	}

	@Override
	public int P() {
		return 2;
	}

}
