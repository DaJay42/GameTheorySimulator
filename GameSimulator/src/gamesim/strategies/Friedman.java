package gamesim.strategies;

import gamesim.GameStrategy;

/**A strategy by James W. Friedman, as seen in Axelrod's first tournament.
 * Plays C until it receives D, then always plays D
 * ("Eternal Damnation"; "Trigger-Strategy").
 * 
 * @author DaJay42
 */
public class Friedman implements GameStrategy {

	boolean triggered;
	
	@Override
	public Strategy first() {
		return Strategy.C;
	}

	@Override
	public Strategy next(Strategy Answer) {
		if(Answer == Strategy.D)
			triggered = true;
		return triggered ? Strategy.D : Strategy.C;
	}

}
