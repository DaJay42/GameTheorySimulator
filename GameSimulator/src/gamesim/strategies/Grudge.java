package gamesim.strategies;

import gamesim.GameStrategy;

/**A Strategy that returns whichever answer it has heard more often. Friendly.
 * @author DaJay42
 *
 */
public class Grudge extends GameStrategy {

	int balance;
	
	@Override
	public Strategy first() {
		return Strategy.C;
	}

	@Override
	public Strategy next(Strategy Answer) {
		if(Answer == Strategy.C)
			balance++;
		else
			balance--;
		return (balance < 0) ? Strategy.D : Strategy.C;
	}

}
