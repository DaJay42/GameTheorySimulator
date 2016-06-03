package gamesim.strategies;

import gamesim.GameStrategy;

/**A Friedman-like Strategy, which forgives the first three defections.
 *<br>The fourth defection, however, is rewarded with eternal damnation.
 * @author DaJay42
 * @see Friedman
 */
public class Buddha implements GameStrategy {

	int slaps = 0;
	
	@Override
	public Strategy first() {
		return Strategy.C;
	}

	@Override
	public Strategy next(Strategy Answer) {
		if(Answer == Strategy.D)
			slaps++;
		return (slaps <= 3) ? Strategy.C : Strategy.D;
	}

}
