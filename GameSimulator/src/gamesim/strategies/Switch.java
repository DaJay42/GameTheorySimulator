package gamesim.strategies;

import gamesim.GameStrategy;

/**A relatively simple strategy devised by me, 2016.
 *<br>Similar to Pavlov, but does not switch back to C on subsequent betrayals.
 *<br>Sometimes performs okay.
 * @author DaJay42
 *
 */
public class Switch extends GameStrategy {
	
	Strategy last = Strategy.C;
	boolean cautious = false;

	@Override
	public Strategy first() {
		return Strategy.D;
	}

	@Override
	public Strategy next(Strategy Answer) {
		if(Answer == Strategy.D ){
			if(last == Strategy.D){
				cautious = false;
			}else{
				cautious = !cautious;
			}
		}
		last = Answer;
		
		return cautious ? Strategy.C : Strategy.D;
	}

}
