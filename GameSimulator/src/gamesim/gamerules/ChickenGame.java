package gamesim.gamerules;

import gamesim.GameRuleSet;

/**Mutually assured destruction.
 * 
 *<p>T > R > S > P
 * @author DaJay42
 *
 */
public class ChickenGame extends GameRuleSet {

	@Override
	public int R() {
		return 3;
	}

	@Override
	public int S() {
		return 2;
	}

	@Override
	public int T() {
		return 4;
	}

	@Override
	public int P() {
		return 0;
	}

}
