package gamesim.gamerules;

import gamesim.GameRuleSet;

/**Player A would rather (+1) do activity 1, player B would rather (+1) do activity 2.
 * But more importantly, they want (+2) to do the same...
 *<br>C = I'll do what you wanted; D = I'll do what I wanted.
 * 
 *<p>T > S > P > R
 * @author DaJay42
 *
 */
public class BattleOfSpouses extends GameRuleSet {

	@Override
	public int R() {
		return 1;
	}

	@Override
	public int S() {
		return 3;
	}

	@Override
	public int T() {
		return 4;
	}

	@Override
	public int P() {
		return 2;
	}

}
