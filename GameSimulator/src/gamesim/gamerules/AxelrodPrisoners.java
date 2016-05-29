package gamesim.gamerules;

import gamesim.GameRuleSet;

/**A Prisoners' Dilemma GameType with the pay-outs as seen in Axelrod's tournaments.
 *
 *<p>T > R > P > S and 2*R = T + P
 * @author DaJay42
 *
 */
public class AxelrodPrisoners extends GameRuleSet {

	@Override
	public int R() {
		return 3;
	}

	@Override
	public int S() {
		return 0;
	}

	@Override
	public int T() {
		return 5;
	}

	@Override
	public int P() {
		return 1;
	}

}
