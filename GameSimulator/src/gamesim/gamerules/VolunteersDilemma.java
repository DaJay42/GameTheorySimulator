package gamesim.gamerules;

import gamesim.GameRuleSet;

/**A GameType to simulate the production of public goods.
 *
 *<p>T > R = S > P
 * @author DaJay42
 *
 */
public class VolunteersDilemma extends GameRuleSet {

	@Override
	public int R() {
		return 2;
	}

	@Override
	public int S() {
		return 2;
	}

	@Override
	public int T() {
		return 3;
	}

	@Override
	public int P() {
		return 0;
	}

}
