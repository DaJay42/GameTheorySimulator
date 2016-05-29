package gamesim.gamerules;

import gamesim.GameRuleSet;

/**"Both players choose a number N from 1 to 9. If both chose the same, they get 1 million plus N.
 * If they didn't choose the same, the one with less gets 1 million, the other 0."
 *<br>C = chose 9; D = chose 1. (other possibilities irrelevant.)
 *
 *<p>R > P > T >> S
 * @author DaJay42
 *
 */
public class MillionDollarsPlusN extends GameRuleSet {

	@Override
	public int R() {
		return 12;
	}

	@Override
	public int S() {
		return 0;
	}

	@Override
	public int T() {
		return 10;
	}

	@Override
	public int P() {
		return 11;
	}

}
