package gamesim.strategies;

import gamesim.GameStrategy;

/**A opportunistic variant of TFT, by me, 2016. Plays D until it receives D.
 *<br>If so, plays C twice ("apology") then switches to TFT.
 *<br>Surprisingly does quite well in single tournaments and tournaments with faulty players.
 * @see TitForThat
 * @author DaJay42
 *
 */
public class Panic implements GameStrategy {
	
	boolean alarmed;
	int apology = 2;

	@Override
	public Strategy first() {
		return Strategy.D;
	}

	@Override
	public Strategy next(Strategy Answer) {
		if(Answer == Strategy.D)
			alarmed = true;
		
		if(alarmed && apology > 0){
			apology--;
			return Strategy.C;
		}
		
		return alarmed ? Answer : Strategy.D;
	}

}
