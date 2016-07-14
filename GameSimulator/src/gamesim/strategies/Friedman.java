package gamesim.strategies;

import gamesim.GameStrategy;

/**A strategy by James W. Friedman, as seen in Axelrod's first tournament.
 *<br>Plays C until it receives D, then always plays D.
 *<br>("Eternal Damnation"; "Trigger-Strategy").
 * 
 * <p>By itself, does quite badly, but its presence alone changes the playing field by
 * severely disadvantaging unfriendly strategies.
 * @author DaJay42
 */
public class Friedman extends GameStrategy {

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
