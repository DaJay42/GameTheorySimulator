package gamesim.strategies;

import gamesim.GameStrategy;

/**The Tit-for-Two-Tat Strategy, as by J. Maynard Smith, seen in Axelrod's second Tournament.
 * Starts with C, and only responds with D if it receives two Ds in a row.
 * @author DaJay42
 *
 */
public class TitForTwoTat implements GameStrategy {

	boolean angry = false;
	@Override
	public Strategy first() {
		return Strategy.C;
	}

	@Override
	public Strategy next(Strategy Answer) {
		Strategy strat = angry ? Answer : Strategy.C;
		
		angry = (Answer == Strategy.D);
		
		return strat;
	}

}
