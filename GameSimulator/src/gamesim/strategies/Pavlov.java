package gamesim.strategies;

import gamesim.GameStrategy;

/**A Win-Stay-Lose-Shift Strategy. See Nowak and Sigmund.
 * @author DaJay42
 *
 */
public class Pavlov implements GameStrategy {

	boolean flip = false;
	
	@Override
	public Strategy first() {
		return Strategy.C;
	}

	@Override
	public Strategy next(Strategy Answer) {
		if(Answer == Strategy.D)
			flip = !flip;
		return flip ? Strategy.D : Strategy.C;
	}

}
